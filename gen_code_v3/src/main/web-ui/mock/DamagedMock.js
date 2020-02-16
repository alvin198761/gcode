/*损坏记录模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/damaged/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_id: "@integer(100,200)",//商品id
                                                weight: "@integer(100,200)",//重量
                                                price: "@integer(100,200)",//进货单价
                                                weight_unit: "@integer(100,200)",//重量单位
                                                time: "@integer(100,200)",//时间
                                                remark : "@word(5,10)",// 备注
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/damaged/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/damaged/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/damaged/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_id: "@integer(100,200)",//商品id
                                                weight: "@integer(100,200)",//重量
                                                price: "@integer(100,200)",//进货单价
                                                weight_unit: "@integer(100,200)",//重量单位
                                                time: "@integer(100,200)",//时间
                                                remark : "@word(5,10)",// 备注
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/damaged/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}