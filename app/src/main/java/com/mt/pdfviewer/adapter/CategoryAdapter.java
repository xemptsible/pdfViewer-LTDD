package com.mt.pdfviewer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.databinding.RvitemCategoryBinding;
import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private RvitemCategoryBinding binding;
    Context context;
    ArrayList<CategoryModel> categories;
    public CategoryAdapter(Context context, ArrayList<CategoryModel> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater.from(context).inflate(R.layout.rvitem_category, parent, false);
        binding = RvitemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        // getRoot() lấy layout ngoài cùng nhất => layout bắt đầu bằng CardView thì lấy CardView
        return new CategoryViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position);

        String uid = category.getUid();
        String theLoai = category.getTheLoai();

        holder.categoryTitle.setText(theLoai);
        holder.deleteBtn.setOnClickListener(v -> {
            Log.d(TAG, theLoai + " was clicked at position " + position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa")
                    .setMessage("Bạn có chắc bạn muốn xóa thể loại này không?")
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");

                        ref.child(uid)
                                .removeValue()
                                .addOnSuccessListener(unused -> {
                                    Log.d(TAG,""+uid);
                                    Toast.makeText(context, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        ImageButton deleteBtn;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = binding.tvCategoryName;
            deleteBtn = binding.btnDeleteCategory;
        }
    }
}
