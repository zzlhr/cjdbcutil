package com.lhrsite.cjdbcutil.jdbc.sql;

import com.lhrsite.cjdbcutil.jdbc.JdbcUtil;
import com.lhrsite.cjdbcutil.jdbc.JdbcUtilException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * sql语句组装
 * @author lhr
 * @create 2018/1/11
 */
@Component
@Slf4j
public class SqlBuildImpl implements SqlBuilder {

    private SQL sql = new SQL();;

    @Autowired
    private JdbcUtil jdbcUtil;

    @Override
    public SqlBuilder table(Table table) {
        List<Table> tables = new ArrayList<>();
        tables.add(table);
        return table(tables);
    }

    @Override
    public SqlBuilder table(List<Table> tables) {
        List<Table> baseTables = sql.getTable();
        if (baseTables == null){
            baseTables = tables;
        }else {
            baseTables.addAll(tables);
        }
        sql.setTable(baseTables);
        return this;
    }

    @Override
    public SqlBuilder field(List<Field> fields) {
        List<Field> baseFields = sql.getFields();
        if (baseFields == null){
            baseFields = fields;
        }else {
            baseFields.addAll(fields);
        }
        sql.setFields(baseFields);
        return this;
    }

    @Override
    public SqlBuilder field(Field field) {
        List<Field> fields = new ArrayList<>();
        fields.add(field);
        return field(fields);
    }

    @Override
    public SqlBuilder select() {
        sql.setSqlType(SQL.TYPE_SELECT);
        build();
        return this;
    }

    @Override
    public SqlBuilder update() {
        sql.setSqlType(SQL.TYPE_UPDATE);
        build();
        return this;
    }

    @Override
    public SqlBuilder insert() {
        sql.setSqlType(SQL.TYPE_INSERT);
        build();
        return this;
    }

    @Override
    public SqlBuilder delect() {
        sql.setSqlType(SQL.TYPE_DELECT);
        build();
        return this;
    }

    @Override
    public SqlBuilder where(Where where) {
        List<Where> wheres = new ArrayList<>();
        wheres.add(where);
        return where(wheres);
    }

    @Override
    public SqlBuilder where(List<Where> wheres) {
        List<Where> baseWheres = sql.getWheres();
       if (baseWheres == null){
           baseWheres = wheres;
       }else {
           baseWheres.addAll(wheres);
       }
       sql.setWheres(baseWheres);
       return this;
    }

    @Override
    public SqlBuilder order(Order order) {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        return order(orders);
    }

    @Override
    public SqlBuilder order(List<Order> orders) {

        List<Order> baseOrders = sql.getOrders();
        if (baseOrders == null){
            baseOrders = orders;
        }else{
            baseOrders.addAll(orders);
        }
        sql.setOrders(baseOrders);
        return this;
    }

    @Override
    public SqlBuilder limit(Limit limit) {
        sql.setLimit(limit);
        return this;
    }

    @Override
    public SqlBuilder param(List<Param> params) {

        List<Param> baseParams = sql.getParams();

        if(baseParams == null){
            baseParams = params;
        }else {
            baseParams.addAll(params);
        }

        sql.setParams(baseParams);

        return this;
    }

    @Override
    public SqlBuilder param(Param param) {

        List<Param> params = new ArrayList<>();

        params.add(param);

        return param(params);

    }

