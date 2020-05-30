package dts.dao;

import dts.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    public List<RouteImg> findRouteImgs(int rid);
}
