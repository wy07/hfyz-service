package com.hfyz.warning
enum AlarmLevel {
    PROMPT(1,'提示告警')
    , NORMAL(2,'一般告警')
    , SERIOUS(3,'严重告警')

    int id
    String cnName

    public AlarmLevel(int id,String cnName) {
        this.id=id
        this.cnName = cnName
    }

    public String toString() {
        cnName
    }
}