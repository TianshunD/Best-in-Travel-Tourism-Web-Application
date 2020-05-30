package dts.dao.impl;

import dts.dao.CategoryDao;
import dts.domain.Category;
import dts.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    String sql;

    /**
     * get all categories
     * @return
     */
    @Override
    public List<Category> findAll() {
        sql = "select * from tab_category order by cid";
        return template.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Category find(int cid) {
        sql = "select * from tab_category where cid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<>(Category.class), cid);
    }
}
