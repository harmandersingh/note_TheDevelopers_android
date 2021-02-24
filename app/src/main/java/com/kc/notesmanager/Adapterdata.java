package com.kc.notesmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Adapterdata extends RecyclerView.Adapter<Adapterdata.myview> {

    ArrayList<Modeldata> list;
    Context context;



    public Adapterdata(Context context, ArrayList<Modeldata> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_data,parent,false);

        return new Adapterdata.myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {

        holder.imageView.setImageBitmap(list.get(position).getImage());
        holder.ttile.setText(list.get(position).getTitle());
        holder.tdate.setText(list.get(position).getDate());
        holder.ttime.setText(list.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myview extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ttile,tdate,ttime;
        ImageView imageView;
        public myview(@NonNull View itemView) {
            super(itemView);
            ttile=itemView.findViewById(R.id.satitle);
            tdate=itemView.findViewById(R.id.sadate);
            ttime=itemView.findViewById(R.id.satime);
            imageView=itemView.findViewById(R.id.saimage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Bitmap bitmap=list.get(position).getImage();
            ByteArrayOutputStream bs=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bs);
            Intent intent=new Intent(context,DisplayActivity.class);



            intent.putExtra("id",list.get(position).getID());
            intent.putExtra("byteArray",bs.toByteArray());
            intent.putExtra("title",list.get(position).getTitle());
            intent.putExtra("subject",list.get(position).getSubject());
            intent.putExtra("loaction",list.get(position).getLocation());
            intent.putExtra("date",list.get(position).getDate());
            intent.putExtra("time",list.get(position).getTime());
            intent.putExtra("notes",list.get(position).getNotes());

            context.startActivity(intent);

        }
    }
}
