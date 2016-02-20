package com.inmobi.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Tags {
    @JsonProperty("FOOTBALL")
    FOOTBALL,
    @JsonProperty("GYM")
    GYM,
    @JsonProperty("POOL")
    POOL,
    @JsonProperty("BADMINTON")
    BADMINTON,
    @JsonProperty("SQUASH")
    SQUASH
}
