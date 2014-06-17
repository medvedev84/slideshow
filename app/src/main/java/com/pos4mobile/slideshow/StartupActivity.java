package com.pos4mobile.slideshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.pos4mobile.slideshow.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StartupActivity extends Activity {
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        splash = (ImageView) findViewById(R.id.splash);
        splash.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                splash.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupActivity.this, SettingsActivity.class));
            }
        });
        try {
            copyPredefinedData();
        } catch (Exception e) {
            Toast.makeText(this, "could not copy data", Toast.LENGTH_SHORT);
        }
    }

    private void copyPredefinedData() throws Exception {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String appDirPath = rootPath + StorageUtils.PREDEFINED_FOLDER;
        File outputDir = new File(appDirPath);

        if (!outputDir.exists()){
            StorageUtils.saveStringValue(StartupActivity.this, StorageUtils.SOURCE_PATH, rootPath);
            if(!outputDir.mkdirs()) throw new RuntimeException("Can't create directory " + outputDir);
            copyFile(R.raw.image1, "image1.jpg", outputDir);
            copyFile(R.raw.image2, "image2.jpg", outputDir);
            copyFile(R.raw.image3, "image3.jpg", outputDir);
            copyFile(R.raw.image4, "image4.jpg", outputDir);
            copyFile(R.raw.image5, "image5.jpg", outputDir);
            copyFile(R.raw.image6, "image6.jpg", outputDir);
            copyFile(R.raw.image7, "image7.jpg", outputDir);
            StorageUtils.saveStringValue(StartupActivity.this, StorageUtils.SOURCE_PATH, appDirPath);
            StorageUtils.saveIntValue(StartupActivity.this, StorageUtils.DELAY, StorageUtils.PREDEFINED_DELAY);
        }
    }

    private void copyFile(int res, String fileName, File directory) throws Exception {
        FileOutputStream fos = new FileOutputStream(directory.getPath() + "/" + fileName);
        InputStream fis = getResources().openRawResource(res);
        copy(fis, fos);
    }

    private void copy(InputStream in, OutputStream out) throws Exception{
        try {
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
                out.write(buffer, 0, read);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            in.close();
            out.close();
        }
    }
}