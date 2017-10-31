package com.example.lab4;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;


public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder>{
    private List<Map<String,Goods>> mData;
    private Context mContext;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter(Context context,int mLayoutId,List<Map<String,Goods>> mData) {
        this.mContext = context;
        this.mData=mData;
        this.mLayoutId=mLayoutId;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        //实例化viewHolder
        ViewHolder viewHolder = ViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        convert(holder,mData.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(v,holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    boolean rt;
                    rt = mOnItemClickListener.onLongClick(v,holder.getAdapterPosition());
                    return rt;
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder,Map<String,Goods> s);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public  interface  OnItemClickListener{
        void onClick(View v,int position);
        boolean onLongClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }



}
