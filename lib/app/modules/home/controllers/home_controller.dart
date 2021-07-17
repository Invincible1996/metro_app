import 'dart:async';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:metro_app/app/modules/home/model/station_model.dart';

class HomeController extends GetxController {
  final MethodChannel platform = const MethodChannel('com.kevin.cyclingApp');
  var lineList = <Line>[].obs;

  var stationList = <String>[].obs;

  TextEditingController textEditingController = TextEditingController();

  //设置防抖周期为3s
  Duration durationTime = Duration(seconds: 2);
  Timer? timer;

  @override
  void onInit() {
    super.onInit();
    platform.setMethodCallHandler(_handler);
  }

  @override
  void onReady() {
    super.onReady();
    // getStationList(cityCode: '3100', cityName: 'shanghai');
    // getStationList(cityCode: '3205', cityName: 'suzhou');
  }

  /// @create at 2021/7/8 17:42
  /// @desc
  getStationList({required String cityCode, required String cityName}) async {
    Dio dio = Dio();
    var response = await dio.get(
        'https://map.amap.com/service/subway?_${DateTime.now().millisecondsSinceEpoch}&srhdata=${cityCode}_drw_$cityName.json');
    // print(response);
    StationModel stationModel = StationModel.fromJson(response.data);

    print(stationModel.l!.length);
    lineList.assignAll(stationModel.l!);
  }

  @override
  void onClose() {}

  /// @create at 2021/7/12 10:11
  /// @desc
  Future<Null> _handler(MethodCall call) async {
    print(call.method);
    print(call.arguments);
    stationList.clear();

    switch (call.method) {
      case "onBusLineSearched":
        print(call.arguments);
        if (call.arguments is List) {
          call.arguments.forEach((item) {
            stationList.add(item['busLineName']);
          });
        }
        break;
      case "onBusStationSearched":
        if (call.arguments is List) {
          call.arguments.forEach((item) {
            stationList.add(item);
          });
        }
        print(stationList);
        break;
      case "openMapView":
        break;
    }
  }
}
