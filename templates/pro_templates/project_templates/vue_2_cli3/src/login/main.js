import Vue from "vue";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import Login from './Login.vue';
import installPlugins from '../views/common/vue-plugins';

Vue.use(ElementUI);

Vue.config.productionTip = true;
installPlugins(Vue);


new Vue({

    render: h => h(Login)
}).$mount('#app')
