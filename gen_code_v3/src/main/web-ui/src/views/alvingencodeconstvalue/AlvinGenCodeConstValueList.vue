/*${cName}管理,作者:${auth},日期:2020-02-23 14:37:44*/
<template>
    <div>
        <el-drawer :visible.sync="show" show-close modal-append-to-body destroy-on-close append-to-body
                   close-on-press-escape :wrapperClosable="false" :close-on-click-modal="false"
                   :close-on-press-escape="false" size="70%">
            <div slot="title" style="font-size: 20px; font-weight: bold">
                常量值管理
            </div>
            <div style="margin-left: 10px">
                <el-form :inline="true">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="类型标识">
                                {{typeRow.typeKey}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="显示文本">
                                {{typeRow.typeLabel}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <hr/>
                <el-form :inline="true">
                    <el-form-item label="数据唯一键">
                        <el-input placeholder="请输入数据唯一键" size="small" v-model="form.dataKey"></el-input>
                    </el-form-item>
                    <el-form-item label="数据显示文本">
                        <el-input placeholder="请输入数据显示文本" size="small" v-model="form.dataLabel"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button icon="search" @click="refresh" title="根据输入的条件查询" size="small">查询</el-button>
                        <el-button type="primary" icon="plus" @click="doAdd()" title="添加" size="small">添加</el-button>
                    </el-form-item>
                </el-form>
                <el-table :data="dataList" v-loading="loading" element-loading-text="正在加载......" style="width: 100%">
                    <el-table-column prop="dataKey" label="数据唯一键"></el-table-column>
                    <el-table-column prop="dataLabel" label="数据显示文本"></el-table-column>
                    <el-table-column label="操作" width="150">
                        <template slot-scope="props">
                            <div>
                                <el-button type="text" @click="doEdit(props.row)">编辑</el-button>
                                <el-button type="text" @click="doDelete(props.row)">删除</el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="drawer-footer">
                <el-button type="primary" @click="show = false">关闭</el-button>
            </div>
        </el-drawer>
        <AlvinGenCodeConstValueDialog ref="dialog" :refresh="refresh"></AlvinGenCodeConstValueDialog>
    </div>
</template>
<script>
    import AlvinGenCodeConstValueDialog from './AlvinGenCodeConstValueDialog.vue';

    export default {
        components: {AlvinGenCodeConstValueDialog},
        data: function () {
            return {
                dataList: [],
                form: {
                    id: null,// 主键
                    typeId: null,// 主键
                    dataKey: null,// 数据唯一键
                    dataLabel: null,// 数据显示文本
                },
                typeRow: {},
                show: false,
                loading: false
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            refresh() {
                const that = this;
                that.loading = true;
                const requestData = {...that.form };
                that.$http.post("/api/alvinGenCodeConstValue/queryList", JSON.stringify(requestData)).then(res => {
                    that.loading = false;
                    that.dataList = res.data
                }).catch(res => {
                    that.$message.error("获取常量值表列表失败：" + res);
                    that.loading = false;
                });
            },
            doAdd() {
                this.$refs["dialog"].addDialog(this.typeRow.id);
            },
            doEdit(row) {
                this.$refs["dialog"].editDialog(row);
            },
            doDelete(row) {
                const that = this;
                this.$confirm('你确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    that.$http.delete("/api/alvinGenCodeConstValue/delete", {
                        params: {id: row.id}
                    }).then(res => {
                        this.$message.success("删除成功");
                        that.refresh();
                    }).catch(res => {
                        that.$message.error("删除失败：" + res);
                    });
                }).catch(() => {
                });
            },
            showDialog(typeRow) {
                this.typeRow = typeRow;
                this.form = {...this.form, typeId: typeRow.id};
                this.refresh();
                this.show = true;
            }
        }
    }
</script>
<style></style>