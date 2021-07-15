import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controllers/home_controller.dart';

class HomeView extends GetView<HomeController> {
  @override
  Widget build(BuildContext context) {
    print(controller.stationList);
    return Scaffold(
      appBar: AppBar(
        title: Text('HomeView'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Expanded(
                  child: TextFormField(
                    onChanged: (String text) {
                      if (text.isEmpty) return;
                      controller.timer?.cancel();
                      controller.timer = new Timer(controller.durationTime, () {
                        //搜索函数
                        controller.platform.invokeMethod(
                          "queryBusStations",
                          {
                            'search_text': controller.textEditingController.text,
                            'city_name': '苏州',
                          },
                        );
                      });
                    },
                    controller: controller.textEditingController,
                    textInputAction: TextInputAction.go,
                    decoration: InputDecoration(
                        isDense: true,
                        border: OutlineInputBorder(),
                        contentPadding: EdgeInsets.symmetric(
                          vertical: 10,
                          horizontal: 5,
                        ),
                        hintText: '请输入公交线路'),
                  ),
                ),
                SizedBox(
                  width: 10,
                ),
                ElevatedButton(
                  onPressed: () {
                    controller.platform.invokeMethod(
                      "queryBusLines",
                      {
                        'search_text': controller.textEditingController.text,
                        'city_code': '0512',
                      },
                    );
                  },
                  child: Text('搜索'),
                ),
              ],
            ),
          ),
          Obx(
            () {
              print(controller.stationList);
              return Expanded(
                child: ListView.builder(
                  itemCount: controller.stationList.length,
                  itemBuilder: (context, index) => ListTile(
                    onTap: () {
                      controller.platform.invokeMethod(
                        "openMapView",
                        {
                          'search_text': controller.textEditingController.text,
                          'city_code': '3205',
                        },
                      );
                    },
                    leading: Icon(Icons.star),
                    title: Text('${controller.stationList[index]}'),
                    // title: Text('细雨带风湿透黄昏的街道'),
                  ),
                ),
              );
            },
          )

          // Expanded(
          //   child: Obx(
          //     () => ListView.builder(
          //       shrinkWrap: true,
          //       itemCount: controller.lineList.length,
          //       itemBuilder: (context, index) => Container(
          //         // height: 45,
          //         padding: EdgeInsets.symmetric(horizontal: 20),
          //         alignment: Alignment.centerLeft,
          //         decoration: BoxDecoration(
          //           border: Border(
          //             bottom: BorderSide(
          //               width: 1,
          //               color: Colors.grey,
          //             ),
          //           ),
          //         ),
          //         child: Column(
          //           children: [
          //             Row(
          //               children: [
          //                 Icon(
          //                   Icons.train,
          //                   size: 20,
          //                   color: Colors.deepPurple,
          //                 ),
          //                 SizedBox(
          //                   width: 10,
          //                 ),
          //                 Text('${controller.lineList[index].ln}'),
          //               ],
          //             ),
          //             Padding(
          //               padding: const EdgeInsets.only(left: 20),
          //               child: Wrap(
          //                 children: controller.lineList[index].st!
          //                     .map((e) => Container(
          //                           padding: EdgeInsets.symmetric(vertical: 15),
          //                           decoration: BoxDecoration(
          //                             border: Border(
          //                               bottom: BorderSide(
          //                                 width: 1,
          //                                 color: Colors.grey,
          //                               ),
          //                             ),
          //                           ),
          //                           child: Row(
          //                             children: [
          //                               Icon(
          //                                 Icons.directions_bus_sharp,
          //                                 size: 20,
          //                                 color: Colors.deepPurple,
          //                               ),
          //                               SizedBox(
          //                                 width: 10,
          //                               ),
          //                               Text('${e.n}'),
          //                             ],
          //                           ),
          //                         ))
          //                     .toList(),
          //               ),
          //             )
          //           ],
          //         ),
          //       ),
          //     ),
          //   ),
          // ),
        ],
      ),
    );
  }
}
