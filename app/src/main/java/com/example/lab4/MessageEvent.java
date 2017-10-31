package com.example.lab4;

/**
 * Created by qingming on 2017/10/29.
 */
public  class MessageEvent {
    private Goods goods;
    public MessageEvent(){
        goods=new Goods();
    }
    public MessageEvent(Goods goods1){
        setGoods(goods1);
    }
    public void setGoods(Goods goods1){
        this.goods=goods1;
    }
    public Goods getGoods(){
        return goods;
    }
}