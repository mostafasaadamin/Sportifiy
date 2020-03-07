package com.work.unknown.sportifiy.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.work.unknown.sportifiy.Chating.chat;
import com.work.unknown.sportifiy.Chating.chatingmessage;
import com.work.unknown.sportifiy.Models.AlluserModel;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by unknown on 2/23/2018.
 */

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageholder> {
    ArrayList<chatingmessage> userList=new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    public messageAdapter(ArrayList<chatingmessage> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    @Override
    public messageholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymessage, parent, false);
        messageAdapter.messageholder user= new messageAdapter.messageholder(form);
        return user;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final messageholder holder, int position) {
        final chatingmessage po = userList.get(position);
        mAuth=FirebaseAuth.getInstance();
        String current_user=mAuth.getCurrentUser().getUid();
       if(po.getFrom().equals(current_user))
        {
            holder.message_text.setText(po.getMessageText());
            holder.message_text.setBackgroundResource(R.drawable.rounded_corner);
            holder.message_text.setTextColor(Color.WHITE);
        }
        else
            {
                holder.message_text.setText(po.getMessageText());
                holder.message_text.setBackgroundResource(R.drawable.rounded_corner1);
                holder.message_text.setTextColor(Color.BLACK);


        /*        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message_text.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin=32;
                holder.message_text.setLayoutParams(params); //causes layout update


                RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)holder.user_image.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin=2;
                holder.message_text.setLayoutParams(p); //causes layout update
*/


            }


  /*
        String url =po.getImageurl();
        if (url != null) {
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.man)
                    .error(R.drawable.man)
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
        }*/
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
    class messageholder extends RecyclerView.ViewHolder
    {
        TextView message_text;
        ImageView user_image;
        public messageholder (View itemView) {
            super(itemView);
            message_text=itemView.findViewById(R.id.messagetextView);
            user_image=itemView.findViewById(R.id.imageView2);
        }

    }
}

