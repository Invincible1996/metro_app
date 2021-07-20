package com.kevin.metro_app.plugin;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;


public class FlutterAMapView implements PlatformView, MethodChannel.MethodCallHandler {

  private TextureMapView mapView;
  private MethodChannel methodChannel;

  private Context context;
  private PoiSearch.Query query;
  private PoiSearch poiSearch;
  private List<PoiItem> pois;


  public FlutterAMapView(@NotNull Context context, BinaryMessenger binaryMessenger, int id, @Nullable Map<String, Object> creationParams) {
    this.context = context;
    AMapOptions aMapOptions = new AMapOptions();
    mapView = new TextureMapView(context, aMapOptions);
    mapView.onCreate(null);

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

      query = new PoiSearch.Query(searchValue, "上海");
      query.setPageSize(20);
      query.setPageNum(1);

      poiSearch = new PoiSearch(context, query);
      poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
          System.out.println(poiResult.getPois());
          pois = poiResult.getPois();
          List<String> dataList = new ArrayList<>();
          if ((pois.size() > 0)) {
            for (PoiItem poiItem : pois) {
              System.out.println(poiItem.getTitle());
              dataList.add(poiItem.getTitle());
            }
            methodChannel.invokeMethod("onPoiSearched", dataList);
          }
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
        mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
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
}
