package dts.dao.impl;

import dts.dao.FavoriteDao;
import dts.domain.Favorite;
import dts.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite isFavorite(int rid, int uid) {
        Favorite fav = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            fav = template.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (Exception e) {
            System.out.println(e);
        }
        return fav;
    }

    /**
     * check the count of this route been added as favorite
     * @param rid
     * @return
     */
    @Override
    public int favoriteCount(int rid) {
        int count = 0;
        try {
            String sql = "select count(*) from tab_favorite where rid = ?";
            count = template.queryForObject(sql, Integer.class, rid);
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    /**
     * add as favorite
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(int rid, int uid) {
        String sql = "insert into tab_favorite values (?,?,?)";
        template.update(sql,rid,new Date(),uid);
    }
}
