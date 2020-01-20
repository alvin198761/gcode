package org.alvin.home.v3.code.system.gencode;

import org.alvin.home.v3.code.beans.TableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 唐植超
 * @date 2020/01/20
 */
@RestController
@RequestMapping("/api/code")
public class GenCodeController {
    @Autowired
    private GenCodeServiceV3 genCodeServiceV3;

    @PostMapping("queryTables")
    public List<TableBean> queryTables() {
        return this.genCodeServiceV3.queryTables();
    }

    //https://blog.csdn.net/xiaoxiaovbb/article/details/81193595
}
