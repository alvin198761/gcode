package org.alvin.code.gen.utils;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import org.alvin.code.gen.beans.BaseCondition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */
public class Page<T> {

    @ApiModelProperty(notes = "记录集合", required = true)
    private final List<T> content = new ArrayList<>();
    @ApiModelProperty(notes = "当前页码", required = true)
    private int number = 0;
    @ApiModelProperty(notes = "每页记录个数", required = true)
    private int size = 0;
    @ApiModelProperty(notes = "总记录数", required = true)
    private long totalElements;

    public Page() {
    }

    public Page(int pageNum, int pageSize, long total, List<T> content) {
        this.number = pageNum;
        this.size = pageSize;
        this.totalElements = total;
        this.content.addAll(content);
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    @ApiModelProperty(notes = "当前页的记录个数", required = true)
    public int getNumberOfElements() {
        return this.content.size();
    }

    public List<T> getContent() {
        return content;
    }

    public <S> Page<S> map(List<S> content) {
        Page<S> page = new Page<S>();
        if (content != null) {
            page.content.addAll(content);
            page.number = this.number;
            page.size = this.size;
            page.totalElements = this.totalElements;
        }
        return page;
    }

    public static <S> Page<S> map(List<S> content, int number, int size, long totalElements) {
        Page<S> page = new Page<S>();
        page.content.addAll(content);
        page.number = number;
        page.size = size;
        page.totalElements = totalElements;
        return page;
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    @ApiModelProperty(notes = "总页数", required = true)
    public int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int) Math.ceil((double) this.getTotalElements() / (double) this.getSize());
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }


    @ApiModelProperty(notes = "是否最后一页", required = true)
    public boolean isLast() {
        return !this.hasNext();
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * 返回空的分页方法
     * @param cond
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Page<T> emptyPage(BaseCondition cond, Class<T> clazz) {
        Page<T> page = new Page<T>();
        page.content.addAll(Lists.newArrayList());
        page.number = cond.getPage();
        page.size = cond.getSize();
        page.totalElements = 0;
        return page;
    }
}
