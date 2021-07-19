package com.kevin.metro_app

import android.os.Bundle
import com.kevin.metro_app.plugin.AMapPlugin
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {
//
//  companion object {
//    const val CHANNEL = "com.kevin.cyclingApp"
//  }
//
//  private lateinit var methodChannel: MethodChannel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    GeneratedPluginRegistrant.registerWith(flutterEngine)
    AMapPlugin.registerWith(flutterEngine)
  }

//  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//    super.configureFlutterEngine(flutterEngine)
//    methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
//    methodChannel.setMethodCallHandler { call, _ ->
//      when (call.method) {
//        Constant.QUERY_BUS_STATIONS -> {
//          println(call.method)
//          println(call.argument<String>("search_text"))
//          val searchValue = call.argument<String>("search_text")
//          val cityName = call.argument<String>("city_name")
//          AMapManager.queryBusStations(methodChannel, applicationContext, searchValue, cityName)
//
//        }
//        Constant.QUERY_BUS_LINES -> {
//          println(call.method)
//          println(call.argument<String>("search_text"))
//          val searchValue = call.argument<String>("search_text")
//          val cityCode = call.argument<String>("city_code")
//          AMapManager.queryBusLines(methodChannel, applicationContext, searchValue, cityCode, 1);
//
//        }
//        Constant.OPEN_MAP_VIEW -> {
//          val intent = Intent(applicationContext, MapActivity2::class.java)
//          startActivity(intent)
//        }
//        Constant.TRACK_SEARCH_ACTIVITY -> {
//          val intent = Intent(applicationContext, TrackSearchActivity::class.java)
//          startActivity(intent)
//        }
//        Constant.SEARCH_LOCATION -> {
//          val intent = Intent(applicationContext, OtherSearchActivity::class.java)
//          startActivity(intent)
//        }
//        Constant.PLACE_SEARCH -> {
//          val intent = Intent(applicationContext, PlaceSearchActivity::class.java)
//          startActivity(intent)
//        }
//      }
//    }
//  }
}
