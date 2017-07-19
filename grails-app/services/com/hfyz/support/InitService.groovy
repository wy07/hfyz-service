package com.hfyz.support

import com.hfyz.platform.PlatformManage
import com.hfyz.car.CarBasicInfo
import com.hfyz.car.CarBasicOperate
import com.hfyz.warning.Warning
import grails.transaction.Transactional
import com.hfyz.security.User
import com.hfyz.security.Role
import com.hfyz.security.UserRole
import com.hfyz.security.PermissionGroup

@Transactional
class InitService {

    def initData() {
        def rootPermission = new PermissionGroup(name: '所有权限', permissions: '*:*').save(flush: true)
        def adminRole = new Role(authority: 'ROLE_ADMIN', name: '平台管理员', permissionGroups: [rootPermission]).save()

        def testUser = new User(username: 'admin', passwordHash: 'admin123',salt:User.getSecureRandomSalt(), name: '管理员').save(failOnError:true)

        UserRole.create testUser, adminRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }
            
        new Menu(name: '关闭全部', code: 'closeall', position: 'TOP_BAR', parent: null).save(flush: true)
        def topbar = new Menu(name: '个人中心', code: 'profile', style: 'hoverdown', position: 'TOP_BAR', parent: null).save(flush: true)
        new Menu(name: '修改密码', code: 'changepwd', position: 'TOP_BAR', parent: topbar).save(flush: true)
        new Menu(name: '退出', code: 'logout', position: 'TOP_BAR', parent: null).save(flush: true)

        def homemenu = new Menu(name: '首页', code: 'home', icon: 'fa-home', parent: null, position: 'SIDE_BAR').save(flush: true)
        new PermissionGroup(name: '浏览', permissions: 'view', menu: homemenu).save(flush: true)

        def sidebar = new Menu(name: '系统管理', code: 'root-syscode', icon: 'fa-wrench', parent: null, position: 'SIDE_BAR').save(flush: true)
        def roleMenu = new Menu(name: '角色', code: 'role', icon: 'fa-users', parent: sidebar, position: 'SIDE_BAR').save(flush: true)
        new PermissionGroup(name: '新增', permissions: 'create', menu: roleMenu).save(flush: true)
        new PermissionGroup(name: '修改', permissions: 'edit', menu: roleMenu).save(flush: true)
        new PermissionGroup(name: '删除', permissions: 'delete', menu: roleMenu).save(flush: true)
        new PermissionGroup(name: '分配权限', permissions: 'assign', menu: roleMenu).save(flush: true)
        new PermissionGroup(name: '浏览', permissions: 'view', menu: roleMenu).save(flush: true)

        def userMenu = new Menu(name: '用户', code: 'user', icon: 'fa-user', parent: sidebar, position: 'SIDE_BAR').save(flush: true)
        new PermissionGroup(name: '新增', permissions: 'create', menu: userMenu).save(flush: true)
        new PermissionGroup(name: '修改', permissions: 'edit', menu: userMenu).save(flush: true)
        new PermissionGroup(name: '删除', permissions: 'delete', menu: userMenu).save(flush: true)
        new PermissionGroup(name: '浏览', permissions: 'view', menu: userMenu).save(flush: true)
        def menu = new Menu(name: '菜单', code: 'menu', icon: 'fa-list', parent: sidebar, position: 'SIDE_BAR').save(flush: true)
        new PermissionGroup(name: '新增', permissions: 'create', menu: menu).save(flush: true)
        new PermissionGroup(name: '修改', permissions: 'edit', menu: menu).save(flush: true)
        new PermissionGroup(name: '删除', permissions: 'delete', menu: menu).save(flush: true)
        new PermissionGroup(name: '浏览', permissions: 'view', menu: menu).save(flush: true)
        new Menu(name: '数据字典', code: 'systemcode', icon: 'fa-book', parent: sidebar, position: 'SIDE_BAR').save(flush: true)

