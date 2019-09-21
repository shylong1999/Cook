package com.cnpm.dcmm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Home");

        Fragment_home fragment_home= new Fragment_home();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment_home,"");
        fragmentTransaction.commit();


    }
    private void checkStatus(){
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
        }
        else {
            startActivity(new Intent(MainActivity.this,SignIn.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkStatus();
        super.onStart();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


              switch (menuItem.getItemId()){
                  case R.id.home:
                      actionBar.setTitle("Home");
                     Fragment_home fragment = new Fragment_home();
                     FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                     ft1.replace(R.id.frameLayout,fragment,"");
                     ft1.commit();
                      return true;
                  case R.id.add:
                      actionBar.setTitle("Add");
                      Fragment_add fragmentAdd = new Fragment_add();
                      FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                      ft2.replace(R.id.frameLayout,fragmentAdd,"");
                      ft2.commit();
                      return true;

                  case R.id.acount    :
                      actionBar.setTitle("Add");
                      Fragment_me fragmentme = new Fragment_me();
                      FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                      ft3.replace(R.id.frameLayout,fragmentme,"");
                      ft3.commit();
                      return true;
              }

              return true;
          }
      };
}
