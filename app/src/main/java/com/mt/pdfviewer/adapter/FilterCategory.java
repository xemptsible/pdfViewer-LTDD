package com.mt.pdfviewer.adapter;

import android.util.Log;
import android.widget.Filter;

import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    ArrayList<CategoryModel> theLoaiLoc;
    CategoryAdapter categoryAdapter;

    public FilterCategory(ArrayList<CategoryModel> filterlist, CategoryAdapter categoryAdapter) {
        this.theLoaiLoc = filterlist;
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().trim();
            ArrayList<CategoryModel> theLoaiDuocLoc = new ArrayList<>();

            for (CategoryModel theloai : theLoaiLoc) {
                if (theloai.getTheLoai().toLowerCase().contains(constraint))
                    theLoaiDuocLoc.add(theloai);
            }
            results.count = theLoaiDuocLoc.size();
            results.values = theLoaiDuocLoc;
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
        categoryAdapter.theLoaiArrayList = (ArrayList<CategoryModel>) results.values;
        categoryAdapter.notifyDataSetChanged();
    }
}
