package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lawnotes.lawadminapp1.Adapters.ChapterAdapter;
import com.lawnotes.lawadminapp1.Adapters.SubjectAdapter;
import com.lawnotes.lawadminapp1.Models.CategoryModel;
import com.lawnotes.lawadminapp1.Models.ChapterModel;
import com.lawnotes.lawadminapp1.Models.SubjectModel;
import com.lawnotes.lawadminapp1.databinding.ActivitySubjectBinding;

import java.util.ArrayList;
import java.util.Date;

public class SubjectActivity extends AppCompatActivity {

    ActivitySubjectBinding binding;
    ArrayList<SubjectModel> list;
    SubjectAdapter adapter;
    FirebaseDatabase database;
    Dialog dialog;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        key = getIntent().getStringExtra("key");
        database = FirebaseDatabase.getInstance();


        dialog = new Dialog(SubjectActivity.this);
        dialog.setContentView(R.layout.loading_dialog);

        if (dialog.getWindow() !=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }

        dialog.show();

        list = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recyChapters.setLayoutManager(layoutManager);

        adapter = new SubjectAdapter(this,list,key);
        binding.recyChapters.setAdapter(adapter);

        database.getReference().child("law_notes").child(key).child("subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        SubjectModel post= dataSnapshot.getValue(SubjectModel.class);
                        post.setKey(dataSnapshot.getKey());
                        list.add(post);

                    }
                    binding.recyChapters.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.chapterNotAv.setVisibility(View.GONE);
                    dialog.dismiss();

                }
                else {

                    binding.chapterNotAv.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SubjectActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnUploadChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SubjectActivity.this, UploadSubjectActivity.class);
                intent.putExtra("catKey",key);
                startActivity(intent);
            }
        });


    }
}