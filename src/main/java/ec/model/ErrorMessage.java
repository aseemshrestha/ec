package ec.model;

import lombok.Data;

@Data
public class ErrorMessage {
    private String message;
    private String errorCode;

    public ErrorMessage(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }


}
