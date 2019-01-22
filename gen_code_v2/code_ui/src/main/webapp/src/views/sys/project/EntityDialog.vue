<template>
    <el-dialog title="字段编辑" :visible.sync="show" width="80%">
        <el-row>
            <el-col :span="6">类名:<b>{{entity.name}}</b></el-col>
            <el-col :span="6">&nbsp;</el-col>
            <el-col :span="6">注释:<b>{{entity.remark}}</b></el-col>
            <el-col :span="6">&nbsp;</el-col>
        </el-row>
        <hr/>
        <el-table :data="entity.fields">
            <el-table-column prop="name" label="属性名">
                <template slot-scope="props">
                    <el-input v-model="props.row.name" size="small"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="150">
                <template slot-scope="props">
                    <el-select size="small" v-model="props.row.type" placeholder="请选择类型"
                               @change="(val) =>  changeType(val,props.row)">
                        <el-option
                                v-for="item in types"
                                :key="item.value"
                                :label="item.label"
                                :value="item">
                        </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="ref_name" label="引用类" width="150">
                <template slot-scope="props">
                    <el-select v-model="props.row.ref_name" :disabled="props.row.type != 'ref'"
                               @change="(val) => changeRefName(val,props.row)" placeholder="请选择类型引用类">
                        <el-option size="small"
                                   v-for="item in project.entitys"
                                   :key="item.name"
                                   :label="item.name"
                                   :value="item.name">
                            <span style="float: left">{{ item.name }}({{item.type == 0 ? '实体类' : '字典常量'}})</span>
                        </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="length" label="长度" width="200">
                <template slot-scope="props">
                    <el-input-number v-model="props.row.length" :disabled="props.row.type != 'java.lang.String'"
                                     :min="0" :max="999999" size="small"
                                     @change="(val) => changeLength(val,props.row)"></el-input-number>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="注释">
                <template slot-scope="props">
                    <el-input size="small" v-model="props.row.remark"></el-input>
                </template>
            </el-table-column>
            <el-table-column prop="isNull" label="为空" width="50">
                <template slot-scope="props">
                    <el-checkbox v-model="props.row.isNullChecked"></el-checkbox>
                </template>
            </el-table-column>
            <el-table-column label="主键" width="50">
                <template slot-scope="props">
                    <el-checkbox v-model="props.row.isPrimaryKeyChecked"></el-checkbox>
                </template>
            </el-table-column>
            <el-table-column label="标签" width="50">
                <template slot-scope="props">
                    <el-checkbox v-model="props.row.isLabelChecked"></el-checkbox>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template slot-scope="props">
                    <el-button type="text" size="small" @click="removeArray(entity.fields,props.row)">删除</el-button>
                    <el-button type="text" size="small" @click="entity.fields.push({})">追加</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div slot="footer">
            <!--<el-button @click="show=false">取 消</el-button>-->
            <el-button type="primary" @click="saveEntity">确 定</el-button>
        </div>
    </el-dialog>
