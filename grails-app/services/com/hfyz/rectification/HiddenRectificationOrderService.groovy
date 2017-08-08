package com.hfyz.rectification

import grails.transaction.Transactional
import com.commons.utils.SQLHelper
import grails.async.Promise
import static grails.async.Promises.task

@Transactional
class HiddenRectificationOrderService {
    def dataSource

    def setStatus(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.getinstanceById(request.JSON.statusId)
                hiddenRectificationOrderIns.save(flush: true,failOnError: true)

        }
    }

    private getHiddenRectificationOrderList(String company,def startDate,def endDate,def max, def offset, def status,def listStatus,def companyCode){
        def sqlParams = [:]
        def sd = startDate
        def ed = endDate
        if(company){
            sqlParams.company = "%${company}%".toString()
        }
        if(startDate){
            sqlParams.sd = sd
        }
        if(endDate){
            sqlParams.ed = ed
        }
        if(listStatus) {
            sqlParams.listStatus = Integer.parseInt(listStatus)
        }
        if(companyCode && status == 'FK'){
            sqlParams.companyCode = companyCode
        }
        Promise hiddenRectificationOrderList = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getHiddenRectificationOrdeSql(company, sd, ed, status,listStatus,companyCode), sqlParams + [max: max, offset: offset])
            }?.collect { obj ->
                [id              : obj.id
                 , billNo        : obj.billNo
                 , enterpirse    : obj.enterpirse
                 , examiner      : obj.examiner
                 , inspectionDate: obj.inspectionDate.format('yyyy-MM-dd HH:mm:ss')
                 , dealineDate   : obj.dealineDate.format('yyyy-MM-dd HH:mm:ss')
                 , status        : HiddenRectificationOrderStatus.getinstanceById(obj.status).type]
            }
        }

        Promise total = task{
            SQLHelper.withDataSource(dataSource){
                sql ->
                    sql.firstRow(getHiddenRectificationOrderPageSql(company, sd, ed, status,listStatus,companyCode),sqlParams).count ?:0
            }

        }

        return [total:total.get(),hiddenRectificationOrderList:hiddenRectificationOrderList.get()]
    }

    private static getHiddenRectificationOrdeSql(String company , def startDate ,def endDate,def status,def listStatus,def companyCode){
        String sql = """
                        SELECT table1.id id 
                            ,table1.bill_no billNo
                            , table2.owner_name enterpirse
                            , table1.status status
                            ,table1.examiner  examiner
                            ,table1.inspection_date inspectionDate
                            ,table1.dealine_date  dealineDate
                        FROM hidden_rectification_order table1 
                            , OWNER_BASICINFO_OWNERIDENTITY table2 
                        WHERE table2.company_code = table1.company_code
                     """
        if(company){
            sql += " and table2.owner_name like :company "
        }
        if(startDate){
            sql += " and table1.inspection_date >= :sd::timestamp "
        }
        if(endDate){
            sql += " and table1.inspection_date <= :ed::timestamp "
        }
        if(status == 'SH'){
            sql += " and (table1.status = 1 or  table1.status = 4 or table1.status = 2)"
        }
        if(status == 'FK'){
            sql += " and (table1.status = 2 or table1.status = 4 or table1.status = 5 or table1.status = 6) "
            if(companyCode){
                sql += " and table1.company_code = :companyCode"
            }

        }

        if(listStatus){
            sql += " and table1.status = :listStatus "
        }

        sql += """
            order by table1.bill_no desc
            limit :max offset :offset;
        """
           return sql
    }

    private static getHiddenRectificationOrderPageSql(String company , def startDate ,def endDate,def status,def listStatus,def companyCode){
        String sql = """
                        SELECT count(table1.id)
                        FROM hidden_rectification_order table1 
                            , OWNER_BASICINFO_OWNERIDENTITY table2 
                        WHERE table2.company_code = table1.company_code
                     """
        if(company){
            sql += " and table2.owner_name like :company "
        }
        if(startDate){
            sql += " and table1.inspection_date >= :sd::timestamp "
        }
        if(endDate){
            sql += " and table1.inspection_date <= :ed::timestamp "
        }
        if(status == 'SH'){
            sql += " and (table1.status = 1 or  table1.status = 4 or table1.status = 2)"
        }
        if(status == 'FK'){
            sql += " and (table1.status = 2 or table1.status = 4 or table1.status = 5 or table1.status = 6) "
            if(companyCode){
                sql += " and table1.company_code = :companyCode"
            }
        }
        if(listStatus){
            sql += " and table1.status = :listStatus "
        }
        return sql
    }

    def getReviewAndApprovalList(def obj){
        def totalOfReviewAndApproval = ReviewAndApprovalForm.createCriteria().get {
            projections {
                count()
            }
            if(obj){
                eq('billId',obj)
            }
        }
        def reviewAndApprovalList = ReviewAndApprovalForm.createCriteria().list(){
            if(obj){
                eq('billId',obj)
            }
        }?.collect {
            reviewAndApprovalFormIns ->
               [ approvalResult:reviewAndApprovalFormIns.approvalResult,
                 approver:reviewAndApprovalFormIns.approver,
                 approveTime:reviewAndApprovalFormIns.approveTime.format('yyyy-MM-dd HH:mm:ss'),
                 approveDesc:reviewAndApprovalFormIns.approveDesc
               ]
        }
        return [totalOfReviewAndApproval:totalOfReviewAndApproval,reviewAndApprovalList:reviewAndApprovalList]
    }


}
