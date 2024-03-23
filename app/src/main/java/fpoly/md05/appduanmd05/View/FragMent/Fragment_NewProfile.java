package fpoly.md05.appduanmd05.View.FragMent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.Account.SignInActivity;
import fpoly.md05.appduanmd05.View.ActivityProfile;
import fpoly.md05.appduanmd05.View.HomeActivity;
import fpoly.md05.appduanmd05.View.SettingActivity;


public class Fragment_NewProfile extends Fragment {

    private LinearLayout flCuaHang, flCaiDat, flDiaChi, DonHang, flThongTin;
    private FirebaseFirestore db;

    TextView tvusername, tvemail;

    ImageView imaProfile;

    Button btnLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_frament_profile, container, false);

        // Ánh xạ
        flCuaHang = view.findViewById(R.id.fl_cuahang);
        flCaiDat = view.findViewById(R.id.fl_caidat);
        flDiaChi = view.findViewById(R.id.fl_diachi);
        DonHang = view.findViewById(R.id.fl_donhang);
        flThongTin = view.findViewById(R.id.fl_thongtincanhan);
        tvusername = view.findViewById(R.id.tvhoten);
        tvemail = view.findViewById(R.id.fl_email);
        imaProfile = view.findViewById(R.id.fl_avatar);
        btnLogout = view.findViewById(R.id.Profile_btnLogout);

        setProFile();



        // Sự kiện click cho Cửa hàng của bạn
        flCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        flThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityProfile.class);
                startActivity(intent);
            }
        });

        flCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void setProFile() {
        db = FirebaseFirestore.getInstance();
        tvemail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        db.collection("thongtinUser").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Profile")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            if (documentSnapshot != null) {
                                try {
                                    tvusername.setText(documentSnapshot.getString("hoten").length() > 0 ?
                                            documentSnapshot.getString("hoten") : "");

                                    if (documentSnapshot.getString("avatar").length() > 0) {
                                        Picasso.get().load(documentSnapshot.getString("avatar").trim()).into(imaProfile);
                                    }
                                } catch (Exception e) {
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        }
                    }
                });
    }

}
