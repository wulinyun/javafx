package learn.controller.top;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import learn.dao.ArticleDao;
import learn.dao.BookDao;
import learn.entity.Article;
import learn.entity.Book;

import java.sql.SQLException;
import java.util.List;

/**
 * 顶部控制层
 */
public class TopController {

    /**
     * 新增笔记本
     * @param title
     */
    public static void newBook(String title) {
        Stage window = new Stage();
        window.getIcons().add(new Image("http://www.lgstatic.com/i/image/M00/01/92/CgqKkVZv9oyAebyDAACpLbec-1E022.jpg"));
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        TextField bookName = new TextField();
        Button saveButton = new Button("保存");
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(e -> window.close());
        saveButton.setOnAction(e -> {
            try {
                BookDao.save(bookName.getText());
                window.close();
            } catch (Exception e1) {
                System.out.println("error");
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(bookName, saveButton, cancelButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    /**
     * 新增文章处理
     * @param title
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void newArticle(String title) throws SQLException, ClassNotFoundException {
        Stage window = new Stage();
        window.getIcons().add(new Image("http://www.lgstatic.com/i/image/M00/01/92/CgqKkVZv9oyAebyDAACpLbec-1E022.jpg"));
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        TextField articleName = new TextField();
        Label nameLabel = new Label("标题");
        nameLabel.setLabelFor(articleName);

        TextArea textArea = new TextArea();
        Label contentLabel = new Label("内容");
        contentLabel.setLabelFor(articleName);

        ComboBox<String> bookId = new ComboBox<String>();
        List<Book> bookList = BookDao.findAll();
        bookId.setValue(bookList.get(0).getName());
        for (Book book : bookList) {
            bookId.getItems().add(book.getName());
        }
        HBox buttonHBox = new HBox();
        // 居中
        buttonHBox.setSpacing(100);
        buttonHBox.setAlignment(Pos.CENTER);
        Button saveButton = new Button("保存");
        saveButton.setStyle("-fx-background-color: #5cb85c;");
        Button cancelButton = new Button("取消");
        cancelButton.setStyle("-fx-background-color: #d9534f;");
        buttonHBox.getChildren().addAll(saveButton, cancelButton);

        cancelButton.setOnAction(e -> window.close());
        saveButton.setOnAction(e -> {
            try {
                List<Book> bookByName = BookDao.findBookByName(bookId.getValue());
                ArticleDao.save(articleName.getText(), textArea.getText(), bookByName.get(0).getId());
                window.close();
            } catch (Exception e1) {
                System.out.println("error");
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameLabel, articleName, contentLabel, textArea, bookId, buttonHBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    /**
     * 刷新笔记目录
     * @param rootBorderPane
     * @param articleContentTextArea
     * @param articleNameText
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void freshen(BorderPane rootBorderPane, TextArea articleContentTextArea, Text articleNameText) throws SQLException, ClassNotFoundException {
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
        TreeView<String> newTree = new TreeView<String>(rootItem);
        leftVBox.getChildren().add(newTree);

        // 选择文章
        newTree.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem>) (observable, oldValue, newValue) -> {
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

        rootBorderPane.setLeft(leftVBox);
        System.out.println("刷新成功");
    }

}
