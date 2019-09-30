package org.alvin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateRange {

    private Date begin; //开始时间

    private Date end; //结束时间

    //只是为了方便查看,实际开发中不要这个鬼
    public String toString() {
        return "{" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getBegin()) + " ," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getEnd()) + "}";
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}