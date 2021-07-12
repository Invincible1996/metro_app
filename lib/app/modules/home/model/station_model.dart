/// @Date: 2021/7/8 18:07
/// @Author: kevin
/// @Description: 星期四
class StationModel {
  String? s;
  String? i;
  List<Line>? l;
  String? o;

  StationModel({this.s, this.i, this.l, this.o});

  StationModel.fromJson(Map<String, dynamic> json) {
    s = json['s'];
    i = json['i'];
    if (json['l'] != null) {
      l = <Line>[];
      json['l'].forEach((v) {
        l!.add(Line.fromJson(v));
      });
    }
    o = json['o'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['s'] = this.s;
    data['i'] = this.i;
    if (this.l != null) {
      data['l'] = this.l!.map((v) => v.toJson()).toList();
    }
    data['o'] = this.o;
    return data;
  }
}

class Line {
  List<St>? st;
  String? ln;
  String? su;
  String? kn;
  List<String>? c;
  String? lo;
  List<String>? lp;
  String? ls;
  String? cl;
  String? la;
  int? x;
  String? li;

  Line({this.st, this.ln, this.su, this.kn, this.c, this.lo, this.lp, this.ls, this.cl, this.la, this.x, this.li});

  Line.fromJson(Map<String, dynamic> json) {
    if (json['st'] != null) {
      st = <St>[];
      json['st'].forEach((v) {
        st!.add(new St.fromJson(v));
      });
    }
    ln = json['ln'];
    su = json['su'];
    kn = json['kn'];
    c = json['c'].cast<String>();
    lo = json['lo'];
    lp = json['lp'].cast<String>();
    ls = json['ls'];
    cl = json['cl'];
    la = json['la'];
    x = json['x'];
    li = json['li'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    if (this.st != null) {
      data['st'] = this.st!.map((v) => v.toJson()).toList();
    }
    data['ln'] = this.ln;
    data['su'] = this.su;
    data['kn'] = this.kn;
    data['c'] = this.c;
    data['lo'] = this.lo;
    data['lp'] = this.lp;
    data['ls'] = this.ls;
    data['cl'] = this.cl;
    data['la'] = this.la;
    data['x'] = this.x;
    data['li'] = this.li;
    return data;
  }
}

class St {
  String? rs;
  String? udpx;
  String? su;
  String? udsu;
  String? n;
  String? sid;
  String? p;
  String? r;
  String? udsi;
  String? t;
  String? si;
  String? sl;
  String? udli;
  String? poiid;
  String? lg;
  String? sp;

  St({this.rs, this.udpx, this.su, this.udsu, this.n, this.sid, this.p, this.r, this.udsi, this.t, this.si, this.sl, this.udli, this.poiid, this.lg, this.sp});

  St.fromJson(Map<String, dynamic> json) {
    rs = json['rs'];
    udpx = json['udpx'];
    su = json['su'];
    udsu = json['udsu'];
    n = json['n'];
    sid = json['sid'];
    p = json['p'];
    r = json['r'];
    udsi = json['udsi'];
    t = json['t'];
    si = json['si'];
    sl = json['sl'];
    udli = json['udli'];
    poiid = json['poiid'];
    lg = json['lg'];
    sp = json['sp'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['rs'] = this.rs;
    data['udpx'] = this.udpx;
    data['su'] = this.su;
    data['udsu'] = this.udsu;
    data['n'] = this.n;
    data['sid'] = this.sid;
    data['p'] = this.p;
    data['r'] = this.r;
    data['udsi'] = this.udsi;
    data['t'] = this.t;
    data['si'] = this.si;
    data['sl'] = this.sl;
    data['udli'] = this.udli;
    data['poiid'] = this.poiid;
    data['lg'] = this.lg;
    data['sp'] = this.sp;
    return data;
  }
}
