package com.lhrsite.cjdbcutil.jdbc.sql;

/**
 * 表对象
 * @author lhr
 * @create 2018/1/11
 */
public class Table {

    private String tableName;

    private Class aClass;


    public Table(String tableName, Class aClass){
        this.tableName = tableName;
        this.aClass = aClass;
    }


    public Table(){ }

    public Table(String tableName){
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }
}
