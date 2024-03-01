package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_Home;
import fpoly.md05.appduanmd05.View.FragMent.FragMent_ProFile;
import fpoly.md05.appduanmd05.View.FragMent.Fragment_Cart;
import fpoly.md05.appduanmd05.View.FragMent.Fragment_bill;
import fpoly.md05.appduanmd05.databinding.ActivityHomeBinding;
import fpoly.md05.appduanmd05.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragMent_Home());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new FragMent_Home());
                    break;
                case R.id.gioHang:
                    replaceFragment(new Fragment_Cart());
                    break;
                case R.id.hoaDon:
                    replaceFragment(new Fragment_bill());
                    break;
                case R.id.profile:
                    replaceFragment(new FragMent_ProFile());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}