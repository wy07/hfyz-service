package com.hfyz.waybill

/**
 * 运管中心-客运线路_物理线路_基本信息
 */
class PassLinePhysicalBasicInfo {
    String lineCode //线路编码
    String lineName //线路名称
    Date modifyTime //更新时间
    String businessArea //经营区域
    String lineType //线路类型
    String startPlace //始发地
    String endPlace //终点
    String mainPoint //途径主要地点
    String startAdminDivsionCode //起点行政区划代码
    String startAdminDivsionName //起点行政区划名称
    String endAdminDivsionCode //终点行政区划代码
    String endAdminDivsionName //终点行政区划名称
    BigDecimal lineMileAge //线路里程
    BigDecimal highwayMileAge //高速里程
    BigDecimal percentage //占总里程的百分比
    String highwayEntry //高速入口
    String highwayExit //高速出口
    boolean highway //是否高速
    boolean villageLine //是否农村客运
    boolean  travelLine //是否旅游线路
    boolean busLine //是否公交线路

    static constraints = {
        lineCode unique: true,blank: false,maxSize: 23
        lineName blank: false,maxSize: 20
        modifyTime nullable: false
        businessArea blank: false,maxSize: 4,inList: ['县内','县际','市际','省际','国际']
        lineType blank: false,maxSize: 12,inList: ['一类客运班线','二类客运班线','三类客运班线','四类客运班线']
        startPlace blank: false,maxSize: 50
        endPlace blank: false,maxSize: 50
        mainPoint blank: true,maxSize: 50
        startAdminDivsionCode blank: false,maxSize: 6
        startAdminDivsionName blank: false,maxSize: 50
        endAdminDivsionCode blank: false,maxSize: 6
        endAdminDivsionName blank: false,maxSize: 50
        lineMileAge blank: false,maxSize: 20,scale: 1
        highwayMileAge blank: true,maxSize: 20,scale: 1
        percentage blank: true,maxSize: 20,scale: 1
        highwayEntry blank: true,maxSize: 20
        highwayExit blank: true,maxSize: 20
    }









}
