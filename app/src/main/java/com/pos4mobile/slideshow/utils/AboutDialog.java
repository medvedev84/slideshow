package com.pos4mobile.slideshow.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.pos4mobile.slideshow.R;

/**
 * Created by Konstantin on 24.09.2014.
 */
public class AboutDialog {

    Dialog dialog = null;

    public AboutDialog(Activity activity){
        String versionName = "1.0";
        try {
            versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.about);
        dialog.setTitle(R.string.about_title);

        TextView text = (TextView) dialog.findViewById(R.id.versionTextView);
        text.setText(versionName);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    public void show(){
        dialog.show();
    }
}
