package org.alvin.springjdbc.system.address;

/**
* 类说明: 收寄信息--Swagger控制器类 ,使用requestbody 实现
* @author 唐植超
* 生成日期 2020-02-17 22:40:15
**/
@lombok.extern.slf4j.Slf4j
@io.swagger.annotations.Api(value = "[收寄信息]控制器", description = "[收寄信息]相关操作")
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/api/address")
public class AddressAction {

    @org.springframework.beans.factory.annotation.Autowired
    private AddressService addressService; //注入[收寄信息]业务逻辑类

    /**
     * 方法说明： 新增[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "新增[收寄信息]", notes = "返回影响记录行数")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "Address", name = "address", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("save")
    public org.alvin.code.gen.beans.RestfullResp<Integer> save(@org.springframework.web.bind.annotation.RequestBody Address address) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.save(address));
    }


    /**
     * 方法说明： 删除[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "删除[收寄信息]", notes = "返回影响记录行数")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[收寄信息]的主键", required = true, dataType = "array", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("delete")
    public org.alvin.code.gen.beans.RestfullResp<Integer> delete(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.delete(id));
    }


    /**
     * 方法说明： 修改[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "修改[收寄信息]", notes = "返回影响记录行数,(必须传入完整对象,否则会有数据丢失)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "Address", name = "address", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("update")
    public org.alvin.code.gen.beans.RestfullResp<Integer> update(@org.springframework.web.bind.annotation.RequestBody Address address) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.update(address));
    }

    /**
     * 方法说明： 修改[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "修改[收寄信息]", notes = "返回影响记录行数(传入需要修改的属性和主键即可,其他可以为空)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "Address", name = "address", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("updateNotNull")
    public org.alvin.code.gen.beans.RestfullResp<Integer> updateNotNull(@org.springframework.web.bind.annotation.RequestBody Address address) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.updateNotNull(address));
    }

    /**
     * 方法说明： 按条件查询分页[收寄信息]列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询分页[收寄信息]列表", notes = "返回分页[收寄信息]列表")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressCond", name = "cond", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryPage")
    public org.alvin.code.gen.beans.RestfullResp<org.alvin.code.gen.utils.Page<Address>> queryPage(@org.springframework.web.bind.annotation.RequestBody AddressCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryPage(cond));
    }

    /**
     * 方法说明： 按条件查询不分页[收寄信息]列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询不分页[收寄信息]列表", notes = "返回不分页[收寄信息]列表")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressCond", name = "cond", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<Address>> queryList(@org.springframework.web.bind.annotation.RequestBody AddressCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryList(cond));
    }

    /**
     * 方法说明： 按条件查询一个 收寄信息 对象
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 收寄信息 对象", notes = "返回 收寄信息对象")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressCond", name = "cond", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("findOne")
    public org.alvin.code.gen.beans.RestfullResp<Address> findOne(@org.springframework.web.bind.annotation.RequestBody AddressCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.findOne(cond));
    }

    /**
     * 方法说明： 按条件查询[收寄信息]记录个数
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询[收寄信息]记录个数", notes = "返回记录个数")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressCond", name = "cond", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryCount")
    public org.alvin.code.gen.beans.RestfullResp<Long> queryCount(@org.springframework.web.bind.annotation.RequestBody AddressCond cond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryCount(cond));
    }

    /**
     * 方法说明： 按主键查询单条[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "按主键查询单个[收寄信息]记录", notes = "返回单个[收寄信息]对象")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[收寄信息]的主键", required = true, dataType = "Long", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("findById")
    public org.alvin.code.gen.beans.RestfullResp<Address> findById(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.findById(id));
    }

    /**
     * 方法说明： 按主键查询单条[收寄信息]记录
     */
    @io.swagger.annotations.ApiOperation(value = "按主键查询单个[收寄信息]记录(带标签)", notes = "返回单个[收寄信息]对象(带标签)")
    @io.swagger.annotations.ApiImplicitParams({@io.swagger.annotations.ApiImplicitParam(name = "id", value = "[收寄信息]的主键", required = true, dataType = "Long", paramType = "query")})
    @org.springframework.web.bind.annotation.PostMapping("findFkById")
    public org.alvin.code.gen.beans.RestfullResp<AddressFk> findFkById(@org.springframework.web.bind.annotation.RequestParam("id") Long id) {
        return  new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.findFkById(id));
    }

    /**
     * 方法说明： 按条件查询一个 收寄信息 对象
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 收寄信息 对象(带外键标签)", notes = "返回 收寄信息对象 (带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressFkCond", name = "{table.varName}Cond", value = "收寄信息(外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("findFkOne")
    public org.alvin.code.gen.beans.RestfullResp<AddressFk> findFkOne(AddressFkCond addressCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.findFkOne(addressCond));
    }

    /**
     * 方法说明: 只显示标签和id的列表
     *
     * @param addressFkCond
     * @return
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询一个 收寄信息 对象(只显示标签和id的列表)", notes = "返回 收寄信息对象 (只显示标签和id的列表)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressFkCond", name = "addressFkCond", value = "收寄信息", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryLabelList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<org.alvin.code.gen.beans.SelectOption>> queryLabelList(AddressFkCond addressFkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryLabelList(addressFkCond));
    }

    /**
     * 方法说明：按条件查询不分页收寄信息列表 (带关系表标签)
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询不分页[收寄信息]列表 (带外键标签)", notes = "返回不分页[收寄信息]列表 (带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressFkCond", name = "addressFkCond", value = "收寄信息 (外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryFkList")
    public org.alvin.code.gen.beans.RestfullResp<java.util.List<AddressFk>> queryFkList(AddressFkCond addressFkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryFkList(addressFkCond));
    }

    /**
     * 方法说明：按条件查询分页收寄信息列表
     */
    @io.swagger.annotations.ApiOperation(value = "按条件查询分页[收寄信息]列表(带外键标签)", notes = "返回分页[收寄信息]列表(带外键标签)")
    @io.swagger.annotations.ApiImplicitParams(
            {
                    @io.swagger.annotations.ApiImplicitParam(paramType = "body", dataType = "AddressFkCond", name = "fkCond", value = "收寄信息(外键)", required = true)
            })
    @io.swagger.annotations.ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 0, message = "操作成功")
    })
    @org.springframework.web.bind.annotation.PostMapping("queryFkPage")
    public org.alvin.code.gen.beans.RestfullResp<org.alvin.code.gen.utils.Page<AddressFk>> queryFkPage(AddressFkCond fkCond) {
        return new org.alvin.code.gen.beans.RestfullResp<>(this.addressService.queryFkPage(fkCond));
    }

}

