package org.alvin.code.gen.beans;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间段
 */
@Data
public class DateRange {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Date begin; //开始时间

    private Date end; //结束时间

    //只是为了方便查看,实际运行中最好不要这个鬼
    public String toString() {
        return "{" + format.format(begin) + " ," + format.format(end) + "}";
    }

    /**
     * 包含关系
     *
     * @param c
     * @return
     */
    public boolean contains(DateRange c) {
        return this.getBegin().before(c.getBegin()) && this.getEnd().after(c.getEnd());
    }

    /**
     * 开始时间包含,结束时间在外面
     * @param c
     * @return
     */
    public boolean beginContainsAfterOuter(DateRange c) {
        return this.getBegin().before(c.getBegin()) && this.getEnd().before(c.getEnd());
    }

    /**
     * 结束时间包含,还是时间在之前
     * @param c
     * @return
     */
    public boolean afterContainsBeginBefore(DateRange c){
        return this.getBegin().after(c.getBegin()) && this.getEnd().after(c.getEnd());
    }

    /**
     * 获取时长
     * @return
     */
    public long getTime(){
        return this.end.getTime() - this.begin.getTime();
    }

}
