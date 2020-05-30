package dts.service.impl;

import dts.dao.*;
import dts.dao.impl.FavoriteDaoImpl;
import dts.dao.impl.RouteDaoImpl;
import dts.dao.impl.RouteImgDaoImpl;
import dts.dao.impl.SellerDaoImpl;
import dts.domain.PageBean;
import dts.domain.Route;
import dts.service.RouteService;

public class RouteServiceImpl implements RouteService {
    private RouteDao dao = new RouteDaoImpl();
    private RouteImgDao img_dao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageList(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        int totalCount = dao.totalCount(cid,rname);
        //System.out.println("totalCount=" + totalCount);

        pb.setTotalCount(totalCount);
        pb.setPageSize(pageSize);
        pb.setList(dao.findRoutesByPage(cid,(currentPage-1)*pageSize,pageSize,rname));
        pb.setTotalPage(totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1));
        //System.out.println(pb);
        return pb;
    }

    @Override
    public Route routeDetail(String rid) {
        //get basic route info
        int rid_int = Integer.parseInt(rid);
        Route route = dao.findRoute(rid_int);
        route.setRouteImgList(img_dao.findRouteImgs(rid_int));
        route.setSeller(sellerDao.findSeller(route.getSid()));
        route.setCount(favDao.favoriteCount(rid_int));

        return route;
    }
}
