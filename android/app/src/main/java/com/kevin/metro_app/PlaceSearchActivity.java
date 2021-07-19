package com.kevin.metro_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;

public class PlaceSearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

  private TextureMapView mapView;
  private PoiSearch.Query query;
  private PoiSearch poiSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_search);

    mapView = findViewById(R.id.map_view);
    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(16));
    mapView.onCreate(savedInstanceState);

    // 启动地图内置定位
    mapView.getMap().setMyLocationEnabled(true);
    mapView.getMap().setMyLocationStyle(
      new MyLocationStyle()
        .interval(2000)
        .myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));
    // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
    query = new PoiSearch.Query("上海虹桥", "上海");
    query.setPageSize(10);
    query.setPageNum(1);

    poiSearch = new PoiSearch(this, query);
    poiSearch.setOnPoiSearchListener(this);
    poiSearch.searchPOIAsyn();
  }

  @Override
  public void onPoiSearched(PoiResult poiResult, int i) {
    System.out.println(poiResult);
    List<PoiItem> pois = poiResult.getPois();
    System.out.println(pois);

    Marker marker = mapView.getMap().addMarker(new MarkerOptions());

    for (PoiItem poiItem : pois) {
      LatLonPoint point = poiItem.getLatLonPoint();
      if (point != null) {
        LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
        marker.setPosition(markerPosition);
        mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
      }
      marker.setTitle(poiItem.getTitle());
      marker.setSnippet(poiItem.getSnippet());
    }
  }

  @Override
  public void onPoiItemSearched(PoiItem poiItem, int i) {

  }
}
