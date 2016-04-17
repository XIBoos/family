package com.will.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;


/**
 * Created by will on 2016/2/1.
 */
public class ChatMsgList extends ListView implements AbsListView.OnScrollListener {

    private pull_refresh_View mheader;
    private RelativeLayout content;
    private ChatMsgListListener mlistener;
    private RefreashMsg refreashMsg;

    View view;
    Context con;


    private float MlastY = -1; //最后事件的Y轴坐标

    public ChatMsgList(Context context) {
        super(context);
        con=context;
        mheader=new pull_refresh_View(context);
        addHeaderView(mheader);
    }


    public ChatMsgList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        con=context;
        mheader=new pull_refresh_View(context);
        addHeaderView(mheader);

    }

    public ChatMsgList(Context context, AttributeSet attrs) {
        super(context, attrs);
        con=context;
        mheader=new pull_refresh_View(context);
        addHeaderView(mheader);
        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                MlastY=ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY=ev.getRawY()-MlastY;
                MlastY=ev.getRawY();

                //纵向滑动
                    if(getFirstVisiblePosition()==0){
                        updataHeard_height((int) deltaY);
                    }
                //否则为横向滑动
                break;
            case MotionEvent.ACTION_UP:
                correctHeard_height();

                break;
            default:

                break;


        }
        return super.onTouchEvent(ev);
    }
    /*
    * @firstVisibleItem 第一个显示的Item的POSITION
    * @visibleItemCount 所有有显示的的Item个数(包括部分显示)
    * @totalItemCount 所有Item的个数*/
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("myself","f"+firstVisibleItem);
        Log.d("myself","v"+visibleItemCount);
        Log.d("myself","t"+totalItemCount);
    }

    /*@param scrollState
    * 屏幕停止滚动为0
    * 屏幕滚动或者手指在屏幕上为1
    * 屏幕惯性滚动为2
    */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("myself", "t" + scrollState);

    }

    public void updataHeard_height(int height){
        int VIS_height=mheader.getVisableHeight();
        //待定取消刷新
        /*if (VIS_height == 0) {
            mHeartView.setState(pull_refresh_View.STATE_NORMAL);
                //缺少取消刷新的操作
        }*/
        if(VIS_height!=0||height>0) {
            if (mheader.getState() != pull_refresh_View.STATE_REFRESHING) {
                if (VIS_height >= 80)
                    mheader.setState(pull_refresh_View.STATE_READY);
                else
                    mheader.setState(pull_refresh_View.STATE_PULL);
            }
            if(VIS_height<=120||height<0) {
                if(height<0)
                    setSelection(0);
                mheader.setVisableHeight(height + VIS_height);
            }
        }

    }

    public void correctHeard_height(){
        int VIS_height=mheader.getVisableHeight();
        if (VIS_height>80) {
            mheader.setVisableHeight(80);
            mheader.setState(pull_refresh_View.STATE_REFRESHING);
            refreashMsg.OnRefreash();
            //缺少刷新操作
        }
        else if (mheader.getState()==pull_refresh_View.STATE_REFRESHING){
            mheader.setVisableHeight(80);
        }else
            mheader.setVisableHeight(0);
    }

    public interface ChatMsgListListener{
        public void OnLoadMore();
    }

    public void setChatMsgListListener(ChatMsgListListener listener){
        mlistener=listener;
    }

    public interface RefreashMsg{
        public void OnRefreash();
    }

    public void setRefreashMsg(RefreashMsg l){
        refreashMsg=l;
    }

    public void resetHeader(){

    }
}
