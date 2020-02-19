package org.alvin.springjdbc.system.maintable;

/**
* 类说明: 主表--Swagger控制器类 ,使用requestbody 实现
* @author 唐植超
* 生成日期 2020-02-19 23:05:35
**/
@lombok.extern.slf4j.Slf4j
@io.swagger.annotations.Api(value = "[主表]控制器", description = "[主表]相关操作")
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/api/mainTable")
public class MainTableAction {

    @org.springframework.beans.factory.annotation.Autowired
    private MainTableService mainTableService; //注入[主表]业务逻辑类

    /**
     * 方法说明： 新增[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "新增[主表]", notes = "返回影响记录行数")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTable", name = "mainTable", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("save")
    public org.alvin.code.gen.beans.RestfullResp<Integer> save(@org.springframework.web.bind.annotation.RequestBody MainTable mainTable) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.save(mainTable));
    }


    /**
     * 方法说明： 删除[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "删除[主表]", notes = "返回影响记录行数")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[主表]的主键", required = true, dataType = "array", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("delete")
    public org.alvin.code.gen.beans.RestfullResp<Integer> delete(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.delete(id));
    }


    /**
     * 方法说明： 修改[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "修改[主表]", notes = "返回影响记录行数,(必须传入完整对象,否则会有数据丢失)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTable", name = "mainTable", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("update")
    public org.alvin.code.gen.beans.RestfullResp<Integer> update(@org.springframework.web.bind.annotation.RequestBody MainTable mainTable) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.update(mainTable));
    }

    /**
     * 方法说明： 修改[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "修改[主表]", notes = "返回影响记录行数(传入需要修改的属性和主键即可,其他可以为空)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTable", name = "mainTable", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("updateNotNull")
    public org.alvin.code.gen.beans.RestfullResp<Integer> updateNotNull(@org.springframework.web.bind.annotation.RequestBody MainTable mainTable) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.updateNotNull(mainTable));
    }

    /**
     * 方法说明： 按条件查询分页[主表]列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询分页[主表]列表", notes = "返回分页[主表]列表")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableCond", name = "cond", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryPage")
    public org.alvin.code.gen.beans.RestfullResp<org.alvin.code.gen.utils.Page<MainTable>> queryPage(@org.springframework.web.bind.annotation.RequestBody MainTableCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryPage(cond));
    }

    /**
     * 方法说明： 按条件查询不分页[主表]列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询不分页[主表]列表", notes = "返回不分页[主表]列表")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableCond", name = "cond", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<MainTable>> queryList(@org.springframework.web.bind.annotation.RequestBody MainTableCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryList(cond));
    }

    /**
     * 方法说明： 按条件查询一个 主表 对象
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 主表 对象", notes = "返回 主表对象")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableCond", name = "cond", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("findOne")
    public org.alvin.code.gen.beans.RestfullResp<MainTable> findOne(@org.springframework.web.bind.annotation.RequestBody MainTableCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.findOne(cond));
    }

    /**
     * 方法说明： 按条件查询[主表]记录个数
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询[主表]记录个数", notes = "返回记录个数")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableCond", name = "cond", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryCount")
    public org.alvin.code.gen.beans.RestfullResp<Long> queryCount(@org.springframework.web.bind.annotation.RequestBody MainTableCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryCount(cond));
    }

    /**
     * 方法说明： 按主键查询单条[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "按主键查询单个[主表]记录", notes = "返回单个[主表]对象")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[主表]的主键", required = true, dataType = "Long", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("findById")
    public org.alvin.code.gen.beans.RestfullResp<MainTable> findById(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.findById(id));
    }

    /**
     * 方法说明： 按主键查询单条[主表]记录
     */
    @io.swagger.annotations.ApiOperation(value = "按主键查询单个[主表]记录(带标签)", notes = "返回单个[主表]对象(带标签)")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[主表]的主键", required = true, dataType = "Long", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("findFkById")
    public org.alvin.code.gen.beans.RestfullResp<MainTableFk> findFkById(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return  new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.findFkById(id));
    }

    /**
     * 方法说明： 按条件查询一个 主表 对象
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 主表 对象(带外键标签)", notes = "返回 主表对象 (带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableFkCond", name = "{table.varName}Cond", value = "主表(外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("findFkOne")
    public org.alvin.code.gen.beans.RestfullResp<MainTableFk> findFkOne(MainTableFkCond mainTableCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.findFkOne(mainTableCond));
    }

    /**
     * 方法说明: 只显示标签和id的列表
     *
     * @param mainTableFkCond
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 主表 对象(只显示标签和id的列表)", notes = "返回 主表对象 (只显示标签和id的列表)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableFkCond", name = "mainTableFkCond", value = "主表", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryLabelList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<org.alvin.code.gen.beans.SelectOption>> queryLabelList(MainTableFkCond mainTableFkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryLabelList(mainTableFkCond));
    }

    /**
     * 方法说明：按条件查询不分页主表列表 (带关系表标签)
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询不分页[主表]列表 (带外键标签)", notes = "返回不分页[主表]列表 (带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableFkCond", name = "mainTableFkCond", value = "主表 (外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryFkList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<MainTableFk>> queryFkList(MainTableFkCond mainTableFkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryFkList(mainTableFkCond));
    }

    /**
     * 方法说明：按条件查询分页主表列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询分页[主表]列表(带外键标签)", notes = "返回分页[主表]列表(带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "MainTableFkCond", name = "fkCond", value = "主表(外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryFkPage")
    public org.alvin.code.gen.beans.RestfullResp<org.alvin.code.gen.utils.Page<MainTableFk>> queryFkPage(MainTableFkCond fkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.mainTableService.queryFkPage(fkCond));
    }

}

