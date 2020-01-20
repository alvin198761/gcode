import moment from 'moment';
//性别过滤
export const genderFilter = (gender) => {
    if (gender === 1) {
        return '男'
    }
    if (gender === 2) {
        return '女'
    }
    if (gender === 3) {
        return '未知'
    }
    if (gender === 4) {
        return '保密'
    }
    return "";
};
//金额,数字格式化
export const number_format = (number, decimals, dec_point, thousands_sep) =>{
    // console.log(number)
    /*
　　 * 参数说明：
    * number：要格式化的数字
    * decimals：保留几位小数
    * dec_point：小数点符号
    * thousands_sep：千分位符号
    * */
    number = (number + '').replace(/[^0-9+-Ee.]/g, '');
    var n = !isFinite(+number) ? 0 : +number,
      prec = !isFinite(+decimals) ? 2 : Math.abs(decimals),
      sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
      dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
      s = '',
      toFixedFix = function(n, prec) {
          var k = Math.pow(10, prec);
          return '' + Math.ceil(n * k) / k;
      };

    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    var re = /(-?\d+)(\d{3})/;
    while(re.test(s[0])) {
        if('' == sep){
            s[0] = s[0].replace(re, "$1" + "$2");
            break;
        }else{
            s[0] = s[0].replace(re, "$1" + sep + "$2");
        }

    }

    if((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
};

export const datatimeformat = (time) => {
    if(!time){
        return time;
    }
    return new moment(time).format("YYYY-MM-DD HH:mm:ss")
}
//日期格式化，自定义日期，时间分隔符
export const dataTimeFormatCustomSep = (time,dateSep,timeSep) => {
    if(!time){
        return time;
    }
    return new moment(time).format("YYYY" + dateSep + "MM" + dateSep +  "DD HH" + timeSep + "mm"+ timeSep + "ss")
}
export const datetime_Noconnection_format = (time) => {
    if(!time){
        return time;
    }
    return new moment(time , "YYYYMMDDHHmmss").format("YYYY-MM-DD HH:mm:ss")
    // return new moment(time).format("YYYYMMDDHHmmss")
}

export const datatimeformat2 = (time) => { //没有秒
    if (time != null)
        return new moment(time).format("YYYY-MM-DD HH:mm")
}

//日期格式化
export const dataformatYMD = (time) => {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD")
}

export const datatimeformatday = (time) => {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD")
}
//日期格式化
function data_format(time) {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD")
}
//时间格式化
function date_time_format(time) {
    if (time != null)
        return new moment(time).format("YYYY-MM-DD HH:mm:ss")
}

export default function (Vue) {
    Vue.filter('gender_filter', genderFilter);
    Vue.filter('data_format', data_format);
    Vue.filter('date_time_filter', date_time_format);
    Vue.filter('data_time_format', datatimeformat);
    Vue.filter('datetime_noconnection_format', datetime_Noconnection_format);
    Vue.filter('number_format', number_format);
}
