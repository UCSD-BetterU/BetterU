package com.betteru.ucsd.myapplication4;

public class ExtrasensoryActivity {
    private String text;
    private int type; //1-main; 2-secondary; 3-place; 4-none; 5-1&2
    private Integer icon;
    private String label;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public ExtrasensoryActivity(String text, String label,int type, Integer icon, int index) {
        this.text = text;
        this.label = label;
        this.type = type;
        this.icon = icon;
        this.index = index;
    }
}
