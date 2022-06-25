package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author feng
 * @create 2022-06-11 16:07
 */
public abstract class BaseServiceImpl<T> {
    protected abstract BaseMapper<T> getEntityMapper();

    public void insert(T t){
        getEntityMapper().insert(t);
    }

    public void delete(Long id){
        getEntityMapper().delete(id);
    }

    public void update(T t){
        getEntityMapper().update(t);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public T getById(Long id){
        return getEntityMapper().getById(id);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<T> findPage(Map<String,Object> filters){
        PageHelper.startPage(CastUtil.castInt(filters.get("pageNum"),1),CastUtil.castInt(filters.get("pageSize"),10));
        return new PageInfo(getEntityMapper().findPage(filters),10);
    }
}
