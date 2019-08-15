// /**
//  * Created by 宏炜 on 2017-07-04.
//  */
//
//
// // $(function() {
// //
// //     getChargeRecords();
// //     getInoutRecords();
// // });
//
// //参考
// function getChargeId() {
//
//     var res1;
//     var res2;
//     if (id) {
//         //今日总收费记录
//         $.ajax({
//             type: 'post',
//             async: false,
//             data: {
//                 parkId: id
//             },
//             dataType: 'json',
//             url: starnetContextPath + "/charge/getChargeRecordsToday",
//             success: function (res) {
//                 res1 = res.data[0][1];
//             }
//         });
//
//         //今日总收费记录组成
//         $.ajax({
//             type: 'post',
//             async: false,
//             data: {
//                 parkId: id
//             },
//             dataType: 'json',
//             url: starnetContextPath + "/charge/getChargeRecordsTodayDetail",
//             success: function (res) {
//                 res2 = res;
//             }
//         });
//
//         //更新数据
//         var pieoption = pieChart.getOption();
//         pieoption.title.text=777+'元' + "\n" + '总金额';
//         var data = new Array();
//         var obj = {};
//         for(var i in res1.data)
//         {
//             obj.value=chargePart.chargePart.data[i][1];
//             obj.name=chargePart.data[i][2];
//             data.push(obj);
//         }
//
//         pieoption.series[0].data=data;
//         pieChart.setOption(pieoption);
//     }
// }
//
//
//
// function getChargeRecords() {
//     var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
//     window.parent.pieChart=pieChart;
//     var pieoption = {
//         title: {
//             text: 666+'元' + "\n" +
//             '总金额',
//             x: 'center',
//             y: 'center',
//             textStyle: {
//                 fontSize: 15,
//                 // fontFamily:'Microsoft YaHei',
//                 color: '#0091FA',
//                 fontWeight: '500'
//             }
//         },
//
//         color: ['rgb(0,150,255)', 'rgb(255,74,77)', 'rgb(255,157,60)', 'rgb(0,221,159)', 'rgb(0,104,199)'],
//         tooltip: {
//             textStyle: {
//                 align: 'center'
//             },
//             trigger: 'item',
//             formatter: "{a} <br/>{b}: {c} ({d}%)"
//         },
//         // legend: {
//         //     orient : 'vertical',
//         //     x : 'left',
//         //     data:nodeName
//         // },
//         calculable: true,
//         // calculableColor: 'rgba(255,111,1,,1)',
//
//         formatter: function (val) {
//             return val.split("-").join("\n");
//         },
//
//         series: [
//             {
//
//                 itemStyle: {
//                     normal: {
//                         formatter: function (val) {   //让series 中的文字进行换行
//                             return val.name.split("-").join("\n");
//                         },
//                         label: {
//                             show: false,
//                             formatter: '{d}%'
//                         },
//                         labelLine: {
//                             show: false
//                         }
//                     },
//                     emphasis: {
//                         label: {
//                             show: false
//                         },
//                         labelLine: {
//                             show: false
//                         }
//                     }
//                 },
//                 allowPointSelect: true,
//                 name: '详情：',
//                 type: 'pie',
//                 radius: ['60%', '80%'],
//                 avoidLabelOverlap: false,
//                 data: [
//                     {value:0, name: '0'},
//
//                 ]
//             }
//         ]
//     };
//
//     pieChart.setOption(pieoption);
//     $(window).resize(pieChart.resize);
// }
//
// //今日停车流量饼图
// function getInoutRecords() {
//     var personnelCompositionPieChart = echarts.init(document.getElementById("personnelCompositionPieChart"));
//     window.parent.personnelCompositionPieChart=personnelCompositionPieChart;
//     var pieoption = {
//         title: {
//             text: '316辆' + "\n" +
//             '车流量',
//             x: 'center',
//             y: 'center',
//             textStyle: {
//                 fontSize: 15,
//                 color: '#0091FA',
//                 fontWeight: '500'
//             }
//         },
//         tooltip: {
//             textStyle: {
//                 align: 'center'
//             },
//             trigger: 'item',
//             formatter: "{a} <br/>{b}: {c} ({d}%)"
//         },
//         color: ['rgb(0,150,255)', 'rgb(255,74,77)', 'rgb(255,157,60)', 'rgb(0,221,159)', 'rgb(0,104,199)'],
//         // legend: {
//         //     orient : 'vertical',
//         //     x : 'left',
//         //     data:nodeName
//         // },
//         calculable: true,
//         series: [
//
//             {
//
//                 itemStyle: {
//                     normal: {
//                         formatter: function (val) {   //让series 中的文字进行换行
//                             return val.name.split("-").join("\n");
//                         },
//                         label: {
//                             show: false,
//                             formatter: '{d}%'
//                         },
//                         labelLine: {
//                             show: false
//                         }
//                     },
//                     emphasis: {
//                         label: {
//                             show: false
//                         },
//                         labelLine: {
//                             show: false
//                         }
//                     }
//                 },
//                 allowPointSelect: true,
//                 name: '详情：',
//                 type: 'pie',
//                 radius: ['60%', '80%'],
//                 avoidLabelOverlap: false,
//                 data: [
//                     {value:0, name: '0'},
//
//                 ],
//             }
//         ]
//     };
//     personnelCompositionPieChart.setOption(pieoption);
//     $(window).resize(personnelCompositionPieChart.resize);
// }
//
