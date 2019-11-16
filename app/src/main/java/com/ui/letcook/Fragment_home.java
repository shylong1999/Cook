package com.ui.letcook;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;

public class Fragment_home extends Fragment {
    private RecyclerView mRecyclerView,mRecyclerViewadmin,mRecyclerViewtop;
    private DishAdapter mAdapter;
    private DishadminAdapter mAdapteradmin;

    private ProgressBar mProgressCircle;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseRef,mDatabaseRefadmin,databaseReferencehello;
    private List<Dish> mUploads;
    private List<Dish> mUploadsadmin;
    private List<Acount> top,top5;

    private TopAdapter topAdapter;
    EditText search;
    private Button ga;
    ImageView avatahome;
    TextView hello;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_home,container,false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        mRecyclerView = v.findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        databaseReferencehello=FirebaseDatabase.getInstance().getReference("User");
        hello=v.findViewById(R.id.longlon);
        avatahome= v.findViewById(R.id.avatarhome);
        Query query = databaseReferencehello.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String image = ds.child("image").getValue().toString();
                    String name= ds.child("username").getValue().toString();
                    if(name.equals("1")){
                        hello.setText(getTimeFromAndroid()+" "+firebaseUser.getEmail());
                    }
                    else hello.setText(getTimeFromAndroid()+" "+name);

                    if(image.equals("1")) {
                    Picasso.get().load(R.drawable.photo_camera).fit().centerCrop().into(avatahome);
                    }
                    else Picasso.get().load(image).fit().centerCrop().into(avatahome);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        LinearLayoutManager layoutManageradmin= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManageradmin.setStackFromEnd(true);
        layoutManageradmin.setReverseLayout(true);
        mRecyclerViewadmin = v.findViewById(R.id.recycleviewadmin);
        mRecyclerViewadmin.setHasFixedSize(true);
        mRecyclerViewadmin.setLayoutManager(layoutManageradmin);

        LinearLayoutManager layoutManagertop= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        mRecyclerViewtop = v.findViewById(R.id.recycleviewtop);
        mRecyclerViewtop.setHasFixedSize(true);
        mRecyclerViewtop.setLayoutManager(layoutManagertop);

        search= v.findViewById(R.id.search);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                    Intent i=new Intent(getActivity(),SearchActivity.class);
                    i.putExtra("name",search.getText().toString());
                    startActivity(i);
                    System.out.println(search.getText().toString());
                    return true;
                }
                return false;

            }
        });



        loadDish();
        loadDishadmin();
        loadTop();
        return v;
    }
    private void loadDish() {
        mUploads = new ArrayList<>();
        mUploads.clear();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Dish");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Dish dish= ds.getValue(Dish.class);
                mUploads.add(dish);
                mAdapter = new DishAdapter(getActivity(), mUploads);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void loadTop(){
        top=new ArrayList<>();
        top5=new ArrayList<>();

//        top.clear();
        databaseReferencehello.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                top.clear();
                top5.clear();

                for ( DataSnapshot ds: dataSnapshot.getChildren()){

                    Acount acount= ds.getValue(Acount.class);
                    top.add(acount);

                }
                selectionSort(top);
                for(int i=top.size()-1;i>top.size()-6;i--){
                    top5.add(top.get(i));
                }
                topAdapter= new TopAdapter(getActivity(),top5);
                topAdapter.notifyDataSetChanged();
                mRecyclerViewtop.setAdapter(topAdapter);
                System.out.println(top5.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void searchDish(final String search){
        mUploads = new ArrayList<>();
        mUploads.clear();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Dish");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Dish dish = postSnapshot.getValue(Dish.class);
                    if(dish.getNamedish().contains(search)){
                        mUploads.add(dish);
                    }}

                mAdapter = new DishAdapter(getActivity(), mUploads);

                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void loadDishadmin() {
        mUploadsadmin = new ArrayList<>();
        mUploadsadmin.clear();
        mDatabaseRefadmin = FirebaseDatabase.getInstance().getReference("Dish");

        mDatabaseRefadmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploadsadmin.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                Dish dish= dataSnapshot1.getValue(Dish.class);
                if (dish.getEmailuser().equals("admin@gmail.com")){
                    mUploadsadmin.add(dish);}
                mAdapteradmin = new DishadminAdapter(getActivity(), mUploadsadmin);
                mAdapteradmin.notifyDataSetChanged();
                mRecyclerViewadmin.setAdapter(mAdapteradmin);

            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.app_bar_search);
        androidx.appcompat.widget.SearchView searchView=  (androidx.appcompat.widget.SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)){
                    searchDish(newText);

                }
                else {
                    loadDish();
                }
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
    private String getTimeFromAndroid() {
        String time="";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            time="Chào buổi sáng";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time="Chào buổi chiều";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            time="Chào buổi tối";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            time="Chúc ngủ ngon";
        }

        return time;
    }
    private void checkStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
        } else {
            startActivity(new Intent(getActivity(), SignIn.class));
        }
    }

    @Override
    public void onStart() {
        checkStatus();
        super.onStart();
    }
    private void selectionSort(List<Acount> list ){
        for (int i = 0; i < list.size() - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < list.size(); j++){
                if (list.get(j).getSobaidang() < list.get(index).getSobaidang()){
                    index = j;//searching for lowest index
                }
            }
            Acount smallerNumber = list.get(index);
            list.set(index,list.get(i));
            list.set(i,smallerNumber);


        }
    }

}

