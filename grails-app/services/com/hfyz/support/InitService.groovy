package com.hfyz.support

import com.commons.utils.NumberUtils
import com.commons.utils.ValidationUtils
import com.hfyz.cases.RegisterReport
import com.hfyz.owner.CompanyRegulation

import com.hfyz.owner.OwnerIdentity
import com.hfyz.owner.OwnerManageInfo
import com.hfyz.people.PeopleBasicInfo
import com.hfyz.people.WorkerCheckMember
import com.hfyz.people.WorkerCoach
import com.hfyz.people.WorkerDriver
import com.hfyz.people.WorkerManager
import com.hfyz.people.WorkerTechnology
import com.hfyz.people.WorkerWaiter
import com.hfyz.platform.PlatformManage
import com.hfyz.car.CarBasicInfo
import com.hfyz.car.CarBasicOperate
import com.hfyz.car.RegistrationInformationCarinfo
import com.hfyz.roster.BlackList
import com.hfyz.roster.Status
import com.hfyz.roster.WhiteList
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.Warning
import com.hfyz.workOrder.WorkOrder
import com.hfyz.workOrder.WorkOrderFlow
import com.hfyz.workOrder.WorkOrderFlowAction
import com.hfyz.waybill.FreightWaybill
import com.hfyz.waybill.PassLineBusinessBasicInfo
import com.hfyz.waybill.PassLinePhysicalBasicInfo
import grails.transaction.Transactional
import com.hfyz.security.User
import com.hfyz.security.Role
import com.hfyz.security.UserRole
import com.hfyz.security.PermissionGroup
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.rectification.HiddenRectificationOrderStatus

@Transactional
class InitService {

    User centerUser = null
    def companys=["安徽省合肥汽车客运有限公司"
                  ,"合肥新亚汽车客运公司"
                  ,"合肥长丰县宏业汽车客运有限公司"
                  ,"合肥客运旅游汽车公司"
                  ,"锐致货运公司"
                  ,"合肥齐力货运公司"
                  ,"合肥铂达货运公司"
                  ,"合肥远征货运公司"
                  ,"合肥聚贤货运有限责任公司"]
    def cars=["皖A36638","皖NB7016","皖D76035","皖A5D042","浙HC8891","皖AB3736","皖AA7553","皖A7J666","皖M53742","皖A67433","冀BR3616","皖A48925","苏AE8130","苏AJ3211","皖A6E498","苏AB8985","皖A7R673","皖A84778","皖A76060","皖A31799","皖M56445","皖HB1389","皖A7J511","皖M58341","皖A84471","苏G55965","皖E18021","皖A58082","皖AB0586","皖AA5471","皖A41986","皖RAN988","皖A33483","皖Q07443","皖A84908","皖M98172","皖A81442","皖N28683","皖A83301","皖G11612","皖A02658","皖A32177","皖A7Q020","皖A7K515","皖A7P266","皖QSQ868","豫QA8295","皖A37202","皖A38870","皖A58076","皖K16738","皖A84719","皖LB4982","皖A7L230","皖AB2133","苏BG5682","皖A6A046","皖D60429","浙E17157","苏G50093","皖NA1665","沪D52068","浙A7L255","皖E18021","皖A5F271","皖A69649","皖A40091","皖A7Q197","浙A5L030","皖A82218","皖A5F227","皖A84335","皖AB3997","皖A45549","皖A42406","皖AA5780","苏C31328","皖HB1847","京ACB259","皖AA5053","皖AB0636","皖A38883","皖A85077","皖AA1388","皖M17210","皖A6D300","皖A57602","皖A58228","皖NB5523","皖A83429","皖A45565","皖AB0586","皖A23288","皖A7P972","皖A7D666","皖A47119","苏AJ90T0","皖AA9680","皖A58290","皖A58202","皖A41967","皖M16487","豫QA8179","皖AB3997","皖A6D301","皖A37312"]

    private static final xing=['赵','钱','孙','李','周','吴','郑','王','冯','陈','楚','魏','蒋','沈','韩','杨','武','仵','吕','雒','张']
    private static final ming=['超','三','一','键','平','丽萍','妙娟','伟','立群','建国','建业','卫国','萌','哲','江涛','辉','建新','海峰','新春','家伟','冰','云景','秦岭','钟楼','鼓楼','雁塔','新城','未央','琴','彪','江']
    private static final xingSize=xing.size()-1
    private static final mingSize=ming.size()-1

    String mockUsername(){
        "${xing[NumberUtils.getRandom(0,xingSize)]}${ming[NumberUtils.getRandom(0,mingSize)]}"
    }


    def initData() {
//        if (User.count() > 0) {
//            return
//        }
        initMenu()
        initSecurityData()
        centerUser = User.findByUsername('center')

        initSystemCode()
        initAlarmType()
        initWorkOrder()
        initWaybill()
        initPassLineBusinessBasicInfo()
        initPassLinePhysicalBasicInfo()
        initPlatformManage()
        initBlackAndWhite()
        initWarning()
        initPeople()
        initCheckRecord()
        initMapSign()
        initRegisterReport()
        initRegistrationInformationCarinfo()
        initCompanyAndRegulation()
        initHiddenRectificationOrder()
        initConfigure()
        initCar()
    }

    private initSystemCode() {
        LicenseType licenseTypeP = new LicenseType(name: "道路运输经营许可证", codeNum: "100", parent: null).save(flush: true)
        new LicenseType(name: "道路运输经营许可证正本", codeNum: "110", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "道路运输经营许可证副本", codeNum: "120", parent: licenseTypeP).save(flush: true)

        licenseTypeP = new LicenseType(name: "车辆运营证（道路运输证核心板证件）", codeNum: "200", parent: null).save(flush: true)
        new LicenseType(name: "道路运输证", codeNum: "210", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "车辆暂扣证明", codeNum: "220", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "车辆年审标贴", codeNum: "230", parent: licenseTypeP).save(flush: true)
        licenseTypeP = new LicenseType(name: "国际汽车运输行车许可证", codeNum: "240", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "A中行车许可证", codeNum: "241", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "B种行车许可证", codeNum: "242", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "C种行车许可证", codeNum: "243", parent: licenseTypeP).save(flush: true)

        LicenseType temp = new LicenseType(name: "营运标致车", codeNum: "300", parent: null).save(flush: true)
        licenseTypeP = new LicenseType(name: "班车客运标志牌", codeNum: "310", parent: temp).save(flush: true)
        new LicenseType(name: "县内班车客运标志牌", codeNum: "311", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "县际班车客运标志牌", codeNum: "312", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "市际班车客运标志牌", codeNum: "313", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "省际班车客运标志牌", codeNum: "314", parent: licenseTypeP).save(flush: true)

        licenseTypeP = new LicenseType(name: "包车客运标志牌", codeNum: "320", parent: temp).save(flush: true)
        new LicenseType(name: "县内包车客运标志牌", codeNum: "321", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "县际包车客运标志牌", codeNum: "322", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "市际包车客运标志牌", codeNum: "323", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "省际包车客运标志牌", codeNum: "324", parent: licenseTypeP).save(flush: true)

        licenseTypeP = new LicenseType(name: "临时客运标志牌", codeNum: "330", parent: temp).save(flush: true)
        new LicenseType(name: "县内临时客运标志牌", codeNum: "331", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "县际临时客运标志牌", codeNum: "332", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "市际临时客运标志牌", codeNum: "333", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "省际临时客运标志牌", codeNum: "334", parent: licenseTypeP).save(flush: true)
        new LicenseType(name: "道路客运班线经营许可证明", codeNum: "340", parent: temp).save(flush: true)

        UnitNature unitNatureP = new UnitNature(name: "单位性质", codeNum: "UnitNature", parent: null).save(flush: true)
        new UnitNature(name: "政府机关", codeNum: "10", parent: unitNatureP).save(flush: true)
        new UnitNature(name: "企业", codeNum: "20", parent: unitNatureP).save(flush: true)
        new UnitNature(name: "事业单位", codeNum: "30", parent: unitNatureP).save(flush: true)
        new UnitNature(name: "社会团体", codeNum: "40", parent: unitNatureP).save(flush: true)

        new DangerousType(name: '危险货物运输（1类1项）', codeNum: '03111', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（1类2项）', codeNum: '03112', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（1类3项）', codeNum: '03113', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（1类4项）', codeNum: '03114', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（1类5项）', codeNum: '03115', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（1类6项）', codeNum: '03116', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（2类1项）', codeNum: '03121', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（2类2项）', codeNum: '03122', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（2类3项）', codeNum: '03123', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（3类）', codeNum: '03131', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（4类1项）', codeNum: '03141', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（4类2项）', codeNum: '03142', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（4类3项）', codeNum: '03143', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（5类1项）', codeNum: '03151', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（5类2项）', codeNum: '03152', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（6类1项）', codeNum: '03161', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（6类2项）', codeNum: '03161', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（7类）', codeNum: '03111', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（8类）', codeNum: '03111', parent: null).save(flush: true)
        new DangerousType(name: '危险货物运输（9类）', codeNum: '03111', parent: null).save(flush: true)
    }

