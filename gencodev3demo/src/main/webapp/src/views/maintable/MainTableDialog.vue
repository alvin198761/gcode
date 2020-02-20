/*主表管理,作者:唐植超,日期:2020-02-20 20:18:37*/
<template>
    <el-dialog :title="title" :visible.sync="show" :close-on-click-modal="false" :close-on-press-escape="false"
               width="80%">
        <el-form :model="form" ref="form" :rules="rules" label-width="100px">

            <el-row>
                <el-col>
                    <el-form-item label="显示文本" prop="mainTitle">
                        <el-input placeholder="请输入显示文本" size="small" v-model="form.mainTitle" clearable
                                  style="width: 100%"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col>
                    <el-form-item label="a表主键" prop="aId">
                        <!-- aId -->
                        <el-select placeholder="请输入a表主键" size="small" v-model="form.aId" clearable style="width: 100%">
                            <el-option
                                    v-for="item in aIdFkList"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col>
                    <el-form-item label="b表主键" prop="bId">
                        <!-- bId -->
                        <el-select placeholder="请输入b表主键" size="small" v-model="form.bId" clearable style="width: 100%">
                            <el-option
                                    v-for="item in bIdFkList"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row>
                <el-col>
                    <el-form-item label="日期" prop="date">
                        <el-date-picker size="small" v-model="form.date" type="date" placeholder="请输入日期" clearable
                                        style="width: 100%"></el-date-picker>
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
                    //主键不处理
                    main_title: [
                        {required: true, message: '请输入显示文本', trigger: 'blur'}, // {min: 1, max: 10, message: '显示文本长度不正确', trigger: 'blur'},
                    ],
                    a_id: [
                        {required: true, message: '请输入a表主键', trigger: 'blur'}, // {min: 1, max: 10, message: 'a表主键长度不正确', trigger: 'blur'},
                    ],
                    b_id: [
                        {required: true, message: '请输入b表主键', trigger: 'blur'}, // {min: 1, max: 10, message: 'b表主键长度不正确', trigger: 'blur'},
                    ],
                    date: [
                        {required: true, message: '请输入日期', trigger: 'blur'}, // {min: 1, max: 10, message: '日期长度不正确', trigger: 'blur'},
                    ],
                },
                aIdFkList: [],
                bIdFkList: [],
            }
        },
        methods: {
            loadAIdFkList() {
                const that = this;
                that.$postBody("/api/aaTable/queryLabelList", {}).then(res => {
                    if (res.code != 0) {
                        that.$message.error(res.errmsg);
                        return;
                    }
                    that.aIdFkList = res.data;
                }).catch(err => {
                    that.$message.error("获取数据出错:" + err);
                })
            },
            loadBIdFkList() {
                const that = this;
                that.$postBody("/api/bbTest/queryLabelList", {}).then(res => {
                    if (res.code != 0) {
                        that.$message.error(res.errmsg);
                        return;
                    }
                    that.bIdFkList = res.data;
                }).catch(err => {
                    that.$message.error("获取数据出错:" + err);
                })
            },
            initData() {
                //初始化数据
                this.loadAIdFkList();
                this.loadBIdFkList();
            },
            save() {//新增及修改记录
                const that = this;
                that.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return;
                    }
                    that.$postBody("/api/mainTable/" + that.dialogMode, that.form).then(res => {
                        that.show = false;
                        if (res.code != 0) {
                            that.$message.error(res.errmsg);
                            return;
                        }
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
                    main_title: null,// 显示文本
                    a_id: null,// a表主键
                    b_id: null,// b表主键
                    date: null,// 日期
                }
            },
            addDialog() {//新增
                this.title = "新增主表";
                this.dialogMode = "save";
                this.form = this.initForm();
                this.initData();
                this.show = true;
            },
            editDialog(row) {//修改
                this.title = "修改主表";
                this.dialogMode = "update";
                this.form = {...row};
                this.initData();
                this.show = true;
            },
        }

    }
</script>
<style></style>