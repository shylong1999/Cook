package com.cnpm.dcmm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter2 extends RecyclerView.Adapter<CommentAdapter2.ImageViewHolder> {
    private Context mContext;
    private List<Comment> mUploads;

    public CommentAdapter2(Context context, List<Comment> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public CommentAdapter2.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment, parent, false);
        return new CommentAdapter2.ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentAdapter2.ImageViewHolder holder, int position) {
        final Comment comment = mUploads.get(position);
        holder.textViewemail.setText(comment.getEmailuser());
        holder.textViewcmt.setText(comment.getComment());
        Picasso.get()
                .load(comment.getImageuser())
                .fit()
                .centerCrop()
                .into(holder.imageusercmt);



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageusercmt;
        public TextView textViewemail,textViewcmt;


        public ImageViewHolder(View itemView) {
            super(itemView);


            imageusercmt = itemView.findViewById(R.id.imagecmt);
            textViewcmt = itemView.findViewById(R.id.cmt);
            textViewemail = itemView.findViewById(R.id.emailcm);

        }
    }
}

