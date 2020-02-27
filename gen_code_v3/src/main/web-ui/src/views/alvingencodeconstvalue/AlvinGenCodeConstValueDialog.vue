/*${cName}新增与修改,作者:${auth},日期:2020-02-23 14:37:44*/
<template>
    <el-dialog :title="title" :visible.sync="show" :close-on-click-modal="false" :close-on-press-escape="false" width="40%">
        <el-form :model="form" ref="form" :rules="rules" label-width="100px">
            <el-row>
                <el-col>
                    <el-form-item label='唯一键' prop='dataKey'>
                        <el-input-number controls-position="right" placeholder='请输入数据唯一键' size="small" v-model='form.dataKey' style="text-align: left;width: 100%"></el-input-number>
                    </el-form-item>
                    <el-form-item label='显示文本' prop='dataLabel'>
                        <el-input placeholder='请输入数据显示文本' size="small" v-model='form.dataLabel'></el-input>
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
                    dataKey: [
                        {required: true, message: '请输入数据唯一键', trigger: 'blur'},
                    ],
                    dataLabel: [
                        {required: true, message: '请输入数据显示文本', trigger: 'blur'},
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
                    that.$http.post("/api/alvinGenCodeConstValue/" + that.dialogMode, JSON.stringify(that.form)).then(res => {
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
                    typeId: null,// 主键
                    dataKey: null,// 数据唯一键
                    dataLabel: null,// 数据显示文本
                }
            },
            addDialog(typeId) {//新增
                this.title = "新增常量值";
                this.dialogMode = "save";
                this.form = this.initForm();
                this.form = {...this.form, typeId: typeId};
                this.show = true;
            },
            editDialog(row) {//修改
                this.title = "修改常量值";
                this.dialogMode = "update";
                this.form = {...row};
                this.show = true;
            },
        }

    }
</script>
<style></style>