import 'dart:async';

import 'package:flutter/services.dart';
// import 'package:permission/permission.dart';

class FaceRecognition {
  static const MethodChannel _channel = const MethodChannel('face_recognition');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void getVersion() async {
    final String version = await _channel.invokeMethod('getSdkVersion');
    print('version $version');
  }

  static void startDetect(String bizToken) async {
    // List<Permissions> permissions =
    //     await Permission.requestPermissions([PermissionName.Camera]);
    // print('permissions $permissions');
    // if (permissions.isNotEmpty) {
    final String startDetect =
        await _channel.invokeMethod('startDetect', bizToken);
    print('version $startDetect');
    // }
  }
}
