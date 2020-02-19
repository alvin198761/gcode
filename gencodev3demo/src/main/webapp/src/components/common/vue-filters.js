import moment from 'moment';

export const genderFilter = (gender) => {
    if (gender == 1) {
        return '男'
    }
    if (gender == 2) {
        return '女'
    }
    if (gender == 3) {
        return '未知'
    }
    if (gender == 4) {
        return '保密'
    }
    return "";
}


function data_format(time) {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD")
}

function data_time_format(time) {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD HH:mm:ss")
}

function category_status_format(status) {
    if (status == 0) {
        return "未使用";
    }
    if (status == 1) {
        return "已使用";
    }
    return "丢失"
}


function order_status_format(status){
    if(status == 0){
        return "未支付"
    }
    if(status == 1){
        return  "支付成功"
    }
    if(status == 2){
        return "取消";
    }
    if(status == 3){
        return "部分支付";
    }
}

export const weightUnit = ["斤","公斤"];

function weightUnitFormat(unit) {
    return weightUnit[unit-1];
}


function payModeFormat(pay) {
    if(pay == 1){
        return "现金"
    }
    if(pay == 2){
        return "微信";
    }
    if(pay == 3){
        return "支付宝"
    }
    if(pay == 4){
        return "银行卡";
    }
}

function payTypeFormat(payType) {
    if(payType == 1){
        return  "收款";
    }if(payType == 2){
        return "收款";
    }
    if(payType == 3){
        return "退货减收";
    }
    return "--"
}

function payCategoryFormart(category) {
    if(category === 1){
        return "进货款"
    }
    if(category === 2){
        return "进货运费"
    }
    if(category === 3){
        return "进货搬运费"
    }
    if(category === 4){
        return "出货收款"
    }
    if(category === 5){
        return "辅料进货"
    }
    return  "其他";
}

export default function (Vue) {
    Vue.filter('gender_filter', genderFilter);
    Vue.filter('date_filter', data_format);
    Vue.filter('date_time_filter', data_time_format);
    Vue.filter('category_status_filter', category_status_format);
    Vue.filter('order_status_filter', order_status_format);
    Vue.filter('weight_unit_filter', weightUnitFormat);
    Vue.filter('pay_mode_filter', payModeFormat);
    Vue.filter('pay_type_filter', payTypeFormat);
    Vue.filter('pay_category_filter', payCategoryFormart);
}
