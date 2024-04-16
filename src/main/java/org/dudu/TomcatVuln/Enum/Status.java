package org.dudu.TomcatVuln.Enum;

public enum Status {
    SUCCESS(0,"成功"),
    FAIL(1,"失败");
    private int code;
    private String status;

    Status(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
