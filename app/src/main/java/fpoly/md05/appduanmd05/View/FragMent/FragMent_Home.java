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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.BannerAdapter;
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

    private BannerAdapter bannerAdapter;

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
        progressBar = view.findViewById(R.id.progressbar);
        progressBar1 = view.findViewById(R.id.progressbar1);
        progressBar2 = view.findViewById(R.id.progressbar2);

    }

    private void Init() {
        arrayList = new ArrayList<>();// Khai báo viewPager (giả sử đã được khai báo trước đó)

        // Khởi tạo tham chiếu tới Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Thực hiện truy vấn để lấy dữ liệu từ bảng "Banner"
        db.collection("Banner").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Duyệt qua tất cả các tài liệu trong kết quả truy vấn
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Với mỗi tài liệu, lấy URL hình ảnh của banner và thêm vào danh sách
                    for (int i = 1; ; i++) {
                        String imageUrlFieldName = "hinhanh" + i;
                        if (document.contains(imageUrlFieldName)) {
                            String imageUrl = document.getString(imageUrlFieldName);
                            arrayList.add(imageUrl);
                        } else {
                            // Nếu không tìm thấy trường có tên "hinhanh" + i, thoát khỏi vòng lặp
                            break;
                        }
                    }
                }
                // Sau khi lấy được danh sách URL hình ảnh, tạo adapter và thiết lập cho viewPager
                bannerAdapter = new BannerAdapter(getContext(), arrayList);
                viewPager.setAdapter(bannerAdapter);

                // Tự động chuyển đổi giữa các banner sau một khoảng thời gian
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int nextItem = currentItem + 1;
                        if (nextItem >= arrayList.size()) {
                            nextItem = 0; // Quay lại banner đầu tiên nếu đang ở banner cuối cùng
                        }
                        viewPager.setCurrentItem(nextItem, true);
                        handler.postDelayed(this, 3000); // Tự động chạy lại sau 3 giây
                    }
                }, 3000); // Khởi đầu sau 3 giây
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý trường hợp lấy dữ liệu thất bại (nếu cần)
                Log.e("FetchData", "Error fetching data: " + e.getMessage());
            }
        });
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