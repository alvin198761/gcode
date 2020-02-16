/*库存记录模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/stock/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                prod_id: "@integer(100,200)",//商品
                                                weight : "@word(5,10)",// 重量
                                                weight_unit : "@word(5,10)",// 重量单位
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/stock/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/stock/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/stock/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                prod_id: "@integer(100,200)",//商品
                                                weight : "@word(5,10)",// 重量
                                                weight_unit : "@word(5,10)",// 重量单位
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/stock/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}