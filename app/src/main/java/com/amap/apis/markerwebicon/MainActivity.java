package com.amap.apis.markerwebicon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.URL;

/**
 * Marker显示网络图片
 * 1、添加marker显示默认图标
 * 2、下载图片
 * 3、下载完成后设置给Marker
 * Created by zxy on 2017/8/23.
 */

public class MainActivity extends Activity {
    private AMap aMap;
    private MapView mapView;

    private String iconUrl = "http://lbs.amap.com/web/public/dist/images/favicon.ico";

    private Marker marker;
    private Marker viewMarker;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView = new MapView(this);
        setContentView(mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }


        testMarkerWithBitmap();

        testMarkerWithView();

    }



    /**
     * 从网络获取Bitmap，再设置给Marker
     */
    private void testMarkerWithBitmap() {

        LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
        //添加Marker 并放置一个默认的图标
        marker = aMap.addMarker(new MarkerOptions().position(BEIJING).icon(BitmapDescriptorFactory.defaultMarker()));


        //开启线程下载图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                //下载图片
                BitmapDescriptor bitmapDescriptor = getBitmapDescriptorFromURL(iconUrl);

                //下载完成之后设置给Icon
                marker.setIcon(bitmapDescriptor);
            }
        }).start();
    }


    /**
     * 从网络获取图片，设置给View，然后再从View转为图片，设置给Marker
     */
    private void testMarkerWithView() {

        LatLng BEIJING1 = new LatLng(39.70403, 116.407525);// 北京市经纬度 偏一点点
        viewMarker =  aMap.addMarker(new MarkerOptions().position(BEIJING1));

        //创建一个layout作为存放自定义图标的控件
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final ImageView imageView = new ImageView(this);
        //设置一个默认图标
        imageView.setImageBitmap(BitmapDescriptorFactory.defaultMarker().getBitmap());
        layout.addView(imageView);

        TextView textView = new TextView(this);
        textView.setText("加载网络图标");
        layout.addView(textView);


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(layout));
        viewMarker.setIcon(bitmapDescriptor);

        //开启线程下载图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                //下载图片
                Bitmap bitmap = LoadImageFromWebOperations(iconUrl);
                //下载完成之后设置给View
                imageView.setImageBitmap(bitmap);

                //如果要使用BitmapDescriptorFactory.fromView 需要创建新的layout
                //根据View生成新的BitmapDescriptor
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(layout));
                viewMarker.setIcon(bitmapDescriptor);
            }
        }).start();
    }

    /**
     * 将View转换为Bitmap
     * @param view
     * @return
     */
    private Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

         return bitmap;
    }



    private BitmapDescriptor getBitmapDescriptorFromURL(String url) {
        Bitmap bitmap = LoadImageFromWebOperations(url);
        if(bitmap == null) {
            return  null;
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    /**
     * 根据url获取Bitmap
     * @param url
     * @return
     */
    private Bitmap LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
