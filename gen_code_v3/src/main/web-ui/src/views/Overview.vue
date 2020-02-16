<template>
    <div>
        <el-form :inline="true">

            <el-form-item label="时间">
                <el-date-picker size="small"
                                v-model="searchDate"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button :disabled="!searchDate || searchDate.length != 2" icon="search" title="根据输入的条件查询"
                           size="small" @click="refresh">查询
                </el-button>
                <!--<el-button type="primary" icon="plus"   title="添加" size="small">添加</el-button>-->
            </el-form-item>
        </el-form>
        <el-tabs type="card">
            <el-tab-pane label="财务信息">
                <el-row :gutter="10" style="padding: 10px;">
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.totalOut}}元
                                <h4>总支出</h4></div>
                        </el-card>

                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.totalIn}}元
                                <h4>总收入</h4></div>
                        </el-card>

                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.payment}}元
                                <h4>待付货款</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.out_shipping}}元
                                <h4>待付运费</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.out_handling}}元
                                <h4>待付搬运</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.pending}}元
                                <h4>待收款</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.supply}}元
                                <h4>进货总额</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.sell}}元
                                <h4>销售总额</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.backOrder}}元
                                <h4>退单总额</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.shipping}}元
                                <h4>已付运费</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.handing}}元
                                <h4>已付搬运</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.freight}}元
                                <h4>辅料</h4></div>
                        </el-card>
                    </el-col>
                    <el-col :span="3" style="margin-top:10px">
                        <el-card shadow="hover">
                            <div style="text-align: center">
                                {{financial.other}}元
                                <h4>其他支出</h4></div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>
        <br/>
        <el-tabs type="card">
            <el-tab-pane label="商品统计">
                <el-table :data="dataList" v-loading="loading" element-loading-text="正在加载......" style="width: 100%">
                    <el-table-column prop="id" label="主键"></el-table-column>
                    <el-table-column prop="goods_name" label="商品名称"></el-table-column>
                    <el-table-column prop="spec_no" label="规格型号"></el-table-column>
                    <el-table-column prop="rem_weight" label="剩余数量(公斤)"></el-table-column>
                    <el-table-column prop="total_in_weight" label="进货量(公斤)"></el-table-column>
                    <el-table-column prop="total_out_weight" label="出货量(公斤)"></el-table-column>
                    <el-table-column prop="total_back_weight" label="退货量(公斤)"></el-table-column>
                    <el-table-column prop="total_damaged_weight" label="损坏(公斤)"></el-table-column>
                    <el-table-column prop="total_out_money" label="进货总额(元)"></el-table-column>
                    <el-table-column prop="total_in_money" label="出货总额(元)"></el-table-column>
                    <el-table-column prop="total_back_money" label="退货总额(元)"></el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>

    </div>

</template>

<script>
    // @ is an alias to /src

    export default {
        name: 'home',
        components: {},
        data() {
            return {
                searchDate: [new Date(), new Date()],
                financial: {},
                loading: false,
                dataList: []
            }
        },
        created() {
            this.refresh()
        },
        methods: {
            refresh() {
                this.loadFinancial();
                this.loadGoods();
            },
            loadFinancial() {
                const that = this;
                that.$http.post("api/staticstics/financial", JSON.stringify({
                    startTime: that.searchDate[0],
                    endTime: that.searchDate[1]
                })).then(res => {
                    that.financial = res.data;
                    for(let p in res.data){
                        if(res.data[p] == null){
                            that.financial[p] = 0;
                        }

                    }
                });
            },
            loadGoods(){
                const that = this;
                that.$http.post("api/staticstics/goods", JSON.stringify({
                    startTime: that.searchDate[0],
                    endTime: that.searchDate[1]
                })).then(res => {
                    that.dataList = res.data.map(item =>{
                        for(let p in item){
                            if(item[p] == null){
                                item[p] = 0;
                            }
                        }
                        return item;
                    });


                });
            }
        }
    }
</script>
