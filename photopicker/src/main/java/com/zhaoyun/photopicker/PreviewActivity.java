package com.zhaoyun.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoyun.photopicker.beans.Photo;
import com.zhaoyun.photopicker.utils.OtherUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * @Class: PreviewActivity
 * @Description: 图片预览界面
 * @author: zhaoyun
 * @Date: 2016/08/30
 */
public class PreviewActivity extends Activity {

    public final static String TAG = "PreviewActivity";
    /**
     * 图片数据
     */
    public final static String EXTRA_DATA = "extra_data";
    /**
     * 当前索引
     */
    public final static String EXTRA_CURRENT_INDEX = "extra_current_index";
    /**
     * 最大数
     */
    public final static String EXTRA_MAX_NUM = "extra_max_num";
    /**
     * 选择的结果集
     */
    public final static String EXTRA_SELECTED_LIST = "extra_selected_list";

    public final static String EXTRA_TYPE = "EXTRA_TYPE";

    private ViewPager mViewPager;
    private CheckBox mCheckBox;
    private TextView mTitle;
    private Button mCommitBtn;

    private ImageAdapter imageAdapter;
    private ArrayList<Photo> mDatas = new  ArrayList<Photo>();
    private int mCurrentIndex = 0;
    private int mMaxNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initIntentParams();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox.setChecked(mDatas.get(mCurrentIndex).isSelected());
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText((mCurrentIndex + 1) + "/" + mDatas.size());
        ((RelativeLayout) findViewById(R.id.bottom_tab_bar)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //消费触摸事件，防止触摸底部tab栏也会选中图片
                return true;
            }
        });
        ((ImageView) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnData(2);
                finish();
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatas.get(mCurrentIndex).setIsSelected(isChecked);
                photoChange();
            }
        });

        imageAdapter = new ImageAdapter(PreviewActivity.this);
        mViewPager.setAdapter(imageAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrentIndex = arg0;
                mTitle.setText((mCurrentIndex + 1) + "/" + mDatas.size());
                mCheckBox.setChecked(mDatas.get(mCurrentIndex).isSelected());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        mCommitBtn = (Button) findViewById(R.id.commit);
        mCommitBtn.setVisibility(View.VISIBLE);
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnData(1);
                finish();
            }
        });
        photoChange();
    }

    private void photoChange(){
        int selectedCount = 0;
        for(Photo photo: mDatas){
            if(photo.isSelected()){
                selectedCount++;
            }
        }
        if(selectedCount>0) {
            mCommitBtn.setEnabled(true);
            mCommitBtn.setText(OtherUtils.formatResourceString(getApplicationContext(),
                    R.string.commit_num, selectedCount, mMaxNum));
        } else {
            mCommitBtn.setEnabled(false);
            mCommitBtn.setText(R.string.commit);
        }
    }
    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        Bundle bundle = getIntent().getExtras();
        mDatas =(ArrayList<Photo>)bundle.getSerializable(EXTRA_DATA);
        mCurrentIndex = bundle.getInt(EXTRA_CURRENT_INDEX, 0);
        mMaxNum = bundle.getInt(EXTRA_MAX_NUM,0);
    }

    private void returnData(int type){
        ArrayList<String> selectList = new ArrayList<String>();
        for (Photo photo : mDatas){
            if(photo.isSelected()) {
                selectList.add(photo.getPath());
            }
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TYPE,type);
        data.putStringArrayListExtra(EXTRA_SELECTED_LIST,selectList);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            returnData(2);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    class ImageAdapter extends PagerAdapter {
        private Context mContext;

        public ImageAdapter(Context mContext) {
            // TODO Auto-generated constructor stub
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            int count=0;
           if(mDatas.size()>0){//本地图片
                count= mDatas.size();
            }
            return count;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            // TODO Auto-generated method stub
            super.finishUpdate(container);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "position:" + position);
            ImageView imageview = new ImageView(mContext);
            imageview.setAdjustViewBounds(true);
            imageview.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT));

            String iFilePath = mDatas.get(position).getPath();
            imageview.setImageDrawable(Drawable.createFromPath(new File(iFilePath).getAbsolutePath()));

            ((ViewPager) container).addView(imageview);

            return imageview;
        }

    }
}
