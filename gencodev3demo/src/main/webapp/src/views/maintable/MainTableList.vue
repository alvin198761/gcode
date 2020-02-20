/*主表管理,作者:唐植超,日期:2020-02-20 20:18:37*/
<template>
      <div>
            <el-form :inline="true">
                				                				                    <el-form-item label="显示文本">
												<el-input placeholder="请输入显示文本" size="small" v-model="form.mainTitle" clearable style="width: 100%"></el-input>
						                    </el-form-item>
					                				                    <el-form-item label="a表主键">
												<!-- aId -->
							<el-select placeholder="请输入a表主键" size="small" v-model="form.aId" clearable style="width: 100%">
								<el-option
										v-for="item in aIdFkList"
										:key="item.value"
										:label="item.label"
										:value="item.value">
								</el-option>
							</el-select>
						                    </el-form-item>
					                				                    <el-form-item label="b表主键">
												<!-- bId -->
							<el-select placeholder="请输入b表主键" size="small" v-model="form.bId" clearable style="width: 100%">
								<el-option
										v-for="item in bIdFkList"
										:key="item.value"
										:label="item.label"
										:value="item.value">
								</el-option>
							</el-select>
						                    </el-form-item>
					                				                    <el-form-item label="日期">
												<el-date-picker size="small" v-model="form.date" type="daterange" range-separator="至"  start-placeholder="日期开始" end-placeholder="日期结束" clearable style="width: 100%"></el-date-picker>
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
												  <el-form-item label="显示文本">{{props.row.main_title}}</el-form-item>
											  </el-col>
										                                        										  										  <el-col :span="6">
												  <el-form-item label="a表主键">{{props.row.a_id}}</el-form-item>
											  </el-col>
										                                        										  										  <el-col :span="6">
												  <el-form-item label="b表主键">{{props.row.b_id}}</el-form-item>
											  </el-col>
										                                        										  											  <el-col :span="6">
												  <el-form-item label="日期">{{props.row.date |date_filter}}</el-form-item>
											  </el-col>
										                                                                              </el-row>
                                </el-form>
                        </template>
                  </el-table-column>
                								  <el-table-column prop="id" label="主键"></el-table-column>
				                      
                								  <el-table-column prop="main_title" label="显示文本"></el-table-column>
				                      
                								  <el-table-column prop="a_id" label="a表主键"></el-table-column>
				                      
                								  <el-table-column prop="b_id" label="b表主键"></el-table-column>
				                      
                								<el-table-column label="操作" width="150">
                    <template slot-scope="props">
					{{props.row.date |date_filter}}
					  </template>
                  </el-table-column>
				                      
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
        <MainTableDialog ref="dialog" :refresh="refresh"></MainTableDialog>
  </div>
</template>
<script>
  import MainTableDialog from './MainTableDialog.vue';
  import moment from 'moment';
  export default {
      components: { MainTableDialog },
      data: function () {
      return {
        total: 0,
        page: 1,
        size: 20,
        dataList: [],
        form: {
        							//主键不输出
			        							mainTitle : null,// 显示文本
			        							aId : null,// a表主键
			        							bId : null,// b表主键
			        							date : [moment(),moment()],// 日期
			                },
        loading: false,
																		aIdFkList:[],
											bIdFkList:[],
										      }
    },
    computed: {},
    created: function () {
      this.refresh();
	  			  			  					this.loadAIdFkList();
			  					this.loadBIdFkList();
			  			      },
    methods: {
	 										loadAIdFkList(){
			const that = this;
			that.$postBody("/api/aaTable/queryLabelList",{}).then(res =>{
				if(res.code != 0){
					that.$message.error(res.errmsg);
					return ;
				}
				that.aIdFkList = res.data;
			}).catch(err => {
				that.$message.error("获取数据出错:" + res);
			})
		},
							loadBIdFkList(){
			const that = this;
			that.$postBody("/api/bbTest/queryLabelList",{}).then(res =>{
				if(res.code != 0){
					that.$message.error(res.errmsg);
					return ;
				}
				that.bIdFkList = res.data;
			}).catch(err => {
				that.$message.error("获取数据出错:" + res);
			})
		},
						      refresh() {
        const that = this;
        that.loading = true;
		const requestData = {...that.form, page: that.page - 1, size: that.size};
																												requestData.date = null;
				if (that.form.date && that.form.date.length === 2) {
					requestData.dateStart=that.form.date[0];
					requestData.dateEnd=that.form.date[1];
				}
					        
        that.$postBody("/api/mainTable/queryPage", requestData).then(res => {
			  that.loading = false;
			  that.dataList = res.data.content;
			  that.total = res.data.totalElements;
			}).catch(res => {
			  that.$message.error("获取主表列表失败：" + res);
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
          that.$post("/api/mainTable/delete", {id: row.id}).then(res => {
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