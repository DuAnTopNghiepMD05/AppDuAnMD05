package fpoly.md05.appduanmd05.View.FragMent.Danhmuc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;

public class Fragment_shoe extends Fragment implements SanPhamView {

    private ArrayList<String> arrayList;
    private ArrayList<SanPhamModels> arr_sp;
    private SanPhamAdapter sanPhamAdapter;
    private RecyclerView rcvSP;
    private SanPhamPreSenter sanPhamPreSenter;
    private View view;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_shoe, container, false);
        InitWidget();
        Init();
        InitSanPham();
        return view;

    }

    private void InitWidget() {

        rcvSP = view.findViewById(R.id.frgShoe_rcvShoe);
    }

    private void Init() {
        arrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    private void InitSanPham() {
        arr_sp = new ArrayList<>();
        sanPhamPreSenter = new SanPhamPreSenter(this);
        sanPhamPreSenter.HandlegetDataSanPham();
    }


    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamAdapter = new SanPhamAdapter(getContext(), arr_sp);
        rcvSP.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcvSP.setAdapter(sanPhamAdapter);
    }

    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {

    }

    @Override
    public void getDataSanPhamGiamGia(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {

    }

    @Override
    public void OnEmptyList() {

    }
}