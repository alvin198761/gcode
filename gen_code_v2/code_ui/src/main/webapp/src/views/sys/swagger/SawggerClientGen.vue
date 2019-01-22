<script src="../../../../../../mock_gen_dir/app-auth-action.js"></script>
<template>
    <div>
        <el-form ref="form" :model="form" :rules="rules" inline>
            <el-form-item prop="search">
                <el-input type="text" v-model="form.search" size="small" placeholder="名称/前端过滤">
                </el-input>
            </el-form-item>
            <el-form-item prop="url">
                <el-input type="text" v-model="form.url" size="small" placeholder="app center 地址" style="width: 300px">
                </el-input>
            </el-form-item>
            <el-form-item prop="ctype">
                <el-select v-model="form.ctype" placeholder="请选择客户端类型" size="small">
                    <el-option label="reqwest js" value="Reqwest"></el-option>
                    <el-option label="axios js" value="Axios"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="refresh" size="small">刷新</el-button>
                <el-button type="primary" @click="genCode" :disabled="selectionRows.length == 0" size="small">生成
                </el-button>
            </el-form-item>
        </el-form>
        <hr/>
        <el-table :data="getData" ref="selection" class="tabClass" @selection-change="selectionData" border
                  size="small">
            <el-table-column type="selection" width="40px"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column prop="description" label="表描述名"></el-table-column>
        </el-table>
    </div>
</template>
<script>

    export default{
        data: function () {
            return {
                form: {
                    search: '',
                    url: 'http://localhost:8003/v2/api-docs',
                },
                rules: {
                    url: [{required: true, message: '请输入访问地址', trigger: 'blur'}
                    ],
                    ctype: [{required: true, message: '请选择客户端类型', trigger: 'blur'}
                    ],
                },
                columns: [{
                    type: 'selection',
                    width: 60,
                    align: 'center'
                }, {
                    title: 'controller 名称',
                    key: 'name'
                }, {
                    title: '描述',
                    key: 'description'
                }
                ],
                data: [],
                selectionRows: []
            }
        },
        computed: {
            getData(){
                if (this.data.length == 0) {
                    return this.data;
                }
                if (this.form.search == '') {
                    return this.data;
                }
                const search = this.form.search;
                return this.data.filter(item => {
                    return item.name.indexOf(search) != -1 || item.description.indexOf(search) != -1;
                });
            }
        },
        created: function () {

        },
        methods: {
            refresh(){
                const that = this;
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        that.$http.post("/api/msiteMock/queryList", JSON.stringify(that.form)).then(res => {
                            that.data = res.data;
                        });
                    }
                })
            },
            genCode(){
                const that = this;
                this.$refs['form'].validate((valid) => {
                    if (valid) {
                        const requestData = {
                            url: that.form.url,
                            token: that.form.token,
                            tags: that.selectionRows,
                            ctype: that.form.ctype
                        }
                        that.$http.post("/api/msiteMock/gencode", JSON.stringify(requestData)).then(res => {
                            location.href = "/api/msiteMock/download?filePath=" + res.data;
                        });
                    }
                });
            },
            selectionData(selectRows){
                this.selectionRows = selectRows;
            }
        },
        components: {}
    }
</script>
<style>

</style>
