import 'package:flutter/services.dart';
import 'package:get/get.dart';

class MapController extends GetxController {
  final MethodChannel platform = const MethodChannel('com.kevin.cyclingApp');

  final count = 0.obs;
  @override
  void onInit() {
    super.onInit();
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {}
  void increment() => count.value++;
}
