package com.mt.pdfviewer.main.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.adapter.PdfAdapterUser;
import com.mt.pdfviewer.databinding.FragmentUserDanhSachPdfBinding;
import com.mt.pdfviewer.main.AdminDashboardActivity;
import com.mt.pdfviewer.main.UserDashboardActivity;
import com.mt.pdfviewer.model.PdfModel;

import java.util.ArrayList;
import java.util.Objects;

public class UserDanhSachPdfFragment extends Fragment {
    private FragmentUserDanhSachPdfBinding binding;
    private Context context;
    private String theLoaiuid, theLoai;
    private SearchView searchViewFragment;
    private ArrayList<PdfModel> pdfModelArrayList;
    private PdfAdapterUser pdfAdapterUser;


    public UserDanhSachPdfFragment() {
        // Required empty public constructor
    }

    public static UserDanhSachPdfFragment newInstance(String uid, String theLoai) {
        UserDanhSachPdfFragment fragment = new UserDanhSachPdfFragment();
        Bundle args = new Bundle();
        args.putString("theLoaiId", uid);
        args.putString("theLoai", theLoai);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            theLoaiuid = getArguments().getString("theLoaiId");
            theLoai = getArguments().getString("theLoai");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDanhSachPdfBinding.inflate(LayoutInflater.from(getContext()), container, false);
        khoiTaoTimKiemTruyen();
        if (theLoai.equals("All"))
            layTatCaTruyen();
        else
            layTruyenTheoTheLoai();

        searchViewFragment = binding.svUserTruyen;
        
        return binding.getRoot();
    }
    private void khoiTaoTimKiemTruyen() {
        requireActivity().runOnUiThread(() -> searchViewFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pdfAdapterUser.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pdfAdapterUser.getFilter().filter(newText);
                return true;
            }
        }));
    }

    private void layTatCaTruyen() {
        pdfModelArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    PdfModel pdf = ds.getValue(PdfModel.class);

                    pdfModelArrayList.add(pdf);
                }

                pdfAdapterUser = new PdfAdapterUser(getContext(), pdfModelArrayList);
                binding.rvPdfUser.setAdapter(pdfAdapterUser);
                binding.rvPdfUser.setHasFixedSize(true);
                binding.rvPdfUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layTruyenTheoTheLoai() {
        pdfModelArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        // Sắp xếp vật thể mà có child đang kiếm
        ref.orderByChild("theLoai_uid").equalTo(theLoaiuid)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            PdfModel pdf = ds.getValue(PdfModel.class);

                            pdfModelArrayList.add(pdf);
                        }
                        pdfAdapterUser = new PdfAdapterUser(getContext(), pdfModelArrayList);
                        binding.rvPdfUser.setAdapter(pdfAdapterUser);
                        binding.rvPdfUser.setHasFixedSize(true);
                        binding.rvPdfUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}