package org.alvin.code.v2.core.utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

import java.io.File;

public class AntUtil {
    /**
     * 导入sql
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param sqlFile
     */
    public static void importSql(String driver, String url, String username, String password, String sqlFile) {
        SQLExec exec = new SQLExec();
        exec.setDriver(driver);
        exec.setUrl(url);
        exec.setUserid(username);
        exec.setPassword(password);
        //
        exec.setSrc(new File(sqlFile));
        exec.setPrint(true);
        exec.setProject(new Project());
        exec.execute();
    }
}
