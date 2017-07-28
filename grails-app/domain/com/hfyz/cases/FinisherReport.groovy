package com.hfyz.cases
/**
 * 案件-结案报告
 */
class FinisherReport {
    String caseRegisterNo //案件登记号
    String illegalContent //违法内容
    String caseInvestigatorOne //案件调查（执法）人员1
    String enforceLicenseOne //执法证号1
    String caseInvestigatorTwo //案件调查（执法）人员2
    String enforceLicesneTwo //执法证号2
    String illegalFact //违法事实
    String punishDecideType //处罚决定类型
    Double fine //罚款金额
    String confiscateReceiptNo //罚没收据号
    String revokeLicenseType //吊销（收缴）证照类型
    Integer rectifyTimeLimit //整顿（改）时限
    String punishExecuteState //处罚执行状态
    String executeSituation //执行情况
    String undertakePeople //承办人员
    Date finishCaseTime //结案日期
    Integer caseFilePage //案卷页数
    String remark //备注

    static constraints = {
        caseRegisterNo maxSize: 17, nullable: false, blank: false, unique: true
        illegalContent maxSize: 300, nullable: true, blank: false
        caseInvestigatorOne maxSize: 30, nullable: true, blank: false
        enforceLicenseOne maxSize: 12, nullable: true, blank: false
        caseInvestigatorTwo maxSize: 30, nullable: true, blank: false
        enforceLicesneTwo maxSize: 12, nullable: true, blank: false
        illegalFact maxSize: 300, nullable: true, blank: false
        punishDecideType maxSize: 20, nullable: true, blank: false
        fine nullable: true
        confiscateReceiptNo maxSize: 50, nullable: true, blank: false
        revokeLicenseType maxSize: 30, nullable: true, blank: false
        rectifyTimeLimit nullable: true
        punishExecuteState maxSize: 4, nullable: true, blank: false
        executeSituation maxSize: 100, nullable: true, blank: false
        undertakePeople maxSize: 30, nullable: true, blank: false
        finishCaseTime nullable: true
        caseFilePage nullable: true
        remark maxSize: 100, nullable: true, blank: false
    }

    static mapping = {
        table "CASE_FINSHCASE_FINISHREPORT"
        version false
    }
}
