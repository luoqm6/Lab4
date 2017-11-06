package com.example.lab4;

/*
 * Created by qingming on 2017/10/26.
 */


import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemInfo extends AppCompatActivity {
    /*为了方便重写手机自带返回键返回这两个变量放在外面*/
    private Bundle bundle;
    private Goods curG=new Goods();
    private static final String DYNAMICACTION = "com.example.lab4.DYNAMICACTION";
    DynamicBroadcastReceiver dynamicBroadcastReceiver=new DynamicBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);


        //注册动态广播关键代码:
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(DYNAMICACTION);
        registerReceiver(dynamicBroadcastReceiver,dynamic_filter);

        //设定商品详情界面的信息
        bundle=getIntent().getExtras();
        curG=new Goods(bundle);
        TextView tv=(TextView) findViewById(R.id.nameInfo);
        tv.setText(curG.getname());
        tv=(TextView) findViewById(R.id.priceInfo);
        tv.setText(curG.getprice());
        tv=(TextView) findViewById(R.id.shortInfo);
        tv.setText(curG.getshortInfo());
        ImageView iv=(ImageView) findViewById(R.id.ItemImgInfo);
        iv.setImageResource(curG.getimgId());
        final ImageView favoriteImg=(ImageView) findViewById(R.id.StarInfo);
        if(!curG.getfavorite()){
            favoriteImg.setImageResource(R.mipmap.empty_star);
        }
        else{
            favoriteImg.setImageResource(R.mipmap.full_star);
        }


        final String []btmInfo={"一键下单","分享商品","不感兴趣","查看更多促销信息"};
        List<Map<String,Object>> listItems1 = new ArrayList<>();
        for(int i=0;i<btmInfo.length;i++){
            Map<String,Object> tmp = new LinkedHashMap<>();
            tmp.put("infos",btmInfo[i]);
            listItems1.add(tmp);
        }
        ListView listview = (ListView) findViewById(R.id.ListViewInfo);
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,listItems1,R.layout.listviewinfo,
                new String[]{"infos"},new int []{R.id.TxInInfo});
        listview.setAdapter(simpleAdapter);

        /*左上角返回按键返回主表，若加入购物车则带回数据*/
        ImageView backImg=(ImageView) findViewById(R.id.backBtnInfo);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ItemInfo.this,MainActivity.class);
                returnMain(intent);
            }
        });

        /*z点购物车将商品加入购物车列表*/
        ImageView shopListImg=(ImageView) findViewById(R.id.shoplist);
        shopListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"商品已加到购物车",Toast.LENGTH_SHORT).show();
                //点击购物车图标时候，传递商品信息
                EventBus.getDefault().post(new MessageEvent(curG));
                //点击购物车图标时候，发送动态广播
                BroadcastDynamic(DYNAMICACTION);
            }
        });
        /*z点星星收藏商品*/
        favoriteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!curG.getfavorite()){
                    favoriteImg.setImageResource(R.mipmap.full_star);
                    Toast.makeText(getApplicationContext(),"商品已收藏",Toast.LENGTH_SHORT).show();
                    curG.setfavorite(true);
                }
                else{
                    favoriteImg.setImageResource(R.mipmap.empty_star);
                    Toast.makeText(getApplicationContext(),"商品已取消收藏",Toast.LENGTH_SHORT).show();
                    curG.setfavorite(false);
                }
            }
        });



    }

    /*重写手机自带的返回键返回主表，若加入购物车则带回数据*/
    @Override
    public boolean onKeyDown(int KeyCode, KeyEvent keyEvent){
        if(KeyCode== KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent();
            intent.setClass(ItemInfo.this,MainActivity.class);
            returnMain(intent);
        }
        return true;
    }

    public void returnMain(Intent intent){
        //注销动态广播关键代码
        unregisterReceiver(dynamicBroadcastReceiver);
        Bundle bun;
        bun=curG.putInBundle();
        bun.putInt("whichView",0);
        intent.putExtras(bun);
        //回到MainActivity
        setResult(1,intent);
        finish();
        //startActivity(intent);
    }
    public void BroadcastDynamic(String message){
        Bundle bundle = curG.putInBundle();
        Intent intentBroadcast = new Intent(message);
        intentBroadcast.putExtras(bundle);
        sendBroadcast(intentBroadcast);
    }
}
