package com.qq.open;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;






























public class SnsStat
{
  private static final String STAT_SVR_NAME = "apistat.tencentyun.com";
  private static final int STAT_SVR_PORT = 19888;
  
  public SnsStat() {}
  
  public static void statReport(long startTime, String serverName, HashMap<String, String> params, String method, String protocol, int rc, String scriptName)
  {
    try
    {
      long endTime = System.currentTimeMillis();
      double timeCost = (endTime - startTime) / 1000.0D;
      

      String sendStr = String.format("{\"appid\":%s, \"pf\":\"%s\",\"rc\":%d,\"svr_name\":\"%s\", \"interface\":\"%s\",\"protocol\":\"%s\",\"method\":\"%s\",\"time\":%.4f,\"timestamp\":%d,\"collect_point\":\"sdk-java-v3\"}", new Object[] {
        params.get("appid"), 
        params.get("pf"), 
        Integer.valueOf(rc), 
        InetAddress.getByName(serverName).getHostAddress(), 
        scriptName, 
        protocol, 
        method, 
        Double.valueOf(timeCost), 
        Long.valueOf(endTime / 1000L) });
      


      DatagramSocket client = new DatagramSocket();
      byte[] sendBuf = sendStr.getBytes();
      

      String reportSvrIp = "apistat.tencentyun.com";
      int reportSvrport = 19888;
      
      InetAddress addr = InetAddress.getByName(reportSvrIp);
      DatagramPacket sendPacket = 
        new DatagramPacket(sendBuf, sendBuf.length, addr, reportSvrport);
      
      client.send(sendPacket);
      client.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
