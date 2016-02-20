package com.inmobi.user.dto;

import com.inmobi.feed.dto.Tags;

public class Gamify {
    private Tags tag;
    private int points;

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
