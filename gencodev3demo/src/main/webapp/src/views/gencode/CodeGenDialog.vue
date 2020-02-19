<template>
    <el-dialog :visible.sync="show" title="代码生成设置" width="80%" :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-form ref="form" :model="form" label-width="80px">
            <el-row :gutter="10">
                <el-col :span="12">
                    <el-form-item label="作者">
                        <el-input v-model="form.author"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="包名">
                        <el-input v-model="form.packageName"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="模板">
                <el-transfer v-model="form.templateDirs" :data="tempList"
                             :titles="['未选择目录', '已选择目录']" style="width: 100%"></el-transfer>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="show = false">取 消</el-button>
            <el-button type="primary" @click="genCode" :disabled="!this.form.templateDirs || this.form.templateDirs.length == 0">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
        components: {},
        data: function () {
            return {
                show: false,
                form: {
                    author: '唐植超', //作者
                    packageName: 'org.alvin.home',
                    templateDirs: [],
                    tables: []
                },
                tempList: [],
                defaultCheckedList: []
            }
        },
        computed: {},
        created: function () {
        },
        methods: {
            loadTemplates(){
                const that = this;
                that.$post("/api/code/queryTemplateDir").then(res => {
                    that.form.templateDirs = [];
                    that.tempList = [];
                    if(res.code !== 0){
                        that.$message.error(res.errmsg);
                        return ;
                    }
                    that.tempList = res.data.map(item => {
                        return ({
                            key: item,
                            label: item,
                        });
                    });
                }).catch(res => {
                    this.$message.error("获取模板列表失败：" + res)
                });
            },
            genCode(){
                const that = this;
                this.$postBody("/api/code/gencode",  this.form ).then(res => {
                    if (res.code !== 0) {
                        this.$notify.error({title: '失败', message: res.date.errorMsg});
                        return;
                    }
                    this.$notify.info({title: '消息', message: '生成代码成功'});
                    that.show = false;
                    location.href = "/api/code/download/" + res.data
                }).catch(res => {
                    this.$notify.error({title: '失败', message: '生成代码失败'});
                });
            },
            showDialog(tables){
                this.form.tables = [...tables];
                this.loadTemplates();
                this.show = true;
            }
        }
    }
</script>
<style>

    .el-transfer .el-transfer-panel {
        width: 380px;
    }
</style>
