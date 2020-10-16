package com.example.selectandcrop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selectandcrop.Interfaces.ListItemClickListener;
import com.example.selectandcrop.Model.AlbumFilePOJO;
import com.example.selectandcrop.Model.FruitAdapter;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.util.ArrayList;

public class ListItem  extends AppCompatActivity implements ListItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<AlbumFilePOJO> imageModelArrayList;
    private FruitAdapter adapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    AlbumFilePOJO albumFilePOJO;

    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycle);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        selectedImage=(ImageView)findViewById(R.id.imageView);
        //imageModelArrayList = eatFruits();
        selectImage();

    }

    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                .columnCount(2)
                .selectCount(6)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .title("Select Images")
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;

                        imageModelArrayList=new ArrayList<>();

                        for (int i =0;i<mAlbumFiles.size();i++)
                        {
                            albumFilePOJO=new AlbumFilePOJO();
                            albumFilePOJO.setmPath(mAlbumFiles.get(i).getPath());
                            albumFilePOJO.setmMimeType(mAlbumFiles.get(i).getMimeType());
                            albumFilePOJO.setmSize(mAlbumFiles.get(i).getSize());

                            imageModelArrayList.add(albumFilePOJO);
                        }

                        adapter = new FruitAdapter(ListItem.this, imageModelArrayList, (ListItemClickListener) ListItem.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                        File imgFile = new File(mAlbumFiles.get(0).getPath());

                        if(imgFile.exists()){

                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                            selectedImage.setImageBitmap(myBitmap);

                        }

                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(ListItem.this, "canceled", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    @Override
    public void onListItemClick(View itemView,int position) {
        File imgFile = new File(mAlbumFiles.get(position).getPath());

       // CardView card =itemView.findViewById(R.id.card_view);
      //  card.setCardBackgroundColor(Color.parseColor("#880000"));
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            selectedImage.setImageBitmap(myBitmap);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crop_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                // do something
                return true;
            case R.id.crop:
                // do something
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
