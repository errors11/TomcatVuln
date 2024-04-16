package org.dudu.TomcatVuln.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dom4j.DocumentException;
import org.dudu.TomcatVuln.utils.FileUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetDictView implements Initializable{

    public TextField usernameFilePath;
    public TextField passwordFilePath;


    public Button reset;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String[] filepath = FileUtil.parse();
            this.usernameFilePath.setText(filepath[0]);
            this.passwordFilePath.setText(filepath[1]);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
    public void setUsernameDict(ActionEvent actionEvent) {
        final String[] file = {null};
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        file[0] = String.valueOf(fileChooser.showOpenDialog(primaryStage));
        this.usernameFilePath.setText(file[0]);
    }
    public void setPasswordDict(ActionEvent actionEvent) {
        final String[] file = {null};
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        file[0] = String.valueOf(fileChooser.showOpenDialog(primaryStage));
        this.passwordFilePath.setText(file[0]);
    }

    public void save(ActionEvent actionEvent) throws DocumentException, IOException, URISyntaxException {
        FileUtil.writeConfig(this.usernameFilePath.getText(),this.passwordFilePath.getText());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set("字典设置保存成功!");
        alert.showAndWait();
    }
    public void reset(ActionEvent actionEvent) throws DocumentException, IOException, URISyntaxException {
        FileUtil.resetUandP();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("信息");
        alert.headerTextProperty().set("重置成功!");
        alert.showAndWait();
        Stage stage = (Stage) this.reset.getScene().getWindow();
        stage.close();
    }


}
