package org.json.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.Before;

public class TestJSONArray extends TestCase {
   private JSONArray jsonarray;
   private String string;

   @Before
   public void setUp() {
      this.jsonarray = new JSONArray();
      this.string = "";
   }

   public static void testJsonArray_IntWithLeadingZeros() {
      try {
         String string = "[001122334455]";
         JSONArray jsonarray = new JSONArray(string);
         assertEquals("[1122334455]", jsonarray.toString());
      } catch (Exception var3) {
         fail(var3.toString());
      }

   }

   public static void testJsonArray_ScintificNotation() {
      try {
         String string = "[666e666]";
         JSONArray jsonarray = new JSONArray(string);
         assertEquals("[\"666e666\"]", jsonarray.toString());
      } catch (Exception var3) {
         fail(var3.toString());
      }

   }

   public static void testJsonArray_DoubleWithLeadingAndTrailingZeros() {
      try {
         String string = "[00.10]";
         JSONArray jsonarray = new JSONArray(string);
         assertEquals("[0.1]", jsonarray.toString());
      } catch (Exception var3) {
         fail(var3.toString());
      }

   }

   public void testConstructor_MissingValue() {
      try {
         this.jsonarray = new JSONArray("[\n\r\n\r}");
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Missing value at 5 [character 0 line 4]", var2.getMessage());
      }

   }

