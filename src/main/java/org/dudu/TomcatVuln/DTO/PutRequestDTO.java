package org.dudu.TomcatVuln.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:  put漏洞请求dto
 * @author：DUDU
 * @date: 2023/9/26
 **/
@Data
@AllArgsConstructor
public class PutRequestDTO {
    String url;
    String method;
}
