<template>
  <div class="app-container">
    <el-button type="primary" @click="addData">添加</el-button>
    <el-button   @click="refresh">刷新</el-button>

    <el-table :data="rolesList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="Role Key" width="220">
        <template slot-scope="scope">
          {{ scope.row.key }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="Role Name" width="220">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column align="header-center" label="Description">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="Operations">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="modifyData(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteBtn(scope)">删除</el-button>
          <el-button type="danger" size="small" @click="deleteBtn(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
  import {queryTables} from '../../api/GenCodeApi';
  import {getPermission} from '../../api/PermissionApi';
  export default {
    data() {
      return {
        loading: false, //加载
        dataList: [], //数据
        page: {
          num: 1, //当前页码
          total: 0, //总条数
          pageSize: 10, //当前条数
        },
        search: { //查询条件

        },
        btnPermission: { //按钮权限控制
          addBtn: false,
          delBtn: false,
          modifyBtn: false,
          detailBtn: false
        }
      }
    },
    computed: {},
    created() {
      this.refresh();
    },
    methods: {
      refresh(){ //分页查询
        this.loading = true;
        queryTables().then(res =>{
          if(res.code != 0){
            this.$message({
              showClose: true,
              message: res.errmsg,
              type: 'error'
            });
          }
        });
      },
      addData(){ //添加

      },
      modifyData(props){ //修改数据

      },
      detailData(props){ //详情界面

      },
      deleteBtn(props){ //删除操作

      }
    }
  }
</script>
<style>

</style>
