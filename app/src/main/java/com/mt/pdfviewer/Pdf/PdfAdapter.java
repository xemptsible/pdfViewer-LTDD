package com.mt.pdfviewer.Pdf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.mt.pdfviewer.R;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.text.DecimalFormat;
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

        long pdfSize = pdfFiles.get(position).length();

        holder.pdfTitle.setText(pdfFiles.get(position).getName());
        holder.pdfCover.setImageBitmap(renderToBitmap(new File(pdfFiles.get(position).getAbsolutePath())));
        holder.pdfSize.setText(getReadableFileSize(pdfSize));
        holder.pdfDateAdded.setText(dateString);
        holder.pdfTitle.setSelected(true);
        holder.pdfMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.pdfMenuBtn);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });

                popupMenu.inflate(R.menu.popup_rv_menu);
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }



    // Lấy trang đầu tiên làm bìa PDF
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

    public void clear() {

        pdfFiles.clear();
        notifyDataSetChanged();

    }

    //Đổi kích cỡ của file và đổi thành định dạng để trình bày kích cỡ file
    public static String getReadableFileSize(long size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (float) size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return dec.format(fileSize) + suffix;
    }


    public static class PdfViewHolder extends RecyclerView.ViewHolder{
        TextView pdfTitle, pdfDateAdded, pdfSize;
        CardView container;
        ImageView pdfCover, pdfMenuBtn;
        PDFView pdfView;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfTitle = itemView.findViewById(R.id.tvPdfTitle);
            pdfDateAdded = itemView.findViewById(R.id.tvPdfDateAdded);
            pdfCover = itemView.findViewById(R.id.coverPdf);
            pdfSize = itemView.findViewById(R.id.tvPdfSize);
            container = itemView.findViewById(R.id.containerPdf);
            pdfMenuBtn = itemView.findViewById(R.id.popupBtn);

            itemView.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "This was clicked at position " + getAdapterPosition() + " and its name is " + pdfTitle.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), PdfViewerActivity.class);

                intent.putExtra("pdfFilePath", "/storage/emulated/0/Download/"+pdfTitle.getText());
                intent.putExtra("pdfName", pdfTitle.getText());
                view.getContext().startActivity(intent);
            });

            /*itemView.setOnLongClickListener(view -> {
                Toast.makeText(view.getContext(), "This was long clicked at position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return true;
            });*/

            /*pdfMenuBtn.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                Toast.makeText(view.getContext(), "This menu was clicked at position " + pos, Toast.LENGTH_SHORT).show();

                PopupMenu popupMenu = new PopupMenu(pdfMenuBtn.getContext(), itemView);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });

                popupMenu.inflate(R.menu.popup_rv_menu);
                popupMenu.show();
            });*/
        }
    }
}
