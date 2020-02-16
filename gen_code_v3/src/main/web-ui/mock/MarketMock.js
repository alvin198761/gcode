/*市场模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/market/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                vendor_name : "@word(5,10)",// 供应商名称
                                                address : "@word(5,10)",// 供应商地址
                                                zone : "@word(5,10)",// 市场
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

  'POST /api/market/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/market/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/market/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                vendor_name : "@word(5,10)",// 供应商名称
                                                address : "@word(5,10)",// 供应商地址
                                                zone : "@word(5,10)",// 市场
                                                remark : "@word(5,10)",// 备注
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/market/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}