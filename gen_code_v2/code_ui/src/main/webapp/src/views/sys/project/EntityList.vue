<template>
    <div>
        <el-table :data="form.entitys" border
                  size="small">
            <el-table-column prop="type" label="类型"   width="180" placeholder="请选择类型" >
                <template slot-scope="props">
                    <el-select size="small" v-model="props.row.type">
                        <el-option label="实体类" :value="0"></el-option>
                        <el-option label="字典常量" :value="1"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="类名" placeholder="只能字母数字下划线" >
                <template slot-scope="props">
                    <el-input v-model="props.row.name" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="注释">
                <template slot-scope="props">
                    <el-input v-model="props.row.remark" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="props">
                    <el-button type="text" size="small" @click="editDialog(props.row)" :disabled="props.row.type == 1">编辑属性
                    </el-button>
                    <el-button type="text" size="small"
                               @click="removeArray(form.entitys,props.row)">删除
                    </el-button>
                    <el-button type="text" size="small" @click="form.entitys.push({})">追加
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <ProjectDialog ref="dialog" :saveProject="save"></ProjectDialog>
    </div>

</template>
<script>
    import ProjectDialog from './EntityDialog.vue';
    import {remove} from '../../../utils/ArraysUtils';
    export default{
        props: ["form", "save"],
        components: {ProjectDialog},
        data: function () {
            return {}
        },
        computed: {},
        created: function () {

        },
        methods: {
            editDialog(row){
                this.$refs["dialog"].editDialog(this.form, row);
            },
            removeArray (_arr, _obj) {
                remove(_arr, _obj);
                if (_arr.length == 0) {
                    _arr.push({});
                }
            },
        }
    }
</script>
<style>

</style>
