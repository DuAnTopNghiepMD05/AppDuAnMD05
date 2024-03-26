package fpoly.md05.appduanmd05.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

        void onUpdateTotalAmount(long totalAmount);
    }

    private AdapterCallback callback;

    public void setCallback(AdapterCallback callback) {
        this.callback = callback;
    }

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

    public void removeItem(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
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
                sanPhamModels.setSoluong(sanPhamModels.getSoluong() + 1);
                Log.d("soluong", "onBindViewHolder: "+sanPhamModels.getSoluong());
                notifyItemChanged(position);

                updateSoLuongSanPham(sanPhamModels.getId(), sanPhamModels.getSoluong());
                // Giảm số lượng sản phẩm trong bảng SanPham
//                updateSoLuongSanPhamTrongSanPham(sanPhamModels.getIdsp(), -1);

        });

        holder.tru.setOnClickListener(view -> {
            if (sanPhamModels.getSoluong() > 1) {
                sanPhamModels.setSoluong(sanPhamModels.getSoluong() - 1);
                Log.d("soluong", "onBindViewHolder: "+sanPhamModels.getSoluong());
                notifyItemChanged(position);
                updateSoLuongSanPham(sanPhamModels.getId(), sanPhamModels.getSoluong());
                // Tăng số lượng sản phẩm trong bảng SanPham
//                updateSoLuongSanPhamTrongSanPham(sanPhamModels.getIdsp(), 1);
            }
        });


    }

    private void calculateTotalAmount() {
        long totalAmount = 0;
        for (SanPhamModels sanPham : arrayList) {
            totalAmount += sanPham.getGiatien() * sanPham.getSoluong();
        }
        // Gọi phương thức callback để thông báo về sự thay đổi tổng tiền
        if (callback != null) {
            callback.onUpdateTotalAmount(totalAmount);
        }
    }



    private void updateSoLuongSanPham(String idSanPham, long soLuongMoi) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            // Lấy tham chiếu đến document của sản phẩm trong giỏ hàng của người dùng
            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("GioHang")
                    .document(userId)
                    .collection("ALL")
                    .document(idSanPham);

            // Cập nhật số lượng mới
            docRef.update("soluong", soLuongMoi)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Cập nhật số lượng thành công"+ soLuongMoi);
                        Toast.makeText(context, "Cập nhật số lượng thành công", Toast.LENGTH_SHORT).show();
                        if (callback != null) {
                            callback.onUpdateSoLuongSanPham(idSanPham, soLuongMoi);

                        }
                    })
                    .addOnFailureListener(e ->
                            Log.e("FirestoreError", "Lỗi cập nhật số lượng: " + e.getMessage()));
        } else {
            Toast.makeText(context, "Người dùng chưa đăng nhập", Toast.LENGTH_LONG).show();
        }
    }



    private void updateSoLuongSanPhamTrongSanPham(String idSanPham, long soLuongThayDoi) {
        DocumentReference sanPhamRef = FirebaseFirestore.getInstance()
                .collection("SanPham")
                .document(idSanPham);

        FirebaseFirestore.getInstance().runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(sanPhamRef);
                    if (snapshot.exists()) {
                        long soluongHienTai = snapshot.getLong("soluong");
                        long soluongMoi = soluongHienTai + soLuongThayDoi;
                        transaction.update(sanPhamRef, "soluong", soluongMoi);
                    }
                    return null;
                }).addOnSuccessListener(result -> Log.d("Firestore", "Transaction success"))
                .addOnFailureListener(e -> Log.e("FirestoreError", "Transaction failed: " + e.getMessage()));
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
