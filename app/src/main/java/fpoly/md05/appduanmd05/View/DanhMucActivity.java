package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.FragMent.Danhmuc.Fragment_Shirt;
import fpoly.md05.appduanmd05.View.FragMent.Danhmuc.Fragment_pants;
import fpoly.md05.appduanmd05.View.FragMent.Danhmuc.Fragment_shoe;

public class DanhMucActivity extends AppCompatActivity {
    private ImageView ivShirt, ivShoe, ivPant;
    private Fragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);
        ivShirt = findViewById(R.id.danhmuc_ivShirt);
        ivPant = findViewById(R.id.danhmuc_ivPants);
        ivShoe = findViewById(R.id.danhmuc_ivShoe);


        ivShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm = new Fragment_Shirt();
                getSupportFragmentManager().beginTransaction().replace(R.id.danhmuc_frameLayout, fm).commit();
            }
        });

        ivPant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm = new Fragment_pants();
                getSupportFragmentManager().beginTransaction().replace(R.id.danhmuc_frameLayout, fm).commit();
            }
        });

        ivShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm = new Fragment_shoe();
                getSupportFragmentManager().beginTransaction().replace(R.id.danhmuc_frameLayout, fm).commit();
            }
        });
    }
}