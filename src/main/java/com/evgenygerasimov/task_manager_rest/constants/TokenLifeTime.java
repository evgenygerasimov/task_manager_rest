package com.evgenygerasimov.task_manager_rest.constants;

import lombok.Getter;

@Getter
public enum TokenLifeTime {

    TEN_MINUTES(600000),
    THIRTY_MINUTES(1800000),
    ;

    private final int minutes;

    TokenLifeTime(int minutes) {
        this.minutes = minutes;
    }

}
