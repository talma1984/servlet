package com.company;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    private static DataSource ds;

    public static Connection conn() throws SQLException{
        if(ds == null){
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context)initContext.lookup("java:/comp/env");
                ds = (DataSource)envContext.lookup("jdbc/northwind");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return ds.getConnection();
    }


    public static void query(String sql, ConfigureStatement configureStatement, ProcessResultSet processResultSet, Object... params){
        try(Connection conn = conn()){
            try(PreparedStatement statement = conn.prepareStatement(sql)){
                if (configureStatement != null) {
                    configureStatement.configureStatement(statement);
                }
                try(ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()){
                        if(!processResultSet.process(resultSet, params))
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void query(String sql, ProcessResultSet processResultSet) {
        query(sql, null, processResultSet);
    }

    public interface ProcessResultSet{
        boolean process(ResultSet resultSet, Object... params) throws SQLException;
    }

    public interface ConfigureStatement{
        void configureStatement(PreparedStatement statement) throws SQLException;
    }
}