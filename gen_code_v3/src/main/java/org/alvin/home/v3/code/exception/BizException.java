package org.alvin.home.v3.code.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常跳转。
 *
 * @author daoshenzzg@163.com
 * @date 2019-09-09 14:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String errMsg;

    public BizException(int code, String errMsg) {
        super(errMsg);
        this.code = code;
    }


}