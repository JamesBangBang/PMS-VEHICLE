var ObjectList = document.getElementsByTagName('object');
var SNMSCloudOcx;
//控件初始化，默认只初始化第一个
function SnCloud_CreateInstance()
{
	for(var i in ObjectList){
		if(ObjectList[i].classid == 'clsid:DEEE91FD-B7EE-451A-BF54-F7A289BC422D'){
			SNMSCloudOcx = ObjectList[i]
			SNMSCloudOcx.SnCloud_CreateInstance();
			return;
		}
	}
}

//播放接口
//参数 ip：综合平台IP
//参数 port：综合平台端口
//参数 username：综合平台权限用户名
//参数 password：综合平台权限用户密码（md5加密一次后）
//参数 devId：综合平台设备Id
function SnCloud_StartVideo(ip,port,username,password,devId)
{
	SnCloud_StopVideo();
	SnCloud_Logon(ip,port,username,password);
	SNMSCloudOcx.SnCloud_StartVideo(devId,1);
	//预览:8a92c87d54514b6901547ae87b30008a
	//SNMSCloudOcx.SnCloud_StartVideo('8a92c87d54514b6901547ae87b30008a',1);
}
//视易设备播放接口
//参数 ip：综合平台IP
//参数 port：综合平台端口
//参数 username：综合平台权限用户名
//参数 password：综合平台权限用户密码（md5加密一次后）
//参数 devId：综合平台设备Id
function SnCloud_StartEVideo(ip,port,username,password,devId)
{
	SnCloud_StopVideo();
	SnCloud_Logon(ip,port,username,password);
	SNMSCloudOcx.SnCloud_StartVideo(devId,1);
	//视易预览:8a92c86e4ed76d8f014eddedb2a90038
	//SNMSCloudOcx.SnCloud_StartVideo('f84e61c34a3d11e5a5ed782bcb574aa8',1);
}
//控件画面关闭接口
function SnCloud_StopVideo()
{
	SNMSCloudOcx.SnCloud_StopVideo();
}
//控件截图录像路径设置接口
//参数 picturePath：截图保存路径
//参数 reocrdPath：录像保存路径
function SnCloud_SetPath(picturePath,reocrdPath)
{
	SNMSCloudOcx.SnCloud_SetPath(picturePath,reocrdPath);
	//SNMSCloudOcx.SnCloud_SetPath('C:\\Picture','C:\\Reocrd');
}
//控件版本号获取接口
function SnCloud_GetVersion()
{
	return SNMSCloudOcx.SnCloud_GetVersion();
}

//视频回放接口
//参数 ip：综合平台IP
//参数 port：综合平台端口10080
//参数 username：综合平台权限用户名
//参数 password：综合平台权限用户密码（md5加密一次后）
//参数 devId：综合平台设备Id
//参数 stime：开始时间
//参数 etime：结束时间
function SnCloud_StartPlayback(ip,port,username,password,devId,stime,etime)
{
	SnCloud_StopVideo();
	SnCloud_Logon(ip,port,username,password);
	SNMSCloudOcx.SnCloud_StartPlayback(devId,stime,etime);
	//SNMSCloudOcx.SnCloud_StartPlayback('8a92c86e535acc8a01535e0de3230008', '2016-04-22 00:00:00', '2016-04-22 23:59:59');
}
//广播设备查询接口
function SnCloud_StartUPnP()
{
	SNMSCloudOcx.SnCloud_StartUPnP();
}
// 获取设备资源信息
// 参数 SourceTypes: 资源类型
// 资源类型列表，其以逗号隔开，值分别如下 （如：0,5）
	// 0	位置节点
	// 1	编码设备
	// 2	报警主机
	// 3	解码设备
	// 4	灯控设备
	// 5	通道类型	
// 返回值：资源列表 
// 节点ID,,节点父ID,,节点资源类型,,节点名称,,节点其它信息;;
function SnCloud_GetSourceInfo(ip,port,username,password,sourceTypes){
	SnCloud_Logon(ip,port,username,password)
	return SNMSCloudOcx.SnCloud_GetSourceInfo(sourceTypes);
}

