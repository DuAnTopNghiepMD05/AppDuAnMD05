package fpoly.md05.appduanmd05.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;

public class DanhMucActivity extends AppCompatActivity {

    private TextView tvQuan, tvAo, tvKhac;

    private boolean isTextViewHidden = false;
    private ImageView btnback;
    private RecyclerView recyclerView;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);

        tvAo = findViewById(R.id.danhmuc_tvAo);
        tvQuan = findViewById(R.id.danhmuc_tvQuan);
        tvKhac = findViewById(R.id.danhmuc_tvKhac);
        recyclerView = findViewById(R.id.rcvDanhMuc);
        btnback = findViewById(R.id.ic_back_dm);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        tvAo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Áo");
                hideTextViews();
            }
        });

        tvQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Quần");
                hideTextViews();
            }
        });

        tvKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Khác");
                hideTextViews();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTextViewHidden) {
                    Intent intent = new Intent(DanhMucActivity.this, DanhMucActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc activity hiện tại để tránh quay lại nó khi nhấn nút back trên màn hình Danh Mục
                } else {
                    onBackPressed(); // Quay về màn hình Home
                }
            }
        });


    }
    private void hideTextViews() {
        tvAo.setVisibility(View.GONE);
        tvQuan.setVisibility(View.GONE);
        tvKhac.setVisibility(View.GONE);
        isTextViewHidden = true;
    }

    private void getDataFromFirestore(String loaiSP) {
        tvAo.setVisibility(View.GONE);
        tvQuan.setVisibility(View.GONE);
        tvKhac.setVisibility(View.GONE);
        Log.d("FirestoreQuery", "Querying Firestore for product type: " + loaiSP);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("SanPham")
                .whereEqualTo("loaisp", loaiSP)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("FirestoreQuery", "Query successful. Retrieved " + queryDocumentSnapshots.size() + " documents.");
                        ArrayList<SanPhamModels> sanPhamList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            SanPhamModels sanPham = document.toObject(SanPhamModels.class);
                            sanPhamList.add(sanPham);
                        }
                        // Tạo adapter và thiết lập cho RecyclerView
                        sanPhamAdapter = new SanPhamAdapter(DanhMucActivity.this, sanPhamList, 0);
                        recyclerView.setAdapter(sanPhamAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreQuery", "Error querying Firestore: " + e.getMessage());
                    }
                });
    }

}
