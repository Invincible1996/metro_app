package com.kevin.metro_app.util;

/**
 *
 */
public class Constant {

  /**
   * 服务id，请修改成您创建的服务的id
   * <p>
   * 猎鹰轨迹服务，同一个开发者账号下的key可以直接使用同账号下的sid，不再需要人工绑定
   */
  public static final long SERVICE_ID = 370138;


  /**
   * 终端名称，该名称可以根据使用方业务而定，比如可以是用户名、用户手机号等唯一标识
   * <p>
   * 通常应该使用该名称查询对应终端id，确定该终端是否存在，如果不存在则创建，然后就可以开启轨迹上报，将上报的轨迹关联
   * 到该终端
   */
  public static final String TERMINAL_NAME = "test_metro_app";

  /**
   * 查询公交线路
   */
  public static final String QUERY_BUS_LINES = "queryBusLines";

  /**
   * 查询公交站
   */
  public static final String QUERY_BUS_STATIONS = "queryBusStations";

  /**
   * 打开MapView
   */
  public static final String OPEN_MAP_VIEW = "openMapView";

  /**
   *
   */
  public static final String SEARCH_LOCATION = "searchLocation";

  /**
   *
   */
  public static final String TRACK_SEARCH_ACTIVITY = "trackSearchActivity";
}
