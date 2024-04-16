package org.dudu.TomcatVuln.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dom4j.DocumentException;
import org.dudu.TomcatVuln.utils.FileUtil;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
public class SetJspView implements Initializable {
    public TextField shellPath;

    public Button reset;
    public void setJspPath(ActionEvent actionEvent) {
        final String[] file = {null};
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSP files (*.jsp)", "*.jsp");
        fileChooser.getExtensionFilters().add(extFilter);
        file[0] = String.valueOf(fileChooser.showOpenDialog(primaryStage));
        this.shellPath.setText(file[0]);
    }

    public void save(ActionEvent actionEvent) throws DocumentException, IOException, URISyntaxException {
        FileUtil.writeConfigJsp(this.shellPath.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set("脚本设置保存成功!");
        alert.showAndWait();
    }

    public void reset(ActionEvent actionEvent) throws DocumentException, IOException, URISyntaxException {
        FileUtil.resetJsp();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set("重置成功!");
        alert.showAndWait();
        Stage stage = (Stage) this.reset.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String jspPath = FileUtil.parseJsp();
            this.shellPath.setText(jspPath);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
