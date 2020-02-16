/*支付记录模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/payRecord/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                type: "@integer(100,200)",//类型 1=收款 2=付款
                                                pay_mode: "@integer(100,200)",//支付方式 1=现金 2=微信 3=支付宝 4=银行卡
                                                pay_money: "@integer(100,200)",//支付金额
                                                pay_time: "@integer(100,200)",//支付时间
                                                pay_user: "@integer(100,200)",//支付人
                                                order_no: "@integer(100,200)",//对应订单
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/payRecord/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/payRecord/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/payRecord/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                type: "@integer(100,200)",//类型 1=收款 2=付款
                                                pay_mode: "@integer(100,200)",//支付方式 1=现金 2=微信 3=支付宝 4=银行卡
                                                pay_money: "@integer(100,200)",//支付金额
                                                pay_time: "@integer(100,200)",//支付时间
                                                pay_user: "@integer(100,200)",//支付人
                                                order_no: "@integer(100,200)",//对应订单
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/payRecord/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}