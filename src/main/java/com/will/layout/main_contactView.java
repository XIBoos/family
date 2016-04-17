package com.will.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.will.adaptor.ContactAdapet;

import com.will.adaptor.contact_ViewPagerAdapet;
import com.will.family.R;

import com.will.utils.DensityUtil;
import com.will.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by will on 2016/3/27.
 */
public class main_contactView extends LinearLayout {

    private Scroller mScroller;

    private LinearLayout mLinearLayout;
    private ViewGroup TopView;


    private ViewPager viewPager;
    private View contactView;
    private View queryView;

    private int PADDING;
    private int SCREENWIDTH;
    final public int VIEWWIDTH;

    private int state=0;
    final public int HIDE=0;
    final public int SHOW=1;


    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public Context mcontext;

    private ListView contactList;
    private ListView queryList;

    public main_contactView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mcontext=context;

        mScroller=new Scroller(context);
        //获取屏幕宽度
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        SCREENWIDTH=displayMetrics.widthPixels;
        PADDING= DensityUtil.dpTOpx(context,100);
        VIEWWIDTH=SCREENWIDTH-PADDING;
        Init(context);
        InitDisplayImageOptions();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLinearLayout.getLayoutParams().width=SCREENWIDTH-PADDING;
        mLinearLayout.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;
        TopView.getLayoutParams().height=(int)(VIEWWIDTH*0.618+0.5f);

    }



    private void Init(Context context){
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        mLinearLayout=(LinearLayout)inflater
                .inflate(R.layout.activity_main_contact, null);
        addView(mLinearLayout);
        TopView=(ViewGroup)mLinearLayout.getChildAt(0);

        contactView =inflater.inflate(R.layout.activity_main_contact_list,null);
        queryView=inflater.inflate(R.layout.activity_main_contact_query,null);
        viewPager=(ViewPager)mLinearLayout.findViewById(R.id.contact_viewPager);
        List<View> views=new ArrayList<View>();
        views.add(0, contactView);
        views.add(1, queryView);
        List<String> titles=new ArrayList<String>();
        titles.add("联系人");
        titles.add("查询");
        contact_ViewPagerAdapet adapet=new contact_ViewPagerAdapet(views,titles);
        viewPager.setAdapter(adapet);
        InitContactView();
        InitQueryView(context);
    }

    public void InitCurrentUser(BmobChatUser CurrentUser){
        CircleImageView avatarView=(CircleImageView)TopView.findViewById(R.id.main_contact_top_avat);
        avatarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入个人信息
            }
        });
        //设置头像
        String avatar=CurrentUser.getAvatar();
        imageLoader.displayImage(avatar, avatarView, imageOptions);
        //设置昵称
        TextView Nick=(TextView)TopView.findViewById(R.id.main_contact_top_nick);
        if(CurrentUser.getNick()==null){
            Nick.setText(CurrentUser.getUsername());
        }else{
            Nick.setText(CurrentUser.getNick());
        }
        smoothScrollTo(0, -VIEWWIDTH);
    }

    private void InitContactView(){
        contactList =(ListView) contactView.findViewById(R.id.contact);

    }

    private void InitQueryView(final Context c){
        queryList=(ListView)queryView.findViewById(R.id.query_list);
        final EditText edit=(EditText)queryView.findViewById(R.id.query_eidt);
        Button button=(Button)queryView.findViewById(R.id.query_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String what=edit.getText().toString().trim();
                if (!what.isEmpty()){
                    BmobUserManager.getInstance(c).queryUserByName(what, new FindListener<BmobChatUser>() {
                        @Override
                        public void onSuccess(List<BmobChatUser> list) {
                            ContactAdapet adapet=new ContactAdapet(c,list);
                            queryList.setAdapter(adapet);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
                            Log.d("family","查询失败"+s);
                        }
                    });
                }
            }
        });
        queryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    public void UpdataContactView(Context con,List<BmobChatUser> list){
        contactList.setAdapter(null);
        if (list.size()>0) {
            ContactAdapet adapet = new ContactAdapet(con, list);
            contactList.setAdapter(adapet);
        }
    }



    void InitDisplayImageOptions(){
        imageLoader= ImageLoader.getInstance();
        imageOptions=new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.test_friend_avat)
                .showImageOnLoading(R.drawable.test_master_avat)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.NONE)
                .build();
    }

    public void display(final int s){
        Log.d("family","参数"+s);
        Log.d("family","状态"+state);
        if (s!=state) {
            if (s == HIDE) {
                smoothScrollTo(0, -VIEWWIDTH);
                state=HIDE;
            } else if (s == SHOW) {
                smoothScrollTo(-VIEWWIDTH, VIEWWIDTH);
                state=SHOW;
            }
        }
    }


    public void smoothScrollTo(int startx,int x){
        if(mScroller.getCurrX()!=x) {
            mScroller.forceFinished(true);
            mScroller.startScroll(startx, 0, x, 0,1000);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),0);
            postInvalidate();
        }
    }
}
