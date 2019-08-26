package com.lib.bandaid.widget.squareview;

import java.io.Serializable;

/**
 * Created by zy on 2018/5/2.
 */

public class GridItem implements Serializable {

    private int icon;

    private String name;

    public static GridItem create(int icon, String name) {
        return new GridItem(icon, name);
    }

    public GridItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
