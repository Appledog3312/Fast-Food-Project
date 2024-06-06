package com.example.project.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongKeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Gọi hàm để tính tổng doanh thu
        calculateTotalRevenue();
    }

    private void calculateTotalRevenue() {
        final TextView txtTotalRevenue = findViewById(R.id.txtTotalRevenue);

        databaseReference.child("DonHang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalRevenue = 0;

                for (DataSnapshot donHangSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ngayGioSnapshot : donHangSnapshot.getChildren()) {
                        DataSnapshot trangThaiSnapshot = ngayGioSnapshot.child("TrangThai");
                        if (trangThaiSnapshot.exists()) {
                            String trangThai = trangThaiSnapshot.child("Trangthai").getValue(String.class);

                            if ("Đã giao".equals(trangThai)) {
                                DataSnapshot tongCongSnapshot = ngayGioSnapshot.child("TongCong");
                                if (tongCongSnapshot.exists()) {
                                    String tongCong = tongCongSnapshot.child("Tongcong").getValue(String.class);
                                    assert tongCong != null;
                                    totalRevenue += Long.parseLong(tongCong);
                                }
                            }
                        }
                    }
                }

                // Hiển thị tổng doanh thu
                txtTotalRevenue.setText("Tổng doanh thu: " + totalRevenue + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });
    }
}
