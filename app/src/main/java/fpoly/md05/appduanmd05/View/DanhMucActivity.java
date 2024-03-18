package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class DanhMucActivity extends AppCompatActivity  {

    private FirebaseFirestore db;

    private TextView tvQuan, tvAo, tvKhac;
    private ArrayList<SanPhamModels> arrayList;
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

        tvAo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Ao");
            }
        });

        tvQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Quan");
            }
        });

        tvKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromFirestore("Khác");
            }
        });
    }

    private void getDataFromFirestore(String loaiSP) {
        // Ví dụ:
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("SanPham")
                .whereEqualTo("loaisp", loaiSP)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<SanPhamModels> sanPhamList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            SanPhamModels sanPham = document.toObject(SanPhamModels.class);
                            sanPhamList.add(sanPham);
                        }
                        sanPhamAdapter = new SanPhamAdapter((Context) DanhMucActivity.this, sanPhamList, 0);
                        recyclerView.setAdapter(sanPhamAdapter);
                    }
                });
    }
}
