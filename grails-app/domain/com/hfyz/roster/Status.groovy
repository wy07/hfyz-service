package com.hfyz.roster;

/**
 * Created by gaojl on 2017/8/4 10:23.
 */
enum Status {
    WBK(0,"未布控"),
    BKZ(1,"布控中"),
    JCBK(2,"解除布控");

    int id
    String type;

    Status(int id, String type) {
        this.id = id
        this.type = type
    }
}