    @Override
    public SqlBuilder build() {

        String sqlStr = "";

        switch (sql.getSqlType()){

            case SQL.TYPE_INSERT:
                //增
                sqlStr = "INSERT INTO `{{table}}` ({{fields}}) values ({{values}})";

                List<Table> tables = sql.getTable();

                if (tables.size() > 1){
                    throw new JdbcUtilException("insert can't more than one table name");
                }

                Table table = tables.get(0);

                // List<Param> params = new ArrayList<>();

                // 设置表名
                // params.add(new Param(1, Param.STRING, table.getTableName()));
                sqlStr = sqlStr.replace("{{table}}", table.getTableName());

                //处理field
                List<Field> fields = sql.getFields();

                StringBuilder params = new StringBuilder();
                for (Field field : fields){
                    params.append("?, ");
                }

                // 切割掉最后的一个,
                params = new StringBuilder(params.substring(0, params.length() - 2));

                sqlStr = sqlStr.replace("{{fields}}", getFieldsToStr(fields));

                sqlStr = sqlStr.replace("{{values}}", params.toString());

                if (sql.getParams().size() != fields.size()){
                    // 参数个数不匹配
                    throw new JdbcUtilException("Parameter mismatch");
                }

                jdbcUtil.insert(sqlStr, sql.getParams());

                break;

            case SQL.TYPE_DELECT:
                //删除
                sqlStr = "DELETE FROM {{table}} where {{wheres}}";
                String tableName = sql.getTable().get(0).getTableName();
                sqlStr = sqlStr.replace("{{table}}", tableName);


                sqlStr = sqlStr.replace("{{where}}", getWhereToStr(sql.getWheres()));

                jdbcUtil.delect(sqlStr, sql.getParams());
                break;

            case SQL.TYPE_UPDATE:
                //改
                sqlStr = "UPDATE {{table}} SET {{value}}";
                tableName = sql.getTable().get(0).getTableName();


                StringBuilder updateFields = new StringBuilder();
                fields = sql.getFields();
                for (Field field : fields){
                    updateFields.append(field.getFiledName()).append("=?, ");
                }

                // 切割掉最后的一个,
                updateFields = new StringBuilder(updateFields.substring(0, updateFields.length() - 2));

                sqlStr = sqlStr;


                sqlStr = sqlStr.replace("{{table}}", tableName)
                        .replace("{{values}}", updateFields.toString());

                // 如果存在where条件就进行查询。
                if (sql.getWheres().size() != 0){
                    sqlStr += " WHERE {{where}}";
                    sqlStr = sqlStr.replace("{{where}}",
                            getWhereToStr(sql.getWheres()));
                }

                jdbcUtil.update(sqlStr, sql.getParams());

                break;

            case SQL.TYPE_SELECT:
                //查询
                sqlStr = "SELECT {{field}} FROM {{table}} {{JOININ}} {{WHERE}} {{ORDERBY}} {{LIMIT}}";


                // 字段
                if (sql.getFields() == null || sql.getFields().size() == 0){
                    sqlStr = sqlStr.replace("{{field}}", "*");
                }else {
                    sqlStr = sqlStr.replace("{{field}}", getFieldsToStr(sql.getFields()));
                }

                // 表
                if(sql.getTable() == null || sql.getTable().size() == 0){
                    throw new JdbcUtilException("table is not found!");
                }else if (sql.getTable().size() > 1){
                    //todo 多表查询。
                    throw new JdbcUtilException("框架暂未实现多表查询，敬请期待！");
                }else {
                    sqlStr = sqlStr.replace("{{table}}",
                            sql.getTable().get(0).getTableName());
                }

                //todo join in
                sqlStr = sqlStr.replace("{{JOININ}}", "");

                if (sql.getWheres() == null || sql.getWheres().size() != 0){
                    sqlStr = sqlStr.replace("{{WHERE}}"," WHERE {{WHERE}} ");
                    sqlStr = sqlStr.replace("{{WHERE}}",getWhereToStr(sql.getWheres()));
                }else {
                    sqlStr = sqlStr.replace("{{WHERE}}","");
                }

                if (sql.getOrders() != null && sql.getOrders().size() != 0){

                    sqlStr = sqlStr.replace("{{ORDERBY}}",
                            " ORDER BY "
                                    + getOrderByStr(sql.getOrders()));

                }else {
                    sqlStr = sqlStr.replace("{{ORDERBY}}",  "");
                }

                if (sql.getLimit() != null){
                    sqlStr = sqlStr.replace("{{LIMIT}}", sql.getLimit().toString());
                }else {
                    sqlStr = sqlStr.replace("{{LIMIT}}", "");
                }


                log.info("【结果】result={}", jdbcUtil.findAll(sqlStr, sql.getParams()).toString());
                break;

            default:
                break;
        }







        return null;
    }

    private String getWhereToStr(List<Where> wheres){
        StringBuilder wheresStr = new StringBuilder();

        int time = 1;

        for(Where where : wheres){
            if (where.getData() == null){
                continue;
            }

            String ws = where.toString();

            if (time != wheres.size()){
                //最后一个where条件
                ws += " and ";
            }

            wheresStr.append(ws);
            time++;
        }
        return wheresStr.toString();
    }



    public String getFieldsToStr(List<Field> fields){
        //处理field

        StringBuilder fieldsStr = new StringBuilder();
        for (Field field : fields){
            fieldsStr.append(field.getFiledName()).append(", ");
        }

        // 切割掉最后的一个,
        fieldsStr = new StringBuilder(fieldsStr.substring(0, fieldsStr.length() - 2));
        // params.add(new Param(2, Param.STRING, insertFields));

        return fieldsStr.toString();

    }


    public String getOrderByStr(List<Order> orders){

        StringBuilder orderStr = new StringBuilder();

        for (Order order : orders){
            orderStr.append(order.getFields());
            switch (order.getType()){
                case Order.TYPE_ASC:
                    orderStr.append("asc");
                    break;
                case Order.TYPE_DESC:
                    orderStr.append("desc");
                default:
                    break;
            }
            orderStr.append(", ");
        }

        return orderStr.substring(0, orderStr.length() - 2);
    }




}
