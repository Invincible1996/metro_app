import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../controllers/place_search_controller.dart';

class PlaceSearchView extends GetView<PlaceSearchController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        iconTheme: IconThemeData(color: Colors.black),
        backgroundColor: Colors.white,
        title: TextFormField(
          autofocus: true,
          onChanged: (String value) {
            if (value.isEmpty) return;
            controller.timer?.cancel();
            controller.timer = new Timer(controller.durationTime, () {
              controller.onValueChanged(value);
            });
          },
        ),
        centerTitle: true,
      ),
      body: Obx(
        () {
          print(controller.placeList);
          return ListView.builder(
            itemCount: controller.placeList.length,
            itemBuilder: (context, index) => GestureDetector(
              onTap: () {
                controller.addMarkerOnMap(index);
                Get.back();
              },
              child: Container(
                // height: 45,
                padding: EdgeInsets.only(left: 10, top: 10, bottom: 10),
                alignment: Alignment.centerLeft,
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border(
                    bottom: BorderSide(
                      width: 1,
                      color: Color(0XFFE6E6E6),
                    ),
                  ),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      '${controller.placeList[index]['title']}',
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(
                      height: 5,
                    ),
                    Text('${controller.placeList[index]['snippet']}'),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
