<template>
    <div>
        <div>
            <el-row :gutter="20">
                <el-col :span="6">
                    <el-card>
                        <div slot="header">
                            <span>模板列表</span>
                            <el-button style="float: right;  margin-right: 5px; " icon="el-icon-refresh"
                                       @click="refresh"
                                       size="small" title="刷新"></el-button>
                            <el-button style="float: right; margin-right: 5px; " icon="el-icon-plus" size="small"
                                       type="primary" title="新增模板" @click="newTemplate"></el-button>
                        </div>
                        <el-tree
                                :data="dataList"
                                :props="defaultProps"
                                default-expand-all
                                @node-click="clickNode"
                                ref="templateTree">
                            <el-row slot-scope="{ node, data }" style="width: 100%">
                                <el-col :span="18">{{ node.label }}</el-col>
                                <el-col :span="6">
                                    <el-button
                                            type="text"
                                            size="mini"
                                    >
                                        保存
                                    </el-button>
                                    <el-button
                                            type="text"
                                            size="mini"
                                    >
                                        删除
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-tree>
                    </el-card>
                </el-col>
                <el-col :span="18">
                    <el-card>
                        <div slot="header">
                            <span>模板信息</span>
                            <el-button style="float: right;  margin-right: 5px; " icon="el-icon-refresh"
                                       size="small" title="刷新"></el-button>
                        </div>
                        <el-tree
                                :data="projectTreeData"
                                :props="defaultProps"
                                default-expand-all
                                ref="templateSettingTree">
                            <el-row slot-scope="{ node, data }" style="width: 100%">
                                <el-col :span="18">{{ node.label }}</el-col>
                                <el-col :span="6">
                                    <el-button
                                            type="text"
                                            size="mini"
                                            @click="() => addFileDialog(node, data)"
                                    >
                                       添加文件
                                    </el-button>
                                    <el-button
                                            type="text"
                                            size="mini"
                                    >
                                        编辑
                                    </el-button>
                                    <el-button
                                            type="text"
                                            size="mini"
                                    >
                                        删除
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-tree>
                    </el-card>
                </el-col>
            </el-row>
        </div>
        <TemplateDialog ref="dialog" :saveTemplate="saveTemplate"></TemplateDialog>
        <FileDialog ref="fileDialog"></FileDialog>
    </div>
</template>
<script>
    import TemplateDialog from './TemplateDialog.vue';
    import FileDialog from './FileDialog.vue';
    export default{
        components: {TemplateDialog,FileDialog},
        data: function () {
            return {
                form: {},
                dataList: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                projectTreeData: []
            }
        },
        computed: {

        },
        created: function () {
            this.refresh();
        },
        methods: {
            refresh(){
                const that = this;
                that.$http.get("/api/template/list").then(res => {
                    that.dataList = res.data;
                    that.form = {};
                }).catch(err => {
                    that.$message.error("获取模板列表失败：" + res);
                });
            },
            clickNode(node){
                this.form = node;
                this.projectTreeData =  [{
                    name: node.name,
                    templateName: node.name,
                    children: this.toTreeNode(node.templateFiles ,"-1",node.name),
                    id: "-1"
                }]
            },
            toTreeNode(children,pid,templateName){
                const that = this;
                return children.filter(item => {
                    if(item.pid == null && pid == null){
                        return true;
                    }
                    if(item.pid == null){
                        return false;
                    }
                    return item.pid == pid;
                }).map(item =>{
                    return {
                        name :item.name,
                        children: that.toTreeNode(children,item.id,templateName),
                        templateName: templateName,
                    }
                });
            },
            newTemplate(){
                this.$refs["dialog"].addDialog();
            },
            saveTemplate(template){
                const that = this;
                that.$http.post("/api/template/save", JSON.stringify(template)).then(res => {
                    that.$message.success("保存成功");
                    that.refresh();
                }).catch(err => {
                    that.$message.error("保存失败:" + err);
                });
            },
            addFileDialog(node,data){
                this.$refs["fileDialog"].addDialog(data);
            }
        }
    }
</script>
<style>

</style>
