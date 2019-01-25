<template>
    <el-dialog title="目录编辑" :visible.sync="show" width="30%">
        <el-form :model="form" :rules="rules" ref="form" label-width="100px">
            <el-form-item label="文件名称" prop="name">
                <el-input v-model="form.name" placeholder="输入文件名" size="small"
                          autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button @click="show = false" size="small">取 消</el-button>
            <el-button type="primary" @click="save" size="small">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>

    export default{
        props: ["refresh"],
        components: {},
        data: function () {
            return {
                show: false,
                form: this.initForm(),
                rules: {},
            }
        },
        computed: {},
        created: function () {

        },
        methods: {
            initForm(){
                return {
                    //文件名称
                    name: '',
                    //文件类型 1 普通模板 2 实体模板 3 非模板 4 目录
                    type: null,
                    //相对项目模板文件夹的位置，相对路径
                    path: '',
                    //模板名称
                    templateName: '',
                    //内容
                    content: '',
                    //父节点名称
                    pid: '',
                    //当前节点id
                    id: '',
                    contentType: 'content'
                }
            },
            save(){

            },
            addDialog(data){
                this.show = true;
            },

        }
    }
</script>
<style>

</style>
