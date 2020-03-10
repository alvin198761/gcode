/*测试表B管理,作者:唐植超,日期:2020-02-20 22:28:50*/
<template>
<div>
      <el-drawer :title="title" :visible.sync="show" show-close modal-append-to-body destroy-on-close append-to-body close-on-press-escape  :wrapperClosable="false" :close-on-click-modal="false" :close-on-press-escape="false" size="60%">
            <el-form :model="form" ref="form" :rules="rules" label-width="100px">
																						 
				 <el-row>
					<el-col>
						<el-form-item label="标题" prop="title">
														<el-input placeholder="请输入标题" size="small" v-model="form.title" clearable style="width: 100%"></el-input>
													</el-form-item>
					</el-col>
				</el-row>
								                </el-form>
		  <div  style="text-align: right;display: flex;">
              <el-button @click="show = false">取消</el-button>
              <el-button type="primary" @click="save()">确定</el-button>
            </div>
          </el-drawer>
		  </div>
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
		// 长度示例 {min: 1, max: 10, message: '长度不正确', trigger: 'blur'},
        							//主键不处理
												title :[
				{required: true, message: '请输入标题', trigger: 'blur'},      
				],
					        },
												      }
    },
    methods: {
								  initData(){
		//初始化数据
													  },
      save() {//新增及修改记录
        const that = this;
        that.$refs['form'].validate((valid) => {
              if (!valid) {
                return;
              }
              that.$postBody("/api/bbTest/" + that.dialogMode, that.form).then(res => {
                that.show = false;
				if(res.code != 0){
					that.$message.error(res.errmsg);
					return ;
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
                        id : null,// 主键
                        title : null,// 标题
                  }
      },
      addDialog() {//新增
        this.title = "新增测试表B";
        this.dialogMode = "save";
        this.form = this.initForm();
		this.initData();
        this.show = true;
      },
      editDialog(row) {//修改
        this.title = "修改测试表B";
        this.dialogMode = "update";
        this.form = {...row};
		this.initData();
        this.show = true;
      },
    }

  }
</script>
<style></style>