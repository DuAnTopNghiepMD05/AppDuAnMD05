package fpoly.md05.appduanmd05.View.FragMent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.HoaDonAdapter;
import fpoly.md05.appduanmd05.Model.HoaDonModels;
import fpoly.md05.appduanmd05.Presenter.HoaDonPreSenter;
import fpoly.md05.appduanmd05.Presenter.HoaDonView;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.HomeActivity;


public class Fragment_bill extends Fragment implements HoaDonView {
    View view;
    private RecyclerView rcvBill;

    private HoaDonPreSenter hoaDonPreSenter;
    private HoaDonAdapter hoaDonAdapter;
    private ArrayList<HoaDonModels> arrayList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bill, container, false);

        rcvBill = view.findViewById(R.id.rcvBill);
        progressBar = view.findViewById(R.id.progressbar);
        hoaDonPreSenter = new HoaDonPreSenter(this);
        arrayList = new ArrayList<>();

        hoaDonPreSenter.HandleReadDataHD();
        HomeActivity.countDownTimer = new CountDownTimer(1, 1) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                arrayList.clear();;
                if(hoaDonAdapter!=null){
                    hoaDonAdapter.notifyDataSetChanged();
                }
                hoaDonPreSenter.HandleReadDataHD();
                HomeActivity.countDownTimer.cancel();
            }
        };


        return view;
    }

    @Override
    public void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long type) {
        arrayList.add(new HoaDonModels(id,uid,diachi,hoten,ngaydat,phuongthuc,sdt,tongtien,type));
        hoaDonAdapter = new HoaDonAdapter(getContext(),arrayList);
        rcvBill.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvBill.setAdapter(hoaDonAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void OnFail() {

    }

    @Override
    public void OnSucess() {

    }
}