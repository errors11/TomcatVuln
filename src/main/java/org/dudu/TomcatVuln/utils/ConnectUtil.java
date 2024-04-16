package org.dudu.TomcatVuln.utils;


import org.dudu.TomcatVuln.Common;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author：DUDU
 * @date: 2023/10/20
 **/
public class ConnectUtil {
    public static String getStandardUrl(String url) throws MalformedURLException {
        URL Url = new URL(url);
        int port = 80;
        if(Url.getPort()!=-1) port=Url.getPort();
        return Url.getProtocol()+"://"+Url.getHost()+":"+port;
    }
    public static String getHost(String url) throws MalformedURLException {
        URL Url = new URL(url);
        return  Url.getHost();
    }

    public static boolean testConnect(String url) {
        try {
            String standardUrl = getStandardUrl(url);
            URL Url = new URL(standardUrl);
            HttpURLConnection con = (HttpURLConnection) Url.openConnection();
            con.connect();
        } catch (IOException e) {
            return  false;
        }
        return true;
    }

    public static String getResponseContent(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    public static String matcher(String response){
        Pattern pattern = Pattern.compile(Common.regex);
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            return matcher.group(1);
        }else {
            throw new RuntimeException("未找到 CSRF_NONCE 值");
        }
    }


    public static int uploadFile(HttpURLConnection connection,String filename,String filepath) {
        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, Common.CHARSET), true)) {
            // Send binary file.
            writer.append("--" + Common.BOUNDARY).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"" + Common.FileFieldName + "\"; filename=\"" + filename + "\"").append("\r\n");
            writer.append("Content-Type: application/octet-stream").append("\r\n");
            writer.append("\r\n").flush();
            try (FileInputStream fileInputStream = new FileInputStream(filepath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush();
            }
            writer.append("\r\n").flush();
            writer.append("--" + Common.BOUNDARY + "--").append("\r\n").flush();
            writer.close();
            return connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int PutUploadFile(HttpURLConnection connection, String filepath) throws ProtocolException {
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            File file = new File(filepath);
            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
            }
            byte[] contentBytes = fileContent.toString().getBytes("UTF-8");
            os.write(contentBytes);
            os.flush();
            System.out.println(connection.getResponseCode());
            return connection.getResponseCode();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
