<template>
    <el-dialog title="文件设置" :visible.sync="show" width="80%">
        <el-form :model="form" :rules="rules" ref="form" label-width="100px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="文件名称" prop="name">
                        <el-input v-model="form.name" placeholder="输入文件名" size="small"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="文件类型" prop="type">
                        <el-select v-model="form.type" size="small" placeholder="请选择文件类型">
                            <el-option label="普通模板" :value="1"></el-option>
                            <el-option label="实体模板" :value="2"></el-option>
                            <el-option label="非模板文件" :value="3"></el-option>
                            <el-option label="目录" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20" v-if="form.type !=4">
                <el-col>
                    <el-form-item label="文件内容" prop="remark">
                        <el-tabs v-model="form.contentType">
                            <el-tab-pane label="编辑内容" name="content">
                                <el-input v-model="form.content" placeholder="请输入模板描述" rows="10" type="textarea"
                                          size="small"
                                          autocomplete="off"></el-input>
                            </el-tab-pane>
                            <el-tab-pane label="上传内容" name="file">
                                <el-upload
                                        size="small"
                                        class="upload-demo"
                                        drag
                                        action="/api/template/upload"
                                        name="file"
                                        :on-success="uploadSuccess"
                                >
                                    <i class="el-icon-upload"></i>
                                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                                    <div class="el-upload__tip" slot="tip">文件不得超过10m</div>
                                </el-upload>
                            </el-tab-pane>
                            <el-tab-pane label="URL下载" name="url">
                                <el-input v-model="form.content" placeholder="地址：http://www.code.com/test.xml" rows="10"
                                          size="small"
                                          autocomplete="off"></el-input>
                            </el-tab-pane>
                        </el-tabs>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer">
            <el-button @click="show = false" size="small">取 消</el-button>
            <el-button type="primary" @click="save" size="small">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
        props: ["refresh"],
        components: {},
        data: function () {
            return {
                show: false,
                form: {
                    //文件名称
                    name: '',
                    //文件类型 1 普通模板 2 实体模板 3 非模板 4 目录
                    type: '',
                    //相对项目模板文件夹的位置，相对路径
                    path: '',
                    //模板名称
                    templateName: '',
                    //内容
                    content: '',
                    //父节点名称
                    pid: '',
                    //当前节点id
                    id: '',
                    contentType: 'content'
                },
                rules: {},
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            save(){
                const that = this;
                that.$http.post("/api/template/addFile", JSON.stringify(this.form)).then(res => {
                    that.$Message.success({content: '添加成功'});
                    that.refresh();
                }).catch(err => {
                    that.$Message.error({content: '添加失败'});
                });
            },
            addDialog(data){
                let copyData = {...data};
                if (copyData.id == -1) {
                    copyData.path = '/';
                }
                this.form = {...this.form, pid: copyData.id, path: copyData.path, templateName: copyData.templateName}
                this.show = true;
            },
            uploadSuccess(res){
                console.log(res);
            }
        }
    }
</script>
<style>

</style>
