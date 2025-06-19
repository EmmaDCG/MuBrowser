package org.json.tests;

import junit.framework.TestCase;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;

public class TestCDL extends TestCase {
   private String string;
   private JSONArray jsonarray;

   @Before
   public void setUp() {
      this.string = "abc,test,123\ngg,hh,jj\naa,bb,cc\n";

      try {
         this.jsonarray = new JSONArray();
         JSONObject jo = new JSONObject();
         JSONObject jo2 = new JSONObject();
         jo.put("abc", (Object)"gg");
         jo.put("test", (Object)"hh");
         jo.put("123", (Object)"jj");
         jo2.put("abc", (Object)"aa");
         jo2.put("test", (Object)"bb");
         jo2.put("123", (Object)"cc");
         this.jsonarray.put((Object)jo);
         this.jsonarray.put((Object)jo2);
      } catch (JSONException var3) {
         var3.printStackTrace();
      }

   }

   public void testToJsonArray() {
      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public static void testToJsonArray_NoNames() {
      try {
         assertEquals((Object)null, CDL.toJSONArray(new JSONArray(), "abc,123"));
      } catch (JSONException var1) {
         var1.printStackTrace();
      }

   }

   public static void testToJsonArray_NullNames() {
      try {
         assertEquals((Object)null, CDL.toJSONArray((JSONArray)null, (String)"abc,123"));
      } catch (JSONException var1) {
         var1.printStackTrace();
      }

   }

   public static void testToJsonArray_NoData() {
      try {
         assertEquals((Object)null, CDL.toJSONArray("abc,123\n"));
      } catch (JSONException var1) {
         var1.printStackTrace();
      }

   }

   public void testToJsonArray_WeirdData() {
      this.string = "abc,test,123\rgg,hh,\"jj\"\raa,\tbb,cc";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public void testToJsonArray_NoClosingQuote() {
      this.string = "abc,test,123\rgg,hh,jj \raa,\"bb ,cc ";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
         fail("Should have thrown Exception");
      } catch (JSONException var4) {
         assertEquals("Missing close quote '\"'. at 35 [character 12 line 5]", var4.getMessage());
      }

      this.string = "abc,test,123\rgg,hh,jj \raa,\"bb ,cc \n";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
         fail("Should have thrown Exception");
      } catch (JSONException var3) {
         assertEquals("Missing close quote '\"'. at 35 [character 0 line 6]", var3.getMessage());
      }

      this.string = "abc,test,123\rgg,hh,jj \raa,\"bb ,cc \r";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
         fail("Should have thrown Exception");
      } catch (JSONException var2) {
         assertEquals("Missing close quote '\"'. at 35 [character 12 line 5]", var2.getMessage());
      }

   }

   public void testToJsonArray_SpaceAfterString() {
      this.string = "abc,test,123\rgg,hh,jj \raa,\"bb\" ,cc\r";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public void testToJsonArray_BadCharacter() {
      this.string = "abc,test,123\rgg,hh,jj \raa,\"bb \"?,cc \r";

      try {
         assertEquals(this.jsonarray.toString(), CDL.toJSONArray(this.string).toString());
         fail("Should have thrown Exception");
      } catch (JSONException var2) {
         assertEquals("Bad character '?' (63). at 32 [character 9 line 5]", var2.getMessage());
      }

   }

   public void testToString() {
      try {
         assertEquals(this.string, CDL.toString(this.jsonarray));
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public void testToString_BadJsonArray() {
      try {
         this.jsonarray = new JSONArray();
         assertEquals((String)null, CDL.toString(this.jsonarray));
         this.jsonarray.put((Object)"abc");
         assertEquals("", CDL.toString(this.jsonarray, this.jsonarray));
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public void testToString_NoNames() {
      try {
         this.jsonarray = new JSONArray();
         JSONObject jo = new JSONObject();
         this.jsonarray.put((Object)jo);
         assertEquals((String)null, CDL.toString(this.jsonarray));
         assertEquals((String)null, CDL.toString(new JSONArray(), this.jsonarray));
         JSONArray names = new JSONArray();
         names.put((Object)"");
         assertEquals("\n", CDL.toString(names, this.jsonarray));
      } catch (JSONException var3) {
         var3.printStackTrace();
      }

   }

   public void testToString_NullNames() {
      try {
         this.jsonarray = new JSONArray();
         JSONObject jo = new JSONObject();
         this.jsonarray.put((Object)jo);
         assertEquals((String)null, CDL.toString((JSONArray)null, this.jsonarray));
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public void testToString_Quotes() {
      try {
         this.jsonarray = CDL.toJSONArray("Comma delimited list test, '\"Strip\"Quotes', 'quote, comma', No quotes, 'Single Quotes', \"Double Quotes\"\n1,'2',\"3\"\n,'It is \"good,\"', \"It works.\"\n\n");
         this.string = CDL.toString(this.jsonarray);
         assertEquals("\"quote, comma\",\"StripQuotes\",Comma delimited list test\n3,2,1\nIt works.,\"It is good,\",\n", this.string);
         assertEquals("[\n {\n  \"quote, comma\": \"3\",\n  \"\\\"Strip\\\"Quotes\": \"2\",\n  \"Comma delimited list test\": \"1\"\n },\n {\n  \"quote, comma\": \"It works.\",\n  \"\\\"Strip\\\"Quotes\": \"It is \\\"good,\\\"\",\n  \"Comma delimited list test\": \"\"\n }\n]", this.jsonarray.toString(1));
         this.jsonarray = CDL.toJSONArray(this.string);
         assertEquals("[\n {\n  \"quote, comma\": \"3\",\n  \"StripQuotes\": \"2\",\n  \"Comma delimited list test\": \"1\"\n },\n {\n  \"quote, comma\": \"It works.\",\n  \"StripQuotes\": \"It is good,\",\n  \"Comma delimited list test\": \"\"\n }\n]", this.jsonarray.toString(1));
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

   }

   public static void testConstructor() {
      CDL cdl = new CDL();
      assertEquals("CDL", cdl.getClass().getSimpleName());
   }
}
