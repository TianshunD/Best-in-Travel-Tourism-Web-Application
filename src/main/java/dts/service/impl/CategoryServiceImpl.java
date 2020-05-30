package dts.service.impl;

import dts.dao.CategoryDao;
import dts.dao.impl.CategoryDaoImpl;
import dts.domain.Category;
import dts.service.CategoryService;
import dts.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao dao = new CategoryDaoImpl();

    /**
     * find all categories
     * @return
     */
    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> categories = jedis.zrangeWithScores("categories", 0, -1);
        List<Category> all = null;

        //add list to redis by sortedset
        if (categories==null || categories.size()==0) {
            System.out.println("by db");
            all = dao.findAll();
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("categories", all.get(i).getCid(), all.get(i).getCname());
            }
        } else {
            System.out.println("by redis");
            all = new ArrayList<Category>();
            for (Tuple category : categories) {
                Category ca = new Category();
                ca.setCname(category.getElement());
                ca.setCid((int)category.getScore());
                all.add(ca);
            }
        }


        return all;
    }

    @Override
    public Category find(int cid) {
        return dao.find(cid);
    }
}
