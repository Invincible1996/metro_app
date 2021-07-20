import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

class PlaceSearchController extends GetxController {
  TextEditingController textEditingController = TextEditingController();

  ///
  MethodChannel platform = MethodChannel("com.kevin.aMap.plugin");

  var placeList = [].obs;

  //设置防抖周期为3s
  Duration durationTime = Duration(milliseconds: 300);
  Timer? timer;

  @override
  void onInit() {
    super.onInit();
    platform.setMethodCallHandler(_handler);
  }

  @override
  void onReady() {
    super.onReady();
    // queryPlaceList();
  }

  onValueChanged(String value) {
    queryPlaceList(value);
  }

  queryPlaceList(String value) async {
    print(value);
    await platform.invokeMethod("placeSearch", {
      "search_value": value,
    });
  }

  Future _handler(MethodCall call) async {
    switch (call.method) {
      case "onPoiSearched":
        placeList.clear();
        print(call.arguments);
        if (call.arguments is List) {
          call.arguments.forEach((item) => placeList.add(item));
        }
        // placeList.assignAll(['上海虹桥站', '上海虹桥国际机场']);
        break;
      default:
        break;
    }
  }

  /// @create at 2021/7/20 14:43
  /// @desc 绘制标记点
  void addMarkerOnMap(int index) async {
    await platform.invokeMethod("addMarker", {'index': index});
  }

  @override
  void onClose() {}
}
