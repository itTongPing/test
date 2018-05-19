package apacheHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class HttpClientTest {
	
	
	@Test
	public void test01() throws IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		   HttpGet httpGet = new HttpGet("https://www.baidu.com/");  
		   CloseableHttpResponse response = httpclient.execute(httpGet);  
		   // The underlying HTTP connection is still held by the response object  
		   // to allow the response content to be streamed directly from the network socket.  
		   // In order to ensure correct deallocation of system resources  
		   // the user MUST either fully consume the response content  or abort request  
		   // execution by calling CloseableHttpResponse#close().  
		   //建立的http连接，仍旧被response1保持着，允许我们从网络socket中获取返回的数据  
		   //为了释放资源，我们必须手动消耗掉response1或者取消连接（使用CloseableHttpResponse类的close方法）  
		  
		   try {  
		       System.out.println(response.getStatusLine());  
		       HttpEntity entity = response.getEntity();  
		       
		       
		       if(entity !=null){
			    	  String stringresponse = EntityUtils.toString(entity,"utf-8");
			    	  System.out.println(stringresponse+"------------");
			      }
		       
		       
		       // do something useful with the response body  
		       // and ensure it is fully consumed  
		       EntityUtils.consume(entity);  
		   } finally {  
		       response.close();  
		   } 
		
	}
	
	
	@Test
	public void test02() throws IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		
		HttpPost httpPost = new HttpPost("https://www.baidu.com/");  
		  //拼接参数  
		  List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
		  nvps.add(new BasicNameValuePair("username", "vip"));  
		  nvps.add(new BasicNameValuePair("password", "secret"));  
		  httpPost.setEntity(new UrlEncodedFormEntity(nvps));  
		  CloseableHttpResponse response = httpclient.execute(httpPost);  
		  
		  try {  
		      System.out.println(response.getStatusLine());  
		      HttpEntity entity = response.getEntity();  
		      if(entity !=null){
		    	  String stringresponse = EntityUtils.toString(entity,"utf-8");
		    	  System.out.println(stringresponse+"------------");
		      }
		      // do something useful with the response body  
		      // and ensure it is fully consumed  
		      //消耗掉response  
		      EntityUtils.consume(entity);  
		  } finally {  
		      response.close();  
		  } 
		
		
	}
	
	
	
	public  Response getResponceData(String token,String customerId,String method,Map<String, Object> param,String api)
	{		
		
		String strJson = JSON.toJSONString(param);
		String requestPut = null;
		try {
			requestPut = requestPut(customerId, token, strJson, "application/json", method,api);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Response response = new Response();
		
		if(!requestPut.isEmpty())
		{
			response = JSON.parseObject(requestPut, Response.class);
		}
		return response;
	}
	
	
	
	
	public  String requestPut(String customerId,String token, String strReq, String mydiaType,String method,String api) throws Exception 
	{
		// token = oDuCfVi88b40oOuMYQUOcTh2b/T+uJdDBsJ+VOrlG6Q=1;customerId = 100800;method = getInventory
		String urlPath = api+"/api/service/woms/order/"+method+"?customerId="+customerId+"&token="+token+"&language=en_US";
		HttpPost httpPost = new HttpPost(urlPath);
		StringEntity stringEntity = null;
		stringEntity = new StringEntity(strReq, "UTF-8");
		String result = sendRequest(httpPost, stringEntity, mydiaType.toString());
		System.out.println("返回结果为:" + result);
		return result;
	}
	
	
	
	
	public String sendRequest(HttpEntityEnclosingRequestBase httpRequest,
			HttpEntity stringEntity, String mediaType) throws Exception
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpRequest.setHeader(HttpHeaders.ACCEPT, mediaType);
		httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, mediaType);
		try {
			httpRequest.setEntity(stringEntity);
			ResponseHandler<String> handler = new ResponseHandler<String>()
			{
				public String handleResponse(HttpResponse response)	throws ClientProtocolException, IOException 
				{
					HttpEntity httpEntity = null;
					httpEntity = response.getEntity();
					if (httpEntity != null) {
						return EntityUtils.toString(httpEntity, "UTF-8");
					} else 
					{
						return null;
					}
				}
			};
			String result = httpClient.execute(httpRequest, handler);
			
			return result;
		} catch (Exception e) {
			throw new Exception("failure", e);
		}
	}
	
	
	
	
	

}
