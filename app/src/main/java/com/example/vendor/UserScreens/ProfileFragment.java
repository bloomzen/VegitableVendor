package com.example.vendor.UserScreens;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    ImageButton img_back;
    CircleImageView mProfile;
    RelativeLayout rl_myProfile, rl_changePassword, rl_viewOrders, rl_aboutUS, btnLogout;



    static int Gallery_pick = 1;
    Bitmap thumb_bitmap;

    byte[] thumb_bite ;
    Context context;

  String imageUrl;
    StorageReference mStoragePath;
    FirebaseAuth mAuth;
    DatabaseReference mUserRef;
    DatabaseReference mImagRef;

    ProgressDialog mProgress;
    String userId,userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            SharedPreferences sh =this.getActivity().getSharedPreferences("MyLogin",MODE_PRIVATE);
            userId = sh.getString("userId","");
            userType = sh.getString("userType","");

            //Toast.makeText(context, userId, Toast.LENGTH_SHORT).show();

            img_back = view.findViewById(R.id.btn_profile_back);
            mProfile = view.findViewById(R.id.img_profile_user);
            rl_myProfile = view.findViewById(R.id.myProfile);
            rl_aboutUS = view.findViewById(R.id.aboutUS);
            rl_changePassword = view.findViewById(R.id.changePassword);
            rl_viewOrders = view.findViewById(R.id.viewOrders);



            mAuth = FirebaseAuth.getInstance();
            mStoragePath = FirebaseStorage.getInstance().getReference();

            if(mAuth.getUid() != null){
                mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
            }else {
                mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            }


//        mProgress = new ProgressDialog(getContext());
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.setTitle("Please wait");


            btnLogout = view.findViewById(R.id.logout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    getContext().getSharedPreferences("MyLogin", MODE_PRIVATE).edit().clear().apply();
                    Intent i = new Intent(getActivity(),
                            Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
            });



            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try{
                        final String image = snapshot.child("image").getValue().toString();
                        //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                        Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_person_blue).into(mProfile, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(image).placeholder(R.drawable.ic_person_blue).into(mProfile);
                            }
                        });
                    }catch (Exception e){
                        mProfile.setImageResource(R.drawable.ic_person_blue);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            rl_changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangePasswordFragment ldf = new ChangePasswordFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });



            rl_myProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyProfileFragment ldf = new MyProfileFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            rl_viewOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewOrdersFragment ldf = new ViewOrdersFragment ();

                    FragmentTransaction transection=getFragmentManager().beginTransaction();



                    transection.replace(R.id.fragment_container, ldf);
                    transection.addToBackStack(null);
                    transection.commit();
                }
            });

            mProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);




                    //startActivityForResult(galleryIntent,Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);

                    //startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);

        //image cropper


        try{
            if(requestCode == Gallery_pick && resultCode == RESULT_OK){

                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1,1)
                        .start(getContext(),this);


            }
            Toast.makeText(Vendor.getAppContext(),String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) , Toast.LENGTH_SHORT).show();
            if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result =CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){


                    Uri resultUri = result.getUri();

                    File thum_file = new File(resultUri.getPath());

                    Toast.makeText(Vendor.getAppContext(), "cropImage2", Toast.LENGTH_SHORT).show();





                    thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(getContext()).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(thum_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_bite = baos.toByteArray();

                    Picasso.with(getContext()).load(thum_file).placeholder(R.drawable.ic_person_profile_blue).into(mProfile);


                    final StorageReference mFilePath =  mStoragePath.child("profile_user").child(mAuth.getUid()+".jpg");

                    UploadTask uploadTask = mFilePath.putBytes(thumb_bite);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mFilePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    try {
                                        if(task.isSuccessful()){
                                            imageUrl = task.getResult().toString();
                                            HashMap<String,Object> userImage = new HashMap<>();
                                            userImage.put("image",imageUrl);

                                            mUserRef.updateChildren(userImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(Vendor.getAppContext(), "Image Updated", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }catch (Exception e){
                                        Log.v("imageError",e.getMessage());
                                    }

                                }
                            });
                        }
                    });




                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }

}
