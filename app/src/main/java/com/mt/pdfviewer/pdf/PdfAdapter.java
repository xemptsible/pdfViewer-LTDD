package com.mt.pdfviewer.pdf;

import static com.mt.pdfviewer.Utils.layCoDinhDangFile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mt.pdfviewer.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> implements Filterable {

    private static final String TAG = "PdfAdapter";
    Context context;
    ArrayList<File> pdfFiles;
    ArrayList<File> pdfFilesLoc;
//    ArrayList<PdfModelOLD> pdfModels;
//    ArrayList<PdfModelOLD> filterPdfModels;

    public PdfAdapter(Context context, ArrayList<File> pdfFiles) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.pdfFilesLoc = pdfFiles;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.rvitem_pdf, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.rangBuocThuocTinh(pdfFilesLoc.get(position));

        holder.pdfMenuBtn.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.pdfMenuBtn);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.hidePdf) {
                    Log.d("PdfAdapter", "Removing RV item at position" + holder.getAdapterPosition());
                    pdfFilesLoc.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, pdfFilesLoc.size());
                }
                return true;
            });

            popupMenu.inflate(R.menu.popup_rv_menu);
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return pdfFilesLoc.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            FilterResults results = new FilterResults();
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (!charSequenceString.isEmpty()) {
                    ArrayList<File> pdfFilesMoiLoc = new ArrayList<>();

                    for (File file : pdfFiles) {
                        if (file.getName().toLowerCase().contains(charSequenceString))
                            pdfFilesMoiLoc.add(file);
                    }
                    results.values = pdfFilesMoiLoc;
                    Log.d(TAG, "ArrayList filtered: " + results.values);
                } else {
                    results.values = pdfFiles;
                    Log.d(TAG, "ArrayList reset");
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pdfFilesLoc = (ArrayList<File>) results.values;
//                Log.d(TAG, "publishResults: " + pdfFilesLoc);
                notifyDataSetChanged();
                Log.d(TAG, "Notified in publish!");
            }
        };
    }

    public void xoaHet() {
        pdfFiles.clear();
        notifyDataSetChanged();
    }



    public static class PdfViewHolder extends RecyclerView.ViewHolder{
        TextView pdfTitle, pdfDateAdded, pdfSize;
        CardView container;
        ImageView pdfCover, pdfMenuBtn;
        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfTitle = itemView.findViewById(R.id.tvTenTruyen);
            pdfDateAdded = itemView.findViewById(R.id.tvThoiGianCapNhat);
            pdfCover = itemView.findViewById(R.id.biaTruyen);
            pdfSize = itemView.findViewById(R.id.tvKichCo);
            container = itemView.findViewById(R.id.pdf_card);
            pdfMenuBtn = itemView.findViewById(R.id.btnLuaChonThem);
        }
        private void rangBuocThuocTinh(final File item) {
            long pdfFileSize = item.length();
            long dateAdded = item.lastModified();
            String dateString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(dateAdded));

            pdfTitle.setText(item.getName());
            pdfCover.setImageBitmap(PdfUtils.xuatRaBitmap(new File(item.getAbsolutePath()), itemView.getContext()));
            pdfSize.setText(layCoDinhDangFile(pdfFileSize));
            pdfDateAdded.setText(dateString);
            pdfTitle.setSelected(true);

            itemView.setOnClickListener(view -> {
                Log.d("PdfAdapter", "This was clicked at position " + getAdapterPosition() + " and its name is " + pdfTitle.getText());
                Intent intent = new Intent(view.getContext(), PdfViewerActivity.class);

                intent.putExtra("pdfFilePath", "/storage/emulated/0/Download/"+pdfTitle.getText());
                intent.putExtra("pdfName", pdfTitle.getText());
                view.getContext().startActivity(intent);
            });
        }
    }
}