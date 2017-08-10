package com.hfyz.support

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
import com.hfyz.warning.Warning
import grails.transaction.Transactional
import com.hfyz.security.User
import com.hfyz.security.Role
import com.hfyz.security.UserRole
import com.hfyz.security.PermissionGroup
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.rectification.HiddenRectificationOrderStatus

@Transactional
class InitService {

    def initData() {
        /*  if(User.count()>0){
              return
          }*/
        

        initMenu()
        initSecurityData()
        def adminUser = User.findByUsername('admin')


        new PlatformManage(ip: '192.168.1.24', port: '4233', name: '云联城市交通', code: 'K001', contactName: '李娜', contactPhone: '13052736784', draftPeople: '张敏', status: '起草').save(flush: true)
        new PlatformManage(ip: '61.123.1.15', port: '2001', name: '合肥客运平台', code: 'K002', contactName: '王平', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)

        new Warning(frameNo: 'LDC92839882929345', carLicenseNo: '皖A-2329J', carColor: '白色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC32552378754654', carLicenseNo: '皖A-2934W', carColor: '黑色', warningSource: 2, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC43657563453234', carLicenseNo: '皖A-2239Q', carColor: '银色', warningSource: 3, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC32541346329643', carLicenseNo: '皖A-K3234', carColor: '白色', warningSource: 9, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 3).save(flush: true)
        new Warning(frameNo: 'LDC45436224543633', carLicenseNo: '皖A-K3234', carColor: '白色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LDC56437683435746', carLicenseNo: '皖A-D2234', carColor: '黑色', warningSource: 3, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 2).save(flush: true)
        new Warning(frameNo: 'FDE24546456468766', carLicenseNo: '皖A-G2432', carColor: '白色', warningSource: 2, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'EFY23423523456678', carLicenseNo: '皖A-2422G', carColor: '白色', warningSource: 1, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 5).save(flush: true)
        new Warning(frameNo: 'TDY23425242366578', carLicenseNo: '皖A-32234G', carColor: '蓝色', warningSource: 3, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'REY24234565563581', carLicenseNo: '皖A-3234G', carColor: '黑色', warningSource: 3, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 3).save(flush: true)
        new Warning(frameNo: 'KIU24234345675682', carLicenseNo: '皖A-4522B', carColor: '红色', warningSource: 2, warningType: '车辆非法移位', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'WEF23544573736868', carLicenseNo: '皖A-2321W', carColor: '红色', warningSource: 9, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 2).save(flush: true)
        new Warning(frameNo: 'WDT25352346457244', carLicenseNo: '皖A-D3324', carColor: '白色', warningSource: 2, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'WQW32534626547688', carLicenseNo: '皖A-R3423', carColor: '红色', warningSource: 1, warningType: '驾驶员身份验证失败', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'LCD23532453467573', carLicenseNo: '皖A-H3432', carColor: '白色', warningSource: 3, warningType: '疲劳驾驶', warningTime: new Date(), warningTimes: 1).save(flush: true)
        new Warning(frameNo: 'DEQ23525446672457', carLicenseNo: '皖A-GF333', carColor: '红色', warningSource: 9, warningType: '车辆非法点火', warningTime: new Date(), warningTimes: 1).save(flush: true)


        new PeopleBasicInfo(name: '张敏', gender: '女', idCardNo: '34212519870314673x', picture: '', nation: '汉', nativePlace: '安徽合肥', phoneNo: '15105512743', address: '', email: 'test1@163.com', postCode: '340101', educationLevel: '大专', technologyTitle: '', healthState: '健康', birthday: new Date()).save(flush: true)
        new PeopleBasicInfo(name: '吴珊', gender: '男', idCardNo: '34132519870314222x', picture: '', nation: '汉', nativePlace: '安徽巢湖', phoneNo: '15703272743', address: '', email: 'test2@163.com', postCode: '340001', educationLevel: '本科', technologyTitle: '', healthState: '健康', birthday: new Date()).save(flush: true)
        new WorkerCheckMember(companyCode: 'changcheng', idCardNo: '34212519870314673x', workLicenseType: '货运', workLicenseNo: 'AH2016', workLicenseGrantTime: new Date(), workLicenseGetTime: new Date(), endTime: new Date(), licenseGrantOrg: '合肥XXX运输有限公司', licenseSituation: '可用', licenseChangeTimes: 2, trainTimes: 2, checkType: '1').save(flush: true)
        new WorkerManager(companyCode: 'changcheng', idCardNo: '34132519870314222x', workLicenseGrantTime: new Date(), workLicenseType: '押运装卸', ownerName: '合肥市汽车客运XXX', driveCarType: '中小型客运汽车', trafficAccidentRecordNo: 0, endTime: new Date(), licenseGrantOrg: '合肥市交通xxx').save(flush: true)
        new WorkerTechnology(companyCode: 'changcheng', idCardNo: '34212519870314673x', ownerName: '合肥市汽车客运XXX').save(flush: true)
        new WorkerWaiter(idCardNo: '34132519870314222x', jobName: '后勤管理', jobLicenseNo: 'xxxx', grantTime: new Date(), beginWorkTime: new Date()).save(flush: true)
        new WorkerCoach(companyCode: 'changcheng', idCardNo: '34212519870314673x', workLicenseType: '教练', workLicenseNo: 'CAH1234', workLicenseGetTime: new Date(), workLicenseGrantTime: new Date(), endTime: new Date(), driveLicenseNo: '34212519870314673x', driveCarType: '小汽车', driveLicenseGetTime: new Date(), licenseGrantOrga: '合肥市新亚驾校', licenseSituation: '可用', workSituation: '中级教练', ownerName: '合肥市新亚驾校', businessPermitCharacter: '3', businessPermitNo: '34xxx', changeTimes: 0, trainTimes: 1, inspectDealSituation: '', teachType: 'C照', driveLicenseSituation: '可用').save(flush: true)
        new WorkerDriver(companyCode: 'changcheng', idCardNo: '34132519870314222x', workLicenseGrantTime: new Date(), workLicenseType: '驾驶员', ownerName: '合肥市汽车客运XXX', driveCarType: '客运汽车', trafficAccidentRecordNo: 0, endTime: new Date(), driveLicenseNo: '34132519870314222x', businessPermitCharacter: 'x').save(flush: true)




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

        new OwnerCheckRecord(auto: false, companyCode: '4598', question: '2+3=?', answer: '5', responsed: true,

                operator: adminUser, responseDate: new Date(new Date().getTime() + 30 * 1000), responseContent: '5', responseTime: 30).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '9578', question: '5+8=?', answer: '13', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '2464', question: '10-1=?', answer: '9', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 200 * 1000),
                responseContent: '9', responseTime: 200).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '1934', question: '2x3=?', answer: '6', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 20 * 1000),
                responseContent: '6', responseTime: 20).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '6427', question: '10÷5=?', answer: '2', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 76 * 1000),
                responseContent: '2', responseTime: 76).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '7294', question: '1x10=?', answer: '10', responsed: false).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '6729', question: '2x2=?', answer: '4', responsed: true,
                responseDate: new Date(new Date().getTime() + 100 * 1000),
                responseContent: '4', responseTime: 100).save(flush: true)
        new OwnerCheckRecord(auto: true, companyCode: '1759', question: '1+8=?', answer: '9', responsed: true,
                responseDate: new Date(new Date().getTime() + 121 * 1000),
                responseContent: '9', responseTime: 121).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '7394', question: '1x10=?', answer: '10', responsed: false,
                operator: adminUser,).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: '6785', question: '8-1=?', answer: '7', responsed: true,

                operator: adminUser, responseDate: new Date(new Date().getTime() + 190 * 1000),

                responseContent: '7', responseTime: 190).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '12÷3=?', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '12÷3=?', answer: '5', responsed: false,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '测试数据1', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '测试数据2', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000001', question: '测试数据3', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '测试数据3', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '测试数据3', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '测试数据3', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '测试数据3', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date(new Date().getTime() + 75 * 1000),
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000002', question: '测试数据3', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date()+1,
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000000', question: '测试数据3', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date()+9,
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000004', question: '测试数据3', answer: '4', responsed: true,
                operator: adminUser, responseDate: new Date()+4,
                responseContent: '4', responseTime: 75).save(flush: true)
        new OwnerCheckRecord(auto: false, companyCode: 'C000000004', question: '测试数据3', answer: '4', responsed: false,
                operator: adminUser, responseDate: new Date()+5,
                responseContent: '4', responseTime: 75).save(flush: true)

        new PlatformManage(ip: '192.168.1.24', port: '4233', name: '云联城市交通', code: 'K001', contactName: '李娜', contactPhone: '13052736784', draftPeople: '张敏', status: '起草').save(flush: true)
        new PlatformManage(ip: '61.123.1.15', port: '2001', name: '合肥客运平台', code: 'K002', contactName: '王平', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '62.23.1.15', port: '2011', name: '合肥汽车客运有限公司', code: 'K003', contactName: '张敏', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '63.42.1.16', port: '2021', name: '合肥新亚汽车客运公司', code: 'K004', contactName: '吴霞', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '64.22.1.16', port: '2031', name: '长丰县宏业汽车客运有限公司', code: 'K005', contactName: '王珊珊', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '65.132.1.17', port: '2041', name: '合肥汽车客运总公司第七客运公司', code: 'K006', contactName: '李莎', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '66.52.1.18', port: '2051', name: '合肥汽车客运总公司第八客运公司', code: 'K007', contactName: '张磊', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '67.53.1.18', port: '2061', name: '合肥客运旅游汽车公司', code: 'K008', contactName: '张娜', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '68.24.1.2', port: '2071', name: '肥西汽车站严店客运站', code: 'K009', contactName: '张晓霞', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '69.12.1.34', port: '2081', name: '合肥客运总公司四合汽车站', code: 'K022', contactName: '吴丽萍', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '611.14.1.24', port: '2091', name: '安徽百众旅游客运有限公司', code: 'K202', contactName: '李建国', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '612.12.1.1', port: '2101', name: '安徽飞雁快速客运公司', code: 'K302', contactName: '张珊米', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '613.16.1.41', port: '2201', name: '宁国光正合肥分公司', code: 'K402', contactName: '安娜', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)
        new PlatformManage(ip: '614.17.1.42', port: '2301', name: '平安四方有限公司', code: 'K502', contactName: '郑磊国', contactPhone: '13023429743', draftPeople: '吴珊', status: '起草').save(flush: true)

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

        new RegisterReport(caseRegisterNo: "tx123", companyCode: "changcheng", idCardNo: "421553").save(flush: true)
        new RegisterReport(caseRegisterNo: "test", companyCode: "changcheng", idCardNo: "2345").save(flush: true)
        new PeopleBasicInfo(idCardNo: "421553").save(flush: true)

        25.times { val ->
            new RegistrationInformationCarinfo(
                    vehicleNo: "陕A-CK" + "${val}".padLeft(4, '0')
                    , vehicleColor: '白色'
                    , plateformId: "plateform${val}"
                    , producerId: "producer${val}"
                    , terminalModelType: "ACRII型号"
                    , terminalId: "terminal${val}"
                    , terminalSimcode: "136458736" + "${val}".padLeft(2, '0')
                    , frameNo: "frameNo${val}").save(flush: true)
        }

