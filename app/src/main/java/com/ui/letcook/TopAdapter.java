package com.ui.letcook;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ui.letcook.R;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Acount> mUploads;
    DatabaseReference databaseReferenceduser;


    public TopAdapter(Context context, List<Acount> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public TopAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.topthanhvien, parent, false);
        return new TopAdapter.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TopAdapter.ImageViewHolder holder, int position) {

        final Acount acount = mUploads.get(position);

        holder.nametop.setText(acount.getEmail());
        holder.sobaidang.setText(acount.getSobaidang()+""+ " công thức");
        Picasso.get()
                .load(acount.getImage())
                .fit()
                .centerCrop()
                .into(holder.imagetop);



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagetop;
        public TextView nametop,sobaidang;


        public ImageViewHolder(View itemView) {
            super(itemView);


            imagetop = itemView.findViewById(R.id.avatartop);
            nametop = itemView.findViewById(R.id.nametop);
            sobaidang = itemView.findViewById(R.id.sobaidang);

        }
    }
    private String splitemail(String a){
        String []b=a.split("@");
        return b[0];
    }
}


