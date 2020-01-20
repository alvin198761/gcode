package org.alvin.home.v3.code.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestApiResult<T> {

    private int code;
    private String errmsg;
    private T data;

    public RestApiResult(T data) {
        this.code = 0;
    }

    public RestApiResult(int code, String errmsg) {
        this.code = code;
        this.errmsg = errmsg;
    }
}
