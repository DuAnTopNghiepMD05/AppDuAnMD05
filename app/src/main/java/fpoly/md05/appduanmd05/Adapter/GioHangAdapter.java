package fpoly.md05.appduanmd05.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.GioHangPreSenter;
import fpoly.md05.appduanmd05.Presenter.SetOnItemClick;
import fpoly.md05.appduanmd05.R;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHodler> {
    private Context context;

    private ArrayList<SanPhamModels> arrayList;

    private int type = 0;

    public interface AdapterCallback {
        void onUpdateSoLuongSanPham(String idSanPham, long soLuongMoi);
    }

    private AdapterCallback callback;

    public GioHangAdapter(Context context, ArrayList<SanPhamModels> arrayList, AdapterCallback callback) {
        this.context = context;
        this.arrayList = arrayList;
        this.callback = callback;
    }


    public GioHangAdapter(Context context, ArrayList<SanPhamModels> arrayList, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type = type;
    }

    public GioHangAdapter(Context context, ArrayList<SanPhamModels> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GioHangAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(type==0){
            view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham,parent,false);
        }else if(type ==2){
            view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham_noibat,parent,false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.dong_giohang,parent,false);
        }
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.ViewHodler holder, int position) {
        SanPhamModels sanPhamModels = arrayList.get(position);

        holder.txttensp.setText(sanPhamModels.getTensp());

        holder.txtgiasp.setText(NumberFormat.getInstance().format(sanPhamModels.getGiatien())+" Đ");
        Picasso.get().load(sanPhamModels.getHinhanh()).into(holder.hinhanh);
        holder.SetOnItem(new SetOnItemClick() {
            @Override
            //chi tiet san phẩm
            public void SetItemClick(View view, int pos) {

            }
        });
        if(type==1){
            holder.txtbaohanh.setText(sanPhamModels.getMausac());
            holder.txtsoluong.setText(sanPhamModels.getSoluong()+"");
        }

        holder.cong.setOnClickListener(view -> {
            // Tăng số lượng và cập nhật giao diện
            sanPhamModels.setSoluong(sanPhamModels.getSoluong() + 1);
            notifyItemChanged(position);
            // Gọi phương thức để cập nhật số lượng sản phẩm trên Firebase
            updateSoLuongSanPham(sanPhamModels.getIdsp(), sanPhamModels.getSoluong());
        });

        holder.tru.setOnClickListener(view -> {
            if (sanPhamModels.getSoluong() > 1) {
                // Giảm số lượng và cập nhật giao diện
                sanPhamModels.setSoluong(sanPhamModels.getSoluong() - 1);
                notifyItemChanged(position);
                // Gọi phương thức để cập nhật số lượng sản phẩm trên Firebase
                updateSoLuongSanPham(sanPhamModels.getIdsp(), sanPhamModels.getSoluong());
            }
        });



    }

    private void updateSoLuongSanPham(String idSanPham, long soLuongMoi) {
        // Gọi phương thức của activity hoặc presenter để thực hiện cập nhật số lượng sản phẩm trên Firebase
        if (callback != null) {
            callback.onUpdateSoLuongSanPham(idSanPham, soLuongMoi);
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txttensp,txtgiasp,txtbaohanh,txtsoluong;
        ImageView hinhanh, cong , tru;
        SetOnItemClick itemClick;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            txtgiasp= itemView.findViewById(R.id.txtgiatien);
            txttensp= itemView.findViewById(R.id.txttensp);
            hinhanh= itemView.findViewById(R.id.hinhanh);
            cong = itemView.findViewById(R.id.btnCong);
            tru = itemView.findViewById(R.id.btnTru);
            if(type==1){
                txtbaohanh = itemView.findViewById(R.id.txtMauSac);
                txtsoluong = itemView.findViewById(R.id.txtsoluong);
            }
            itemView.setOnClickListener(this);

        }

        public  void  SetOnItem(SetOnItemClick itemClick){
            this.itemClick = itemClick;
        }

        @Override
        public void onClick(View view) {
            itemClick.SetItemClick(view,getAdapterPosition());
        }
    }
}
