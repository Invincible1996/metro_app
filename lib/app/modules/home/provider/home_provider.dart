import 'package:dio/dio.dart';
import 'package:get/get.dart';

/// @Date: 2021/7/8 17:41
/// @Author: kevin
/// @Description: 星期四

class HomeProvider extends GetConnect {
  /// @create at 2021/7/8 17:42
  /// @desc
  getStationList() async {
    Dio dio = Dio();
    var response = await dio.get('https://map.amap.com/service/subway?_1615467204533&srhdata=3100_drw_shanghai.json');
    print(response);
  }
}
