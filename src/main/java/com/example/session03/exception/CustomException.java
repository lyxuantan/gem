package com.example.session03.exception;

import com.example.session03.constant.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private String code;
    private String desc;
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Exception exception) {
        super(message, exception);
    }

    public CustomException(ErrorCode error) {
        this.code = error.getCode();
        this.desc = error.getDesc();
    }


}
