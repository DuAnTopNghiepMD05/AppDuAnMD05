package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.Bill.ContentBillActivity;

public class HoanThanhActivity extends AppCompatActivity {
    Button btnTTMS,btnXDH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoan_thanh);
        btnTTMS=findViewById(R.id.btnTTMS);
        btnXDH=findViewById(R.id.btnXDH);

        btnTTMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HoanThanhActivity.this,HomeActivity.class));
            }
        });
        btnXDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HoanThanhActivity.this, ContentBillActivity.class));
            }
        });//

    }
}