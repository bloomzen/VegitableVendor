package com.example.vendor.AdminScreen;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBannerFragment extends Fragment {


    public AddBannerFragment() {
        // Required empty public constructor
    }

    ImageView img_currentBanner;
    TextView txt_remove;
    CircleImageView cir_banner;
    RelativeLayout rl_add_remove;

    DatabaseReference db;

    AlertDialog.Builder builder;


    static int Gallery_pick = 1;
    Bitmap thumb_bitmap;

    byte[] thumb_bite;
    Context context;

    String imageUrl;
    StorageReference mStoragePath;

    ProgressDialog mProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_banner, container, false);

        try {
            img_currentBanner = view.findViewById(R.id.img_current_banner);
            txt_remove = view.findViewById(R.id.text_remove_banner);
            cir_banner = view.findViewById(R.id.img_add_banner);
            rl_add_remove = view.findViewById(R.id.btn_add_banner);
            builder = new AlertDialog.Builder(getContext());


            mStoragePath = FirebaseStorage.getInstance().getReference();

            db = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            db.child("banner").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    try {

                        final String imageurl = snapshot.getValue().toString();
                        //Picasso.with(context).load(ItemImages).placeholder(R.drawable.ic_cart_blue).into(holder.ItemImage);
                        Picasso.with(getContext()).load(imageurl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_cart_blue).into(img_currentBanner, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getContext()).load(imageurl).placeholder(R.drawable.ic_cart_blue).into(img_currentBanner);
                            }
                        });
                    } catch (Exception e) {
                        img_currentBanner.setImageResource(R.drawable.ic_cart_blue);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "show Banner"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try{
            txt_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Vendor.getAppContext(), "clicked", Toast.LENGTH_SHORT).show();

                    builder.setTitle("Delete")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.child("banner").setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Vendor.getAppContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    builder.show();


                }
            });

        }catch (Exception e){
            Toast.makeText(Vendor.getAppContext(), "Remove Banner: "+ e.getMessage() , Toast.LENGTH_SHORT).show();
        }



        cir_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
            }
        });


        rl_add_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final StorageReference thumb_filepath = mStoragePath.child("/" + "banner").child("banner" + ".jpg");
                try {
                    UploadTask uploadTask = thumb_filepath.putBytes(thumb_bite);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            thumb_filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    imageUrl = task.getResult().toString();


                                    db.child("banner").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Vendor.getAppContext(), "banner Added", Toast.LENGTH_SHORT).show();
                                            } else {


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

                } catch (Exception e) {
                    mProgress.hide();
                    Toast.makeText(Vendor.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == Gallery_pick && resultCode == RESULT_OK) {

                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);


            }
            //Toast.makeText(getContext(),String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) , Toast.LENGTH_SHORT).show();
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {


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

                    Picasso.with(getContext()).load(thum_file).placeholder(R.drawable.ic_cart_blue).into(cir_banner);


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
