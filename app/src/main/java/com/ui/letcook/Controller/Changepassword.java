package com.ui.letcook.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ui.letcook.R;

public class Changepassword extends AppCompatActivity {
    EditText mkcu,mkmoi,mkmoi2;
     FirebaseUser user;
     Button change,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        back=findViewById(R.id.bachchangepass);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mkcu=findViewById(R.id.mkcu);
        mkmoi=findViewById(R.id.mkmoi);
        mkmoi2=findViewById(R.id.mkmoi2);




         user = FirebaseAuth.getInstance().getCurrentUser();

        change=findViewById(R.id.changepassbutton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail().toString(), mkcu.getText().toString());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(mkmoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Changepassword.this,"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Changepassword.this,"Đổi mật khẩu thất bại",Toast.LENGTH_SHORT).show();                                    }
                                        }
                                    });
                                } else {
                                }
                            }
                        });
            }
        });

    }
}
