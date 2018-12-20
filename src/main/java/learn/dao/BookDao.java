package learn.dao;

import learn.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static learn.dao.DaoUtils.close;
import static learn.dao.DaoUtils.getConnection;

public class BookDao {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        for (Book book : findAll()) {
//            System.out.println(book);
//        }
//        save("ss");
        System.out.println(findBookByName("Java"));
    }

    /**
     * 获取所有笔记本
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Book> findAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * from t_book";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery();
        List<Book> books = new ArrayList<Book>();
        while (rs.next()) {
            Book book = new Book();
            book.setId(Integer.valueOf(rs.getString("id")));
            book.setName(rs.getString("name"));
            books.add(book);
        }
        // 释放资源
        close(stat, conn);
        return books;
    }

    /**
     * 新增笔记本
     * @param bookName
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void save(String bookName) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO t_book(name) VALUES(?)";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, bookName);
        int rs = stat.executeUpdate();
        if (rs == 1) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    /**
     * 根据笔记本名获取笔记本
     * @param bookName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Book> findBookByName(String bookName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM t_book WHERE name=?";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, bookName);
        ResultSet rs = stat.executeQuery();
        List<Book> books = new ArrayList<Book>();
        while (rs.next()) {
            Book book = new Book();
            book.setId(Integer.valueOf(rs.getString("id")));
            book.setName(rs.getString("name"));
            books.add(book);
        }
        // 释放资源
        close(stat, conn);
        return books;
    }

}
