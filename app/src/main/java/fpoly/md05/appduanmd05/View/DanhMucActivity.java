package fpoly.md05.appduanmd05.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;

public class DanhMucActivity extends AppCompatActivity implements SanPhamView {
    private Spinner spinerthongke;

    private FirebaseFirestore db;

    private Toolbar toolbar;

    private List<String> list;
    private ArrayList<SanPhamModels> arrayList;
    private SanPhamPreSenter sanPhamPreSenter;
    private RecyclerView rCvSP;
    private SanPhamAdapter sanPhamAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);
        InitWidget();
        Init();

    }

    private void Init() {
        Intent intent = getIntent();



        sanPhamPreSenter = new SanPhamPreSenter(this);
        arrayList  =new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        db= FirebaseFirestore.getInstance();
        list.add("Chọn Danh Mục");
        db.collection("LoaiSP").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot q :  queryDocumentSnapshots){
                    list.add(q.getString("tenloai"));
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(DanhMucActivity.this, android.R.layout.simple_list_item_1,list);
                spinerthongke.setAdapter(arrayAdapter);

            }
        });

        if(intent.hasExtra("KEY")){ arrayList.clear();
            if(sanPhamAdapter!=null){
                sanPhamAdapter.notifyDataSetChanged();
            }
            String key = intent.getStringExtra("KEY");
            sanPhamPreSenter.HandlegetDataSanPham(key,2);
        }
        spinerthongke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    arrayList.clear();
                    if(sanPhamAdapter!=null){
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                    sanPhamPreSenter.HandlegetDataSanPham(spinerthongke.getSelectedItem().toString(),1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void InitWidget() {
        spinerthongke = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        rCvSP = findViewById(R.id.rcvDanhMuc);
    }

    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arrayList.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamAdapter = new SanPhamAdapter(this, arrayList, 1);
        rCvSP.setLayoutManager(new LinearLayoutManager(this));
        rCvSP.setAdapter(sanPhamAdapter);
    }

    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arrayList.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamAdapter = new SanPhamAdapter(this, arrayList, 2);
        rCvSP.setLayoutManager(new LinearLayoutManager(this));
        rCvSP.setAdapter(sanPhamAdapter);
    }

    @Override
    public void getDataSanPhamGiamGia(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arrayList.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamAdapter = new SanPhamAdapter(this, arrayList, 3);
        rCvSP.setLayoutManager(new LinearLayoutManager(this));
        rCvSP.setAdapter(sanPhamAdapter);
    }

    @Override
    public void OnEmptyList() {
        Toast.makeText(this, "Không tìm thấy sản phẩm nào trong danh mục : "+spinerthongke.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }
}