package com.example.anonymousgraffiti;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;

public class makeImageActivity extends AppCompatActivity {

    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_image);
        drawView = findViewById(R.id.draw);
        drawView.setBackgroundColor(Color.LTGRAY);

        // Did the user lift up their finger or draw more?
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Determine if user lifted finger vs. drew more
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // continuing a line
                    drawView.actionUp();
                } else {
                    // stopping a line being drawn
                    drawView.addLine(event.getX(), event.getY());
                    drawView.invalidate();
                }
                return true;
            }
        };
        drawView.setOnTouchListener(onTouchListener);


        // FOR CHANGING THE DRAW LINE'S WIDTH
        SeekBar seekBar = findViewById(R.id.lineWidth);
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // updated continuously as the user slides the thumb
                drawView.setWidth((progress) / 5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        };
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        // Get info from previous action
        String info = this.getIntent().getAction();

    }

    // Back Button
    public void handleBackButton(View v) {
        Intent back = new Intent(this, MainActivity.class);
        this.startActivity(back);

    }

    public void clearLines(View v) {
        for (int i = 0; i < 100; i++) {
            handleUndo(v);
        }
    };

    // Undo Button
    public void handleUndo(View v) {
        drawView.undo();
        drawView.invalidate();
    }

    // Taking a Photo
    public void addImage(View v) {
        clearLines(v);

        Intent x = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(x,123);
    }

    // Save Photo
    public void saveImage(View v) {
        // Uses a linear layout as a wrapper to get all of the canvas
        LinearLayout wrapper = findViewById(R.id.drawWrap);
        wrapper.setDrawingCacheEnabled(true);
        wrapper.destroyDrawingCache();
        wrapper.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(wrapper.getDrawingCache());

        // convert bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] ba = stream.toByteArray();
        // adds image to database
        MainActivity.addImage(ba);
    }

    @Override
    // Automatically called when camera activity returns, currently set-up to set bitmap
    protected void onActivityResult(int requestCode, int resultCode, Intent x) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Bundle extras = x.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = Bitmap.createBitmap(imageBitmap);
            imageBitmap = imageBitmap.copy(imageBitmap.getConfig(), true);
            //imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 300, 400, true);
            drawView.setBitmap(imageBitmap);

        }
    }

    // Changing the color of the line
    public void changeColor(View v) {
        switch (v.getId()) {
            case R.id.red: { drawView.setColor(Color.RED); break; }
            case R.id.yellow: { drawView.setColor(Color.YELLOW); break; }
            case R.id.green: { drawView.setColor(Color.GREEN); break; }
            case R.id.blue: { drawView.setColor(Color.BLUE); break; }
            case R.id.magenta: { drawView.setColor(Color.MAGENTA); break; }
            case R.id.white: { drawView.setColor(Color.WHITE); break; }
            case R.id.black: { drawView.setColor(Color.BLACK); break; }
            default: { drawView.setColor(Color.BLACK); break;}
        }

    }

}
