package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnotes.lawadminapp1.Adapters.ChapterAdapter;
import com.lawnotes.lawadminapp1.Models.ChapterModel;
import com.lawnotes.lawadminapp1.databinding.ActivityChapterBinding;

import java.util.ArrayList;

public class ChapterActivity extends AppCompatActivity {

    ActivityChapterBinding binding;
    ArrayList<ChapterModel> list;
    ChapterAdapter adapter;
    FirebaseDatabase database;
    Dialog dialog;
    private String catKey,subKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catKey = getIntent().getStringExtra("catKey");
        subKey = getIntent().getStringExtra("subKey");
        database = FirebaseDatabase.getInstance();


        dialog = new Dialog(ChapterActivity.this);
        dialog.setContentView(R.layout.loading_dialog);

        if (dialog.getWindow() !=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }

        dialog.show();

        list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyChapters.setLayoutManager(layoutManager);

        adapter = new ChapterAdapter(this,list,catKey,subKey);
        binding.recyChapters.setAdapter(adapter);

        database.getReference().child("law_notes").child(catKey).child("subject").child(subKey).child("content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        ChapterModel post= dataSnapshot.getValue(ChapterModel.class);
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

                Toast.makeText(ChapterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnUploadChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChapterActivity.this, UploadChapterActivity.class);
                intent.putExtra("catKey",catKey);
                intent.putExtra("subKey",subKey);
                startActivity(intent);
            }
        });


    }
}