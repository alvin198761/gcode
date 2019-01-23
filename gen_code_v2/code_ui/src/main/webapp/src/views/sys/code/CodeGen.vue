<template>
    <div>
        <div style="color: blue;">
            【<span style="color: red;">注意</span>】目前只支持<b>mysql</b>
            [查看字段]确保表名注释与字段名注释<span style="color: red;">为中文</span>,建议类名中表名前缀部分<span
                style="color: red;">已去掉</span>】
        </div>
        <br/>
        <div>
            表名
            <el-input v-model="form.t_name" placeholder="请输入表名" style="width:200px" size="small"></el-input>
            &nbsp;
            <el-button @click="query" type="primary" size="small">查询</el-button>
            &nbsp;
            <el-button @click="createCode" type="primary" size="small" :disabled="form.c_list.length == 0">生成代码
            </el-button>

            <el-button @click="openSql" type="primary" size="small">执行SQL</el-button>
            &nbsp;
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

        <CodeGenDialog ref="codeGenDialog"></CodeGenDialog>
        <ExecuteSqlDialog ref="executeSqlDialog" :refresh="query"></ExecuteSqlDialog>
    </div>
</template>
<script>
    import ExecuteSqlDialog from './ExecuteSqlDialog.vue';
    import CodeGenDialog  from './CodeGenDialog.vue';
    export default{
        components: {
            CodeGenDialog,
            ExecuteSqlDialog
        },
        data: function () {
            return {
                form: {
                    t_name: '',
                    c_list: [],
                },
                t_list: [],
                f_list: [],
                show:false
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
            openSql() {
                this.$refs["executeSqlDialog"].showDialog();
            },
            deleteTable(tableName) {
                this.$http.get("/api/code/executeSql", {params: {sql: " DROP TABLE IF EXISTS " + tableName}}).then(res => {
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
                this.$refs["codeGenDialog"].showDialog(this.form.c_list);
            },
        },
    }
</script>
<style>

</style>
