/**
 * Created by fuhh on 2017-06-15.
 */

//各种函数
$(function(){
    setInterval(initWarningData,5000);
});

function initWarningData(){
    $.post(starnetContextPath + "/path/getDayWarningPathList",function(res){
        if(res.result == 0){
            for(var i in res.data) {
                // "user_type" -> "2"
                if(res.data[i].user_type==2){
                    warningPrompt("特定人员:",res.data[i].true_name+"在"+(new Date(res.data[i].etime)).Format("yyyy-MM-dd hh:mm:ss.S")+
                    "出现在"+res.data[i].org_node_address+"请注意"

                    );
                }
                if(res.data[i].user_type==1){
                    errorPrompt("黑名单人员:",res.data[i].true_name+"在"+(new Date(res.data[i].etime)).Format("yyyy-MM-dd hh:mm:ss.S")+
                    "出现在"+res.data[i].org_node_address+"请注意"
                );
                }

                if(res.data[i].user_type==0){
                    successfulPrompt("保安人员:",res.data[i].true_name+"在"+(new Date(res.data[i].etime)).Format("yyyy-MM-dd hh:mm:ss.S")+
                    "出现在"+res.data[i].org_node_address+"巡检"

                );
                }


            }

            // successfulPrompt("预警信息","");
        }else{
            errorPrompt("获取预警信息失败",res.msg);
        }
    });
}






