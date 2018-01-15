package com.lhrsite.cjdbcutil.jdbc;

import com.lhrsite.cjdbcutil.jdbc.sql.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * jdbc数据库工具类实现
 * @author lhr
 * @create 2018/1/5
 */
@Component
@Slf4j
public class JdbcUtilImpl implements JdbcUtil {

    @Resource
    private BaseConnention baseConnention;

    private Connection conn;

    private ResultSet rs;

    private PreparedStatement ps;

    @Override
    public Object findOne(String sql, List<Param> params, Class cs) throws JdbcUtilException {

        ps = init(sql, params);

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交失败: " + e.getMessage());
        }

        Object t = null;
        try {
            t = this.getOne(rs, cs);
        } catch (SQLException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("获取结果集失败: " + e.getMessage());
        }


        //关闭连接
        this.close();

        return t;

    }

    @Override
    public Object findOne(String sql, Class cs) throws JdbcUtilException {


        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交失败: " + e.getMessage());
        }



        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交失败: " + e.getMessage());
        }

        Object t = null;
        try {
            t = this.getOne(rs, cs);
        } catch (SQLException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("获取结果集失败: " + e.getMessage());
        }


        //关闭连接
        this.close();

        return t;
    }

    @Override
    public Object findOne(String sql, Param param, Class cs) throws JdbcUtilException {
        return findOne(sql, Arrays.asList(param), cs);
    }

    @Override
    public Map findOne(String sql, List<Param> params) throws JdbcUtilException {

        List<Map<String, Object>> result = null;

        if ((result = findAll(sql, params)).size() == 0){
            return null;
        }

        return result.get(0);
    }

    @Override
    public Map findOne(String sql, Param param) throws JdbcUtilException {

        List<Map<String, Object>> result = null;

        List<Param> params = new ArrayList<>();

        params.add(param);

        if ((result = findAll(sql, params)).size() == 0){
            return null;
        }

        return result.get(0);
    }

    @Override
    public List<Map<String, Object>> findAll(String sql, List<Param> params) throws JdbcUtilException {
        ps = init(sql, params);

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交失败: " + e.getMessage());
        }


        List<Map<String, Object>> result = null;
        try {
            result = this.resultSetToList(rs);
        } catch (SQLException e){
            log.error(e.getMessage());
            throw new JdbcUtilException("获取结果集失败: " + e.getMessage());
        }


        //关闭连接
        this.close();

        return result;
    }

    @Override
    public List findAll(String sql, List<Param> params, Class cs) throws JdbcUtilException {
        ps = init(sql, params);

        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交异常!");
        }

        List ts = null;
        try {
            ts = this.getList(rs, cs);
        } catch (SQLException
                | InvocationTargetException
                | IllegalAccessException
                | InstantiationException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("获取结果集失败!");
        }

        //关闭连接
        this.close();
        return ts;
    }

    @Override
    public List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        // 获取所有列名
        String[] cols = getResultSetMetaData(rs);

        //创建返回对象
        List<Map<String, Object>> result = new ArrayList<>();

        while (rs.next()){
            //循环rs-行单元
            for (String col : cols){
                //获取列元素存入一个map
                Map map = new HashMap();
                map.put(col, rs.getObject(col));
                //map存入返回集合中
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public int update(String sql, List<Param> params) throws JdbcUtilException {

        ps = init(sql, params);


        int rs = 0;

        try {
            rs = ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("提交异常!");
        }

        //关闭连接
        this.close();

        return rs;
    }

    @Override
    public void insert(String sql, List<Param> params) {
        ps = this.init(sql, params);

        try {
            ps.execute();
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new JdbcUtilException("提交异常!");
        }

        //关闭连接
        this.close();
    }

    @Override
    public void delect(String sql, List<Param> params) {
        ps = this.init(sql, params);

        try {
            ps.execute();
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new JdbcUtilException("提交异常!");
        }

        this.close();

    }

    @Override
    public PreparedStatement init(String sql, List<Param> params) {

        log.info("【SQL】 sql={}, param={}",sql, params);

        // 获取连接
        try {
            conn = baseConnention.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("创建conn连接失败!");
        }

        // 获取ps
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("创建PreparedStatement失败!");
        }

        // 设置参数
        try {
            ps = Param.setParams(ps, params);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("设置参数失败!");
        }

        return ps;
    }

    @Override
    public List getList(ResultSet rs, Class cs) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List list = new ArrayList<>();
        while (rs.next()){
            list.add(rsToObject(rs, cs));
        }
        return list;
    }

    @Override
    public Object getOne(ResultSet rs, Class cs) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (rs.next()){
            return rsToObject(rs, cs);
        }
        return null;
    }

    @Override
    public Object rsToObject(ResultSet rs, Class cs) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException {
        Object object = cs.newInstance();

        Method[] methods =  cs.getMethods();
        int i = 1;
        for (Method method : methods){
            //判断setter
            if ("set".equals(method.getName().substring(0,3))){
//

                String methodName = method.getName();

                // 取对象属性
                String fieldName
                        = methodName.substring(3, methodName.length());

                // 处理字段名
                fieldName = reNameColNameAndFieldName(fieldName);

                /*
                   对java对象字段名和查询结果集的col名进行统一处理
                  */
                Map<String, String> colMap = getResultSetMetaDataToMap(rs);

                String colName = colMap.get(fieldName);

                if (colName == null){
                    // 返回的结果集中没有该字段跳过
                    continue;
                }

                //获取类型
                Object value = rs.getObject(colName);

                object = setter(object, method, value);
                i++;
            }
        }
        System.out.println(object);
        return object;
    }

    /**
     * 对java Object的属性名称和结果集的数据列名进行统一处理。
     * @param name   字段名或属性名
     * @return  处理后的值
     */
    @Override
    public String reNameColNameAndFieldName(String name){

        // 去下划线
        name = name.replace("_", "");

        // 全部转为小写
        name = name.toLowerCase();

        return name;
    }


    @Override
    public String[] getResultSetMetaData(ResultSet rs) throws SQLException {
        // 获得列集
        ResultSetMetaData rsm =rs.getMetaData();

        // 获得列的个数
        int col = rsm.getColumnCount();

        String colName[] = new String[col];

        //取结果集中的表头名称, 放在colName数组中

        Map<String, String> result = new HashMap<>();


        for (int i = 0; i < col; i++) {
            //-->第一列,从1开始.所以获取列名,或列值,都是从1开始
            colName[i] = rsm.getColumnName(i + 1);
        }

        return colName;
    }

    @Override
    public Map<String, String> getResultSetMetaDataToMap(ResultSet rs) throws SQLException {
        String [] cols = getResultSetMetaData(rs);

        Map<String, String> result = new HashMap();
        for (int i = 0; i < cols.length; i++) {

            // 对应一下去符号和转小写的新字段名存入map，返回。
            String reColName = cols[i].replace("_", "").toLowerCase();

            result.put(reColName, cols[i]);
            //-->获得列值的方式一:通过其序号
        }

        return result;
    }

    @Override
    public Object setter(Object t, Method method, Object value) throws InvocationTargetException, IllegalAccessException {
        if (value == null){
            return t;
        }
        //获取setter参数类型
        Type[] types = method.getParameterTypes();
        Type type = types[0];
        if ("java.lang.Integer".equals(type.getTypeName())){
            method.invoke(t, Integer.parseInt(value.toString()));
        }
        if ("java.lang.String".equals(type.getTypeName())){
            method.invoke(t, value.toString());
        }
        if ("java.lang.Date".equals(type.getTypeName())){
            method.invoke(t, Date.valueOf(value.toString()));
        }
        //todo:...其他类型
        return t;
    }

    @Override
    public void close() {

        try {

            if (rs != null){
                rs.close();
            }
            if (ps != null){
                ps.close();
            }
//            if (conn != null){
//                conn.close();
//            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new JdbcUtilException("关闭连接异常!");
        } catch (NullPointerException e){
            log.warn(e.getMessage());
        }
    }
}
