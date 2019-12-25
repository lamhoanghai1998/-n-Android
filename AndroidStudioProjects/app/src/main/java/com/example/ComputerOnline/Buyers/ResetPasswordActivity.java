package com.example.ComputerOnline.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ComputerOnline.Prevalent.Prevalent;
import com.example.ComputerOnline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber,question1, question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reset_password );

        check = getIntent().getStringExtra( "check" );


        pageTitle = findViewById( R.id.page_title );
        titleQuestions = findViewById( R.id.title_questions );
        phoneNumber = findViewById( R.id.find_phone_number );
        question1 =  findViewById( R.id.question_1 );
        question2 =  findViewById( R.id.question_2);
        verifyButton =  findViewById( R.id.verify_btn );

        verifyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        } );
    }

    @Override
    protected void onStart(){
        super.onStart();

        phoneNumber.setVisibility( View.GONE );

        if(check.equals( "settings" ))
        {
            pageTitle.setText( "Đặt câu hỏi" );
            titleQuestions.setText( "Vui lòng đặt câu trả lời" );
            verifyButton.setText( "Đặt" );

            displayPreviousAnswers();
            
            verifyButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswers();
                }
            } );
        }
        else if(check.equals( "login" ))
        {
            phoneNumber.setVisibility( View.VISIBLE );

            verifyButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            } );
        }
    }

    private void setAnswers(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals( "" ))
        {
            Toast.makeText( ResetPasswordActivity.this, "Vui lòng trả lời cả hai câu", Toast.LENGTH_SHORT ).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child( "Users" )
                    .child( Prevalent.currentOnlineUser.getPhone() );

            final HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren( userdataMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText( ResetPasswordActivity.this, "Ban da co cau hoi bao mat!!", Toast.LENGTH_SHORT ).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, SettinsActivity.class);
                        startActivity(intent);
                    }
                }
            } );
        }
    }

    private void displayPreviousAnswers(){
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child( "Users" )
                .child( Prevalent.currentOnlineUser.getPhone() );

        ref.child( "Security Questions" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if(dataSnapshot.child( "Users" ).exists()){
                    String ans1 =   dataSnapshot.child( "Sercurity Questions" ).child( "Answer1" ).getValue() .toString();
                    String ans2 =   dataSnapshot.child( "Sercurity Questions" ).child( "Answer2" ).getValue() .toString();

                    question1.setText( ans1 );
                    question2.setText( ans2 );

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }




    private void verifyUser()
    {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();


        final DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child( "Users" )
                .child( Prevalent.currentOnlineUser.getPhone() );


        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mPhone = dataSnapshot.child( "phone" ).getValue().toString();

                if(phone.equals( mPhone ) || phone.equals(Prevalent.currentOnlineUser.getPhone() ))
                {
                    if(dataSnapshot.hasChild( "Security Questions" ))
                    {
                        String ans1 = dataSnapshot.child("Security Questions").child( "answer1" ).getValue().toString();
                        String ans2 = dataSnapshot.child("Security Questions").child( "answer2" ).getValue().toString();

                        if(!ans1.equals( answer1 ))
                        {
                            Toast.makeText( ResetPasswordActivity.this, "", Toast.LENGTH_SHORT ).show();
                        }
                        else if(!ans2.equals( answer2 ))
                        {
                            Toast.makeText( ResetPasswordActivity.this, "", Toast.LENGTH_SHORT ).show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder( ResetPasswordActivity.this );
                            builder.setTitle( "New Password" );

                            final EditText newPassword = new EditText( ResetPasswordActivity.this );
                            newPassword.setHint( "Điền mật khẩu tại đây" );
                            builder.setView( newPassword );

                            builder.setPositiveButton( "Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!newPassword.getText().toString().equals( ""))
                                    {
                                        ref.child( "password" )
                                                .setValue( newPassword.getText().toString() )
                                                .addOnCompleteListener( new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText( ResetPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT ).show();
                                                        }
                                                    }
                                                } );
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.cancel();
                            }
                        });

                            builder.show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Vui long điền câu hỏi bảo mật", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ResetPasswordActivity.this, "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

}
