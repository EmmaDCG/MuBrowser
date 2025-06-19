package org.json.tests;

import junit.framework.TestCase;
import org.json.JSONException;

public class TestJSONException extends TestCase {
   JSONException jsonexception;

   public void testConstructor_String() {
      this.jsonexception = new JSONException("test String");
      assertEquals("test String", this.jsonexception.getMessage());
   }

   public void testConstructor_Exception() {
      Exception e = new Exception();
      this.jsonexception = new JSONException(e);
      assertEquals(e, this.jsonexception.getCause());
   }
}
