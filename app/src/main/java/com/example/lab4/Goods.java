package com.example.lab4;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingming on 2017/10/26.
 */

public class Goods implements Serializable{
    private String firstLetters;//商品的名字的首字母
    private String name;//商品名字
    private String price;//商品价格
    private String shortInfo;//商品的其他信息
    private int imgId;//商品对应的图片id
    private boolean favorite;//标记是否被收藏
    public Goods(){
        this.name="";
        this.price="";
        this.shortInfo="";
        this.firstLetters="";
        this.imgId=0;
        this.favorite=false;
    }
    public Goods(Bundle bundle){
        setByBundle(bundle);
    }
    public Goods(String firstLetters,String name,String price,String shortInfo,int imgId,boolean favorite){
        this.firstLetters=firstLetters;
        this.name=name;
        this.price=price;
        this.shortInfo=shortInfo;
        this.imgId=imgId;
        this.favorite=favorite;
    }
    public Goods(Goods goods){
        this.firstLetters=goods.getfirstLetters();
        this.name=goods.getname();
        this.price=goods.getprice();
        this.shortInfo=goods.getshortInfo();
        this.imgId=goods.getimgId();
        this.favorite=goods.getfavorite();
    }
    public String getfirstLetters(){
        return firstLetters;
    }
    public String getname(){
        return name;
    }
    public String getprice(){
        return price;
    }
    public String getshortInfo(){
        return shortInfo;
    }

    public int getimgId(){
        return imgId;
    }
    public boolean getfavorite(){
        return favorite;
    }
    public void  setfavorite(boolean favorite){
        this.favorite=favorite;
    }
    public Bundle putinbundle(){
        Bundle bundle = new Bundle();
        bundle.putString("firstLetters",getfirstLetters());
        bundle.putString("name",getname());
        bundle.putString("price",getprice());
        bundle.putString("shortInfo",getshortInfo());
        bundle.putInt("imgId",getimgId());
        bundle.putBoolean("favorite",getfavorite());
        return bundle;
    }
    public Bundle putInBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("firstLetters",getfirstLetters());
        bundle.putString("name",getname());
        bundle.putString("price",getprice());
        bundle.putString("shortInfo",getshortInfo());
        bundle.putInt("imgId",getimgId());
        bundle.putBoolean("favorite",getfavorite());
        return bundle;
    }
    public void setByBundle(Bundle bundle){
        this.firstLetters = bundle.getString("firstLetters");
        this.name = bundle.getString("name");
        this.price=bundle.getString("price");
        this.shortInfo=bundle.getString("shortInfo");
        this.imgId=bundle.getInt("imgId");
        this.favorite=bundle.getBoolean("favorite");
    }
    public static List<Map<String,Object>> getSimpleList(List<Map<String,Goods>> listItems2){
        List<Map<String,Object>> simpleListItems2=new ArrayList<>();
        for(int i=0;i<listItems2.size();i++){
            Map<String,Object> tmp=new LinkedHashMap<>();
            tmp.put("firstLetters",listItems2.get(i).get("Goods").getfirstLetters());
            tmp.put("name",listItems2.get(i).get("Goods").getname());
            tmp.put("price",listItems2.get(i).get("Goods").getprice());
            simpleListItems2.add(tmp);
        }
        return simpleListItems2;
    }
}