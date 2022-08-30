package org.orm.dataSource;

import java.sql.SQLException;

public class SessionFactory {
    private final DataSourceWithConnectionPool dataSourceWithConnectionPool;

    public SessionFactory(String url, String userName, String password) throws SQLException {
        this.dataSourceWithConnectionPool = new DataSourceWithConnectionPool(url, userName, password);
    }

    public Session createSession() {
        try {
            return new Session(this.dataSourceWithConnectionPool);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }
}
