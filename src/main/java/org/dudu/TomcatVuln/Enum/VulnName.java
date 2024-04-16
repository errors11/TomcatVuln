package org.dudu.TomcatVuln.Enum;

public enum VulnName{
    PutVuln(1,"Put文件上传漏洞"),
    PasswordVuln(2,"弱口令漏洞"),
    AJPVuln(3,"AJP协议漏洞");
    private  int code;
    private String info;

    VulnName(int code, String info) {
        this.code = code;
        this.info = info;
    }
    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
    public static VulnName findByInfo(String info) {
        for (VulnName vulnName : VulnName.values()) {
            if (vulnName.getInfo().equals(info)) {
                return vulnName;
            }
        }
        throw new IllegalArgumentException("No matching vulnName for info: " + info);
    }
}
