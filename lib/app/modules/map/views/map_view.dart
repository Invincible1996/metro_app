import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:metro_app/app/native_view/a_map_view.dart';
import 'package:metro_app/app/routes/app_pages.dart';

import '../controllers/map_controller.dart';

class MapView extends GetView<MapController> {
  @override
  Widget build(BuildContext context) {
    // return Scaffold(
    //   appBar: AppBar(
    //     title: Text('MapView'),
    //     centerTitle: true,
    //   ),
    //   body: Column(
    //     children: [
    //       ElevatedButton(
    //         onPressed: () {
    //           controller.platform.invokeMethod(
    //             "searchLocation",
    //             {
    //               // 'search_text': controller.textEditingController.text,
    //               'city_code': '0512',
    //             },
    //           );
    //         },
    //         child: Text(
    //           '查询当前位置',
    //         ),
    //       ),
    //       ElevatedButton(
    //         onPressed: () {
    //           controller.platform.invokeMethod(
    //             "openMapView",
    //             {
    //               // 'search_text': controller.textEditingController.text,
    //               'city_code': '0512',
    //             },
    //           );
    //         },
    //         child: Text(
    //           '开启轨迹追踪',
    //         ),
    //       ),
    //       ElevatedButton(
    //         onPressed: () {
    //           controller.platform.invokeMethod(
    //             "trackSearchActivity",
    //             {
    //               // 'search_text': controller.textEditingController.text,
    //               'city_code': '0512',
    //             },
    //           );
    //         },
    //         child: Text(
    //           '开启轨迹查询',
    //         ),
    //       ),
    //       ElevatedButton(
    //         onPressed: () {
    //           controller.platform.invokeMethod(
    //             "placeSearch",
    //             {
    //               // 'search_text': controller.textEditingController.text,
    //               'city_code': '0512',
    //             },
    //           );
    //         },
    //         child: Text(
    //           '兴趣点搜索',
    //         ),
    //       ),
    //       ElevatedButton(
    //         onPressed: () {
    //           controller.platform.invokeMethod(
    //             "placeSearch",
    //             {
    //               // 'search_text': controller.textEditingController.text,
    //               'city_code': '0512',
    //             },
    //           );
    //         },
    //         child: Text(
    //           '导航',
    //         ),
    //       ),
    //     ],
    //   ),
    // );
    return Scaffold(
      // appBar: AppBar(
      //   title: Text('AMapView'),
      // ),
      body: Stack(
        alignment: Alignment.center,
        children: [
          AMapView(),
          Positioned(
            top: 35,
            child: GestureDetector(
              onTap: () {
                Get.toNamed(Routes.PLACE_SEARCH);
              },
              child: Container(
                width: Get.width * .85,
                height: 50,
                padding: EdgeInsets.only(left: 10),
                alignment: Alignment.centerLeft,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(5),
                ),
                child: Text('请输入搜索内容'),
              ),
            ),
          )
        ],
      ),
    );
  }
}
