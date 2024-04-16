package org.dudu.TomcatVuln.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author：DUDU
 * @date: 2023/10/9
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AjpRequestDTO {
    private String url;

    private String uri="/abc";

    private String path="WEB-INF/web.xml";

    private String ip="localhost";

    private String port="8009";

    public AjpRequestDTO(String url,String ip, String port) {
        this.url = url;
        this.ip = ip;
        this.port = port;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
