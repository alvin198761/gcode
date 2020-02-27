/*测试A管理,作者:唐植超,日期:2020-02-20 22:28:50*/
<template>
<div>
      <el-drawer :title="title" :visible.sync="show" show-close modal-append-to-body destroy-on-close append-to-body close-on-press-escape  :wrapperClosable="false" :close-on-click-modal="false" :close-on-press-escape="false" size="60%">
            <el-form :model="form" ref="form" :rules="rules" label-width="100px">
																						 
				 <el-row>
					<el-col>
						<el-form-item label="提示文本" prop="tip">
														<el-input placeholder="请输入提示文本" size="small" v-model="form.tip" clearable style="width: 100%"></el-input>
													</el-form-item>
					</el-col>
				</el-row>
																		 
				 <el-row>
					<el-col>
						<el-form-item label="日期" prop="date">
														<el-date-picker size="small" v-model="form.date" type="date" placeholder="请输入日期" clearable style="width: 100%"></el-date-picker>
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
												tip :[
				{required: true, message: '请输入提示文本', trigger: 'blur'},      
				],
												date :[
				{required: true, message: '请输入日期', trigger: 'blur'},      
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
              that.$postBody("/api/aaTable/" + that.dialogMode, that.form).then(res => {
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
                        tip : null,// 提示文本
                        date : null,// 日期
                  }
      },
      addDialog() {//新增
        this.title = "新增测试A";
        this.dialogMode = "save";
        this.form = this.initForm();
		this.initData();
        this.show = true;
      },
      editDialog(row) {//修改
        this.title = "修改测试A";
        this.dialogMode = "update";
        this.form = {...row};
		this.initData();
        this.show = true;
      },
    }

  }
</script>
<style></style>