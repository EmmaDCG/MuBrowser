package org.json.tests;

import junit.framework.TestCase;
import org.json.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

public class TestCookie extends TestCase {
   JSONObject jsonobject;

   public static void testToJsonObject_RandomCookieData() {
      try {
         new JSONObject();
         JSONObject jsonobject = Cookie.toJSONObject("f%oo=blah; secure ;expires = April 24, 2002");
         assertEquals("{\n  \"expires\": \"April 24, 2002\",\n  \"name\": \"f%oo\",\n  \"secure\": true,\n  \"value\": \"blah\"\n}", jsonobject.toString(2));
         assertEquals("f%25oo=blah;expires=April 24, 2002;secure", Cookie.toString(jsonobject));
      } catch (JSONException var1) {
         fail(var1.getMessage());
      }

   }

   public static void testEscape() {
      StringBuilder testString = new StringBuilder();
      testString.append('h');

      for(int i = 0; i < 32; ++i) {
         testString.append((char)i);
      }

      testString.append('\n');
      testString.append('\t');
      testString.append('\b');
      testString.append('%');
      testString.append('+');
      testString.append('=');
      testString.append(';');
      String result = "h%00%01%02%03%04%05%06%07%08%09%0a%0b%0c%0d%0e%0f%10%11%12%13%14%15%16%17%18%19%1a%1b%1c%1d%1e%1f%0a%09%08%25%2b%3d%3b";
      assertEquals(result, Cookie.escape(testString.toString()));
   }

   public static void testUnescape() {
      StringBuilder testString = new StringBuilder();
      testString.append('h');

      for(int i = 0; i < 32; ++i) {
         testString.append((char)i);
      }

      testString.append('\n');
      testString.append('\t');
      testString.append('\b');
      testString.append('%');
      testString.append('+');
      testString.append('%');
      testString.append('0');
      testString.append('\r');
      testString.append(' ');
      testString.append(' ');
      testString.append('%');
      testString.append('\n');
      testString.append('z');
      testString.append('z');
      testString.append('=');
      testString.append(';');
      testString.append('%');
      String result = "h%00%01%02%03%04%05%06%07%08%09%0a%0b%0c%0d%0e%0f%10%11%12%13%14%15%16%17%18%19%1a%1b%1c%1d%1e%1f%0a%09%08%25%2b%0\r +%\nzz%3d%3b%";
      assertEquals(testString.toString(), Cookie.unescape(result));
   }

   public void testToJsonObject_ValueWithoutEquals() {
      try {
         this.jsonobject = Cookie.toJSONObject("f%oo=blah; notsecure ;expires = April 24, 2002");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Missing '=' in cookie parameter. at 22 [character 23 line 1]", var2.getMessage());
      }

   }

   public static void testToString() {
      try {
         JSONObject jsonobject = new JSONObject();
         jsonobject.put("secure", true);
         jsonobject.put("expires", (Object)"string1");
         jsonobject.put("domain", (Object)"string2");
         jsonobject.put("path", (Object)"string3");
         jsonobject.put("name", (Object)"foo");
         jsonobject.put("value", (Object)"bar");
         assertEquals("foo=bar;expires=string1;domain=string2;path=string3;secure", Cookie.toString(jsonobject));
      } catch (JSONException var1) {
         fail(var1.getMessage());
      }

   }

   public static void testConstructor() {
      Cookie cookie = new Cookie();
      assertEquals("Cookie", cookie.getClass().getSimpleName());
   }
}
