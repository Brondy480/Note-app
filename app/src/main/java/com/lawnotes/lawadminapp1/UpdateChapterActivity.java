package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnotes.lawadminapp1.databinding.ActivityUpdateChapterBinding;

import java.util.HashMap;

public class UpdateChapterActivity extends AppCompatActivity {

    ActivityUpdateChapterBinding binding;
    FirebaseDatabase database;
    private String name,catId,subKey,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateChapterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        name = getIntent().getStringExtra("name");
        catId = getIntent().getStringExtra("catId");
        subKey = getIntent().getStringExtra("subKey");
        key = getIntent().getStringExtra("key");

        binding.edtChapter.setText(name);

        binding.updateChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("chapterName",binding.edtChapter.getText().toString());

                database.getReference().child("law_notes").child(catId).child("subject").child(subKey).child("content")
                        .child(key)
                        .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Toast.makeText(UpdateChapterActivity.this, "updated", Toast.LENGTH_SHORT).show();
                                    onBackPressed();

                                }
                                else {

                                    Toast.makeText(UpdateChapterActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


            }
        });

    }
}