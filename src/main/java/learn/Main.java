package learn;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import learn.controller.center.CenterController;
import learn.controller.top.TopController;
import learn.dao.ArticleDao;
import learn.dao.BookDao;
import learn.entity.Article;
import learn.entity.Book;

import java.sql.SQLException;
import java.util.List;

import static learn.controller.top.TopController.newArticle;
import static learn.controller.top.TopController.newBook;

/**
 * 启动类
 */
public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws SQLException, ClassNotFoundException {
        stage.setTitle("ITAEM Note");
        // 设置 app icon 为 alibaba logo
        stage.getIcons().add(new Image("http://www.lgstatic.com/i/image/M00/01/92/CgqKkVZv9oyAebyDAACpLbec-1E022.jpg"));
        Group rootGroup = new Group();
        Scene scene = new Scene(rootGroup, 800, 500, Color.WHITE);
        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.prefHeightProperty().bind(scene.heightProperty());
        rootBorderPane.prefWidthProperty().bind(scene.widthProperty());

        // 左侧树---------------------------------------------------------------------------------
        VBox leftVBox = new VBox();
        leftVBox.setPadding(new Insets(10)); //内边距
        leftVBox.setSpacing(8); //节点间距
        leftVBox.setStyle("-fx-background-color: #2a333c;"); //背景色
        TreeItem<String> rootItem = new TreeItem<String>("笔记本");
        rootItem.setExpanded(true);
        // 记事本s
        List<Book> bookList = BookDao.findAll();
        for (Book showBook : bookList) {
            TreeItem<String> bookItem = new TreeItem<String>(showBook.getName());
            bookItem.setExpanded(true);
            // 日志s
            List<Article> articleList = ArticleDao.findAllArticlesByBookId(showBook.getId());
            for (Article showArticle : articleList) {
                TreeItem<String> articleItem = new TreeItem<String>(showArticle.getName());

                bookItem.getChildren().add(articleItem);
            }
            rootItem.getChildren().add(bookItem);
        }

        TreeView<String> tree = new TreeView<String>(rootItem);


        leftVBox.getChildren().add(tree);
        rootBorderPane.setLeft(leftVBox);


        // 顶部菜单栏---------------------------------------------------------------------------------
        MenuBar topMenuBar = new MenuBar();
        Menu fileMenu = new Menu("文件(F)");
        Menu editMenu = new Menu("编辑(E)");
        Menu checkMenu = new Menu("查看(V)");
        Menu noteMenu = new Menu("笔记(N)");
        Menu toolsMenu = new Menu("工具(T)");
        Menu helpMenu = new Menu("帮助(H)");
        MenuItem article = new MenuItem("new 笔记");
        MenuItem book = new MenuItem("new 笔记本");
        MenuItem freshen = new MenuItem("刷新");
        fileMenu.getItems().addAll(article, book, freshen);
        topMenuBar.getMenus().addAll(fileMenu, editMenu, checkMenu, noteMenu, toolsMenu, helpMenu);

        rootBorderPane.setTop(topMenuBar);

        // 中部日志内容、CRUD 按钮---------------------------------------------------------------------------------
        Article articleFromDB = ArticleDao.findAll().get(0);

        HBox topHBox = new HBox();
        topHBox.setSpacing(100);
        topHBox.setAlignment(Pos.CENTER);

        // 文章标题
        Text articleNameText = new Text(articleFromDB.getName());
        articleNameText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        topHBox.getChildren().add(articleNameText);

        // 文章内容
        TextArea articleContentTextArea = new TextArea();
        articleContentTextArea.setStyle("-fx-background-color: yellow");
        articleContentTextArea.setText(articleFromDB.getContent());

        BorderPane articleBorderPane = new BorderPane();
        articleBorderPane.setTop(topHBox);

        Button updateButton = new Button("更新");
        Button deleteButton = new Button("删除");
        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(100);
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().addAll(updateButton, deleteButton);
        deleteButton.setStyle("-fx-background-color: #d9534f;");
        updateButton.setStyle("-fx-background-color: #337ab7;");
        articleBorderPane.setBottom(bottomHBox);
        articleBorderPane.setCenter(articleContentTextArea);

        rootBorderPane.setCenter(articleBorderPane);


        // 下部---------------------------------------------------------------------------------------------------
        Label bottomLabel = new Label(
                "                                                                                      " +
                        "CopyRight © 2018 linhongcun 版权所有" +
                        "                                                                                   ");
        bottomLabel.setAlignment(Pos.CENTER);
        bottomLabel.setStyle("-fx-background-color: rgba(179,212,189,0.56);");
        rootBorderPane.setBottom(bottomLabel);


        // --------------------------------------------------------点击事件------------------------------------------------------------
        // 更新文章
        updateButton.setOnAction(event -> CenterController.updateArticleContentByName("Info", articleNameText, articleContentTextArea));
        // 删除文章
        deleteButton.setOnAction(event -> {
            try {
                ArticleDao.deleteArticleByName(articleNameText.getText());
            } catch (Exception e) {
                System.out.println("error");
            }
        });
        // 新增笔记本
        book.setOnAction(event -> newBook("新增笔记本"));
        // 新增笔记
        article.setOnAction(event -> {
            try {
                newArticle("新增笔记");
            } catch (Exception e) {
                System.out.println("error");
            }
        });
        // 刷新目录
        freshen.setOnAction(event -> {
            try {
                TopController.freshen(rootBorderPane, articleContentTextArea, articleNameText);
            } catch (Exception e) {
                System.out.println("error");
            }
        });
        // 选择文章
        tree.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem>) (observable, oldValue, newValue) -> {
            String articleName = (String) newValue.getValue();
            System.out.println(newValue.getValue());
            try {
                Article selectedArticle = ArticleDao.findArticleByName(articleName).get(0);
                articleContentTextArea.setText(selectedArticle.getContent());
                articleNameText.setText(selectedArticle.getName());
            } catch (Exception e) {
                System.out.println("error");
            }
        });

        rootGroup.getChildren().add(rootBorderPane);
        stage.setScene(scene);
        stage.show();

    }

}