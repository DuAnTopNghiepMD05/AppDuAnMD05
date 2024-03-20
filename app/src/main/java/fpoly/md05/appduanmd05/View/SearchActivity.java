package fpoly.md05.appduanmd05.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.R;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView productRecyclerView;
    private FirebaseFirestore db;
    private SanPhamAdapter adapter;
    private ArrayList<SanPhamModels> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productsList = new ArrayList<>();
        adapter = new SanPhamAdapter(this, productsList);
        productRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();
                searchProducts(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void searchProducts(String keyword) {
        if (keyword.isEmpty()) {
            productsList.clear();
            adapter.notifyDataSetChanged();
        } else {
            db.collection("SanPham")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        productsList.clear(); // Xóa dữ liệu cũ trước khi tìm kiếm mới
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            SanPhamModels product = document.toObject(SanPhamModels.class);
                            String productName = product.getTensp().toLowerCase();
                            if (productName.contains(keyword.toLowerCase())) {
                                productsList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi nếu có
                    });
        }
    }



}
