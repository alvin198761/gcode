import Vue from "vue";
import Router from "vue-router";
import RouterViewPage from "./views/RouterViewPage.vue";
import CodeList from  './views/gencode/CodeList.vue';
import ConstList from './views/alvingencodeconst/AlvinGenCodeConstList.vue';
Vue.use(Router)
export const constantRoutes = [
    {
        path: '/',
        component: RouterViewPage,
        name: '首页',
        redirect: '/codelist',
        children: [
            {
                path: 'codelist',
                component: CodeList,
                name: 'CodeList',
                meta: {title: '数据库表', icon: 'el-icon-c-scale-to-original'}
            },
            {
                path: 'constlist',
                component: ConstList,
                name: 'ConstList',
                meta: {title: '常量字典表', icon: 'el-icon-c-scale-to-original'}
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
