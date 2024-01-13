package com.mt.pdfviewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private static final String TAG = "PdfAdapter";
    Context context;
    ArrayList<Pdf> pdfFiles;

    public PdfAdapter(Context context, ArrayList<Pdf> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.rvitem_pdf, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
//        holder.pdfTitle.setText(pdfFiles.get(position).getName());
        holder.pdfTitle.setText(pdfFiles.get(position).fileName);
        holder.pdfDateAdded.setText(pdfFiles.get(position).fileDateAdded.toString());

        holder.pdfTitle.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    public class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView pdfTitle, pdfDateAdded;
        CardView container;
        PDFView pdfView;
        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfTitle = itemView.findViewById(R.id.tvPdfTitle);
            pdfDateAdded = itemView.findViewById(R.id.tvPdfDateAdded);
            container = itemView.findViewById(R.id.containerPdf);
            pdfView = itemView.findViewById(R.id.coverPdf);
        }
    }
}
