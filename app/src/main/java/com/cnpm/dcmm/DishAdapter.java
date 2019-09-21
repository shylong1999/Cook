package com.cnpm.dcmm;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ImageViewHolder> {
        private Context mContext;
        private List<Dish> mUploads;

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
            final Dish dish = mUploads.get(position);
            holder.namedish.setText(dish.getNamedish());
            holder.nameuser.setText(dish.getEmailuser());
            Picasso.get()
                    .load(dish.getImage())
                    .fit()
                    .centerCrop()
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

        }

        @Override
        public int getItemCount() {
            return mUploads.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            public TextView nameuser,namedish;
            public ImageView imageDish,imageUser;

            public ImageViewHolder(View itemView) {
                super(itemView);

                nameuser = itemView.findViewById(R.id.nameuser);
                namedish = itemView.findViewById(R.id.namedish);
                imageDish = itemView.findViewById(R.id.imagedish);
                imageUser = itemView.findViewById(R.id.imageuser);

            }
        }
    }

