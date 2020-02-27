<template>
    <div>
        <el-form :inline="true">
            <el-form-item label="唯一键">
                <el-input placeholder="请输入常量类型唯一键" size="small" v-model="form.typeKey"></el-input>
            </el-form-item>
            <el-form-item label="显示文本">
                <el-input placeholder="请输入常量类型显示文本" size="small" v-model="form.typeLabel"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button icon="search" @click="refresh" title="根据输入的条件查询" size="small">查询</el-button>
                <el-button type="primary" icon="plus" @click="doAdd()" title="添加" size="small">添加</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="dataList" v-loading="loading" element-loading-text="正在加载......" style="width: 100%">
            <el-table-column prop="id" label="主键" width="150"></el-table-column>
            <el-table-column prop="typeKey" label="唯一键"></el-table-column>
            <el-table-column prop="typeLabel" label="显示文本"></el-table-column>
            <el-table-column label="操作" width="250">
                <template slot-scope="props">
                    <div>
                        <el-button type="text" @click="doEditValue(props.row)">常量值管理</el-button>
                        <el-button type="text" @click="doEdit(props.row)">编辑</el-button>
                        <el-button type="text" @click="doDelete(props.row)">删除</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <br/>
        <div style="text-align: right" v-if="total > 0">
            <el-pagination small layout="total,sizes,prev, pager, next" :current-page="page" :total="total"
                           @current-change="(curr) => {this.page = curr ; this.refresh();}"
                           :page-sizes="[10, 15, 20, 100]" @size-change="(s) => {this.size = s ; this.refresh();}"
                           :page-size="size"></el-pagination>
        </div>
        <AlvinGenCodeConstDialog ref="dialog" :refresh="refresh"></AlvinGenCodeConstDialog>
        <AlvinGenCodeConstValueList ref="valueDialog"></AlvinGenCodeConstValueList>
    </div>
</template>
<script>
    import AlvinGenCodeConstDialog from './AlvinGenCodeConstDialog.vue';
    import AlvinGenCodeConstValueList from  '../alvingencodeconstvalue/AlvinGenCodeConstValueList.vue';
    export default {
        components: {AlvinGenCodeConstDialog,AlvinGenCodeConstValueList},
        data: function () {
            return {
                total: 0,
                page: 1,
                size: 20,
                dataList: [],
                form: {
                    id: null,// 主键
                    typeKey: null,// 常量类型唯一键
                    typeLabel: null,// 常量类型显示文本
                },
                loading: false
            }
        },
        computed: {},
        created: function () {
            this.refresh();
        },
        methods: {
            refresh() {
                const that = this;
                that.loading = true;
                const requestData = {...that.form, page: that.page - 1, size: that.size};
                that.$http.post("/api/alvinGenCodeConst/queryPage", JSON.stringify(requestData)).then(res => {
                    that.loading = false;
                    that.dataList = res.data.content;
                    that.total = res.data.totalElements;
                }).catch(res => {
                    that.$message.error("获取常量字典表列表失败：" + res);
                    that.loading = false;
                });
            },
            doAdd() {
                this.$refs["dialog"].addDialog(this.typeRow.id);
            },
            doEdit(row) {
                this.$refs["dialog"].editDialog(row);
            },
            doEditValue(row){
                this.$refs["valueDialog"].showDialog(row);
            },
            doDelete(row) {
                const that = this;
                this.$confirm('你确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    that.$http.delete("/api/alvinGenCodeConst/delete", {
                        params: {id: row.id}
                    }).then(res => {
                        this.$message.success("删除成功");
                        that.refresh();
                    }).catch(res => {
                        that.$message.error("删除失败：" + res);
                    });
                }).catch(() => {
                });
            }
        }
    }
</script>
<style></style>