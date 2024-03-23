package fpoly.md05.appduanmd05.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.Account.SignInActivity;
import fpoly.md05.appduanmd05.View.Bill.CartActivity;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_Home;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_ProFile;
import fpoly.md05.appduanmd05.View.FragMent.Fragment_NewProfile;
import fpoly.md05.appduanmd05.View.FragMent.Fragment_bill;

public class HomeActivity extends AppCompatActivity implements FragMent_Home.FragMent_HomeListener {

    private NavigationView navigationView;
    private BottomNavigationView navigationView2;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Fragment fm;
    private FirebaseAuth firebaseAuth;
    private TextView editsearch;
    private TextView tvusername, tvemail;
    private CircleImageView imaProfile;
    public static CountDownTimer countDownTimer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitWidget();
        Init();
        setProFile();
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

    private void InitWidget() {
        navigationView = findViewById(R.id.navigationview);
        View headerLayout = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        navigationView2 = findViewById(R.id.navigationview2);
        drawerLayout = findViewById(R.id.drawerlayout);
        editsearch = findViewById(R.id.txttimkiem);
        tvusername = headerLayout.findViewById(R.id.tvusername);
        tvemail = headerLayout.findViewById(R.id.tvemail);
        imaProfile = headerLayout.findViewById(R.id.profile_image);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    private void Init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        toggle.syncState();

        fm = new FragMent_Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fm = new FragMent_Home();
                        break;
                    case R.id.dangnhap:
                        startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                        break;
                    case R.id.your_bill:
                        fm = new Fragment_bill();
                        break;
                    case R.id.your_cart:
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        break;
                    case R.id.your_profile:
                        fm = new Fragment_NewProfile();
                        break;
                    case R.id.signout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, SignInActivity.class));
                        finish();
                        break;
                    case R.id.danhmuc:
                        startActivity(new Intent(HomeActivity.this, DanhMucActivity.class));
                        break;
                    case R.id.thongtinungdung:
                        startActivity(new Intent(HomeActivity.this, ThongTinUngDung.class));
                        break;
                }
                if (fm != null) {
                    replaceFragment(fm);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fm = new FragMent_Home();
                        break;
                    case R.id.danhmuc:
                        startActivity(new Intent(HomeActivity.this, DanhMucActivity.class));
                        break;
                    case R.id.your_cart:
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                        break;
                    case R.id.lienhe:
                        fm = new Fragment_NewProfile();
                        break;
                }
                if (fm != null) {
                    replaceFragment(fm);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        editsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProFile();
    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(HomeActivity.this, DanhMucActivity.class);
        startActivity(intent);
    }
//    public void getToken(){
//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if(!TextUtils.isEmpty(task))
//            }
//        })
//    }
}

