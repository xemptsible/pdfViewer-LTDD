package com.mt.pdfviewer.main;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    // Kích cỡ mảng byte cho PdfAdapterAdmin khi lấy PDF từ Storage
    private static final String ADAPTER_TAG = "PdfAdapterAdmin";
    public static final long BYTE_ARRAY = 52428800; // 50MB

    //Đổi kích cỡ của file và đổi thành định dạng để trình bày kích cỡ file
    public static String layCoDinhDangFile(double size) {
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
    public static String dinhDangThoiGian(long timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(timestamp));
    }

    public static void xoaTruyen(Context context, String truyenId, String tenTruyen, String duongDanTruyen) {
        Log.d(ADAPTER_TAG, "Xóa truyện " + tenTruyen + " trong " + ADAPTER_TAG);

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(duongDanTruyen);
        storageRef.delete()
                .addOnSuccessListener(unused -> {
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Truyen");
                    dbRef.child(truyenId)
                            .removeValue()
                            .addOnSuccessListener(unused1 -> {
                                Log.d(ADAPTER_TAG, "Xóa truyện trong Realtime Database thành công");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(ADAPTER_TAG, "Xóa truyện trong Realtime Database thất bại. Lý do: " + e.getMessage());
                            });

                    Toast.makeText(context, "Xóa truyện thành công!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.d(ADAPTER_TAG, "Xóa truyện trong Storage thất bại. Lý do: " + e.getMessage());
                    Toast.makeText(context, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    public static void layKichCoPdf(String duongDan, TextView tvKichCo) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);
        ref.getMetadata()
                .addOnSuccessListener(storageMetadata -> {
                    // Lấy kích cỡ trong byte
                    double bytes = storageMetadata.getSizeBytes();

                    tvKichCo.setText(Utils.layCoDinhDangFile(bytes));
                })
                .addOnFailureListener(e ->
                        Log.e(ADAPTER_TAG, "Thất bại lấy kích cỡ: " + e.getMessage())
                );
    }

    public static void layBiaPdfTuDuongDan(String duongDan, String tenTruyen, PDFView biaTruyen) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(duongDan);
        ref.getBytes(BYTE_ARRAY)
                .addOnSuccessListener(bytes -> {
                    Log.d(ADAPTER_TAG, "Lấy bìa cho" + tenTruyen);
                    biaTruyen.fromBytes(bytes)
                            .pages(0)
                            .spacing(0)
                            .enableSwipe(false)
                            .onError(throwable -> Log.e(ADAPTER_TAG, "Không mở làm bìa được: " + throwable.getMessage()))
                            .onPageError((i, throwable) -> Log.e(ADAPTER_TAG, "Lỗi trang " + i + " : " + throwable.getMessage()))
                            .load();
                })
                .addOnFailureListener(e -> Log.e(ADAPTER_TAG, "Thất bại lấy PDF từ đường dẫn: " + e.getMessage()));
    }

    public static void layTheLoaiPdf(String theLoaiId, TextView theLoai) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");

        ref.child(theLoaiId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = (String) snapshot.child("theLoai").getValue();
                        theLoai.setText(category);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
