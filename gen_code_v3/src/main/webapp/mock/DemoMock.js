/*体测仪管理,:author:gzz_gzz@163.com,DATE:2017-12-22 13:54:27*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

  'GET /gd/juiauto/invoicename' : function (req,res,next) {
    var data = [
      {
        "additionalProp1": {},
        "additionalProp2": {},
        "additionalProp3": {}
      }
    ];
    setTimeout(function () {
      res.json(data);
    }, 500);
  },

  'GET /api/demo/queryPage': function (req, res, next) {
    var data = Mock.mock({
      "content|10": [{
        branch_id: "@integer(1,10)",
        member_name: "@cname",
        member_no: "@word(5,10)",
        card_no: "@word(5,10)",
      }],
      "number": '@integer(100,200)',
      "size": 10,
      "totalElements": 500,
    });
    setTimeout(function () {
      res.json(data);
    }, 500);
  },
  'POST /api/demo/update': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
  'POST /api/demo/save': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
  'GET /api/demo/queryList': function (req, res, next) {
    var data = Mock.mock({
      "content|10": [{
        branch_id: "@integer(1,10)",
        member_name: "@cname",
        member_no: "@word(5,10)",
        card_no: "@word(5,10)",
      }]
    });
    setTimeout(function () {
      res.json(data.content);
    }, 500);
  },
  'DELETE /api/demo/delete': function (req, res, next) {
    setTimeout(function () {
      res.json({});
    }, 500);
  },
}
