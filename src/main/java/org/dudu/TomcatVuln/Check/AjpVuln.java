package org.dudu.TomcatVuln.Check;

import org.dudu.TomcatVuln.DTO.AjpRequestDTO;
import org.dudu.TomcatVuln.DTO.ResultDTO;
import org.dudu.TomcatVuln.Enum.Status;
import org.dudu.TomcatVuln.Enum.VulnName;
import org.dudu.TomcatVuln.Interface.strategy.ExecuteStrategy;
import org.dudu.TomcatVuln.request.AjpRequest;
import org.dudu.TomcatVuln.view.MainMenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author：DUDU
 * @date: 2023/10/9
 **/
public class AjpVuln extends AjpRequest implements ExecuteStrategy<AjpRequestDTO>{
    @Override
    public String mark() {
        return "ajp";
    }

    @Override
    public String execute(AjpRequestDTO data)  {
        String result = null;
        ResultDTO resultDTO;
        List<ResultDTO> resultDTOList = MainMenuView.ResultMap.getOrDefault(data.getUrl(),new ArrayList<>());
        try {
            String response = request_string(data);
            if(response.contains("500")){
                resultDTO = ResultDTO.builder().vulnName(VulnName.AJPVuln).status(Status.FAIL).build();
                resultDTOList.add(resultDTO);
                MainMenuView.ResultMap.put(data.getUrl(),resultDTOList);
                result="文件路径不存在";
            }else{
                resultDTO = ResultDTO.builder().vulnName(VulnName.AJPVuln).status(Status.SUCCESS).ip(data.getIp())
                        .port(data.getPort()).path(data.getPath()).build();
                resultDTOList.add(resultDTO);
                MainMenuView.ResultMap.put(data.getUrl(),resultDTOList);
                result="AJP漏洞存在-----文件内容如下:\n"+response;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
