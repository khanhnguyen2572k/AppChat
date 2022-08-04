package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    MaterialEditText et_username, et_password, et_email;
    Button registerbtn;
    Toolbar toolbar;

    String username, email, password;

    FirebaseAuth mAuth;

    DatabaseReference reference;

    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbarregis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng Ký");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        et_username = findViewById(R.id.reg_username);
        et_email = findViewById(R.id.reg_email);
        et_password = findViewById(R.id.reg_password);
        registerbtn = findViewById(R.id.register_Account_btn);

        mAuth = FirebaseAuth.getInstance();




        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();
                username = et_username.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Email không dược bỏ trống");
                } else if (TextUtils.isEmpty(username)) {
                    et_username.setError("Tên người dùng không được bỏ trống");

                } else if (TextUtils.isEmpty(password)) {
                    et_password.setError("Mật khẩu không được bỏ trống");

                } else if (password.length() < 6) {
                    et_password.setError("Mật khẩu phải có trên 6 kí tự");
                } else {
                    registerUser(username, password, email);
                }
                }
        });

    }

    private void registerUser(final String username, String password, final String email) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());


                    if (user!=null) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(RegisterActivity.this, StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));

                                }
                            }
                        });

                    }
                }
                else {
                    et_email.setError("Email Đăng Ký Đã Tồn Tại");
                }

            }
        });

    }


}