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
        def sqlParams = [businessType: businessType]
        if (licenseNo) {
            sqlParams.licenseNo = licenseNo
        }

        Promise carList = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getSearchCarsSql(licenseNo), sqlParams + [max: max, offset: offset])
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
                sql.firstRow(getSearchCarsCountSql(licenseNo), [sqlParams]).count ?: 0

            }
        }

        [carList: carList.get(), carCount: carCount.get()]
    }

    private static String getSearchCarsSql(String licenseNo) {
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
            WHERE operate.business_type=:businessType
        """
        if (licenseNo) {
            sqlStr += ' and carinfo.license_no=:licenseNo'
        }
        sqlStr += """
            order by carinfo.settle_time desc
            limit :max offset :offset;
        """

        return sqlStr
    }

    private static String getSearchCarsCountSql(String licenseNo) {
        String sqlStr = """
            SELECT  count(carinfo.frame_no)
            FROM runcar_basic_carinfo carinfo
            JOIN runcar_basicoperate operate on carinfo.frame_no=operate.frame_no
            WHERE operate.business_type=:businessType
        """
        if (licenseNo) {
            sqlStr += ' and carinfo.license_no=:licenseNo'
        }

        return sqlStr
    }

}
