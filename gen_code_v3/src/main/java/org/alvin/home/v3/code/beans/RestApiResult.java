package org.alvin.home.v3.code.beans;

import lombok.Data;

@Data
public class RestApiResult<T> {

    private int code;
    private String errmsg;
    private T data;

    public RestApiResult(T data) {
        this.code = 0;
        this.data = data;
    }

    public RestApiResult(int code, String errmsg) {
        this.code = code;
        this.errmsg = errmsg;
    }
}
