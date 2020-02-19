/*入库单模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/inOrder/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                date: "@integer(100,200)",//入库日期
                                                remark : "@word(5,10)",// 备注
                                                shipping: "@integer(100,200)",//运费
                                                handling: "@integer(100,200)",//搬运费
                                                money: "@integer(100,200)",//货款
                                                status: "@integer(100,200)",//状态 0=未支付 1=完成 2=取消
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/inOrder/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/inOrder/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/inOrder/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                date: "@integer(100,200)",//入库日期
                                                remark : "@word(5,10)",// 备注
                                                shipping: "@integer(100,200)",//运费
                                                handling: "@integer(100,200)",//搬运费
                                                money: "@integer(100,200)",//货款
                                                status: "@integer(100,200)",//状态 0=未支付 1=完成 2=取消
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/inOrder/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}