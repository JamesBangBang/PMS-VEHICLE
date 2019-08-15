var ocxFuncObject = function(){
	var obj = {
		url: null,
		wServerPort: null,
		username: null,
		password: null,
		channel: null,
		trantype: null,
		streamType: null,
		httpport: null,
		ocxObject: null,
		drawLine:null,
		drawRectange:null,
		drawRegionX:null,
		drawRegionY:null,
		drawRegionW:null,
		drawRegionH:null,
		drawPX1:null,
		drawPY1:null,
		drawPX2:null,
		drawPY2:null,
		drawPX3:null,
		drawPY3:null,
		drawPX4:null,
		drawPY4:null,
		drawPX5:null,
		drawPY5:null,
		init: function (params) {

			this.url = params.url;
			this.wServerPort = params.wServerPort;
			this.username = params.username;
			this.password = params.password;
			this.channel = params.channel;
			this.trantype = params.trantype;
			this.streamType = params.streamType;
			this.httpport = params.httpport;
			this.ocxObject = document.getElementById(params.ocxObjectId);

			this.setUrl(this.url);
			this.setWServerPort(this.wServerPort);
			this.setUsername(this.username);
			this.setPassword(this.password);
			this.setChannel(this.channel);
			this.setTrantype(this.trantype);
			this.setStreamType(this.streamType);
			this.setWHttpport(this.httpport);

			this.setDrawLine(params.drawLine);
			this.setDrawRectange(params.drawRectange);
			this.setDrawRegionX(params.drawRegionX);
			this.setDrawRegionY(params.drawRegionY);
			this.setDrawRegionW(params.drawRegionW);
			this.setDrawRegionH(params.drawRegionH);
			this.setDrawPX1(params.drawPX1);
			this.setDrawPY1(params.drawPY1);
			this.setDrawPX2(params.drawPX2);
			this.setDrawPY2(params.drawPY2);
			this.setDrawPX3(params.drawPX3);
			this.setDrawPY3(params.drawPY3);
			this.setDrawPX4(params.drawPX4);
			this.setDrawPY4(params.drawPY4);
			this.setDrawPX5(params.drawPX5);
			this.setDrawPY5(params.drawPY5);

		},
		setUrl: function (data) {
			this.url = data;
			this.ocxObject.url = data;
		},
		setWServerPort: function (data) {
			this.wServerPort = data;
			this.ocxObject.wServerPort = data;
		},
		setUsername: function (data) {
			this.username = data;
			this.ocxObject.username = data;
		},
		setPassword: function (data) {
			this.password = data;
			this.ocxObject.password = data;
		},
		setChannel: function (data) {
			this.channel = data;
			this.ocxObject.channel = data;
		},
		setTrantype: function (data) {
			this.trantype = data;
			this.ocxObject.trantype = data;
		},
		setStreamType: function (data) {
			this.streamType = data;
			this.ocxObject.StreamType = data;
		},
		setWHttpport: function (data) {
			this.httpport = data;
			this.ocxObject.wHttpPort = data;
		},
		getOcxParams: function () {
			alert(JSON.stringify(this));
		},
		startView: function () {
			this.ocxObject.StartView();
		},
		stopView: function () {
			this.ocxObject.StopView();
		},
		setDrawLine:function(data){
			this.ocxObject.DrawLine = data;
			this.drawLine = data;
		},
		setDrawRectange:function(data){
			this.ocxObject.DrawRectange = data;
			this.drawRectange = data;
		},
		setDrawRegionX:function (data) {
			this.ocxObject.Draw_RegionX = data;
			this.drawRegionX = data;
		},
		setDrawRegionY:function(data){
			this.ocxObject.Draw_RegionY = data;
			this.drawRegionY = data;
		},
		setDrawRegionW:function(data){
			this.ocxObject.Draw_RegionW = data;
			this.drawRegionW = data;
		},
		setDrawRegionH:function(data){
			this.ocxObject.Draw_RegionH = data;
			this.drawRegionH = data;
		},
		setDrawPX1:function(data){
			this.ocxObject.Draw_PX1 = data;
			this.drawPX1 = data;
		},
		setDrawPY1:function(data){
			this.ocxObject.Draw_PY1 = data;
			this.drawPY1 = data;
		},
		setDrawPX2:function(data){
			this.ocxObject.Draw_PX2 = data;
			this.drawPX2 = data;
		},
		setDrawPY2:function(data){
			this.ocxObject.Draw_PY2 = data;
			this.drawPY2 = data;
		},
		setDrawPX3:function(data){
			this.ocxObject.Draw_PX3 = data;
			this.drawPX3 = data;
		},
		setDrawPY3:function(data){
			this.ocxObject.Draw_PY3 = data;
			this.drawPY3 = data;
		},
		setDrawPX4:function(data){
			this.ocxObject.Draw_PX4 = data;
			this.drawPX4 = data;
		},
		setDrawPY4:function(data){
			this.ocxObject.Draw_PY4 = data;
			this.drawPY4 = data;
		},
		setDrawPX5:function(data){
			this.ocxObject.Draw_PX5 = data;
			this.drawPX5 = data;
		},
		setDrawPY5:function(data){
			this.ocxObject.Draw_PY5 = data;
			this.drawPY5 = data;
		},
		startDrawLine:function () {
			this.ocxObject.StartDrawLine();
		}
	};
	return obj;
}