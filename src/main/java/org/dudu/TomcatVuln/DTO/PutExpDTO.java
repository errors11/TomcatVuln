package org.dudu.TomcatVuln.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PutExpDTO {
    private String url;
    private String filename;
    private String filePath;
}
