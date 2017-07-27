package com.hfyz.car

import com.commons.utils.NumberUtils
import com.commons.utils.SQLHelper
import grails.async.Promise
import grails.transaction.Transactional

import static grails.async.Promises.task
import static grails.async.Promises.task

@Transactional
class CarService {
    def dataSource

    def search(String businessType, String licenseNo, Long max, Long offset) {
        def sqlParams = [:]
        if (businessType) {
            sqlParams.businessType = businessType
        }
        if (licenseNo) {
            sqlParams.licenseNo = "${licenseNo}%".toString()
        }

        Promise carList = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getSearchCarsSql(businessType, licenseNo), sqlParams + [max: max, offset: offset])
            }?.collect { obj ->
                [transformLicenseNo: obj.transformLicenseNo
                 , licenseNo       : obj.licenseNo
                 , ownerName       : obj.ownerName
                 , frameNo         : obj.frameNo
                 , carType         : obj.carType
                 , carPlateColor   : obj.carPlateColor
                 , carColor        : obj.carColor]
            }
        }


        Promise carCount = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.firstRow(getSearchCarsCountSql(businessType, licenseNo), [sqlParams]).count ?: 0

            }
        }

        [carList: carList.get(), carCount: carCount.get()]
    }

    def networkRate(rate = 100) {
        SQLHelper.withDataSource(dataSource) { sql ->
           def networkRateList = sql.rows(getNetworkRateListSql(),[rate:rate])
        }?.collect { obj ->
            [companyCode     : obj.companyCode
             , operateCount: obj.operateCount
             , carinfoCount: obj.carinfoCount
             , rate        : obj.rate.setScale(2,BigDecimal.ROUND_HALF_UP)]
        }
    }

    private static String getNetworkRateListSql() {
        String sqlStr = """
            with result as(
                select owner_name ownerName
                       , count(owner_name) operateCount
                       , count(carinfo.frame_no) carinfoCount
                       , count(carinfo.frame_no)*0.1/count(owner_name)*1000 rate
                from runcar_basicoperate operate
                left join registration_infornation_carinfo carinfo on  carinfo.frame_no = operate.frame_no
                group by owner_name)
                
                select company_code companyCode, operateCount, carinfoCount, rate
                from result, owner_identity iden
                where ownerName = iden.owner_name and rate <= :rate
                order by company_code
        """
        return sqlStr
    }

    private static String getSearchCarsSql(String businessType, String licenseNo) {
        String sqlStr = """
            SELECT  operate.transform_license_no transformLicenseNo
            ,carinfo.license_no licenseNo
            ,operate.owner_name ownerName
            ,carinfo.frame_no frameNo
            ,carinfo.car_type carType
            ,carinfo.car_plate_color carPlateColor
            ,carinfo.car_color carColor
            FROM runcar_basic_carinfo carinfo
            JOIN runcar_basicoperate operate on carinfo.frame_no=operate.frame_no
            WHERE 1=1
        """

        if (businessType) {
            sqlStr += 'and operate.business_type=:businessType'
        }

        if (licenseNo) {
            sqlStr += ' and carinfo.license_no like :licenseNo'
        }
        sqlStr += """
            order by carinfo.settle_time desc
            limit :max offset :offset;
        """

        return sqlStr
    }

    private static String getSearchCarsCountSql(String businessType, String licenseNo) {
        String sqlStr = """
            SELECT  count(carinfo.frame_no)
            FROM runcar_basic_carinfo carinfo
            JOIN runcar_basicoperate operate on carinfo.frame_no=operate.frame_no
            WHERE 1=1
        """
        if (businessType) {
            sqlStr += 'and operate.business_type=:businessType'
        }

        if (licenseNo) {
            sqlStr += ' and carinfo.license_no like :licenseNo'
        }

        return sqlStr
    }

}
