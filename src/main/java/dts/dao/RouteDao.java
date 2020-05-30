package dts.dao;

import dts.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * get record number based on cid
     */
    public int totalCount(int cid, String rname);

    /**
     * get route info based on cid, limit ?, ?(current page, page size)
     */
    public List<Route> findRoutesByPage(int cid, int start, int pageSize, String rname);

    /**
     * find route by rid
     * @param rid
     * @return
     */
    public Route findRoute(int rid);
}
