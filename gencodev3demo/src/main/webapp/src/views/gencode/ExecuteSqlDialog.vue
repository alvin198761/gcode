<template>
    <el-dialog :visible.sync="show" title="执行SQL[只支持mysql]" :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="执行脚本" name="script">
                <textarea id="code"></textarea>
            </el-tab-pane>
            <el-tab-pane label="执行sql文件" name="file">
                <el-row>
                    <el-col :span="12">
                        <el-upload
                                drag
                                ref="upload"
                                accept="application/sql"
                                action="/api/code/executeSqlFile"
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
            <el-button v-if="activeName == 'script'" @click="formatSql"  type="primary" size="small">格式化代码</el-button>
            <el-button v-if="activeName == 'script'" @click="executeSql" type="primary" size="small">运行</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default {
        props: ["refresh"],
        components: {},
        data: function () {
            return {
                activeName: 'script',
                show: false,
                fileList: [],
                editor: null
            }
        },
        computed: {},
        created: function () {

        },
        mounted: function () {

        },
        methods: {
            showDialog() {
                this.show = true;
                const that = this;
                if (that.editor) {
                    return;
                }
                setTimeout(function () {
                    //根据DOM元素的id构造出一个编辑器
                    that.editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                        mode: "text/x-sql", //实现Java代码高亮
                        lineNumbers: true,
                        theme: "dracula",
                        keyMap: "sublime",
                        extraKeys: {"Ctrl": "autocomplete"},
                        hint: CodeMirror.hint.sql,
                        lineWrapping: true,         //是否换行
                        foldGutter: true,           //是否折叠
                        gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"], //添加行号栏，折叠栏
                    });
                    that.editor.setSize('height', '500px');
                }, 100)
            },
            executeSql() {
                const that = this;
                this.$post("/api/code/executeSql", {sql: that.editor.getValue()}).then(res => {
                    console.log(res)
                    that.show = false;
                    that.refresh();
                }).catch(res => {
                    console.log(res);
                    this.$notify.error({title: 'SQL执行失败', message: '请输入正确的建表语句!'});
                });
            },
            uploadSuccess(response, file, fileList) {
                this.$notify.success(file.name + " 执行成功")
//                this.show = false;
            },
            doSubmitSql() {
                this.$refs["upload"].submit();
            },
            formatSql(){
                let range = { from: this.editor.getCursor(true), to: this.editor.getCursor(false) };
                this.editor.autoFormatRange(range.from, range.to);
                let str = sqlFormatter.format(this.editor.getValue(), {language: 'sql'});
                this.editor.setValue(str);
            }

        }
    }
</script>
<style>

</style>
