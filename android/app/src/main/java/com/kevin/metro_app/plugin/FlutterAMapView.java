package com.kevin.metro_app.plugin;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.TextureMapView;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;


public class FlutterAMapView implements PlatformView, MethodChannel.MethodCallHandler {

  private TextureMapView mapView;
  private MethodChannel methodChannel;

  private TextView textView;

  public FlutterAMapView(@NotNull Context context, int id, @Nullable Map<String, Object> creationParams) {
//    mapView = new TextureMapView(context);
    textView = new TextView(context);
  }

  @Override
  public void onMethodCall(@NonNull @NotNull MethodCall call, @NonNull @NotNull MethodChannel.Result result) {

  }

  @Override
  public View getView() {
    return textView;
  }

  @Override
  public void dispose() {

  }
}
