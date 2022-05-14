/*
 * ProjectExample.java
 * Copyright(C) 20xx-2015 xxxxxx公司
 * All rights reserved.
 * -----------------------------------------------
 * 2018-08-10 Created
 */
package com.hori.grms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectExample {

    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;

    public ProjectExample() {
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

    /**
     * 项目
     * 
     * @author 
     * @version 1.0 2018-08-10
     */
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
        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }
        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }
        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }
        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }
        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }
        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }
        public Criteria andProductCodeIsNull() {
            addCriterion("product_code is null");
            return (Criteria) this;
        }
        public Criteria andProductCodeIsNotNull() {
            addCriterion("product_code is not null");
            return (Criteria) this;
        }
        public Criteria andProductCodeEqualTo(String value) {
            addCriterion("product_code =", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeNotEqualTo(String value) {
            addCriterion("product_code <>", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeGreaterThan(String value) {
            addCriterion("product_code >", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("product_code >=", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeLessThan(String value) {
            addCriterion("product_code <", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeLessThanOrEqualTo(String value) {
            addCriterion("product_code <=", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeLike(String value) {
            addCriterion("product_code like", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeNotLike(String value) {
            addCriterion("product_code not like", value, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeIn(List<String> values) {
            addCriterion("product_code in", values, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeNotIn(List<String> values) {
            addCriterion("product_code not in", values, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeBetween(String value1, String value2) {
            addCriterion("product_code between", value1, value2, "productCode");
            return (Criteria) this;
        }
        public Criteria andProductCodeNotBetween(String value1, String value2) {
            addCriterion("product_code not between", value1, value2, "productCode");
            return (Criteria) this;
        }
        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }
        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }
        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }
        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }
        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }
        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }
        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }
        public Criteria andCustomerIdIsNull() {
            addCriterion("customer_id is null");
            return (Criteria) this;
        }
        public Criteria andCustomerIdIsNotNull() {
            addCriterion("customer_id is not null");
            return (Criteria) this;
        }
        public Criteria andCustomerIdEqualTo(String value) {
            addCriterion("customer_id =", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdNotEqualTo(String value) {
            addCriterion("customer_id <>", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdGreaterThan(String value) {
            addCriterion("customer_id >", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdGreaterThanOrEqualTo(String value) {
            addCriterion("customer_id >=", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdLessThan(String value) {
            addCriterion("customer_id <", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdLessThanOrEqualTo(String value) {
            addCriterion("customer_id <=", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdLike(String value) {
            addCriterion("customer_id like", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdNotLike(String value) {
            addCriterion("customer_id not like", value, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdIn(List<String> values) {
            addCriterion("customer_id in", values, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdNotIn(List<String> values) {
            addCriterion("customer_id not in", values, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdBetween(String value1, String value2) {
            addCriterion("customer_id between", value1, value2, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerIdNotBetween(String value1, String value2) {
            addCriterion("customer_id not between", value1, value2, "customerId");
            return (Criteria) this;
        }
        public Criteria andCustomerNameIsNull() {
            addCriterion("customer_name is null");
            return (Criteria) this;
        }
        public Criteria andCustomerNameIsNotNull() {
            addCriterion("customer_name is not null");
            return (Criteria) this;
        }
        public Criteria andCustomerNameEqualTo(String value) {
            addCriterion("customer_name =", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameNotEqualTo(String value) {
            addCriterion("customer_name <>", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameGreaterThan(String value) {
            addCriterion("customer_name >", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameGreaterThanOrEqualTo(String value) {
            addCriterion("customer_name >=", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameLessThan(String value) {
            addCriterion("customer_name <", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameLessThanOrEqualTo(String value) {
            addCriterion("customer_name <=", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameLike(String value) {
            addCriterion("customer_name like", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameNotLike(String value) {
            addCriterion("customer_name not like", value, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameIn(List<String> values) {
            addCriterion("customer_name in", values, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameNotIn(List<String> values) {
            addCriterion("customer_name not in", values, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameBetween(String value1, String value2) {
            addCriterion("customer_name between", value1, value2, "customerName");
            return (Criteria) this;
        }
        public Criteria andCustomerNameNotBetween(String value1, String value2) {
            addCriterion("customer_name not between", value1, value2, "customerName");
            return (Criteria) this;
        }
        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }
        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }
        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }
        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
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
        public Criteria andCreaterAccountIsNull() {
            addCriterion("creater_account is null");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountIsNotNull() {
            addCriterion("creater_account is not null");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountEqualTo(String value) {
            addCriterion("creater_account =", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountNotEqualTo(String value) {
            addCriterion("creater_account <>", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountGreaterThan(String value) {
            addCriterion("creater_account >", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountGreaterThanOrEqualTo(String value) {
            addCriterion("creater_account >=", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountLessThan(String value) {
            addCriterion("creater_account <", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountLessThanOrEqualTo(String value) {
            addCriterion("creater_account <=", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountLike(String value) {
            addCriterion("creater_account like", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountNotLike(String value) {
            addCriterion("creater_account not like", value, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountIn(List<String> values) {
            addCriterion("creater_account in", values, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountNotIn(List<String> values) {
            addCriterion("creater_account not in", values, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountBetween(String value1, String value2) {
            addCriterion("creater_account between", value1, value2, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterAccountNotBetween(String value1, String value2) {
            addCriterion("creater_account not between", value1, value2, "createrAccount");
            return (Criteria) this;
        }
        public Criteria andCreaterNameIsNull() {
            addCriterion("creater_name is null");
            return (Criteria) this;
        }
        public Criteria andCreaterNameIsNotNull() {
            addCriterion("creater_name is not null");
            return (Criteria) this;
        }
        public Criteria andCreaterNameEqualTo(String value) {
            addCriterion("creater_name =", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameNotEqualTo(String value) {
            addCriterion("creater_name <>", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameGreaterThan(String value) {
            addCriterion("creater_name >", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameGreaterThanOrEqualTo(String value) {
            addCriterion("creater_name >=", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameLessThan(String value) {
            addCriterion("creater_name <", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameLessThanOrEqualTo(String value) {
            addCriterion("creater_name <=", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameLike(String value) {
            addCriterion("creater_name like", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameNotLike(String value) {
            addCriterion("creater_name not like", value, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameIn(List<String> values) {
            addCriterion("creater_name in", values, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameNotIn(List<String> values) {
            addCriterion("creater_name not in", values, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameBetween(String value1, String value2) {
            addCriterion("creater_name between", value1, value2, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterNameNotBetween(String value1, String value2) {
            addCriterion("creater_name not between", value1, value2, "createrName");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelIsNull() {
            addCriterion("creater_level is null");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelIsNotNull() {
            addCriterion("creater_level is not null");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelEqualTo(String value) {
            addCriterion("creater_level =", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelNotEqualTo(String value) {
            addCriterion("creater_level <>", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelGreaterThan(String value) {
            addCriterion("creater_level >", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelGreaterThanOrEqualTo(String value) {
            addCriterion("creater_level >=", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelLessThan(String value) {
            addCriterion("creater_level <", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelLessThanOrEqualTo(String value) {
            addCriterion("creater_level <=", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelLike(String value) {
            addCriterion("creater_level like", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelNotLike(String value) {
            addCriterion("creater_level not like", value, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelIn(List<String> values) {
            addCriterion("creater_level in", values, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelNotIn(List<String> values) {
            addCriterion("creater_level not in", values, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelBetween(String value1, String value2) {
            addCriterion("creater_level between", value1, value2, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterLevelNotBetween(String value1, String value2) {
            addCriterion("creater_level not between", value1, value2, "createrLevel");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdIsNull() {
            addCriterion("creater_dedpartment_id is null");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdIsNotNull() {
            addCriterion("creater_dedpartment_id is not null");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdEqualTo(String value) {
            addCriterion("creater_dedpartment_id =", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdNotEqualTo(String value) {
            addCriterion("creater_dedpartment_id <>", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdGreaterThan(String value) {
            addCriterion("creater_dedpartment_id >", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdGreaterThanOrEqualTo(String value) {
            addCriterion("creater_dedpartment_id >=", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdLessThan(String value) {
            addCriterion("creater_dedpartment_id <", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdLessThanOrEqualTo(String value) {
            addCriterion("creater_dedpartment_id <=", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdLike(String value) {
            addCriterion("creater_dedpartment_id like", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdNotLike(String value) {
            addCriterion("creater_dedpartment_id not like", value, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdIn(List<String> values) {
            addCriterion("creater_dedpartment_id in", values, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdNotIn(List<String> values) {
            addCriterion("creater_dedpartment_id not in", values, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdBetween(String value1, String value2) {
            addCriterion("creater_dedpartment_id between", value1, value2, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andCreaterDedpartmentIdNotBetween(String value1, String value2) {
            addCriterion("creater_dedpartment_id not between", value1, value2, "createrDedpartmentId");
            return (Criteria) this;
        }
        public Criteria andContractCodeIsNull() {
            addCriterion("contract_code is null");
            return (Criteria) this;
        }
        public Criteria andContractCodeIsNotNull() {
            addCriterion("contract_code is not null");
            return (Criteria) this;
        }
        public Criteria andContractCodeEqualTo(String value) {
            addCriterion("contract_code =", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeNotEqualTo(String value) {
            addCriterion("contract_code <>", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeGreaterThan(String value) {
            addCriterion("contract_code >", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeGreaterThanOrEqualTo(String value) {
            addCriterion("contract_code >=", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeLessThan(String value) {
            addCriterion("contract_code <", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeLessThanOrEqualTo(String value) {
            addCriterion("contract_code <=", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeLike(String value) {
            addCriterion("contract_code like", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeNotLike(String value) {
            addCriterion("contract_code not like", value, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeIn(List<String> values) {
            addCriterion("contract_code in", values, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeNotIn(List<String> values) {
            addCriterion("contract_code not in", values, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeBetween(String value1, String value2) {
            addCriterion("contract_code between", value1, value2, "contractCode");
            return (Criteria) this;
        }
        public Criteria andContractCodeNotBetween(String value1, String value2) {
            addCriterion("contract_code not between", value1, value2, "contractCode");
            return (Criteria) this;
        }
        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }
        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }
        public Criteria andStatusEqualTo(Short value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusLessThan(Short value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }
        public Criteria andStatusIn(List<Short> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }
        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }
        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }
        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }
        public Criteria andActionStatusIsNull() {
            addCriterion("action_status is null");
            return (Criteria) this;
        }
        public Criteria andActionStatusIsNotNull() {
            addCriterion("action_status is not null");
            return (Criteria) this;
        }
        public Criteria andActionStatusEqualTo(Short value) {
            addCriterion("action_status =", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusNotEqualTo(Short value) {
            addCriterion("action_status <>", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusGreaterThan(Short value) {
            addCriterion("action_status >", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("action_status >=", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusLessThan(Short value) {
            addCriterion("action_status <", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusLessThanOrEqualTo(Short value) {
            addCriterion("action_status <=", value, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusIn(List<Short> values) {
            addCriterion("action_status in", values, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusNotIn(List<Short> values) {
            addCriterion("action_status not in", values, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusBetween(Short value1, Short value2) {
            addCriterion("action_status between", value1, value2, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andActionStatusNotBetween(Short value1, Short value2) {
            addCriterion("action_status not between", value1, value2, "actionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusIsNull() {
            addCriterion("exception_status is null");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusIsNotNull() {
            addCriterion("exception_status is not null");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusEqualTo(Short value) {
            addCriterion("exception_status =", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusNotEqualTo(Short value) {
            addCriterion("exception_status <>", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusGreaterThan(Short value) {
            addCriterion("exception_status >", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("exception_status >=", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusLessThan(Short value) {
            addCriterion("exception_status <", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusLessThanOrEqualTo(Short value) {
            addCriterion("exception_status <=", value, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusIn(List<Short> values) {
            addCriterion("exception_status in", values, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusNotIn(List<Short> values) {
            addCriterion("exception_status not in", values, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusBetween(Short value1, Short value2) {
            addCriterion("exception_status between", value1, value2, "exceptionStatus");
            return (Criteria) this;
        }
        public Criteria andExceptionStatusNotBetween(Short value1, Short value2) {
            addCriterion("exception_status not between", value1, value2, "exceptionStatus");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {


        protected Criteria() {
            super();
        }
    }

    /**
     * 项目
     * 
     * @author 
     * @version 1.0 2018-08-10
     */
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