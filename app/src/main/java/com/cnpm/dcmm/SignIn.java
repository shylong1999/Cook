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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    EditText tk;
    EditText mk;
    Button signin;
    TextView signup;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        firebaseAuth=FirebaseAuth.getInstance();
        tk=(EditText)findViewById(R.id.tksi);
        mk=(EditText) findViewById(R.id.mksi);
        signup= (TextView) findViewById(R.id.textsignup);
        signin=(Button)findViewById(R.id.bSin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                String email=tk.getText().toString();
                String password=mk.getText().toString();
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
                else loginUser(email,password);
            }
        });
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

        private void loginUser(String email, final String password){
            progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();;
                           FirebaseUser user=firebaseAuth.getCurrentUser();
                           startActivity(new Intent(SignIn.this,MainActivity.class));
                           finish();
                        } else {
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignIn.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