    private initAlarmType() {
        new AlarmType(name: "超速驾驶", codeNum: "101", parent: null).save(flush: true)
        new AlarmType(name: "疲劳驾驶", codeNum: "102", parent: null).save(flush: true)
        new AlarmType(name: "偏离路线", codeNum: "111", parent: null).save(flush: true)
        new AlarmType(name: "企业营运资质到期", codeNum: "201", parent: null).save(flush: true)
        new AlarmType(name: "企业营运资质过期", codeNum: "202", parent: null).save(flush: true)
        new AlarmType(name: "企业营运资质锁定", codeNum: "203", parent: null).save(flush: true)
        new AlarmType(name: "专职监控人员配置不达标", codeNum: "204", parent: null).save(flush: true)
        new AlarmType(name: "运营企业内部管理制度未上传", codeNum: "205", parent: null).save(flush: true)
        new AlarmType(name: "车辆营运资格到期", codeNum: "206", parent: null).save(flush: true)
        new AlarmType(name: "车辆营运资格过期", codeNum: "207", parent: null).save(flush: true)
        new AlarmType(name: "车辆营运资格锁定", codeNum: "208", parent: null).save(flush: true)
        new AlarmType(name: "从业人员营运资格到期", codeNum: "209", parent: null).save(flush: true)
        new AlarmType(name: "从业人员营运资格过期", codeNum: "210", parent: null).save(flush: true)
        new AlarmType(name: "从业人员营运资格锁定", codeNum: "211", parent: null).save(flush: true)
        new AlarmType(name: "从业人员营运资格和企业申请的运营服务范围不一致", codeNum: "212", parent: null).save(flush: true)
        new AlarmType(name: "营运车辆和从业人员不匹配", codeNum: "213", parent: null).save(flush: true)
        new AlarmType(name: "平台查岗未响应", codeNum: "214", parent: null).save(flush: true)
        new AlarmType(name: "车辆超时未上报数据", codeNum: "215", parent: null).save(flush: true)
        new AlarmType(name: "车辆离线", codeNum: "216", parent: null).save(flush: true)
        new AlarmType(name: "凌晨2~5点未停运", codeNum: "217", parent: null).save(flush: true)
        new AlarmType(name: "车辆超员/超载", codeNum: "218", parent: null).save(flush: true)
        new AlarmType(name: "入网率不达标", codeNum: "219", parent: null).save(flush: true)
        new AlarmType(name: "上线率不达标", codeNum: "220", parent: null).save(flush: true)
        new AlarmType(name: "数据不合格率不达标", codeNum: "221", parent: null).save(flush: true)
        new AlarmType(name: "查岗响应率不达标", codeNum: "222", parent: null).save(flush: true)
        new AlarmType(name: "车辆在线时长不达标", codeNum: "223", parent: null).save(flush: true)
        new AlarmType(name: "超速车辆率不达标", codeNum: "224", parent: null).save(flush: true)
        new AlarmType(name: "超速车辆处理率不达标", codeNum: "225", parent: null).save(flush: true)
        new AlarmType(name: "疲劳驾驶车辆率不达标", codeNum: "226", parent: null).save(flush: true)
        new AlarmType(name: "疲劳驾驶处理率不达标", codeNum: "227", parent: null).save(flush: true)
    }

    private initWaybill() {
        def dangerousType = DangerousType.findByCodeNum('03111')
        new FreightWaybill(
                vehicleNo: cars[0],
                frameNo: getFrameNo(0),
                companyCode: "C000000000",
                ownerName: companys[0],
                dangerousName: '液碱',
                dangerousType: dangerousType,
                ratifiedPayload: 7000.0,
                emergencyPlan: '无',
                price: 3500.0,
                operatedType: '经营性',
                loadedType: '卸',
                fullLoaded: '否',
                amount: 8.0,
                mile: 263.0,
                departTime: new Date(117, 8, 12, 6, 23, 40),
                driverName: '赵坤',
                idCardNo: '3400000',
                consignCompany: '无',
                backTime: new Date(117, 8, 13, 5, 23, 40),
                departArea: '合肥',
                arriveArea: '张家港',
                status: '无',
                routerName: '合肥-太原',
                startProvince: '安徽省',
                startProvinceCode: '340000',
                startCity: '合肥市',
                startCityCode: '340100',
                startDistrict: '蜀山区',
                startDistrictCode: '340104',
                endProvince: '山西省',
                endProvinceCode: '140000',
                endCity: '太原市',
                endCityCode: '140100',
                endDistrict: '迎泽区',
                endDistrictCode: '140106'
        ).save(flush: true)
        new FreightWaybill(
                vehicleNo: cars[1],
                frameNo: getFrameNo(1),
                companyCode: "C000000001",
                ownerName: companys[1],
                dangerousName: '甲醇',
                dangerousType: dangerousType,
                ratifiedPayload: 7000.0,
                emergencyPlan: '无',
                price: 3500.0,
                operatedType: '经营性',
                loadedType: '卸',
                fullLoaded: '否',
                amount: 4.90,
                mile: 263.0,
                departTime: new Date(117, 8, 14, 6, 23, 40),
                driverName: '赵坤',
                idCardNo: '3400000',
                consignCompany: '无',
                backTime: new Date(117, 8, 15, 5, 23, 40),
                departArea: '合肥',
                arriveArea: '武汉',
                status: '无',
                routerName: '合肥-武汉',
                startProvince: '安徽省',
                startProvinceCode: '340000',
                startCity: '合肥市',
                startCityCode: '340100',
                startDistrict: '蜀山区',
                startDistrictCode: '340104',
                endProvince: '湖北省',
                endProvinceCode: '420000',
                endCity: '武汉市',
                endCityCode: '420100',
                endDistrict: '江岸区',
                endDistrictCode: '420102'
        ).save(flush: true)
        new FreightWaybill(
                vehicleNo: cars[2],
                frameNo: getFrameNo(2),
                companyCode: "C000000002",
                ownerName: companys[2],
                dangerousName: '环己酮',
                dangerousType: dangerousType,
                ratifiedPayload: 7000.0,
                emergencyPlan: '无',
                price: 3500.0,
                operatedType: '经营性',
                loadedType: '卸',
                fullLoaded: '否',
                amount: 2.30,
                mile: 263.0,
                departTime: new Date(117, 8, 10, 14, 23, 40),
                driverName: '赵坤',
                idCardNo: '3400000',
                consignCompany: '无',
                backTime: new Date(117, 8, 11, 01, 23, 40),
                departArea: '合肥',
                arriveArea: '武汉',
                status: '无',
                routerName: '合肥-连云港',
                startProvince: '安徽省',
                startProvinceCode: '340000',
                startCity: '合肥市',
                startCityCode: '340100',
                startDistrict: '蜀山区',
                startDistrictCode: '340104',
                endProvince: '江苏省',
                endProvinceCode: '320000',
                endCity: '连云港市',
                endCityCode: '320700',
                endDistrict: '连云区',
                endDistrictCode: '320703'
        ).save(flush: true)
    }

