package com.qq.open;

import biz.source_code.base64Coder.Base64Coder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;























public class SnsSigCheck
{
  private static final String CONTENT_CHARSET = "UTF-8";
  private static final String HMAC_ALGORITHM = "HmacSHA1";
  
  public SnsSigCheck() {}
  
  public static String encodeUrl(String input)
    throws OpensnsException
  {
    try
    {
      return URLEncoder.encode(input, "UTF-8").replace("+", "%20").replace("*", "%2A");
    }
    catch (UnsupportedEncodingException e)
    {
      throw new OpensnsException(1804, e);
    }
  }
  








  public static String makeSig(String method, String url_path, HashMap<String, String> params, String secret)
    throws OpensnsException
  {
    String sig = null;
    try
    {
      Mac mac = Mac.getInstance("HmacSHA1");
      
      SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), mac.getAlgorithm());
      
      mac.init(secretKey);
      
      String mk = makeSource(method, url_path, params);
      
      byte[] hash = mac.doFinal(mk.getBytes("UTF-8"));
      

      sig = new String(Base64Coder.encode(hash));
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new OpensnsException(1804, e);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new OpensnsException(1804, e);
    }
    catch (InvalidKeyException e)
    {
      throw new OpensnsException(1804, e);
    }
    return sig;
  }
  






  public static String makeSource(String method, String url_path, HashMap<String, String> params)
    throws OpensnsException
  {
    Object[] keys = params.keySet().toArray();
    
    Arrays.sort(keys);
    
    StringBuilder buffer = new StringBuilder(128);
    
    buffer.append(method.toUpperCase()).append("&").append(encodeUrl(url_path)).append("&");
    
    StringBuilder buffer2 = new StringBuilder();
    
    for (int i = 0; i < keys.length; i++)
    {
      buffer2.append(keys[i]).append("=").append((String)params.get(keys[i]));
      
      if (i != keys.length - 1)
      {
        buffer2.append("&");
      }
    }
    
    buffer.append(encodeUrl(buffer2.toString()));
    
    return buffer.toString();
  }
  
  public static boolean verifySig(String method, String url_path, HashMap<String, String> params, String secret, String sig)
    throws OpensnsException
  {
    params.remove("sig");
    

    codePayValue(params);
    

    String sig_new = makeSig(method, url_path, params, secret);
    

    return sig_new.equals(sig);
  }
  








  public static void codePayValue(Map<String, String> params)
  {
    Set<String> keySet = params.keySet();
    Iterator<String> itr = keySet.iterator();
    
    while (itr.hasNext())
    {
      String key = (String)itr.next();
      String value = (String)params.get(key);
      value = encodeValue(value);
      params.put(key, value);
    }
  }
  





  public static String encodeValue(String s)
  {
    String rexp = "[0-9a-zA-Z!*\\(\\)]";
    StringBuffer sb = new StringBuffer(s);
    StringBuffer sbRtn = new StringBuffer();
    Pattern p = Pattern.compile(rexp);
    


    for (int i = 0; i < sb.length(); i++)
    {
      char temp = sb.charAt(i);
      String tempStr = String.valueOf(temp);
      Matcher m = p.matcher(tempStr);
      
      boolean result = m.find();
      if (!result) {
        tempStr = hexString(tempStr);
      }
      sbRtn.append(tempStr);
    }
    
    return sbRtn.toString();
  }
  





  private static String hexString(String s)
  {
    byte[] b = s.getBytes();
    String retStr = "";
    for (int i = 0; i < b.length; i++)
    {
      String hex = Integer.toHexString(b[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      retStr = "%" + hex.toUpperCase();
    }
    return retStr;
  }
}