//播放前登录验证接口，无需调用
function SnCloud_Logon(ip,port,username,password)
{ 
	SNMSCloudOcx.SnCloud_Logon(ip, port,username,password,username,password);
	//SNMSCloudOcx.SnCloud_Logon('10.18.72.253', 10080, 'root', 'e10adc3949ba59abbe56e057f20f883e', 'root', 'e10adc3949ba59abbe56e057f20f883e');
}


//控件自动初始化，这里只能初始化一个控件
window.onload = function()
{
	SnCloud_CreateInstance();
	SNMSCloudOcx.SnCloud_SetPath('C:\\Picture','C:\\Reocrd');
}
//{------------------------------------------------------------------------------
//【功能】获取报警类型
//【参数】
//【返回值】报警类型列表
//报警类型标识ID,,类型名称;

//------------------------------------------------------------------------------}
function SnCloud_GetAlarmTypes(ip,port,username,password)
{
	SnCloud_Logon(ip,port,username,password);
	var SourceInfo;
	SourceInfo = SNMSCloudOcx.SnCloud_GetAlarmTypes();
	return SourceInfo;
}

//{------------------------------------------------------------------------------
//【功能】获取报警信息列表
//【参数】
//AlarmTypeId: 报警类型
//DevName: 报警设备名称(检索条件，支持模糊搜索)
//AlarmPosi: 报警地点(检索条件，支持模糊搜索)
//AlarmTiemB: 报警时间起(检索条件)
//AlarmTiemE: 报警时间止(检索条件)
//StartLine: 开始行(必填)
//LineCnt：行数(必填)
//【返回值】报警信息列表，按照报警时间倒序排列
//报警ID,,报警设备ID,,报警设备名称,,报警地点,,报警时间,,报警类型,,报警内容,,报警图片索引;;
//报警ID2,,报警设备ID2,,报警设备名称2,,报警地点2,,报警时间2,,报警类型2,,报警内容2,,报警图片索引2;;
//如下:
//8a92c86e5692133c01569244abb60002,,8a92c86e5692133c01569244abb60003,,园区门口摄像头,,园区门口,,2016-11-29 16:50:00,,拌线报警,,拌线报警详情,,AlarmPic//199.jpg;;
//8a92c86e5692133c01569244abb60003,,8a92c86e5692133c01569244abb60004,,园区门外摄像头,,园区门外,,2016-11-29 16:50:00,,拌线报警,,拌线报警详情,,AlarmPic//200.jpg;;	
//------------------------------------------------------------------------------}
function SnCloud_GetAlarmInfos(ip,port,username,password,AlarmTypeId,DevName,AlarmPosi,AlarmTiemB,AlarmTiemE,StartLine,LineCnt)
{
	SnCloud_Logon(ip,port,username,password);
	var SourceInfo;
	SourceInfo = SNMSCloudOcx.SnCloud_GetAlarmInfos(AlarmTypeId,DevName,AlarmPosi,AlarmTiemB,AlarmTiemE,StartLine, LineCnt);
	// SourceInfo = SNMSCloudOcx.SnCloud_GetAlarmInfos(-1,'','','','',0, 10);
	return SourceInfo;
}

// 启动报警推送
function SnClound_StartAlarmInfoNotify(ip,port,username,password){
	SnCloud_Logon(ip,port,username,password);
}


// 【功能】获取报警图片
// 【参数】PicIndex: 报警图片索引
// 【返回值】报警图片存储的绝对路径
function SnCloud_GetAlarmPic(picIndex){
	return SNMSCloudOcx.SnCloud_GetAlarmPic(picIndex);
}
//【功能】调用预置点后开启通道预览
//参数 ip: 综合平台IP
//参数 port: 综合平台端口
//参数 username: 综合平台权限用户名
//参数 password: 综合平台权限用户密码（md5加密一次后）
//参数 channelId: 通道ID
//参数 streamType: 码流类型，0主码流，1子码流
//参数 prePointIndex: 大于等于0=预置点号 -1=不调用预置点
//【返回值】无
function SnCloud_StartVideoWithPrePoint(ip,port,username,password,channelId,streamType,prePointIndex){
	SnCloud_Logon(ip,port,username,password);
	SNMSCloudOcx.SnCloud_StartVideoWithPrePoint(channelId,streamType,prePointIndex);
}