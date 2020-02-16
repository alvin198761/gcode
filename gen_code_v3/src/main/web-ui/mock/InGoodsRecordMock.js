/*入库商品记录模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/inGoodsRecord/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_id: "@integer(100,200)",//入库商品
                                                price: "@integer(100,200)",//单价
                                                num: "@integer(100,200)",//数量
                                                vendor_id: "@integer(100,200)",//来源档口
                                                unit: "@integer(100,200)",//单位
                                                total_proce: "@integer(100,200)",//总价
                                                ref_id: "@integer(100,200)",//入库ID
                                                weight: "@integer(100,200)",//重量
                                                weight_unit: "@integer(100,200)",//重量单位
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/inGoodsRecord/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/inGoodsRecord/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/inGoodsRecord/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_id: "@integer(100,200)",//入库商品
                                                price: "@integer(100,200)",//单价
                                                num: "@integer(100,200)",//数量
                                                vendor_id: "@integer(100,200)",//来源档口
                                                unit: "@integer(100,200)",//单位
                                                total_proce: "@integer(100,200)",//总价
                                                ref_id: "@integer(100,200)",//入库ID
                                                weight: "@integer(100,200)",//重量
                                                weight_unit: "@integer(100,200)",//重量单位
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/inGoodsRecord/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}