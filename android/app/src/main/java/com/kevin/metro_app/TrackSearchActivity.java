package com.kevin.metro_app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.query.entity.DriveMode;
import com.amap.api.track.query.entity.Point;
import com.amap.api.track.query.entity.Track;
import com.amap.api.track.query.entity.TrackPoint;
import com.amap.api.track.query.model.QueryTerminalRequest;
import com.amap.api.track.query.model.QueryTerminalResponse;
import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.track.query.model.QueryTrackResponse;
import com.kevin.metro_app.util.Constant;
import com.kevin.metro_app.util.SimpleOnTrackListener;

import java.util.LinkedList;
import java.util.List;

public class TrackSearchActivity extends AppCompatActivity {

  private AMapTrackClient aMapTrackClient;
  private CheckBox bindRoadCheckBox;
  private CheckBox recoupCheckBox;
  private TextureMapView textureMapView;

  private List<Polyline> polylines = new LinkedList<>();
  private List<Marker> endMarkers = new LinkedList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_track_search);

    aMapTrackClient = new AMapTrackClient(getApplicationContext());

    bindRoadCheckBox = findViewById(R.id.activity_track_search_bindroad);
    recoupCheckBox = findViewById(R.id.activity_track_search_recoup);

    textureMapView = findViewById(R.id.activity_track_search_map);
    textureMapView.onCreate(savedInstanceState);

    TextView tv_search_track = findViewById(R.id.activity_track_search_search_track);

    tv_search_track.setOnClickListener(v -> {
      clearTracksOnMap();
      // 先查询terminal id，然后用terminal id查询轨迹
      // 查询符合条件的所有轨迹，并分别绘制
      aMapTrackClient.queryTerminal(new QueryTerminalRequest(Constant.SERVICE_ID, Constant.TERMINAL_NAME), new SimpleOnTrackListener() {
        @Override
        public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
          if (queryTerminalResponse.isSuccess()) {
            if (queryTerminalResponse.isTerminalExist()) {
              long tid = queryTerminalResponse.getTid();
              // 搜索最近12小时以内上报的属于某个轨迹的轨迹点信息，散点上报不会包含在该查询结果中
              QueryTrackRequest queryTrackRequest = new QueryTrackRequest(
                Constant.SERVICE_ID,
                tid,
                -1,
                System.currentTimeMillis() - 12 * 60 * 60 * 1000,
                System.currentTimeMillis(),
                0,
                bindRoadCheckBox.isChecked() ? 1 : 0,
                0,
                DriveMode.DRIVING,
                recoupCheckBox.isChecked() ? 1 : 0,
                5000,
                1,
                1,
                100
              );

              aMapTrackClient.queryTerminalTrack(queryTrackRequest, new SimpleOnTrackListener() {
                @Override
                public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
                  if (queryTrackResponse.isSuccess()) {
                    List<Track> tracks = queryTrackResponse.getTracks();
                    if (tracks != null && !tracks.isEmpty()) {
                      boolean allEmpty = true;
                      for (Track track : tracks) {
                        List<Point> points = track.getPoints();
                        if (points != null && points.size() > 0) {
                          allEmpty = false;
                          drawTrackOnMap(points, track.getStartPoint(), track.getEndPoint());
                        }
                      }
                      if (allEmpty) {
                        Toast.makeText(TrackSearchActivity.this,
                          "所有轨迹都无轨迹点，请尝试放宽过滤限制，如：关闭绑路模式", Toast.LENGTH_SHORT).show();
                      } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("共查询到").append(tracks.size()).append("条轨迹，每条轨迹行驶距离分别为：");
                        for (Track track : tracks) {
                          sb.append(track.getDistance()).append("m,");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        Toast.makeText(TrackSearchActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                      }
                    }else {
                      Toast.makeText(TrackSearchActivity.this, "未获取到轨迹", Toast.LENGTH_SHORT).show();
                    }
                  }else {
                    Toast.makeText(TrackSearchActivity.this, "查询历史轨迹失败，" + queryTrackResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
                  }
                }
              });
            }
          } else {
            Toast.makeText(TrackSearchActivity.this, "Terminal不存在", Toast.LENGTH_SHORT).show();
          }
        }
      });
    });
  }

  /**
   * 绘制轨迹
   *
   * @param points
   * @param startPoint 起点
   * @param endPoint   终点
   */
  private void drawTrackOnMap(List<Point> points, TrackPoint startPoint, TrackPoint endPoint) {
    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
    PolylineOptions polylineOptions = new PolylineOptions();
    polylineOptions.color(Color.BLUE).width(20);

    if (startPoint != null && startPoint.getLocation() != null) {
      LatLng latLng = new LatLng(startPoint.getLocation().getLat(), startPoint.getLocation().getLng());
      MarkerOptions markerOptions = new MarkerOptions()
        .position(latLng)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
      endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
    }

    if (endPoint != null && endPoint.getLocation() != null) {
      LatLng latLng = new LatLng(endPoint.getLocation().getLat(), endPoint.getLocation().getLng());
      MarkerOptions markerOptions = new MarkerOptions()
        .position(latLng)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
      endMarkers.add(textureMapView.getMap().addMarker(markerOptions));
    }

    for (Point p : points) {
      LatLng latLng = new LatLng(p.getLat(), p.getLng());
      polylineOptions.add(latLng);
      boundsBuilder.include(latLng);
    }
    Polyline polyline = textureMapView.getMap().addPolyline(polylineOptions);
    polylines.add(polyline);
    textureMapView.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 30));
  }

  private void clearTracksOnMap() {
    for (Polyline polyline : polylines) {
      polyline.remove();
    }
    for (Marker marker : endMarkers) {
      marker.remove();
    }
    endMarkers.clear();
    polylines.clear();
  }

  private void showNetErrorHint(String errorMsg) {
    Toast.makeText(this, "网络请求失败，错误原因: " + errorMsg, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onPause() {
    super.onPause();
    textureMapView.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    textureMapView.onResume();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    textureMapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    textureMapView.onDestroy();
  }

}