</template>
<script>
    import {remove} from '../../../utils/ArraysUtils';

    export default{
        props: ["saveProject"],
        components: {},
        data: function () {
            return {
                show: false,
                form: {
                    //属性名称
                    name: null,
                    //属性类型
                    type: null,
                    //引用类型对应的实体
                    ref_name: null,
                    //列名称
                    col_name: null,
                    //列备注
                    remark: null,
                    //数据长度
                    length: 50,
                    //数据库类型（包含长度）
                    sql_type: null,
                    //是否为空
                    isNull: 'NULL'
                },
                rules: {
                    name: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                    ],

                },
                project: {},
                entity: {},
                oldName: null,
                types: [
                    {
                        value: 'java.lang.String',
                        label: 'java.lang.String',
                        db_type: 'VARCHAR',
                        length: 50
                    },
                    {
                        value: 'java.lang.Long',
                        label: 'java.lang.Long',
                        db_type: 'BIGINT(11)',
                    },
                    {
                        value: 'java.lang.Byte',
                        label: 'java.lang.Byte',
                        db_type: 'TINYINT',
                    },
                    {
                        value: 'java.lang.Integer',
                        label: 'java.lang.Integer',
                        db_type: 'INT(4)',
                    },
                    {
                        value: 'java.lang.Boolean',
                        label: 'java.lang.Boolean',
                        db_type: 'BIT',
                    },
                    {
                        value: 'java.lang.Float',
                        label: 'java.lang.Float',
                        db_type: 'FLOAT',
                    },
                    {
                        value: 'java.util.Date',
                        label: 'java.util.Date',
                        db_type: 'DATETIME',
                    },
                    {
                        value: 'ref',
                        label: 'ref'
                    },
                ]
            }
        },
        computed: {
            refTypes(){
                if (this.project == null) {
                    return []
                }
                if (this.project.entities == null) {
                    return [];
                }
                let list = this.project.entities.map(item => {
                    return {
                        value: item[item.idName].type,
                        label: item[item.labelName].name
                    }
                })
                console.log(list)
                return list;
            }

        },
        created: function () {

        },
        methods: {
            editDialog(project, entity){
                this.project = project;
                this.entity = {...entity};
                this.oldName = entity.name;
                if (this.entity.fields == null || this.entity.fields.length == 0) {
                    this.entity.fields = [{}];
                }

                this.show = true;
            },
            removeArray (_arr, _obj) {
                remove(_arr, _obj);
                if (_arr.length == 0) {
                    _arr.push({});
                }
            },
            saveEntity(){
                const that = this;
                if(this.entity.fields.length <= 1){
                    this.$message.error("实体类不能只有一个字段")
                    return ;
                }
                for(let i = 0 ; i < this.entity.fields.length ;i++ ){
                    let fi =  this.entity.fields[i];
                    if(fi.name == null ||fi.name.length == 0){
                        this.$message.error("属性名不能为空")
                        return ;
                    }
                    for(let j = 0 ; j <  this.entity.fields.length ;j++){
                        if(j == i){
                            continue;
                        }
                        let cfi =  this.entity.fields[j];
                        if(cfi.name == fi.name){
                            this.$message.error("属性名不能重名")
                            return ;
                        }
                    }
                    if(fi.type == null){
                        this.$message.error("属性类型不能为空")
                        return ;
                    }
                    if(fi.type == "java.lang.String" && fi.length == null){
                        this.$message.error("属性长度不能为空")
                        return ;
                    }
                    if(fi.type == "ref" && fi.ref_name == null){
                        this.$message.error("引用类不能为空")
                        return ;
                    }
                    if(fi.remark == null ||fi.remark.length == 0){
                        this.$message.error("注释不能为空")
                        return ;
                    }
                }
                let idField = null;
                this.entity.fields = this.entity.fields.map(field => {
                    field.isNull = field.isNullChecked ? "NULL" : "NOT NULL";
                    if (field.isPrimaryKeyChecked == true) {
                        that.entity.idName = field.name;
                       idField = field;
                    }
                    if (field.isLabelChecked == true) {
                        that.entity.labalName = field.name;
                    }
                    return field;
                });
                if(this.entity.idName == null){
                    this.$message.error("主键必须不能为空")
                    return ;
                }
                if(idField.type != "java.lang.Long" && idField.type != 'java.lang.Integer'){
                    this.$message.error("主键只能选 java.lang.Long ,java.lang.Integer 类型" )
                    return ;
                }
                if(this.entity.labalName == null){
                    this.$message.error("标签不能为空")
                    return ;
                }
                for (let i = 0; i < this.project.entitys.length; i++) {
                    let entity = this.project.entitys[i];
                    if (entity.name == this.entity.name) {
                        this.project.entitys.splice(i, 1, this.entity);
                        break;
                    }
                }
                this.saveProject(this.project);
                this.show = false;
            },
            changeType(type, row){
                if (type.value == 'java.lang.String') {
                    if (row.length == null) {
                        row.length = 50;
                    }
                    row.sql_type = type.db_type + "(" + row.length + ")"
                } else {
                    row.sql_type = type.db_type
                }
                row.type = type.value;
            },
            changeRefName(ref, row){
                if (ref.type == 1) {
                    row.sql_type = "VARCHAR(100)";
                } else {
                    row.sql_type = "BIGINT(11)";
                }
            },
            changeLength(len, row){
                if (row.value == 'java.lang.String') {
                    row.sql_type = "VARCHAR(" + len + ")"
                }
            }
        },


    }
</script>
<style>

</style>
