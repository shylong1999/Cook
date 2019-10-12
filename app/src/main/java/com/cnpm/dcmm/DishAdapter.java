package com.cnpm.dcmm;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ImageViewHolder> {
        private Context mContext;
        private List<Dish> mUploads;
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference;
        DatabaseReference databaseReference2;


        public DishAdapter(Context context, List<Dish> uploads) {
            mContext = context;
            mUploads = uploads;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.dish_item, parent, false);
            return new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            final ArrayList<String>arrayList= new ArrayList<>();

            final Dish dish = mUploads.get(position);
            databaseReference2=FirebaseDatabase.getInstance().getReference();
            databaseReference= FirebaseDatabase.getInstance().getReference("User");

            databaseReference.child(firebaseUser.getUid()).child(firebaseUser.getUid()).child("save").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    if(dish.getNamedish().equals(dataSnapshot.getValue().toString())){
//                        save="false";
//                    }
                    arrayList.add(dataSnapshot.getValue().toString());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.namedish.setText(dish.getNamedish());
            holder.nameuser.setText(dish.getEmailuser());
            holder.date.setText(dish.getDate());
            Picasso.get()
                    .load(dish.getImage())
                    .placeholder(R.drawable.common_full_open_on_phone)
//                    .fit()
//                    .centerCrop()
                    .into(holder.imageDish);
            Picasso.get()
                    .load(dish.getImageuser())
                    .fit()
                    .centerCrop()
                    .into(holder.imageUser);
            holder.imageDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(mContext,RecipeActivity.class);
                    intent.putExtra("imageuser",dish.getImageuser());
                    intent.putExtra("emailuser",dish.getEmailuser());
                    intent.putExtra("namedish",dish.getNamedish());
                    intent.putExtra("making",dish.getMake());
                    intent.putExtra("image",dish.getImage());
                    mContext.startActivity(intent);
                }
            });
            holder.save.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    String save="true";
                    for (int i=0;i<arrayList.size();i++){
                        if(dish.getNamedish().equals(arrayList.get(i).toString())){
                            save="false";
                        }
                    }
                    System.out.println(save);
                    if(save=="true"){
                        databaseReference2.child("User").child(firebaseUser.getUid()).child(firebaseUser.getUid()).child("save").push().setValue(dish.getNamedish());
                        Toast.makeText(v.getContext(),"saved",Toast.LENGTH_LONG).show();
                    }
                    else Toast.makeText(v.getContext(),"Bạn đã lưu trước đó",Toast.LENGTH_LONG).show();
                    arrayList.clear();
                }

                }
            );

        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            public TextView nameuser,namedish,date;
            public ImageView imageDish,imageUser,save;

            public ImageViewHolder(View itemView) {
                super(itemView);
                save=itemView.findViewById(R.id.save);
                date= itemView.findViewById(R.id.textViewdate);
                nameuser = itemView.findViewById(R.id.nameuser);
                namedish = itemView.findViewById(R.id.namedish);
                imageDish = itemView.findViewById(R.id.imagedish);
                imageUser = itemView.findViewById(R.id.imageuser);

            }
        }
    }

