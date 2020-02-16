<template>
    <el-dialog :visible.sync="show" title="代码生成设置" width="80%" :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="作者">
                <el-input v-model="form.author"></el-input>
            </el-form-item>
            <el-form-item label="包名">
                <el-input v-model="form.packageName"></el-input>
            </el-form-item>
            <el-form-item label="模板">
                <el-transfer v-model="form.templateDirs" :data="tempList"
                             :titles="['未选择目录', '已选择目录']"></el-transfer>
            </el-form-item>

        </el-form>
        <div slot="footer">
            <el-button @click="show = false">取 消</el-button>
            <el-button type="primary" @click="genCode">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
//        props: ["c_list"],
        components: {},
        data: function () {
            return {
                show: false,
                form: {
                    author: '唐植超', //作者
                    packageName: 'org.alvin.home',
                    templateDirs: [],
                    c_list: []
                },
                tempList: [],
                defaultCheckedList: []
            }
        },
        computed: {},
        created: function () {
            this.loadTemplates();
        },
        methods: {
            loadTemplates(){
                const that = this;
                that.$http.get("/api/code/templateDirs").then(res => {
                    that.form.templateDirs = [];
                    that.tempList = res.data.map(item => {
                        let data = ({
                            key: item,
                            label: item,
                        });
                        if (["web", "data", "model", "element_ui_vue"].indexOf(item) != -1) {
                            that.form.templateDirs.push(item);
                        }
                        return data;
                    });
                }).catch(res => {
                    this.$message.error("获取模板列表失败：" + res)
                });
            },
            genCode(){
                const that = this;
                this.$http.post("/api/code/create", JSON.stringify(this.form)).then(res => {
                    if (res.data.code != 0) {
                        this.$notify.error({title: '失败', message: res.date.errorMsg});
                        return;
                    }
                    this.$notify.info({title: '消息', message: '生成代码成功'});
                    that.show = false;
                    location.href = "/api/code/downCode/" + res.data.data.download_url;
                }).catch(res => {
                    this.$notify.error({title: '失败', message: '生成代码失败'});
                });
            },
            showDialog(c_list){
                this.form.c_list = [...c_list];
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
