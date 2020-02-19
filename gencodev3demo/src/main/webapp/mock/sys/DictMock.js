/*类型字典模拟数据},作者:唐植超,日期:2018-11-27 14:04:59*/
'use strict';
var Mock = require('mockjs')
var Random = Mock.Random;
module.exports = {

    'POST /api/dict/queryPage': function (req, res, next) {
        var data = Mock.mock({
            'content|10': [{
                id: "@integer(100,200)",//主键
                type: "@integer(100,200)",//type
                name: "@word(5,10)",// name
            }],
            'number': '@integer(100,200)',
            'size': 10,
            'totalElements': 500,
        });
        setTimeout(function () {
            res.json(data);
        }, 500);
    },

    'POST /api/dict/update': function (req, res, next) {
        setTimeout(function () {
            res.json({});
        }, 500);
    },

    'POST /api/dict/save': function (req, res, next) {
        setTimeout(function () {
            res.json({});
        }, 500);
    },

    'POST /api/dict/queryList': function (req, res, next) {
        var data = Mock.mock({
            'content|10': [{
                id: "@integer(100,200)",//主键
                type: "@integer(100,200)",//type
                name: "@word(5,10)",// name
            }]
        });
        setTimeout(function () {
            res.json(data.content);
        }, 500);
    },

    'DELETE /api/dict/delete': function (req, res, next) {
        setTimeout(function () {
            res.json({});
        }, 500);
    },

    'GET /api/dict/fetchCompany': function (req, res, next) {
        setTimeout(function () {
            res.json({
                address: "测试地址",
                name: "测试公司",
                head: "负责人呵呵"
            });
        }, 500);
    },
    'POST /api/dict/saveCompany': function (req, res, next) {
        setTimeout(function () {
            res.json();
        }, 500);
    },
}