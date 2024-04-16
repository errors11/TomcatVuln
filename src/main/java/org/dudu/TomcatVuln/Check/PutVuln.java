package org.dudu.TomcatVuln.Check;

import lombok.extern.slf4j.Slf4j;
import org.dudu.TomcatVuln.Common;
import org.dudu.TomcatVuln.DTO.PutRequestDTO;
import org.dudu.TomcatVuln.DTO.ResultDTO;
import org.dudu.TomcatVuln.Enum.Status;
import org.dudu.TomcatVuln.Enum.VulnName;
import org.dudu.TomcatVuln.Interface.strategy.ExecuteStrategy;
import org.dudu.TomcatVuln.request.HttpRequest;
import org.dudu.TomcatVuln.utils.FileUtil;
import org.dudu.TomcatVuln.view.MainMenuView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 测试put上传漏洞
 * @author：DUDU
 * @date: 2023/9/26
 **/


public class PutVuln extends HttpRequest<PutRequestDTO, Integer> implements ExecuteStrategy<String>{

    @Override
    public String mark() {
        return "put";
    }

    @Override
    protected Integer request(PutRequestDTO request){
        try {
            this.setConnection(request.getUrl());
            connection.setRequestMethod(request.getMethod());
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String execute(String data) {
        String url = data+Common.PUTFILENAME;
        ResultDTO resultDTO;
        List<ResultDTO> resultDTOList = MainMenuView.ResultMap.getOrDefault(data,new ArrayList<>());
        int firstCode = request(new PutRequestDTO(url,"PUT"));
        int secondCode = request(new PutRequestDTO(url.substring(0,url.lastIndexOf("/")),"GET"));
        if((firstCode==201 || firstCode==204) && secondCode==200){
            resultDTO = ResultDTO.builder()
                            .vulnName(VulnName.PutVuln)
                            .status(Status.SUCCESS).build();
            resultDTOList.add(resultDTO);
            MainMenuView.ResultMap.put(data,resultDTOList);
            return Common.PUTSECCESS;
        }else {
            resultDTO = ResultDTO.builder()
                    .vulnName(VulnName.PutVuln)
                    .status(Status.FAIL).build();
            resultDTOList.add(resultDTO);
            MainMenuView.ResultMap.put(data,resultDTOList);
            return Common.PUTFAILED;
        }
    }
}
