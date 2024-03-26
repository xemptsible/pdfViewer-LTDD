package com.mt.pdfviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.mt.pdfviewer.Utils;
import com.mt.pdfviewer.databinding.RvitemPdfBinding;
import com.mt.pdfviewer.main.admin.AdminDanhSachPdfActivity;
import com.mt.pdfviewer.model.PdfModel;

import java.util.ArrayList;

public class PdfAdapterAdmin extends RecyclerView.Adapter<PdfAdapterAdmin.PdfAdminViewHolder> {
    private Context context;
    private RvitemPdfBinding binding;
    private ArrayList<PdfModel> pdfArrayList;
    private PdfModel pdfModel;

    public PdfAdapterAdmin(Context context, ArrayList<PdfModel> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public PdfAdapterAdmin.PdfAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RvitemPdfBinding.inflate(LayoutInflater.from(context), parent, false);
        return new PdfAdminViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PdfAdapterAdmin.PdfAdminViewHolder holder, int position) {
        pdfModel =  pdfArrayList.get(position);
        String ten = pdfModel.getTenTruyen(),
                moTa = pdfModel.getMoTa();
        long dauThoiGian = pdfModel.getDauThoiGian();
        String thoiGianDinhDang = Utils.dinhDangThoiGian(dauThoiGian);

        holder.tenTruyen.setText(ten);
        holder.moTaTruyen.setText(moTa);
        holder.ngayCapNhat.setText(thoiGianDinhDang);

        taiKichCoPdf(pdfModel, holder);
        taiPdfTuDuongDan(pdfModel, holder);
        taiTheLoaiPdf(pdfModel, holder);
    }

    private void taiKichCoPdf(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String duongDan = pdfModel.getDuongDanPdf();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);
        ref.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    // Lấy kích cỡ trong byte
                    double bytes = storageMetadata.getSizeBytes();

                    holder.ngayCapNhat.setText(Utils.layCoDinhDangFile(bytes));

                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void taiPdfTuDuongDan(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String duongDan = pdfModel.getDuongDanPdf();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);


    }

    private void taiTheLoaiPdf(PdfModel pdfModel, PdfAdminViewHolder holder) {
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    public class PdfAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tenTruyen, moTaTruyen, kichCoTruyen, ngayCapNhat;
        PDFView biaTruyen;
        ImageButton btnLuaChon;
        public PdfAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tenTruyen = binding.tvTenTruyen;
            moTaTruyen = binding.tvMoTaTruyen;
            kichCoTruyen = binding.tvKichCo;
            ngayCapNhat = binding.tvThoiGianCapNhat;
            biaTruyen = binding.biaTruyen;
            btnLuaChon = binding.btnLuaChonThem;
        }
    }
}
