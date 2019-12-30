package com.example.ComputerOnline.Sellers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ComputerOnline.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView asus, asusRog , acer, msi,gigabyte,galax,anotherVga,radeon,eVga;
    private ImageView ryzen,intel;
    private Button backtoSellerHomeBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_seller_product_category );


//


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

        backtoSellerHomeBtn = (Button) findViewById( R.id.seller_logout_btn ) ;

//

        backtoSellerHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        asus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent( SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Asus");
                startActivity(intent);
            }
        });


        asusRog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Asus Rog");
                startActivity(intent);
            }
        });

        acer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Acer");
                startActivity(intent);
            }
        });


//


        msi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
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
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Gigabyte");
                startActivity(intent);
            }
        });

        galax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Gigabyte");
                startActivity(intent);
            }
        });

        eVga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this,SellerAddNewProductActivity.class);
                intent.putExtra("category", "Evga");
                startActivity(intent);
            }
        });

        intel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Intel");
                startActivity(intent);
            }
        });


        ryzen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Ryzen");
                startActivity(intent);
            }
        });

        anotherVga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
                intent.putExtra("category", "Another Vga");
                startActivity(intent);
            }
        });

    }
}
