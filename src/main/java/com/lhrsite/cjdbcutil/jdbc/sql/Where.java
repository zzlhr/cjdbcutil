package com.lhrsite.cjdbcutil.jdbc.sql;

/**
 * Where条件
 * @author lhr
 * @create 2018/1/11
 */
public class Where {

    /**
     * and连接
     */
    public static final int TYPE_AND = 0;

    /**
     * or连接
     */
    public static final int TYPE_OR = 1;

    private int type;

    private String fields;

    private String operator;

    private String data;


    /**
     * 默认and连接
     * 调用方式: new Where("name", "=", "Kenny-Liu")
     * @param fields    字段名
     * @param operator  运算符
     * @param data      值
     */
    public Where(String fields, String operator, String data){
        this.type = Where.TYPE_AND;
        this.fields = fields;
        this.operator = operator;
        this.data = data;
    }

    /**
     * @param type      类型 Where.xxx
     * @param fields    字段
     * @param operator  运算符
     * @param data      值
     */
    public Where(int type, String fields, String operator, String data){
        this.type = type;
        this.fields = fields;
        this.operator = operator;
        this.data = data;
    }

    /**
     * 默认type and, 默认运算符位"="
     * @param fields  字段
     * @param data    值
     */
    public Where(String fields, String data){
        this.type = Where.TYPE_AND;
        this.fields = fields;
        this.operator = "=";
        this.data = data;
    }

    public static int getTypeAnd() {
        return TYPE_AND;
    }

    public static int getTypeOr() {
        return TYPE_OR;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return fields + operator + data;
    }
}
