<template>
    <el-dialog :visible.sync="show" title="执行SQL[只支持mysql]" :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="执行脚本" name="script">
                <el-input type="textarea" v-model="form.sqlText" rows="20"></el-input>
            </el-tab-pane>
            <el-tab-pane label="执行sql文件" name="file">
                <el-row>
                    <el-col :span="12">
                        <el-upload
                                drag
                                ref="upload"
                                accept="application/sql"
                                action="/api/code/upload"
                                :on-success="uploadSuccess"
                                :auto-upload="false"
                                multiple>
                            <i class="el-icon-upload"></i>
                            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                            <div class="el-upload__tip" slot="tip">只能上传sql文件，且不超过500kb</div>
                        </el-upload>
                    </el-col>
                    <el-col :span="12">
                        <el-button type="primary" @click="doSubmitSql">上传并执行</el-button>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>
        <div slot="footer">
            <el-button v-if="activeName == 'script'" @click="executeSql" type="primary" size="small">运行</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
        props: ["refresh"],
        components: {},
        data: function () {
            return {
                activeName: 'script',
                show: false,
                form: {
                    sqlText: null
                },
                fileList: []
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            showDialog(){
                this.show = true;
            },
            executeSql() {
                const that = this;
                this.$http.get("/api/code/executeSql", {params: {sql: this.form.sqlText}}).then(res => {
                    that.show = false;
                    that.refresh();
                }).catch(res => {
                    this.$notify.error({title: 'SQL执行失败', message: '请输入正确的建表语句!'});
                });
            },
            uploadSuccess(response, file, fileList){
                this.$notify.success(file.name + " 执行成功")
//                this.show = false;
            },
            doSubmitSql(){
                this.$refs["upload"].submit();
            }

        }
    }
</script>
<style>

</style>
