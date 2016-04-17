package com.will.adaptor;

import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by will on 2016/2/12.
 */
public abstract class AdapetBase<E> extends BaseAdapter {

    Context mcontext;
    public List<E> list;
    public Map<Integer,onInternalClickListener> ItemClicklist;


    public AdapetBase(Context context) {
        super();
        mcontext=context;
    }

    public AdapetBase(Context con,List<E> list) {
        super();
        this.list = list;
        mcontext=mcontext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=bindView(position,convertView,parent);
        bindIntenalListener(convertView,position,list.get(position));
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //添加内部控件监听者
    public void addItemClicklist(final Integer ID,onInternalClickListener listener){
        if(ItemClicklist==null){
            ItemClicklist=new HashMap<Integer,onInternalClickListener>();
        }
        ItemClicklist.put(ID,listener);
    }

    //绑定内部监听者
    public void bindIntenalListener(final View convertView,final int position,final Object value){
        if(ItemClicklist!=null){
            for(Integer id:ItemClicklist.keySet()) {
                final View inView = convertView.findViewById(id);
                final onInternalClickListener listener=ItemClicklist.get(id);
                inView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listener.OnClickListener(convertView,v,position,value);                   }
                });
            }
        }
    }

    //绑定视图
    public abstract View bindView(int position, View convertView, ViewGroup parent);


    public void addList(List<E> l){
        list=l;
        notifyDataSetChanged();
    }

    public List<E> getList(){
        return list;
    }
    public void add(E e){
        list.add(e);
        notifyDataSetChanged();
    }
    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }


    public interface onInternalClickListener{
        public void OnClickListener(View parent,View v,Integer position,Object values);
    }
}
