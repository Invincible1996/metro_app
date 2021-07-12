package com.kevin.metro_app.util;

import android.content.Context;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

/**
 *
 */
public class AMapManager {

  /**
   * 查询公交站
   *
   * @param methodChannel
   * @param context       Context
   * @param search        搜索条件
   * @param city          城市
   */
  public static void queryBusStations(MethodChannel methodChannel, Context context, String search, String city) {
    BusStationQuery busStationQuery = new BusStationQuery(search, city);
    BusStationSearch busStationSearch = new BusStationSearch(context, busStationQuery);
    busStationSearch.setOnBusStationSearchListener((result, i) -> {
      // 发送数据到flutter
      System.out.println(result.getBusStations());
      List<String> data = new ArrayList<>();

      for (BusStationItem busStation : result.getBusStations()) {
        if (busStation.getBusLineItems().size() > 0) {
          data.add(busStation.getBusLineItems().get(0).getBusLineName());
        }
      }
      methodChannel.invokeMethod("onBusStationSearched", data);
    });
    busStationSearch.searchBusStationAsyn();
  }

  /**
   * 公交线路查询
   *
   * @param context     Context
   * @param search      搜索条件
   * @param currentPage 当前页数
   */
  public static void queryBusLines(MethodChannel methodChannel, Context context, String search, String cityCode, int currentPage) {
    BusLineQuery busLineQuery = new BusLineQuery(search, BusLineQuery.SearchType.BY_LINE_NAME, cityCode);
    busLineQuery.setPageSize(50);
    busLineQuery.setPageNumber(currentPage);
    BusLineSearch busLineSearch = new BusLineSearch(context, busLineQuery);
    busLineSearch.setOnBusLineSearchListener((busLineResult, i) -> {
      List<Map<String, String>> data = new ArrayList<>();


      for (BusLineItem busLineItem : busLineResult.getBusLines()) {

        HashMap<String, String> dataMap = new HashMap<>();

        dataMap.put("busLineName", busLineItem.getBusLineName());
        dataMap.put("busCompany", busLineItem.getBusCompany());
        dataMap.put("busLineId", busLineItem.getBusLineId());
        dataMap.put("busLineType", busLineItem.getBusLineType());
        dataMap.put("cityCode", busLineItem.getCityCode());
        dataMap.put("basicPrice", String.valueOf(busLineItem.getBasicPrice()));
        dataMap.put("originatingStation", busLineItem.getOriginatingStation());
        dataMap.put("terminalStation", busLineItem.getTerminalStation());

        data.add(dataMap);
      }
      System.out.println(busLineResult.getBusLines());
      methodChannel.invokeMethod("onBusLineSearched", data);
    });
    busLineSearch.searchBusLineAsyn();
  }
}
