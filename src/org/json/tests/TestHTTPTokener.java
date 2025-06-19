package org.json.tests;

import junit.framework.TestCase;
import org.json.HTTPTokener;
import org.json.JSONException;

public class TestHTTPTokener extends TestCase {
   private HTTPTokener httptokener;

   public void testNextToken_SimpleString() {
      try {
         this.httptokener = new HTTPTokener("{\n  \"Accept-Language\": 'en-us' ,\n  \"Host\": 23");
         assertEquals("{", this.httptokener.nextToken());
         assertEquals("Accept-Language", this.httptokener.nextToken());
         assertEquals(":", this.httptokener.nextToken());
         assertEquals("en-us", this.httptokener.nextToken());
         assertEquals(",", this.httptokener.nextToken());
         assertEquals("Host", this.httptokener.nextToken());
         assertEquals(":", this.httptokener.nextToken());
         assertEquals("23", this.httptokener.nextToken());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_UnterminatedString() {
      try {
         this.httptokener = new HTTPTokener("'en-us");
         this.httptokener.nextToken();
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("Unterminated string. at 7 [character 8 line 1]", var2.getMessage());
      }

   }
}
