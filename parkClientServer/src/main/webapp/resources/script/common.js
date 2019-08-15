/**
 * Created by 宏炜 on 2017-06-15.
 */
$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function (element) {
        element.closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function (error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
        } else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"
});
// 手机号码验证
$.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;
    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})|(19[0-9]{9})$/;
    return this.optional(element) || (length == 11 && mobile.test(value));
}, "请输入正确的手机号码");

$.validator.addMethod("isUsername", function (value, element) {
    var un = /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/i;
    return this.optional(element) || (un.test(value));
}, "字母开头，允许5-16字符，允许字母数字下划线");
$.validator.addMethod("stringCheck", function(value, element) {
    return this.optional(element) || /^[a-zA-Z][a-zA-Z0-9_]*$/.test(value);
}, "字母开头，只能包括英文字母、数字和下划线");

$.validator.addMethod("isIPV4", function(value, element) {
    if(value == ""){
        return true;
    }
    var regex = /((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))/;
    return this.optional(element) || regex.test(value);
}, "请输入正确的IPV4地址");

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
function ajaxReuqest(url,requestType,params,success){
    $.ajax({
        url: url,
        type: requestType,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(params),
        beforeSend: function (request) {
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        },
        success: success,
        error:function(){
            errorPrompt("无法访问网络","")
        }
    });
}

function successfulPrompt(title,desc) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "progressBar": false,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.success(desc, title);
}
function errorPrompt(title,desc) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "progressBar": false,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.error(desc, title);
}
function warningPrompt(title,desc) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "progressBar": false,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.warning(desc, title);
}
function infoPrompt(title,desc) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-bottom-right",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "7000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.info(desc, title);
}
var dataTableLanguage = {
    "sProcessing": "处理中...",
        "sLengthMenu": "显示 _MENU_ 项结果",
        "sZeroRecords": "没有匹配结果",
        "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
        "sInfoPostFix": "",
        "sSearch": "搜索:",
        "sUrl": "",
        "sEmptyTable": "表中数据为空",
        "sLoadingRecords": "载入中...",
        "sInfoThousands": ",",
        "oPaginate": {
        "sFirst": "首页",
            "sPrevious": "上页",
            "sNext": "下页",
            "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
            "sSortDescending": ": 以降序排列此列"
    }
}

$.fn.populateForm = function(data){
    return this.each(function(){
        var formElem, name;
        if(data == null){this.reset(); return; }
        for(var i = 0; i < this.length; i++){
            formElem = this.elements[i];
            //checkbox的name可能是name[]数组形式
            name = (formElem.type == "checkbox")? formElem.name.replace(/(.+)\[\]$/, "$1") : formElem.name;
            if(data[name] == undefined) continue;
            switch(formElem.type){
                case "checkbox":
                    if(data[name] == ""){
                        formElem.checked = false;
                    }else{
                        //数组查找元素
                        if(data[name].indexOf(formElem.value) > -1){
                            formElem.checked = true;
                        }else{
                            formElem.checked = false;
                        }
                    }
                    break;
                case "radio":
                    if(data[name] == ""){
                        formElem.checked = false;
                    }else if(formElem.value == data[name]){
                        formElem.checked = true;
                    }
                    break;
                case "button": break;
                case "select":
                    if(data[name] == ""){
                        formElem.options[1] = true;
                    }else if(data[name] == "yes"){
                        formElem.options[0] = true;
                    }else{
                        formElem.options[1] = true;
                    }
                    break;
                default: formElem.value = data[name];
            }
        }
    });
};

function strIsEmpty(val){
    if (val == null || val == undefined || val == '') {
        return true;
    }else{
        return false;
    }
}

