package com.zkys.pad.fota;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zkys.pad.fota.base.App;
import com.zkys.pad.fota.main.MainService;
import com.zkys.pad.fota.util.Logs;
import com.zkys.pad.fota.util.install.PackageUtils;

public class Main2Activity extends Activity {

    private static final String TAG="Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=new Intent(this, MainService.class);
        startService(intent);

        Logs.e(TAG,"isSystem:"+PackageUtils.isSystemApplication(App.getContext()));
    }
}
