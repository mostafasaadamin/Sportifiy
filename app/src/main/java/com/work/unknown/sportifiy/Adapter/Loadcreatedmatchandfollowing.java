package com.work.unknown.sportifiy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.CreatedMatchModel;

import java.util.ArrayList;

/**
 * Created by unknown on 2/6/2018.
 */

public class Loadcreatedmatchandfollowing extends RecyclerView.Adapter<Loadcreatedmatchandfollowing.typeholder>{
    ArrayList<CreatedMatchModel> detail;
    Context con;
    String key;
    public Loadcreatedmatchandfollowing(ArrayList<CreatedMatchModel>detail,Context con)
    {
        this.detail=detail;
        this.con=con;
    }
    @Override
    public Loadcreatedmatchandfollowing.typeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form= LayoutInflater.from(parent.getContext()).inflate(R.layout.createdmatchcardview,parent,false);
        typeholder type=new typeholder(form);
        return type;
    }
    @Override
    public void onBindViewHolder(typeholder holder, int position) {
        CreatedMatchModel po=detail.get(position);
        holder.Sport.setText(po.getSport().toString());
        holder.Date.setText(po.getDate().toString());
        holder.Location.setText(po.getLocation().toString());
        holder.Place.setText(po.getPlace().toString());

    }

    @Override
    public int getItemCount() {

        return detail.size();
    }
    class typeholder extends RecyclerView.ViewHolder
    {
        TextView Sport,Date,Location,Place;
        public typeholder(View itemView) {
            super(itemView);
            Sport=itemView.findViewById(R.id.sportvalue);
            Date=itemView.findViewById(R.id.datevalue);
            Location=itemView.findViewById(R.id.locationvalue);
            Place=itemView.findViewById(R.id.placevalue);

        }
    }
}
