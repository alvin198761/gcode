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
                                        编辑
                                    </el-button>
                                    <el-button
                                            type="text"
                                            size="mini"
                                    >
                                        刷新
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
                                <el-col :span="18">
                                    <img :src="'/api/template/getIcon?templateName='+ data.templateName + '&path='+data.path + '&type='+data.type"/>
                                    {{ node.label }}
                                </el-col>
                                <el-col :span="6">
                                    <el-button
                                            type="text"
                                            size="mini"
                                            @click="() => addFileDialog(node, data)"
                                            v-if="data.type==4 || data.type == -1"
                                    >
                                        添加文件
                                    </el-button>
                                    <el-button
                                            type="text"
                                            size="mini"
                                            @click="()=> doEditFile(node,data)"
                                            v-if="data.type != -1"
                                    >
                                        编辑
                                    </el-button>

                                    <el-button
                                            type="text"
                                            size="mini"
                                            @click="()=> deleteTemplate(node,data)" v-if="data.type != -1"
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
        <FileDialog ref="fileDialog" :refresh="refresh"></FileDialog>
        <EditContentFileDialog ref="contentDialog" :refresh="refresh"></EditContentFileDialog>
        <EditDirFileDialog ref="dirDialog" :refresh="refresh"></EditDirFileDialog>
        <EditUploadFileDialog ref="uploadDialog" :refresh="refresh"></EditUploadFileDialog>
        <EditURLFileDialog ref="urlDialog" :refresh="refresh"></EditURLFileDialog>
    </div>
</template>
<script>
    import TemplateDialog from './TemplateDialog.vue';
    import FileDialog from './FileDialog.vue';
    import EditContentFileDialog from './EditContentFileDialog.vue';
    import EditDirFileDialog from './EditDirFileDialog.vue';
    import EditUploadFileDialog from './EditUploadFileDialog.vue';
    import EditURLFileDialog from './EditURLFileDialog.vue';
    export default{
        components: {
            TemplateDialog,
            FileDialog,
            EditContentFileDialog,
            EditDirFileDialog,
            EditUploadFileDialog,
            EditURLFileDialog
        },
        data: function () {
            return {
                form: {},
                dataList: [],
                defaultProps: {
                    children: 'children',
                    label: 'name',
                },
                projectTreeData: []
            }
        },
        computed: {},
        created: function () {
            this.refresh(false);
        },
        methods: {
            refresh(clickFlag){
                const that = this;
                that.$http.get("/api/template/list").then(res => {
                    that.dataList = res.data;
                    if (clickFlag) {
                        for (let i = 0; i < that.dataList.length; i++) {
                            if (that.dataList[i].name == that.form.name) {
                                that.clickNode(that.dataList[i])
                                break;
                            }
                        }
                    } else {
                        that.form = {};
                    }
                }).catch(err => {
                    that.$message.error("获取模板列表失败：" + err);
                });
            },
            clickNode(node){
                this.form = node;
                this.projectTreeData = [{
                    name: node.name,
                    templateName: node.name,
                    children: this.toTreeNode(node.templateFiles, "-1", node.name),
                    id: "-1",
                    type: "-1"
                }]
            },
            toTreeNode(children, pid, templateName){
                const that = this;
//                debugger
                let resultList = [];
                for (let i = 0; i < children.length; i++) {
                    let item = children[i];
                    if (item.pid == null && pid == null) {
                        continue;
                    }
                    if (item.pid == null) {
                        continue;
                    }
                    if (item.pid == pid) {
                        if (item.type == 4) {
                            resultList.push({
                                ...item,
                                children: that.toTreeNode(children, item.id, templateName),
                                templateName: templateName,
                            });
                        } else {
                            resultList.push({
                                ...item,
                                templateName: templateName,
                            });
                        }

                    }
                }
                return resultList;
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
            addFileDialog(node, data){
                this.$refs["fileDialog"].addDialog(data);
            },
            deleteTemplate(node, data){
                const that = this;
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    that.$http.post("/api/template/deleteFile", JSON.stringify(data)).then(res => {
                        if (res.data == 1) {
                            that.refresh(true);
                            that.$message.success("删除成功")
                        } else {
                            that.$message.error("删除失败")
                        }
                    }).catch(res => {
                        that.$notify.error("删除出错：" + res);
                    });
                }).catch(() => {

                });
            },
            doEditFile(node, data){
                if (data.type == 4) {
                    this.$refs["dirDialog"].addDialog(data);
                    return;
                }
                if (data.contentType = "content") {
                    this.$refs["contentDialog"].addDialog(data);
                    return;
                }
                if (data.contentType = "file") {
                    this.$refs["uploadDialog"].addDialog(data);
                    return;
                }
                if (data.contentType = "url") {
                    this.$refs["urlDialog"].addDialog(data);
                    return;
                }
            }
        }
    }
</script>
<style>

</style>
