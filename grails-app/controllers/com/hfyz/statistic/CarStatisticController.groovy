package com.hfyz.statistic

import com.commons.utils.ControllerHelper

class CarStatisticController implements ControllerHelper{

    def passengerList(){
        if(getCurrentUser().isCompanyUser()) {
            def total = 1
            def passengerStatisticList = [["id": 300, "ownerName": "安徽省合肥汽车客运有限公司", "onlineing": "1526", "online": "1098", "crossCar": "51", "warning": "5"]]
            renderSuccessesWithMap([passengerStatisticList: passengerStatisticList, total: total])
            return
        }
        def total = 5
        def passengerStatisticList = [["id": 300, "ownerName":"安徽省合肥汽车客运有限公司", "onlineing": "1526", "online": "1098", "crossCar": "51", "warning": "5"]
                                     ,["id": 301, "ownerName": "合肥新亚汽车客运公司", "onlineing": "2578", "online": "2045", "crossCar": "68", "warning": "7"]
                                     ,["id": 302, "ownerName": "合肥长丰县宏业汽车客运有限公司", "onlineing": "2814", "online": "1957", "crossCar": "58", "warning": "6"]
                                     ,["id": 303, "ownerName": "合肥客运旅游汽车公司", "onlineing": "2947", "online": "1758", "crossCar": "75", "warning": "13"]
                                     ,["id": 304, "ownerName": "锐致货运公司", "onlineing": "2875", "online": "2058", "crossCar": "65", "warning": "8"]]
        renderSuccessesWithMap([passengerStatisticList: passengerStatisticList, total: total])

    }

    def travelList(){
        if(getCurrentUser().isCompanyUser()) {
            def total = 1
            def travelStatisticList = [["id": 300, "ownerName": "安徽省合肥汽车客运有限公司", "onlineing": "1255", "online": "982", "crossCar": "45", "warning": "3"]]
            renderSuccessesWithMap([travelStatisticList: travelStatisticList, total: total])
            return
        }
        def total = 5
        def travelStatisticList = [["id": 300, "ownerName": "安徽省合肥汽车客运有限公司", "onlineing": "1526", "online": "1098", "crossCar": "51", "warning": "5"]
                                      ,["id": 301, "ownerName": "合肥新亚汽车客运公司", "onlineing": "2578", "online": "2045", "crossCar": "68", "warning": "7"]
                                      ,["id": 302, "ownerName": "合肥长丰县宏业汽车客运有限公司", "onlineing": "2814", "online": "1957", "crossCar": "58", "warning": "6"]
                                      ,["id": 303, "ownerName": "合肥客运旅游汽车公司", "onlineing": "2947", "online": "1758", "crossCar": "75", "warning": "13"]
                                      ,["id": 304, "ownerName": "锐致货运公司", "onlineing": "2875", "online": "2058", "crossCar": "65", "warning": "8"]]
        renderSuccessesWithMap([travelStatisticList: travelStatisticList, total: total])
    }

    def dangerousList(){
        if(getCurrentUser().isCompanyUser()) {
            def total = 1
            def dangerousStatisticList = [["id": 300, "ownerName": "安徽省合肥汽车客运有限公司", "onlineing": "2487", "online": "1345", "crossCar": "25", "dangerousGoods":"易燃物","haulway":"蜀山区-庐阳区", "warning": "9"]]
            renderSuccessesWithMap([dangerousStatisticList: dangerousStatisticList, total: total])
            return
        }
        def total = 5
        def dangerousStatisticList = [["id": 300, "ownerName": "安徽省合肥汽车客运有限公司", "onlineing": "1526", "online": "1098", "crossCar": "51", "dangerousGoods":"易燃物","haulway":"蜀山区-庐阳区", "warning": "5"]
                                      ,["id": 301, "ownerName": "合肥新亚汽车客运公司", "onlineing": "2578", "online": "2045", "crossCar": "68", "dangerousGoods":"化学药品","haulway":"滨湖新区-庐阳区", "warning": "7"]
                                      ,["id": 302, "ownerName": "合肥长丰县宏业汽车客运有限公司", "onlineing": "2814", "online": "1957", "crossCar": "58", "dangerousGoods":"易爆物","haulway":"蜀山区-庐阳区", "warning": "6"]
                                      ,["id": 303, "ownerName": "合肥客运旅游汽车公司", "onlineing": "2947", "online": "1758", "crossCar": "75", "dangerousGoods":"易燃物","haulway":"瑶海区-包河区","warning": "13"]
                                      ,["id": 304, "ownerName": "锐致货运公司", "onlineing": "2875", "online": "2058", "crossCar": "65", "dangerousGoods":"有毒气体","haulway":"新站区-庐阳区","warning": "8"]]
        renderSuccessesWithMap([dangerousStatisticList: dangerousStatisticList, total: total])
    }
}
