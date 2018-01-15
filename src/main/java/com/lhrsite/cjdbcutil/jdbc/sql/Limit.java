package com.lhrsite.cjdbcutil.jdbc.sql;

/**
 * 分页对象
 * @author lhr
 * @create 2018/1/11
 */
public class Limit {

    private int begin;

    private int end;

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
         return " limit " + begin + ", " + end;
    }
}