   public void testConstructor_Nan() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(Double.NaN);
         this.jsonarray.toString();
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
      }

   }

   public void testConstructor_NegativeInfinity() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(Double.NEGATIVE_INFINITY);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
      }

   }

   public void testConstructor_PositiveInfinity() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(Double.POSITIVE_INFINITY);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
      }

   }

   public void testPut_PositiveInfinity() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(Double.POSITIVE_INFINITY);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
      }

   }

   public void testGetDouble_EmptyArray() {
      try {
         this.jsonarray.getDouble(0);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] not found.", var2.getMessage());
      }

   }

   public void testGet_NegativeIndex() {
      try {
         this.jsonarray.get(-1);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_Nan() {
      try {
         this.jsonarray.put(Double.NaN);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
      }

   }

   public void testConstructor_Object() {
      try {
         this.jsonarray = new JSONArray(new Object());
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSONArray initial value should be a string or collection or array.", var2.getMessage());
      }

   }

   public void testConstructor_BadJson() {
      try {
         this.string = "[)";
         this.jsonarray = new JSONArray(this.string);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Expected a ',' or ']' at 3 [character 4 line 1]", var2.getMessage());
      }

   }

   public void testToString_Locations() {
      try {
         this.string = " [\"San Francisco\", \"New York\", \"Seoul\", \"London\", \"Seattle\", \"Shanghai\"]";
         this.jsonarray = new JSONArray(this.string);
         assertEquals("[\"San Francisco\",\"New York\",\"Seoul\",\"London\",\"Seattle\",\"Shanghai\"]", this.jsonarray.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testConstructor_Collection() {
      Collection stringCol = new Stack();
      stringCol.add("string1");
      stringCol.add("string2");
      stringCol.add("string3");
      stringCol.add("string4");
      this.jsonarray = new JSONArray(stringCol);
      assertEquals("[\"string1\",\"string2\",\"string3\",\"string4\"]", this.jsonarray.toString());
   }

   public void testConstructor_NullCollection() {
      Collection stringCol = null;
      this.jsonarray = new JSONArray((Collection)stringCol);
      assertEquals("[]", this.jsonarray.toString());
   }

   public void testConstructor_StringArray() {
      try {
         this.jsonarray = new JSONArray(new String[]{"string1", "string2"});
         assertEquals("[\"string1\",\"string2\"]", this.jsonarray.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOpt() {
      try {
         this.jsonarray = new JSONArray(new String[]{"string1", "string2"});
         assertEquals("string1", this.jsonarray.opt(0));
         assertEquals("string2", this.jsonarray.opt(1));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_Exception() {
      this.jsonarray = new JSONArray();
      class BadJsonString implements JSONString {
         public String toJSONString() {
            String[] arString = new String[]{"abc"};
            return arString[1];
         }
      }

      this.jsonarray.put((Object)(new BadJsonString()));
      assertEquals((String)null, this.jsonarray.toString());
   }

   public void testToString_Indents() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)(new JSONArray()).put((Object)(new JSONArray())).put((Object)(new JSONArray()).put((Object)"123").put((Object)"abc")));
         assertEquals("[\n    \"123\",\n    {\"abc\": \"123\"},\n    \"abc\",\n    [\n        [],\n        [\n            \"123\",\n            \"abc\"\n        ]\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("[\"123\"]", (new JSONArray()).put((Object)"123").toString(4));
         assertEquals("[]", (new JSONArray()).toString(4));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGet_InvalidIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.get(1);
      } catch (JSONException var2) {
         assertEquals("JSONArray[1] not found.", var2.getMessage());
      }

   }

   public void testGet_ValidIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         assertEquals("123", this.jsonarray.get(0));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"true");
         this.jsonarray.put((Object)"false");
         this.jsonarray.put(true);
         this.jsonarray.put(false);
         this.jsonarray.put((Object)"TRUE");
         this.jsonarray.put((Object)"FALSE");
         assertTrue(this.jsonarray.getBoolean(0));
         assertFalse(this.jsonarray.getBoolean(1));
         assertTrue(this.jsonarray.getBoolean(2));
         assertFalse(this.jsonarray.getBoolean(3));
         assertTrue(this.jsonarray.getBoolean(4));
         assertFalse(this.jsonarray.getBoolean(5));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean_NonBoolean() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.getBoolean(0);
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a boolean.", var2.getMessage());
      }

   }

   public void testOptBoolean() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"true");
      this.jsonarray.put((Object)"false");
      this.jsonarray.put(true);
      this.jsonarray.put(false);
      this.jsonarray.put((Object)"TRUE");
      this.jsonarray.put((Object)"FALSE");
      this.jsonarray.put((Object)"grass");
      assertTrue(this.jsonarray.optBoolean(0));
      assertFalse(this.jsonarray.optBoolean(1));
      assertTrue(this.jsonarray.optBoolean(2));
      assertFalse(this.jsonarray.optBoolean(3));
      assertTrue(this.jsonarray.optBoolean(4));
      assertFalse(this.jsonarray.optBoolean(5));
      assertFalse(this.jsonarray.optBoolean(6));
   }

   public void testGetInt() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45);
         this.jsonarray.put(-98);
         assertEquals(123, this.jsonarray.getInt(0));
         assertEquals(-12, this.jsonarray.getInt(1));
         assertEquals(45, this.jsonarray.getInt(2));
         assertEquals(-98, this.jsonarray.getInt(3));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetInt_NonInteger() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"abc");
         this.jsonarray.getInt(0);
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a number.", var2.getMessage());
      }

   }

   public void testOptInt() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      this.jsonarray.put((Object)"-12");
      this.jsonarray.put(45);
      this.jsonarray.put(-98);
      this.jsonarray.put((Object)"abc");
      assertEquals(123, this.jsonarray.optInt(0));
      assertEquals(-12, this.jsonarray.optInt(1));
      assertEquals(45, this.jsonarray.optInt(2));
      assertEquals(-98, this.jsonarray.optInt(3));
      assertEquals(0, this.jsonarray.optInt(4));
   }

   public void testGetDouble() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45);
         this.jsonarray.put(-98);
         this.jsonarray.put((Object)"123.5");
         this.jsonarray.put((Object)"-12.87");
         this.jsonarray.put(45.22D);
         this.jsonarray.put(-98.18D);
         assertEquals(123.0D, this.jsonarray.getDouble(0));
         assertEquals(-12.0D, this.jsonarray.getDouble(1));
         assertEquals(45.0D, this.jsonarray.getDouble(2));
         assertEquals(-98.0D, this.jsonarray.getDouble(3));
         assertEquals(123.5D, this.jsonarray.getDouble(4));
         assertEquals(-12.87D, this.jsonarray.getDouble(5));
         assertEquals(45.22D, this.jsonarray.getDouble(6));
         assertEquals(-98.18D, this.jsonarray.getDouble(7));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetDouble_NonDouble() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"abc");
         this.jsonarray.getDouble(0);
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a number.", var2.getMessage());
      }

   }

   public void testOptDouble() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      this.jsonarray.put((Object)"-12");
      this.jsonarray.put(45);
      this.jsonarray.put(-98);
      this.jsonarray.put((Object)"123.5");
      this.jsonarray.put((Object)"-12.87");

      try {
         this.jsonarray.put(45.22D);
         this.jsonarray.put(-98.18D);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

      assertEquals(123.0D, this.jsonarray.optDouble(0));
      assertEquals(-12.0D, this.jsonarray.optDouble(1));
      assertEquals(45.0D, this.jsonarray.optDouble(2));
      assertEquals(-98.0D, this.jsonarray.optDouble(3));
      assertEquals(123.5D, this.jsonarray.optDouble(4));
      assertEquals(-12.87D, this.jsonarray.optDouble(5));
      assertEquals(45.22D, this.jsonarray.optDouble(6));
      assertEquals(-98.18D, this.jsonarray.optDouble(7));
      assertEquals(Double.NaN, this.jsonarray.optDouble(8));
   }

   public void testGetLong() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45L);
         this.jsonarray.put(-98L);
         assertEquals(123L, this.jsonarray.getLong(0));
         assertEquals(-12L, this.jsonarray.getLong(1));
         assertEquals(45L, this.jsonarray.getLong(2));
         assertEquals(-98L, this.jsonarray.getLong(3));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetLong_NonLong() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"abc");
         this.jsonarray.getLong(0);
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a number.", var2.getMessage());
      }

   }

   public void testOptLong() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      this.jsonarray.put((Object)"-12");
      this.jsonarray.put(45L);
      this.jsonarray.put(-98L);
      assertEquals(123L, this.jsonarray.optLong(0));
      assertEquals(-12L, this.jsonarray.optLong(1));
      assertEquals(45L, this.jsonarray.optLong(2));
      assertEquals(-98L, this.jsonarray.optLong(3));
      assertEquals(0L, this.jsonarray.optLong(8));
   }

   public void testGetString() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)"123");
         assertEquals("123", this.jsonarray.getString(0));
         assertEquals("-12", this.jsonarray.getString(1));
         assertEquals("abc", this.jsonarray.getString(2));
         assertEquals("123", this.jsonarray.getString(3));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetString_NonString() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(123);
         this.jsonarray.getString(0);
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] not a string.", var2.getMessage());
      }

   }

   public void testOptString() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      this.jsonarray.put((Object)"-12");
      this.jsonarray.put((Object)"abc");
      this.jsonarray.put((Object)"123");
      assertEquals("123", this.jsonarray.optString(0));
      assertEquals("-12", this.jsonarray.optString(1));
      assertEquals("abc", this.jsonarray.optString(2));
      assertEquals("123", this.jsonarray.optString(3));
      assertEquals("", this.jsonarray.optString(4));
   }

   public void testOptJSONObject() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         assertEquals("{\"abc\":\"123\"}", this.jsonarray.optJSONObject(0).toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOptJSONObject_NonJsonObject() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      assertEquals((Object)null, this.jsonarray.optJSONObject(0));
   }

   public void testOptJSONArray() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)(new JSONArray()).put((Object)"abc"));
      assertEquals("[\"abc\"]", this.jsonarray.optJSONArray(0).toString());
   }

   public void testOptJSONArray_NonJsonArray() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Object)"123");
      assertEquals((Object)null, this.jsonarray.optJSONArray(0));
   }

   public void testIsNull() {
      this.jsonarray = new JSONArray();
      this.jsonarray.put(JSONObject.NULL);
      this.jsonarray.put((Object)"null");
      assertTrue(this.jsonarray.isNull(0));
      assertFalse(this.jsonarray.isNull(1));
   }

   public void testWriter() {
      try {
         StringWriter sw = new StringWriter();
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45);
         this.jsonarray.put(-98);
         this.jsonarray.put((Object)(new JSONArray()).put((Object)"abc"));
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)"123");
         this.jsonarray.write(sw);
         assertEquals("[\"123\",\"-12\",45,-98,[\"abc\"],\"-12\",\"abc\",\"123\",\"123\",{\"abc\":\"123\"},\"abc\",\"123\"]", sw.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testWriter_BadWriter() {
      try {
         class BadWriter extends Writer {
            public void write(char[] cbuf, int off, int len) throws IOException {
               throw new IOException("Test Message From Bad Writer");
            }

            public void flush() throws IOException {
            }

            public void close() throws IOException {
            }
         }

         BadWriter sw = new BadWriter();
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45);
         this.jsonarray.put(-98);
         this.jsonarray.put((Object)(new JSONArray()).put((Object)"abc"));
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonarray.put((Object)"abc");
         this.jsonarray.put((Object)"123");
         this.jsonarray.write(sw);
      } catch (JSONException var2) {
         assertEquals("Test Message From Bad Writer", var2.getMessage());
      }

   }

   public void testPut_ObjectAndSpecificIndex() {
      try {
         TestJSONArray.testObject a = new TestJSONArray.testObject();
         TestJSONArray.testObject b = new TestJSONArray.testObject();
         TestJSONArray.testObject c = new TestJSONArray.testObject();
         TestJSONArray.testObject d = new TestJSONArray.testObject();
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, (Object)a);
         this.jsonarray.put(1, (Object)b);
         assertEquals(a, this.jsonarray.get(0));
         assertEquals(b, this.jsonarray.get(1));
         this.jsonarray.put(0, (Object)c);
         assertEquals(c, this.jsonarray.get(0));
         assertEquals(b, this.jsonarray.get(1));
         this.jsonarray.put(8, (Object)d);
         assertEquals(d, this.jsonarray.get(8));
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var5) {
         fail(var5.getMessage());
      }

   }

   public void testPut_ObjectAndNegativeIndex() {
      try {
         TestJSONArray.testObject a = new TestJSONArray.testObject();
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, (Object)a);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testToJSONObject() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.put((Object)"-12");
         this.jsonarray.put(45);
         this.jsonarray.put(-98);
         this.jsonarray.put((Object)(new JSONArray()).put((Object)"abc"));
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         JSONArray names = new JSONArray(new String[]{"bdd", "fdsa", "fds", "ewre", "rer", "gfs"});
         assertEquals("{\"gfs\":{\"abc\":\"123\"},\"fdsa\":\"-12\",\"bdd\":\"123\",\"ewre\":-98,\"rer\":[\"abc\"],\"fds\":45}", this.jsonarray.toJSONObject(names).toString());
         assertEquals((Object)null, this.jsonarray.toJSONObject(new JSONArray()));
         assertEquals((Object)null, this.jsonarray.toJSONObject((JSONArray)null));
         assertEquals((Object)null, (new JSONArray()).toJSONObject(names));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJSONObject() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)(new JSONObject()).put("abc", (Object)"123"));
         assertEquals("{\"abc\":\"123\"}", this.jsonarray.getJSONObject(0).toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJSONObject_NonJsonObject() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.getJSONObject(0);
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a JSONObject.", var2.getMessage());
      }

   }

   public void testGetJSONArray() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)(new JSONArray()).put((Object)"abc"));
         assertEquals("[\"abc\"]", this.jsonarray.getJSONArray(0).toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJSONArray_NonJsonArray() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"123");
         this.jsonarray.getJSONArray(0);
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONArray[0] is not a JSONArray.", var2.getMessage());
      }

   }

   public void testPut_Map() {
      Map map = new HashMap();
      map.put("abc", "123");
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Map)map);
      assertEquals("[{\"abc\":\"123\"}]", this.jsonarray.toString());
   }

   public void testConstructor_BadJsonArray() {
      try {
         this.jsonarray = new JSONArray("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("A JSONArray text must start with '[' at 1 [character 2 line 1]", var2.getMessage());
      }

   }

   public void testConstructor() {
      try {
         this.jsonarray = new JSONArray("[]");
         assertEquals("[]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[\"abc\"]");
         assertEquals("[\"abc\"]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[\"abc\",\"123\"]");
         assertEquals("[\"abc\",\"123\"]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[123,{}]");
         assertEquals("[123,{}]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[123,,{}]");
         assertEquals("[123,null,{}]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[123,,{},]");
         assertEquals("[123,null,{}]", this.jsonarray.toString());
         this.jsonarray = new JSONArray("[123,,{};]");
         assertEquals("[123,null,{}]", this.jsonarray.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPut_Collection() {
      Collection stringCol = new Stack();
      stringCol.add("string1");
      stringCol.add("string2");
      stringCol.add("string3");
      stringCol.add("string4");
      this.jsonarray = new JSONArray();
      this.jsonarray.put((Collection)stringCol);
      assertEquals("[[\"string1\",\"string2\",\"string3\",\"string4\"]]", this.jsonarray.toString());
   }

   public void testPut_BooleanAndSpecificIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, true);
         this.jsonarray.put(1, true);
         assertEquals(true, this.jsonarray.get(0));
         assertEquals(true, this.jsonarray.get(1));
         this.jsonarray.put(0, false);
         assertEquals(false, this.jsonarray.get(0));
         assertEquals(true, this.jsonarray.get(1));
         this.jsonarray.put(8, false);
         assertEquals(false, this.jsonarray.get(8));
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPut_BooleanAndNegativeIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, true);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_CollectionAndSpecificIndex() {
      try {
         Collection a = new Stack();
         a.add("string1");
         a.add("string4");
         Collection b = new Stack();
         b.add("string2");
         b.add("string3");
         Collection c = new Stack();
         c.add("string3");
         c.add("string4");
         Collection d = new Stack();
         d.add("string1");
         d.add("string2");
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, (Collection)a);
         this.jsonarray.put(1, (Collection)b);
         assertEquals((new JSONArray(a)).toString(), this.jsonarray.get(0).toString());
         assertEquals((new JSONArray(b)).toString(), this.jsonarray.get(1).toString());
         this.jsonarray.put(0, (Collection)c);
         assertEquals((new JSONArray(c)).toString(), this.jsonarray.get(0).toString());
         assertEquals((new JSONArray(b)).toString(), this.jsonarray.get(1).toString());
         this.jsonarray.put(8, (Collection)d);
         assertEquals((new JSONArray(d)).toString(), this.jsonarray.get(8).toString());
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var5) {
         fail(var5.getMessage());
      }

   }

   public void testPut_CollectionAndNegativeIndex() {
      try {
         Collection a = new Stack();
         a.add("string1");
         a.add("string4");
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, (Collection)a);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_DoubleAndSpecificIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, 10.0D);
         this.jsonarray.put(1, 30.2D);
         assertEquals(10.0D, this.jsonarray.get(0));
         assertEquals(30.2D, this.jsonarray.get(1));
         this.jsonarray.put(0, 52.64D);
         assertEquals(52.64D, this.jsonarray.get(0));
         assertEquals(30.2D, this.jsonarray.get(1));
         this.jsonarray.put(8, 14.23D);
         assertEquals(14.23D, this.jsonarray.get(8));
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPut_DoubleAndNegativeIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, 30.65D);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_IntAndSpecificIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, 54);
         this.jsonarray.put(1, 82);
         assertEquals(Integer.valueOf(54), this.jsonarray.get(0));
         assertEquals(Integer.valueOf(82), this.jsonarray.get(1));
         this.jsonarray.put(0, 36);
         assertEquals(Integer.valueOf(36), this.jsonarray.get(0));
         assertEquals(Integer.valueOf(82), this.jsonarray.get(1));
         this.jsonarray.put(8, 67);
         assertEquals(Integer.valueOf(67), this.jsonarray.get(8));
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPut_IntAndNegativeIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, 3);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_LongAndSpecificIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, 54L);
         this.jsonarray.put(1, 456789123L);
         assertEquals(54L, this.jsonarray.get(0));
         assertEquals(456789123L, this.jsonarray.get(1));
         this.jsonarray.put(0, 72887L);
         assertEquals(72887L, this.jsonarray.get(0));
         assertEquals(456789123L, this.jsonarray.get(1));
         this.jsonarray.put(8, 39397L);
         assertEquals(39397L, this.jsonarray.get(8));
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPut_LongAndNegativeIndex() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, 456486794L);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testPut_MapAndSpecificIndex() {
      try {
         Map a = new HashMap();
         a.put("abc", "123");
         Map b = new HashMap();
         b.put("abffc", "1253");
         Map c = new HashMap();
         c.put("addbc", "145623");
         Map d = new HashMap();
         d.put("abffdc", "122623");
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, (Map)a);
         this.jsonarray.put(1, (Map)b);
         assertEquals((new JSONObject(a)).toString(), this.jsonarray.get(0).toString());
         assertEquals((new JSONObject(b)).toString(), this.jsonarray.get(1).toString());
         this.jsonarray.put(0, (Map)c);
         assertEquals((new JSONObject(c)).toString(), this.jsonarray.get(0).toString());
         assertEquals((new JSONObject(b)).toString(), this.jsonarray.get(1).toString());
         this.jsonarray.put(8, (Map)d);
         assertEquals((new JSONObject(d)).toString(), this.jsonarray.get(8).toString());
         assertEquals(JSONObject.NULL, this.jsonarray.get(2));
         assertEquals(JSONObject.NULL, this.jsonarray.get(3));
         assertEquals(JSONObject.NULL, this.jsonarray.get(4));
         assertEquals(JSONObject.NULL, this.jsonarray.get(5));
         assertEquals(JSONObject.NULL, this.jsonarray.get(6));
         assertEquals(JSONObject.NULL, this.jsonarray.get(7));
      } catch (JSONException var5) {
         fail(var5.getMessage());
      }

   }

   public void testPut_MapAndNegativeIndex() {
      try {
         Map a = new HashMap();
         a.put("abc", "123");
         this.jsonarray = new JSONArray();
         this.jsonarray.put(-1, (Map)a);
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("JSONArray[-1] not found.", var2.getMessage());
      }

   }

   public void testRemove() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put(0, 54);
         this.jsonarray.put(1, 456789123);
         this.jsonarray.put(2, 72887);
         this.jsonarray.remove(1);
         assertEquals(Integer.valueOf(54), this.jsonarray.get(0));
         assertEquals(72887, this.jsonarray.get(1));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public class testObject {
   }
}
