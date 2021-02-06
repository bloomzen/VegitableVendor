package com.example.vendor.AdminScreen;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.R;
import com.example.vendor.Vendor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHotelFragment extends Fragment {


    public AddHotelFragment() {
        // Required empty public constructor
    }

    CircleImageView img_hotel;
    EditText edt_hotelName, edt_hotelAddress;
    ImageView img_add_member;
    TextView txt_memberType;
    RelativeLayout rl_add_hotel;

    public static final String[] AddHotelType = {
            "Gold",
            "Silver",
            "Bronze",
            "Fixed"
    };


    static int Gallery_pick = 1;
    Bitmap thumb_bitmap;

    byte[] thumb_bite = {} ;
    Context context;

    String imageUrl = "";
    StorageReference mStoragePath;

    ProgressDialog mProgress;

    String hotelName, hotelMember, hotelAddress, hotelMemberType;

    DatabaseReference mHotelRef;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_hotel, container, false);


        try{
            img_hotel = view.findViewById(R.id.img_add_hotels);
            edt_hotelName = view.findViewById(R.id.edt_addHotelName);
            edt_hotelAddress = view.findViewById(R.id.edt_addHotelAddress);
            img_add_member = view.findViewById(R.id.img_add_hotel_member);
            txt_memberType = view.findViewById(R.id.text_hotelMember);
            rl_add_hotel = view.findViewById(R.id.btn_add_hotel);

            mProgress = new ProgressDialog(getActivity());
            mProgress.setTitle("Please wait");
            mProgress.setCanceledOnTouchOutside(false);

            mStoragePath = FirebaseStorage.getInstance().getReference();
            mHotelRef = FirebaseDatabase.getInstance().getReference().child("HotelName");

            rl_add_hotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkData();

                }
            });

            img_add_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Choose Category")
                            .setItems(AddHotelType, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    hotelMember = AddHotelType[which];
                                    txt_memberType.setText(hotelMember);
                                    txt_memberType.setTextColor(Color.BLACK);

                                }
                            }).show();
                }
            });

            img_hotel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
                }
            });


        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "On Create: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    public void checkData(){
        try {
            mProgress.setMessage("Checking credentials......");
            mProgress.show();

            hotelName = edt_hotelName.getText().toString();
            hotelAddress = edt_hotelAddress.getText().toString();
            hotelMemberType = txt_memberType.getText().toString();

            if(hotelName.isEmpty() && hotelAddress.isEmpty()&& hotelMember.isEmpty()){
                mProgress.hide();
                Toast.makeText(Vendor.getAppContext(), "empty fields", Toast.LENGTH_SHORT).show();
                return;
            }

            addHotel();
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Check Data: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void addHotel() {
        try{
            mProgress.setMessage("Saving Item info..");
            HashMap<String,Object> addData = new HashMap<>();
            addData.put("hotelName", hotelName);
            addData.put("memberShip",hotelMemberType);

            String member = hotelMember.toLowerCase();

            final StorageReference thumb_filepath =mStoragePath.child("/"+member).child(hotelName + ".jpg");

            mHotelRef.child(hotelMemberType).child(hotelName).updateChildren(addData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    try{
                        UploadTask uploadTask = thumb_filepath.putBytes(thumb_bite);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                thumb_filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        imageUrl = task.getResult().toString();
                                        Map images = new HashMap<>();
                                        if(imageUrl == null){
                                            imageUrl = "";
                                        }
                                        images.put("itemImage", imageUrl);
                                        mHotelRef.child(hotelMemberType).child(hotelName).updateChildren(images).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {

                                                    mProgress.dismiss();
                                                    Toast.makeText(Vendor.getAppContext(), "Hotel Added", Toast.LENGTH_SHORT).show();
                                                    img_hotel.setImageResource(R.drawable.ic_cart_blue);
                                                    edt_hotelAddress.setText("");
                                                    edt_hotelName.setText("");

                                                    txt_memberType.setText("Add Member");
                                                    txt_memberType.setTextColor(getResources().getColor(R.color.textColorHint));


                                                } else {

                                                    mProgress.hide();
                                                    Toast.makeText(Vendor.getAppContext(), "error images", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgress.hide();
                                Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }catch (Exception e){
                        mProgress.hide();
                        Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.hide();
                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Add Hotel"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == Gallery_pick && resultCode == RESULT_OK){

                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1,1)
                        .start(getContext(),this);


            }
            //Toast.makeText(getContext(),String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) , Toast.LENGTH_SHORT).show();
            if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result =CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){


                    Uri resultUri = result.getUri();

                    File thum_file = new File(resultUri.getPath());

                    //Toast.makeText(getContext(), "cropImage2", Toast.LENGTH_SHORT).show();





                    thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(getContext()).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(thum_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_bite = baos.toByteArray();

                    Picasso.with(getContext()).load(thum_file).placeholder(R.drawable.ic_cart_blue).into(img_hotel);







                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
