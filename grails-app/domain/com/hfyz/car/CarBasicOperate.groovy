package com.hfyz.car

class CarBasicOperate {  //营运车辆_基本信息_营运信息

    Date modifyTime                 //更新时间
    String carfileRecordNo          //车辆档案号
    String ownerName                //业户名称
    String orgCode                  //组织机构代码
    String businessLicenseCharacter //经营许可证字
    String businessLicenseNo        //经营许可证号
    String transformLicenseCharacter//道路运输证字
    String transformLicenseNo       //道路运输证号
    String transformLicenseMedium   //道路运输证介质
    String transformLicensePhysicalno //道路运输证物理编号
    String grantOrg                 //发证机构
    Date beginTime                  //有效期起
    Date endTime                    //有效期止
    BigDecimal dutyTonnage          //计征吨位
    Integer dutySeat                //计征座位
    String businessType             //行业类别
    String businessScope            //经营范围
    String carOperateSituation      //车辆营运状态
    String secondMaintainSituation  //二级维护状态
    Integer checkMaintainTimes      //核定维护次数
    Integer changeLicenseTimes      //补换证次数
    Integer bigMaintainTimes        //大修次数
    String carTechnologyLevel       //车辆技术等级
    char finePaySituation           //规费缴纳状态
    char inspectDealSituation       //稽查处理状态
    Integer trafficAccidentTimes    //交通事故次数
    String insureSituation          //投保状态
    String yearExamineSituation     //年度审验状态
    Integer exitOrEntry             //是否出入境
    String lineMarketNo             //线路标志牌号
    Integer communicationTool       //是否配备有效通讯工具
    String communicationNo          //有效通讯工具号码
    Integer driveRecorder           //是否安装行驶记录仪
    Integer locator                 //是否安装定位系统
    String driveRecorderSituation   //行车记录仪安装状态
    String locatorSituation         //定位系统安装状态
    Date tankExamTime               //罐体检测日期
    Date fuelExamTime               //燃油核查日期
    Date firstGrantTime             //首次发证日期
    String licensePlate             //车牌号码
    String carColor                 //车辆颜色
    String anchored                 //挂靠
    String anchoredName             //挂靠车主姓名
    String anchoredAddress          //挂靠车主地址
    String anchoredPhone            //挂靠车主电话
    String administrativeDivisionCode //行政区划代码
    String lineType                 //班线类型
    String frameNo                  //车架号

    static constraints = {
    }
    static mapping = {
        table 'RUNCAR_BASICOPERATE'
        version false
    }
}