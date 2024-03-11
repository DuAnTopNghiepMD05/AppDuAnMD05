package fpoly.md05.appduanmd05.Adapter;

import android.content.Context;
import android.content.Intent;
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
import fpoly.md05.appduanmd05.Presenter.SetOnItemClick;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.Bill.ContentProDuctActivity;

public class SanPhamAdapter  extends RecyclerView.Adapter<SanPhamAdapter.ViewHodler> {
    private Context context;
    private ArrayList<SanPhamModels> arrayList;
    private  int type = 0;

    public SanPhamAdapter(Context context, ArrayList<SanPhamModels> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    public SanPhamAdapter(Context context, ArrayList<SanPhamModels> arrayList,int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.type= type;
    }

    @NonNull
    @Override
    public SanPhamAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(type==0){  //type = 0 là sản phẩm thông thường
            view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham,parent,false);
        }else if(type ==2){  //type = 2 là sản phẩm nổi bật
            view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham,parent,false);
        }else if(type ==3){  //type = 3 là sản phẩm thức uống
            view = LayoutInflater.from(context).inflate(R.layout.dong_sanpham,parent,false);
        }else{  //type = 1 là sản phẩm trong giỏ hàng
            view = LayoutInflater.from(context).inflate(R.layout.dong_giohang,parent,false);
        }


        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.ViewHodler holder, int position) {

        SanPhamModels sanPhamModels = arrayList.get(position);

        holder.txttensp.setText(sanPhamModels.getTensp());

        holder.txtgiasp.setText(NumberFormat.getInstance().format(sanPhamModels.getGiatien())+" Đ"); //format giá tiền
        Picasso.get().load(sanPhamModels.getHinhanh()).into(holder.hinhanh); //load hình ảnh
        holder.SetOnItem(new SetOnItemClick() {
            @Override
            //chi tiet san phẩm
            public void SetItemClick(View view, int pos) {
                Intent intent = new Intent(context, ContentProDuctActivity.class);
                intent.putExtra("SP",sanPhamModels);
                context.startActivity(intent);
            }
        });
//        if(type==1){ //type = 1 là sản phẩm trong giỏ hàng
//            holder.txtmausac.setText(sanPhamModels.getMausac()); //set màu sắc
//            holder.txtsoluong.setText(sanPhamModels.getSoluong()+"");  //set số lượng
//        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView txttensp,txtgiasp,txtmausac,txtsoluong;
        ImageView hinhanh;
        SetOnItemClick itemClick;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            txtgiasp= itemView.findViewById(R.id.txtgiatien);
            txttensp= itemView.findViewById(R.id.txttensp);
            hinhanh= itemView.findViewById(R.id.hinhanh);
            if(type==1){
                txtmausac = itemView.findViewById(R.id.txtbaohanh);
                txtsoluong = itemView.findViewById(R.id.txtsoluong);
            }
            itemView.setOnClickListener(this);
        }
        public  void  SetOnItem(SetOnItemClick itemClick){
            this.itemClick = itemClick;
        }

        @Override
        public void onClick(View v) {
            itemClick.SetItemClick(v,getAdapterPosition());
        }
    }
}