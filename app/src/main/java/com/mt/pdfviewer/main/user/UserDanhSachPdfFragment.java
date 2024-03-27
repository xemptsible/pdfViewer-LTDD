package com.mt.pdfviewer.main.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.adapter.PdfAdapterUser;
import com.mt.pdfviewer.databinding.FragmentUserDanhSachPdfBinding;
import com.mt.pdfviewer.model.PdfModel;

import java.util.ArrayList;
import java.util.Objects;

public class UserDanhSachPdfFragment extends Fragment {
    private FragmentUserDanhSachPdfBinding binding;
    private Context context;
    private String theLoaiuid, theLoai;
    private EditText edTimKiem;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            theLoaiuid = getArguments().getString("theLoaiId");
            theLoai = getArguments().getString("theLoai");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDanhSachPdfBinding.inflate(LayoutInflater.from(getContext()), container, false);

        if (theLoai.equals("All"))
            layTatCaTruyen();
        else
            layTruyenTheoTheLoai();


        binding.edTimKiemUserTruyen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    pdfAdapterUser.getFilter().filter(s);
                }
                catch (Exception e) {
                    Log.e("UserDanhSachPdfFragment", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    private void layTatCaTruyen() {
        pdfModelArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfModelArrayList.clear();
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
                        pdfModelArrayList.clear();
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