package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.lawnotes.lawadminapp1.Adapters.CategoryAdapter;
import com.lawnotes.lawadminapp1.Models.CategoryModel;
import com.lawnotes.lawadminapp1.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<CategoryModel> list;
    CategoryAdapter adapter;
    FirebaseDatabase database;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();


        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.loading_dialog);

        if (dialog.getWindow() !=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
        }

        dialog.show();

        list = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recyCategories.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this,list);
        binding.recyCategories.setAdapter(adapter);

        database.getReference().child("law_notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        CategoryModel post= dataSnapshot.getValue(CategoryModel.class);
                        post.setKey(dataSnapshot.getKey());
                        list.add(post);

                    }
                    binding.recyCategories.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.noData.setVisibility(View.GONE);
                    dialog.dismiss();

                }
                else {

                    binding.noData.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnUploadCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,UploadCategoryActivity.class);
                startActivity(intent);
            }
        });

        binding.control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AdsControlActivity.class);
                startActivity(intent);
            }
        });


    }
}