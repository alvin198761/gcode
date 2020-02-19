/*测试表B管理,作者:唐植超,日期:2020-02-19 23:26:37*/
<template>
      <div>
            <el-form :inline="true">
                				                				                    <el-form-item label="标题">
												<el-input placeholder="请输入标题" size="small" v-model="form.title" clearable style="width: 100%"></el-input>
						                    </el-form-item>
					                				  <el-form-item>
                        <el-button icon="search" @click="refresh" title="根据输入条件查询" size="small">查询</el-button>
                        <el-button icon="plus"   @click="doAdd()" title="添加" size="small" type="primary" >添加</el-button>
                      </el-form-item>
                </el-form>
            <el-table :data="dataList" v-loading="loading" element-loading-text="正在加载......" style="width: 100%">
              <el-table-column type="expand">
                    <template slot-scope="props">
                            <el-form>
                                  <el-row :gutter="10">
                                      										  										  <el-col :span="6">
												  <el-form-item label="主键">{{props.row.id}}</el-form-item>
											  </el-col>
										                                        										  										  <el-col :span="6">
												  <el-form-item label="标题">{{props.row.title}}</el-form-item>
											  </el-col>
										                                                                              </el-row>
                                </el-form>
                        </template>
                  </el-table-column>
                								  <el-table-column prop="id" label="主键"></el-table-column>
				                      
                								  <el-table-column prop="title" label="标题"></el-table-column>
				                      
                              <el-table-column label="操作" width="150">
                    <template slot-scope="props">
                          <div>
                                <el-button type="text" @click="doEdit(props.row)">编辑</el-button>
                                <el-button type="text" @click="doDelete(props.row)">删除</el-button>
                              </div>
                        </template>
                  </el-table-column>
            </el-table>
            <br/>
            <div style="text-align: right" v-if="total > 0">
              <el-pagination small layout="total,sizes,prev, pager, next" :current-page="page" :total="total" @current-change="(curr) => {this.page = curr ; this.refresh();}"
               :page-sizes="[10, 15, 20, 100]" @size-change="(s) => {this.size = s ; this.refresh();}" :page-size="size"></el-pagination>
            </div>
        <BbTestDialog ref="dialog" :refresh="refresh"></BbTestDialog>
  </div>
</template>
<script>
  import BbTestDialog from './BbTestDialog.vue';
  import moment from 'moment';
  export default {
      components: { BbTestDialog },
      data: function () {
      return {
        total: 0,
        page: 1,
        size: 20,
        dataList: [],
        form: {
        							//主键不输出
			        							title : null,// 标题
			                },
        loading: false,
												      }
    },
    computed: {},
    created: function () {
      this.refresh();
	  			  			      },
    methods: {
	 						      refresh() {
        const that = this;
        that.loading = true;
		const requestData = {...that.form, page: that.page - 1, size: that.size};
												        
        that.$postBody("/api/bbTest/queryPage", requestData).then(res => {
			  that.loading = false;
			  that.dataList = res.data.content;
			  that.total = res.data.totalElements;
			}).catch(res => {
			  that.$message.error("获取测试表B列表失败：" + res);
			  that.loading = false;
			});
      },
      doAdd() {
        this.$refs["dialog"].addDialog();
      },
      doEdit(row) {
        this.$refs["dialog"].editDialog(row);
      },
      doDelete(row) {
        const that = this;
        this.$confirm('你确定要删除吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.$post("/api/bbTest/delete", {id: row.id}).then(res => {
				this.$message.success("删除成功");
				that.refresh();
			  }).catch(res => {
				that.$message.error("删除失败：" + res);
			  });
        }).catch(() => {
			 
        });
      }
    }
  }
</script>
<style></style>