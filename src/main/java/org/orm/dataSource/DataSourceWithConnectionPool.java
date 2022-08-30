package org.orm.dataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataSourceWithConnectionPool extends MysqlDataSource {
    private final Queue<Connection> connectionsPoll;

    public DataSourceWithConnectionPool(String url, String userName, String password) throws SQLException {
        this.connectionsPoll = new ConcurrentLinkedQueue<>();
        this.setUrl(url);
        this.setUser(userName);
        this.setPassword(password);
        initConnectionsPoll();
    }

    private void initConnectionsPoll() throws SQLException {
        for (int i = 0; i < 10; i++) {
            connectionsPoll.add(new ConnectionPoll(super.getConnection(), connectionsPoll));
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return connectionsPoll.poll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
