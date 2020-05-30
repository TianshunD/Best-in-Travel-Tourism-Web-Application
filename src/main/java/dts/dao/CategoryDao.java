package dts.dao;

import dts.domain.Category;

import java.util.List;

public interface CategoryDao {
    public List<Category> findAll();
    public Category find(int cid);
}
