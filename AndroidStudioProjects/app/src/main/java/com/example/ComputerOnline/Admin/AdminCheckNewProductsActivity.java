package com.example.ComputerOnline.Admin;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ComputerOnline.Model.Products;
import com.example.ComputerOnline.R;
import com.example.ComputerOnline.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckNewProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_check_new_products );


        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child( "Products" );

        recyclerView = findViewById( R.id.admin_products_checklist );
        recyclerView.setHasFixedSize( true );
        layoutManager = new GridLayoutManager(  this,2);
        recyclerView.setLayoutManager( layoutManager );
    }


    @Override
    protected void onStart() {
        super.onStart();

         FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery( unverifiedProductsRef.orderByChild( "productState" ).equalTo( "Chưa phê duyệt" ), Products.class )
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText( model.getPname() );
                        holder.txtProductDescription.setText( model.getDescription() );
                        holder.txtProductPrice.setText( "Giá :"+model.getPrice() +"VNĐ" );
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
                                AlertDialog.Builder builder = new AlertDialog.Builder( AdminCheckNewProductsActivity.this );
                                builder.setTitle( "Bạn có muốn xét duyệt sản phẩm của người bán!!" );
                                builder.setItems( options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        if(position == 0)
                                        {
                                            changeProductsState(productID);
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
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter( adapter );
        adapter.startListening();
    }

    private void changeProductsState(String productID) {

        unverifiedProductsRef.child( productID )
                .child( "productState" )
                .setValue( "Đã phê duyệt" )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText( AdminCheckNewProductsActivity.this, "Sản phẩm đã được xét duyệt và sẵn sàng để bán", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }


}
