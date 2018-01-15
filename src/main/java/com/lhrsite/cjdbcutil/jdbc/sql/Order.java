package com.lhrsite.cjdbcutil.jdbc.sql;

/**
 * 排序对象
 * @author lhr
 * @create 2018/1/11
 */
public class Order {


    public static final int TYPE_ASC = 0;


    public static final int TYPE_DESC = 1;

    private String fields;

    private int type;


    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
