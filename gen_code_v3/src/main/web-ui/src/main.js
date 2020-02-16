import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './icons';
import VueHighlightJS from 'vue-highlightjs'
import 'highlight.js/styles/googlecode.css'

Vue.use(VueHighlightJS)
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import NProgress from 'nprogress'//页面顶部进度条
import installFilter from './components/common/vue-filters';
import installPlugins from './components/common/vue-plugins';

import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import './styles/element-variables.scss'

import './styles/index.scss' // global css
import {post,postBody} from './utils/http'
Vue.prototype.$post = post;
Vue.prototype.$postBody = postBody;

Vue.use(ElementUI);
installFilter(Vue);
installPlugins(Vue);

Vue.config.productionTip = true;

router.beforeEach((to, from, next) => {
    NProgress.start();
    next()
})

router.afterEach(transition => {
    NProgress.done();
});

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
