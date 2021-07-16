package com.kevin.metro_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.amap.api.track.query.entity.LocationMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.model.AddTerminalRequest;
import com.amap.api.track.query.model.AddTerminalResponse;
import com.amap.api.track.query.model.AddTrackResponse;
import com.amap.api.track.query.model.DistanceRequest;
import com.amap.api.track.query.model.DistanceResponse;
import com.amap.api.track.query.model.HistoryTrackResponse;
import com.amap.api.track.query.model.LatestPointRequest;
import com.amap.api.track.query.model.LatestPointResponse;
import com.amap.api.track.query.model.OnTrackListener;
import com.amap.api.track.query.model.ParamErrorResponse;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackResponse;

public class MapActivity2 extends AppCompatActivity implements View.OnClickListener {


  private TextView tv_location;

  private TextView tv_distance;

  private TextView tv_address;

  private AMapTrackClient aMapTrackClient;

  final OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListener() {
    @Override
    public void onBindServiceCallback(int i, String s) {

    }

    @Override
    public void onStartGatherCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
        status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
        Toast.makeText(MapActivity2.this, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(MapActivity2.this, "定位采集启动异常，" + msg, Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onStartTrackCallback(int status, String msg) {
      if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
        status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
        status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
        // 服务启动成功，继续开启收集上报
        aMapTrackClient.startGather(this);
      } else {
        Toast.makeText(MapActivity2.this, "轨迹上报服务服务启动异常，" + msg, Toast.LENGTH_SHORT).show();
      }
    }

    @Override
    public void onStopGatherCallback(int i, String s) {

    }

    @Override
    public void onStopTrackCallback(int i, String s) {

    }
  };
  private long terminalId;
  private GeocodeSearch geocodeSearch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map2);

    Button bt_distance = findViewById(R.id.bt_distance);
    Button bt_location = findViewById(R.id.bt_location);
    tv_distance = findViewById(R.id.tv_distance);
    tv_location = findViewById(R.id.tv_location);
    tv_address = findViewById(R.id.tv_desc);

    bt_location.setOnClickListener(this);
    bt_distance.setOnClickListener(this);

    initSdk();


    final long serviceId = Constant.SERVICE_ID;  // 这里填入你创建的服务id
    final String terminalName = Constant.TERMINAL_NAME;   // 唯一标识某个用户或某台设备的名称
    aMapTrackClient.queryTerminal(new QueryTerminalRequest(serviceId, terminalName), new OnTrackListener() {
      @Override
      public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
        if (queryTerminalResponse.isSuccess()) {
          if (queryTerminalResponse.getTid() <= 0) {
            // terminal还不存在，先创建
            aMapTrackClient.addTerminal(new AddTerminalRequest(terminalName, serviceId), new OnTrackListener() {
              @Override
              public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

              }

              @Override
              public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
                if (addTerminalResponse.isSuccess()) {
                  // 创建完成，开启猎鹰服务
                  terminalId = addTerminalResponse.getTid();
                  aMapTrackClient.startTrack(new TrackParam(serviceId, terminalId), onTrackLifecycleListener);
                } else {
                  // 请求失败
                  Toast.makeText(MapActivity2.this, "请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
              }

              @Override
              public void onDistanceCallback(DistanceResponse distanceResponse) {

              }

              @Override
              public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

              }

              @Override
              public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

              }

              @Override
              public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

              }

              @Override
              public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

              }

              @Override
              public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

              }
            });
          } else {
            // terminal已经存在，直接开启猎鹰服务
            terminalId = queryTerminalResponse.getTid();
            aMapTrackClient.startTrack(new TrackParam(serviceId, terminalId), onTrackLifecycleListener);
          }
        } else {
          // 请求失败
          Toast.makeText(MapActivity2.this, "请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

      }

      @Override
      public void onDistanceCallback(DistanceResponse distanceResponse) {

      }

      @Override
      public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

      }

      @Override
      public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

      }

      @Override
      public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

      }

      @Override
      public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

      }

      @Override
      public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

      }
    });

    geocodeSearch = new GeocodeSearch(MapActivity2.this);

    geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
      @Override
      public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
//        simpleAddress = formatAddress.substring(9);
        tv_address.setText("查询经纬度对应详细地址：\n" + formatAddress);
      }

      @Override
      public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

      }
    });
  }

  private void initSdk() {
    aMapTrackClient = new AMapTrackClient(getApplicationContext());
    aMapTrackClient.setInterval(5, 30);
    aMapTrackClient.setCacheSize(20);
    aMapTrackClient.setLocationMode(LocationMode.DEVICE_SENSORS);
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bt_location:
        Toast.makeText(this, "获取当前位置", Toast.LENGTH_SHORT).show();
        aMapTrackClient.queryLatestPoint(new LatestPointRequest(Constant.SERVICE_ID, terminalId), new OnTrackListener() {
          @Override
          public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

          }

          @Override
          public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

          }

          @Override
          public void onDistanceCallback(DistanceResponse distanceResponse) {

          }

          @SuppressLint("SetTextI18n")
          @Override
          public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
            if (latestPointResponse.isSuccess()) {
              Point point = latestPointResponse.getLatestPoint().getPoint();
              // 查询实时位置成功，point为实时位置信息
              Toast.makeText(MapActivity2.this, "获取当前位置成功", Toast.LENGTH_SHORT).show();
              System.out.println(point.getLat());
              System.out.println(point.getLng());
              tv_location.setText("当前经纬度为:" + point.getLat() + point.getLng());

              LatLonPoint latLonPoint = new LatLonPoint(point.getLat(), point.getLng());
              RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
              geocodeSearch.getFromLocationAsyn(query);
            } else {
              // 查询实时位置失败
            }
          }

          @Override
          public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

          }

          @Override
          public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

          }

          @Override
          public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

          }

          @Override
          public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

          }
        });
        break;
      case R.id.bt_distance:
        Toast.makeText(this, "获取当前位行驶距离", Toast.LENGTH_SHORT).show();
        long curr = System.currentTimeMillis();
        DistanceRequest distanceRequest = new DistanceRequest(
          Constant.SERVICE_ID,
          terminalId,
          curr - 12 * 60 * 60 * 1000, // 开始时间
          curr,   // 结束时间
          -1  // 轨迹id，传-1表示包含散点在内的所有轨迹点
        );
        aMapTrackClient.queryDistance(distanceRequest, new OnTrackListener() {
          @Override
          public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {

          }

          @Override
          public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {

          }

          @Override
          public void onDistanceCallback(DistanceResponse distanceResponse) {
            if (distanceResponse.isSuccess()) {
              double meters = distanceResponse.getDistance();
              // 行驶里程查询成功，行驶了meters米
              Toast.makeText(MapActivity2.this, "获取当前位行驶距离成功" + meters, Toast.LENGTH_SHORT).show();
              System.out.println(meters);
              tv_distance.setText("当前行驶距离为:" + meters);
            } else {
              // 行驶里程查询失败
            }
          }

          @Override
          public void onLatestPointCallback(LatestPointResponse latestPointResponse) {

          }

          @Override
          public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {

          }

          @Override
          public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {

          }

          @Override
          public void onAddTrackCallback(AddTrackResponse addTrackResponse) {

          }

          @Override
          public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {

          }
        });
        break;
      default:
        break;
    }
  }
}
