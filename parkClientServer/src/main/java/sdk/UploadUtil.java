package sdk;

public class UploadUtil {
	/**
	 *  @brief  初始化停车场SDK，必须在调用下面的方法之前调用。
	 *  @param  [IN] strJsonData	初始化需要的Json格式字符串
	 *  @return 初始化结果的Json格式字符串(使用完须释放空间)
	 */
	public static String init(String strJsonData){
		return SDKLibrary.INSTANCE.UploadUtilNative_Init(strJsonData);
	}

	/**
	 *  @brief  反初始化停车场SDK。
	 *  @return
	 */
	public static void unInit(){
		SDKLibrary.INSTANCE.UploadUtilNative_UnInit();
	}

	/**
	 *  @brief  发送车辆进场订单
	 *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串
	 *  @return 车辆进场结果的Json格式字符串(使用完须释放空间)
	 */
	public static String uploadInParkOrder(String strJsonData){
		return SDKLibrary.INSTANCE.UploadUtilNative_UploadInParkOrder(strJsonData);
	}

	/**
	 *  @brief  发送车辆出场订单
	 *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串
	 *  @return 车辆出场结果的Json格式字符串(使用完须释放空间)
	 */
	public static String uploadOutParkOrder(String strJsonData){
		return SDKLibrary.INSTANCE.UploadUtilNative_UploadOutParkOrder(strJsonData);
	}

	/**
	 *  @brief  获取订单支付状态信息
	 *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串
	 *  @return 车辆订单状态信息结果的Json格式字符串(使用完须释放空间)
	 */
	public static String getPayStatus(String strJsonData){
		return SDKLibrary.INSTANCE.UploadUtilNative_GetPayStatus(strJsonData);
	}

	/**
	 *  @brief  注册获取SDK发送到停车场消息的接口类型实例
	 *  @param  [IN] interfacePark 接口类型实例
	 *  @exception 设置的实例需要保证在使用期间不被释放
	 */
	public static void setInterfacePark(ReceiveDataCallBackProc callback){
		SDKLibrary.INSTANCE.UploadUtilNative_SetReceiveData(callback);
	}

	/**
	 *  @brief  回调函数中处理SDK消息后需要返回给SDK消息时的调用方法
	 *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串
	 *  @return 返回给停车场的Json格式字符串(使用完须释放空间)
	 */
	public static String uploadData(String strJsonData){
		return SDKLibrary.INSTANCE.UploadUtilNative_UploadData(strJsonData);
	}
}
