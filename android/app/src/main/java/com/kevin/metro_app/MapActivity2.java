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
        // ????????????
        Toast.makeText(MapActivity2.this, "??????????????????", Toast.LENGTH_SHORT).show();
        isServiceRunning = true;
        updateBtnStatus();
      } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
        // ????????????
        Toast.makeText(MapActivity2.this, "??????????????????", Toast.LENGTH_SHORT).show();
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
        // ????????????
        Toast.makeText(MapActivity2.this, "??????????????????", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MapActivity2.this, "????????????????????????", Toast.LENGTH_SHORT).show();
        isGatherRunning = true;
        updateBtnStatus();
      } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
        Toast.makeText(MapActivity2.this, "????????????????????????", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(MapActivity2.this, "????????????????????????", Toast.LENGTH_SHORT).show();
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
    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(20));
    mapView.onCreate(savedInstanceState);

    // ????????????????????????
    mapView.getMap().setMyLocationEnabled(true);
    mapView.getMap().setMyLocationStyle(
      new MyLocationStyle()
        .interval(2000)
        .myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));

    // ???????????????
    startTrackView = findViewById(R.id.activity_track_service_start_track);
    startGatherView = findViewById(R.id.activity_track_service_start_gather);

    startTrackView.setOnClickListener(this);
    startGatherView.setOnClickListener(this);
    updateBtnStatus();
    // ????????????
  }

  private void startTrack() {
    // ?????????Terminal????????????Terminal ID?????????Terminal???????????????????????????????????????Terminal ID??????
    // ???Terminal ID??????????????????
    aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constant.SERVICE_ID, Constant.TERMINAL_NAME), new SimpleOnTrackListener() {
      @Override
      public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
        if (queryTerminalResponse.isSuccess()) {
          if (queryTerminalResponse.isTerminalExist()) {
            // ??????????????????????????????????????????????????????terminal id
            terminalId = queryTerminalResponse.getTid();
            if (uploadToTrack) {
              aMapTrackClient.addTrack(new AddTrackRequest(Constant.SERVICE_ID, terminalId), new SimpleOnTrackListener() {
                @Override
                public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
                  if (addTrackResponse.isSuccess()) {
                    // trackId??????????????????????????????????????????????????????????????????????????????startGather???????????????track id
                    trackId = addTrackResponse.getTrid();
                    TrackParam trackParam = new TrackParam(Constant.SERVICE_ID, terminalId);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      trackParam.setNotification(createNotification());
                    }
                    aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
                  } else {
                    Toast.makeText(MapActivity2.this, "?????????????????????" + addTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                  }
                }
              });
            } else {
              // ?????????track id????????????????????????????????????????????????
              TrackParam trackParam = new TrackParam(Constant.SERVICE_ID, terminalId);
              if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                trackParam.setNotification(createNotification());
              }
              aMapTrackClient.startTrack(trackParam, onTrackLifecycleListener);
            }
          } else {
            // ?????????????????????????????????????????????????????????????????????????????????terminal id
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
                  Toast.makeText(MapActivity2.this, "?????????????????????" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
              }
            });
          }
        } else {
          Toast.makeText(MapActivity2.this, "?????????????????????" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }


  private void updateBtnStatus() {
    startTrackView.setText(isServiceRunning ? "????????????" : "????????????");
    startTrackView.setTextColor(isServiceRunning ? 0xFFFFFFFF : 0xFF000000);
    startTrackView.setBackgroundResource(isServiceRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
    startGatherView.setText(isGatherRunning ? "????????????" : "????????????");
    startGatherView.setTextColor(isGatherRunning ? 0xFFFFFFFF : 0xFF000000);
    startGatherView.setBackgroundResource(isGatherRunning ? R.drawable.round_corner_btn_bg_active : R.drawable.round_corner_btn_bg);
  }

  /**
   * ???8.0?????????????????????app????????????????????????????????????????????????????????????
   * ???????????????????????????????????????????????????????????????Service???????????????????????????????????????Service????????????????????????
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
      .setContentTitle("??????sdk?????????")
      .setContentText("??????sdk?????????");
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
