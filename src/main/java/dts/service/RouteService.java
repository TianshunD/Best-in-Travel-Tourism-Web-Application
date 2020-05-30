package dts.service;

import dts.domain.PageBean;
import dts.domain.Route;

public interface RouteService {
    public PageBean<Route> pageList(int cid, int currentPage, int pageSize, String rname);

    public Route routeDetail(String rid);
}
