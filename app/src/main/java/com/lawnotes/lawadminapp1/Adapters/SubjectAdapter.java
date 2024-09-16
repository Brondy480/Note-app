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
import com.lawnotes.lawadminapp1.ChapterActivity;
import com.lawnotes.lawadminapp1.Models.CategoryModel;
import com.lawnotes.lawadminapp1.Models.SubjectModel;
import com.lawnotes.lawadminapp1.R;
import com.lawnotes.lawadminapp1.SubjectActivity;
import com.lawnotes.lawadminapp1.databinding.ItemCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.viewHolder>{

    Context context;
    ArrayList<SubjectModel> list;
    private String key;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public SubjectAdapter(Context context, ArrayList<SubjectModel> list, String key) {
        this.context = context;
        this.list = list;
        this.key = key;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        SubjectModel model = list.get(position);


        holder.binding.categoryName.setText(model.getSubjectName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChapterActivity.class);
                intent.putExtra("name",model.getSubjectName());
                intent.putExtra("catKey",key);
                intent.putExtra("subKey",model.getKey());
                context.startActivity(intent);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Category");
                builder.setMessage("Are you sure, you want to delete this category");

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {


                    database.getReference().child("law_notes").child(key).child("subject")
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

        ItemCategoryBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCategoryBinding.bind(itemView);

        }
    }

}