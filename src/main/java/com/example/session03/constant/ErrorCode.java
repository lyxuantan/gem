package com.example.session03.constant;

public enum ErrorCode {
    FAIL_UNKNOW("ERR001", "Lỗi hệ thống"),
    NOT_FOUND("ERR002", "Không tìm thấy thông tin"),
    DATE_INVALID("ERR003","Date không hợp lệ");



    ErrorCode(String err002, String s) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
