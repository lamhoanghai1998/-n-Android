package com.example.ComputerOnline.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComputerOnline.Admin.AdminCheckNewProductsActivity;
import com.example.ComputerOnline.Model.Cart;
import com.example.ComputerOnline.Model.Products;
import com.example.ComputerOnline.Prevalent.Prevalent;
import com.example.ComputerOnline.R;
import com.example.ComputerOnline.ViewHolder.CartViewHolder;
import com.example.ComputerOnline.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn,logOutBtn;
    private TextView txtTotalAmount, txtMsg1;


    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button) findViewById(R.id.next_btn);
        logOutBtn = (Button) findViewById(R.id.cart_logout_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);

        logOutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCart = new Intent( CartActivity.this, HomeActivity.class );
                intentCart.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity( intentCart );
                finish();
            }
        } );

        // Them su kien cho nút Thêm sim vào giỏ
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTotalAmount.setText( "Total Price = $" + String.valueOf( overTotalPrice ) );
                CharSequence options[] = new CharSequence[]
                        {
                                "Chơi",
                                "Thôi"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder( CartActivity.this );
                builder.setTitle( "Tổng tiền thanh toán của là "+ overTotalPrice+ " VNĐ");
                builder.setItems( options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                          if (overTotalPrice == 0)
                        {
                            Toast.makeText( CartActivity.this, "Hãy kiểm tra giỏ của bạn kỹ càng trước khi thanh toán!!", Toast.LENGTH_SHORT ).show();
                        }
                       else if(position == 1)
                        {
                            Toast.makeText( CartActivity.this, "Vui lòng đặt một linh kiện trước khi thanh toán", Toast.LENGTH_SHORT ).show();
                        }
                       else if(position == 0)
                        {
                            Intent intent = new Intent( CartActivity.this, ConfirmFinalOrderActivity.class );
                            intent.putExtra( "Total Price", String.valueOf( overTotalPrice ) );
                            startActivity( intent );
                            finish();
                        }

                    }
                } );
                builder.show();

//                if (overTotalPrice == 0) {
//                    Toast.makeText( CartActivity.this, "Vui lòng đặt một linh kiện trước khi thanh toán", Toast.LENGTH_SHORT ).show();
//                }
//                else {
//                    Intent intent = new Intent( CartActivity.this, ConfirmFinalOrderActivity.class );
//                    intent.putExtra( "Total Price", String.valueOf( overTotalPrice ) );
//                    startActivity( intent );
//                    finish();
//                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class)
                                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

//            CartViewHolder
//            Hiển thị sim trong giỏ hàng
//             CHỉnh sửa giỏ hàng
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Số lượng = " + model.getQuantity());
                holder.txtProductPrice.setText("Giá " + model.getPrice() + " VNĐ");
                holder.txtProductName.setText(model.getPname());
                Picasso.get().load( model.getImage() ).into( holder.cartImageView );

                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Sửa sản phẩm",
                                        "Xóa sản phẩm"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Tùy chỉnh giỏ hàng:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Sản phẩm đã được xóa!!.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }





    //Tạo chức năng kiểm duyệt đơn hàng đã ship chưa
    private void CheckOrderState() {
        DatabaseReference odersRef;
        odersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        odersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        txtTotalAmount.setText(("Than chao " + userName + "\n Đơn hàng đã chuyển đến cho bạn  !!."));
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Chúc mừng!! Đơn hàng của bạn đã được đặt. Hãy chờ đơn vận chuyển đến bạn nhé! ");
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Bạn có thể đặt thêm linh kiện khi đơn hàng đã được giao!!", Toast.LENGTH_SHORT).show();
                    } else if (shippingState.equals("not shipped")) {
                        txtTotalAmount.setText("Trạng thái: Chưa giao! ");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.GONE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Bạn có thể thanh toán thêm sản phẩm nếu đơn hàng được giao đến cho bạn!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {


            }

            });
        }
    }



