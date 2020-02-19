/*商品模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/goods/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_name : "@word(5,10)",// 商品名称
                                                spec_no : "@word(5,10)",// 规格型号
                                                note : "@word(5,10)",// 产品说明
                                                unit : "@word(5,10)",// 单位
                                                pic1 : "@word(5,10)",// 产品图片
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

  'POST /api/goods/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/goods/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/goods/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                goods_name : "@word(5,10)",// 商品名称
                                                spec_no : "@word(5,10)",// 规格型号
                                                note : "@word(5,10)",// 产品说明
                                                unit : "@word(5,10)",// 单位
                                                pic1 : "@word(5,10)",// 产品图片
                                                remark : "@word(5,10)",// 备注
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/goods/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}