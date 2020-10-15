package com.example.selectandcrop.Model;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.selectandcrop.Interfaces.ListItemClickListener;
import com.example.selectandcrop.R;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AlbumFile> imageModelArrayList;

    final private ListItemClickListener mOnClickListener;

    public FruitAdapter(Context ctx, ArrayList<AlbumFile> imageModelArrayList, ListItemClickListener mOnClickListener){

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public FruitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FruitAdapter.MyViewHolder holder, int position) {


        File imgFile = new File(imageModelArrayList.get(position).getPath());

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());



            holder.iv.setImageBitmap(myBitmap);

        }
        //holder.iv.setImageBitmap(Integer.parseInt(imageModelArrayList.get(position).getPath()));
       // holder.time.setText(imageModelArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout backgroundLayout;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            backgroundLayout=(LinearLayout)itemView.findViewById(R.id.backgroundLayout);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            mOnClickListener.onListItemClick(itemView,position);
        }

    }
}
