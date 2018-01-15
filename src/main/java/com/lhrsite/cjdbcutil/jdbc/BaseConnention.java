package com.lhrsite.cjdbcutil.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取Connection
 * @author lhr
 * @create 2017/12/22
 */
@Component("baseConnention")
public class BaseConnention {


    @Autowired
    private DataSource dataSource;


    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (this.connection == null){
            this.connection = dataSource.getConnection();
        }
        return this.connection;
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }
}
