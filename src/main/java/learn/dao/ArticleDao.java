package learn.dao;

import learn.entity.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static learn.dao.DaoUtils.close;
import static learn.dao.DaoUtils.getConnection;

public class ArticleDao {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        for (Article article : findAll()) {
//        for (Article article : findAllArticlesByBookId(1)) {
//            System.out.println(article);
//        }
//        save("xxx", "qqq", 2);
//        System.out.println(findArticleByName("cun"));
        deleteArticleByName("555");
    }

    /**
     * 获取所有文章
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Article> findAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * from t_article";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery();
        List<Article> articleArrayList = new ArrayList<Article>();
        while (rs.next()) {
            Article article = new Article();
            article.setId(Integer.valueOf(rs.getString("id")));
            article.setName(rs.getString("name"));
            article.setContent(rs.getString("content"));
//            article.setBookId(Integer.valueOf(rs.getString("book_id")));
            articleArrayList.add(article);
        }
        // 释放资源
        close(stat, conn);
        return articleArrayList;
    }

    /**
     * 根据外键笔记本id获取文章
     * @param bookId
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Article> findAllArticlesByBookId(Integer bookId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM t_article WHERE book_id=?";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setInt(1, bookId);
        ResultSet rs = stat.executeQuery();
        List<Article> articleArrayList = new ArrayList<Article>();
        while (rs.next()) {
            Article article = new Article();
            article.setId(Integer.valueOf(rs.getString("id")));
            article.setName(rs.getString("name"));
            article.setContent(rs.getString("content"));
            article.setBookId(Integer.valueOf(rs.getString("book_id")));
            articleArrayList.add(article);
        }
        // 释放资源
        close(stat, conn);
        return articleArrayList;
    }

    /**
     * 新增文章
     * @param name
     * @param content
     * @param bookId
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void save(String name, String content, Integer bookId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO t_article(name,content,book_id) VALUES(?,?,?)";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, name);
        stat.setString(2, content);
        stat.setInt(3, bookId);
        int rs = stat.executeUpdate();
        if (rs == 1) {
            System.out.println("插入成功");
        } else {
            System.out.println("插入失败");
        }
    }

    /**
     * 根据文章名获取文章
     * @param articleName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Article> findArticleByName(String articleName) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM t_article WHERE name=?";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, articleName);
        ResultSet rs = stat.executeQuery();
        List<Article> articleArrayList = new ArrayList<Article>();
        while (rs.next()) {
            Article article = new Article();
            article.setId(Integer.valueOf(rs.getString("id")));
            article.setName(rs.getString("name"));
            article.setContent(rs.getString("content"));
            article.setBookId(Integer.valueOf(rs.getString("book_id")));
            articleArrayList.add(article);
        }
        // 释放资源
        close(stat, conn);
        return articleArrayList;
    }

    /**
     * 根据文章名跟新文章
     * @param articleName
     * @param newContent
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void updateArticleByName(String articleName, String newContent) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE t_article SET content=? WHERE name=?";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, newContent);
        stat.setString(2, articleName);
        int rs = stat.executeUpdate();
        if (rs != 0) {
            System.out.println("更新成功");
        } else {
            System.out.println("更新失败");
        }
        // 释放资源
        close(stat, conn);
    }

    /**
     * 根据文章名删除文章
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void deleteArticleByName(String articleName) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM t_article WHERE name = ?";
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, articleName);
        int rs = stat.executeUpdate();
        if (rs == 1) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        // 释放资源
        close(stat, conn);
    }

}
