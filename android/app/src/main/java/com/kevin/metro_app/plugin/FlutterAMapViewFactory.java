package com.kevin.metro_app.plugin;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class FlutterAMapViewFactory extends PlatformViewFactory {

  @NonNull
  private final BinaryMessenger messenger;


  public FlutterAMapViewFactory(@NonNull BinaryMessenger messenger) {
    super(StandardMessageCodec.INSTANCE);
    this.messenger = messenger;
  }

  @Override
  public PlatformView create(Context context, int viewId, Object args) {
    return new FlutterAMapView(context, viewId, (Map<String, Object>) args);
  }
}
