package com.kevin.metro_app

import android.content.Intent
import com.kevin.metro_app.util.AMapManager
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
        "queryBusStations" -> {
          println(call.method)
          println(call.argument<String>("search_text"))
          val searchValue = call.argument<String>("search_text")
          val cityName = call.argument<String>("city_name")
          AMapManager.queryBusStations(methodChannel, applicationContext, searchValue, cityName)

        }
        "queryBusLines" -> {
          println(call.method)
          println(call.argument<String>("search_text"))
          val searchValue = call.argument<String>("search_text")
          val cityCode = call.argument<String>("city_code")
          AMapManager.queryBusLines(methodChannel, applicationContext, searchValue, cityCode, 1);

        }
        "openMapView" -> {
          val intent = Intent(applicationContext, MapActivity::class.java)
          startActivity(intent)
        }
      }
    }
  }
}