        def logMenu = new Menu(name: '日志管理', code: 'root-logManage', icon: 'fa-list-alt', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '操作日志', code: 'operationLog', icon: 'fa-table', parent: logMenu, position: 'SIDE_BAR').save(flush: true)

        def monitorMenu = new Menu(name: '联网联控', code: 'root-monitor', icon: 'fa-eercast', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '车辆信息', code: 'carList', icon: 'fa-car', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '实时状态', code: 'realTimeMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '实时监控', code: 'realTimeMonitorMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '历史轨迹', code: 'historyMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
//        new Menu(name: '其他地图', code: 'otherMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '报警信息', code: 'warning', icon: 'fa-car', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Warning(frameNo: 'LDC92839882929345', carLicenseNo: '皖A-2329J', carColor: '白色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC32552378754654', carLicenseNo: '皖A-2934W', carColor: '黑色', warningSource: 2, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC43657563453234', carLicenseNo: '皖A-2239Q', carColor: '银色', warningSource: 3, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC62324346329643', carLicenseNo: '皖A-2149C', carColor: '红色', warningSource: 9, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)

        def infoManage = new Menu(name: '信息管理', code: 'root-infomanage', icon: 'fa-laptop', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '信息发布', code: 'infoPublish', icon: 'fa-bullhorn', parent: infoManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '信息审核', code: 'infoCheck', icon: 'fa-check-square', parent: infoManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '信息列表', code: 'infoList', icon: 'fa-envelope-square', parent: infoManage, position: 'SIDE_BAR').save(flush: true)

        new Menu(name: '组织机构', code: 'organization', icon: 'fa-sitemap', parent: sidebar, position: 'SIDE_BAR').save(flush: true)

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


        Organization organizationP = new Organization(name: '运管中心', code: '100', parent: null).save(flush: true)
        new Organization(name: '货运管理所', code: '110', parent: organizationP).save(flush: true)
        new Organization(name: '客运管理所', code: '120', parent: organizationP).save(flush: true)

        organizationP = new Organization(name: '业务', code: '200', parent: null).save(flush: true)
        new Organization(name: '信息科', code: '210', parent: organizationP).save(flush: true)
        new Organization(name: '法制科', code: '220', parent: organizationP).save(flush: true)
        new Organization(name: '安全监督科', code: '230', parent: organizationP).save(flush: true)

        organizationP = new Organization(name: '运营企业', code: '300', parent: null).save(flush: true)
        new Organization(name: '客运', code: '310', parent: organizationP).save(flush: true)
        new Organization(name: '货运', code: '320', parent: organizationP).save(flush: true)


        def carType = ['班线客车', '旅游包车', '危险品运输车']
        50.times { val ->
            Date date = new Date()
            new CarBasicInfo(modifyTime: date - val
                    , licenseNo: "陕A-CK" + "${val}".padLeft(4, '0')
                    , carPlateColor: '黄色'
                    , brand: '比亚迪'
                    , model: 'SS'
                    , carType: "大型客车"
                    , passengerLevel: '一级'
                    , carColor: '白色'
                    , engNo: "eng${val}"
                    , frameNo: "frameNo${val}"
                    , carIdentityCode: "${val}"
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
                    frameNo: "frameNo${val}"
                    , modifyTime: date - val
                    , carfileRecordNo: "${val}".padLeft(10, '0')
                    , ownerName: "企业${val % 4}"
                    , businessLicenseCharacter: "经营许可证字${val}"
                    , businessLicenseNo: "经营许可证号${val}"
                    , transformLicenseCharacter: "道路运输证字${val}"
                    , transformLicenseNo: "道路运输证号${val}"
                    , transformLicenseMedium: "道路运输证介质${val}"
                    , transformLicensePhysicalno: "道路运输证物理编号${val}"
                    , grantOrg: "运管中心"
                    , beginTime: date - 100
                    , endTime: date + 300
                    , dutyTonnage: 20
                    , dutySeat: 15
                    , businessType: carType[val % 3]
                    , businessScope: carType[val % 3]
                    , carOperateSituation: "车辆营运状态01"
                    , secondMaintainSituation: "二级维护状态02"
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
        //车辆类型
//        new VehicleType(name: '大型客车',code: 'A1',parent: null).save(flush: true)
//        new VehicleType(name: '牵引车',code: 'A2',parent: null).save(flush: true)
//        new VehicleType(name: '城市公交车',code: 'A3',parent: null).save(flush: true)
//        new VehicleType(name: '中型客车',code: 'B1',parent: null).save(flush: true)
//        new VehicleType(name: '大型货车',code: 'B2',parent: null).save(flush: true)
//        new VehicleType(name: '小型汽车',code: 'C1',parent: null).save(flush: true)
//        new VehicleType(name: '小型自动挡汽车',code: 'C2',parent: null).save(flush: true)
//        new VehicleType(name: '低速载货汽车',code: 'C3',parent: null).save(flush: true)
//        new VehicleType(name: '三轮汽车',code: 'C4',parent: null).save(flush: true)
//        new VehicleType(name: '普通三轮摩托车',code: 'D',parent: null).save(flush: true)
//        new VehicleType(name: '普通二轮摩托车',code: 'E',parent: null).save(flush: true)
//        new VehicleType(name: '轻便摩托车',code: 'F',parent: null).save(flush: true)
//        new VehicleType(name: '轮式自行机械车',code: 'M',parent: null).save(flush: true)
//        new VehicleType(name: '无轨电车',code: 'N',parent: null).save(flush: true)
//        new VehicleType(name: '有轨电车',code: 'P',parent: null).save(flush: true)

        def platForm = new Menu(name: '查岗', code: 'root-pluponForm', icon: 'fa-cog', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '查岗信息', code: 'ownerCheckRecord', icon: 'fa-hand-o-right', parent: platForm, position: 'SIDE_BAR').save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '4598', question: '2+3=?', answer: '5', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 30*1000), responseContent: '5', responseTime: 30).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '9578', question: '5+8=?', answer: '13', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '2464', question: '10-1=?', answer: '9', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 200*1000),
                responseContent: '9', responseTime: 200).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '1934', question: '2x3=?', answer: '6', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 20*1000),
                responseContent: '6', responseTime: 20).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '6427', question: '10÷5=?', answer: '2', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 76*1000),
                responseContent: '2', responseTime: 76).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '7294', question: '1x10=?', answer: '10', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '6729', question: '2x2=?', answer: '4', responsed: true,
                responseDate: new Date(new Date().getTime() + 100*1000),
                responseContent: '4', responseTime: 100).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '1759', question: '1+8=?', answer: '9', responsed: true,
                responseDate: new Date(new Date().getTime() + 121*1000),
                responseContent: '9', responseTime: 121).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '7394', question: '1x10=?', answer: '10', responsed: false,
                operator: testUser,).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '6785', question: '8-1=?', answer: '7', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 190*1000),
                responseContent: '7', responseTime: 190).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '3427', question: '12÷3=?', answer: '4', responsed: true,
                operator: testUser, responseDate: new Date(new Date().getTime() + 75*1000),
                responseContent: '4', responseTime: 75).save(flush: true)

        new Menu(name: '平台配置管理', code: 'platformManage', icon: 'fa-columns', parent: platForm, position: 'SIDE_BAR').save(flush: true)
        new PlatformManage(ip: '192.168.1.24', port: '4233', name: '云联城市交通', code: 'K001', contactName: '李娜', contactPhone: '13052736784', draftPeople: '张敏', status: '起草').save(flush: true)
        new PlatformManage(ip: '61.123.1.15', port: '2001', name: '合肥客运平台', code: 'K002', contactName: '王平', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)


    }
}
