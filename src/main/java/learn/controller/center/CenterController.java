package learn.controller.center;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import learn.dao.ArticleDao;

import java.sql.SQLException;

/**
 * 中右侧栏控制层
 */
public class CenterController {

    /**
     * 更新文章内容处理
     * @param title
     * @param articleNameTextField
     * @param articleContentTextArea
     */
    public static void updateArticleContentByName(String title, Text articleNameTextField, TextArea articleContentTextArea) {

        try {
            ArticleDao.updateArticleByName(articleNameTextField.getText(), articleContentTextArea.getText());
        } catch (Exception e) {
            System.out.println("error");
        }

        // 交互信息
        Stage window = new Stage();
        window.getIcons().add(new Image("http://www.lgstatic.com/i/image/M00/01/92/CgqKkVZv9oyAebyDAACpLbec-1E022.jpg"));
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);
        Button button = new Button("ok");
        button.setOnAction(e -> window.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }
}
