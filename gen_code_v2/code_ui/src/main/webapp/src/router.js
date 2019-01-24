import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import TableListView from './views/sys/code/TableListView.vue';
import SawggerClientGen from './views/sys/swagger/SawggerClientGen.vue';
import ProjectGen from './views/sys/project/ProjectGen.vue';
import TemplateList from './views/sys/template/TemplateList.vue';

Vue.use(Router)


export default new Router({
    routes: [
        {
            path: '/',
            component: Home,
            name: '首页',
            redirect: '/code',
            children: [
                {
                    path: 'code', component: TableListView, name: '代码生成',
                },
                {
                    path: 'swagger', component: SawggerClientGen, name: 'Swagger 客户端'
                },
                {
                    path: 'project', component: ProjectGen, name: '项目设计'
                },
                {
                    path: 'template', component: TemplateList, name: '模板设计'
                }
            ]
        }
    ]
})
