package com.example.ComputerOnline.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ComputerOnline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {


    private Button loginSellerBtn;
    private EditText  emailInput, passwordInput;
    private CheckBox showPasswordCk;


    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_seller_login );

        mAuth = FirebaseAuth.getInstance();


        emailInput = findViewById( R.id.seller_login_email );
        passwordInput = findViewById( R.id.seller_login_password);
        loginSellerBtn = findViewById( R.id.seller_login_btn );
        showPasswordCk = findViewById( R.id.show_password_ck );
        loadingBar = new ProgressDialog(this);


        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginSeller();
            }
        });

        showPasswordCk.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    passwordInput.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                }
                else
                {
                    passwordInput.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                }
            }
        } );
    }

    private void loginSeller() {

         final String email = emailInput.getText().toString();
         final String password =passwordInput.getText().toString();


        if( !email.equals( "" )  && !password.equals( "" )) {

            loadingBar.setTitle( "Truy cập vào tài khoản kinh doanh" );
            loadingBar.setMessage( "Chờ chút!!! Hệ thống đang xét duyệt tài khoản." );
            loadingBar.setCanceledOnTouchOutside( false );
            loadingBar.show();

            mAuth.signInWithEmailAndPassword( email, password )
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(SellerLoginActivity.this, "Đăng nhập tài khoản thành công!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } );
        }
        else
        {
            Toast.makeText( this, "Vui long dien day du thong tin dang nhap!!", Toast.LENGTH_SHORT ).show();
        }

    }



}
