package com.cnpm.dcmm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        listView= (ListView) findViewById(R.id.listhistory);
        loadhistory();

    }

    public void loadhistory(){


        arrayList= new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Dish");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Dish dish = postSnapshot.getValue(Dish.class);
                    if(dish.getEmailuser().contains(firebaseUser.getEmail())){
                        arrayList.add(dish.getNamedish());
                    }}


                ArrayAdapter arrayAdapter= new ArrayAdapter(History.this,android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
    });}
}
