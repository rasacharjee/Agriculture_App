package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class DdapendingAdapter extends RecyclerView.Adapter<DdapendingAdapter.ViewHolderDda>{
    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    ArrayList<String> mtextview4;
    Context mcontext;

    public DdapendingAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,ArrayList<String> mtextview3,ArrayList<String> mtextview4) {
        this.mcontext = mcontext;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.mtextview4 = mtextview4;
    }

    @NonNull
    @Override
    public ViewHolderDda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notassignedlist,parent,false);
        ViewHolderDda viewHolderDda = new ViewHolderDda(view);
        return viewHolderDda;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDda holder, int position) {
        holder.tv1.setText(mtextview1.get(position));
        holder.tv2.setText(mtextview2.get(position));
        holder.tv3.setText(mtextview3.get(position));
        holder.tv4.setText(mtextview4.get(position));
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolderDda extends RecyclerView.ViewHolder{

        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        RelativeLayout parentnotassigned;

        public ViewHolderDda(@NonNull View itemView) {
            super(itemView);
            parentnotassigned = itemView.findViewById(R.id.parentnotassigned);
            tv1 = itemView.findViewById(R.id.lid);
            tv2 = itemView.findViewById(R.id.date);
            tv3 = itemView.findViewById(R.id.Time);
            tv4 = itemView.findViewById(R.id.address);
        }
    }

}
