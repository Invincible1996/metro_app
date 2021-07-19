/// @Date: 2021/7/19 17:58
/// @Author: kevin
/// @Description: 星期一
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AMapView extends StatefulWidget {
  @override
  _AMapViewState createState() => _AMapViewState();
}

class _AMapViewState extends State<AMapView> {
  @override
  initState() {
    super.initState();
  }

  Widget build(BuildContext context) {
    // This is used in the platform side to register the view.
    final String viewType = 'com.kevin.aMap.plugin';
    // Pass parameters to the platform side.
    final Map<String, dynamic> creationParams = <String, dynamic>{};
    return AndroidView(
      viewType: viewType,
      creationParams: {
        "myContent": "我是flutter通过参数传入的文本内容",
      },
      creationParamsCodec: const StandardMessageCodec(),
      onPlatformViewCreated: (int id) {}, //初始化
    );
    // return PlatformViewLink(
    //   viewType: viewType,
    //   surfaceFactory: (BuildContext context, PlatformViewController controller) {
    //     return AndroidViewSurface(
    //       controller: controller as AndroidViewController,
    //       gestureRecognizers: const <Factory<OneSequenceGestureRecognizer>>{},
    //       hitTestBehavior: PlatformViewHitTestBehavior.opaque,
    //     );
    //   },
    //   onCreatePlatformView: (PlatformViewCreationParams params) {
    //     return PlatformViewsService.initSurfaceAndroidView(
    //       id: params.id,
    //       viewType: viewType,
    //       layoutDirection: TextDirection.ltr,
    //       creationParams: creationParams,
    //       creationParamsCodec: StandardMessageCodec(),
    //     )
    //       ..addOnPlatformViewCreatedListener(params.onPlatformViewCreated)
    //       ..create();
    //   },
    // );
  }

  @override
  void dispose() {
    super.dispose();
  }
}
