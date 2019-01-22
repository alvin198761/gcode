<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="6">
                <el-card>
                    <div slot="header">
                        <span>项目列表</span>
                        <el-button style="float: right;  margin-right: 5px; " icon="el-icon-refresh" @click="refresh"
                                   size="small" title="刷新"></el-button>
                        <el-button style="float: right; margin-right: 5px; " icon="el-icon-plus" size="small"
                                   type="primary" title="新增项目" @click="newProject"></el-button>
                    </div>
                    <el-tree
                            :data="dataList"
                            :props="defaultProps"
                            default-expand-all
                            @node-click="clickNode"
                            ref="projectTree">
                        <el-row slot-scope="{ node, data }" style="width: 100%">
                            <el-col :span="18">{{ node.label }}</el-col>
                            <el-col :span="6">
                                <el-button
                                        type="text"
                                        size="mini"
                                        @click="() => save(data)">
                                    保存
                                </el-button>
                                <el-button
                                        type="text"
                                        size="mini"
                                        @click="() => genproject(data)">
                                    生成
                                </el-button>
                                <el-button
                                        type="text"
                                        size="mini"
                                        @click="() => deleteProject(data)">
                                    删除
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-tree>
                </el-card>
            </el-col>
            <el-col :span="18">
                <el-form :model="form" :rules="rules" label-width="100px">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="项目名称" prop="name">
                                <el-input v-model="form.name" placeholder="只能字母数字下划线" size="small"
                                          autocomplete="off"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="项目包名" prop="base_package">
                                <el-input v-model="form.base_package" placeholder="必须小写，如： org.alvin.test" size="small"
                                          autocomplete="off"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="项目作者" prop="author">
                                <el-input v-model="form.author" placeholder="请输入项目作者" size="small"
                                          autocomplete="off"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col>
                            <el-form-item label="项目描述" prop="remark">
                                <el-input v-model="form.remark" placeholder="请输入项目描述" type="textarea" size="small"
                                          autocomplete="off"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <EntityList :form="form" :save="save"></EntityList>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>
<script>

    import EntityList from './EntityList.vue';
    export default{
        components: {EntityList},
        data: function () {
            return {
                dataList: [],
                loading: false,
                templateName: '',
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                form: {
                    name: '',
                    base_package: '',
                    author: '',
                    date: null, //创建日期
                    entitys: [], //实体类列表
                    remark: '' //备注
                },
                rules: {
                    name: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                    ],
                    base_package: [
                        {required: true, message: '请输入项目包名', trigger: 'blur'},
                    ],
                    author: [
                        {required: true, message: '请输入项目作者', trigger: 'blur'},
                    ]
                },
            }
        },
        computed: {},
        created: function () {
            this.refresh();
        },
        methods: {
            refresh(){
                this.form = {entitys: [{type:0}]};
                const that = this;
                that.loading = true;
                this.$http.post("/api/project/list", JSON.stringify({})).then(res => {
                    that.dataList = res.data.map(pro => {
                        if (pro.entitys == null || pro.entitys.length == 0) {
                            pro.entitys = [{}];
                        }
                        pro.entitys = pro.entitys.map(entity => {
                            if (entity.fields == null || entity.fields.length == 0) {
                                entity.fields = [{}];
                            }
                            entity.fields = entity.fields.map(field => {
                                field.isNullChecked = field.isNull == 'NULL'
                                field.isPrimaryKeyChecked = entity.idName == field.name;
                                field.isLabelChecked = entity.labalName == field.name;
                                return field;
                            });
                            return entity;
                        })
                        return pro;
                    });
                    that.loading = false;
                }).catch(res => {
                    that.$message.error("获取项目列表失败：" + res)
                    that.loading = false;
                })
            },
            clickNode(data){
                this.form = data;
            },
            save(project){
                const that = this;
                for(let i = 0 ; i < project.entitys.length ;i++){
                    let entity = project.entitys[i];
                    if(entity.type == null){
                        this.$message.error("实体类型不能为空");
                        return ;
                    }
                    if(entity.name == null || entity.name.length == 0){
                        this.$message.error("实体名称不能为空");
                        return ;
                    }
                    for(let j = 0 ; j < project.entitys.length;j++){
                        if(j == i){
                            continue;
                        }
                        let centity = project.entitys[j];
                        if(centity.name == entity.name){
                            this.$message.error("实体名称不能重复");
                            return ;
                        }
                    }
                    if(entity.remark == null || entity.remark.length == 0){
                        this.$message.error("实体注释不能为空");
                        return ;
                    }
                }
                that.$http.post("/api/project/save", JSON.stringify(project)).then(res => {
                    this.$message.success("保存成功");
                }).catch(res => {
                    this.$message.error("保存出错");
                });
            },
            genproject(project){
                const  that = this;
                that.$http.post("/api/project/genProject",JSON.stringify(project)).then(res =>{
                    this.$message.success("生成项目成功");
                }).catch(err =>{
                    this.$message.error("生成项目出错");
                });
            },
            deleteProject(project){
                const that = this;
                this.$confirm('项目将彻底删除，你确认吗？', '确认信息', {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '确认',
                    cancelButtonText: '放弃'
                })
                    .then(() => {
                        console.log(project)
                        that.$http.post("/api/project/delete", JSON.stringify(project)).then(res => {
                            if (project == that.form) {
                                that.form = {};
                            }
                            that.dataList.splice(that.dataList.indexOf(project), 1)
                        }).catch(err => {
                            this.$message.error("删除项目出错");
                        });
                    })
                    .catch(action => {

                    });
            },
            newProject(){
                this.form = {name: 'test', entitys: [{ type: 0}]}
                this.dataList.push(this.form);
            }
        }
    }
</script>
<style>

</style>
