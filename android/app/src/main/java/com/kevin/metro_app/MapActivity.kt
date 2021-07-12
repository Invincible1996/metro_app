package com.kevin.metro_app

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.TextureMapView
import com.amap.api.maps.model.MyLocationStyle

class MapActivity : AppCompatActivity(), LocationSource, AMapLocationListener {

  private lateinit var mapView: TextureMapView
  private lateinit var aMap: AMap
  private lateinit var locationClient: AMapLocationClient
  private lateinit var locationClientOption: AMapLocationClientOption
  private var listener: LocationSource.OnLocationChangedListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_map)
    mapView = findViewById<TextureMapView>(R.id.map)
    mapView.onCreate(savedInstanceState)

    aMap = mapView.map



    showLocationIcon()
  }

  private fun showLocationIcon() {

    aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0F))
    aMap.setLocationSource(this)

    val locationStyle = MyLocationStyle();
    locationStyle.interval(2000)
    locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
    locationStyle.showMyLocation(true)

    aMap.myLocationStyle = locationStyle
    aMap.isMyLocationEnabled = true
  }

  override fun onDestroy() {
    super.onDestroy()
    //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
    mapView.onDestroy()
    locationClient.onDestroy()
  }

  override fun onResume() {
    super.onResume()
    //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
    mapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
    mapView.onPause()
  }

  override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
    super.onSaveInstanceState(outState, outPersistentState)
    mapView.onSaveInstanceState(outState);
  }

  override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener?) {

    listener = onLocationChangedListener!!

    locationClient = AMapLocationClient(this)
    locationClientOption = AMapLocationClientOption();
    locationClient.setLocationListener(this)
    // 设置为高精度模式
    locationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy

    //设置定位参数
    locationClient.setLocationOption(locationClientOption)

    locationClient.startLocation()

  }

  override fun deactivate() {
    listener = null
    locationClient.stopLocation()
    locationClient.onDestroy()
  }

  override fun onLocationChanged(location: AMapLocation?) {
    if (listener != null) {
      if (location != null && location.errorCode == 0) {
        listener!!.onLocationChanged(location)
      }
    } else {
      val errText = "定位失败," + location!!.errorCode.toString() + ": " + location.errorCode
      Log.e("定位Err", errText)
    }
  }
}
