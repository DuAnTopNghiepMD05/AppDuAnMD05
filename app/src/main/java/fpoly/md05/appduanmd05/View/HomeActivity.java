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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.Account.SignInActivity;
import fpoly.md05.appduanmd05.View.Bill.CartActivity;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_Home;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_ProFile;
import fpoly.md05.appduanmd05.View.FragMent.Fragment_bill;

public class HomeActivity extends AppCompatActivity implements FragMent_Home.FragMent_HomeListener {
    private NavigationView navigationView;

    private BottomNavigationView navigationView2;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle toggle;

    private Fragment fm;

    private FirebaseAuth firebaseAuth;

    private EditText editsearch;

    private TextView tvusername,tvemail;

    private CircleImageView imaProfile;

    public static CountDownTimer countDownTimer;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_home);
        InitWidget();
        Init();
        setProFile();
    }

    private void setProFile() {

    }

    private void InitWidget() {
        navigationView = findViewById(R.id.navigationview);
        View headerLayout = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        navigationView2 = findViewById(R.id.navigationview2);
        drawerLayout= findViewById(R.id.drawerlayout);
        editsearch = findViewById(R.id.editSearch);
//        tvusername = headerLayout.findViewById(R.id.tvusername);
//        tvemail =headerLayout. findViewById(R.id.tvemail);
//        imaProfile =headerLayout. findViewById(R.id.profile_image);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
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
                switch (item.getItemId()){
                    case  R.id.home: fm = new FragMent_Home();break;
                    case  R.id.dangnhap:startActivity(new Intent( HomeActivity.this, SignInActivity.class));break;
                    case  R.id.your_bill:fm=new Fragment_bill();break;
                    case  R.id.your_cart:startActivity(new Intent(HomeActivity.this, CartActivity.class));break;
                    case  R.id.your_profile:fm = new FragMent_ProFile();break;
                    case  R.id.signout:FirebaseAuth.getInstance().signOut();startActivity(new Intent(HomeActivity.this,SignInActivity.class));finish();break;
                    case R.id.danhmuc: startActivity(new Intent( HomeActivity.this,DanhMucActivity.class));break;
                }
                if (fm != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.home: fm = new FragMent_Home();break;
                    case R.id.danhmuc: startActivity(new Intent( HomeActivity.this,DanhMucActivity.class));break;
                    case  R.id.your_cart:startActivity(new Intent(HomeActivity.this, CartActivity.class));break;
                    case  R.id.lienhe:startActivity(new Intent( HomeActivity.this, ContactActivity.class));break;
                }
                if (fm != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();
                }
                drawerLayout.closeDrawers();
                return true;
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
    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(HomeActivity.this, DanhMucActivity.class);
        startActivity(intent);
    }
}