    private initPassLineBusinessBasicInfo() {
        5.times { val ->
            new PassLineBusinessBasicInfo(
                    lineCode: '1',
                    ownerName: cars[val%4],
                    companyCode: "C00000000${val % 4}",
                    licenseCharacter: "${val}",
                    licenseNo: "${val}",
                    busType: '大车',
                    startStationName: '滨湖时代广场',
                    endStationName: '市政务办公区西',
                    stopStation: '师范附小,一中,四十六中,周岗,市政务办公区西',
                    mainPoint: '滨湖时代广场,市政务办公区西',
                    dayTimes: val,
                    businessWay: '国营',
                    licenseDecideBookNo: "${val}",
                    decideTime: new Date(),
                    decideOrc: '运输处',
                    beginTime: new Date(117, 8, 10, 14, 23, 40),
                    endTime: new Date(118, 8, 10, 14, 23, 40),
                    licenseType: '国营',
                    businessSituation: '正常',
                    changeLicenseTimes: 0,
                    generalinfoChangeTimes: 0,
                    businessinfoChangeTimes: 0,
                    inputTotalCar: 20 + val,
                    inputTotalSeat: 54 + val,
                    totalLinePlate: 30 + val
            ).save(flush: true)
        }
    }

    private initSecurityData() {
        new Organization(name: '监控指挥中心', code: '01', parent: null).save(flush: true)
        new Organization(name: '办公室', code: '02', parent: null).save(flush: true)
        new Organization(name: '人事教育科', code: '03', parent: null).save(flush: true)
        new Organization(name: '信息科', code: '04', parent: null).save(flush: true)
        new Organization(name: '纪委', code: '05', parent: null).save(flush: true)
        new Organization(name: '工会', code: '06', parent: null).save(flush: true)
        new Organization(name: '财务室', code: '07', parent: null).save(flush: true)
        new Organization(name: '客运管理所', code: '08', parent: null).save(flush: true)
        new Organization(name: '货运管理所', code: '09', parent: null).save(flush: true)
        new Organization(name: '机动车维修管理所', code: '10', parent: null).save(flush: true)
        new Organization(name: '机动车驾驶员培训管理科', code: '11', parent: null).save(flush: true)
        new Organization(name: '证件管理科', code: '12', parent: null).save(flush: true)
        new Organization(name: '出租汽车管理所', code: '13', parent: null).save(flush: true)
        new Organization(name: '稽查大队', code: '14', parent: null).save(flush: true)
        new Organization(name: '法制科', code: '15', parent: null).save(flush: true)
        new Organization(name: '安全监督管理科', code: '16', parent: null).save(flush: true)
        new Organization(name: '瑶海道路运输管理所', code: '17', parent: null).save(flush: true)
        new Organization(name: '庐阳道路运输管理所', code: '18', parent: null).save(flush: true)
        new Organization(name: '蜀山道路运输管理所', code: '19', parent: null).save(flush: true)
        new Organization(name: '包河道路运输管理所', code: '20', parent: null).save(flush: true)
        new Organization(name: '新站道路运输管理所', code: '21', parent: null).save(flush: true)
        new Organization(name: '高新道路运输管理所', code: '22', parent: null).save(flush: true)
        new Organization(name: '经开道路运输管理所', code: '23', parent: null).save(flush: true)
        new Organization(name: '业户', code: '24', parent: null).save(flush: true)


//        (1..100).each {
//            new Role(authority: "ROLE_AAA${it}", name: "角色${it}", org: null).save(failOnError: true, flush: true)
//        }

        def companyRole = new Role(authority: 'ROLE_COMPANY_ROOT', name: '企业管理员', org: Organization.findByCode('24')).save(failOnError: true, flush: true)

        (1..100).each {
            def aaa = new User(username: "user${it}", passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: mockUsername(), org: Organization.findByCode('24'),companyCode: "C00000000${it%9}").save(failOnError: true, flush: true)
            UserRole.create aaa, companyRole, true
        }

        9.times { it ->
            def companyUser = new User(username: "company-${it}", passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: mockUsername(), org: Organization.findByCode('24'), companyCode: "C00000000${it}").save(failOnError: true, flush: true)
            UserRole.create companyUser, companyRole, true

        }


        def adminRole = new Role(authority: 'ROLE_ROOT', name: '超级管理员', org: null).save(failOnError: true, flush: true)
        def adminUser = new User(username: 'admin', passwordHash: 'admin123', salt: ValidationUtils.getSecureRandomSalt(), name: '管理员').save(failOnError: true, flush: true)
        UserRole.create adminUser, adminRole, true


        def controlRole = new Role(authority: 'ROLE_CONTROL_CENTER_ROOT', name: '监控指挥中心管理员', org: Organization.findByCode('01')).save(failOnError: true, flush: true)
        def controlUser = new User(username: 'center', passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: '运管指挥中心管理员', org: Organization.findByCode('01')).save(failOnError: true, flush: true)
        UserRole.create controlUser, controlRole, true



        def passengerSectionRole = new Role(authority: 'ROLE_PASSENGER_SECTION_ROOT', name: '客运管理所管理员', org: Organization.findByCode('08')).save(failOnError: true, flush: true)
        def passengerSectionUser = new User(username: 'passenger', passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: '客运管理所管理员', org: Organization.findByCode('08')).save(failOnError: true, flush: true)
        UserRole.create passengerSectionUser, passengerSectionRole, true

        //系统管理
        new PermissionGroup(url: '/organizations/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "组织机构管理", category: "系统管理", code: 'organization_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/roles/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "角色管理", category: "系统管理", code: 'role_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/permission-groups/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "权限管理", category: "系统管理", code: 'permission_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/operation_logs/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "操作日志", category: "系统管理", code: 'log_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/menus/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "菜单管理", category: "系统管理", code: 'menu_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/sysusers/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "用户管理", category: "系统管理", code: 'sysuser_manage').save(failOnError: true, flush: true)

        //基础信息
        new PermissionGroup(url: '/map-signs/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "路标管理", category: "基础信息", code: 'map_sign_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/system-codes/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "数据字典管理", category: "基础信息", code: 'system_code_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/configures/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "系统配置管理", category: "基础信息", code: 'configure_manage').save(failOnError: true, flush: true)

        new PermissionGroup(url: '/home', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_PASSENGER_SECTION_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "首页", category: "其他", code: 'home').save(failOnError: true, flush: true)

        //实时监控
        new PermissionGroup(url: '/cars/monitor/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "车辆实时", category: "实时监控", code: 'car_real_time_monitor').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/cars/control/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "车辆控制", category: "实时监控", code: 'car_real_time_control').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/cars/history/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "车辆历史", category: "实时监控", code: 'car_history').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/warnings/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "报警信息", category: "实时监控", code: 'warning').save(failOnError: true, flush: true)

        //工单
        new PermissionGroup(url: '/work-orders/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_PASSENGER_SECTION_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "工单列表", category: "工单", code: 'work_order_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/work-orders/pending/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_PASSENGER_SECTION_ROOT', httpMethod: null, name: "工单审批/研判", category: "工单", code: 'work_order_pending').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/work-orders/feedback/**', configAttribute: 'ROLE_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "工单反馈", category: "工单", code: 'work_order_feedback').save(failOnError: true, flush: true)

        //隐患整改单
        new PermissionGroup(url: '/hidden-rectification-orders/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_PASSENGER_SECTION_ROOT', httpMethod: null, name: "隐患整改单管理", category: "隐患整改单", code: 'hidden_rectification_order_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/hidden-rectification-orders/pending/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_PASSENGER_SECTION_ROOT', httpMethod: null, name: "整改单审核", category: "隐患整改单", code: 'hidden_rectification_order_pending').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/hidden-rectification-orders/feedback/**', configAttribute: 'ROLE_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "整改单反馈", category: "隐患整改单", code: 'hidden_rectification_order_feedback').save(failOnError: true, flush: true)

        //信息管理
        new PermissionGroup(url: '/platform-manages/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "平台管理", category: "信息管理", code: 'platform_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/owner-identitys/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "业户信息", category: "信息管理", code: 'owner_identity_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/black-lists/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "黑名单管理", category: "信息管理", code: 'black_list_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/white-lists/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "白名单管理", category: "信息管理", code: 'white_list_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/owner-check-records/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "查岗记录", category: "信息管理", code: 'owner_check_record_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/owner-check-records/manual', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "手动查岗", category: "信息管理", code: 'owner_check_record_manual').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/cars/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "车辆信息", category: "信息管理", code: 'car_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/people-basic-infos/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "人员信息", category: "信息管理", code: 'people_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/freight-routers/list', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "危货路线列表", category: "信息管理", code: 'freight_router_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/freight-routers/manage/**', configAttribute: 'ROLE_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "危货路线管理", category: "信息管理", code: 'freight_router_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/pass-line-business-basic-infos/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "客运路线", category: "信息管理", code: 'pass_line_business_basic_info_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/pass-line-physical-basic-infos/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "客运物理路线", category: "信息管理", code: 'pass_line_physical_basic_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/freight-waybills/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "危货电子路单", category: "信息管理", code: 'freight_waybill_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/company-regulations/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "管理制度", category: "信息管理", code: 'company_regulation_list').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/info-publish/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "信息发布", category: "信息管理", code: 'info_publish').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/info-check/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "信息审核", category: "信息管理", code: 'info_check').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/info-list/**/**', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "信息查询", category: "信息管理", code: 'info-list').save(failOnError: true, flush: true)

        //统计
        new PermissionGroup(url: '/check-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "查岗统计", category: "统计", code: 'check_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/work-order-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "工单统计", category: "统计", code: 'work_order_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/company-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "企业统计", category: "统计", code: 'company_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/passenger-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "班线客运车辆统计", category: "统计", code: 'passenger_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/travel-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "旅游包车车辆统计", category: "统计", code: 'travel_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/dangerous-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "旅游包车车辆统计", category: "统计", code: 'dangerous_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/car-basic-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "运营商统计", category: "统计", code: 'car_basic_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/alarm-info-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "报警信息统计", category: "统计", code: 'alarm_info_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/owner-identity-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "考核统计", category: "统计", code: 'owner_identity_statistic').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/platform-statistic', configAttribute: 'ROLE_ROOT,ROLE_CONTROL_CENTER_ROOT', httpMethod: null, name: "平台统计", category: "统计", code: 'platform_statistic').save(failOnError: true, flush: true)
    }

    private initMenu() {
        new Menu(name: '关闭全部', code: 'closeall', position: 'TOP_BAR', parent: null).save(flush: true)
        def topbar = new Menu(name: '个人中心', code: 'profile', style: 'hoverdown', position: 'TOP_BAR', parent: null).save(flush: true)
        new Menu(name: '修改密码', code: 'changepwd', position: 'TOP_BAR', parent: topbar).save(flush: true)
        new Menu(name: '退出', code: 'logout', position: 'TOP_BAR', parent: null).save(flush: true)

        new Menu(name: '首页', code: 'home', icon: 'fa-home', parent: null, position: 'SIDE_BAR', permissionCode: 'home').save(flush: true)

        def systemManage = new Menu(name: '系统管理', code: 'root-syscode', icon: 'fa-cogs', parent: null, position: 'SIDE_BAR', permissionCode: 'organization_manage;role_manage;permission_manage;sysuser_manage;log_manage;menu_manage').save(flush: true)
        new Menu(name: '组织机构', code: 'organization', icon: 'fa-sitemap', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'organization_manage').save(flush: true)
        new Menu(name: '角色', code: 'role', icon: 'fa-users', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'role_manage').save(flush: true)
        new Menu(name: '权限', code: 'permission', icon: 'fa-users', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'permission_manage').save(flush: true)
        new Menu(name: '用户', code: 'user', icon: 'fa-user', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'sysuser_manage').save(flush: true)
        new Menu(name: '操作日志', code: 'operationLog', icon: 'fa-table', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'log_manage').save(flush: true)
        new Menu(name: '菜单', code: 'menu', icon: 'fa-list', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'menu_manage').save(flush: true)

        def basicInfo = new Menu(name: '基础信息', code: 'root-basicinfo', icon: 'fa-wrench', parent: null, position: 'SIDE_BAR', permissionCode: 'map_sign_manage;system_code_manage;configure_manage').save(flush: true)
        new Menu(name: '路标管理', code: 'mapSign', icon: 'fa-map-marker', parent: basicInfo, position: 'SIDE_BAR', permissionCode: 'map_sign_manage').save(flush: true)
        new Menu(name: '数据字典', code: 'systemcode', icon: 'fa-book', parent: basicInfo, position: 'SIDE_BAR', permissionCode: 'system_code_manage').save(flush: true)
        new Menu(name: '系统配置', code: 'configure', icon: 'fa-cogs', parent: basicInfo, position: 'SIDE_BAR', permissionCode: 'configure_manage').save(flush: true)

        def monitorMenu = new Menu(name: '实时监控', code: 'root-monitor', icon: 'fa-eercast', parent: null, position: 'SIDE_BAR',permissionCode: 'car_real_time_monitor;car_real_time_control;car_history;warning').save(flush: true)
        new Menu(name: '车辆实时', code: 'realTimeMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR', permissionCode: 'car_real_time_monitor').save(flush: true)
        new Menu(name: '车辆控制', code: 'realTimeMonitorMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR', permissionCode: 'car_real_time_control').save(flush: true)
        new Menu(name: '车辆历史', code: 'historyMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR', permissionCode: 'car_history').save(flush: true)
        new Menu(name: '报警信息', code: 'warning', icon: 'fa-car', parent: monitorMenu, position: 'SIDE_BAR', permissionCode: 'warning').save(flush: true)

        def msgManage = new Menu(name: '信息管理', code: 'root-msgmanage', icon: 'fa-indent', parent: null, position: 'SIDE_BAR',permissionCode: 'owner_identity_manage;work_order_list').save(flush: true)
        new Menu(name: '平台管理', code: 'platformManage', icon: 'fa-columns', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'platform_manage').save(flush: true)
        new Menu(name: '查岗信息', code: 'ownerCheckRecord', icon: 'fa-hand-o-right', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'owner_check_record_list').save(flush: true)
        new Menu(name: '业户信息', code: 'ownerIdentity', icon: 'fa-building', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'owner_identity_manage').save(flush: true)
        new Menu(name: '车辆信息', code: 'carList', icon: 'fa-car', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'car_list').save(flush: true)
        new Menu(name: '人员信息', code: 'peopleList', icon: 'fa-group', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'people_list').save(flush: true)
        new Menu(name: '黑名单', code: 'blackList', icon: 'fa-file-text', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'black_list_manage').save(flush: true)
        new Menu(name: '白名单', code: 'whiteList', icon: 'fa-file-text-o', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'white_list_manage').save(flush: true)
        new Menu(name: '客运路线', code: 'passLineBusinessInfo', icon: 'fa-info', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'pass_line_business_basic_info_list').save(flush: true)
        new Menu(name: '客运物理路线', code: 'passLinePhysicalInfo', icon: 'fa fa-road', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'pass_line_physical_basic_list').save(flush: true)
        new Menu(name: '危货路线', code: 'waybillRoute', icon: 'fa-map-signs', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'freight_router_list').save(flush: true)
        new Menu(name: '危货电子路单', code: 'freightWaybill', icon: 'fa-list-alt', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'freight_waybill_list').save(flush: true)
        new Menu(name: '隐患整改单', code: 'hiddenDanger', icon: 'fa-hand-o-right', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'hidden_rectification_order_list').save(flush: true)
        new Menu(name: '整改单审核', code: 'orderExamine', icon: 'fa-hand-o-right', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'hidden_rectification_order_pending').save(flush: true)
        new Menu(name: '整改单反馈', code: 'enterpriseFeedback', icon: 'fa-hand-o-right', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'hidden_rectification_order_feedback').save(flush: true)
        new Menu(name: '信息发布', code: 'infoPublish', icon: 'fa-bullhorn', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'info_publish').save(flush: true)
        new Menu(name: '信息审核', code: 'infoCheck', icon: 'fa-check-square', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'info_check').save(flush: true)
        new Menu(name: '发布信息查询', code: 'infoList', icon: 'fa-envelope-square', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'info-list').save(flush: true)
        new Menu(name: '管理制度', code: 'companyRegulation', icon: 'fa-book', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'company_regulation_list').save(flush: true)
        new Menu(name: '工单列表', code: 'workOrder', icon: 'fa-sticky-note-o', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'work_order_list').save(flush: true)
        new Menu(name: '工单审批/研判', code: 'pendingWorkOrder', icon: 'fa-sticky-note-o', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'work_order_pending').save(flush: true)
        new Menu(name: '企业工单反馈', code: 'feedbackWorkOrder', icon: 'fa-sticky-note-o', parent: msgManage, position: 'SIDE_BAR', permissionCode: 'work_order_feedback').save(flush: true)

        def statisticMenu = new Menu(name: '统计', code: 'root-statistic', icon: 'fa-pie-chart', parent: null, position: 'SIDE_BAR',permissionCode: 'work_order_statistic').save(flush: true)
        new Menu(name: '查岗统计', code: 'checkStatistic', icon: 'fa-odnoklassniki', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'check_statistic').save(flush: true)
        new Menu(name: '工单统计', code: 'workOrderStatistic', icon: 'fa-file-text', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'work_order_statistic').save(flush: true)
        new Menu(name: '企业统计', code: 'companyReport', icon: 'fa-line-chart', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'company_statistic').save(flush: true)
        new Menu(name: '班线客运车辆统计', code: 'passengerStatistic', icon: 'fa-bus', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'passenger_statistic').save(flush: true)
        new Menu(name: '旅游包车车辆统计', code: 'travelStatistic', icon: 'fa-car', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'travel_statistic').save(flush: true)
        new Menu(name: '危险品车辆统计', code: 'dangerousStatistic', icon: 'fa-exclamation-triangle', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'dangerous_statistic').save(flush: true)
        new Menu(name: '运营商统计', code: 'carBasicStatistics', icon: 'fa-car', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'car_basic_statistic').save(flush: true)
        new Menu(name: '报警信息统计', code: 'alarmInfoStatistics', icon: 'fa-list', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'alarm_info_statistic').save(flush: true)
        new Menu(name: '考核统计', code: 'ownerIdentityStatistics', icon: 'fa-list-alt', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'owner_identity_statistic').save(flush: true)
        new Menu(name: '平台统计', code: 'platformStatisticComponent', icon: 'fa-list-alt', parent: statisticMenu, position: 'SIDE_BAR', permissionCode: 'platform_statistic').save(flush: true)
    }

    private initWorkOrder() {
        def flow11 = new WorkOrderFlow(alarmType: AlarmType.findByCodeNum('202'), flowVersion: 1, enabled: true, flows: [])

        [[role: 'ROLE_CONTROL_CENTER_ROOT', name: '初审', action: 'SP']
         , [role: 'ROLE_PASSENGER_SECTION_ROOT', name: '复审', action: 'SP']
         , [role: 'ROLE_COMPANY_ROOT', name: '企业反馈', action: 'FK']
         , [role: 'ROLE_CONTROL_CENTER_ROOT', name: '研判', action: 'YP']].each {
            flow11.flows << it
        }
        flow11.save(flush: true)



//        def flow12 = new WorkOrderFlow(alarmType: AlarmType.findByCodeNum('202'), flowVersion: 2, flows: [])
//        [[role: 'ROLE_CONTROL_CENTER_ROOT', name: '审批', action: 'SP']
//         , [role: 'ROLE_COMPANY_ROOT', name: '企业反馈', action: 'FK']
//         , [role: 'ROLE_CONTROL_CENTER_ROOT', name: '研判', action: 'YP']].each {
//            flow12.flows << it
//        }
//        flow12.save(flush: true)

//        def flow21 = new WorkOrderFlow(alarmType: AlarmType.findByCodeNum('205'), flowVersion: 2, enabled: true, flows: [])
//        [[role: 'ROLE_CONTROL_CENTER_ROOT', name: '审批', action: 'SP']
//         , [role: 'ROLE_COMPANY_ROOT', name: '企业反馈', action: 'FK']
//         , [role: 'ROLE_CONTROL_CENTER_ROOT', name: '研判', action: 'YP']].each {
//            flow21.flows << it
//        }
//        flow21.save(flush: true)


        def saveOrder = { flow, flowStep, snTitle, snIndex, companyIndex ->
            WorkOrder temp = new WorkOrder(
                    sn: "${snTitle}${snIndex}"
                    , alarmType: flow.alarmType
                    , alarmLevel: AlarmLevel.SERIOUS
                    , companyCode: "C00000000${companyIndex}"
                    , ownerName: companys[companyIndex]
                    , operateManager: "吴珊"
                    , phone: "010-${companyIndex}2425722"
                    , flows: []
                    , flowStep: flowStep
                    , todoRole: flow.flows[flowStep - 1].role
                    , checkTime: new Date()
                    , rectificationTime: new Date() + 5
                    , note: "${flow.alarmType.name}"
                    , status: WorkOrderFlowAction.valueOf(flow.flows[flowStep - 1].action).workOrderStatus)

            flow.flows.each {
                temp.flows << it
            }
            temp.save(flush: true)
        }

        100.times { it ->
            saveOrder(flow11, 1, '20170730001', it, 1)
//            saveOrder(flow21, 2, '20170730005', it, 1)
        }
        9.times {it->
            saveOrder(flow11, 3, '20170730002', it, it)
//            saveOrder(flow21, 2, '20170730003', it, it)
            saveOrder(flow11, 3, '20170730004', it, 1)
        }
    }


    private initPassLinePhysicalBasicInfo() {
        new PassLinePhysicalBasicInfo(lineCode: '01', lineName: '线路1', modifyTime: new Date(), businessArea: '省际', lineType: '二类客运班线', startPlace: '西安', endPlace: '合肥', mainPoint: '咸阳 郑州', startAdminDivsionCode: '01', startAdminDivsionName: '西安市', endAdminDivsionCode: '02', endAdminDivsionName: '合肥市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '02', lineName: '线路2', modifyTime: new Date(), businessArea: '市际', lineType: '一类客运班线', startPlace: '合肥', endPlace: '西安', mainPoint: '郑州 咸阳', startAdminDivsionCode: '01', startAdminDivsionName: '合肥市', endAdminDivsionCode: '02', endAdminDivsionName: '西安市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '03', lineName: '线路3', modifyTime: new Date(), businessArea: '县际', lineType: '三类客运班线', startPlace: '西安', endPlace: '合肥', mainPoint: '咸阳 郑州', startAdminDivsionCode: '01', startAdminDivsionName: '西安市', endAdminDivsionCode: '02', endAdminDivsionName: '合肥市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '04', lineName: '线路4', modifyTime: new Date(), businessArea: '省际', lineType: '一类客运班线', startPlace: '西安', endPlace: '合肥', mainPoint: '咸阳 郑州', startAdminDivsionCode: '01', startAdminDivsionName: '西安市', endAdminDivsionCode: '02', endAdminDivsionName: '合肥市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '05', lineName: '线路5', modifyTime: new Date(), businessArea: '国际', lineType: '二类客运班线', startPlace: '西安', endPlace: '合肥', mainPoint: '咸阳 郑州', startAdminDivsionCode: '01', startAdminDivsionName: '西安市', endAdminDivsionCode: '02', endAdminDivsionName: '合肥市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '06', lineName: '线路6', modifyTime: new Date(), businessArea: '省际', lineType: '四类客运班线', startPlace: '西安', endPlace: '合肥', mainPoint: '咸阳 郑州', startAdminDivsionCode: '01', startAdminDivsionName: '西安市', endAdminDivsionCode: '02', endAdminDivsionName: '合肥市', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
        new PassLinePhysicalBasicInfo(lineCode: '07', lineName: '线路7', modifyTime: new Date(), businessArea: '省内', lineType: '一类客运班线', startPlace: '合肥', endPlace: '北京', mainPoint: '郑州 天津', startAdminDivsionCode: '01', startAdminDivsionName: '合肥市', endAdminDivsionCode: '02', endAdminDivsionName: '北京', lineMileAge: 100.3, highwayMileAge: 20.5, percentage: 0.2, highwayEntry: 'a1', highwayExit: 'a2', highway: true, villageLine: false, travelLine: true, busLine: false).save(flush: true)
    }

    private initBlackAndWhite() {
        new BlackList(vehicleNo: cars[0], frameNo: getFrameNo(0), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 7, 13), controlEnd: new Date(117, 7, 20)).save(flush: true)
        new BlackList(vehicleNo: cars[53], frameNo: getFrameNo(53), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 12), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: cars[23], frameNo: getFrameNo(23), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 7, 22), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: cars[75], frameNo: getFrameNo(75), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 1), controlEnd: new Date(117, 8, 10)).save(flush: true)
        new BlackList(vehicleNo: cars[12], frameNo: getFrameNo(12), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 20), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: cars[17], frameNo: getFrameNo(17), blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 9, 12), controlEnd: new Date(117, 9, 17)).save(flush: true)

        new WhiteList(vehicleNo: cars[21], frameNo: getFrameNo(21), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: cars[12], frameNo: getFrameNo(12), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: cars[34], frameNo: getFrameNo(34), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: cars[31], frameNo: getFrameNo(31), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: cars[22], frameNo: getFrameNo(22), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: cars[8], frameNo: getFrameNo(8), controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
    }

    private initWarning() {
        new Warning(frameNo: getFrameNo(0), carLicenseNo: cars[0], carColor: '白色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(56), carLicenseNo: cars[56], carColor: '黑色', warningSource: 2, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(6), carLicenseNo: cars[6], carColor: '银色', warningSource: 3, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(11), carLicenseNo: cars[11], carColor: '白色', warningSource: 9, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 3).save(flush: true)
        new Warning(frameNo: getFrameNo(25), carLicenseNo: cars[25], carColor: '白色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(2), carLicenseNo: cars[2], carColor: '黑色', warningSource: 3, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 2).save(flush: true)
        new Warning(frameNo: getFrameNo(0), carLicenseNo: cars[0], carColor: '白色', warningSource: 2, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(31), carLicenseNo: cars[31], carColor: '白色', warningSource: 1, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 5).save(flush: true)
        new Warning(frameNo: getFrameNo(12), carLicenseNo: cars[12], carColor: '蓝色', warningSource: 3, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(52), carLicenseNo: cars[52], carColor: '黑色', warningSource: 3, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 3).save(flush: true)
        new Warning(frameNo: getFrameNo(21), carLicenseNo: cars[21], carColor: '红色', warningSource: 2, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(3), carLicenseNo: cars[3], carColor: '红色', warningSource: 9, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 2).save(flush: true)
        new Warning(frameNo: getFrameNo(7), carLicenseNo: cars[7], carColor: '白色', warningSource: 2, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(32), carLicenseNo: cars[32], carColor: '红色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(21), carLicenseNo: cars[21], carColor: '白色', warningSource: 3, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: getFrameNo(64), carLicenseNo: cars[64], carColor: '红色', warningSource: 9, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)
    }

    private initPeople() {
        new PeopleBasicInfo(companyCode: 'C000000001', name: '张敏', gender: '女', idCardNo: '34212519870314673x', picture: '', nation: '汉', nativePlace: '安徽合肥', phoneNo: '15105512743', address: '', email: 'test1@163.com', postCode: '340101', educationLevel: '大专', technologyTitle: '一级', healthState: '健康', birthday: new Date()).save(flush: true)
        new PeopleBasicInfo(companyCode: 'C000000002', name: '吴珊', gender: '男', idCardNo: '34132519870314222x', picture: '', nation: '汉', nativePlace: '安徽巢湖', phoneNo: '15703272743', address: '', email: 'test2@163.com', postCode: '340001', educationLevel: '本科', technologyTitle: '一级', healthState: '健康', birthday: new Date()).save(flush: true)
        new WorkerCheckMember(companyCode: 'C000000001', idCardNo: '34212519870314673x', workLicenseType: '货运', workLicenseNo: 'AH2016', workLicenseGrantTime: new Date(), workLicenseGetTime: new Date(), endTime: new Date(), licenseGrantOrg: '合肥XXX运输有限公司', licenseSituation: '可用', licenseChangeTimes: 2, trainTimes: 2, checkType: '1').save(flush: true)
        new WorkerManager(companyCode: 'C000000002', idCardNo: '34132519870314222x', workLicenseGrantTime: new Date(), workLicenseType: '押运装卸', ownerName: '合肥市汽车客运XXX', driveCarType: '中小型客运汽车', trafficAccidentRecordNo: 0, endTime: new Date(), licenseGrantOrg: '合肥市交通xxx').save(flush: true)
        new WorkerTechnology(companyCode: 'C000000001', idCardNo: '34212519870314673x', ownerName: '合肥市汽车客运XXX').save(flush: true)
        new WorkerWaiter(idCardNo: '34132519870314222x', jobName: '后勤管理', jobLicenseNo: 'xxxx', grantTime: new Date(), beginWorkTime: new Date()).save(flush: true)
        new WorkerCoach(companyCode: 'C000000001', idCardNo: '34212519870314673x', workLicenseType: '教练', workLicenseNo: 'CAH1234', workLicenseGetTime: new Date(), workLicenseGrantTime: new Date(), endTime: new Date(), driveLicenseNo: '34212519870314673x', driveCarType: '小汽车', driveLicenseGetTime: new Date(), licenseGrantOrga: '合肥市新亚驾校', licenseSituation: '可用', workSituation: '中级教练', ownerName: '合肥市新亚驾校', businessPermitCharacter: '3', businessPermitNo: '34xxx', changeTimes: 0, trainTimes: 1, inspectDealSituation: '', teachType: 'C照', driveLicenseSituation: '可用').save(flush: true)
        new WorkerDriver(companyCode: 'C000000002', idCardNo: '34132519870314222x', workLicenseGrantTime: new Date(), workLicenseType: '驾驶员', ownerName: '合肥市汽车客运XXX', driveCarType: '客运汽车', trafficAccidentRecordNo: 0, endTime: new Date(), driveLicenseNo: '34132519870314222x', businessPermitCharacter: 'x').save(flush: true)
    }

    private initCar() {
        def carType = ['班线客车', '旅游包车', '危险品运输车']
        Date date = new Date()
        cars.eachWithIndex{ String entry, int i ->
            new CarBasicInfo(modifyTime: date - i
                    , licenseNo: entry
                    , carPlateColor: '黄色'
                    , brand: '比亚迪'
                    , model: 'SS'
                    , carType: carType[i%3]
                    , passengerLevel: '一级'
                    , carColor: '白色'
                    , engNo: "eng${i}"
                    , frameNo: getFrameNo(i)
                    , carIdentityCode: "${i}"
                    , seatNo: 15
                    , carTonnage: 2.1
                    , carBoxNo: 4
                    , volume: 50.2
                    , fuelType: "氢气"
                    , engPower: 32.2
                    , leaveFactoryTime: date - 500
                    , buyCarTime: date - 300
                    , settleTime: date - 280
                    , wheelbase: 1
                    , carLength: 5
                    , carHeight: 2
                    , carWidth: 1
                    , carSmokeNo: 4
                    , leafSpringNo: 8
                    , tractionTonnage: 80
                    , picture: "11111").save(flush: true)

            new CarBasicOperate(
                    frameNo: getFrameNo(i)
                    , modifyTime: date - i
                    , carfileRecordNo: "${i}".padLeft(10, '0')
                    , ownerName: companys[i%9]
                    , ownerCode: "C00000000${i%9}"
                    , businessLicenseCharacter: "JYXKZ字${i}"
                    , businessLicenseNo: "JYXKZ号${i}"
                    , transformLicenseCharacter: "DLYSZ字${i}"
                    , transformLicenseNo: "FLYSZ号${i}"
                    , transformLicenseMedium: "输证介质${i}"
                    , transformLicensePhysicalno: "000${i}"
                    , grantOrg: "运管中心"
                    , beginTime: date - 100
                    , endTime: date + 300
                    , dutyTonnage: 20
                    , dutySeat: 15
                    , businessType: carType[i % 3]
                    , businessScope: carType[i % 3]
                    , carOperateSituation: "01"
                    , secondMaintainSituation: "02"
                    , checkMaintainTimes: 20
                    , changeLicenseTimes: 1
                    , bigMaintainTimes: 0
                    , carTechnologyLevel: "一级"
                    , finePaySituation: '1' as char
                    , inspectDealSituation: '1' as char
                    , trafficAccidentTimes: 1
                    , insureSituation: '01'
                    , yearExamineSituation: '01'
                    , exitOrEntry: 1
                    , lineMarketNo: '0001'
                    , communicationTool: 1
                    , communicationNo: '88888888'
                    , driveRecorder: 1
                    , locator: 1
                    , driveRecorderSituation: '01'
                    , locatorSituation: '01'
                    ,).save(flush: true)
        }
    }

    private initCheckRecord() {
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '2+3=?', answer: '5', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 30 * 1000), responseContent: '5', responseTime: 30).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: 'C000000002', question: '5+8=?', answer: '13', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '10-1=?', answer: '9', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 200 * 1000),
                responseContent: '9', responseTime: 200).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '2x3=?', answer: '6', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 20 * 1000),
                responseContent: '6', responseTime: 20).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000003', question: '10÷5=?', answer: '2', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 76 * 1000),
                responseContent: '2', responseTime: 76).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: 'C000000001', question: '1x10=?', answer: '10', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: 'C000000001', question: '2x2=?', answer: '4', responsed: true,
                responseDate: new Date(new Date().getTime() + 100 * 1000),
                responseContent: '4', responseTime: 100).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: 'C000000005', question: '1+8=?', answer: '9', responsed: true,
                responseDate: new Date(new Date().getTime() + 121 * 1000),
                responseContent: '9', responseTime: 121).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000005', question: '1x10=?', answer: '10', responsed: false,
                operator: centerUser,).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '8-1=?', answer: '7', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 190 * 1000),
                responseContent: '7', responseTime: 190).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '12÷3=?', answer: '4', responsed: true,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '12÷3=?', answer: '5', responsed: false,
                operator: centerUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
    }

    private initPlatformManage() {
        new PlatformManage(ip: '192.168.1.24', port: '4233', name: '云联城市交通', code: 'K001', contactName: '李娜', contactPhone: '13052736784').save(flush: true)
        new PlatformManage(ip: '61.123.1.15', port: '2001', name: '合肥客运平台', code: 'K002', contactName: '王平', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '62.23.1.15', port: '2011', name: '合肥汽车客运有限公司', code: 'K003', contactName: '张敏', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '63.42.1.16', port: '2021', name: '合肥新亚汽车客运公司', code: 'K004', contactName: '吴霞', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '64.22.1.16', port: '2031', name: '长丰县宏业汽车客运有限公司', code: 'K005', contactName: '王珊珊', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '65.132.1.17', port: '2041', name: '合肥汽车客运总公司第七客运公司', code: 'K006', contactName: '李莎', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '66.52.1.18', port: '2051', name: '合肥汽车客运总公司第八客运公司', code: 'K007', contactName: '张磊', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '67.53.1.18', port: '2061', name: '合肥客运旅游汽车公司', code: 'K008', contactName: '张娜', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '68.24.1.2', port: '2071', name: '肥西汽车站严店客运站', code: 'K009', contactName: '张晓霞', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '69.12.1.34', port: '2081', name: '合肥客运总公司四合汽车站', code: 'K022', contactName: '吴丽萍', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '611.14.1.24', port: '2091', name: '安徽百众旅游客运有限公司', code: 'K202', contactName: '李建国', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '612.12.1.1', port: '2101', name: '安徽飞雁快速客运公司', code: 'K302', contactName: '张珊米', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '613.16.1.41', port: '2201', name: '宁国光正合肥分公司', code: 'K402', contactName: '安娜', contactPhone: '13023429743').save(flush: true)
        new PlatformManage(ip: '614.17.1.42', port: '2301', name: '平安四方有限公司', code: 'K502', contactName: '郑磊国', contactPhone: '13023429743').save(flush: true)
    }

    private initMapSign() {
        def MapSignType1 = new MapSignType(name: "国企", codeNum: "100", parent: null).save(flush: true)
        def childMapType1 = new MapSignType(name: "银行部门", codeNum: "101", parent: MapSignType1).save(flush: true)
        new MapSign(name: '中国农业银行', mapSignType: childMapType1, longitude: 110.4578914, latitude: 30.7542289, display: true).save(flush: true)
        new MapSign(name: '中国工商银行', mapSignType: childMapType1, longitude: 112.6458796, latitude: 28.1578542, display: false).save(flush: true)
        new MapSign(name: '中国建设银行', mapSignType: childMapType1, longitude: 135.7851562, latitude: 19.9425723, display: true).save(flush: true)
        def childMapType2 = new MapSignType(name: "政府部门", codeNum: "102", parent: MapSignType1).save(flush: true)
        new MapSign(name: '税务局', mapSignType: childMapType2, longitude: 120.4578914, latitude: 27.7542289, display: true).save(flush: true)
        new MapSign(name: '法院', mapSignType: childMapType2, longitude: 127.6458796, latitude: 39.1578542, display: true).save(flush: true)
        new MapSign(name: '教育局', mapSignType: childMapType2, longitude: 119.7851562, latitude: 25.9425723, display: true).save(flush: true)
        def childMapType3 = new MapSignType(name: "能源部门", codeNum: "103", parent: MapSignType1).save(flush: true)
        new MapSign(name: '中国石油', mapSignType: childMapType3, longitude: 135.8546758, latitude: 28.6497285, display: true).save(flush: true)
        new MapSign(name: '中国电网', mapSignType: childMapType3, longitude: 109.7548964, latitude: 50.1785436, display: false).save(flush: true)
        new MapSign(name: '中国矿业', mapSignType: childMapType3, longitude: 119.3586457, latitude: 32.9427851, display: true).save(flush: true)

        def MapSignType2 = new MapSignType(name: "私企", codeNum: "200", parent: null).save(flush: true)
        def childMapType4 = new MapSignType(name: "有限公司", codeNum: "201", parent: MapSignType2).save(flush: true)
        new MapSign(name: '兴乐集团有限公司', mapSignType: childMapType4, longitude: 100.4578914, latitude: 30.1937586, display: true).save(flush: true)
        new MapSign(name: '星月集团有限公司', mapSignType: childMapType4, longitude: 117.6458796, latitude: 28.1578572, display: true).save(flush: true)
        new MapSign(name: '森马企业有限公司', mapSignType: childMapType4, longitude: 135.4824311, latitude: 19.7896541, display: false).save(flush: true)
        new MapSign(name: '兴乐集团有限公司', mapSignType: childMapType4, longitude: 110.9435725, latitude: 30.3571598, display: true).save(flush: true)
        new MapSign(name: '苏宁电器连锁集团股份有限公司', mapSignType: childMapType4, longitude: 114.6458796, latitude: 29.1578542, display: true).save(flush: true)
        new MapSign(name: '澄星实业集团公司', mapSignType: childMapType4, longitude: 125.7851562, latitude: 30.9425723, display: true).save(flush: true)

        def childMapType5 = new MapSignType(name: "集团", codeNum: "202", parent: MapSignType2).save(flush: true)
        new MapSign(name: '正泰集团', mapSignType: childMapType5, longitude: 100.1578914, latitude: 31.1937586, display: false).save(flush: true)
        new MapSign(name: '新希望集团', mapSignType: childMapType5, longitude: 118.6458796, latitude: 19.1578572, display: false).save(flush: true)
        new MapSign(name: '报喜鸟集团', mapSignType: childMapType5, longitude: 135.8424311, latitude: 29.7896541, display: true).save(flush: true)
        new MapSign(name: '金州集团', mapSignType: childMapType5, longitude: 111.9435725, latitude: 33.3571598, display: true).save(flush: true)
    }

    private initRegisterReport() {
        new RegisterReport(caseRegisterNo: "2017080100001", companyCode: "C000000001", idCardNo: "421553").save(flush: true)
        new RegisterReport(caseRegisterNo: "2017080200002", companyCode: "C000000001", idCardNo: "2345").save(flush: true)
    }

    private initRegistrationInformationCarinfo() {
        cars.eachWithIndex{ String entry, int i ->
            new RegistrationInformationCarinfo(
                    vehicleNo: entry
                    , vehicleColor: '白色'
                    , plateformId: "plateform${i}"
                    , producerId: "producer${i}"
                    , terminalModelType: "ACRII型号"
                    , terminalId: "terminal${i}"
                    , terminalSimcode: "136458" + "${i}".padLeft(5, '0')
                    , frameNo: getFrameNo(i)).save(flush: true)
        }
    }

    private getFrameNo(int i){
        "frameNo"+"${i}".padLeft(5, '0')
    }

    private initCompanyAndRegulation() {
        new OwnerIdentity(ownerName: "安徽省合肥汽车客运有限公司"
                , companyCode: "C000000000"
                , ownerCode: "C000000000"
                , ownerAddress: "明光路168号"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥新亚汽车客运公司"
                , companyCode: "C000000001"
                , ownerCode: "C000000001"
                , ownerAddress: "胜利路大窑湾1号附近"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "64212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-88763722"
                , telephone: "15387676253"
                , email: "xinya@163.com"
                , website: "http://www.xinya.com"
                , shortName: '新亚汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥长丰县宏业汽车客运有限公司"
                , companyCode: "C000000002"
                , ownerCode: "C000000002"
                , ownerAddress: "濉溪东路9号嘉华中心A座15层"
                , postCode: 230000
                , administrativeDivisionName: '长丰县'
                , administrativeDivisionCode: 340201
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "612125198703146735"
                , operateManager: mockUsername()
                , phone: "010-88973622"
                , telephone: "13387673452"
                , email: "hongye@163.com"
                , website: "http://www.hongye.com"
                , shortName: '宏业客运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥客运旅游汽车公司"
                , companyCode: "C000000003"
                , ownerCode: "C000000003"
                , ownerAddress: "安徽省合肥市瑶海区大窑湾路5号"
                , postCode: 230000
                , administrativeDivisionName: '瑶海区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "hk@163.com"
                , website: "http://www.hk.com"
                , shortName: '合肥客运').save(flush: true)
        new OwnerIdentity(ownerName: "锐致货运公司"
                , companyCode: "C000000004"
                , ownerCode: "C000000004"
                , ownerAddress: "锐致货运公司(合肥总部)"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥齐力货运公司"
                , companyCode: "C000000005"
                , ownerCode: "C000000005"
                , ownerAddress: "莱茵河畔花园10幢"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥铂达货运公司"
                , companyCode: "C000000006"
                , ownerCode: "C000000006"
                , ownerAddress: "石台路118号附近"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥远征货运公司"
                , companyCode: "C000000007"
                , ownerCode: "C000000007"
                , ownerAddress: "047县道附近"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)
        new OwnerIdentity(ownerName: "合肥聚贤货运有限责任公司"
                , companyCode: "C000000008"
                , ownerCode: "C000000008"
                , ownerAddress: "新安江路漕冲物流园南门"
                , postCode: 230000
                , administrativeDivisionName: '蜀山区'
                , administrativeDivisionCode: 340101
                , economicType: '私营经济'
                , legalRepresentative: mockUsername()
                , idCardType: '居民身份证'
                , idCardNo: "54212519870314673x"
                , operateManager: mockUsername()
                , phone: "010-82425722"
                , telephone: "15387673452"
                , email: "wushan@163.com"
                , website: "http://www.xiaojukeji.com"
                , shortName: '合肥汽运').save(flush: true)


        companys.eachWithIndex{val,index->
            new OwnerManageInfo(
                    companyCode: "C00000000${index}",
                    licenseCharacter: "字:00000${index}",
                    licenseNo: "3795${index}",
                    beginTime: new Date()-index*100,
                    endTime: new Date(117, 8 + index, 10 + index),
                    grantOrganization: "合肥市道路运输管理处",
                    checkTime: new Date()-2*index,
                    domicileOperateName: "蜀山区户籍所",
                    domicileOperateNo: "0001",
                    paymentSituation: "1",
                    licenseChangeTimes: "${index+1}",
                    inspectTreatmentSituation: "2"
            ).save(flush: true)

            new CompanyRegulation(
                    companyCode: "C00000000${index}",
                    ownerName: "${val}",
                    regulationName: "${val}规章制度",
                    fileName: "${val}规章制度",
                    fileType: "doc",
                    fileSize: 9801.0
            ).save(flush: true)
        }
    }

    private initHiddenRectificationOrder() {
        new HiddenRectificationOrder(billNo: '20170730001', enterprise: companys[0], companyCode: 'C000000000', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 5, insPosition: '所属公司', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730002', enterprise: companys[1], companyCode: 'C000000001', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 6, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730003', enterprise: companys[2], companyCode: 'C000000002', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 7, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '无验证', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730004', enterprise: companys[3], companyCode: 'C000000003', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 8, insPosition: '所属公司', insDesc: '例行巡检', insQuestion: '无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730005', enterprise: companys[1], companyCode: 'C000000001', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 9, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730006', enterprise: companys[0], companyCode: 'C000000000', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 5, insPosition: '所属公司', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730007', enterprise: companys[1], companyCode: 'C000000001', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 6, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730008', enterprise: companys[2], companyCode: 'C000000002', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 7, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '无验证', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730009', enterprise: companys[3], companyCode: 'C000000003', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 8, insPosition: '所属公司', insDesc: '例行巡检', insQuestion: '无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730010', enterprise: companys[1], companyCode: 'C000000001', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 9, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730011', enterprise: companys[2], companyCode: 'C000000002', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 7, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '无验证', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730012', enterprise: companys[3], companyCode: 'C000000003', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 8, insPosition: '所属公司', insDesc: '例行巡检', insQuestion: '无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730013', enterprise: companys[1], companyCode: 'C000000001', examiner: mockUsername(), inspectionDate: new Date(), dealineDate: new Date() + 9, insPosition: '中转站', insDesc: '例行巡检', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
    }

    private initConfigure() {
        new Configure(configKey: 'carRateAlarm', configValue: '100', name: '车辆入网率告警阈值',note: '单位：%').save(flush: true)
    }
}