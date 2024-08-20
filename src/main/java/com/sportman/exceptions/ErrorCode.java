package com.sportman.exceptions;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {

    FALLBACK_EXC(1000, "UnCatchException", 500),
    UN_AUTHENTICATED(1001, "unAuthenticated", 401),
    UN_AUTHORIZED(1002, "unAuthorized", 403),
    CLUB_EXISTED(1003, "Club has already existed", 400),
    CLUB_NOT_FOUND(1003, "Club not found", 404),
    BAD_REQUEST(1004, "Bad request", 400),
    CLUB_NAME_NOT_NULL(1005, "Club name must not be null", 400),
    CLUB_COLOR_HEX_NOT_NULL(1005, "Club color hex must not be null", 400),
    CLUB_SHORT_NAME_NOT_NULL(1006, "Club short name must not be null", 400),
    CLUB_COLOR_HEX_OVER_BOUNCE(1007, "Club color hex must be 7 characters", 400),
    CLUB_SHORT_NAME_OVER_BOUNCE(1007, "Club color hex must be 3 characters", 400)
    ;

    int code;
    String msg;
    int statusCode;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}
