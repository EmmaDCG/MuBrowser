package org.json.tests;

import junit.framework.TestCase;
import org.json.CookieList;
import org.json.JSONException;
import org.json.JSONObject;

public class TestCookieList extends TestCase {
   JSONObject jsonobject = new JSONObject();

   public void testToJsonObject_RandomCookieList() {
      try {
         this.jsonobject = CookieList.toJSONObject("  f%oo = b+l=ah  ; o;n%40e = t.wo ");
         assertEquals("{\n  \"o;n@e\": \"t.wo\",\n  \"f%oo\": \"b l=ah\"\n}", this.jsonobject.toString(2));
         assertEquals("o%3bn@e=t.wo;f%25oo=b l%3dah", CookieList.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonObject_NullKey() {
      try {
         this.jsonobject = CookieList.toJSONObject("  f%oo = b+l=ah  ; o;n%40e = t.wo ");
         this.jsonobject.put("abc", JSONObject.NULL);
         assertEquals("o%3bn@e=t.wo;f%25oo=b l%3dah", CookieList.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public static void testConstructor() {
      CookieList cookielist = new CookieList();
      assertEquals("CookieList", cookielist.getClass().getSimpleName());
   }
}
