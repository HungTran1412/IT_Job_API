package backend.main.exception;

import backend.main.enums.Code;

public class AppException extends RuntimeException
{
    private Code code;

    public AppException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    public Code getErrorCode() {
        return code;
    }

    public void setErrorCode(Code code) {
        this.code = code;
    }
}
