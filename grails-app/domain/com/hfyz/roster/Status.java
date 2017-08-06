package com.hfyz.roster;

/**
 * Created by gaojl on 2017/8/4 10:23.
 */
enum ControlStatus {
    WBK("未布控", 1),
    BKZ("布控中", 2),
    JCBK("解除布控", 3);

    String type;
    int id;

    ControlStatus(String type, int id) {
        this.type = type;
        this.id = id;
    }
}
