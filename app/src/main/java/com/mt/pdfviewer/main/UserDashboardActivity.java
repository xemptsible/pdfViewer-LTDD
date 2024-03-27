package com.mt.pdfviewer.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.auth.LoginActivity;
import com.mt.pdfviewer.databinding.ActivityUserDashboardBinding;
import com.mt.pdfviewer.main.user.UserDanhSachPdfFragment;
import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;
import java.util.Objects;

public class UserDashboardActivity extends AppCompatActivity {
    private ActivityUserDashboardBinding binding;
    private FirebaseAuth firebaseAuth;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<CategoryModel> theLoaiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        xacThucNguoiDung();

        taoViewPagerAdapter(binding.viewPagerPdf);
        binding.tabLayoutChoTheLoai.setupWithViewPager(binding.viewPagerPdf);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Library");
    }

    private void taoViewPagerAdapter(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);

        theLoaiArrayList = new ArrayList<>();

        DatabaseReference theLoaiRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        theLoaiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                theLoaiArrayList.clear();

                // Tạo một tab chứa tất cả các pdf có trong lưu trữ
                CategoryModel all = new CategoryModel("01", "All", 1);

                theLoaiArrayList.add(all);
                viewPagerAdapter.addFragment(UserDanhSachPdfFragment.newInstance(
                        all.getUid(),
                        all.getTheLoai()), all.getTheLoai());
                viewPagerAdapter.notifyDataSetChanged();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    CategoryModel model = ds.getValue(CategoryModel.class);
                    theLoaiArrayList.add(model);
                    viewPagerAdapter.addFragment(UserDanhSachPdfFragment.newInstance(
                            Objects.requireNonNull(model).getUid(),
                            model.getTheLoai()), model.getTheLoai());
                    viewPagerAdapter.notifyDataSetChanged();
                }

                viewPager.setAdapter(viewPagerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager.setAdapter(viewPagerAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<UserDanhSachPdfFragment> danhSachPdfFragments = new ArrayList<>();
        private ArrayList<String> danhSachTheLoai = new ArrayList<>();
        private Context context;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return danhSachPdfFragments.get(position);
        }

        @Override
        public int getCount() {
            return danhSachPdfFragments.size();
        }

        private void addFragment(UserDanhSachPdfFragment fragment, String theLoai) {
            danhSachPdfFragments.add(fragment);
            danhSachTheLoai.add(theLoai);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return danhSachTheLoai.get(position);
        }
    }

    private void xacThucNguoiDung() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dashboardLogout) {
            Toast.makeText(this, "This was clicked", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            xacThucNguoiDung();
        }
        return super.onOptionsItemSelected(item);
    }
}