//        公司（经营业户）
        4.times { it ->
            new OwnerIdentity(ownerName: "企业${it}"
                    , companyCode: "C00000000${it}"
                    , ownerCode: "dwcode00${it}"
                    , parentCompanyName: '企业1'
                    , parentOwner: '企业1'
                    , ownerAddress: "合肥市怀宁路1${it}6号"
                    , postCode: 23002 + "${it}"
                    , administrativeDivisionName: '蜀山区'
                    , administrativeDivisionCode: 34010 + "${it}"
                    , economicType: '私营经济'
                    , legalRepresentative: '张敏'
                    , idCardType: '居民身份证'
                    , idCardNo: "${it}4212519870314673x"
                    , picture: ''
                    , operateManager: '吴珊'
                    , phone: "010-${it}2425722"
                    , fax: ''
                    , telephone: "1${it}387673452"
                    , email: "wushan${it}@163.com"
                    , website: "http://www.${it}xiaojukeji.com"
                    , shortName: '企业0').save(flush: true)
            new OwnerManageInfo(
                    companyCode: "C00000000${it}",
                    licenseCharacter: "${it}",
                    licenseNo: "${it}",
                    beginTime: new Date(),
                    endTime: new Date(117, 8 + it, 10 + it),
                    grantOrganization: "合肥市道路运输管理处",
                    checkTime: new Date(),
                    domicileOperateName: "${it}",
                    domicileOperateNo: "${it}",
                    paymentSituation: "${it}",
                    licenseChangeTimes: "${it}",
                    inspectTreatmentSituation: "${it}"
            ).save(flush: true)
        }
        new Configure(configKey: 'carRateAlarm', configValue: '100', name: '车辆入网率告警阈值', note: '触发告警为小于等于该阈值').save(flush: true)

