package org.fmemetaj.warehousemanagment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Result<T>(T result, Result.Code code) {

    public static <T> Result<T> successful(T result) {
        return new Result<>(result, Result.Code.SUCCESS);
    }

    public static <T> Result<T> error(Result.Code code) {
        return new Result<>(null, code);
    }

    public static <T> ResponseEntity<Result<T>> response(Result<T> result) {
        if (result.code() != Result.Code.SUCCESS) {
            var httpStatus = result.code().toHttpStatus();
            log.info("Error: {}", result.code());

            return ResponseEntity.status(httpStatus).body(result);
        }

        return ResponseEntity.ok(result);
    }

    public boolean isSuccessful() {
        return code == Result.Code.SUCCESS;
    }

    public boolean isFailure() {
        return !isSuccessful();
    }

    public T getData() {
        return result;
    }

    public Result.Code getErrorCode() {
        return code;
    }

    public enum Code {
        SUCCESS,
        ORDER_ITEM_LIST_EMPTY,
        ORDER_NOT_FOUND,
        ORDER_CANNOT_BE_UPDATED,
        ORDER_ITEM_NOT_FOUND,
        USER_ORDER_NOT_FOUND,
        ORDER_IS_NOT_AWAITING_APPROVAL,
        INVENTORY_ITEM_NOT_FOUND,
        DELIVERY_CANNOT_BE_SCHEDULED_ON_SUNDAY,
        ORDER_NOT_APPROVED,
        TRUCKS_NOT_FOUND,
        TRUCK_NOT_FOUND,
        TRUCK_NOT_AVAILABLE,
        EXCEEDS_TRUCK_CAPACITY,
        INSUFFICIENT_INVENTORY,
        DELIVERY_NOT_FOUND,
        ORDER_CANNOT_BE_COMPLETED;

        public HttpStatus toHttpStatus() {
            return switch (this) {
                case TRUCK_NOT_AVAILABLE -> HttpStatus.GONE;
                case ORDER_IS_NOT_AWAITING_APPROVAL, ORDER_NOT_APPROVED -> HttpStatus.UNAUTHORIZED;
//                case "INVITATION_REVOKED" -> HttpStatus.LOCKED;
                case DELIVERY_CANNOT_BE_SCHEDULED_ON_SUNDAY, EXCEEDS_TRUCK_CAPACITY,
                     INSUFFICIENT_INVENTORY, ORDER_CANNOT_BE_COMPLETED -> HttpStatus.CONFLICT;
                case ORDER_ITEM_LIST_EMPTY, ORDER_NOT_FOUND, ORDER_ITEM_NOT_FOUND,
                     USER_ORDER_NOT_FOUND, INVENTORY_ITEM_NOT_FOUND, TRUCKS_NOT_FOUND,
                     TRUCK_NOT_FOUND, DELIVERY_NOT_FOUND-> HttpStatus.NOT_FOUND;
                default -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }
    }
}
