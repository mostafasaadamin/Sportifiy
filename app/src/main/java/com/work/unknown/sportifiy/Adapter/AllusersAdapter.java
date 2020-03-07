package com.work.unknown.sportifiy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.work.unknown.sportifiy.Chating.chat;
import com.work.unknown.sportifiy.Models.AlluserModel;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by unknown on 2/23/2018.
 */

public class AllusersAdapter extends RecyclerView.Adapter<AllusersAdapter.usersholder> {
    ArrayList<AlluserModel> userList=new ArrayList<>();
    Context context;

    public AllusersAdapter(ArrayList<AlluserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public usersholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.availble_user_cardview, parent, false);
        AllusersAdapter.usersholder user= new AllusersAdapter.usersholder(form);
        return user;
    }

    @Override
    public void onBindViewHolder(final usersholder holder, int position) {
        final AlluserModel po = userList.get(position);
        holder.user_name.setText(po.getUsername());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,chat.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("user_Key",po.getKey());
                context.startActivity(i);
            }
        });
        String url =po.getImageurl();
        if (url != null) {
			
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.sman)
                    .error(R.drawable.sman)
                    .into(holder.user_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imagebitmap=((BitmapDrawable)holder.user_image.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imgdrawable= RoundedBitmapDrawableFactory.create(context.getResources(),imagebitmap);
                            imgdrawable.setCircular(true);
                            imgdrawable.setCornerRadius(Math.max(imagebitmap.getWidth(),imagebitmap.getHeight())/2.0f);
                            holder.user_image.setImageDrawable(imgdrawable);
                        }
                        @Override
                        public void onError() {
                            holder.user_image.setImageResource(R.drawable.man);
                        }
                    });
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
    class usersholder extends RecyclerView.ViewHolder
    {
        ImageView user_image;
        CardView cardView;
        TextView user_name;
        public usersholder (View itemView) {
            super(itemView);
            user_image=itemView.findViewById(R.id.av_user);
            user_name=itemView.findViewById(R.id.av_user_name);
            cardView=itemView.findViewById(R.id.card);


        }

    }
}

