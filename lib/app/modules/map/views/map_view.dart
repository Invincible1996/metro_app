import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/map_controller.dart';

class MapView extends GetView<MapController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('MapView'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          ElevatedButton(
            onPressed: () {
              controller.platform.invokeMethod(
                "searchLocation",
                {
                  // 'search_text': controller.textEditingController.text,
                  'city_code': '0512',
                },
              );
            },
            child: Text(
              '查询当前位置',
            ),
          ),
          ElevatedButton(
            onPressed: () {
              controller.platform.invokeMethod(
                "trackSearchActivity",
                {
                  // 'search_text': controller.textEditingController.text,
                  'city_code': '0512',
                },
              );
            },
            child: Text(
              '开启轨迹追踪',
            ),
          ),
        ],
      ),
    );
  }
}
