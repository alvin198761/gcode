<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
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
            <el-col :span="16">
                <el-form  v-if="form != null" :model="form" :rules="rules" label-width="100px">
                    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
                        <el-tab-pane label="基本信息" name="basic">
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="项目名称" prop="name">
                                        <el-input v-model="form.name" placeholder="只能字母数字下划线" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="源目录" prop="base_package">
                                        <el-input v-model="form.srcDir" placeholder="项目根目录算起如果: /src/main/java" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="项目作者" prop="author">
                                        <el-input v-model="form.author" placeholder="请输入项目作者" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col>
                                    <el-form-item label="项目描述" prop="remark">
                                        <el-input v-model="form.remark" placeholder="请输入项目描述" type="textarea" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>

                        </el-tab-pane>
                        <el-tab-pane label="数据库配置" name="dbconfig">
                            <el-row>
                                <el-col :span="24">
                                    <el-form-item label="驱动名称:"   >{{form.dbConfig.driverName}}</el-form-item>
                                </el-col>
                                <el-col :span="24">
                                    <el-form-item label="连接地址"  >
                                        <el-input v-model="form.dbConfig.url" placeholder="只能字母数字下划线" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="用户名" >
                                        <el-input v-model="form.dbConfig.username" placeholder="请输入用户名" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12" >
                                    <el-form-item label="密码"  >
                                        <el-input v-model="form.dbConfig.password" placeholder="请输入密码" size="small"
                                                  autocomplete="off" type="password"></el-input>
                                    </el-form-item>
                                </el-col>

                            </el-row>
                        </el-tab-pane>
                        <el-tab-pane label="自定义属性" name="dynAttrs">
                            <el-row v-for="(item,index) in form.dynAttrs">
                                <el-col :span="7">
                                    <el-form-item label="属性名称">
                                        <el-input v-model="form.dynAttrs[index].name" placeholder="请输入属性名称" size="small"
                                                  autocomplete="off"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="7">
                                    <el-form-item label="属性类型">
                                        <el-select v-model="form.dynAttrs[index].type">
                                            <el-option :value="1" label="整数"></el-option>
                                            <el-option :value="2" label="浮点数"></el-option>
                                            <el-option :value="3" label="字符"></el-option>
                                            <el-option :value="4" label="日期"></el-option>
                                            <el-option :value="5" label="布尔"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="7">
                                    <el-form-item label="属性值">
                                        <el-input-number v-if="form.dynAttrs[index].type === 1" v-model="form.dynAttrs[index].value"></el-input-number>
                                        <el-input-number v-if="form.dynAttrs[index].type === 2" v-model="form.dynAttrs[index].value"></el-input-number>
                                        <el-input v-if="form.dynAttrs[index].type === 3" v-model="form.dynAttrs[index].value"></el-input>
                                        <el-date-picker type="datetime" v-if="form.dynAttrs[index].type === 4" v-model="form.dynAttrs[index].value" ></el-date-picker>
                                        <el-radio-group v-if="form.dynAttrs[index].type === 5" >
                                            <el-radio :label="true">是</el-radio>
                                            <el-radio :label="false">否</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="3"  >
                                    <el-button style="margin-left: 5px" type="danger" icon="el-icon-delete" circle size="mini" title="删除" @click="form.dynAttrs.splice(index,1)"></el-button>
                                </el-col>
                            </el-row>
                            <el-button size="small" @click="form.dynAttrs.push({name: null , type: 3 ,value: null})">添加属性</el-button>
                        </el-tab-pane>
                    </el-tabs>
                    <br/>
                    <EntityList :form="form" :save="save"></EntityList>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>
<script>

    import EntityList from './EntityList.vue';
    export default{
        components: {            EntityList},
        data: function () {
            return {
                dataList: [],
                loading: false,
                templateName: '',
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                form: this.initForm(),
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
                activeName: 'basic'
            }
        },
        computed: {},
        created: function () {
            this.refresh();
        },
        methods: {
            refresh(){
                this.form = null;
                const that = this;
                that.loading = true;
                this.$http.post("/api/project/list", JSON.stringify({})).then(res => {
                    that.dataList = res.data.map(pro => {
                        if (pro.entities == null || pro.entities.length == 0) {
                            pro.entities = [{}];
                        }
                        pro.entities = pro.entities.map(entity => {
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
                for(let i = 0 ; i < project.entities.length ;i++){
                    let entity = project.entities[i];
                    if(entity.type == null){
                        this.$message.error("实体类型不能为空");
                        return ;
                    }
                    if(entity.name == null || entity.name.length == 0){
                        this.$message.error("实体名称不能为空");
                        return ;
                    }
                    for(let j = 0 ; j < project.entities.length;j++){
                        if(j == i){
                            continue;
                        }
                        let centity = project.entities[j];
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
                this.form = this.initForm();
                this.dataList.push(this.form);
            },
            handleClick(e){
                console.log(e);
            },
            initForm(){
                return {
                    name: 'test',
                    srcDir: '',
                    author: '',
                    date: null, //创建日期
                    entities: [{type: 0}], //实体类列表
                    remark: '', //备注,
                    dbConfig: {
                        driverName: "com.mysql.jdbc.Driver",
                        url:"jdbc:mysql://${db_ip}:${db_port}/${db_name}?useUnicode=true&characterEncoding=utf-8&useSSL=false",
                        username:"root",
                        password:"root"
                    },
                    dynAttrs:[]
                };
            }
        }
    }
</script>
<style>

</style>
