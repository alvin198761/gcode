<template>
    <el-dialog title="数据表设置" :visible.sync="show" width="50%" :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-form :model="form" :rules="rules" label-width="80px">
            <el-row :gutter="20">
                <el-col>
                    <el-form-item label="标签字段">
                        <el-select v-model="form.labelCol" placeholder="请选择字段" clearable size="small"
                                   style="width: 100%">
                            <el-option
                                    v-for="item in fields"
                                    :key="item.colName"
                                    :label="item.colName"
                                    :value="item.colName">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>

        <hr/>
        <el-table :data="fields">
            <el-table-column prop="colName" label="字段名称">
            </el-table-column>
            <el-table-column prop="comment" label="字段描述">
            </el-table-column>
            <el-table-column prop="refType" label="引用类型">
                <template slot-scope="props">
                    <el-select v-model="props.row.refType" placeholder="请选择引用类型" clearable size="small"
                               style="width: 100%" @change="onRefTypeChange(props.index,props.row)">
                        <el-option label="引用外键" :value="1"></el-option>
                        <el-option label="引用常量" :value="2"></el-option>
                        <el-option label="常规字段" :value="3"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="refTableName" label="关联表">
                <template slot-scope="props">
                    <div v-if="props.row.refType === 1">
                        <el-cascader style="width: 100%" clearable size="small" placeholder="请选择关联表/字段"
                                     v-model="props.row.ref"
                                     :options="refDataList"
                        ></el-cascader>
                    </div>
                    <div v-else-if="props.row.refType === 2">
                        <el-select v-model="props.row.refConstTypeKey" placeholder="请选择引用常量" clearable size="small"
                                   style="width: 100%">
                            <el-option v-for="item in constList" :key="item.id" :label="item.typeLabel" :value="item.typeKey"></el-option>
                        </el-select>
                    </div>
                    <div v-else>
                        --
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div slot="footer">
            <el-button @click="show=false">取 消</el-button>
            <el-button @click="save" type="primary">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>
    export default {
        props: ["refresh"],
        components: {},
        data: function () {
            return {
                show: false,
                fields: [],
                refTables: [],
                form: {
                    labelCol: null
                },
                rules: {
                    labelCol: []
                },
                refDataList: [],
                constList: []
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            showDialog(table) {
                this.form = table;
                this.initData();
                this.show = true;
            },
            initData() {
                this.loadFields();
                this.loadRefTables();
                this.loadConstList();
            },
            loadFields() {
                //加载属性
                const that = this;
                that.$post("/api/code/loadFields", {tableName: that.form.tableName}).then(res => {
                    if (res.code !== 0) {
                        this.$message.error(res.errmsg)
                        return;
                    }
                    that.fields = res.data.map(item => {
                        if (item.refTableName && item.refColName) {
                            item.ref = [item.refTableName, item.refColName];
                        }
                        if(item.refType === null){
                            item.refType = 3;
                        }
                        return item;
                    });
                });
            },
            loadRefTables() {
                //加载关联表
                const that = this;
                that.$post("/api/code/loadRefTables", {
                    tableName: that.form.tableName,
                    showConfigTable: true
                }).then(res => {
                    if (res.code !== 0) {
                        this.$message.error(res.errmsg)
                        return;
                    }
                    that.refTables = res.data;
                    that.refDataList = res.data.map(item => {
                        return {
                            label: item.tableName,
                            value: item.tableName,
                            children: item.fields.map(subItem => {
                                return {
                                    label: subItem,
                                    value: subItem
                                }
                            })
                        }
                    })
                });
            },
            loadConstList(){
                const that = this;
                that.$postBody("/api/alvinGenCodeConst/queryList",{}).then(res =>{
                    that.constList = res;
                })
            },
            onRefTypeChange(index,row){
                row.ref = [];
                row.refConstTypeKey = null;
            },
            save() {
                const requestData = {
                    ...this.form, fields: this.fields.map(item => {
                        if (item.refType === 0 && item.ref && item.ref.length === 2) {
                            return {...item, refTableName: item.ref [0], refColName: item.ref[1]}
                        }else if(item.refType === 1 && item.refConstTypeKey !== null){
                            return  {...item, refTableName: null, refColName: null}
                        }
                        return null;
                    }).filter(item => item !== null)
                }
                const that = this;
                that.$postBody("/api/code/tableSettings", requestData).then(res => {
                    if (res.code !== 0) {
                        that.$message.error(res.errmsg)
                        return;
                    }
                    that.refresh();
                    that.$message.success("设置成功")
                    that.show = false;
                })

            }
        },
    }
</script>
<style>

</style>
