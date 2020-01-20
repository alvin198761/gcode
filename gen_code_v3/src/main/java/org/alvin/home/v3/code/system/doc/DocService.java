package org.alvin.home.v3.code.system.doc;

import lombok.extern.slf4j.Slf4j;
import org.alvin.home.v3.code.system.gencode.GenCodeDaoV3;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author 唐植超
 * @date 2020/01/19
 */
@Slf4j
@Service
public class DocService {
    @Autowired
    private GenCodeDaoV3 genCodeDaoV3;
//    https://www.cnblogs.com/Crysta1/p/10250270.html
    public void genDoc(String dbName, String outDir) throws IOException {
        int row = 0;
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
        HSSFSheet sheet = workbook.createSheet("数据库表"); // 创建工作表

        List<DocTable> tables = genCodeDaoV3.getDocTables();
        for (DocTable table : tables) {
            HSSFRow rowm = sheet.createRow(row);
            rowm.createCell(0).setCellValue("表名");
            rowm.createCell(1).setCellValue(table.getTable_name());
            rowm.createCell(3).setCellValue("描述");
            rowm.createCell(4).setCellValue(table.getTable_comment());
            row++;// 表头
            rowm = sheet.createRow(row);
            rowm.createCell(0).setCellValue("字段名");
            rowm.createCell(1).setCellValue("字段描述");
            rowm.createCell(2).setCellValue("数据类型");
            rowm.createCell(3).setCellValue("可为空");
            rowm.createCell(4).setCellValue("是主键");
            rowm.createCell(5).setCellValue("规则");
            List<DocField> docFields = genCodeDaoV3.getDocFields(table.getTable_name());
            for (DocField field : docFields) {
                row++;// 字段
                HSSFRow rowf = sheet.createRow(row);
                rowf.createCell(0).setCellValue(field.getColumn_name());
                rowf.createCell(1).setCellValue(field.getColumn_comment());
                rowf.createCell(2).setCellValue(field.getColumn_type());
                rowf.createCell(3).setCellValue(field.getIs_nullable());
                rowf.createCell(4).setCellValue(field.getPri());
                rowf.createCell(5).setCellValue("无");
            }
            row++;
        }
        workbook.write(new File(outDir + "/" + dbName + "数据库.xls"));
        workbook.close();
    }
}

