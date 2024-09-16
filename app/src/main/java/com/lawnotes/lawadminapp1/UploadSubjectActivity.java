package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.lawnotes.lawadminapp1.Models.SubjectModel;
import com.lawnotes.lawadminapp1.databinding.ActivityUploadSubjectBinding;

import java.util.ArrayList;

public class UploadSubjectActivity extends AppCompatActivity {

    ActivityUploadSubjectBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ArrayList<SubjectModel> list;
    ProgressDialog progressDialog;
    private String catKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadSubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        list = new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("please wait");

        catKey = getIntent().getStringExtra("catKey");

        binding.btnUploadCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtCategoryName.getText().toString().isEmpty()) {

                    binding.edtCategoryName.setError("Enter categoryName");
                }

                else {

                    progressDialog.show();
                    uploadData();

                }

            }
        });


    }


    private void uploadData() {

        SubjectModel model = new SubjectModel();
        model.setSubjectName(binding.edtCategoryName.getText().toString());

        database.getReference().child("law_notes").child(catKey).child("subject")
                .push()
                .setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(UploadSubjectActivity.this, "data added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(UploadSubjectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });


    }

}