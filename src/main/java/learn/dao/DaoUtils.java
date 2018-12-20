package learn.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 数据库连接、释放
 */
public class DaoUtils {

    /**
     * 连接
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                // ?useUnicode=true&characterEncoding=utf-8解决中文乱码
                "jdbc:mysql://120.79.197.130:3307/javafx?useUnicode=true&characterEncoding=utf-8&autoReconnect=true", "root", "123");
        return conn;
    }

    /**
     * 断开
     * @param stat
     * @param conn
     * @throws SQLException
     */
    public static void close(PreparedStatement stat, Connection conn) throws SQLException {
        stat.close();
        conn.close();
    }

}
