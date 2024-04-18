package org.dudu.TomcatVuln.utils;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @description:
 * @author：DUDU
 * @date: 2023/9/27
 **/
public class FileUtil {
    public  static SAXReader reader = new SAXReader();

    public  static  String initUsernamePath;

    public  static  String initPasswordPath;

    public static String initShellPath;

    public static String initWarPath;
    public static String getJarPath() throws UnsupportedEncodingException {
        String jarPath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        jarPath = URLDecoder.decode(jarPath, "UTF-8");
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory;
    }

    public static File getConfigFile() throws UnsupportedEncodingException {
        String jarDirectory = getJarPath();
        String configFilePath = jarDirectory + File.separator + "config.xml";
        File configFile = new File(configFilePath);
        return configFile;
    }
    public static String[] parse() throws DocumentException, UnsupportedEncodingException {
        Document document = reader.read(getConfigFile());
        Element filePathElement = document.getRootElement();
        String username = filePathElement.elementText("username");
        String password = filePathElement.elementText("password");
        return new String[]{username,password};
    }
    public static String parseJsp() throws DocumentException, UnsupportedEncodingException {
        Document document = reader.read(getConfigFile());
        Element filePathElement = document.getRootElement();
        String jsp = filePathElement.elementText("jsp");
        return jsp;
    }
    public static String parseWar() throws DocumentException, UnsupportedEncodingException {
        Document document = reader.read(getConfigFile());
        Element filePathElement = document.getRootElement();
        String jsp = filePathElement.elementText("war");
        return jsp;

    }
    public static List<String> readFile(String filepath) throws IOException {
        String str;
        List<String> list = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(filepath));
        while ((str = in.readLine())!=null){
            list.add(str);
        }
        return list;
    }

    public static void writeConfig(String username,String password) throws DocumentException, IOException, URISyntaxException {
        Document document = reader.read(getConfigFile());
        Element rootElement = document.getRootElement();
        Element usernameElement = rootElement.element("username");
        usernameElement.setText(username);
        Element passwordElement = rootElement.element("password");
        passwordElement.setText(password);
        FileWriter fileWriter = new FileWriter(getConfigFile());
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
        xmlWriter.write(document);
        // 关闭 XMLWriter
        xmlWriter.close();
    }
    public static void writeConfigJsp(String shellPath) throws DocumentException, IOException, URISyntaxException {
        Document document = reader.read(getConfigFile());
        Element rootElement = document.getRootElement();
        Element jspElement = rootElement.element("jsp");
        jspElement.setText(shellPath);
        FileWriter fileWriter = new FileWriter(getConfigFile());
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
        xmlWriter.write(document);
        // 关闭 XMLWriter
        xmlWriter.close();
    }
    public static void writeConfigWar(String warPath) throws DocumentException, IOException, URISyntaxException {
        Document document = reader.read(getConfigFile());
        Element rootElement = document.getRootElement();
        Element jspElement = rootElement.element("war");
        jspElement.setText(warPath);
        FileWriter fileWriter = new FileWriter(getConfigFile());
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
        xmlWriter.write(document);
        // 关闭 XMLWriter
        xmlWriter.close();
    }

    public static List<String> getBase64Str() throws IOException, DocumentException {
        List<String> base64 = new ArrayList<>();
        String[] dict = parse();
        List<String> usernames = readFile(dict[0]);
        List<String> passwords = readFile(dict[1]);
        for (String username:usernames){
           for (String password:passwords){
               String content = username+":"+password;
               base64.add(Base64.getEncoder().encodeToString(content.getBytes()));
           }
        }
       return base64;
    }

    public static void resetUandP() throws DocumentException, IOException, URISyntaxException {
        writeConfig(initUsernamePath,initPasswordPath);
    }

    public static void resetJsp() throws DocumentException, IOException, URISyntaxException {
        writeConfigJsp(initShellPath);
    }

    public static void resetWar() throws DocumentException, IOException, URISyntaxException {
        writeConfigWar(initWarPath);
    }

}
