import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:metro_app/app/routes/app_pages.dart';
import 'package:permission_handler/permission_handler.dart';

class SplashView extends StatefulWidget {
  @override
  _SplashViewState createState() => _SplashViewState();
}

class _SplashViewState extends State<SplashView> {
  @override
  void initState() {
    super.initState();
    initPermission();
  }

  initPermission() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.location,
      Permission.storage,
    ].request();
    print(statuses[Permission.location]);

    1.delay(() {
      Get.offAllNamed(Routes.MAP);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      //   title: Text('SplashView'),
      //   centerTitle: true,
      // ),
      body: Center(
        child: Text(
          'SplashView is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
