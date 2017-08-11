package org.zhenchao.oauth.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthorizeRelationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AuthorizeRelationExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andAppIdIsNull() {
            addCriterion("app_id is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("app_id is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(Long value) {
            addCriterion("app_id =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(Long value) {
            addCriterion("app_id <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(Long value) {
            addCriterion("app_id >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(Long value) {
            addCriterion("app_id >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(Long value) {
            addCriterion("app_id <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(Long value) {
            addCriterion("app_id <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<Long> values) {
            addCriterion("app_id in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<Long> values) {
            addCriterion("app_id not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(Long value1, Long value2) {
            addCriterion("app_id between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(Long value1, Long value2) {
            addCriterion("app_id not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andScopeIsNull() {
            addCriterion("scope is null");
            return (Criteria) this;
        }

        public Criteria andScopeIsNotNull() {
            addCriterion("scope is not null");
            return (Criteria) this;
        }

        public Criteria andScopeEqualTo(String value) {
            addCriterion("scope =", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotEqualTo(String value) {
            addCriterion("scope <>", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeGreaterThan(String value) {
            addCriterion("scope >", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeGreaterThanOrEqualTo(String value) {
            addCriterion("scope >=", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLessThan(String value) {
            addCriterion("scope <", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLessThanOrEqualTo(String value) {
            addCriterion("scope <=", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLike(String value) {
            addCriterion("scope like", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotLike(String value) {
            addCriterion("scope not like", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeIn(List<String> values) {
            addCriterion("scope in", values, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotIn(List<String> values) {
            addCriterion("scope not in", values, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeBetween(String value1, String value2) {
            addCriterion("scope between", value1, value2, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotBetween(String value1, String value2) {
            addCriterion("scope not between", value1, value2, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeSignIsNull() {
            addCriterion("scope_sign is null");
            return (Criteria) this;
        }

        public Criteria andScopeSignIsNotNull() {
            addCriterion("scope_sign is not null");
            return (Criteria) this;
        }

        public Criteria andScopeSignEqualTo(String value) {
            addCriterion("scope_sign =", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignNotEqualTo(String value) {
            addCriterion("scope_sign <>", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignGreaterThan(String value) {
            addCriterion("scope_sign >", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignGreaterThanOrEqualTo(String value) {
            addCriterion("scope_sign >=", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignLessThan(String value) {
            addCriterion("scope_sign <", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignLessThanOrEqualTo(String value) {
            addCriterion("scope_sign <=", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignLike(String value) {
            addCriterion("scope_sign like", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignNotLike(String value) {
            addCriterion("scope_sign not like", value, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignIn(List<String> values) {
            addCriterion("scope_sign in", values, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignNotIn(List<String> values) {
            addCriterion("scope_sign not in", values, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignBetween(String value1, String value2) {
            addCriterion("scope_sign between", value1, value2, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andScopeSignNotBetween(String value1, String value2) {
            addCriterion("scope_sign not between", value1, value2, "scopeSign");
            return (Criteria) this;
        }

        public Criteria andTokenKeyIsNull() {
            addCriterion("token_key is null");
            return (Criteria) this;
        }

        public Criteria andTokenKeyIsNotNull() {
            addCriterion("token_key is not null");
            return (Criteria) this;
        }

        public Criteria andTokenKeyEqualTo(String value) {
            addCriterion("token_key =", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyNotEqualTo(String value) {
            addCriterion("token_key <>", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyGreaterThan(String value) {
            addCriterion("token_key >", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyGreaterThanOrEqualTo(String value) {
            addCriterion("token_key >=", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyLessThan(String value) {
            addCriterion("token_key <", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyLessThanOrEqualTo(String value) {
            addCriterion("token_key <=", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyLike(String value) {
            addCriterion("token_key like", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyNotLike(String value) {
            addCriterion("token_key not like", value, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyIn(List<String> values) {
            addCriterion("token_key in", values, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyNotIn(List<String> values) {
            addCriterion("token_key not in", values, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyBetween(String value1, String value2) {
            addCriterion("token_key between", value1, value2, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andTokenKeyNotBetween(String value1, String value2) {
            addCriterion("token_key not between", value1, value2, "tokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyIsNull() {
            addCriterion("refresh_token_key is null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyIsNotNull() {
            addCriterion("refresh_token_key is not null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyEqualTo(String value) {
            addCriterion("refresh_token_key =", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyNotEqualTo(String value) {
            addCriterion("refresh_token_key <>", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyGreaterThan(String value) {
            addCriterion("refresh_token_key >", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyGreaterThanOrEqualTo(String value) {
            addCriterion("refresh_token_key >=", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyLessThan(String value) {
            addCriterion("refresh_token_key <", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyLessThanOrEqualTo(String value) {
            addCriterion("refresh_token_key <=", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyLike(String value) {
            addCriterion("refresh_token_key like", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyNotLike(String value) {
            addCriterion("refresh_token_key not like", value, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyIn(List<String> values) {
            addCriterion("refresh_token_key in", values, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyNotIn(List<String> values) {
            addCriterion("refresh_token_key not in", values, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyBetween(String value1, String value2) {
            addCriterion("refresh_token_key between", value1, value2, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenKeyNotBetween(String value1, String value2) {
            addCriterion("refresh_token_key not between", value1, value2, "refreshTokenKey");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeIsNull() {
            addCriterion("refresh_token_expiration_time is null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeIsNotNull() {
            addCriterion("refresh_token_expiration_time is not null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeEqualTo(Long value) {
            addCriterion("refresh_token_expiration_time =", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeNotEqualTo(Long value) {
            addCriterion("refresh_token_expiration_time <>", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeGreaterThan(Long value) {
            addCriterion("refresh_token_expiration_time >", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("refresh_token_expiration_time >=", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeLessThan(Long value) {
            addCriterion("refresh_token_expiration_time <", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeLessThanOrEqualTo(Long value) {
            addCriterion("refresh_token_expiration_time <=", value, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeIn(List<Long> values) {
            addCriterion("refresh_token_expiration_time in", values, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeNotIn(List<Long> values) {
            addCriterion("refresh_token_expiration_time not in", values, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeBetween(Long value1, Long value2) {
            addCriterion("refresh_token_expiration_time between", value1, value2, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenExpirationTimeNotBetween(Long value1, Long value2) {
            addCriterion("refresh_token_expiration_time not between", value1, value2, "refreshTokenExpirationTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}