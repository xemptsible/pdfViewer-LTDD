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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.main.ChiTietPdfActivity;
import com.mt.pdfviewer.main.Utils;
import com.mt.pdfviewer.databinding.RvitemPdfBinding;
import com.mt.pdfviewer.main.admin.AdminChinhSuaPdfActivity;
import com.mt.pdfviewer.model.PdfModel;

import java.util.ArrayList;

public class PdfAdapterAdmin extends RecyclerView.Adapter<PdfAdapterAdmin.PdfAdminViewHolder> implements Filterable {
    private final static String TAG = "PdfAdapterAdmin";
    private Context context;
    private RvitemPdfBinding binding;
    private ArrayList<PdfModel> pdfArrayList, pdfLoc;
    private PdfModel pdfModel;

    public PdfAdapterAdmin(Context context, ArrayList<PdfModel> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.pdfLoc = pdfArrayList;
    }

    @NonNull
    @Override
    public PdfAdapterAdmin.PdfAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RvitemPdfBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PdfAdminViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PdfAdapterAdmin.PdfAdminViewHolder holder, int position) {
        pdfModel = pdfArrayList.get(position);
        String uid = pdfModel.getUid(),
                ten = pdfModel.getTenTruyen(),
                theLoaiId = pdfModel.getTheLoai_uid(),
                moTa = pdfModel.getMoTa(),
                duongDan = pdfModel.getDuongUrlTruyen();
        long dauThoiGian = pdfModel.getDauThoiGianCapNhat();
        String thoiGianDinhDang = Utils.dinhDangThoiGian(dauThoiGian);

        holder.tenTruyen.setText(ten);
        holder.moTaTruyen.setText(moTa);
        holder.ngayCapNhat.setText(thoiGianDinhDang);

        Utils.layKichCoPdf(duongDan, binding.tvKichCo);
        Utils.layBiaPdfTuDuongDan(duongDan, ten, holder.biaTruyen);
        Utils.layTheLoaiPdf(theLoaiId, binding.tvPdfTheLoai);

        holder.btnLuaChon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnLuaChon);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.xoaPdf) {
                    Toast.makeText(context, "Đang xóa truyện", Toast.LENGTH_LONG).show();
                    Utils.xoaTruyen(context, uid, ten, duongDan);

                }
                else if (menuItem.getItemId() == R.id.chinhSuaPdf) {
                    String idTruyen = pdfModel.getUid();
                    Log.d(TAG, "Chỉnh sửa trong " + ten + " trong " + TAG);
                    Intent intent = new Intent(context, AdminChinhSuaPdfActivity.class);
                    intent.putExtra("idTruyen", idTruyen);
                    context.startActivity(intent);
                }
                return true;
            });
            popupMenu.inflate(R.menu.popup_rv_menu);
            popupMenu.show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietPdfActivity.class);
            intent.putExtra("idTruyen", uid);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    constraint = constraint.toString().trim();
                    ArrayList<PdfModel> daLoc = new ArrayList<>();

                    for (PdfModel pdfModel : pdfLoc) {
                        if (pdfModel.getTenTruyen().toLowerCase().contains(constraint))
                            daLoc.add(pdfModel);
                    }
                    results.count = daLoc.size();
                    results.values = daLoc;
                    Log.d("FilterCategory", "ArrayList filtered: " + results.values);
                }
                // Nếu xóa từ khóa lọc
                else {
                    results.count = pdfLoc.size();
                    results.values = pdfLoc;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pdfArrayList = (ArrayList<PdfModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class PdfAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tenTruyen, moTaTruyen, theLoai, kichCoTruyen, ngayCapNhat;
        PDFView biaTruyen;
        ImageButton btnLuaChon;
        public PdfAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTruyen = binding.tvTenTruyen;
            moTaTruyen = binding.tvMoTaTruyen;
            theLoai = binding.tvPdfTheLoai;
            kichCoTruyen = binding.tvKichCo;
            ngayCapNhat = binding.tvThoiGianCapNhat;
            biaTruyen = binding.biaTruyen;
            btnLuaChon = binding.btnLuaChonThem;
        }
    }
}
