# android-markerwebicon
基于高德地图Android API Marker展示网络图片的示例

## 前述 ##
- [高德官网申请Key](http://lbs.amap.com/dev/#/).
- 阅读[参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html).
- 工程基于Android 3D地图SDK实现


## 实现步骤 ##
1. 添加Marker并设置默认图标；
2. 开启子线程下载网络图片；
3. 将图片生成BitmapDescriptor；
4. 调用Marker的setIcon更新图片

***注：如果是从View生成的图片，网络图片下载完成后也需要走一遍生成新的BitmapDescriptor并设置给Marker的流程***

