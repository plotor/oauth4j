package org.zhenchao.oauth.dao;

import org.apache.ibatis.annotations.Param;
import org.zhenchao.oauth.entity.Scope;
import org.zhenchao.oauth.entity.ScopeExample;

import java.util.List;

public interface ScopeMapper {
    long countByExample(ScopeExample example);

    int deleteByExample(ScopeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Scope record);

    int insertSelective(Scope record);

    List<Scope> selectByExample(ScopeExample example);

    Scope selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Scope record, @Param("example") ScopeExample example);

    int updateByExample(@Param("record") Scope record, @Param("example") ScopeExample example);

    int updateByPrimaryKeySelective(Scope record);

    int updateByPrimaryKey(Scope record);
}