//        公司内部制度
        new CompanyRegulation(companyCode: 'C000000001').save(flush: true)
        new CompanyRegulation(companyCode: 'C000000002').save(flush: true)

        new BlackList(vehicleNo: '陕A-CK0002', frameNo: 'frameNo2', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 7, 13), controlEnd: new Date(117, 7, 20)).save(flush: true)
        new BlackList(vehicleNo: '陕A-CK0003', frameNo: 'frameNo3', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 12), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: '陕A-CK0004', frameNo: 'frameNo4', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 7, 22), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: '陕A-CK0005', frameNo: 'frameNo5', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 1), controlEnd: new Date(117, 8, 10)).save(flush: true)
        new BlackList(vehicleNo: '陕A-CK0006', frameNo: 'frameNo6', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 8, 20), controlEnd: new Date(117, 8, 22)).save(flush: true)
        new BlackList(vehicleNo: '陕A-CK0007', frameNo: 'frameNo7', blackType: '重点车辆盘查', controlBehavior: '卡口抓拍', scheme: '实时报警', controlRange: '合肥市主干道', controlOrg: '合肥市公安局', executor: '管理员', status: Status.BKZ, controlBegin: new Date(117, 9, 12), controlEnd: new Date(117, 9, 17)).save(flush: true)

        new WhiteList(vehicleNo: '陕A-CK0008', frameNo: 'frameNo8', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: '陕A-CK0009', frameNo: 'frameNo9', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: '陕A-CK0010', frameNo: 'frameNo10', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: '陕A-CK0011', frameNo: 'frameNo11', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: '陕A-CK0012', frameNo: 'frameNo12', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)
        new WhiteList(vehicleNo: '陕A-CK0013', frameNo: 'frameNo13', controlBegin: new Date(117, 9, 12), status: Status.JCBK).save(flush: true)

        def parentMenu = new Menu(name: '整改', code: 'root-rectification', icon: 'fa-hand-o-up', parent: null, position: 'SIDE_BAR').save(flush: true)

        new Menu(name: '隐患整改单', code: 'hiddenDanger', icon: 'fa-hand-o-right', parent: parentMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '整改单审核', code: 'orderExamine', icon: 'fa-hand-o-right', parent: parentMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '企业反馈', code: 'enterpriseFeedback', icon: 'fa-hand-o-right', parent: parentMenu, position: 'SIDE_BAR').save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730001',enterprise:'企业0' ,companyCode:'C000000000', examiner:'李四', inspectionDate:new Date(), dealineDate:new Date()+5, insPosition:'车间', insDesc:'机器', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730002',enterprise:'企业1', companyCode:'C000000001', examiner:'王五', inspectionDate:new Date(), dealineDate:new Date()+6, insPosition:'工厂', insDesc:'线路', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730003',enterprise:'企业2', companyCode:'C000000002', examiner:'赵六', inspectionDate:new Date(), dealineDate:new Date()+7, insPosition:'工厂', insDesc:'压缩机', insQuestion:'无验证', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730004',enterprise:'企业3', companyCode:'C000000003', examiner:'tony', inspectionDate:new Date(), dealineDate:new Date()+8, insPosition:'车间', insDesc:'发动机', insQuestion:'无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730005',enterprise:'企业1', companyCode:'C000000001', examiner:'kobe', inspectionDate:new Date(), dealineDate:new Date()+9, insPosition:'工厂', insDesc:'jiqi', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730006',enterprise:'企业0', companyCode:'C000000000', examiner:'KG', inspectionDate:new Date(), dealineDate:new Date()+5, insPosition:'车间', insDesc:'机器', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730007',enterprise:'企业1', companyCode:'C000000001', examiner:'KD', inspectionDate:new Date(), dealineDate:new Date()+6, insPosition:'工厂', insDesc:'线路', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730008',enterprise:'企业2', companyCode:'C000000002', examiner:'lucy', inspectionDate:new Date(), dealineDate:new Date()+7, insPosition:'工厂', insDesc:'压缩机', insQuestion:'无验证', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730009',enterprise:'企业3',companyCode:'C000000003', examiner:'bob', inspectionDate:new Date(), dealineDate:new Date()+8, insPosition:'车间', insDesc:'发动机', insQuestion:'无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730010',enterprise:'企业1', companyCode:'C000000001', examiner:'fisher', inspectionDate:new Date(), dealineDate:new Date()+9, insPosition:'工厂', insDesc:'jiqi', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730011',enterprise:'企业2', companyCode:'C000000002', examiner:'lucy', inspectionDate:new Date(), dealineDate:new Date()+7, insPosition:'工厂', insDesc:'压缩机', insQuestion:'无验证', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730012',enterprise:'企业3', companyCode:'C000000003', examiner:'bob', inspectionDate:new Date(), dealineDate:new Date()+8, insPosition:'车间', insDesc:'发动机', insQuestion:'无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730013',enterprise:'企业1', companyCode:'C000000001', examiner:'fisher', inspectionDate:new Date(), dealineDate:new Date()+9, insPosition:'工厂', insDesc:'jiqi', insQuestion:'安全问题  管理隐患 无验证  无人看管', proPosal:'1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status:HiddenRectificationOrderStatus.QC).save(flush: true)




        new Menu(name: '隐患整改', code: 'hiddenDanger', icon: 'fa-hand-o-right', parent: parentMenu, position: 'SIDE_BAR').save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730001', enterpirse: 'a公司', examiner: '李四', inspectionDate: new Date(), dealineDate: new Date() + 5, insPosition: '车间', insDesc: '机器', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730002', enterpirse: 'b公司', examiner: '王五', inspectionDate: new Date(), dealineDate: new Date() + 6, insPosition: '工厂', insDesc: '线路', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730003', enterpirse: 'c公司', examiner: '赵六', inspectionDate: new Date(), dealineDate: new Date() + 7, insPosition: '工厂', insDesc: '压缩机', insQuestion: '无验证', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730004', enterpirse: 'd公司', examiner: 'tony', inspectionDate: new Date(), dealineDate: new Date() + 8, insPosition: '车间', insDesc: '发动机', insQuestion: '无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)
        new HiddenRectificationOrder(billNo: '20170730005', enterpirse: 'f公司', examiner: 'kobe', inspectionDate: new Date(), dealineDate: new Date() + 9, insPosition: '工厂', insDesc: 'jiqi', insQuestion: '安全问题  管理隐患 无验证  无人看管', proPosal: '1电子台账未更新，2电子路单未及时上传，3动态监控，4监控室，培训室，监控设备未安装，5人员培训不到位，', status: HiddenRectificationOrderStatus.QC).save(flush: true)


        new Configure(configKey: 'carRateAlarm', configValue: '100', name: '车辆入网率告警阈值').save(flush: true)

        def workOrderMenu = new Menu(name: '工单管理', code: 'root-workOrderManger', icon: 'fa-file-text-o', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '工单列表', code: 'workOrder', icon: 'fa-sticky-note-o', parent: workOrderMenu, position: 'SIDE_BAR').save(flush: true)

        new Configure(configKey: 'carRateAlarm', configValue: '100', name: '车辆入网率告警阈值').save(flush: true)

        initSystemCode()
        initAlarmType()

        def statisticMenu = new Menu(name: '统计', code: 'root-statistic', icon: 'fa-pie-chart', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '查岗统计', code: 'checkStatistic', icon: 'fa-odnoklassniki', parent: statisticMenu, position: 'SIDE_BAR').save(flush: true)

    }

    private initSystemCode(){
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

    private initSecurityData() {
        Organization organizationP = new Organization(name: '运管处', code: '100', parent: null).save(flush: true)
        new Organization(name: '运管指挥中心', code: '110', parent: organizationP).save(flush: true)
        new Organization(name: '货运管理所', code: '120', parent: organizationP).save(flush: true)
        new Organization(name: '客运管理所', code: '130', parent: organizationP).save(flush: true)

        organizationP = new Organization(name: '业务', code: '200', parent: null).save(flush: true)
        new Organization(name: '信息科', code: '210', parent: organizationP).save(flush: true)
        new Organization(name: '法制科', code: '220', parent: organizationP).save(flush: true)
        new Organization(name: '安全监督科', code: '230', parent: organizationP).save(flush: true)

        organizationP = new Organization(name: '运营企业', code: '300', parent: null).save(flush: true)
        def test = new Organization(name: '客运', code: '310', parent: organizationP).save(flush: true)
        new Organization(name: '货运', code: '320', parent: organizationP).save(flush: true)

        (1..100).each {
            new Role(authority: "ROLE_AAA${it}", name: "角色${it}", org: null).save(failOnError: true, flush: true)
        }

        def companyRole = new Role(authority: 'ROLE_COMPANY_ROOT', name: '企业管理员', org: test).save(failOnError: true, flush: true)

        (1..100).each {
            def aaa = new User(username: "company${it}", passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: "企业用户${it}", org: test).save(failOnError: true, flush: true)
            UserRole.create aaa, companyRole, true
        }

        def companyUser = new User(username: 'company', passwordHash: '666666', salt: ValidationUtils.getSecureRandomSalt(), name: '企业用户', org: test).save(failOnError: true, flush: true)
        UserRole.create companyUser, companyRole, true

        def adminRole = new Role(authority: 'ROLE_ROOT', name: '超级管理员', org: null).save(failOnError: true, flush: true)
        def adminUser = new User(username: 'admin', passwordHash: 'admin123',companyCode: 'C000000001',salt: ValidationUtils.getSecureRandomSalt(), name: '管理员').save(failOnError: true, flush: true)
        UserRole.create adminUser, adminRole, true

        new PermissionGroup(url: '/organizations/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "组织机构管理", category: "系统管理", code: 'organization_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/roles/**/**', configAttribute: 'ROLE_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "角色管理", category: "系统管理", code: 'role_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/roles/**', configAttribute: 'ROLE_ROOT,ROLE_COMPANY_ROOT', httpMethod: null, name: "角色管理2", category: "系统管理", code: 'role_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/permission-groups/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "权限管理", category: "系统管理", code: 'permission_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/operation_logs/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "操作日志", category: "系统管理", code: 'log_manage').save(failOnError: true, flush: true)
        new PermissionGroup(url: '/menus/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "菜单管理", category: "系统管理", code: 'menu_manage').save(failOnError: true, flush: true)

        new PermissionGroup(url: '/map-signs/**/**', configAttribute: 'ROLE_ROOT', httpMethod: null, name: "路标管理", category: "基础信息", code: 'map_sign_manage').save(failOnError: true, flush: true)
    }

    private initMenu() {
        new Menu(name: '关闭全部', code: 'closeall', position: 'TOP_BAR', parent: null).save(flush: true)
        def topbar = new Menu(name: '个人中心', code: 'profile', style: 'hoverdown', position: 'TOP_BAR', parent: null).save(flush: true)
        new Menu(name: '修改密码', code: 'changepwd', position: 'TOP_BAR', parent: topbar).save(flush: true)
        new Menu(name: '退出', code: 'logout', position: 'TOP_BAR', parent: null).save(flush: true)

        new Menu(name: '首页', code: 'home', icon: 'fa-home', parent: null, position: 'SIDE_BAR').save(flush: true)

        def systemManage = new Menu(name: '系统管理', code: 'root-syscode', icon: 'fa-cogs', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '组织机构', code: 'organization', icon: 'fa-sitemap', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'organization_manage').save(flush: true)
        new Menu(name: '角色', code: 'role', icon: 'fa-users', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'role_manage').save(flush: true)
        new Menu(name: '权限', code: 'permission', icon: 'fa-users', parent: systemManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '用户', code: 'user', icon: 'fa-user', parent: systemManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '操作日志', code: 'operationLog', icon: 'fa-table', parent: systemManage, position: 'SIDE_BAR', permissionCode: 'log_manage').save(flush: true)
        new Menu(name: '菜单', code: 'menu', icon: 'fa-list', parent: systemManage, position: 'SIDE_BAR').save(flush: true)

        def basicInfo = new Menu(name: '基础信息', code: 'root-basicinfo', icon: 'fa-wrench', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '路标管理', code: 'mapSign', icon: 'fa-map-marker', parent: basicInfo, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '数据字典', code: 'systemcode', icon: 'fa-book', parent: basicInfo, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '系统配置', code: 'configure', icon: 'fa-cogs', parent: basicInfo, position: 'SIDE_BAR').save(flush: true)

        def monitorMenu = new Menu(name: '实时监控', code: 'root-monitor', icon: 'fa-eercast', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '车辆实时', code: 'realTimeMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '车辆控制', code: 'realTimeMonitorMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '车辆历史', code: 'historyMap', icon: 'fa-map-o', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '报警信息', code: 'warning', icon: 'fa-car', parent: monitorMenu, position: 'SIDE_BAR').save(flush: true)

        def msgManage = new Menu(name: '信息管理', code: 'root-msgmanage', icon: 'fa-indent', parent: null, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '平台管理', code: 'platformManage', icon: 'fa-columns', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '查岗信息', code: 'ownerCheckRecord', icon: 'fa-hand-o-right', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '业户信息', code: 'ownerIdentity', icon: 'fa-building', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '车辆信息', code: 'carList', icon: 'fa-car', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '人员信息', code: 'peopleList', icon: 'fa-group', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '黑名单', code: 'blackList', icon: 'fa-file-text', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '白名单', code: 'whiteList', icon: 'fa-file-text-o', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '信息发布', code: 'infoPublish', icon: 'fa-bullhorn', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '信息审核', code: 'infoCheck', icon: 'fa-check-square', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        new Menu(name: '发布信息查询', code: 'infoList', icon: 'fa-envelope-square', parent: msgManage, position: 'SIDE_BAR').save(flush: true)
        }


}
