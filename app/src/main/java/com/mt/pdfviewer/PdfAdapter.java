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

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        String dateString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(dateAdded));

        holder.pdfTitle.setText(pdfFiles.get(position).getName());
        holder.pdfCover.setImageBitmap(renderToBitmap(new File(pdfFiles.get(position).getAbsolutePath())));
        holder.pdfDateAdded.setText(dateString);
        holder.pdfTitle.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    public Bitmap renderToBitmap(File filePath) {
        Bitmap bmp = null;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(filePath, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, 0);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, 0);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, 0);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, 0, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static class PdfViewHolder extends RecyclerView.ViewHolder {
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
