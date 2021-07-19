package com.kevin.metro_app.plugin;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.flutter.Log;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry;
import io.flutter.plugin.common.PluginRegistry;

public class AMapPlugin implements FlutterPlugin, ActivityAware {

  public static final String A_MAP_VIEW_TYPE = "com.kevin.aMap.plugin";

  @Override
  public void onAttachedToEngine(@NonNull @NotNull FlutterPluginBinding binding) {

    Log.d(A_MAP_VIEW_TYPE, "onAttachedToEngine");

    binding.getPlatformViewRegistry().registerViewFactory(A_MAP_VIEW_TYPE, new FlutterAMapViewFactory(binding.getBinaryMessenger()));
  }

  public static void registerWith(FlutterEngine flutterEngine) {
    final String key = AMapPlugin.class.getCanonicalName();
    ShimPluginRegistry shimPluginRegistry = new ShimPluginRegistry(flutterEngine);

    if (shimPluginRegistry.hasPlugin(key)) {
      return;
    }

    PluginRegistry.Registrar registrar = shimPluginRegistry.registrarFor(key);
    registrar.platformViewRegistry().registerViewFactory(A_MAP_VIEW_TYPE, new FlutterAMapViewFactory(registrar.messenger()));
  }


  @Override
  public void onDetachedFromEngine(@NonNull @NotNull FlutterPluginBinding binding) {

  }

  @Override
  public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
