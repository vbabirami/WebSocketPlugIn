package websocketConnection

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
@WebSocket
public  class WebSocketConnect {
	private final CountDownLatch closeLatch;
	@SuppressWarnings("unused")
	private Session session;
	private String textToSend;
	public String reply


	public WebSocketConnect() {
		this.closeLatch = new CountDownLatch(1);
	}

	@Keyword
	public CreateWebSocketConnection() {

		return new WebSocketConnect()
	}

	/*public void setTextToSend(String textToSend) {
	 this.textToSend = textToSend;
	 }*/

	@Keyword
	public sendMessage(String s) {
		return this.textToSend = s
	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration,unit);
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n",statusCode,reason);
		this.session = null;
	}



	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n",session);
		this.session = session;


		try{

			Future<Void> fut;
			fut = session.getRemote().sendStringByFuture(this.textToSend);
			fut.get(20,TimeUnit.SECONDS); // wait for send to complete.


		}
		catch (Throwable t) {
			println("Exception caught")
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("Got msg: %s%n",msg);
		this.reply = msg

		session.close(StatusCode.NORMAL,"I'm done");
	}
}




