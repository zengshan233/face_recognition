package com.example.face_recognition;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.megvii.meglive_sdk.listener.DetectCallback;
import com.megvii.meglive_sdk.listener.PreCallback;
import com.megvii.meglive_sdk.manager.MegLiveManager;

// import static androidx.core.content.ContextCompat.startActivity;

/** FaceRecognitionPlugin */
public class FaceRecognitionPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private MegLiveManager megLiveManager;
  private Result pluginResult;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "face_recognition");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
    megLiveManager = MegLiveManager.getInstance();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    pluginResult = result;
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("getSdkVersion")) {
      String version = megLiveManager.getVersion();
      result.success(version);
    } else if (call.method.equals("startDetect")) {
      String bizToken = call.arguments.toString();
      Log.e("beginDetect ", "bizToken ::   " + bizToken);
      megLiveManager.preDetect(context, bizToken, "zh", "https://api.megvii.com", new preCallback());
    } else if (call.method.equals("test")) {
      Intent i = new Intent();
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      i.setData(Uri.parse("tel:10086"));
      i.setAction(Intent.ACTION_DIAL);
      // startActivity(context, i, new Bundle());
      result.success("success");
    } else {
      result.notImplemented();
    }
  }

  private class detectCallback implements DetectCallback {
    @Override
    public void onDetectFinish(String token, int errorCode, String errorMessage, String data) {
      Log.e("onDetectFinish code ", String.valueOf(errorCode));
      Log.e("onDetectFinish err ", errorMessage);

    }
  }

  private class preCallback implements PreCallback {
    @Override
    public void onPreFinish(String token, int errorCode, String errorMessage) {
      if (errorCode == 1000) {
        megLiveManager.setVerticalDetectionType(MegLiveManager.DETECT_VERITICAL_FRONT);
        megLiveManager.startDetect(new detectCallback());
      } else {
        pluginResult.error(String.valueOf(errorCode), errorMessage, null);
      }
    }

    @Override
    public void onPreStart() {

    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
