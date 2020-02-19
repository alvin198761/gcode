import Vue from "vue";
import Router from "vue-router";
import RouterViewPage from "./views/RouterViewPage.vue";
import AaTableList from  './views/aatable/AaTableList.vue';
import BbTestList from  './views/bbtest/BbTestList.vue';
import MainTableList from  './views/maintable/MainTableList.vue';
Vue.use(Router)
export const constantRoutes = [
    {
        path: '/',
        component: RouterViewPage,
        name: '首页',
        redirect: '/aa',
        children: [
            {
                path: 'aa',
                component: AaTableList,
                name: 'AaTableList',
                meta: {title: 'A表', icon: 'el-icon-c-scale-to-original'}
            },
            {
                path: 'bb',
                component: BbTestList,
                name: 'BbTestList',
                meta: {title: 'B表', icon: 'el-icon-c-scale-to-original'}
            },
            {
                path: 'mm',
                component: MainTableList,
                name: 'MainTableList',
                meta: {title: 'M表', icon: 'el-icon-c-scale-to-original'}
            }
        ]
    }
]

export const asyncRoutes = [
    {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
    scrollBehavior: () => ({y: 0}),
    routes: constantRoutes
})
const router = createRouter();
export default router;
