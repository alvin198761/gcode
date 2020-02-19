/*退货记录模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/backOrder/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                shops_id: "@integer(100,200)",//档口id
                                                goods_id: "@integer(100,200)",//商品id
                                                num: "@integer(100,200)",//数量
                                                unit: "@integer(100,200)",//单位
                                                weight: "@integer(100,200)",//重量
                                                weight_unit: "@integer(100,200)",//重量单位
                                                status: "@integer(100,200)",//状态
                                                ref_outorder_id: "@integer(100,200)",//关联出库单ID
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

  'POST /api/backOrder/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/backOrder/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/backOrder/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                shops_id: "@integer(100,200)",//档口id
                                                goods_id: "@integer(100,200)",//商品id
                                                num: "@integer(100,200)",//数量
                                                unit: "@integer(100,200)",//单位
                                                weight: "@integer(100,200)",//重量
                                                weight_unit: "@integer(100,200)",//重量单位
                                                status: "@integer(100,200)",//状态
                                                ref_outorder_id: "@integer(100,200)",//关联出库单ID
                                                remark : "@word(5,10)",// 备注
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/backOrder/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}