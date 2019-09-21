package com.cnpm.dcmm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button sign;
    EditText tk;
    EditText mk;
    EditText usename;
    DatabaseReference mData;
    ProgressDialog progressDialog;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        tk = (EditText) findViewById(R.id.tk);
        mk = (EditText) findViewById(R.id.mk);
        sign = (Button) findViewById(R.id.button2);
        signin=(Button) findViewById(R.id.signin);
        usename = (EditText) findViewById(R.id.usename);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Wait...");
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
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    tk.setError("Invalid Email");
                    tk.setFocusable(true);
                }
                else
                    registerUser(email,password);
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
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                            String email=user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap= new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("usename","1");
                            hashMap.put("image","2");
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference= database.getReference("User");
                            reference.child(uid).setValue(hashMap);}

                            Toast.makeText(SignUp.this,"Success",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(SignUp.this,"Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUp.this,""+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
