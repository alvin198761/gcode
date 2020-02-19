package org.alvin.code.gen.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveKeyObj<T> {

    private Number key;
    private int res;
}
