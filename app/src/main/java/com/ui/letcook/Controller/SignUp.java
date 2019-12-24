package com.ui.letcook.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ui.letcook.Model.Acount;
import com.ui.letcook.R;


import java.util.ArrayList;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button sign;
    EditText tk;
    EditText mk,mk2;
    EditText usename;
    DatabaseReference mData;
    ProgressDialog progressDialog;
    TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        tk = (EditText) findViewById(R.id.tk);
        mk = (EditText) findViewById(R.id.mk);
        mk2 = (EditText) findViewById(R.id.password2);
        sign = (Button) findViewById(R.id.button2);
        signin=(TextView) findViewById(R.id.textsignin);
        usename = (EditText) findViewById(R.id.usename);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ trong giây lát...");
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,SignIn.class));
                finish();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=tk.getText().toString();
                String password=mk.getText().toString();
                String password2=mk2.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    tk.setError("Tài khoản phải ở định dạng email");
                    tk.setFocusable(true);
                }
                if(email.isEmpty()){
                    tk.setError("Mời nhập email");
                    tk.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    tk.setError("Mời nhập đúng email");
                    tk.requestFocus();
                    return;
                }
                if(password.length()<6){
                    mk.setError("Mật khẩu phải nhiều hơn 6 kí tự");
                    mk.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    mk.setError("Mời nhập mật khẩu");
                    mk.requestFocus();
                    return;
                }
                if(!password.equals(password2)){
                    mk2.setError("Mật khẩu không khớp");
                    mk2.requestFocus();
                    return;
                }
                else{
                    registerUser(email,password);


                    }
            }
        });
    }
    private void registerUser(String email,String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            tk.setText("");
                            mk.setText("");
                            mk2.setText("");
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                ArrayList<String> save = new ArrayList();
                                String email=user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object, Acount> hashMap= new HashMap<>();
                            hashMap.put(uid,new Acount(email,uid,"1",splitemail(email),save,0));
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("User");
                                reference.child(uid).setValue(new Acount(email,uid,"https://firebasestorage.googleapis.com/v0/b/dcmm-bc67e.appspot.com/o/user%2F65a0512d074d9913cddb61bc803b6648.png?alt=media&token=3f6316b2-3ba1-4dbf-bd53-b20a9f57f245",splitemail(email),save,0));
                            }

                            Toast.makeText(SignUp.this,"Tạo tài khoản thành công",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(SignUp.this,"Tạo tài khoản thất bại",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
//                Toast.makeText(SignUp.this,""+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private String splitemail(String a){
        String[]b=a.split("@");
        return b[0];
    }
}
