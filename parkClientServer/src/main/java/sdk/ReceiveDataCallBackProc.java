package sdk;

import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

import java.io.IOException;

public interface ReceiveDataCallBackProc extends StdCallCallback {

	public void receive(String msg) throws IOException;
}
