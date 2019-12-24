package com.ui.letcook.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ui.letcook.R;

public class Forgotpass extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    EditText email;
    ProgressDialog pd;
    Button send,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
            email=findViewById(R.id.emailforgot);
            send=findViewById(R.id.send);
            back=findViewById(R.id.bachquenmk);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            pd=new ProgressDialog(Forgotpass.this);
            pd.setMessage("Vui lòng chờ trong giây lát...");
           auth = FirebaseAuth.getInstance();
           firebaseUser=auth.getCurrentUser();

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Toast.makeText(Forgotpass.this, "Kiểm tra gmail của bạn", Toast.LENGTH_SHORT).show();
                                    } else {
                                        pd.dismiss();
                                Toast.makeText(Forgotpass.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });


    }
}
