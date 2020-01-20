import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import installFilter from  './utils/filters';
installFilter(Vue);

import {post,postBody} from './utils/http'
Vue.prototype.$post = post;
Vue.prototype.$postBody = postBody;

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
// npm cache clean --force
// template not found
//第一步执行：git config --global url.“https://”.insteadOf git://
// 第二步执行：npm install
