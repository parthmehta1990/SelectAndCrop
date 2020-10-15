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

import androidx.cardview.widget.CardView;
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

    int selectedPosition=-1;

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
    public void onBindViewHolder(FruitAdapter.MyViewHolder holder, final int position) {

        if(selectedPosition==position)
            holder.backgroundLayout.setBackgroundColor(Color.parseColor("#88cccc"));
        else
            holder.backgroundLayout.setBackgroundColor(Color.parseColor("#ffffff"));

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

        CardView backgroundLayout;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            backgroundLayout=(CardView)itemView.findViewById(R.id.card_view);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            selectedPosition=position;
            notifyDataSetChanged();

            mOnClickListener.onListItemClick(itemView,position);
        }

    }
}
