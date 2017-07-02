package com.kaivanshah.assignment92;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Permission;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    Button btn_Save;
    TextView tv_Sample, tv_Sample1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 21;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Save = (Button) (this.findViewById(R.id.btn_Save));
        tv_Sample = (TextView) (this.findViewById(R.id.tv_Sample));
        tv_Sample1 = (TextView) (this.findViewById(R.id.tv_Sample1));

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkWriteExternalPermission() == true)
                {
                    WriteFile();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You don't have permission", Toast.LENGTH_LONG).show();
                }



            }
        });



    }

    private boolean checkWriteExternalPermission()
    {

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck  != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else
        {
            return true;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    WriteFile();

                } else {

                    Toast.makeText(getApplicationContext(), "You don't have permission", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void WriteFile()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            Drawable drawable = getDrawable(R.drawable.errormsg);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] drawableImage = stream.toByteArray();
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + File.separator +"saved_images");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            tv_Sample.setText(myDir.getAbsolutePath());

            File file = new File(myDir, "Sample_Image.png");
            try
            {
                file.createNewFile();
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                fileoutputstream.write(drawableImage);
                fileoutputstream.close();
                fileoutputstream.flush();
                Toast.makeText(MainActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                tv_Sample1.setText(e.getMessage());

            }
        }
    }

}
