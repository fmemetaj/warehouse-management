package org.fmemetaj.warehousemanagment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Result<T>(T result, Result.Code code) {

    public static <T> Result<T> successful(T result) {
        return new Result<>(result, Result.Code.SUCCESS);
    }

    public static <T> Result<T> error(Result.Code code) {
        return new Result<>(null, code);
    }

    public enum Code {
        SUCCESS,
        ORDER_ITEM_LIST_EMPTY,
        USER_NOT_FOUND,
        NOT_A_MEMBER,
        ROLE_NOT_FOUND,
        INVALID_ROLE_NAME;

        public HttpStatus toHttpStatus() {
            return switch (this) {
//                case "INVITATION_EXPIRED" -> HttpStatus.GONE;
                case NOT_A_MEMBER -> HttpStatus.UNAUTHORIZED;
//                case "INVITATION_REVOKED" -> HttpStatus.LOCKED;
//                case "MEMBER_ALREADY_EXISTS, MEMBER_ALREADY_INVITED, INVITE_ALREADY_ACCEPTED"
//                        -> HttpStatus.CONFLICT;
                case USER_NOT_FOUND, ROLE_NOT_FOUND -> HttpStatus.NOT_FOUND;
                default -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }
    }
}
