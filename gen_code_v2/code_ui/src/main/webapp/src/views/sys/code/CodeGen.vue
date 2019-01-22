<template>
    <div>
        <div style="color: blue;">
            【<span style="color: red;">注意</span>】
            【调整[作者名]\[模块名],点击[查看字段]确保表名注释与字段名注释<span style="color: red;">为中文</span>,建议类名中表名前缀部分<span
                style="color: red;">已去掉</span>】
        </div>
        <br/>
        <div>
            表名
            <el-input v-model="form.t_name" placeholder="请输入表名" style="width:100px" size="small"></el-input> &nbsp;
            <el-button @click="query" type="primary" size="small">查询</el-button>&nbsp;
            作者名
            <el-input v-model="form.auth" style="width:80px" size="small"></el-input>&nbsp;
            公司名
            <el-input v-model="form.company" style="width:80px" size="small"></el-input>&nbsp;
            模块名
            <el-input v-model="form.model" style="width:80px" size="small"></el-input>&nbsp;
            <el-button @click="createCode" type="primary" size="small">生成代码</el-button>&nbsp;
            项目名[alvin]
            <el-button @click="openSql" type="primary" size="small">建表</el-button>&nbsp;
        </div>
        <br/>
        <el-table :data="filterTableList" class="tabClass" @selection-change="onSelectChange" border size="small">
            <el-table-column type="selection" width="40px"></el-table-column>
            <el-table-column prop="t_name" label="表名"></el-table-column>
            <el-table-column prop="comment" label="注释"></el-table-column>
            <el-table-column label="建议类名">
                <template slot-scope="props">
                    <el-input v-model="props.row.cls_upp" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="类中文名">
                <template slot-scope="props">
                    <el-input v-model="props.row.c_name" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200px">
                <template slot-scope="props">
                    <el-button @click="queryfieldList(props.row.t_name)" type="primary" size="small">查看字段</el-button>
                    <el-button @click="deleteTable(props.row.t_name)" type="primary" size="small">删除表</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog :visible.sync="show">
            <el-table :data="f_list" class="tabClass" border size="small">
                <el-table-column prop="name" label="字段名"></el-table-column>
                <el-table-column prop="comment" label="字段注释"></el-table-column>
                <el-table-column prop="type" label="JAVA数据类型"></el-table-column>
            </el-table>
        </el-dialog>
        <el-dialog :visible.sync="showSql" title="执行SQL(只能一条一条执行!)">
            <el-input type="textarea" v-model="form.sqlText" rows="20"></el-input>
            <el-button @click="executeSql" type="primary" size="small">运行</el-button>
        </el-dialog>
    </div>
</template>
<script>

    export default{
        data: function () {
            return {
                form: {
                    t_name: '',
                    c_list: [],
                    auth: "唐植超",
                    company: "alvin",
                    model: "demo",
                    sqlText: null,
                },
                t_list: [],
                f_list: [],
                show: false,
                showSql: false,
            }
        },
        created: function () {
            this.query();
        },
        computed: {
            filterTableList() {
                const that = this;
                return that.t_list.filter(item => item.t_name.indexOf(that.form.t_name) != -1);
            }
        },
        methods: {
            queryfieldList(t_name) {
                this.show = true;
                this.$http.post("/api/code/queryField", JSON.stringify({"t_name_eq": t_name})).then(res => {
                    this.f_list = res.data;
                }).catch(res => {
                    this.$notify.error({title: '失败', message: '加载表信息败!'});
                });
            },
            openSql(t_name) {
                this.showSql = true;
            },
            executeSql() {
                // {params: {sql: this.form.sqlText}}
                this.$http.get("/api/code/executeSql",  {params: {sql : this.form.sqlText}}).then(res => {
                    this.showSql = false;
                    this.query();
                }).catch(res => {
                    this.$notify.error({title: 'SQL执行失败', message: '请输入正确的建表语句!'});
                });
            },
            deleteTable(tableName) {
                this.$http.get("/api/code/executeSql",  {params: {sql :" DROP TABLE IF EXISTS "+tableName}}).then(res => {
                    this.showSql = false;
                    this.query();
                }).catch(res => {
                    this.$notify.error({title: 'SQL执行失败', message: '请输入正确的建表语句!'});
                });
            },
            onSelectChange(val) {
                this.form.c_list = val;
            },
            query() {
                this.$http.post("/api/code/queryList", JSON.stringify({"t_name": this.form.t_name})).then(res => {
                    this.t_list = res.data;
                }).catch(res => {
                    this.$notify.error({title: '失败', message: '加载表信息败!'});
                });
            },
            createCode() {
                if (this.form.c_list.length == 0) {
                    this.$notify({title: '警告', message: '请至少选择一条记录.!', type: 'warning'});
                    return;
                }
                this.$http.post("/api/code/create", JSON.stringify(this.form)).then(res => {
                    this.$notify.info({title: '消息', message: '生成代码成功'});
                    location.href = "/api/code/downCode";
                }).catch(res => {
                    this.$notify.error({title: '失败', message: '生成代码失败,请检查代码是否已经存在!'});
                });
            },
        },
    }
</script>
<style  >

</style>
