/*
author : ${project.author}
github: https://github.com/alvin198761/code
data: ${project.date}

*/
CREATE DATABASE ${project.name} DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use ${project.name};

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for `base_dict`
-- ----------------------------
DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict` (
 `id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
 `type_name` VARCHAR(100) DEFAULT NOT NULL COMMENT '类型名称',
 `type_value` VARCHAR(100) DEFAULT NOT NULL COMMENT '类型值',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='dict';

#foreach($cls in $project.entitys)
#if($cls.type == 0)
-- ----------------------------
-- Table structure for `${cls.name}`
-- ----------------------------
DROP TABLE IF EXISTS `${cls.name}`;
CREATE TABLE `${cls.name}` (
    #foreach($field in $cls.fields)
        #if($field.name == $cls.idName)
        `${field.name}` ${field.sql_type} NOT NULL AUTO_INCREMENT COMMENT '${field.remark}',
        #end
         #if($field.name != $cls.idName)
        `${field.name}` ${field.sql_type} DEFAULT ${field.isNull} COMMENT '${field.remark}',
        #end
    #end
PRIMARY KEY (`$cls.idName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='${cls.remark}';
#end
#end



