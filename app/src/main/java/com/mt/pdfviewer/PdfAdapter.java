package com.mt.pdfviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private static final String TAG = "PdfAdapter";
    Context context;
    ArrayList<File> pdfFiles;

    public PdfAdapter(Context context, ArrayList<File> pdfFiles) {
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
        long dateAdded = pdfFiles.get(position).lastModified();
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(dateAdded));

        holder.pdfTitle.setText(pdfFiles.get(position).getName());
        holder.pdfDateAdded.setText(dateString);
        holder.pdfTitle.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    public class PdfViewHolder extends RecyclerView.ViewHolder {
        TextView pdfTitle, pdfDateAdded;
        CardView container;
        ImageView pdfCover;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfTitle = itemView.findViewById(R.id.tvPdfTitle);
            pdfDateAdded = itemView.findViewById(R.id.tvPdfDateAdded);
            container = itemView.findViewById(R.id.containerPdf);
            pdfCover = itemView.findViewById(R.id.coverPdf);
        }
    }
}
