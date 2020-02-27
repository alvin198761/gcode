<template>
    <div>
        <el-form :inline="true" :model="form">
            <el-form-item label="表名称">
                <el-input v-model="form.tableName" placeholder="表名称" size="small"></el-input>
            </el-form-item>
            <el-form-item>
                <el-checkbox v-model="showConfigTable" @change="changeShowConfigTable">隐藏配置表</el-checkbox>
                &nbsp;
                <el-button type="primary" @click="refresh" size="small">刷新</el-button>
                <el-button type="primary" @click="genCode" size="small" :disabled="selectList.length == 0">生成代码
                </el-button>
                <el-button type="primary" @click="executeSql" size="small">执行SQL</el-button>
                <el-button type="primary" @click="designTable" size="small">表设计</el-button>
                <!--<el-button type="primary" size="small">导出表结构</el-button>-->
                <!--<el-button type="primary" size="small">导出数据</el-button>-->
            </el-form-item>
        </el-form>
        <el-table :data="filterTableList" class="tabClass" @selection-change="onSelectChange" size="small">
            <el-table-column type="selection" width="50px"></el-table-column>
            <el-table-column prop="tableName" label="表名"></el-table-column>
            <el-table-column prop="comment" label="注释"></el-table-column>
            <el-table-column label="建议类名">
                <template slot-scope="props">
                    <el-input v-model="props.row.className" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="类中文名">
                <template slot-scope="props">
                    <el-input v-model="props.row.cnName" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="160px">
                <template slot-scope="props">
                    <el-button type="text" size="small" @click="showTableSettingsDialog(props.row)">设置</el-button>
                    <!--<el-button @click="doEdit(props.row)" type="text" size="small">编辑</el-button>-->
                    <!--<el-button @click="doDelete(props.row)" type="text" size="small">删除</el-button>-->
                </template>
            </el-table-column>
        </el-table>
        <CodeGenDialog ref="genDialog"></CodeGenDialog>
        <ExecuteSqlDialog ref="executeSqlDialog" :refresh="refresh"></ExecuteSqlDialog>
        <TableDesignDialog ref="tableDesignDialog"></TableDesignDialog>
        <TableSettingsDialog ref="tableSettingsDialog" :refresh="refresh"></TableSettingsDialog>
    </div>
</template>
<script>
    import CodeGenDialog from './CodeGenDialog.vue';
    import ExecuteSqlDialog from './ExecuteSqlDialog.vue';
    import TableDesignDialog from './TableDesignDialog.vue';
    import TableSettingsDialog from './TableSettingsDialog.vue';

    export default {
        components: {CodeGenDialog, ExecuteSqlDialog, TableDesignDialog, TableSettingsDialog},
        data() {
            return {
                form: {
                    tableName: null
                },
                dataList: [],
                selectList: [],
                showConfigTable: true,
            }
        },
        computed: {
            filterTableList() {
                const that = this;
                if (that.form.tableName === null) {
                    return that.dataList;
                }
                return that.dataList.filter(item => item.tableName.indexOf(that.form.tableName) !== -1);
            }
        },
        created() {
            this.refresh();
        },
        methods: {
            refresh() {
                //刷新
                this.$post("/api/code/queryTables", {showConfigTable: this.showConfigTable}).then(res => {
                    if (res.code !== 0) {
                        this.$message.error(res.errmsg)
                        return;
                    }
                    this.dataList = res.data;
                });
            },
            genCode() {
                //生成代码
                this.$refs["genDialog"].showDialog(this.selectList);
            },
            executeSql() {
                //执行sql生成
                this.$refs["executeSqlDialog"].showDialog();
            },
            designTable() {
                //设计表
                this.$refs["tableDesignDialog"].showDialog();
            },
            exportStruts() {
                //导出表结构

            },
            exportStrutsAndData() {
                //导出表结构和数据
            },
            onSelectChange(val) {
                // 选择
                this.selectList = val;
            },
            doEdit(row) {
                //编辑表

            },
            doDelete(row) {
                //删除表

            },
            showTableSettingsDialog(table) {
                this.$refs["tableSettingsDialog"].showDialog(table);
            },
            changeShowConfigTable(val) {
                this.refresh()
            }
        }
    }
</script>
<style></style>
