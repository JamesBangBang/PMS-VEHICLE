package sdk;

import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface SDKLibrary extends StdCallLibrary {
    String libName = AppInfo.dllParh + "SDK64.dll";
//    String libName = "D:\\SMARTART\\IPARK_TRANSMISSION\\tomcat\\webapps\\ROOT\\WEB-INF\\classes\\SDK64.dll";
    SDKLibrary INSTANCE = (SDKLibrary)
            Native.loadLibrary(libName, SDKLibrary.class);

    /**
     *  @brief  初始化停车场SDK，必须在调用下面的方法之前调用。
     *  @param  [IN] strJsonData	初始化需要的Json格式字符串(UTF-8编码)
     *  @return 初始化结果的Json格式字符串(使用完须释放空间)
     */
    public String UploadUtilNative_Init(String strJsonData);

    /**
     *  @brief  反初始化停车场SDK。
     *  @return
     */
    public void UploadUtilNative_UnInit();

    /**
     *  @brief  发送车辆进场订单
     *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串(UTF-8编码)
     *  @return 车辆进场结果的Json格式字符串(使用完须释放空间)
     */
    public String UploadUtilNative_UploadInParkOrder(String strJsonData);

    /**
     *  @brief  发送车辆出场订单
     *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串(UTF-8编码)
     *  @return 车辆出场结果的Json格式字符串(使用完须释放空间)
     */
    public String UploadUtilNative_UploadOutParkOrder(String strJsonData);

    /**
     *  @brief  获取订单支付状态信息
     *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串(UTF-8编码)
     *  @return 车辆订单状态信息结果的Json格式字符串(使用完须释放空间)
     */
    public String UploadUtilNative_GetPayStatus(String strJsonData);

    /**
     *  @brief  注册获取SDK发送到停车场消息的回调函数
     *  @param  [IN] func 回调函数方法
     *  @note   此回调为后台线程调用，注意线程安全和界面操作
     */
    public void UploadUtilNative_SetReceiveData(ReceiveDataCallBackProc func);

    /**
     *  @brief  回调函数中处理SDK消息后需要返回给SDK消息时的调用方法
     *  @param  [IN] strJsonData	发送给停车场SDK的Json格式字符串(UTF-8编码)
     *  @return 返回给停车场的Json格式字符串(使用完须释放空间)
     */
    public String UploadUtilNative_UploadData(String strJsonData);
}
