/*
 用axios封装Ajax请求返回promise类型数据
 */
import axios from 'axios'
import qs from 'qs'
import {MessageBox} from 'element-ui';


'use strict'  //"use strict" 的目的是指定代码在严格条件下执行。 严格模式下你不能使用未声明的变量。
// axios interceptors 拦截器中添加headers 属性
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem("kccd_token");
        config.headers['Accept'] = 'application/json, text/plain, */*';
        config.headers['Content-Type'] = "application/json;charset=utf-8";
        config.headers['Authorization'] = token;
        config.headers["CLIENT-PLATFORM"] = "webapp";
        config.headers["CLIENT-VERSION"] = "1.0";
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
        return config
    },
    error => {
        return Promise.reject(error)//es6 报错回调
    }
)

function parseData(data) {
    var obj = {};
    for (var p in data) {
        if (!data[p]) {
            continue;
        }
        obj[p] = data[p];
    }
    return obj;

}

//全局 导出 表格的请求方法
export function postByExport(url, data = {}) {
    return new Promise((resolve, reject) => {
        axios({
            url: url,
            method: 'post',
            data: qs.stringify(parseData(data)),
            responseType: 'blob',
        }).then(response => {
            resolve(response)
        }, error => {
            reject(error)

        })
    })
}

//全局 导出 表格的请求方法
export function postBodyByExport(url, data = {}) {
    return new Promise((resolve, reject) => {
        axios({
            url: url,
            method: 'post',
            headers: {'content-type': 'application/json;charset=UTF-8'},
            data: JSON.stringify(data),
            responseType: 'blob',
        }).then(response => {
            resolve(response)
        }, error => {
            reject(error)

        })
    })
}

export function post(url, data = {}) {
    return new Promise((resolve, reject) => {
        axios({
            url: url,
            method: 'post',
            data: qs.stringify(parseData(data)),
        }).then(response => {
            if (response.data.code === 105) {
                window.location.href = "/#/login";
                return;
            }
            if (response.data.errcode == 301) {
                MessageBox.alert('回话过期，请重新登录', '提示', {
                    confirmButtonText: '确定',
                    callback: function () {
                        window.location.href = '/';
                    }
                });
            } else {
                resolve(response.data)
            }
        }, error => {
            reject(error)
        })
    })
}

export function postBody(url, data = {}) {
    return new Promise((resolve, reject) => {
        axios({
            url: url,
            method: 'post',
            headers: {'content-type': 'application/json'},
            data: JSON.stringify(data),
        }).then(response => {
            console.log(response);
            // debugger
            if (response.data.code === 105) {
                window.location.href = "/#/login";
                return;
            }
            if (response.data.errcode == 301) {
                MessageBox.alert(response.data.replydata.msg, '提示', {
                    confirmButtonText: '确定',
                    callback: function () {
                        window.location.href = '/';
                    }
                });
            } else {
                resolve(response.data)
            }
        }, error => {
            reject(error)
        })
    })
}

//其他情况
export const http = axios.create({});
