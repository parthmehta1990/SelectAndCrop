package com.example.selectandcrop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selectandcrop.Cropping.CropActivity;
import com.example.selectandcrop.Interfaces.ListItemClickListener;
import com.example.selectandcrop.Model.AlbumFilePOJO;
import com.example.selectandcrop.Model.FruitAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.steelkiwi.cropiwa.image.CropIwaResultReceiver;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ListItem  extends AppCompatActivity implements ListItemClickListener,CropIwaResultReceiver.Listener{

    private RecyclerView recyclerView;
    private ArrayList<AlbumFilePOJO> imageModelArrayList;
    private FruitAdapter adapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    AlbumFilePOJO albumFilePOJO;

    ImageView selectedImage;
    Uri selectedPath;

    private CropIwaResultReceiver cropResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_crop);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        selectedImage=(ImageView)findViewById(R.id.imageView);
        //imageModelArrayList = eatFruits();

        cropResultReceiver = new CropIwaResultReceiver();
        cropResultReceiver.setListener(this);
        cropResultReceiver.register(this);

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

                        selectedPath=Uri.fromFile(new File(mAlbumFiles.get(0).getPath()));

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
                        finish();
                    }
                })
                .start();
    }

    @Override
    public void onListItemClick(View itemView,int position) {
        File imgFile = new File(mAlbumFiles.get(position).getPath());
        selectedPath=Uri.fromFile(new File(mAlbumFiles.get(position).getPath()));
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
                // Completing crop functionality and passing the data for upload
                finish();
                return  true;

            case R.id.crop:
                // Initiate to crop of a single selected image and initiate fro cropping
                startActivity(CropActivity.callingIntent(ListItem.this, selectedPath));

                return true;

            default:
                return super.onContextItemSelected(item);

        }
    }

    @Override
    public void onCropSuccess(Uri croppedUri) {
        cropResultReceiver.unregister(ListItem.this);
        //here we get the image after cropping
        getFileIMGSize(croppedUri);
    }

    @Override
    public void onCropFailed(Throwable e) {

        Toast.makeText(ListItem.this,R.string.msg_crop_failed,Toast.LENGTH_LONG).show();
    }

    private void getFileIMGSize(Uri uri){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // options.inTargetDensity=300;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        //bmp.setDensity(DisplayMetrics.DENSITY_XXXHIGH);
        Log.d("ImageSize=>",""+options.outMimeType+""+imageHeight+"=>"+imageWidth+"=>"+options.inDensity+"=>"+options.inTargetDensity+"=>"+new File(uri.getPath()).getAbsolutePath()+"=>"+new File(uri.getPath()).length()+"=>"+getMimeType(uri.getPath()));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            SaveImage( bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".png";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
