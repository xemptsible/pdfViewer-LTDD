package com.mt.pdfviewer.adapter;

import static com.mt.pdfviewer.Utils.BYTE_ARRAY;

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
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.Utils;
import com.mt.pdfviewer.databinding.RvitemPdfBinding;
import com.mt.pdfviewer.main.admin.AdminChinhSuaPdfActivity;
import com.mt.pdfviewer.model.CategoryModel;
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
        String ten = pdfModel.getTenTruyen(),
                moTa = pdfModel.getMoTa();
        long dauThoiGian = pdfModel.getDauThoiGianCapNhat();
        String thoiGianDinhDang = Utils.dinhDangThoiGian(dauThoiGian);

        holder.tenTruyen.setText(ten);
        holder.moTaTruyen.setText(moTa);
        holder.ngayCapNhat.setText(thoiGianDinhDang);

        layKichCoPdf(pdfModel, holder);
        layPdfTuDuongDan(pdfModel, holder);
        layTheLoaiPdf(pdfModel, holder);

        holder.btnLuaChon.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnLuaChon);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.xoaPdf) {
                    Toast.makeText(context, "Đang xóa truyện", Toast.LENGTH_LONG).show();
                    xoaTruyen(pdfModel, holder);
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
    }

    private void layKichCoPdf(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String duongDan = pdfModel.getDuongUrlTruyen();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);
        ref.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    // Lấy kích cỡ trong byte
                    double bytes = storageMetadata.getSizeBytes();

                    holder.kichCoTruyen.setText(Utils.layCoDinhDangFile(bytes));
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Thất bại lấy kích cỡ: " + e.getMessage())
                );
    }

    private void layPdfTuDuongDan(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String duongDan = pdfModel.getDuongUrlTruyen();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);
        ref.getBytes(BYTE_ARRAY)
                .addOnSuccessListener(bytes -> {
                    Log.d(TAG, "Lấy bìa cho" + pdfModel.getTenTruyen());
                    holder.biaTruyen.fromBytes(bytes)
                            .pages(0)
                            .spacing(0)
                            .enableSwipe(false)
                            .onError(throwable -> Log.e(TAG, "Không mở làm bìa được: " + throwable.getMessage()))
                            .onPageError((i, throwable) -> Log.e(TAG, "Lỗi trang " + i + " : " + throwable.getMessage()))
                            .load();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Thất bại lấy PDF từ đường dẫn: " + e.getMessage()));
    }

    private void xoaTruyen(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String truyenId = pdfModel.getUid(),
                tenTruyen = pdfModel.getTenTruyen(),
                duongDanTruyen = pdfModel.getDuongUrlTruyen();

        Log.d(TAG, "Xóa truyện " + tenTruyen + " trong " + TAG);

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(duongDanTruyen);
        storageRef.delete()
                .addOnSuccessListener(unused -> {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Truyen");
                    dbRef.child(truyenId)
                            .removeValue()
                            .addOnSuccessListener(unused1 -> {
                                Log.d(TAG, "Xóa truyện trong Realtime Database thành công");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "Xóa truyện trong Realtime Database thất bại. Lý do: " + e.getMessage());
                            });

                    Toast.makeText(context, "Xóa truyện thành công!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Xóa truyện trong Storage thất bại. Lý do: " + e.getMessage());
                    Toast.makeText(context, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void layTheLoaiPdf(PdfModel pdfModel, PdfAdminViewHolder holder) {
        String tl_uid = pdfModel.getTheLoai_uid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");

        ref.child(tl_uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = (String) snapshot.child("theLoai").getValue();

                        holder.theLoai.setText(category);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
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
