/*档口模拟数据},作者:唐植超,日期:2020-02-02 22:46:05*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'POST /api/shops/queryPage': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                cus_no : "@word(5,10)",// 档口号
                                                m_id: "@integer(100,200)",//所属市场
                                                cus_type: "@integer(100,200)",//类型 1=客户 2=供应商
                                                remark : "@word(5,10)",// 备注
                                                cus_user : "@word(5,10)",// 联系人
                                                cus_phone : "@word(5,10)",// 联系电话
                          }],
      'number': '@integer(100,200)',
      'size': 10,
      'totalElements': 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'POST /api/shops/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/shops/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },

  'POST /api/shops/queryList': function (req, res, next) {
    var data = Mock.mock({
      'content|10': [{
                                    id: "@integer(100,200)",//主键
                                                cus_no : "@word(5,10)",// 档口号
                                                m_id: "@integer(100,200)",//所属市场
                                                cus_type: "@integer(100,200)",//类型 1=客户 2=供应商
                                                remark : "@word(5,10)",// 备注
                                                cus_user : "@word(5,10)",// 联系人
                                                cus_phone : "@word(5,10)",// 联系电话
                          }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },

  'POST /api/shops/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}