package com.example.ComputerOnline.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComputerOnline.Buyers.MainActivity;
import com.example.ComputerOnline.Model.Products;
import com.example.ComputerOnline.R;
import com.example.ComputerOnline.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentHome = new Intent( SellerHomeActivity.this, SellerHomeActivity.class );
                    return true;

                case R.id.navigation_add:
                    Intent intentCate = new Intent( SellerHomeActivity.this, SellerProductCategoryActivity.class );
                    startActivity( intentCate );
                    return true;

                case R.id.navigation_logout:

                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();


                    Intent intentMain = new Intent( SellerHomeActivity.this, MainActivity.class );
                    intentMain.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity( intentMain );
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child( "Products" );

        recyclerView = findViewById( R.id.seller_home_list );
        recyclerView.setHasFixedSize( true );
        layoutManager = new GridLayoutManager( this,2 );
        recyclerView.setLayoutManager( layoutManager );
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery( unverifiedProductsRef.orderByChild( "sid" ).equalTo( FirebaseAuth.getInstance().getCurrentUser() .getUid()), Products.class )
                        .build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText( model.getPname() );
                        holder.txtProductDescription.setText( model.getDescription() );
                        holder.txtProductPrice.setText( "Giá :"+model.getPrice() +" VNĐ" );
                        holder.txtProductStatus.setText("Trạng thái: " +model.getProductState() );
                        Picasso.get().load( model.getImage() ).into( holder.imageView );


                        holder.itemView.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String productID = model.getPid();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Có",
                                                "Không"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder( SellerHomeActivity.this );
                                builder.setTitle( "Bạn có muốn xóa linh kiện đăng bán này!!" );
                                builder.setItems( options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        if(position == 0)
                                        {
                                            deleteProduct(productID);
                                        }
                                        if(position == 1)
                                        {

                                        }
                                    }
                                } );
                                builder.show();

                            }
                        } );


                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter( adapter );
        adapter.startListening();
    }

    private void deleteProduct(String productID) {
        unverifiedProductsRef.child( productID )
                .removeValue()
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText( SellerHomeActivity.this, "Xóa sim đăng bán thành công", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }
}
