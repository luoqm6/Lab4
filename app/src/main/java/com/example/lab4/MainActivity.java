package com.example.lab4;

import android.app.ActionBar;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;


public class MainActivity extends AppCompatActivity {

    /*所有的商品信息*/
    final String []name={"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    final String []firstLetters={"E","A","D","K","w","M","F","M","L","B"};
    final String []price={"￥5.00","￥59.00","￥79.00","￥2399.00","￥179.00","￥14.90","￥132.59","￥141.43","￥139.43","￥28.90"};
    final String []shortInfo={"作者 Johanna Basford","产地 德国","产地 澳大利亚","版本 8G", "重量 2Kg","产地 英国","重量 300g","重量 118g","重量 249g","重量 640g"};
    final int []imgId={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};

    /*为了回调函数使用这些变量向购物车列表添加商品，声明放在外面*/
    final List<Map<String,Goods>> listItems1 = new ArrayList<>();
    final List<Map<String,Goods>> listItems2 = new ArrayList<>();
    private List<Map<String,Object>> simpleListItems2 = Goods.getSimpleList(listItems2);
    Map<String,Goods> s;
    Map<String,Object> s2;
    SimpleAdapter simpleAdapter;

    //这两个声明放在onCreate外面不能初始化byid！！！！！！！！！！！！！！！！！！！！！！！！！！
    private FloatingActionButton fab1 ;
    private FloatingActionButton fab2 ;
    private FloatingActionButton fab3 ;
    //这两个声明放在onCreate外面不能初始化byid！！！！！！！！！！！！！！！！！！！！！！！！！！

    //广播使用的filter
    String STATICACTION="com.example.lab4.STATICACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //商品列表的初始化
        //final List<Map<String,Object>> listItems1 = new ArrayList<>();
        for(int i=0;i<name.length;i++){
            Map<String,Goods> tmp = new LinkedHashMap<>();
            Goods tmpG = new Goods(firstLetters[i],name[i],price[i],shortInfo[i],imgId[i],false);
            tmp.put("Goods",tmpG);
            listItems1.add(tmp);
        }


        //eventbus的注册
        EventBus.getDefault().register(this);


        //两个右下角按钮的初始化以及将进入页面初始化为商品列表
        fab1 = (FloatingActionButton) findViewById(R.id.fabSL1);
        fab2 = (FloatingActionButton) findViewById(R.id.fabSL2);
        changeToRecyclerView();

        //发送静态广播推送推荐商品
        BroadcastStatic(STATICACTION);


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToListView();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToRecyclerView();
            }
        });

        //RecyclerView实现物品清单begin/////////////////
        final RecyclerView mRecyclerView;
        final CommonAdapter recycleAdapter;
        final RecyclerView.LayoutManager mLayoutManager;



        mLayoutManager = new LinearLayoutManager(this);
        recycleAdapter = new CommonAdapter(this,R.layout.recyclerviewitem,listItems1){
            @Override
            public void convert(ViewHolder holder,Map<String,Goods> s){
                TextView itemImgInR = holder.getView(R.id.itemImgInR);
                itemImgInR.setText(s.get("Goods").getfirstLetters());
                TextView itemNameInR = holder.getView(R.id.itemNameInR);
                itemNameInR.setText(s.get("Goods").getname());
            }
        };
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recycleAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view,int position) {
                s=listItems1.get(position);
                Goods tmpG=s.get("Goods");

                /*跳转至商品详情界面*/
                Bundle bundle = tmpG.putInBundle();
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,ItemInfo.class);
                startActivityForResult(intent,1);
            }
            @Override
            public boolean onLongClick(View view,int position) {
                s=listItems1.get(position);
                Snackbar.make( view,"商品 " + s.get("Goods").getname() + "从列表移除", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                listItems1.remove(position);
                recycleAdapter.notifyItemRemoved(position);
                recycleAdapter.notifyItemChanged(position);
                return true;
            }
        });


        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(recycleAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        //mRecyclerView.setAdapter(recycleAdapter);/////
        //RecyclerView实现物品清单end/////////////////////////////


        //ListView实现购物车清单begin/////////////////////////////
        //初始化向购物车列表加入表头
        Map<String,Goods> tmp = new LinkedHashMap<>();
        Goods tmpG=new Goods("*","购物车","价格","价格",0,false);
        tmp.put("Goods",tmpG);
        listItems2.add(tmp);


        final ListView listview = (ListView) findViewById(R.id.ListView);
        simpleListItems2 = Goods.getSimpleList(listItems2);
        simpleAdapter = new SimpleAdapter(
                this,simpleListItems2,R.layout.listviewitem,
                new String[]{"firstLetters","name","price"},new int []{R.id.itemImgInL,R.id.itemNameInL,R.id.itemPriceInL});
        listview.setAdapter(simpleAdapter);

        final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) return ;
                s=listItems2.get(position);
                Goods tmpG=listItems2.get(position).get("Goods");
                Bundle bundle = tmpG.putInBundle();
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,ItemInfo.class);
                startActivityForResult(intent,1);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                if(position==0) return true;
                s=listItems2.get(position);
                /*购物车界面长按删除提示信息*/
                alterDialog.setTitle("移除商品").setMessage("从购物车移除"+s.get("Goods").getname()+"?").setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                ).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make( view,"商品 " + s.get("Goods").getname() + "从购物车移除", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                listItems2.remove(position);
                                simpleListItems2.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        }
                ).create();
                alterDialog.show();
                return true;
            }
        });
        //ListView实现购物车清单end//////////////////////////////////


    }

    /*回调函数，当从子界面回到这个父界面时调用，如果回传数据不为空也即是按下加入购物车则更新购物车列表*/
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(requestCode==1){
            if(resultCode==1){
                Bundle bundle=getIntent().getExtras();
                if(bundle!=null){
                    if(bundle.getInt("whichView")==0){
                        changeToRecyclerView();

                    }
                    else{
                        changeToListView();
                    }
                }
            }
        }
    }
    public void BroadcastStatic(String message){
        Random random=new Random();
        int index=random.nextInt(10);
        Goods tmpG=listItems1.get(index).get("Goods");
        Bundle bundle = tmpG.putInBundle();
        Intent intentBroadcast = new Intent(message);
        intentBroadcast.putExtras(bundle);
        sendBroadcast(intentBroadcast);
    }


    //声明并重写自己的订阅函数
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        Goods tmpG=messageEvent.getGoods();
        Map<String,Goods> tmp = new LinkedHashMap<>();
        tmp.put("Goods",tmpG);
        listItems2.add(tmp);
        Map<String,Object> tmp1 = new LinkedHashMap<>();
        tmp1.put("firstLetters",tmpG.getfirstLetters());
        tmp1.put("name",tmpG.getname());
        tmp1.put("price",tmpG.getprice());
        simpleListItems2.add(tmp1);
        simpleAdapter.notifyDataSetChanged();
    }



    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            if(bundle.getInt("whichView")==0){
                changeToRecyclerView();
            }
            else{
                changeToListView();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void changeToRecyclerView(){
        RecyclerView reLayout =(RecyclerView) findViewById(R.id.RecyclerView);
        reLayout.setVisibility(View.VISIBLE);
        ListView liLayout =(ListView) findViewById(R.id.ListView);
        liLayout.setVisibility(View.GONE);
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.GONE);
    }

    public void changeToListView(){
        RecyclerView reLayout =(RecyclerView) findViewById(R.id.RecyclerView);
        reLayout.setVisibility(View.GONE);
        ListView liLayout =(ListView) findViewById(R.id.ListView);
        liLayout.setVisibility(View.VISIBLE);
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.VISIBLE);
    }
    ///////////////////////////////////////////////
}

