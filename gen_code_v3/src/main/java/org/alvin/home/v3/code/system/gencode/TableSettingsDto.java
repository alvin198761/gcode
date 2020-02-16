package org.alvin.home.v3.code.system.gencode;

import lombok.Data;
import org.alvin.home.v3.code.beans.TableBean;
import org.alvin.home.v3.code.system.gencoderef.AlvinGenCodeRef;

import java.util.List;

@Data
public class TableSettingsDto extends TableBean {

    private List<AlvinGenCodeRef> fields;
}
