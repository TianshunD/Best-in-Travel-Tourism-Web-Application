package dts.dao.impl;

import dts.dao.RouteDao;
import dts.domain.Route;
import dts.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private String sql;

    @Override
    public int totalCount(int cid, String rname) {
        List list = new ArrayList();
        StringBuilder sb = new StringBuilder("select count(*) from tab_route where 1=1 ");

        if (cid!=0) {
            sb.append(" and cid = ? ");
            list.add(cid);
        }
        if (rname!=null && rname.length()!=0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }

        sql = sb.toString();
        return template.queryForObject(sql,Integer.class,list.toArray());
    }

    @Override
    public List<Route> findRoutesByPage(int cid, int start, int pageSize, String rname) {
        List list = new ArrayList();
        StringBuilder sb = new StringBuilder("select * from tab_route where 1=1 ");

        if (cid!=0) {
            sb.append(" and cid = ? ");
            list.add(cid);
        }
        if (rname!=null && rname.length()!=0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        sb.append(" limit ?,? ");
        list.add(start);
        list.add(pageSize);

        sql = sb.toString();
        return template.query(sql,new BeanPropertyRowMapper<>(Route.class),list.toArray());
    }

    @Override
    public Route findRoute(int rid) {
        sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
    }
}
