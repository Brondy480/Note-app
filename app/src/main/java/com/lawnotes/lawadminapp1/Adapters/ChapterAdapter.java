package com.lawnotes.lawadminapp1.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnotes.lawadminapp1.Models.ChapterModel;
import com.lawnotes.lawadminapp1.R;
import com.lawnotes.lawadminapp1.UpdateChapterActivity;
import com.lawnotes.lawadminapp1.databinding.ItemChapterBinding;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.viewHolder>{

    Context context;
    ArrayList<ChapterModel> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String catKey,subKey;

    public ChapterAdapter(Context context, ArrayList<ChapterModel> list, String catKey, String subKey) {
        this.context = context;
        this.list = list;
        this.catKey = catKey;
        this.subKey = subKey;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ChapterModel model = list.get(position);

        holder.binding.chapterName.setText(model.getChapterName());
//        Picasso.get()
//                .load(model.getLogo())
//                .placeholder(R.drawable.logo)
//                .into(holder.binding.chapterLogo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, UpdateChapterActivity.class);
                intent.putExtra("name",model.getChapterName());
                intent.putExtra("catId",catKey);
                intent.putExtra("subKey",subKey);
                intent.putExtra("key",model.getKey());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure, you want to delete this");

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {


                    database.getReference().child("law_notes").child(catKey).child("subject").child(subKey).child("content")
                            .child(model.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemChapterBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemChapterBinding.bind(itemView);

        }
    }

}