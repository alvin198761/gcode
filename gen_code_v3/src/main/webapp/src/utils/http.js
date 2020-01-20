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
    const token = sessionStorage.getItem("mj_auth_token");
    config.headers['Accept'] = 'application/json, text/plain, */*';
    config.headers['Content-Type'] = "application/json;charset=utf-8";
    config.headers['Authorization'] = token;
    config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
    return config
  },
  error => {
    return Promise.reject(error)//es6 报错回调
  }
)

//添加全局post 方法
export function postByJson(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios({
      url: url,
      method: 'post',
      data: data,
    }).then(response => {
      if (response.data.errcode == 301) {
        MessageBox.alert({
          title: '系统提示',
          message: response.data.errmsg,
          showCancelButton: false,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            window.location.href = "#/login"
          }
        })
      } else {
        resolve(response.data)
      }
    }, error => {
      reject(error)
    })
  })
}

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
      headers: {'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'},
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
      let r = new FileReader();
      r.onload = function () {
        try {
          let resData = JSON.parse(this.result);
          if (resData && resData['errcode'] && resData['errcode'] === 300) {
            MessageBox.alert({
              title: '系统提示',
              message: response.data.errmsg,
              showCancelButton: false,
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              beforeClose: (action, instance, done) => {
                window.location.href = "#/login"
              }
            })
          }
        } catch (e) {
          resolve(response)
        }
      }
      r.readAsText(response.data);
    }, error => {
      reject(error)
    })
  })
}

export function post(url, data = {}) {
  const that = this;
  return new Promise((resolve, reject) => {
    axios({
      url: url,
      method: 'post',
      data: qs.stringify(parseData(data)),
    }).then(response => {
      if (response.data.errcode == 301) {
        MessageBox.alert({
          title: '系统提示',
          message: response.data.replydata.msg,
          showCancelButton: false,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            window.location.href = "#/login"
          }
        })
      } else if (response.data.errcode == 300) {
        MessageBox.alert({
          title: '系统提示',
          message: response.data.replydata.msg,
          showCancelButton: false,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            window.location.href = "#/login"
          }
        })
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
      if (response.data.errcode == 301) {
        MessageBox.alert({
          title: '系统提示',
          message: response.data.replydata.msg,
          showCancelButton: false,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            window.location.href = "#/login"
          }
        })
      } else if (response.data.errcode == 300) {
        MessageBox.alert({
          title: '系统提示',
          message: response.data.replydata.msg,
          showCancelButton: false,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          beforeClose: (action, instance, done) => {
            window.location.href = "#/login"
          }
        })
      } else {
        resolve(response.data)
      }
    }, error => {
      reject(error)
    })
  })
}

//其他情况
export const http = axios.create({

  // validateStatus: function (status) {
  //     return status == 200 || status == 400;
  // }
});
