<template>
    <el-dialog :visible.sync="show">
        <div>This is a vue 2.x page</div>
    </el-dialog>
</template>
<script>

    export default{
        components: {},
        data: function () {
            return {
                show:false,
                form:{
                    author: '唐植超', //作者
                    packageName: 'org.alvin.home',

                }
            }
        },
        computed: {},
        created: function () {

        },
        methods: {}
    }
</script>
<style>

</style>
