package com.example.anonymousgraffiti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.List;

public class seeImagesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_images);

        // Get info from previous action
        String info = this.getIntent().getAction();

        List<byte[]> photos = MainActivity.getImages();

        GridLayout grid = findViewById(R.id.gridLayout);

        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        grid.setColumnCount(1);
        grid.setRowCount(photos.size());

        int row = 0;
        for (byte[] ba : photos) {
            ImageView img = new ImageView(this);
            Bitmap bit = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            img.setImageBitmap(bit);


            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.columnSpec = GridLayout.spec(0);
            param.rowSpec = GridLayout.spec(row);
            img.setLayoutParams (param);

            grid.addView(img, row);
            row++;
        }

    }



    public void handleBack(View v) {
        Intent back = new Intent(this, MainActivity.class);
        this.startActivity(back);

    }


}
