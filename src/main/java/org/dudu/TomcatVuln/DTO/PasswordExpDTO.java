package org.dudu.TomcatVuln.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PasswordExpDTO {
    private String url;
    private String base64_content;
    private String csrfNonceValue;
    private String cookie;
    private String filename;
    private String filePath;
}
