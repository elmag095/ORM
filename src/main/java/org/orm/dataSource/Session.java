package org.orm.dataSource;

import org.orm.annotation.Column;
import org.orm.annotation.Table;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<EntityKey<?>, Object> entityMap = new HashMap<>();
    private final Connection connection;
    private final String SELECT = "SELECT * FROM %s where id = %d";

    public Session(DataSourceWithConnectionPool dataSourceWithConnectionPool) {
        this.connection = dataSourceWithConnectionPool.getConnection();
    }

    public <T> T find(Class<T> entityType, Long id) {
        EntityKey<T> entityKey = new EntityKey<>(entityType, id);
        Object o = entityMap.get(entityKey);
        if (o != null) {
            return (T) o;
        } else {
            Statement statement;
            ResultSet resultSet;
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(getSelect(entityType, id));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            T entity = createObject(entityType, resultSet);
            entityMap.put(entityKey, entity);
            return entity;
        }
    }

    private <T> String getSelect(Class<T> entityType, Long id) {
        return String.format(SELECT, getEntityName(entityType), id);
    }

    private <T> String getEntityName(Class<T> entityType) {
        return entityType.getAnnotation(Table.class).name();
    }

    private <T> T createObject(Class<T> entityType, ResultSet resultSet) {
        T entity = null;
        try {
            resultSet.next();
            entity = entityType.getConstructor().newInstance();
            for (Field declaredField : entityType.getDeclaredFields()) {
                String name = declaredField.getAnnotation(Column.class).name();
                declaredField.setAccessible(true);
                declaredField.set(entity, resultSet.getObject(name));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

record EntityKey<T>(Class<T> type, Object id) {
};
