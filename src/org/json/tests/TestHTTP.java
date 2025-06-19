package org.json.tests;

import junit.framework.TestCase;
import org.json.HTTP;
import org.json.JSONObject;

public class TestHTTP extends TestCase {
   JSONObject jsonobject = new JSONObject();

   public void testToJsonObject_Request() {
      try {
         this.jsonobject = HTTP.toJSONObject("GET / HTTP/1.0\nAccept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*\nAccept-Language: en-us\nUser-Agent: Mozilla/4.0 (compatible; MSIE 5.5; Windows 98; Win 9x 4.90; T312461; Q312461)\nHost: www.nokko.com\nConnection: keep-alive\nAccept-encoding: gzip, deflate\n");
         assertEquals("{\n  \"Accept-Language\": \"en-us\",\n  \"Request-URI\": \"/\",\n  \"Host\": \"www.nokko.com\",\n  \"Method\": \"GET\",\n  \"Accept-encoding\": \"gzip, deflate\",\n  \"User-Agent\": \"Mozilla/4.0 (compatible; MSIE 5.5; Windows 98; Win 9x 4.90; T312461; Q312461)\",\n  \"HTTP-Version\": \"HTTP/1.0\",\n  \"Connection\": \"keep-alive\",\n  \"Accept\": \"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*\"\n}", this.jsonobject.toString(2));
         assertEquals("GET \"/\" HTTP/1.0\r\nAccept-Language: en-us\r\nHost: www.nokko.com\r\nAccept-encoding: gzip, deflate\r\nUser-Agent: Mozilla/4.0 (compatible; MSIE 5.5; Windows 98; Win 9x 4.90; T312461; Q312461)\r\nConnection: keep-alive\r\nAccept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*\r\n\r\n", HTTP.toString(this.jsonobject));
      } catch (Exception var2) {
         fail(var2.toString());
      }

   }

   public void testToJsonObject_Response() {
      try {
         this.jsonobject = HTTP.toJSONObject("HTTP/1.1 200 Oki Doki\nDate: Sun, 26 May 2002 17:38:52 GMT\nServer: Apache/1.3.23 (Unix) mod_perl/1.26\nKeep-Alive: timeout=15, max=100\nConnection: Keep-Alive\nTransfer-Encoding: chunked\nContent-Type: text/html\n");
         assertEquals("{\n  \"Reason-Phrase\": \"Oki Doki\",\n  \"Status-Code\": \"200\",\n  \"Transfer-Encoding\": \"chunked\",\n  \"Date\": \"Sun, 26 May 2002 17:38:52 GMT\",\n  \"Keep-Alive\": \"timeout=15, max=100\",\n  \"HTTP-Version\": \"HTTP/1.1\",\n  \"Content-Type\": \"text/html\",\n  \"Connection\": \"Keep-Alive\",\n  \"Server\": \"Apache/1.3.23 (Unix) mod_perl/1.26\"\n}", this.jsonobject.toString(2));
         assertEquals("HTTP/1.1 200 Oki Doki\r\nTransfer-Encoding: chunked\r\nDate: Sun, 26 May 2002 17:38:52 GMT\r\nKeep-Alive: timeout=15, max=100\r\nContent-Type: text/html\r\nConnection: Keep-Alive\r\nServer: Apache/1.3.23 (Unix) mod_perl/1.26\r\n\r\n", HTTP.toString(this.jsonobject));
      } catch (Exception var2) {
         fail(var2.toString());
      }

   }

   public void testToString_NullKey() {
      try {
         this.jsonobject = new JSONObject("{\n  \"Reason-Phrase\": \"Oki Doki\",\n  \"Status-Code\": \"200\",\n  \"Transfer-Encoding\": \"chunked\",\n  \"Date\": \"Sun, 26 May 2002 17:38:52 GMT\",\n  \"Keep-Alive\": \"timeout=15, max=100\",\n  \"HTTP-Version\": \"HTTP/1.1\",\n  \"Content-Type\": \"text/html\",\n  \"Connection\": \"Keep-Alive\",\n  \"Server\": \"Apache/1.3.23 (Unix) mod_perl/1.26\"\n}");
         this.jsonobject.put("testKey", JSONObject.NULL);
         assertEquals("HTTP/1.1 200 Oki Doki\r\nDate: Sun, 26 May 2002 17:38:52 GMT\r\nTransfer-Encoding: chunked\r\nKeep-Alive: timeout=15, max=100\r\nConnection: Keep-Alive\r\nContent-Type: text/html\r\nServer: Apache/1.3.23 (Unix) mod_perl/1.26\r\n\r\n", HTTP.toString(this.jsonobject));
      } catch (Exception var2) {
         fail(var2.toString());
      }

   }

   public void testToString_StatusCodeButNoReasonPhrase() {
      try {
         this.jsonobject = new JSONObject("{\n  \"Status-Code\": \"200\",\n  \"Transfer-Encoding\": \"chunked\",\n  \"Date\": \"Sun, 26 May 2002 17:38:52 GMT\",\n  \"Keep-Alive\": \"timeout=15, max=100\",\n  \"HTTP-Version\": \"HTTP/1.1\",\n  \"Content-Type\": \"text/html\",\n  \"Connection\": \"Keep-Alive\",\n  \"Server\": \"Apache/1.3.23 (Unix) mod_perl/1.26\"\n}");
         HTTP.toString(this.jsonobject);
         fail("Should have thrown an exception.");
      } catch (Exception var2) {
         assertEquals("Not enough material for an HTTP header.", var2.getMessage());
      }

   }

   public void testToString_MethodButNoRequestUri() {
      try {
         this.jsonobject = new JSONObject("{\n  \"Accept-Language\": \"en-us\",\n  \"Host\": \"www.nokko.com\",\n  \"Method\": \"GET\",\n  \"Accept-encoding\": \"gzip, deflate\",\n  \"User-Agent\": \"Mozilla/4.0 (compatible; MSIE 5.5; Windows 98; Win 9x 4.90; T312461; Q312461)\",\n  \"HTTP-Version\": \"HTTP/1.0\",\n  \"Connection\": \"keep-alive\",\n  \"Accept\": \"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*\"\n}");
         HTTP.toString(this.jsonobject);
         fail("Should have thrown an exception.");
      } catch (Exception var2) {
         assertEquals("Not enough material for an HTTP header.", var2.getMessage());
      }

   }

   public static void testConstructor() {
      HTTP http = new HTTP();
      assertEquals("HTTP", http.getClass().getSimpleName());
   }
}
