package com.example.xianfish.utils;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;


public class mPagerAdapter extends PagerAdapter {
    private int [] mImage;

    public mPagerAdapter(int[] mImage) {
        this.mImage = mImage;//接收传入的mIamge
    }

    @Override
    public int getCount() {
        return 3;//在Viewpager显示3个页面
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    //设置每一页显示的内容
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(mImage[position]);//ImageView设置图片
        container.addView(imageView); // 添加到ViewPager容器
        return imageView;// 返回填充的View对象
    }
    // 销毁条目对象
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
