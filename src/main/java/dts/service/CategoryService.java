package dts.service;

import dts.domain.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();
    public Category find(int cid);
}
