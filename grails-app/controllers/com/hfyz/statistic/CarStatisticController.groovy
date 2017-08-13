package com.hfyz.statistic

import com.commons.utils.ControllerHelper

class CarStatisticController implements ControllerHelper{

    def passengerList(){
        println getCurrentUser().companyCode
        println '=============passengerList=================='
        def total = 5
        def passengerStatisticList =  [["id":300,"ownerName":"企业0","onlineing":"1526","online":"1098","crossCar":"51","warning":"5"]
                                       ,["id":301,"ownerName":"企业1","onlineing":"2726","online":"1758","crossCar":"43","warning":"0"]
                                       ,["id":302,"ownerName":"企业2","onlineing":"2952","online":"1958","crossCar":"58","warning":"4"]
                                       ,["id":303,"ownerName":"企业3","onlineing":"1856","online":"1520","crossCar":"64","warning":"7"]
                                       ,["id":304,"ownerName":"企业4","onlineing":"2258","online":"2058","crossCar":"78","warning":"3"]]
        renderSuccessesWithMap([passengerStatisticList: passengerStatisticList, total: total])
    }

    def travelList(){
        println getCurrentUser().companyCode
        println '=============travelList=================='
        def total = 5
        def travelStatisticList =  [["id":300,"ownerName":"企业0","onlineing":"1526","online":"1098","crossCar":"51","warning":"5"]
                                       ,["id":301,"ownerName":"企业1","onlineing":"2726","online":"1758","crossCar":"43","warning":"0"]
                                       ,["id":302,"ownerName":"企业2","onlineing":"2952","online":"1958","crossCar":"58","warning":"4"]
                                       ,["id":303,"ownerName":"企业3","onlineing":"1856","online":"1520","crossCar":"64","warning":"7"]
                                       ,["id":304,"ownerName":"企业4","onlineing":"2258","online":"2058","crossCar":"78","warning":"3"]]
        renderSuccessesWithMap([travelStatisticList: travelStatisticList, total: total])
    }

    def dangerousList(){
        println getCurrentUser().companyCode
        println '=============dangerousList=================='
        def total = 5
        def dangerousStatisticList =  [["id":300,"ownerName":"企业0","onlineing":"1526","online":"1098","crossCar":"51","warning":"5"]
                                    ,["id":301,"ownerName":"企业1","onlineing":"2726","online":"1758","crossCar":"43","warning":"0"]
                                    ,["id":302,"ownerName":"企业2","onlineing":"2952","online":"1958","crossCar":"58","warning":"4"]
                                    ,["id":303,"ownerName":"企业3","onlineing":"1856","online":"1520","crossCar":"64","warning":"7"]
                                    ,["id":304,"ownerName":"企业4","onlineing":"2258","online":"2058","crossCar":"78","warning":"3"]]
        renderSuccessesWithMap([dangerousStatisticList: dangerousStatisticList, total: total])
    }

    def detailList(){
        println getCurrentUser().companyCode
        println '=============detailList=================='
        def total = 5
        def detailList =  [["id":300,"licenseNo":"皖A35898","time":"2017-07-15","speed":"30","acc":"51","mileage":"5","positioning":"开","location":"一街3号"]
                           ,["id":301,"licenseNo":"皖B48597","time":"2017-07-15","speed":"28","acc":"43","mileage":"3","positioning":"开","location":"五街25号"]
                           ,["id":302,"licenseNo":"皖A64859","time":"2017-07-15","speed":"30","acc":"58","mileage":"4","positioning":"开","location":"十一街4号"]
                           ,["id":303,"licenseNo":"皖A39724","time":"2017-07-15","speed":"33","acc":"64","mileage":"7","positioning":"开","location":"七街11号"]
                           ,["id":304,"licenseNo":"皖B17586","time":"2017-07-15","speed":"25","acc":"78","mileage":"3","positioning":"开","location":"五街7号"]]
        renderSuccessesWithMap([detailList: detailList, total: total])
    }
}
