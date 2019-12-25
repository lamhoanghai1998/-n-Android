package com.example.ComputerOnline.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ComputerOnline.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView asus, asusRog , acer, msi,gigabyte,galax,anotherVga,radeon,eVga;
    private ImageView ryzen,intel;
    private Button BacktoAdminHomeBtn;
//    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
//    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_admin_category);

        BacktoAdminHomeBtn = (Button) findViewById(R.id.admin_logout_btn);





        BacktoAdminHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });




        asus = (ImageView) findViewById(R.id.asus);
        asusRog = (ImageView) findViewById(R.id.asus_rog);
        msi = (ImageView) findViewById(R.id.msi);
        acer = (ImageView) findViewById(R.id.acer);

        anotherVga = (ImageView) findViewById(R.id.another_vga);
        intel = (ImageView) findViewById(R.id.intel);
        ryzen = (ImageView) findViewById(R.id.ryzen_chip);
        eVga = (ImageView) findViewById(R.id.evga);

        gigabyte = (ImageView) findViewById(R.id.giga);
        galax = (ImageView) findViewById(R.id.galax);



        asus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Asus");
                startActivity(intent);
            }
        });


        asusRog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Asus Rog");
                startActivity(intent);
            }
        });

        acer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Acer");
                startActivity(intent);
            }
        });


//


        msi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "MSI");
                startActivity(intent);
            }
        });

        galax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "MSI");
                startActivity(intent);
            }
        });

//
//
//
        gigabyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Gigabyte");
                startActivity(intent);
            }
        });

        eVga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Evga");
                startActivity(intent);
            }
        });

        intel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Intel");
                startActivity(intent);
            }
        });


        ryzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Ryzen");
                startActivity(intent);
            }
        });

        anotherVga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Another Vga");
                startActivity(intent);
            }
        });
    }
}
