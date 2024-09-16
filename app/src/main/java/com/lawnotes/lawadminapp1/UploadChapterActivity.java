package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lawnotes.lawadminapp1.Models.ChapterModel;
import com.lawnotes.lawadminapp1.databinding.ActivityUploadChapterBinding;

import java.util.ArrayList;
import java.util.Date;

public class UploadChapterActivity extends AppCompatActivity {

    ActivityUploadChapterBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<ChapterModel> list;
    Uri imageUri,file;
    ProgressDialog progressDialog;
    private String catKey,subKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catKey = getIntent().getStringExtra("catKey");
        subKey = getIntent().getStringExtra("subKey");

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("please wait");


//        binding.feImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent intent= new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent,7);
//
//            }
//        });

        binding.featchPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),2);

            }
        });



        binding.btnUploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (file==null){

                    Toast.makeText(UploadChapterActivity.this, "please upload category image", Toast.LENGTH_SHORT).show();

                }
                else if (binding.edtCategoryName.getText().toString().isEmpty()) {

                    binding.edtCategoryName.setError("Enter chapter name");

                }
                else {

                    progressDialog.show();
                    uploadData();

                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==7){
            if (data!=null){

                imageUri=data.getData();
              //  binding.categoryImages.setImageURI(imageUri);

                StorageReference reference = storage.getReference().child("Pdf/"+System.currentTimeMillis()+".pdf");
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imageUri = uri;

                                Toast.makeText(UploadChapterActivity.this, "logo uploaded", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(UploadChapterActivity.this, "pdf not uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        }
        else {

            file = data.getData();


        }


    }


    private void uploadData() {

        final StorageReference reference= storage.getReference().child("law_chapter").child(new Date().getTime()+"");

        reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(UploadChapterActivity.this, "image Uploaded", Toast.LENGTH_SHORT).show();

                        ChapterModel chapterModel = new ChapterModel();
                        chapterModel.setChapterName(binding.edtCategoryName.getText().toString());
                       // chapterModel.setLogo(imageUri.toString());
                        chapterModel.setPdfUrl(uri.toString());

                        database.getReference().child("law_notes").child(catKey).child("subject").child(subKey).child("content")
                                .push()
                                .setValue(chapterModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(UploadChapterActivity.this, "data added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(UploadChapterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                });

            }
        });


    }


}