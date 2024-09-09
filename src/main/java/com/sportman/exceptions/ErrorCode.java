package com.sportman.exceptions;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ErrorCode {

    FALLBACK_EXC(1000, "UnCatchException", 500),

    //authentication & authorization
    UN_AUTHENTICATED(1001, "unAuthenticated", 401),
    UN_AUTHORIZED(1002, "unAuthorized", 403),
    BAD_REQUEST(1004, "Bad request", 400),
    WRONG_TOKEN(1022, "Token type invalid", 400),
    TOKEN_NOT_EMPTY(1023, "Token must not be empty", 400),

    //club
    CLUB_EXISTED(1003, "Club has already existed", 400),
    CLUB_NOT_FOUND(1003, "Club not found", 404),
    CLUB_NAME_NOT_NULL(1005, "Club name must not be empty", 400),
    CLUB_COLOR_HEX_NOT_NULL(1005, "Club color hex must not be null", 400),
    CLUB_SHORT_NAME_NOT_NULL(1006, "Club short name must not be null", 400),
    CLUB_COLOR_HEX_OVER_BOUNCE(1007, "Club color hex must be 7 characters", 400),
    CLUB_SHORT_NAME_OVER_BOUNCE(1007, "Club color hex must be 3 characters", 400),

    //season
    SEASON_NOT_FOUND(1008, "Season not found", 404),
    SEASON_YEAR_START_NOT_NULL(1009, "Season year start must not be null", 400),
    SEASON_END_START_NOT_NULL(1010, "Season year end must not be null", 400),
    SEASON_YEAR_INVALID(1011, "Season year is invalid", 400),
    SEASON_EXISTED(1012, "Season has already existed", 400),


    //user
    USER_EXISTED(1013, "Username or Email has already existed", 400),
    USER_NOT_FOUND(1014, "Username or password is not correct", 404),
    PASSWORD_INVALID(1015, "Password must have at least 6 characters", 400),
    EMAIL_INVALID(1015, "Email is invalid", 400),
    USERNAME_INVALID(1019, "Username must have at least 8 characters", 400),
    USERNAME_NOT_EMPTY(1020, "Username must not be empty", 400),
    PASSWORD_NOT_EMPTY(1020, "Password must not be empty", 400),
    USER_INVALID(1021, "Username or password is not correct", 400),


    //role
    ROLE_EXISTED(1016, "Role has already existed", 400),
    ROLE_NOT_FOUND(1017, "Role not found", 404),
    ROLE_INVALID(1018, "Role is invalid", 400),

    //permission
    PERMISSION_EXISTED(1016, "Permission has already existed", 400),
    PERMISSION_EXISTED_NOT_FOUND(1017, "Permission not found", 404),
    PERMISSION_EXISTED_INVALID(1018, "Permission is invalid", 400),
    PER_NAME_NOT_EMPTY(1018, "Permission's name must not be empty", 400),
    PER_DESC_NOT_EMPTY(1018, "Permission's desc must not be empty", 400),


    //size
    SIZE_EXISTED(1016, "Size has already existed", 400),
    SIZE_NOT_FOUND(1017, "Size not found", 404),
    SIZE_NOT_EMPTY(1018, "Size must not be empty", 400),
    SIZE_OVER_LENGTH(1018, "Size must have between 1 and 10 characters", 400),

    //color
    COLOR_EXISTED(1016, "Color has already existed", 400),
    COLOR_NOT_FOUND(1017, "Color not found", 404),
    COLOR_NOT_EMPTY(1018, "Color must not be empty", 400),
    COLOR_OVER_BOUNCE(1018, "Color must have 7 characters", 400),

    //product
    PRODUCT_EXISTED(1016, "Product has already existed", 400),
    PRODUCT_NOT_FOUND(1017, "Product not found", 404),
    PRODUCT_NAME_NOT_EMPTY(1018, "Product name must not be empty", 400),
    PRODUCT_PRICE_NOT_EMPTY(1018, "Product price must not be empty", 400),
    PRODUCT_IMAGE_NOT_EMPTY(1018, "Product image must not be empty", 400),
    PRODUCT_DESC_NOT_EMPTY(1018, "Product description must not be empty", 400),
    PRODUCT_NOT_EMPTY(1018, "Product must not be empty", 400),
    PRODUCT_PRICE_INVALID(1022, "Price must be at least 50,000", 400),
    PRODUCT_SEASON_INVALID(1023, "Season must have at least 2 years", 400),
    PRODUCT_STOCK_INVALID(1024, "Product stock must larger than 1 and smaller than 100", 400),
    PRODUCT_STOCK_EMPTY(1025, "Product stock must not be empty", 400),
    PRODUCT_SIZE_EMPTY(1026, "Product size must not be empty", 400),
    PRODUCT_INVALID_FILE(1027, "Product must have 2 images", 400),
    PRODUCT_STOCK_NOT_EMPTY(1028, "Product stock must not be empty", 400),
    PRODUCT_ID_NOT_EMPTY(1030, "Product id must not be empty", 400),

    //rate
    RATE_NUMBER_INVALID(1029, "Rate must be >= 1 or <= 5", 400),
    RATE_COMMENT_NOT_EMPTY(1031, "Comment must not be empty", 400),

    //cart
    CART_NOT_FOUND(1032, "Cart not found", 404),
//    RATE_COMMENT_NOT_EMPTY(1031, "Comment must not be empty", 400),

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
