package org.fmemetaj.warehousemanagment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResetPasswordResponse {
    private String message;
    private String error;

    public static ResetPasswordResponse error(String error) {
        var response = new ResetPasswordResponse();
        response.setError(error);

        return response;
    }

    public static ResetPasswordResponse success(String message) {
        var response = new ResetPasswordResponse();
        response.setMessage(message);

        return response;
    }

    public boolean isSuccess() {
        return error == null;
    }
}
