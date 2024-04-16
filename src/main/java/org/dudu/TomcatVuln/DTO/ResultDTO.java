package org.dudu.TomcatVuln.DTO;

import lombok.Builder;
import lombok.Data;
import org.dudu.TomcatVuln.Enum.Status;
import org.dudu.TomcatVuln.Enum.VulnName;

@Builder
@Data
public class ResultDTO {
    private VulnName vulnName;

    private Status status;
    /**
     * AJP协议端口
     */
    private String port;

    private String auth;

    private String ip;
    private String path;

}
