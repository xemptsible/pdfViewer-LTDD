package com.mt.pdfviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mt.pdfviewer.databinding.RvitemCategoryBinding;
import com.mt.pdfviewer.main.AdminDashboardActivity;
import com.mt.pdfviewer.main.admin.AdminDanhSachPdfActivity;
import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {
    private static final String TAG = "CategoryAdapter";
    private RvitemCategoryBinding binding;
    Context context;
    ArrayList<CategoryModel> theLoaiArrayList, theLoaiLoc;
    public CategoryAdapter(Context context, ArrayList<CategoryModel> categories) {
        this.context = context;
        this.theLoaiArrayList = categories;
        this.theLoaiLoc = categories;
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
        CategoryModel category = theLoaiArrayList.get(position);

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
                                    Toast.makeText(context, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminDanhSachPdfActivity.class);
            intent.putExtra("uidTheLoai", uid);
            intent.putExtra("tenTheLoai", theLoai);
            context.startActivity(intent);
            ((AdminDashboardActivity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return theLoaiArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    constraint = constraint.toString().trim();
                    ArrayList<CategoryModel> daLoc = new ArrayList<>();

                    for (CategoryModel theLoai : theLoaiLoc) {
                        if (theLoai.getTheLoai().toLowerCase().contains(constraint))
                            daLoc.add(theLoai);
                    }
                    results.count = daLoc.size();
                    results.values = daLoc;
                    Log.d("FilterCategory", "ArrayList filtered: " + results.values);
                }
                // Nếu xóa từ khóa lọc
                else {
                    results.count = theLoaiLoc.size();
                    results.values = theLoaiLoc;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                theLoaiArrayList = (ArrayList<CategoryModel>) results.values;
                notifyDataSetChanged();
            }
        };
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
