package fpoly.md05.appduanmd05.View.FragMent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;


public class FragMent_Home extends Fragment implements SanPhamView {

    View view;

    private ArrayList<String> arrayList;

    private ViewPager viewPager;

    private FirebaseFirestore db;

    private SanPhamPreSenter sanPhamPreSenter;

    private ArrayList<SanPhamModels> arr_sp, arr_sp_nb, arr_sp_gg;

    private SanPhamAdapter sanPhamAdapter, sanPhamNBAdapter, sanPhamGGAdapter;

    private RecyclerView rcvSP, rcvSpNoiBat, rcvSPGiamGia;

    private ImageButton imgBtnDanhMuc;

    public FragMent_HomeListener activityCallback;

    private ProgressBar progressBar, progressBar1, progressBar2;

    public interface FragMent_HomeListener {
        void onButtonClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activityCallback = (FragMent_HomeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Lỗi phải implement FragMent_HomeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_ment__home, container, false);
        InitWidget();
        Init();
        InitSanPham();


        return view;
    }

    private void InitWidget() {
        viewPager = view.findViewById(R.id.viewpager);
        rcvSP = view.findViewById(R.id.rcvSP);
        rcvSpNoiBat = view.findViewById(R.id.rcvNB);
        rcvSPGiamGia = view.findViewById(R.id.rcvGG);
        progressBar= view.findViewById(R.id.progressbar);
        progressBar1= view.findViewById(R.id.progressbar1);
        progressBar2= view.findViewById(R.id.progressbar2);

    }

    private void Init() {
        arrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    private void InitSanPham() {
        arr_sp = new ArrayList<>();
        arr_sp_nb = new ArrayList<>();
        arr_sp_gg = new ArrayList<>();
        sanPhamPreSenter = new SanPhamPreSenter(this);
        sanPhamPreSenter.HandlegetDataSanPham();
        sanPhamPreSenter.HandlegetDataSanPhamNB();
        sanPhamPreSenter.HandlegetDataSanPhamGiamGia();
    }

    //    @Override
//    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
//        arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
//        sanPhamAdapter = new SanPhamAdapter(getContext(), arr_sp);
//        rcvSP.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//        rcvSP.setAdapter(sanPhamAdapter);
//    }
    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arr_sp.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamAdapter = new SanPhamAdapter(getContext(), arr_sp);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvSP.setHasFixedSize(true);
        rcvSP.setLayoutManager(gridLayoutManager);
        rcvSP.setAdapter(sanPhamAdapter);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arr_sp_nb.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamNBAdapter = new SanPhamAdapter(getContext(), arr_sp_nb, 2);
        rcvSpNoiBat.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcvSpNoiBat.setAdapter(sanPhamNBAdapter);
        progressBar1.setVisibility(View.GONE);
    }

    @Override
    public void getDataSanPhamGiamGia(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arr_sp_gg.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamGGAdapter = new SanPhamAdapter(getContext(), arr_sp_gg, 3);
        rcvSPGiamGia.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcvSPGiamGia.setAdapter(sanPhamGGAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void OnEmptyList() {

    }
}