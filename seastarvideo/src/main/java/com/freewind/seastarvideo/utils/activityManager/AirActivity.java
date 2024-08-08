package com.freewind.seastarvideo.utils.activityManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.freewind.seastarvideo.EnvArgument;

/**
 * @author wiatt
 * 用于将主app唤置前台
 * @time 2019/8/30 10:54
 */
public class AirActivity extends FragmentActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AirActivity.class);
        context.startActivity(starter);
    }

    /**
     * 这种启动方式是让此activity从app的主task内启动，达到切换task回到主app的目的
     */
    public static void startFromHost() {
        Intent starter = new Intent(EnvArgument.getInstance().getApp(), AirActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EnvArgument.getInstance().getApp().getApplicationContext().startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
