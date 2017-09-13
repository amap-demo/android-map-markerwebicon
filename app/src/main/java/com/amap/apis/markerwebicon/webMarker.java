package com.amap.apis.markerwebicon;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by my94493 on 2017/9/13.
 */

public class webMarker {

    public Marker marker;
    public String iconUrl;
    public MarkerOptions options;
    public AMap amap;
    public webMarker(AMap amap, String miconUrl, MarkerOptions moptions){
        this.amap = amap;
        this.options = moptions;
        this.iconUrl = miconUrl;
        addMarker();
    }

    private void addMarker() {
        //开启线程下载图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                //下载图片
                BitmapDescriptor bitmapDescriptor = getBitmapDescriptorFromURL(iconUrl);

                //下载完成之后设置给Icon
                options.icon(bitmapDescriptor);
                marker = amap.addMarker(options);
            }
        }).start();
    }

    public Marker getMarker(){
        return marker;
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
}
