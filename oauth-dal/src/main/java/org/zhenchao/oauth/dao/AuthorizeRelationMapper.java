package org.zhenchao.oauth.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.entity.AuthorizeRelationExample;

public interface AuthorizeRelationMapper {
    long countByExample(AuthorizeRelationExample example);

    int deleteByExample(AuthorizeRelationExample example);

    int insert(AuthorizeRelation record);

    int insertSelective(AuthorizeRelation record);

    List<AuthorizeRelation> selectByExample(AuthorizeRelationExample example);

    int updateByExampleSelective(@Param("record") AuthorizeRelation record, @Param("example") AuthorizeRelationExample example);

    int updateByExample(@Param("record") AuthorizeRelation record, @Param("example") AuthorizeRelationExample example);
}