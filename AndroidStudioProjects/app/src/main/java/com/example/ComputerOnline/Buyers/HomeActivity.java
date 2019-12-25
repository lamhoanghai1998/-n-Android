package com.example.ComputerOnline.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ComputerOnline.Admin.AdminMaintainProductsActivity;
import com.example.ComputerOnline.Model.Products;
import com.example.ComputerOnline.Prevalent.Prevalent;
import com.example.ComputerOnline.R;
import com.example.ComputerOnline.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gLayoutManager; // Sắp xếp
    SharedPreferences mSharePref; // Lưu việc sắp xếp

    private String type ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }


        mSharePref = getSharedPreferences( "SortSettings", MODE_PRIVATE );
        String mSorting = mSharePref.getString( "Sort","Mới nhất" );

        if(mSorting.equals( "Mới nhất" ))
        {
            gLayoutManager = new GridLayoutManager( this,2 );

            //This sẽ tải xuống sản phẩm từ dưới, Lấy sản phẩm mới nhất trước tiên
            gLayoutManager.setReverseLayout( false );
            gLayoutManager.setStackFromEnd( false );
        }

        else if(mSorting.equals( "Cũ nhất" ))
        {
            gLayoutManager = new GridLayoutManager( this,2 );
            //This sẽ tải xuống sản phẩm từ dưới, Lấy sản phẩm cũ nhất trước tiên

            gLayoutManager.setReverseLayout( false );
            gLayoutManager.setStackFromEnd( false );
        }




        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals( "Admin" )) {

                    Intent intent = new Intent( HomeActivity.this, CartActivity.class );
                    startActivity( intent );
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


        if(!type.equals( "Admin" )) {
            userNameTextView.setText( Prevalent.currentOnlineUser.getName() );
            Picasso.get().load( Prevalent.currentOnlineUser.getImage() ).placeholder( R.drawable.profile ).into( profileImageView );
        }

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(gLayoutManager);
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Đã phê duyệt"), Products.class)
                        .build();

        //Hiển thị sản phẩm lên trang chính
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText( model.getPname() );
                        holder.txtProductDescription.setText( model.getDescription() );
                        holder.txtProductPrice.setText( "Giá = " + model.getPrice() + " VNĐ" );
                        Picasso.get().load( model.getImage() ).into( holder.imageView );


                        holder.itemView.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (type.equals( "Admin" )) {
                                    Intent intent = new Intent( HomeActivity.this, AdminMaintainProductsActivity.class );
                                    intent.putExtra( "pid", model.getPid() );
                                    startActivity( intent );
                                } else {
                                    Intent intent = new Intent( HomeActivity.this, ProductDetailsActivity.class );
                                    intent.putExtra( "pid", model.getPid() );
                                    startActivity( intent );
                                }
                            }

                        } );

                    }



                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        // Các tùy chọn để hiển thị trong mục sắp xếp
        String[] sortOptions = {"Mới nhất", "Cũ nhất"};

        //Tạo thanh thông báo
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Sort by" ) //Đặt tiêu đề
            .setIcon( R.drawable.ic_action_sort ) //Đặt icon
            .setItems( sortOptions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //0 là mới nhất và 1 là cũ nhất
                    if(which==0)
                    {
                        // Sắp xếp theo mới nhất
                        //Thiết lập Shared preferences
                        SharedPreferences.Editor editor = mSharePref.edit();
                        editor.putString( "Sort", "Mới nhất" );// Sort là khóa và mới nhất là giá trị
                        editor.apply(); // Lưu giá trị vào Shared preference
                        recreate();// Khởi động lại hoạt động để lấy giá trị khác
                    }
                    else if(which==1)
                    {
                        // Sắp xếp theo cũ nhất
                        //Thiết lập Shared preferences
                        SharedPreferences.Editor editor = mSharePref.edit();
                        editor.putString( "Sort", "Cũ nhất" );// Sort là khóa và cũ nhất là giá trị
                        editor.apply(); // Lưu giá trị vào Shared preference
                        recreate();// Khởi động lại hoạt động để lấy giá trị khác
                    }
                }
            } );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //Các chức năng trong thanh menu
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
            startActivity(intent);


        }else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
