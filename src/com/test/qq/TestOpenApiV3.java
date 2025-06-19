package com.test.qq;

import com.mu.config.Global;
import com.qq.open.OpenApiV3;
import com.qq.open.OpensnsException;
import flexjson.JSONDeserializer;
import java.io.PrintStream;
import java.util.HashMap;















public class TestOpenApiV3
{
  public TestOpenApiV3() {}
  
  public static void main(String[] args)
  {
    String appid = "1105007049";
    String appkey = "wFfPfy3KS6yfg84Y";
    

    String openid = "1FA5BC374E1012197BD004E3B613D675";
    String openkey = "40A10E780A8E442CD74D39CF55FAA7E9";
    


    String serverName = "119.147.19.43";
    

    String pf = "qqgame";
    
    OpenApiV3 sdk = new OpenApiV3(appid, appkey);
    sdk.setServerName(serverName);
    
    System.out.println("===========test GetUserInfo===========");
    testGetUserInfo(sdk, openid, openkey, pf);
    
    get_result("/v3/user/get_info", String.format("{'openid':'%s', 'openkey':'%s'}", new Object[] { openid, openkey }));
  }
  









  public static void testGetUserInfo(OpenApiV3 sdk, String openid, String openkey, String pf)
  {
    String scriptName = "/v3/user/get_info";
    

    String protocol = "http";
    

    HashMap<String, String> params = new HashMap();
    params.put("openid", openid);
    params.put("openkey", openkey);
    params.put("pf", pf);
    
    try
    {
      String resp = sdk.api(scriptName, params, protocol);
      System.out.println(resp);
    }
    catch (OpensnsException e)
    {
      System.out.printf("Request Failed. code:%d, msg:%s\n", new Object[] { Integer.valueOf(e.getErrorCode()), e.getMessage() });
      e.printStackTrace();
    }
  }
  
  public static String get_result(String name, String paramStr)
  {
    try {
      OpenApiV3 sdk = new OpenApiV3(Global.appid, Global.appkey);
      sdk.setServerName("119.147.19.43");
      

      String protocol = "http";
      

      HashMap<String, String> params = (HashMap)new JSONDeserializer().deserialize(paramStr);
      params.put("pf", "qqgame");
      
      String result = sdk.api(name, params, protocol);
      System.out.printf("%s result: %s\n", new Object[] { name, result });
      return result;
    }
    catch (Exception e)
    {
      System.out.printf("%s result: %s\n", new Object[] { name, e.getMessage() });
      e.printStackTrace(); }
    return null;
  }
}
