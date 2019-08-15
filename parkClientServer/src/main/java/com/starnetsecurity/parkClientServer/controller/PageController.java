package com.starnetsecurity.parkClientServer.controller;

import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by 宏炜 on 2017-06-15.
 */
@Controller
@RequestMapping("/")
public class PageController extends BaseController {

    @Autowired
    DeviceManageService deviceManageService;

    @RequestMapping("orgMgr/index")
    public String orgMgrIndex(){
        return "admin/orgMgr";
    }

    @RequestMapping("nodeMgr/index")
    public String nodeMgrIndex(){
        return "admin/nodeMgr";
    }

    @RequestMapping("person/index")
    public String personIndex(){
        return "admin/person";
    }

    @RequestMapping("securityJudged/index")
    public String securityJudgedIndex(){
        return "admin/securityJudged";
    }

    @RequestMapping("trajectory/index")
    public String trajectoryIndex(){
        return "admin/trajectory";
    }

    @RequestMapping("personnelDistribution/index")
    public String personnelDistributionIndex(){
        return "admin/personnelDistribution";
    }

    @RequestMapping("visitorsFlowrate/index")
    public String visitorsFlowrateIndex(){
        return "admin/visitorsflowrate";
    }


    @RequestMapping("sectionMap/index")
    public String sectionMapIndex(){
        return "admin/sectionMap";
    }


    @RequestMapping("home/index")
    public String homeIndex(ModelMap modelMap){
        List<DeviceInfo> deviceInfos = deviceManageService.getIpcInfoList();
        modelMap.put("deviceInfos", deviceInfos);
        return "admin/home";
    }
    @RequestMapping("screen/index")
    public String screenIndex(){
        return "admin/screenPlayback";
    }
    @RequestMapping("systemnew/index")
    public String systemIndex(){
        return "sys/systemSet";
    }


    @RequestMapping("warn/index")
    public String warnIndex(){
        return "admin/warn";
    }



    @RequestMapping("securityJudgedSecurity/index")
    public String securityJudgedSecurity(){
        return "admin/securityTrajectory";
    }




    @RequestMapping("totalMap/index")
    public String totalMapIndex(){
        return "admin/totalMap";
    }


    @RequestMapping("realTime/index")
    public String realTimeIndex(){
        return "admin/realTime";
    }

    @RequestMapping("vehicle/index")
    public String vehicleIndex(){
        return "admin/vehicle";
    }
    @RequestMapping("realPayment/index")
    public String realPaymentIndex(){
        return "admin/realPayment";
    }

    @RequestMapping("parkManagement/index")
    public String parkManagementIndex(){
        return  "admin/parkManagement";
    }

    @RequestMapping(" memberBalance/index")
    public String  memberBalanceIndex(){
        return  "admin/memberBalance";

    }

    @RequestMapping(" memberRecharge/index")
    public String memberRechargeIndex(){
        return  "admin/memberRecharge";

    }

    @RequestMapping("reservationRegistion/index")
    public String  reservationRegistionIndex(){
        return  "admin/reservationRegistion";

    }

    @RequestMapping("paymentDetails/index")
    public String  paymentDetailsIndex(){
        return  "admin/paymentDetails";

    }

    @RequestMapping("entryDetails/index")
    public String  entryDetailsIndex(){
        return  "admin/entryDetails";

    }

    @RequestMapping("costsDetails/index")
    public String  costsDetailsIndex(){
        return  "admin/costsDetails";

    }


    @RequestMapping("releaseDetails/index")
    public String  releaseDetailsIndex(){
        return  "admin/releaseDetails";

    }

    @RequestMapping("shiftDetails/index")
    public String  shiftDetailsIndex(){
        return  "admin/shiftDetails";

    }

    @RequestMapping("logDetails/index")
    public String  logDetailsIndex(){
        return  "admin/logDetails";

    }


    @RequestMapping("accountSetting/index")
    public String  accountSettingIndex(){
        return  "admin/accountSetting";

    }

    @RequestMapping("parameterSetting/index")
    public String  parameterSettingIndex(){
        return  "admin/parameterSetting";

    }

    @RequestMapping("chargingRules/index")
    public String chargingRulesIndex(){
        return  "admin/chargingRules";

    }

    @RequestMapping("boothCharge/index")
    public String boothChargeIndex(){
        return  "admin/boothCharge";

    }

    @RequestMapping("chargeType/index")
    public String chargeTypeIndex(){
        return  "admin/chargeType";

    }

    @RequestMapping("attributeCharge/index")
    public String attributeChargeIndex(){
        return  "admin/attributeCharge";

    }

    @RequestMapping("amountSubsection/index")
    public String amountSubsectionIndex(){
        return  "admin/amountSubsection";

    }

    @RequestMapping("parkingSituation/index")
    public String parkingSituationIndex(){
        return  "admin/parkingSituation";

    }

    @RequestMapping("parkingLength/index")
    public String parkingLengthIndex(){
        return  "admin/parkingLength";

    }

    @RequestMapping("vehicleAttributes/index")
    public String vehicleAttributesIndex(){
        return  "admin/vehicleAttributes";

    }

    @RequestMapping("boxCar/index")
    public String boxCarIndex(){
        return  "admin/boxCar";

    }

    @RequestMapping("reservationQuery/index")
    public String reservationQueryIndex(){
        return  "admin/reservationQuery";

    }

    @RequestMapping("ownerFile/index")
    public String ownerFileIndex(){
        return  "admin/ownerFile";
    }

    @RequestMapping("prePayment/index")
    public String prePaymentIndex(){
        return  "admin/prePayment";
    }

    @RequestMapping("realTimeReport/index")
    public String realTimeReportIndex(){
        return  "admin/realTimeReport";
    }


    @RequestMapping("realPaymentReport/index")
    public String realPaymentReportIndex(){
        return  "admin/realPaymentReport";
    }

    @RequestMapping("tranSactionFlow/index")
    public String tranSactionFlowIndex(){
        return  "admin/tranSactionFlow";
    }

    @RequestMapping("blacklist/index")
    public String  blacklistIndex(){
        return  "admin/blacklist";

    }

    @RequestMapping("/post/computer/index")
    public String postComputerIndex(){
        return  "admin/computerIndex";
    }

    @RequestMapping("/preCostsDetails/index")
    public String preCostsDetailsIndex (){
        return "/admin/preCostsDetails";
    }

    @RequestMapping("/discountInfo/index")
    public String discountInfoIndex (){
        return "/admin/discountInfo";
    }

    @RequestMapping("/carInPark/index")
    public String carInParkIndex (){
        return "/admin/carInPark";
    }

    @RequestMapping("/abnormalPass/index")
    public String abnormalPassIndex (){
        return "/admin/abnormalPass";
    }
}
