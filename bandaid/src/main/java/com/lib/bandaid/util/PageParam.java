package com.lib.bandaid.util;

public class PageParam {

    private int pageSize = 30;
    private int pageNum = 1;
    private Type type = Type.NEW;

    public static PageParam create() {
        return new PageParam();
    }

    public static PageParam create(int pageSize) {
        return new PageParam(pageSize);
    }

    public static PageParam create(int pageSize, int pageNum) {
        return new PageParam(pageSize, pageNum);
    }

    private PageParam() {

    }

    private PageParam(int pageSize) {
        this.pageSize = pageSize;
    }

    private PageParam(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public PageParam next() {
        this.typeMore();
        this.pageNum++;
        return this;
    }

    public PageParam before() {
        this.pageNum--;
        if (this.pageNum == 0) this.pageNum = 1;
        return this;
    }

    public int getNum() {
        return pageNum;
    }

    public int getSize() {
        return pageSize;
    }

    public Type getType() {
        return type;
    }

    public PageParam New() {
        this.type = Type.NEW;
        this.pageNum = 1;
        return this;
    }

    public PageParam typeMore() {
        this.type = Type.MORE;
        return this;
    }

    public boolean isNew() {
        return this.type == Type.NEW;
    }

    public boolean isMore() {
        return this.type == Type.MORE;
    }

    public enum Type {
        NEW,
        MORE
    }
}
