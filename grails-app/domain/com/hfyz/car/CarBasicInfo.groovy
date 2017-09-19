package com.hfyz.car

class CarBasicInfo {        //营运车辆_基本信息_车辆信息
    Date modifyTime    //更新时间
    String licenseNo   //车辆（挂车）号牌
    String carPlateColor  //车牌颜色
    String brand       //厂牌
    String model       //型号
    String carType     //车辆类型
    String passengerLevel //客车等级
    String carColor    //车身颜色
    String engNo       //发动机号
    String frameNo     //车架号
    String carIdentityCode //车辆识别VIP码
    BigDecimal seatNo     //核定载客位
    BigDecimal carTonnage  //车辆（挂车）吨位
    Integer carBoxNo   //车辆箱位
    BigDecimal volume  //罐体容积
    String fuelType    //燃料类型
    BigDecimal engPower //发动机功率
    Date leaveFactoryTime //出厂日期
    Date buyCarTime  //购车日期
    Date settleTime  //落户日期
    String picture   //车辆照片
    Integer wheelbase //轴距
    Integer carLength //车长
    Integer carHeight //车高
    Integer carWidth  //车宽
    Integer carSmokeNo //车轴数
    Integer leafSpringNo //后轴钢板弹簧片数
    BigDecimal tractionTonnage //准牵引总质量
    BigDecimal totalTonnage //总质量
    BigDecimal curbWeight //整备质量
    String drivingWay//驱动方式
    String transformLicenseNo //道路运输证号

    static constraints = {
    }
    static mapping = {
        table 'RUNCAR_BASIC_CARINFO'
        version false
    }
}