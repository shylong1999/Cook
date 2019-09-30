package com.cnpm.dcmm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class SaveDishActivity extends AppCompatActivity {
    ListView listView;
    String imageuser1="";
    String emailuser1="";
    String namedish="";
    String making="";
    String image="";
    ArrayList<String> arrayList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Dish dish = new Dish();
    private List<Dish> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_dish);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        listView=(ListView)findViewById(R.id.listsave);

        loadsave();
        loaddish();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getdatasavedish(listView.getItemAtPosition(position).toString());
                Intent intent= new Intent(SaveDishActivity.this,RecipeActivity.class);
                intent.putExtra("imageuser",dish.getImageuser());
                intent.putExtra("emailuser",dish.getEmailuser());
                intent.putExtra("namedish",dish.getNamedish());
                intent.putExtra("making",dish.getMake());
                intent.putExtra("image",dish.getImage());
                System.out.println(dish.getImage());
                System.out.println(dish.getEmailuser());
                System.out.println(dish.getNamedish());
                System.out.println(dish.getImageuser());
                startActivity(intent);
            }
        });
    }

    public void loadsave(){


        arrayList= new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Saved");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SaveDish saveDish = postSnapshot.getValue(SaveDish.class);
                    if(saveDish.getEmailuser().equals(firebaseUser.getEmail())){
                        arrayList.add(saveDish.getNamedish());
                    }}


                ArrayAdapter arrayAdapter= new ArrayAdapter(SaveDishActivity.this,android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}
    public  void  getdatasavedish( final String a){
        for(int i=0;i< mUploads.size();i++){
            if(a.equals(mUploads.get(i).getNamedish())){
                dish=mUploads.get(i);
            }
        }
    }
    public void loaddish(){
            mUploads = new ArrayList<>();
            databaseReference = FirebaseDatabase.getInstance().getReference("Dish");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Dish dish = postSnapshot.getValue(Dish.class);
                        mUploads.add(dish);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


