package com.hfyz.waybill

class PassLineBusinessBasicInfo {
    String lineCode                     //线路编号
    String ownerName                    //业户名称*
    String companyCode                  //业户编码(组织机构代码）*
    String licenseCharacter             // 经营许可证字
    String licenseNo                    // 经营许可证号
    String busType                      // 班车类别
    String startStationName             //起点站
    String endStationName               //终点站
    String stopStation                  //途经站点
    String mainPoint                    //途经主要站点
    Integer dayTimes                    //日发班次
    String businessWay                  //经营方式
    String licenseDecideBookNo          //许可决定书编号
    Date decideTime                     //许可日期
    String decideOrc                    //许可机构
    Date beginTime                      //有效起始日期
    Date endTime                        //有效起始日期
    String licenseType                  //牌证类别
    String businessSituation            //营运状态
    Integer changeLicenseTimes          //补换证次数
    Integer generalinfoChangeTimes      //一般信息变更次数
    Integer businessinfoChangeTimes     //营运状态变更次数
    Integer inputTotalCar               //投入的车辆总数
    Integer inputTotalSeat              //投入车辆的座位总数
    Integer totalLinePlate              //线路牌总数

    static constraints = {
    }
}
