package org.alvin.home.v3.code.system.gencode;

import org.alvin.home.v3.code.beans.RestApiResult;
import org.alvin.home.v3.code.beans.TableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

    /**
     * 查询所有的表
     *
     * @return
     */
    @PostMapping("queryTables")
    public RestApiResult<List<TableBean>> queryTables() {
        return new RestApiResult<List<TableBean>>(this.genCodeServiceV3.queryTables());
    }

    /**
     * 根据选择的条件生成文件
     *
     * @param codeCond
     * @return
     */
    @PostMapping("gencode")
    public RestApiResult<String> gencode(@RequestBody GenCodeCond codeCond) throws IOException {
        Assert.notNull(codeCond.getAuthor(), "作者不能为空");
        Assert.notNull(codeCond.getPackageName(), "包名不能为空");
        Assert.isTrue(codeCond.getTables().isEmpty(), "请选择要生成的表");
        Assert.notNull(codeCond.getTemplateDirs(), "请选择要模板");
        return new RestApiResult<String>(this.genCodeServiceV3.gencode(codeCond));
    }

    //https://blog.csdn.net/xiaoxiaovbb/article/details/81193595
}
