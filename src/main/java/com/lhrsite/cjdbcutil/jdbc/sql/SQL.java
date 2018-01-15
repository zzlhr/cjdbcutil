package com.lhrsite.cjdbcutil.jdbc.sql;

import java.util.List;

/**
 * Sql语句对象
 * @author lhr
 * @create 2018/1/11
 */
public class SQL {

    /**
     * 操作类型
     */
    public static final int TYPE_INSERT = 0;

    public static final int TYPE_DELECT = 1;

    public static final int TYPE_UPDATE = 2;

    public static final int TYPE_SELECT = 3;


    /**
     * sql类型，select,update,insert
     */
    private int sqlType;

    /**
     * 查询的字段
     */
    private List<Field> fields;


    /**
     * 表名
     */
    private List<Table> tables;


    /**
     * where条件
     */
    private List<Where> wheres;

    /**
     * 分页
     */
    private Limit limit;

    /**
     * 排序
     */
    private List<Order> orders;


    /**
     * 是否编译,只有编译了的SQL对象才能提交.
     */
    private Boolean build = false;

    /**
     * sql语句
     */
    private String sql;


    /**
     * 参数
     */
    private List<Param> params;




    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Table> getTable() {
        return tables;
    }

    public void setTable(List<Table> tables) {
        this.tables = tables;
    }

    public List<Where> getWheres() {
        return wheres;
    }

    public void setWheres(List<Where> wheres) {
        this.wheres = wheres;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Boolean getBuild() {
        return build;
    }

    public void setBuild(Boolean build) {
        this.build = build;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
