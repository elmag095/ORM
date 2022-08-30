package org.orm;

import org.orm.dataSource.Session;
import org.orm.dataSource.SessionFactory;

import java.sql.*;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/gelius";
    private static final String USER = "root";
    private static final String PASSWORD = "vfpt_YBRFYNFPT095";
    public static void main(String[] args) throws SQLException {
        SessionFactory sessionFactory = new SessionFactory(URL, USER, PASSWORD);
        Session session = sessionFactory.createSession();
        Role role = session.find(Role.class, 1L);
        Role role2 = session.find(Role.class, 1L);
        Role role3 = session.find(Role.class, 2L);
        System.out.println(role == role2);
        System.out.println(role == role3);
        System.out.println(role);
        System.out.println(role3);
        session.close();
    }
}