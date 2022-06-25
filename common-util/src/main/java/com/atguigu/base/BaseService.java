package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author feng
 * @create 2022-06-11 16:06
 */
public interface BaseService<T> {
    void insert(T t);

    void delete(Long id);

    void update(T t);

    T getById(Long id);

    PageInfo<T> findPage(Map<String, Object> filters);
}
