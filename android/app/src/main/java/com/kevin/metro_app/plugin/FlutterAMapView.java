package com.kevin.metro_app.plugin;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.kevin.metro_app.util.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;


public class FlutterAMapView implements PlatformView, MethodChannel.MethodCallHandler, AMap.OnMapLoadedListener {

  private TextureMapView mapView;
  private MethodChannel methodChannel;

  private Context context;
  private PoiSearch.Query query;
  private PoiSearch poiSearch;
  private List<PoiItem> pois;
  //声明AMapLocationClient类对象
  public AMapLocationClient mLocationClient = null;
  public AMapLocationListener mLocationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
      System.out.println(aMapLocation.getLatitude());
      Toast.makeText(context, aMapLocation.getAddress(), Toast.LENGTH_SHORT).show();
    }
  };
  //声明AMapLocationClientOption对象
  public AMapLocationClientOption mLocationOption = null;

  public FlutterAMapView(@NotNull Context context, BinaryMessenger binaryMessenger, int id, @Nullable Map<String, Object> creationParams) {
    this.context = context;
    AMapOptions aMapOptions = new AMapOptions();
    mapView = new TextureMapView(context, aMapOptions);
    mapView.getMap().setOnMapLoadedListener(this);
    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
    mapView.onCreate(null);

    //初始化定位
    mLocationClient = new AMapLocationClient(context);
//设置定位回调监听
    mLocationClient.setLocationListener(mLocationListener);
    mLocationOption = new AMapLocationClientOption();
    mLocationOption.setOnceLocation(true);
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//    mLocationOption.setInterval(1000);

    mLocationClient.setLocationOption(mLocationOption);
//启动定位
    mLocationClient.startLocation();
//    MyLocationStyle myLocationStyle;
    // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//    myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
// 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//    myLocationStyle.interval(2000);
//    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
//    mapView.getMap().setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//    mapView.getMap().setMyLocationEnabled(true);
//    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);

    methodChannel = new MethodChannel(binaryMessenger, AMapPlugin.A_MAP_VIEW_TYPE);
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull @NotNull MethodCall call, @NonNull @NotNull MethodChannel.Result result) {
    System.out.println("⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️⚽️" + call.method);
    if (call.method.equals(Constant.PLACE_SEARCH)) {
      // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
      System.out.println(call.arguments);

      String searchValue = call.argument("search_value");

      System.out.println("🏀🏀🏀🏀🏀🏀🏀🏀🏀🏀🏀🏀🏀" + searchValue);

      query = new PoiSearch.Query(searchValue, "", "上海");
      query.setPageSize(20);
      query.setPageNum(1);

      poiSearch = new PoiSearch(context, query);
      poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
          System.out.println(poiResult.getPois());
          pois = poiResult.getPois();
          List<Map<String, Object>> data = new ArrayList<>();
          for (PoiItem poiItem : pois) {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", poiItem.getTitle());
            dataMap.put("snippet", poiItem.getSnippet());
            dataMap.put("latitude", poiItem.getLatLonPoint().getLatitude());
            dataMap.put("longitude", poiItem.getLatLonPoint().getLongitude());
            data.add(dataMap);
          }
          methodChannel.invokeMethod("onPoiSearched", data);
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
      });
      poiSearch.searchPOIAsyn();
    } else if (call.method.equals("addMarker")) {

      int index = call.argument("index");
      PoiItem poiItem = pois.get(index);
      Marker marker = mapView.getMap().addMarker(new MarkerOptions());
      LatLonPoint point = poiItem.getLatLonPoint();
      if (point != null) {
        LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
        marker.setPosition(markerPosition);
        mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 14));
      }
      marker.setTitle(poiItem.getTitle());
      marker.setSnippet(poiItem.getSnippet());
    } else {
      System.out.println("方法名不匹配");
    }
  }

  @Override
  public View getView() {
    return mapView;
  }

  @Override
  public void dispose() {

  }

  @Override
  public void onMapLoaded() {
//    LatLng marker1 = new LatLng(31.220411, 121.430642);
//    //设置中心点和缩放比例
//    mapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(marker1));
//    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
  }
}
