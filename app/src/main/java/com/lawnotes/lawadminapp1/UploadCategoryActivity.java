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
import com.lawnotes.lawadminapp1.Models.CategoryModel;
import com.lawnotes.lawadminapp1.databinding.ActivityUploadCategoryBinding;

import java.util.ArrayList;
import java.util.Date;

public class UploadCategoryActivity extends AppCompatActivity {

    ActivityUploadCategoryBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<CategoryModel> list;
    Uri imageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("please wait");

        binding.feImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,7);

            }
        });

        binding.btnUploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri==null){

                    Toast.makeText(UploadCategoryActivity.this, "please upload category image", Toast.LENGTH_SHORT).show();

                }

                else if (binding.edtCategoryName.getText().toString().isEmpty()) {

                    binding.edtCategoryName.setError("Enter categoryName");
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
                binding.categoryImages.setImageURI(imageUri);

            }
        }

    }

    private void uploadData() {

        final StorageReference reference= storage.getReference().child("law_notes").child(new Date().getTime()+"");

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(UploadCategoryActivity.this, "image Uploaded", Toast.LENGTH_SHORT).show();

                        CategoryModel model = new CategoryModel();
                        model.setCategoryLogo(uri.toString());
                        model.setCategoryName(binding.edtCategoryName.getText().toString());

                        database.getReference().child("law_notes")
                                .push()
                                .setValue(model)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(UploadCategoryActivity.this, "data added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(UploadCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                });

            }
        });


    }

}