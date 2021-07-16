package com.kevin.metro_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackRequest;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.kevin.metro_app.util.Constant;
import com.kevin.metro_app.util.SimpleOnTrackLifecycleListener;
import com.kevin.metro_app.util.SimpleOnTrackListener;

public class MapActivity2 extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "TrackServiceActivity";
  private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";

  private AMapTrackClient aMapTrackClient;
  private TextureMapView mapView;
  private TextView startTrackView;
  private TextView startGatherView;

  private long terminalId;
  private long trackId;

  private boolean isServiceRunning;
  private boolean isGatherRunning;

  private boolean uploadToTrack = false;

  private final OnTrackLifecycleListener onTrackLifecycleListener = new SimpleOnTrackLifecycleListener() {
    @Override
    public void onBindServiceCallback(int status, String msg) {
      Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
    }

    @Override
    public void onStartTrackCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
        // 成功启动
        Toast.makeText(MapActivity2.this, "启动服务成功", Toast.LENGTH_SHORT).show();
        isServiceRunning = true;
        updateBtnStatus();
      } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
        // 已经启动
        Toast.makeText(MapActivity2.this, "服务已经启动", Toast.LENGTH_SHORT).show();
        isServiceRunning = true;
        updateBtnStatus();
      } else {
        Log.w(TAG, "error onStartTrackCallback, status: " + status + ", msg: " + msg);
        Toast.makeText(MapActivity2.this,
          "error onStartTrackCallback, status: " + status + ", msg: " + msg,
          Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public void onStopTrackCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
        // 成功停止
        Toast.makeText(MapActivity2.this, "停止服务成功", Toast.LENGTH_SHORT).show();
        isServiceRunning = false;
        isGatherRunning = false;
        updateBtnStatus();
      } else {
        Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
        Toast.makeText(MapActivity2.this,
          "error onStopTrackCallback, status: " + status + ", msg: " + msg,
          Toast.LENGTH_LONG).show();

      }
    }

    @Override
    public void onStartGatherCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
        Toast.makeText(MapActivity2.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
        isGatherRunning = true;
        updateBtnStatus();
      } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
        Toast.makeText(MapActivity2.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
        isGatherRunning = true;
        updateBtnStatus();
      } else {
        Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
        Toast.makeText(MapActivity2.this,
          "error onStartGatherCallback, status: " + status + ", msg: " + msg,
          Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public void onStopGatherCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
        Toast.makeText(MapActivity2.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
        isGatherRunning = false;
        updateBtnStatus();
      } else {
        Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
        Toast.makeText(MapActivity2.this,
          "error onStopGatherCallback, status: " + status + ", msg: " + msg,
          Toast.LENGTH_LONG).show();
      }
    }

  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map2);

    aMapTrackClient = new AMapTrackClient(getApplicationContext());
    aMapTrackClient.setInterval(5, 30);

    mapView = findViewById(R.id.activity_track_service_map);
    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(14));
    mapView.onCreate(savedInstanceState);

    // 启动地图内置定位
    mapView.getMap().setMyLocationEnabled(true);
    mapView.getMap().setMyLocationStyle(
      new MyLocationStyle()
        .interval(2000)
        .myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));

    // 初始化控件
    startTrackView = findViewById(R.id.activity_track_service_start_track);
    startGatherView = findViewById(R.id.activity_track_service_start_gather);

    startTrackView.setOnClickListener(this);
    startGatherView.setOnClickListener(this);
    updateBtnStatus();
    // 服务启停
  }

  private void startTrack() {
    // 先根据Terminal名称查询Terminal ID，如果Terminal还不存在，就尝试创建，拿到Terminal ID后，
    // 用Terminal ID开启轨迹服务
    aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constant.SERVICE_ID, Constant.TERMINAL_NAME), new SimpleOnTrackListener() {
      @Override
      public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
        if (queryTerminalResponse.isSuccess()) {
          if (queryTerminalResponse.isTerminalExist()) {
            // 当前终端已经创建过，直接使用查询到的terminal id
            terminalId = queryTerminalResponse.getTid();
            if (uploadToTrack) {
              aMapTrackClient.addTrack(new AddTrackRequest(Constant.SERVICE_ID, terminalId), new SimpleOnTrackListener() {
                @Override
                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                  if (addTrackResponse.isSuccess()) {
                    // trackId需要在启动服务后设置才能生效，因此这里不设置，而是在startGather之前设置了track id
                    trackId = addTrackResponse.getTrid();
                    TrackParam trackParam = new TrackParam(Constant.SERVICE_ID, terminalId);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      trackParam.setNotification(createNotification());
                    }
                    aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                  } else {
                    Toast.makeText(MapActivity2.this, "网络请求失败，" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                  }
                }
              });
            } else {
              // 不指定track id，上报的轨迹点是该终端的散点轨迹
              TrackParam trackParam = new TrackParam(Constant.SERVICE_ID, terminalId);
              if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                trackParam.setNotification(createNotification());
              }
              aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
            }
          } else {
            // 当前终端是新终端，还未创建过，创建该终端并使用新生成的terminal id
            aMapTrackClient.addTerminal(new AddTerminalRequest(Constant.TERMINAL_NAME, Constant.SERVICE_ID), new SimpleOnTrackListener() {
              @Override
              public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                if (addTerminalResponse.isSuccess()) {
                  terminalId = addTerminalResponse.getTid();
                  TrackParam trackParam = new TrackParam(Constant.SERVICE_ID, terminalId);
                  if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    trackParam.setNotification(createNotification());
                  }
                  aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                } else {
                  Toast.makeText(MapActivity2.this, "网络请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
              }
            });
          }
        } else {
          Toast.makeText(MapActivity2.this, "网络请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }


  private void updateBtnStatus() {
    startTrackView.setText(isServiceRunning ? "停止服务" : "启动服务");
    startTrackView.setTextColor(isServiceRunning ? 0xFFFFFFFF : 0xFF000000);
    startTrackView.setBackgroundResource(isServiceRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
    startGatherView.setText(isGatherRunning ? "停止采集" : "开始采集");
    startGatherView.setTextColor(isGatherRunning ? 0xFFFFFFFF : 0xFF000000);
    startGatherView.setBackgroundResource(isGatherRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
  }

  /**
   * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
   * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
   */
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  private Notification createNotification() {
    Notification.Builder builder;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
      nm.createNotificationChannel(channel);
      builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
    } else {
      builder = new Notification.Builder(getApplicationContext());
    }
    Intent nfIntent = new Intent(MapActivity2.this, MapActivity2.class);
    nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    builder.setContentIntent(PendingIntent.getActivity(MapActivity2.this, 0, nfIntent, 0))
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle("猎鹰sdk运行中")
      .setContentText("猎鹰sdk运行中");
    Notification notification = builder.build();
    return notification;
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.activity_track_service_start_track:
        if (isServiceRunning) {
          aMapTrackClient.stopTrack(new TrackParam(Constant.SERVICE_ID, terminalId), onTrackLifecycleListener);
        } else {
          startTrack();
        }
        break;
      case R.id.activity_track_service_start_gather:
        if (isGatherRunning) {
          aMapTrackClient.stopGather(onTrackLifecycleListener);
        } else {
          aMapTrackClient.setTrackId(trackId);
          aMapTrackClient.startGather(onTrackLifecycleListener);
        }
        break;
      default:
        break;
    }
  }
}
