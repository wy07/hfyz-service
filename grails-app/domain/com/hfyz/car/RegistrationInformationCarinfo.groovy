package com.hfyz.car

class RegistrationInformationCarinfo {

    String vehicleNo           //车牌号
    String vehicleColor        //车辆颜色
    String plateformId         //平台唯一编码
    String producerId          //车载终端厂商唯一编码
    String terminalModelType   //车载终端型号
    String terminalId          //车载终端编号，大写字母和数字组成
    String terminalSimcode     //车载终端SIM卡电话号码

    static constraints = {
    }
    static mapping = {
        table 'REGISTRATION_INFORNATION_CARINFO'
        version false
    }
}
