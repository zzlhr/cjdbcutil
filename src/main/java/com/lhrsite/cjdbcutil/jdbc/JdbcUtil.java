package com.lhrsite.cjdbcutil.jdbc;


import com.lhrsite.cjdbcutil.jdbc.sql.Param;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * jdbc操作，sql语句
 * @author lhr
 * @create 2018/1/9
 */
public interface JdbcUtil {



    /**
     * 查询一个
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param params    参数集合
     * @param cs        Class类 例如 User.class
     * @return          查询对象
     * @throws JdbcUtilException
     */
    Object findOne(String sql, List<Param> params, Class cs) throws JdbcUtilException;


    /**
     * 查询一个
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param cs        Class类 例如 User.class
     * @return          查询对象
     * @throws JdbcUtilException
     */
    Object findOne(String sql, Class cs) throws JdbcUtilException;


    /**
     * 查询一个
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param param     参数
     * @param cs        Class类 例如 User.class
     * @return          查询对象
     * @throws JdbcUtilException
     */
    Object findOne(String sql, Param param, Class cs) throws JdbcUtilException;



    /**
     * 查询一个
     * <p>本方法不会对查询sql进行加工，查询出来的结果集个数可能是对个，只是对最后的list进行了get(0);</p>
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param params    参数集合
     * @return          Map or <b>null</b>
     * @throws JdbcUtilException
     */
    Map findOne(String sql, List<Param> params) throws JdbcUtilException;



    /**
     * 查询一个
     * <p>本方法不会对查询sql进行加工，查询出来的结果集个数可能是对个，只是对最后的list进行了get(0);</p>
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param param     单个参数
     * @return          Map or <b>null</b>
     * @throws JdbcUtilException
     */
    Map findOne(String sql, Param param) throws JdbcUtilException;

    /**
     * 查询多个
     * @param sql       sql语句推荐使用select xxx from where id=?表示也可以直接使用。
     * @param params    参数集合
     * @return          结果集合
     * @throws JdbcUtilException
     */
    List<Map<String, Object>> findAll(String sql, List<Param> params)
            throws JdbcUtilException;


    /**
     * 查询多个
     * @param sql       sql语句
     * @param params    参数
     * @param cs        类
     * @return          结果集
     * @throws JdbcUtilException
     */
    List findAll(String sql, List<Param> params, Class cs) throws JdbcUtilException;


    /**
     * 结果集转List<Map>
     * @param rs
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException;


    /**
     * 更新
     * @param sql       sql语句
     * @param params    参数
     * @return          受影响的行
     * @throws SQLException
     */
    int update(String sql, List<Param> params) throws JdbcUtilException;



    void insert(String sql, List<Param> params);

    void delect(String sql, List<Param> params);


    /**
     * 初始化方法，
     * 包含连接，设置sql语句，设置参数。
     * @param sql       sql语句
     * @param params    参数集合
     * @return          连接结果。
     */
    PreparedStatement init(String sql, List<Param> params);

    /**
     * 结果集转对象集合
     * @param rs    结果集
     * @param cs    对象类
     * @return      转换的对象集合
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    List getList(ResultSet rs, Class cs) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * 结果集转对象
     * @param rs    结果集
     * @param cs    对象类
     * @return      对象
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    Object getOne(ResultSet rs, Class cs) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * <p>通过泛型转换rs成为对象</p>
     * <b>
     *     必读：该方法的前提条件是查询出来的结果集中的
     *     col名必须去 下划线，转小写 后对应上对象的字段名
     * </b>
     * @param rs
     * @return
     */
    Object rsToObject(ResultSet rs, Class cs) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException;

    /**
     * 对java Object的属性名称和结果集的数据列名进行统一处理。
     * @param name   字段名或属性名
     * @return      处理后的值
     */
    String reNameColNameAndFieldName(String name);

    /**
     * 获取ResultSet对象的colName数组
     * @param rs    Result对象
     * @return      ColName数组
     * @throws SQLException
     */
    String[] getResultSetMetaData(ResultSet rs) throws SQLException;


    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    Map<String, String> getResultSetMetaDataToMap(ResultSet rs) throws SQLException;

    /**
     * 执行setter方法
     * @param t         泛型对象，被 <code>Class cs;   cs.newInstamce();</code>创建出来的对象
     *                  该参数，并非实际意义的泛型，只使用泛型承载，实际执行中是一个被实例化的对象。
     * @param method    执行的方法对象
     * @param value     欲赋的值
     * @return          传入的泛型对象
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object setter(Object t, Method method, Object value) throws InvocationTargetException, IllegalAccessException;


    /**
     * 关闭连接
     */
    void close();
}
