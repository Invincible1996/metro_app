import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

/// @Date: 2021/7/12 10:05
/// @Author: kevin
/// @Description: 星期一
///
///
///

/// @create at 2021/7/12 10:05
/// @desc
showToast(String text) {
  Fluttertoast.showToast(
    msg: '$text',
    toastLength: Toast.LENGTH_SHORT,
    gravity: ToastGravity.CENTER,
    timeInSecForIosWeb: 1,
    backgroundColor: Colors.black,
    textColor: Colors.white,
    fontSize: 16.0,
  );
}
