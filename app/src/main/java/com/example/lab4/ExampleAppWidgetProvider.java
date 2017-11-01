package com.example.lab4;

import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * Implementation of App Widget functionality.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private static String STATICACTION="com.example.lab4.STATICACTION";
    private static final String DYNAMICACTION = "com.example.lab4.DYNAMICACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.example_app_widget_provider);//实例化RemoteView,其对应相应的Widget布局
        Intent i=new Intent("com.example.lab4.CLICK");
        PendingIntent pi=PendingIntent.getBroadcast(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        ComponentName me=new ComponentName(context,ExampleAppWidgetProvider.class);
        appWidgetManager.updateAppWidget(me,updateViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent)
    {
        super.onReceive(context,intent);
        if(intent.getAction().equals(STATICACTION)){
            Bundle bundle=intent.getExtras();
            Goods tmpG=new Goods(bundle);
            Toast.makeText(context,tmpG.getname(),Toast.LENGTH_LONG).show();
            RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.example_app_widget_provider);//实例化RemoteView,其对应相应的Widget布局
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);//获取AppWidgetManager实例
            ComponentName me=new ComponentName(context,ExampleAppWidgetProvider.class);
            updateViews.setTextViewText(R.id.appwidget_text,tmpG.getname()+"仅售"+tmpG.getprice()+"!");
            Toast.makeText(context,tmpG.getname()+"仅售"+tmpG.getprice(),Toast.LENGTH_LONG).show();
            updateViews.setImageViewResource(R.id.widgetImg,tmpG.getimgId());


            //绑定intent，点击图标能够进入某activity
            Intent mInent=new Intent(context,ItemInfo.class);
            mInent.putExtras(tmpG.putinbundle());
            PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mInent,PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.appwidget_text,mPendingIntent);
            updateViews.setOnClickPendingIntent(R.id.widgetImg,mPendingIntent);
            updateViews.setOnClickPendingIntent(R.id.widgetView,mPendingIntent);

            appWidgetManager.updateAppWidget(me,updateViews);
            //绑定Notification，发送通知请求
        }
        if(intent.getAction().equals(DYNAMICACTION)){

        }
    }


//    CharSequence widgetText = context.getString(R.string.appwidget_text);
//    // Construct the RemoteViews object
//    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_app_widget_provider);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
//
//    // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
}

