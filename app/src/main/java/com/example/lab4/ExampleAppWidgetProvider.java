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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.example_app_widget_provider);//实例化RemoteView,其对应相应的Widget布局
        ComponentName me=new ComponentName(context,ExampleAppWidgetProvider.class);
        //绑定intent，点击图标能够进入某activity
        Intent mInent=new Intent(context,MainActivity.class);
        mInent.addCategory(Intent.CATEGORY_LAUNCHER);
        //设置widget的文字为传入商品相关信息
        updateViews.setTextViewText(R.id.appwidget_text,"当前没有任何信息");
        //设置widget的图片为传入商品图片
        updateViews.setImageViewResource(R.id.widgetImg,R.mipmap.shoplist);//设置
        //设置跳转至主界面所显示的列表
        Bundle bundle=new Bundle();
        bundle.putInt("whichView",1);
        mInent.putExtras(bundle);
        PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mInent,PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widgetView,mPendingIntent);
        appWidgetManager.updateAppWidget(me,updateViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //
        for(int appWidgetId:appWidgetIds){
            updateAppWidget(context,appWidgetManager,appWidgetId);
        }
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
    public void onReceive(Context context,Intent intent) {
        super.onReceive(context,intent);
        //静态广播推荐信息
        if(intent.getAction().equals(STATICACTION)){
            //获得intent传入信息
            Bundle bundle=intent.getExtras();
            Goods tmpG=new Goods(bundle);
            //实例化RemoteView,其对应相应的Widget布局
            RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.example_app_widget_provider);
            //获取AppWidgetManager实例
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName me=new ComponentName(context,ExampleAppWidgetProvider.class);
            //设置widget的文字为传入商品相关信息
            updateViews.setTextViewText(R.id.appwidget_text,tmpG.getname()+"仅售"+tmpG.getprice()+"!");
            //设置widget的图片为传入商品图片
            updateViews.setImageViewResource(R.id.widgetImg,tmpG.getimgId());//设置
            //绑定intent，点击图标能够进入某activity
            Intent mInent=new Intent(context,ItemInfo.class);
            //mInent.addCategory(Intent.CATEGORY_LAUNCHER);
            mInent.addCategory(Intent.CATEGORY_DEFAULT);
            mInent.putExtras(tmpG.putinbundle());
            PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mInent,PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widgetView,mPendingIntent);
            //更新widget显示信息
            appWidgetManager.updateAppWidget(me,updateViews);
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

