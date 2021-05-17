package com.example.snapapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.snapapp.adapter.MyAdapter;
import com.example.snapapp.model.Snap;
import com.example.snapapp.repo.Repo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Updatable{

    // skal have billeder fra db ind i denne liste
    List<Snap> items = new ArrayList<>();

    ListView listView;
    MyAdapter myAdapter;
    //Initialize variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //vi skal lave en forbindelse til listView her.
        //R kompiler hele tiden, så f.eks hvergang der bliver lavet en knap rekompiler den klassen.
        //Hver gang vi laver noget nyt, bliver et nyt nr(id) klar
        listView = findViewById(R.id.listView1);
        // vi impotere adapteren og vælger listen som parameter og "this" er context vi vælger som er den mest alm.
        myAdapter = new MyAdapter(items, this);
        listView.setAdapter(myAdapter);
        Repo.repo().setup(this, items);
        setupListView();


    }

    //Denne knap skal fører dig til din profil.
    public void MyProfilPressed(View view){
        System.out.println("MyProfil Is Pressed");
        //Intent er til for at vælge hvilken destination vi vil til, ved at declare this(objekt) fra en klasse(MyProfill)
        Intent intet = new Intent(this, MyProfil.class);
        //når vi kalder på startActivity, vil vi starte en ny aktivitet med den intent som vi lavede ovenover
        startActivity(intet);
    }

    //Denne knap skal fører dig til din tagbillede.
    public void TakePicturePressed(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e){
            System.out.println("hej der er fejl");
        }
    }

//____________________________________________________________________

    //snapp ting

    private void setupListView(){
        //når man klikker vil vi gerne have den tager et "item" med et id fra listviewet fra db
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snap tempSnap = items.get(position);

                Intent snapIntent = new Intent(MainActivity.this, SnapOpen.class);

                snapIntent.putExtra("id", tempSnap.getId());

                startActivity(snapIntent);


            }
        });
    }


    @Override
    public void update(Object o) {
        myAdapter.notifyDataSetChanged();
    }



    //________________

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //get capture image
            //Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image to imageview
            insertText((Bitmap)data.getExtras().get("data"));
        }
    }

    public void insertText(Bitmap image){
        //Laver pop op
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Indsæt text");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", ((dialog, which) -> insertTextToBitmap(image, input.getText().toString())));
        builder.setNegativeButton("Annuller", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    public void insertTextToBitmap(Bitmap image, String gText) {
        Bitmap.Config bitmapConfig = image.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        image = image.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// new antialised Paint
        paint.setColor(Color.rgb(161, 161, 161));
        paint.setTextSize((int) (20)); // text size in pixels
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE); // text shadow
        canvas.drawText(gText, 10, 100, paint);
        Repo.repo().uploadBitmap(image, gText);
    }

//    @Override
//    public void update(Object o) {
//        System.out.println("trololololo for at se om den opdatere");
//
//    }

    //Denne knap skal føre dig til homepage.
    public void HomePagePressed(View view) {
        //nu siger vi at vi er færdige med denne side og henviser os tilbage til start
        finish();
    }

}





