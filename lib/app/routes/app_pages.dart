import 'package:get/get.dart';

import 'package:metro_app/app/modules/home/bindings/home_binding.dart';
import 'package:metro_app/app/modules/home/views/home_view.dart';
import 'package:metro_app/app/modules/map/bindings/map_binding.dart';
import 'package:metro_app/app/modules/map/views/map_view.dart';
import 'package:metro_app/app/modules/place_search/bindings/place_search_binding.dart';
import 'package:metro_app/app/modules/place_search/views/place_search_view.dart';
import 'package:metro_app/app/modules/splash/bindings/splash_binding.dart';
import 'package:metro_app/app/modules/splash/views/splash_view.dart';

part 'app_routes.dart';

class AppPages {
  AppPages._();

  static const INITIAL = Routes.SPLASH;

  static final routes = [
    GetPage(
      name: _Paths.HOME,
      page: () => HomeView(),
      binding: HomeBinding(),
    ),
    GetPage(
      name: _Paths.SPLASH,
      page: () => SplashView(),
      binding: SplashBinding(),
    ),
    GetPage(
      name: _Paths.MAP,
      page: () => MapView(),
      binding: MapBinding(),
    ),
    GetPage(
      name: _Paths.PLACE_SEARCH,
      page: () => PlaceSearchView(),
      binding: PlaceSearchBinding(),
    ),
  ];
}
