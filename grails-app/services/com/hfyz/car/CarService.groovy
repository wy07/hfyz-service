package com.hfyz.car

import com.commons.exception.InstancePermException
import com.commons.exception.RecordNotFoundException
import com.commons.utils.NumberUtils
import com.commons.utils.SQLHelper
import com.hfyz.security.User
import com.hfyz.support.Organization
import grails.async.Promise
import grails.transaction.Transactional

import static grails.async.Promises.task

@Transactional
class CarService {
    def dataSource
    def warningService

    def search(Map inputParams, User user, Long max, Long offset) {
        def sqlParams = [:]
        def dateFlag = inputParams.dateBegin && inputParams.dateEnd
        if (inputParams.businessType) {
            sqlParams.businessType = inputParams.businessType
        }
        if (inputParams.licenseNo) {
            sqlParams.licenseNo = "${inputParams.licenseNo}%".toString()
        }
        if (dateFlag) {
            sqlParams.dateBegin = inputParams.dateBegin
            sqlParams.dateEnd = inputParams.dateEnd
        }

        if (user.isCompanyUser()) {
            sqlParams.ownerCode = user.companyCode
        }

        Promise carList = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getSearchCarsSql(dateFlag, inputParams.businessType, inputParams.licenseNo, user.isCompanyUser()), sqlParams + [max: max, offset: offset])
            }?.collect { obj ->
                [id                  : obj.id
                 , transformLicenseNo: obj.transformLicenseNo
                 , licenseNo         : obj.licenseNo
                 , ownerName         : obj.ownerName
                 , frameNo           : obj.frameNo
                 , carType           : obj.carType
                 , carPlateColor     : obj.carPlateColor
                 , carColor          : obj.carColor
                 , endTime           : obj.endTime?.format("yyyy-MM-dd")]
            }
        }


        Promise carCount = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.firstRow(getSearchCarsCountSql(dateFlag, inputParams.businessType, inputParams.licenseNo, user.isCompanyUser()), [sqlParams]).count ?: 0

            }
        }

        [carList: carList.get(), carCount: carCount.get()]
    }


    def networkRate(rate = 100) {
        SQLHelper.withDataSource(dataSource) { sql ->
            def networkRateList = sql.rows(getNetworkRateListSql(), [rate: rate])
        }?.collect { obj ->
            [companyCode     : obj.companyCode
             , ownerName     : obj.ownerName
             , operateManager: obj.operateManager
             , phone         : obj.phone
             , operateCount  : obj.operateCount
             , carinfoCount  : obj.carinfoCount
             , rate          : obj.rate.setScale(2, BigDecimal.ROUND_HALF_UP)]
        }
    }

    def carNumStatistic(Organization organization = null) {
        def result

        if (organization?.code == '24') {
            result = [carNum        : 843
                      , enterCarNum : 821
                      , onlineCarNum: 783]
        } else {
            result = [carNum        : 13202
                      , enterCarNum : 9890
                      , onlineCarNum: 6507]
        }

        return result
    }

    def historyStatistic(Organization organization = null, year) {
        int currentYear = new Date().format('yyyy').toInteger()
        year = year ?: currentYear
        int month = currentYear == year ? new Date().format('MM').toInteger() : 12

        def initDate = {
            (1..month).collect {
                new Random().nextInt(100)
            }
        }
        return [enterRate           : initDate()
                , onlineRate        : initDate()
                , onlineTimeRate    : initDate()
                , overspeedRate     : initDate()
                , fatigueRate       : initDate()
                , realTimeOnlineRate: initDate()]
    }

    def getCompanyCars(String companyCode) {
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_COMPANY_CARS_SQL, [companyCode: companyCode])
        }?.collect { obj ->
            [licenseNo      : obj.licenseNo
             , carPlateColor: obj.carPlateColor]
        }
    }

    def getWarningAndHistoryList(String licenseNo, User currentUser, int max) {

        CarBasicInfo carInstance = licenseNo ? CarBasicInfo.findByLicenseNo(licenseNo) : null
        if (!carInstance) {
            throw new RecordNotFoundException()
        }

        if (currentUser.isCompanyUser()) {
            CarBasicOperate carOperateInstance = CarBasicOperate.createCriteria().get {
                eq('ownerCode', currentUser.companyCode)
                eq('frameNo', carInstance.frameNo)
                le('beginTime', new Date())
                ge('endTime', new Date())
                maxResults(1)
            }
            if (!carOperateInstance) {
                throw new InstancePermException()
            }
        }

        def warninglist = warningService.getWarningsByCar(carInstance.frameNo, max, 0)
        def historyLocations = historyLocations(licenseNo, max, 0)

        [warnings          : warninglist
         , historyLocations: historyLocations]
    }

    def getHistoryInfo(String licenseNo, User currentUser, Date startDate, Date endDate) {
        CarBasicInfo carInstance = licenseNo ? CarBasicInfo.findByLicenseNo(licenseNo) : null
        if (!carInstance) {
            throw new RecordNotFoundException()
        }

        if (currentUser.isCompanyUser()) {
            CarBasicOperate carOperateInstance = CarBasicOperate.createCriteria().get {
                eq('ownerCode', currentUser.companyCode)
                eq('frameNo', carInstance.frameNo)
                le('beginTime', new Date())
                ge('endTime', new Date())
                maxResults(1)
            }
            if (!carOperateInstance) {
                throw new InstancePermException()
            }
        }
        def warningList = warningService.getWarningsByCarAndDate(carInstance.frameNo, startDate, endDate)
        def historyLocations = historyLocationsByDate(licenseNo, startDate, endDate)


        [warnings          : warningList
         , historyLocations: historyLocations]
    }

    BigDecimal lng = 116.37168
    BigDecimal lat = 39.93218

    def historyLocations(String plateNo, def max, def offset) {
        lng += 0.01
        lat -= 0.01
        (1..max).collect {
            [dateStr     : new Date().format('yyyy-MM-dd HH:mm:ss'),
             plateColor  : '白色',
             plateNo     : plateNo,
             posEncrypt  : 0,
             geoPoint    : "${lng},${lat}",
             gpsSpeed    : '60',
             totalMileage: 1,
             recSpeed    : 60,
             direction   : NumberUtils.getRandom(0, 360),
             altitude    : 0,
             vehicleState: 3,
             alarmState  : NumberUtils.getRandom(0, 10) % 2]
        }
    }


    def historyLocationsByDate(String plateNo, Date startDate, Date endDate) {
        Long startDateTime = startDate.time
        Long endDateTime = endDate.time
        def list = []
        startDateTime.step(endDateTime, 30000) {
            lng -= 0.01
            lat += 0.01
            list << [dateStr     : new Date(it).format('yyyy-MM-dd HH:mm:ss'),
                     plateColor  : '白色',
                     plateNo     : plateNo,
                     posEncrypt  : 0,
                     geoPoint    : "${lng},${lat}",
                     gpsSpeed    : '60',
                     totalMileage: 1,
                     recSpeed    : 60,
                     direction   : NumberUtils.getRandom(0, 360),
                     altitude    : 0,
                     vehicleState: 3,
                     alarmState  : NumberUtils.getRandom(0, 10) % 2]
        }
        list
    }

    private static String GET_COMPANY_CARS_SQL = """
        SELECT carinfo.license_no licenseNo
            ,operate.owner_name ownerName
            ,carinfo.frame_no frameNo
            ,carinfo.car_plate_color carPlateColor
            ,carinfo.car_color carColor
        FROM runcar_basic_carinfo carinfo
        JOIN runcar_basicoperate operate on carinfo.frame_no=operate.frame_no
        WHERE operate.owner_code=:companyCode
    """

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
                
                select company_code companyCode, ownerName, operate_manager operateManager, phone, operateCount, carinfoCount, rate
                from result, owner_identity iden
                where ownerName = iden.owner_name and rate <= :rate
                order by company_code
        """
        return sqlStr
    }

    private
    static String getSearchCarsSql(boolean dateFlag, String businessType, String licenseNo, boolean isCompanyUser) {
        String sqlStr = """
            SELECT  carinfo.id id,
            operate.transform_license_no transformLicenseNo
            ,carinfo.license_no licenseNo
            ,operate.owner_name ownerName
            ,carinfo.frame_no frameNo
            ,carinfo.car_type carType
            ,carinfo.car_plate_color carPlateColor
            ,carinfo.car_color carColor
            ,operate.end_time endTime
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
        if (dateFlag) {
            sqlStr += " and operate.end_time between to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS') "
        }
        if (isCompanyUser) {
            sqlStr += " and operate.owner_code=:ownerCode"
        }

        sqlStr += """
            order by carinfo.settle_time desc
            limit :max offset :offset;
        """

        return sqlStr
    }

    private
    static String getSearchCarsCountSql(boolean dateFlag, String businessType, String licenseNo, boolean isCompanyUser) {
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

        if (dateFlag) {
            sqlStr += " and operate.end_time between to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS')"
        }
        if (isCompanyUser) {
            sqlStr += " and operate.owner_code=:ownerCode"
        }
        return sqlStr
    }

}
