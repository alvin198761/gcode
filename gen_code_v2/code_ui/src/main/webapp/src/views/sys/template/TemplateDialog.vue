<template>
    <el-dialog title="模板设置" :visible.sync="show" width="600px">
        <el-form :model="form" :rules="rules" ref="form" label-width="100px">
            <el-row>
                <el-col :span="12">
                    <el-form-item label="模板名称" prop="name">
                        <el-input v-model="form.name" placeholder="只能字母数字下划线" size="small"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="模板作者" prop="author">
                        <el-input v-model="form.author" placeholder="请输入模板作者" size="small"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col>
                    <el-form-item label="模板描述" prop="remark">
                        <el-input v-model="form.remark" placeholder="请输入模板描述" type="textarea" size="small"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer">
            <el-button @click="show = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
        props: ["saveTemplate"],
        components: {},
        data: function () {
            return {
                show: false,
                form: {
                    name: '',
                    author: '',
                    remark: '',//备注
                },
                rules: {
                    name: [
                        {required: true, message: '请输入模板名称', trigger: 'blur'},
                    ],
                    author: [
                        {required: true, message: '请输入模板作者', trigger: 'blur'},
                    ]
                },
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            addDialog(){
                this.form = {
                    name: '',
                    author: '',
                    remark: '',//备注
                }
                this.show = true;
            },
            save(){
                const that = this;
                this.$refs["form"].validate((valid) => {
                    if (!valid) {
                        return;
                    }
                    that.saveTemplate(that.form);
                    this.show = false;
                });
            }
        }
    }
</script>
<style>

</style>
