package com.kevin.metro_app

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.TextureMapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.track.AMapTrackClient
import com.amap.api.track.ErrorCode
import com.amap.api.track.OnTrackLifecycleListener
import com.amap.api.track.TrackParam
import com.amap.api.track.query.model.*


class MapActivity : AppCompatActivity(), LocationSource, AMapLocationListener {

  private lateinit var mapView: TextureMapView
  private lateinit var aMap: AMap
  private lateinit var locationClient: AMapLocationClient
  private lateinit var locationClientOption: AMapLocationClientOption
  private var listener: LocationSource.OnLocationChangedListener? = null
  private var trackId: Long? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_map)
    mapView = findViewById(R.id.map)
    mapView.onCreate(savedInstanceState)
    aMap = mapView.map
    showLocationIcon()

    drawTrajectory()
  }

  /**
   * 猎鹰轨迹
   */
  private fun drawTrajectory() {
    val aMapTrackClient = AMapTrackClient(applicationContext)
//    下面的代码将定位信息采集周期设置为5s，上报周期设置为30s
//    aMapTrackClient.setInterval(5,30)

//    可以使用下面的代码修改缓存大小为20MB
//    aMapTrackClient.setCacheSize(20);

//    可以使用下面的代码修改定位模式为仅设备定位，既仅使用GPS定位
//    aMapTrackClient.setLocationMode(LocationMode.DEVICE_SENSORS);

    val onTrackLifecycleListener = object : OnTrackLifecycleListener {
      override fun onBindServiceCallback(status: Int, msg: String?) {

      }

      override fun onStartGatherCallback(status: Int, msg: String?) {
        if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
          status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
          Toast.makeText(applicationContext, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(applicationContext, "定位采集启动异常，$msg", Toast.LENGTH_SHORT).show();
        }
      }

      override fun onStartTrackCallback(status: Int, msg: String?) {
        if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
          status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
          status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
          // 服务启动成功，继续开启收集上报
          aMapTrackClient.startGather(this);
        } else {
          Toast.makeText(applicationContext, "轨迹上报服务服务启动异常，$msg", Toast.LENGTH_SHORT).show();
        }
      }

      override fun onStopGatherCallback(status: Int, msg: String?) {

      }

      override fun onStopTrackCallback(status: Int, msg: String?) {

      }
    };

    // 获取终端id，启动上报
    aMapTrackClient.queryTerminal(QueryTerminalRequest(Constant.SERVICE_ID, Constant.TERMINAL_NAME), object : OnTrackListener {


      override fun onQueryTerminalCallback(queryTerminalResponse: QueryTerminalResponse?) {
        if (queryTerminalResponse!!.isSuccess) {
          if (queryTerminalResponse.tid <= 0) {
            // terminal还不存在，先创建
            aMapTrackClient.addTerminal(AddTerminalRequest(Constant.TERMINAL_NAME, Constant.SERVICE_ID), object : OnTrackListener {
              override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {

              }

              override fun onCreateTerminalCallback(addTerminalResponse: AddTerminalResponse?) {
                if (addTerminalResponse!!.isSuccess) {
                  // 创建完成，开启猎鹰服务
                  val terminalId = addTerminalResponse.tid
                  val trackParam = TrackParam(Constant.SERVICE_ID, terminalId)
                  trackParam.trackId = trackId!!
                  aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener)
                } else {
                  // 请求失败
                  Toast.makeText(applicationContext, "请求失败，${addTerminalResponse.errorMsg}", Toast.LENGTH_SHORT).show()
                }
              }

              override fun onDistanceCallback(p0: DistanceResponse?) {
                TODO("Not yet implemented")
              }

              override fun onLatestPointCallback(p0: LatestPointResponse?) {
                TODO("Not yet implemented")
              }

              override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {
                TODO("Not yet implemented")
              }

              override fun onQueryTrackCallback(p0: QueryTrackResponse?) {
                TODO("Not yet implemented")
              }

              override fun onAddTrackCallback(addTrackResponse: AddTrackResponse?) {
                if (addTrackResponse!!.isSuccess) {
                  trackId = addTrackResponse.trid
                }
              }

              override fun onParamErrorCallback(p0: ParamErrorResponse?) {
                TODO("Not yet implemented")
              }

            })
          } else {
            // terminal已经存在，直接开启猎鹰服务
            val terminalId = queryTerminalResponse.tid;
            val trackParam = TrackParam(Constant.SERVICE_ID, terminalId)
            trackParam.trackId = trackId!!
            aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener)
          }
        } else {
          Toast.makeText(applicationContext, "请求失败，${queryTerminalResponse.errorMsg}", Toast.LENGTH_SHORT).show();
        }
      }

      override fun onCreateTerminalCallback(addTerminalResponse: AddTerminalResponse?) {
        TODO("Not yet implemented")
      }

      override fun onDistanceCallback(distanceResponse: DistanceResponse?) {
        TODO("Not yet implemented")
      }

      override fun onLatestPointCallback(latestPointRequest: LatestPointResponse?) {
        TODO("Not yet implemented")
      }

      override fun onHistoryTrackCallback(historyTrackResponse: HistoryTrackResponse?) {
        TODO("Not yet implemented")
      }

      override fun onQueryTrackCallback(queryTrackResponse: QueryTrackResponse?) {
        TODO("Not yet implemented")
      }

      override fun onAddTrackCallback(addTrackResponse: AddTrackResponse?) {
        if (addTrackResponse!!.isSuccess) {
          trackId = addTrackResponse.trid
        }
      }

      override fun onParamErrorCallback(paramErrorResponse: ParamErrorResponse?) {
        TODO("Not yet implemented")
      }
    })
  }


  /**
   * 添加图标点
   */
  private fun addMarkerByLatLng() {
    val latLng = LatLng(31.220412, 121.430635)
    aMap.addMarker(MarkerOptions().position(latLng).title("江苏路地铁站").snippet("DefaultMarker"))

    val latLng2 = LatLng(31.319675, 121.276906)
    aMap.addMarker(MarkerOptions().position(latLng2).title("马陆地铁站").snippet("DefaultMarker"))

    aMap.setOnMarkerClickListener { _ ->
      true
    }
  }


  /**
   *
   */
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
