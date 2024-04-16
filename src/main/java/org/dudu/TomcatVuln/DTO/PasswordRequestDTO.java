package org.dudu.TomcatVuln.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Proxy;

/**
 * @description:  弱口令漏洞请求dto
 * @author：DUDU
 * @date: 2023/9/27
 **/
@Data
@AllArgsConstructor
public class PasswordRequestDTO {
    private String url;
    private String base64_content;
}