var cityMap = {
    "北京市": "110100",
    "天津市": "120100",
    "上海市": "310100",
    "重庆市": "500100",

    "崇明县": "310200",
    "湖北省直辖县市": "429000",
    "铜仁市": "522200",
    "毕节市": "522400",

    "石家庄市": "130100",
    "唐山市": "130200",
    "秦皇岛市": "130300",
    "邯郸市": "130400",
    "邢台市": "130500",
    "保定市": "130600",
    "张家口市": "130700",
    "承德市": "130800",
    "沧州市": "130900",
    "廊坊市": "131000",
    "衡水市": "131100",
    "太原市": "140100",
    "大同市": "140200",
    "阳泉市": "140300",
    "长治市": "140400",
    "晋城市": "140500",
    "朔州市": "140600",
    "晋中市": "140700",
    "运城市": "140800",
    "忻州市": "140900",
    "临汾市": "141000",
    "吕梁市": "141100",
    "呼和浩特市": "150100",
    "包头市": "150200",
    "乌海市": "150300",
    "赤峰市": "150400",
    "通辽市": "150500",
    "鄂尔多斯市": "150600",
    "呼伦贝尔市": "150700",
    "巴彦淖尔市": "150800",
    "乌兰察布市": "150900",
    "兴安盟": "152200",
    "锡林郭勒盟": "152500",
    "阿拉善盟": "152900",
    "沈阳市": "210100",
    "大连市": "210200",
    "鞍山市": "210300",
    "抚顺市": "210400",
    "本溪市": "210500",
    "丹东市": "210600",
    "锦州市": "210700",
    "营口市": "210800",
    "阜新市": "210900",
    "辽阳市": "211000",
    "盘锦市": "211100",
    "铁岭市": "211200",
    "朝阳市": "211300",
    "葫芦岛市": "211400",
    "长春市": "220100",
    "吉林市": "220200",
    "四平市": "220300",
    "辽源市": "220400",
    "通化市": "220500",
    "白山市": "220600",
    "松原市": "220700",
    "白城市": "220800",
    "延边朝鲜族自治州": "222400",
    "哈尔滨市": "230100",
    "齐齐哈尔市": "230200",
    "鸡西市": "230300",
    "鹤岗市": "230400",
    "双鸭山市": "230500",
    "大庆市": "230600",
    "伊春市": "230700",
    "佳木斯市": "230800",
    "七台河市": "230900",
    "牡丹江市": "231000",
    "黑河市": "231100",
    "绥化市": "231200",
    "大兴安岭地区": "232700",
    "南京市": "320100",
    "无锡市": "320200",
    "徐州市": "320300",
    "常州市": "320400",
    "苏州市": "320500",
    "南通市": "320600",
    "连云港市": "320700",
    "淮安市": "320800",
    "盐城市": "320900",
    "扬州市": "321000",
    "镇江市": "321100",
    "泰州市": "321200",
    "宿迁市": "321300",
    "杭州市": "330100",
    "宁波市": "330200",
    "温州市": "330300",
    "嘉兴市": "330400",
    "湖州市": "330500",
    "绍兴市": "330600",
    "金华市": "330700",
    "衢州市": "330800",
    "舟山市": "330900",
    "台州市": "331000",
    "丽水市": "331100",
    "合肥市": "340100",
    "芜湖市": "340200",
    "蚌埠市": "340300",
    "淮南市": "340400",
    "马鞍山市": "340500",
    "淮北市": "340600",
    "铜陵市": "340700",
    "安庆市": "340800",
    "黄山市": "341000",
    "滁州市": "341100",
    "阜阳市": "341200",
    "宿州市": "341300",
    "六安市": "341500",
    "亳州市": "341600",
    "池州市": "341700",
    "宣城市": "341800",
    "福州市": "350100",
    "厦门市": "350200",
    "莆田市": "350300",
    "三明市": "350400",
    "泉州市": "350500",
    "漳州市": "350600",
    "南平市": "350700",
    "龙岩市": "350800",
    "宁德市": "350900",
    "南昌市": "360100",
    "景德镇市": "360200",
    "萍乡市": "360300",
    "九江市": "360400",
    "新余市": "360500",
    "鹰潭市": "360600",
    "赣州市": "360700",
    "吉安市": "360800",
    "宜春市": "360900",
    "抚州市": "361000",
    "上饶市": "361100",
    "济南市": "370100",
    "青岛市": "370200",
    "淄博市": "370300",
    "枣庄市": "370400",
    "东营市": "370500",
    "烟台市": "370600",
    "潍坊市": "370700",
    "济宁市": "370800",
    "泰安市": "370900",
    "威海市": "371000",
    "日照市": "371100",
    "莱芜市": "371200",
    "临沂市": "371300",
    "德州市": "371400",
    "聊城市": "371500",
    "滨州市": "371600",
    "菏泽市": "371700",
    "郑州市": "410100",
    "开封市": "410200",
    "洛阳市": "410300",
    "平顶山市": "410400",
    "安阳市": "410500",
    "鹤壁市": "410600",
    "新乡市": "410700",
    "焦作市": "410800",
    "濮阳市": "410900",
    "许昌市": "411000",
    "漯河市": "411100",
    "三门峡市": "411200",
    "南阳市": "411300",
    "商丘市": "411400",
    "信阳市": "411500",
    "周口市": "411600",
    "驻马店市": "411700",
    "省直辖县级行政区划": "469000",
    "武汉市": "420100",
    "黄石市": "420200",
    "十堰市": "420300",
    "宜昌市": "420500",
    "襄阳市": "420600",
    "鄂州市": "420700",
    "荆门市": "420800",
    "孝感市": "420900",
    "荆州市": "421000",
    "黄冈市": "421100",
    "咸宁市": "421200",
    "随州市": "421300",
    "恩施土家族苗族自治州": "422800",
    "长沙市": "430100",
    "株洲市": "430200",
    "湘潭市": "430300",
    "衡阳市": "430400",
    "邵阳市": "430500",
    "岳阳市": "430600",
    "常德市": "430700",
    "张家界市": "430800",
    "益阳市": "430900",
    "郴州市": "431000",
    "永州市": "431100",
    "怀化市": "431200",
    "娄底市": "431300",
    "湘西土家族苗族自治州": "433100",
    "广州市": "440100",
    "韶关市": "440200",
    "深圳市": "440300",
    "珠海市": "440400",
    "汕头市": "440500",
    "佛山市": "440600",
    "江门市": "440700",
    "湛江市": "440800",
    "茂名市": "440900",
    "肇庆市": "441200",
    "惠州市": "441300",
    "梅州市": "441400",
    "汕尾市": "441500",
    "河源市": "441600",
    "阳江市": "441700",
    "清远市": "441800",
    "东莞市": "441900",
    "中山市": "442000",
    "潮州市": "445100",
    "揭阳市": "445200",
    "云浮市": "445300",
    "南宁市": "450100",
    "柳州市": "450200",
    "桂林市": "450300",
    "梧州市": "450400",
    "北海市": "450500",
    "防城港市": "450600",
    "钦州市": "450700",
    "贵港市": "450800",
    "玉林市": "450900",
    "百色市": "451000",
    "贺州市": "451100",
    "河池市": "451200",
    "来宾市": "451300",
    "崇左市": "451400",
    "海口市": "460100",
    "三亚市": "460200",
    "三沙市": "460300",
    "成都市": "510100",
    "自贡市": "510300",
    "攀枝花市": "510400",
    "泸州市": "510500",
    "德阳市": "510600",
    "绵阳市": "510700",
    "广元市": "510800",
    "遂宁市": "510900",
    "内江市": "511000",
    "乐山市": "511100",
    "南充市": "511300",
    "眉山市": "511400",
    "宜宾市": "511500",
    "广安市": "511600",
    "达州市": "511700",
    "雅安市": "511800",
    "巴中市": "511900",
    "资阳市": "512000",
    "阿坝藏族羌族自治州": "513200",
    "甘孜藏族自治州": "513300",
    "凉山彝族自治州": "513400",
    "贵阳市": "520100",
    "六盘水市": "520200",
    "遵义市": "520300",
    "安顺市": "520400",
    "黔西南布依族苗族自治州": "522300",
    "黔东南苗族侗族自治州": "522600",
    "黔南布依族苗族自治州": "522700",
    "昆明市": "530100",
    "曲靖市": "530300",
    "玉溪市": "530400",
    "保山市": "530500",
    "昭通市": "530600",
    "丽江市": "530700",
    "普洱市": "530800",
    "临沧市": "530900",
    "楚雄彝族自治州": "532300",
    "红河哈尼族彝族自治州": "532500",
    "文山壮族苗族自治州": "532600",
    "西双版纳傣族自治州": "532800",
    "大理白族自治州": "532900",
    "德宏傣族景颇族自治州": "533100",
    "怒江傈僳族自治州": "533300",
    "迪庆藏族自治州": "533400",
    "拉萨市": "540100",
    "昌都地区": "542100",
    "山南地区": "542200",
    "日喀则地区": "542300",
    "那曲地区": "542400",
    "阿里地区": "542500",
    "林芝地区": "542600",
    "西安市": "610100",
    "铜川市": "610200",
    "宝鸡市": "610300",
    "咸阳市": "610400",
    "渭南市": "610500",
    "延安市": "610600",
    "汉中市": "610700",
    "榆林市": "610800",
    "安康市": "610900",
    "商洛市": "611000",
    "兰州市": "620100",
    "嘉峪关市": "620200",
    "金昌市": "620300",
    "白银市": "620400",
    "天水市": "620500",
    "武威市": "620600",
    "张掖市": "620700",
    "平凉市": "620800",
    "酒泉市": "620900",
    "庆阳市": "621000",
    "定西市": "621100",
    "陇南市": "621200",
    "临夏回族自治州": "622900",
    "甘南藏族自治州": "623000",
    "西宁市": "630100",
    "海东地区": "632100",
    "海北藏族自治州": "632200",
    "黄南藏族自治州": "632300",
    "海南藏族自治州": "632500",
    "果洛藏族自治州": "632600",
    "玉树藏族自治州": "632700",
    "海西蒙古族藏族自治州": "632800",
    "银川市": "640100",
    "石嘴山市": "640200",
    "吴忠市": "640300",
    "固原市": "640400",
    "中卫市": "640500",
    "乌鲁木齐市": "650100",
    "克拉玛依市": "650200",
    "吐鲁番地区": "652100",
    "哈密地区": "652200",
    "昌吉回族自治州": "652300",
    "博尔塔拉蒙古自治州": "652700",
    "巴音郭楞蒙古自治州": "652800",
    "阿克苏地区": "652900",
    "克孜勒苏柯尔克孜自治州": "653000",
    "喀什地区": "653100",
    "和田地区": "653200",
    "伊犁哈萨克自治州": "654000",
    "塔城地区": "654200",
    "阿勒泰地区": "654300",
    "自治区直辖县级行政区划": "659000",
    "台湾省": "710000",
    "香港特别行政区": "810100",
    "澳门特别行政区": "820000"
};
function pad(num, n) {
    return Array(n>num?(n-(''+num).length+1):0).join(0)+num;
}

function secondFormat(second) {
    second = second + "";
    second = parseInt(second);
    var format = "";
    var day = second / (24 * 3600);
    if(day > 1){
        format += (Math.floor(day) + "天");
        second -= (Math.floor(day) * 24 * 3600);
    }
    var hour = second / 3600;
    if(hour > 1){
        format += (Math.floor(hour) + "小时");
        second -= (Math.floor(hour) * 3600);
    }
    var minute = second / 60;
    if(minute > 1){
        format += (Math.floor(minute) + "分钟");
        second -= (Math.floor(minute) * 60);
    }
    format += (Math.floor(second) + "秒");
    return format;
}