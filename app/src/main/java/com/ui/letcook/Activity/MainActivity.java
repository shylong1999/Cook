package com.ui.letcook.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ui.letcook.R;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    final Fragment home = new Fragment_home();
    final Fragment add = new Fragment_add();
    final Fragment me = new Fragment_me();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = home;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        editText= findViewById(R.id.search);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        fm.beginTransaction().add(R.id.frameLayout, me, "3").hide(me).commit();
        fm.beginTransaction().add(R.id.frameLayout, add, "2").hide(add).commit();

        fm.beginTransaction().add(R.id.frameLayout,home, "1").commit();



//        Fragment_home fragment_home= new Fragment_home();
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,fragment_home,"");
//        fragmentTransaction.commit();


    }
//    private void checkStatus(){
//        FirebaseUser user=mAuth.getCurrentUser();
//        if(user!=null){
//        }
//        else {
//            startActivity(new Intent(MainActivity.this,SignIn.class));
//            finish();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        checkStatus();
//        super.onStart();
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


              switch (menuItem.getItemId()){
                  case R.id.home:

                      fm.beginTransaction().hide(active).show(home).commit();
                      active = home;

                      return true;
                  case R.id.add:

                      fm.beginTransaction().hide(active).show(add).commit();
                      active = add;
                      return true;

                  case R.id.acount :

                      fm.beginTransaction().hide(active).show(me).commit();
                      active = me;
                      return true;
              }

              return true;
          }
      };
}
