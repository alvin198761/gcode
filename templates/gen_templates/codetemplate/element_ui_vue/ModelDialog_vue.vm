/*${cName}新增与修改,作者:${auth},日期:${time}*/
<template>
      <el-dialog :title="title" :visible.sync="show" :close-on-click-modal="false" :close-on-press-escape="false">
            <el-form :model="form" ref="form" :rules="rules" label-width="100px">
                  <el-row>
                        <el-col>
                            #foreach($field in $fList)
                                <el-form-item label='${field.comment}' prop='${field.name}'>
                                    <el-input placeholder='请输入${field.comment}'  size="small" v-model='form.${field.name}'></el-input>
                                </el-form-item>
                            #end
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
        #foreach($field in $fList)
            ${field.name} :[
            {required: true, message: '请输入${field.comment}', trigger: 'blur'},
            // {min: 1, max: 10, message: '${field.comment}长度不正确', trigger: 'blur'},
        ],
        #end
        }
      }
    },
    methods: {
      save() {//新增及修改记录
        const that = this;
        this.${dollar}refs['form'].validate((valid) => {
              if (!valid) {
                return;
              }
              that.${dollar}http.post("/api/${lowUpp}/" + that.dialogMode, JSON.stringify(that.form)).then(res => {
                that.show = false;
                that.${dollar}message.success(that.title + "成功!");
                that.refresh();
              }).catch(res => {
                that.${dollar}message.error(that.title + "出错!" + res);
              });
        });
      },
      initForm() {//初始数据
        return {
          #foreach($field in $fList)
              ${field.name} : null,// ${field.comment}
          #end
        }
      },
      addDialog() {//新增
        this.title = "新增${cName}";
        this.dialogMode = "save";
        this.form = this.initForm();
        this.show = true;
      },
      editDialog(row) {//修改
        this.title = "修改${cName}";
        this.dialogMode = "update";
        this.form = {...row};
        this.show = true;
      },
    }

  }
</script>
<style></style>