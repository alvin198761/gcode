<template>
    <el-dialog :title="title" :visible.sync="show" :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="form" ref="form" :rules="rules" label-width="100px">
            <el-row>
                <el-col>
                    <el-form-item label='唯一键' prop='typeKey'>
                        <el-input placeholder='请输入常量类型唯一键' size="small" v-model='form.typeKey'></el-input>
                    </el-form-item>
                    <el-form-item label='显示文本' prop='typeLabel'>
                        <el-input placeholder='请输入常量类型显示文本' size="small" v-model='form.typeLabel'></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer" style="text-align: right">
            <el-button @click="show = false">取消</el-button>
            <el-button type="primary" @click="save()">确定</el-button>
        </div>
    </el-dialog>
</template>
<script>
    export default {
        components: {},
        props: ["refresh"],
        data() {
            return {
                title: '',
                form: this.initForm(),
                dialogMode: "save",
                show: false,
                rules: {
                    typeKey: [
                        {required: true, message: '请输入常量类型唯一键', trigger: 'blur'},
                    ],
                    typeLabel: [
                        {required: true, message: '请输入常量类型显示文本', trigger: 'blur'},
                    ],
                }
            }
        },
        methods: {
            save() {//新增及修改记录
                const that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return;
                    }
                    that.$http.post("/api/alvinGenCodeConst/" + that.dialogMode, JSON.stringify(that.form)).then(res => {
                        that.show = false;
                        that.$message.success(that.title + "成功!");
                        that.refresh();
                    }).catch(res => {
                        that.$message.error(that.title + "出错!" + res);
                    });
                });
            },
            initForm() {//初始数据
                return {
                    id: null,// 主键
                    typeKey: null,// 常量类型唯一键
                    typeLabel: null,// 常量类型显示文本
                }
            },
            addDialog() {//新增
                this.title = "新增常量字典表";
                this.dialogMode = "save";
                this.form = this.initForm();
                this.show = true;
            },
            editDialog(row) {//修改
                this.title = "修改常量字典表";
                this.dialogMode = "update";
                this.form = {...row};
                this.show = true;
            },
        }
    }
</script>
<style></style>