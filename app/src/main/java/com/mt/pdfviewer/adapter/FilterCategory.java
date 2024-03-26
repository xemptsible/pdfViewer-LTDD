package com.mt.pdfviewer.adapter;

import android.widget.Filter;

import com.mt.pdfviewer.model.CategoryModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FilterCategory extends Filter {
    ArrayList<CategoryModel> filterlist;
    CategoryAdapter categoryAdapter;


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
//        FilterResults
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
