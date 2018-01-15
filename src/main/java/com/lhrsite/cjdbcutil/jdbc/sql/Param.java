package com.lhrsite.cjdbcutil.jdbc.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC参数
 * @author lhr
 * @create 2018/1/5
 */
public class Param {

    public static final int INT = 0;
    public static final int STRING = 1;
    public static final int BYTE = 3;
    public static final int DATE = 4;

    public Param(int id, int type, Object data){
        this.id = id;
        this.type = type;
        this.data = data;
    }

    /**
     * 此处作为序号使用
     */
    private int id;

    /**
     * 参数数据类型
     */
    private int type;

    /**
     * 参数值
     */
    private Object data;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Param{" +
                "id=" + id +
                ", type=" + type +
                ", data=" + data +
                '}';
    }




    /**
     * 设置参数-多个参数
     * @param ps        PreparedStatement对象
     * @param params    参数数组
     * @return          PreparedStatement对象
     */
    public static PreparedStatement setParams(PreparedStatement ps, List<Param> params)
            throws SQLException {

        if (params != null){
            for (Param param : params){
                setParam(ps, param);
            }
        }


        return ps;
    }

    /**
     * 设置参数-单个参数
     * @param ps      PreparedStatement对象
     * @param param   单个参数对象
     * @return        PreparedStatement对象
     */
    public static PreparedStatement setParam(PreparedStatement ps, Param param) throws SQLException {
        switch (param.getType()){
            case Param.INT:
                ps.setInt(param.getId(), (Integer) param.getData());
                break;
            case Param.STRING:
                ps.setString(param.getId(), param.getData().toString());
                break;
            case Param.BYTE:
                ps.setByte(param.getId(), (Byte) param.getData());
                break;
            case Param.DATE:
                ps.setDate(param.getId(), (Date) param.getData());
                break;
        }
        return ps;
    }
}
