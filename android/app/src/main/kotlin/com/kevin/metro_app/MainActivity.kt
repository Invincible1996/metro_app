package com.kevin.metro_app

import android.content.Intent
import com.kevin.metro_app.util.AMapManager
import com.kevin.metro_app.util.Constant
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

  companion object {
    const val CHANNEL = "com.kevin.cyclingApp"
  }

  private lateinit var methodChannel: MethodChannel

  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
    methodChannel.setMethodCallHandler { call, _ ->
      when (call.method) {
        Constant.QUERY_BUS_STATIONS -> {
          println(call.method)
          println(call.argument<String>("search_text"))
          val searchValue = call.argument<String>("search_text")
          val cityName = call.argument<String>("city_name")
          AMapManager.queryBusStations(methodChannel, applicationContext, searchValue, cityName)

        }
        Constant.QUERY_BUS_LINES -> {
          println(call.method)
          println(call.argument<String>("search_text"))
          val searchValue = call.argument<String>("search_text")
          val cityCode = call.argument<String>("city_code")
          AMapManager.queryBusLines(methodChannel, applicationContext, searchValue, cityCode, 1);

        }
        Constant.OPEN_MAP_VIEW -> {
          val intent = Intent(applicationContext, MapActivity2::class.java)
          startActivity(intent)
        }
        Constant.TRACK_SEARCH_ACTIVITY -> {
          val intent = Intent(applicationContext, TrackSearchActivity::class.java)
          startActivity(intent)
        }
        Constant.SEARCH_LOCATION -> {
          val intent = Intent(applicationContext, OtherSearchActivity::class.java)
          startActivity(intent)
        }
      }
    }
  }
}
