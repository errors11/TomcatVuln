package org.dudu.TomcatVuln;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author：DUDU
 * @date: 2023/9/26
 **/
public  class Common {
    public static final String PUTFILENAME="/1092wxa.jsp/";
    public static final String PUTSECCESS= "[++] 检出PUT漏洞，可直接上传webshell!";
    public static final String PUTFAILED= "[-] 不存在PUT上传漏洞";
    public static final String MANAGERNAME= "/manager/html";

    public static final String HEADERCOOKIE="Set-Cookie";

    public static final String regex = "org\\.apache\\.catalina\\.filters\\.CSRF_NONCE=([a-zA-Z0-9]+)";
    public static final String PASSWRODVULN= "Tomcat Web Application Manager";
    public static final ExecutorService POOL = Executors.newFixedThreadPool(5);
    public static final String PASSWOEDUPLOAD="/manager/html/upload?org.apache.catalina.filters.CSRF_NONCE=";

    public static final String BOUNDARY = "WebKitFormBoundaryvfBZf2AGwnF8zkIY";
    public static final String CHARSET = "UTF-8";
    public static final String FileFieldName = "deployWar";
}
