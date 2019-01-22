<template>
    <div id="app">
        <el-container>
            <el-header style="text-align: center">
                <el-row>
                    <el-col :span="2"><a href="/#/">代码生成工具</a></el-col>
                    <el-col :span="20">
                        <el-menu default-active="/code" mode="horizontal" router
                                 background-color="#545c64"
                                 text-color="#fff"
                                 active-text-color="#ffd04b">
                            <el-menu-item index="/code">代码生成</el-menu-item>
                            <el-menu-item index="/swagger">Swagger 客户端</el-menu-item>
                            <el-menu-item index="/project">项目设计</el-menu-item>
                            <el-menu-item index="/template">模板设计</el-menu-item>
                        </el-menu>
                    </el-col>
                    <el-col :span="2">
                        <a href="/j_spring_security_logout">退出登录</a>
                    </el-col>
                </el-row>
            </el-header>
            <el-main style="min-height: 800px">
                <router-view></router-view>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        data(){
            return {
                timer: null
            };
        },
        created(){
            const that = this;
            this.timer = setInterval(() => that.$http.get("/api/heartbeat"), 2 * 60 * 1000);
        },
        destroyed(){
            clearInterval(this.timer);
        }
    }
</script>


<style lang="less">
    html, body, #app {
        width: 100%;
        height: 100%;
        margin: 0px;
    }

    .el-header, .el-footer, .el-header a, .el-footer a {
        background-color: #545c64;
        color: #fff;
        text-align: center;
        line-height: 60px;
        text-decoration: none;
    }

    body {
        /*background-color: #324057;*/
        margin: 0px;
        padding: 0px;
        /*background: url(assets/bg1.jpg) center !important;
            background-size: cover;*/
        /*background: #1F2D3D;*/
        font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
        font-weight: 400;
        -webkit-font-smoothing: antialiased;
    }

    #app {
        position: absolute;
        top: 0px;
        bottom: 0px;
        width: 100%;
    }

    .el-submenu [class^=fa] {
        vertical-align: baseline;
        margin-right: 10px;
    }

    .el-menu-item [class^=fa] {
        vertical-align: baseline;
        margin-right: 10px;
    }

    .el-table td {
        padding: 2px 0 !important;
    }

    input, button, .el-card, .el-message, .el-alert ,textarea ,.el-dialog ,.el-select-dropdown,.el-tag,.el-radio-button:first-child .el-radio-button__inner,.el-radio-button:last-child .el-radio-button__inner,.el-popover,.el-message-box{
        border-radius: 0px !important;
    }

    .el-form-item__content, .el-form-item__label {
        /*line-height: 30px;*/
    }

    .el-input__inner, .el-button ,.el-cascader{
        /*height: 30px;*/
        line-height: 30px;
        font-size: 12px !important;

    }

    .el-button {
        padding:  0 20px;
    }

    .el-button--text {
        padding-right: 0px;
        padding-left: 0px;
    }

    .el-tree-node__content {
        height: 30px;
    }
    /*下拉展示样式*/
    .el-table__expanded-cell .el-form{
        margin:20px 0px 10px 58px;

    }
    /*    调整间距*/
    .el-form .el-row .el-col .el-form-item{
        margin-bottom:15px;
    }
    /*调整验证距离*/
    .el-form-item__error{
        padding-top:1px !important
    }
</style>
