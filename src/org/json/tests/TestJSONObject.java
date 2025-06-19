package org.json.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javax.print.PrintException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.XML;

public class TestJSONObject extends TestCase {
   JSONObject jsonobject = new JSONObject();
   Iterator iterator;
   JSONArray jsonarray;
   Object object;
   String string;
   double eps = 2.220446049250313E-16D;

   public void testNull() throws Exception {
      this.jsonobject = new JSONObject("{\"message\":\"null\"}");
      assertFalse(this.jsonobject.isNull("message"));
      assertEquals("null", this.jsonobject.getString("message"));
      this.jsonobject = new JSONObject("{\"message\":null}");
      assertTrue(this.jsonobject.isNull("message"));
   }

   public void testConstructor_DuplicateKey() {
      try {
         this.string = "{\"koda\": true, \"koda\": true}";
         this.jsonobject = new JSONObject(this.string);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Duplicate key \"koda\"", var2.getMessage());
      }

   }

   public void testConstructor_NullKey() {
      try {
         this.jsonobject.put((String)null, (Object)"howard");
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Null key.", var2.getMessage());
      }

   }

   public void testGetDouble_InvalidKeyHoward() {
      try {
         this.jsonobject.getDouble("howard");
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"howard\"] not found.", var2.getMessage());
      }

   }

   public void testGetDouble_InvalidKeyStooge() {
      this.jsonobject = new JSONObject();

      try {
         this.jsonobject.getDouble("stooge");
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"stooge\"] not found.", var2.getMessage());
      }

   }

   public void testIsNull() {
      try {
         this.jsonobject = new JSONObject();
         this.object = null;
         this.jsonobject.put("booga", this.object);
         this.jsonobject.put("wooga", JSONObject.NULL);
         assertEquals("{\"wooga\":null}", this.jsonobject.toString());
         assertTrue(this.jsonobject.isNull("booga"));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testIncrement_NewKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.increment("two");
         this.jsonobject.increment("two");
         assertEquals("{\"two\":2}", this.jsonobject.toString());
         assertEquals(2, this.jsonobject.getInt("two"));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testToString_ListofLists() {
      try {
         this.string = "{     \"list of lists\" : [         [1, 2, 3],         [4, 5, 6],     ] }";
         this.jsonobject = new JSONObject(this.string);
         assertEquals("{\"list of lists\": [\n    [\n        1,\n        2,\n        3\n    ],\n    [\n        4,\n        5,\n        6\n    ]\n]}", this.jsonobject.toString(4));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testToString_Indentation() {
      try {
         this.string = "{ \"entity\": { \"imageURL\": \"\", \"name\": \"IXXXXXXXXXXXXX\", \"id\": 12336, \"ratingCount\": null, \"averageRating\": null } }";
         this.jsonobject = new JSONObject(this.string);
         assertEquals("{\"entity\": {\n  \"id\": 12336,\n  \"averageRating\": null,\n  \"ratingCount\": null,\n  \"name\": \"IXXXXXXXXXXXXX\",\n  \"imageURL\": \"\"\n}}", this.jsonobject.toString(2));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testMultipleThings() {
      try {
         this.jsonobject = new JSONObject("{foo: [true, false,9876543210,    0.0, 1.00000001,  1.000000000001, 1.00000000000000001, .00000000000000001, 2.00, 0.1, 2e100, -32,[],{}, \"string\"],   to   : null, op : 'Good',ten:10} postfix comment");
         this.jsonobject.put("String", (Object)"98.6");
         this.jsonobject.put("JSONObject", (Object)(new JSONObject()));
         this.jsonobject.put("JSONArray", (Object)(new JSONArray()));
         this.jsonobject.put("int", 57);
         this.jsonobject.put("double", 1.2345678901234568E29D);
         this.jsonobject.put("true", true);
         this.jsonobject.put("false", false);
         this.jsonobject.put("null", JSONObject.NULL);
         this.jsonobject.put("bool", (Object)"true");
         this.jsonobject.put("zero", -0.0D);
         this.jsonobject.put("\\u2028", (Object)"\u2028");
         this.jsonobject.put("\\u2029", (Object)"\u2029");
         this.jsonarray = this.jsonobject.getJSONArray("foo");
         this.jsonarray.put(666);
         this.jsonarray.put(2001.99D);
         this.jsonarray.put((Object)"so \"fine\".");
         this.jsonarray.put((Object)"so <fine>.");
         this.jsonarray.put(true);
         this.jsonarray.put(false);
         this.jsonarray.put((Object)(new JSONArray()));
         this.jsonarray.put((Object)(new JSONObject()));
         this.jsonobject.put("keys", (Object)JSONObject.getNames(this.jsonobject));
         assertEquals("{\n    \"to\": null,\n    \"ten\": 10,\n    \"JSONObject\": {},\n    \"JSONArray\": [],\n    \"op\": \"Good\",\n    \"keys\": [\n        \"to\",\n        \"ten\",\n        \"JSONObject\",\n        \"JSONArray\",\n        \"op\",\n        \"int\",\n        \"true\",\n        \"foo\",\n        \"zero\",\n        \"double\",\n        \"String\",\n        \"false\",\n        \"bool\",\n        \"\\\\u2028\",\n        \"\\\\u2029\",\n        \"null\"\n    ],\n    \"int\": 57,\n    \"true\": true,\n    \"foo\": [\n        true,\n        false,\n        9876543210,\n        0,\n        1.00000001,\n        1.000000000001,\n        1,\n        1.0E-17,\n        2,\n        0.1,\n        2.0E100,\n        -32,\n        [],\n        {},\n        \"string\",\n        666,\n        2001.99,\n        \"so \\\"fine\\\".\",\n        \"so <fine>.\",\n        true,\n        false,\n        [],\n        {}\n    ],\n    \"zero\": -0,\n    \"double\": 1.2345678901234568E29,\n    \"String\": \"98.6\",\n    \"false\": false,\n    \"bool\": \"true\",\n    \"\\\\u2028\": \"\\u2028\",\n    \"\\\\u2029\": \"\\u2029\",\n    \"null\": null\n}", this.jsonobject.toString(4));
         assertEquals("<to>null</to><ten>10</ten><JSONObject></JSONObject><op>Good</op><keys>to</keys><keys>ten</keys><keys>JSONObject</keys><keys>JSONArray</keys><keys>op</keys><keys>int</keys><keys>true</keys><keys>foo</keys><keys>zero</keys><keys>double</keys><keys>String</keys><keys>false</keys><keys>bool</keys><keys>\\u2028</keys><keys>\\u2029</keys><keys>null</keys><int>57</int><true>true</true><foo>true</foo><foo>false</foo><foo>9876543210</foo><foo>0.0</foo><foo>1.00000001</foo><foo>1.000000000001</foo><foo>1.0</foo><foo>1.0E-17</foo><foo>2.0</foo><foo>0.1</foo><foo>2.0E100</foo><foo>-32</foo><foo></foo><foo></foo><foo>string</foo><foo>666</foo><foo>2001.99</foo><foo>so &quot;fine&quot;.</foo><foo>so &lt;fine&gt;.</foo><foo>true</foo><foo>false</foo><foo></foo><foo></foo><zero>-0.0</zero><double>1.2345678901234568E29</double><String>98.6</String><false>false</false><bool>true</bool><\\u2028>\u2028</\\u2028><\\u2029>\u2029</\\u2029><null>null</null>", XML.toString(this.jsonobject));
         assertEquals(98.6D, this.jsonobject.getDouble("String"), this.eps);
         assertTrue(this.jsonobject.getBoolean("bool"));
         assertEquals("[true,false,9876543210,0,1.00000001,1.000000000001,1,1.0E-17,2,0.1,2.0E100,-32,[],{},\"string\",666,2001.99,\"so \\\"fine\\\".\",\"so <fine>.\",true,false,[],{}]", this.jsonobject.getJSONArray("foo").toString());
         assertEquals("Good", this.jsonobject.getString("op"));
         assertEquals(10, this.jsonobject.getInt("ten"));
         assertFalse(this.jsonobject.optBoolean("oops"));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testMultipleThings2() {
      try {
         this.jsonobject = new JSONObject("{string: \"98.6\", long: 2147483648, int: 2147483647, longer: 9223372036854775807, double: 9223372036854775808}");
         assertEquals("{\n \"int\": 2147483647,\n \"string\": \"98.6\",\n \"longer\": 9223372036854775807,\n \"double\": \"9223372036854775808\",\n \"long\": 2147483648\n}", this.jsonobject.toString(1));
         assertEquals(Integer.MAX_VALUE, this.jsonobject.getInt("int"));
         assertEquals(Integer.MIN_VALUE, this.jsonobject.getInt("long"));
         assertEquals(-1, this.jsonobject.getInt("longer"));

         try {
            this.jsonobject.getInt("double");
            fail("should fail with - JSONObject[\"double\"] is not an int.");
         } catch (JSONException var5) {
            assertEquals("JSONObject[\"double\"] is not an int.", var5.getMessage());
         }

         try {
            this.jsonobject.getInt("string");
            fail("should fail with - JSONObject[\"string\"] is not an int.");
         } catch (JSONException var4) {
            assertEquals("JSONObject[\"string\"] is not an int.", var4.getMessage());
         }

         assertEquals(2147483647L, this.jsonobject.getLong("int"));
         assertEquals(2147483648L, this.jsonobject.getLong("long"));
         assertEquals(Long.MAX_VALUE, this.jsonobject.getLong("longer"));

         try {
            this.jsonobject.getLong("double");
            fail("should fail with - JSONObject[\"double\"] is not a long.");
         } catch (JSONException var3) {
            assertEquals("JSONObject[\"double\"] is not a long.", var3.getMessage());
         }

         try {
            this.jsonobject.getLong("string");
            fail("should fail with - JSONObject[\"string\"] is not a long.");
         } catch (JSONException var2) {
            assertEquals("JSONObject[\"string\"] is not a long.", var2.getMessage());
         }

         assertEquals(2.147483647E9D, this.jsonobject.getDouble("int"), this.eps);
         assertEquals(2.147483648E9D, this.jsonobject.getDouble("long"), this.eps);
         assertEquals(9.223372036854776E18D, this.jsonobject.getDouble("longer"), this.eps);
         assertEquals(9.223372036854776E18D, this.jsonobject.getDouble("double"), this.eps);
         assertEquals(98.6D, this.jsonobject.getDouble("string"), this.eps);
         this.jsonobject.put("good sized", Long.MAX_VALUE);
         assertEquals("{\n \"int\": 2147483647,\n \"string\": \"98.6\",\n \"longer\": 9223372036854775807,\n \"good sized\": 9223372036854775807,\n \"double\": \"9223372036854775808\",\n \"long\": 2147483648\n}", this.jsonobject.toString(1));
         this.jsonarray = new JSONArray("[2147483647, 2147483648, 9223372036854775807, 9223372036854775808]");
         assertEquals("[\n 2147483647,\n 2147483648,\n 9223372036854775807,\n \"9223372036854775808\"\n]", this.jsonarray.toString(1));
         List expectedKeys = new ArrayList(6);
         expectedKeys.add("int");
         expectedKeys.add("string");
         expectedKeys.add("longer");
         expectedKeys.add("good sized");
         expectedKeys.add("double");
         expectedKeys.add("long");
         this.iterator = this.jsonobject.keys();

         while(this.iterator.hasNext()) {
            this.string = (String)this.iterator.next();
            assertTrue(expectedKeys.remove(this.string));
         }

         assertEquals(0, expectedKeys.size());
      } catch (JSONException var6) {
         fail(var6.toString());
      }

   }

   public void testPut_CollectionAndMap() {
      try {
         this.string = "{plist=Apple; AnimalSmells = { pig = piggish; lamb = lambish; worm = wormy; }; AnimalSounds = { pig = oink; lamb = baa; worm = baa;  Lisa = \"Why is the worm talking like a lamb?\" } ; AnimalColors = { pig = pink; lamb = black; worm = pink; } } ";
         this.jsonobject = new JSONObject(this.string);
         assertEquals("{\"AnimalColors\":{\"worm\":\"pink\",\"lamb\":\"black\",\"pig\":\"pink\"},\"plist\":\"Apple\",\"AnimalSounds\":{\"worm\":\"baa\",\"Lisa\":\"Why is the worm talking like a lamb?\",\"lamb\":\"baa\",\"pig\":\"oink\"},\"AnimalSmells\":{\"worm\":\"wormy\",\"lamb\":\"lambish\",\"pig\":\"piggish\"}}", this.jsonobject.toString());
         Collection collection = null;
         Map map = null;
         this.jsonobject = new JSONObject((Map)map);
         this.jsonarray = new JSONArray((Collection)collection);
         this.jsonobject.append("stooge", "Joe DeRita");
         this.jsonobject.append("stooge", "Shemp");
         this.jsonobject.accumulate("stooges", "Curly");
         this.jsonobject.accumulate("stooges", "Larry");
         this.jsonobject.accumulate("stooges", "Moe");
         this.jsonobject.accumulate("stoogearray", this.jsonobject.get("stooges"));
         this.jsonobject.put("map", (Map)map);
         this.jsonobject.put("collection", (Collection)collection);
         this.jsonobject.put("array", (Object)this.jsonarray);
         this.jsonarray.put((Map)map);
         this.jsonarray.put((Collection)collection);
         assertEquals("{\"stooge\":[\"Joe DeRita\",\"Shemp\"],\"map\":{},\"stooges\":[\"Curly\",\"Larry\",\"Moe\"],\"collection\":[],\"stoogearray\":[[\"Curly\",\"Larry\",\"Moe\"]],\"array\":[{},[]]}", this.jsonobject.toString());
      } catch (JSONException var3) {
         fail(var3.getMessage());
      }

   }

   public void testAccumulate() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.accumulate("stooge", "Curly");
         this.jsonobject.accumulate("stooge", "Larry");
         this.jsonobject.accumulate("stooge", "Moe");
         this.jsonarray = this.jsonobject.getJSONArray("stooge");
         this.jsonarray.put(5, (Object)"Shemp");
         assertEquals("{\"stooge\": [\n    \"Curly\",\n    \"Larry\",\n    \"Moe\",\n    null,\n    null,\n    \"Shemp\"\n]}", this.jsonobject.toString(4));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testWrite() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.accumulate("stooge", "Curly");
         this.jsonobject.accumulate("stooge", "Larry");
         this.jsonobject.accumulate("stooge", "Moe");
         this.jsonarray = this.jsonobject.getJSONArray("stooge");
         this.jsonarray.put(5, (Object)"Shemp");
         assertEquals("{\"stooge\":[\"Curly\",\"Larry\",\"Moe\",null,null,\"Shemp\"]}", this.jsonobject.write(new StringWriter()).toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_Html() {
      try {
         this.jsonobject = new JSONObject("{script: 'It is not allowed in HTML to send a close script tag in a string<script>because it confuses browsers</script>so we insert a backslash before the /'}");
         assertEquals("{\"script\":\"It is not allowed in HTML to send a close script tag in a string<script>because it confuses browsers<\\/script>so we insert a backslash before the /\"}", this.jsonobject.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_MultipleTestCases() {
      try {
         this.jsonobject = new JSONObject("{ fun => with non-standard forms ; forgiving => This package can be used to parse formats that are similar to but not stricting conforming to JSON; why=To make it easier to migrate existing data to JSON,one = [[1.00]]; uno=[[{1=>1}]];'+':+6e66 ;pluses=+++;empty = '' , 'double':0.666,true: TRUE, false: FALSE, null=NULL;[true] = [[!,@;*]]; string=>  o. k. ; \r oct=0666; hex=0x666; dec=666; o=0999; noh=0x0x}");
         assertEquals("{\n \"noh\": \"0x0x\",\n \"one\": [[1]],\n \"o\": 999,\n \"+\": 6.0E66,\n \"true\": true,\n \"forgiving\": \"This package can be used to parse formats that are similar to but not stricting conforming to JSON\",\n \"fun\": \"with non-standard forms\",\n \"double\": 0.666,\n \"uno\": [[{\"1\": 1}]],\n \"dec\": 666,\n \"oct\": 666,\n \"hex\": \"0x666\",\n \"string\": \"o. k.\",\n \"empty\": \"\",\n \"false\": false,\n \"[true]\": [[\n  \"!\",\n  \"@\",\n  \"*\"\n ]],\n \"pluses\": \"+++\",\n \"why\": \"To make it easier to migrate existing data to JSON\",\n \"null\": null\n}", this.jsonobject.toString(1));
         assertTrue(this.jsonobject.getBoolean("true"));
         assertFalse(this.jsonobject.getBoolean("false"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testConstructor_PriativeClass() {
      this.jsonobject = new JSONObject(new TestJSONObject.ObjectWithPrimatives());
      assertEquals("{\"l\":5748548957230984584,\"m\":true,\"big\":false,\"j\":\"3\",\"k\":10.03,\"ZERO\":0,\"i\":3}", this.jsonobject.toString());
   }

   public void testConstructor_SubClass() {
      TestJSONObject.ObjectWithPrimatives ob = new TestJSONObject.ObjectWithPrimativesExtension();
      this.jsonobject = new JSONObject(ob);
      assertEquals("{\"l\":5748548957230984584,\"m\":true,\"big\":false,\"j\":\"3\",\"k\":10.03,\"ZERO\":0,\"i\":3}", this.jsonobject.toString());
   }

   public void testConstructor_PrivateClass() {
      class PrivateObject {
         private int i = 3;

         public int getI() {
            return this.i;
         }
      }

      this.jsonobject = new JSONObject(new PrivateObject());
      assertEquals("{}", this.jsonobject.toString());
   }

   public void testConstructor_ArrayList() {
      List ar = new ArrayList();
      ar.add("test1");
      ar.add("test2");
      this.jsonobject = new JSONObject(ar);
      assertEquals("{\"empty\":false}", this.jsonobject.toString());
   }

   public void testConstructor_ClassClass() {
      try {
         this.jsonobject = new JSONObject(this.getClass());
         assertEquals("class junit.framework.TestCase", this.jsonobject.get("genericSuperclass").toString());
      } catch (Exception var2) {
         fail(var2.getMessage());
      }

   }

   public static void testConstructor_FrenchResourceBundle() {
      try {
         Locale currentLocale = new Locale("fr", "CA", "UNIX");
         assertEquals("{\"ASCII\":\"Number that represent chraracters\",\"JSON\":\"What are we testing?\",\"JAVA\":\"The language you are running to see this\"}", (new JSONObject("org.json.tests.SampleResourceBundle", currentLocale)).toString());
      } catch (JSONException var1) {
         fail(var1.getMessage());
      }

   }

   public static void testConstructor_UsResourceBundle() {
      try {
         Locale currentLocale = new Locale("en");
         assertEquals("{\"ASCII\":\"American Standard Code for Information Interchange\",\"JSON\":\"JavaScript Object Notation\",\"JAVA\":{\"desc\":\"Just Another Vague Acronym\",\"data\":\"Sweet language\"}}", (new JSONObject("org.json.tests.SampleResourceBundle", currentLocale)).toString());
      } catch (JSONException var1) {
         fail(var1.getMessage());
      }

   }

   public void testConstructor_ObjectWithStringArray() {
      assertEquals("{\"m\":true,\"i\":3}", (new JSONObject(new TestJSONObject.ObjectWithPrimatives(), new String[]{"i", "m", "k"})).toString());
   }

   public void testOpt() {
      try {
         this.jsonobject = new JSONObject("{\"a\":2}");
         assertEquals(Integer.valueOf(2), this.jsonobject.opt("a"));
         assertEquals((Object)null, this.jsonobject.opt("b"));
         assertEquals((Object)null, this.jsonobject.opt((String)null));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public static void testStringToValue() {
      assertEquals("", JSONObject.stringToValue(""));
      assertEquals(true, JSONObject.stringToValue("true"));
      assertEquals(false, JSONObject.stringToValue("false"));
      assertEquals(JSONObject.NULL, JSONObject.stringToValue("null"));
      assertEquals(Integer.valueOf(10), JSONObject.stringToValue("10"));
      assertEquals(10000.0D, JSONObject.stringToValue("10e3"));
      assertEquals(10000.0D, JSONObject.stringToValue("10E3"));
      assertEquals("10E3000000000", JSONObject.stringToValue("10E3000000000"));
   }

   public static void testQuote() {
      assertEquals("\"\"", JSONObject.quote(""));
      assertEquals("\"\"", JSONObject.quote((String)null));
      assertEquals("\"true\"", JSONObject.quote("true"));
      assertEquals("\"10\"", JSONObject.quote("10"));
      assertEquals("\"\\b\\t\\n\\f\\r\"", JSONObject.quote("\b\t\n\f\r"));
      assertEquals("\"\\u0012\\u0085\\u2086⊆\"", JSONObject.quote("\u0012\u0085₆⊆"));
   }

   public void testGetNames() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("a", (Object)"123");
         this.jsonobject.put("b", (Object)"123");
         this.jsonobject.put("c", (Object)"123");
         String[] names = JSONObject.getNames(this.jsonobject);
         assertEquals(3, names.length);
         assertEquals("b", names[0]);
         assertEquals("c", names[1]);
         assertEquals("a", names[2]);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetNames_EmptyJsonObject() {
      this.jsonobject = new JSONObject();
      assertEquals((Object)null, JSONObject.getNames(this.jsonobject));
   }

   public void testGetNames_ObjectWithPrimatives() {
      String[] names = JSONObject.getNames((Object)(new TestJSONObject.ObjectWithPrimatives()));
      assertEquals(2, names.length);
      assertEquals("i", names[0]);
      assertEquals("m", names[1]);
   }

   public static void testGetNames_EmptyObject() {
      class EmptyObject {
      }

      assertEquals((Object)null, JSONObject.getNames((Object)(new EmptyObject())));
   }

   public static void testGetNames_Null() {
      TestJSONObject.ObjectWithPrimatives owp = null;
      assertEquals((Object)null, JSONObject.getNames(owp));
   }

   public void testGetLong_Long() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"98765432");
         this.jsonobject.put("123", 98765432);
         assertEquals(98765432L, this.jsonobject.getLong("abc"));
         assertEquals(98765432L, this.jsonobject.getLong("123"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJsonObject_JsonObject() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)(new JSONObject()));
         assertEquals("{}", this.jsonobject.getJSONObject("abc").toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJsonObject_Int() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", 45);
         this.jsonobject.getJSONObject("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not a JSONObject.", var2.getMessage());
      }

   }

   public void testGetJsonObject_InvalidKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.getJSONObject("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGetJsonArray_JsonArray() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)(new JSONArray()));
         assertEquals("[]", this.jsonobject.getJSONArray("abc").toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetJsonArray_Int() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", 45);
         this.jsonobject.getJSONArray("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not a JSONArray.", var2.getMessage());
      }

   }

   public void testGetJsonArray_InvalidKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.getJSONArray("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGetInt_Int() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", 45);
         assertEquals(45, this.jsonobject.getInt("abc"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetInt_IntString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"45");
         assertEquals(45, this.jsonobject.getInt("abc"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetInt_LetterString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"def");
         this.jsonobject.getInt("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not an int.", var2.getMessage());
      }

   }

   public void testGetInt_InvalidKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.getInt("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGetDouble_Double() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", 45.1D);
         assertEquals(45.1D, this.jsonobject.getDouble("abc"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetDouble_DoubleString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"45.20");
         assertEquals(45.2D, this.jsonobject.getDouble("abc"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetDouble_LetterString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"def");
         this.jsonobject.getDouble("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not a number.", var2.getMessage());
      }

   }

   public void testGetDouble_InvalidKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.getDouble("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGetBoolean_Boolean() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", true);
         this.jsonobject.put("123", false);
         assertTrue(this.jsonobject.getBoolean("abc"));
         assertFalse(this.jsonobject.getBoolean("123"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean_BooleanString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"true");
         this.jsonobject.put("123", (Object)"false");
         this.jsonobject.put("ABC", (Object)"TRUE");
         this.jsonobject.put("456", (Object)"FALSE");
         assertTrue(this.jsonobject.getBoolean("abc"));
         assertFalse(this.jsonobject.getBoolean("123"));
         assertTrue(this.jsonobject.getBoolean("ABC"));
         assertFalse(this.jsonobject.getBoolean("456"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean_LetterString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"def");
         this.jsonobject.getBoolean("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not a Boolean.", var2.getMessage());
      }

   }

   public void testGetBoolean_Int() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", 45);
         this.jsonobject.getBoolean("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] is not a Boolean.", var2.getMessage());
      }

   }

   public void testGetBoolean_InvalidKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.getBoolean("abc");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGet_NullKey() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.get((String)null);
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Null key.", var2.getMessage());
      }

   }

   public void testToString_Indents() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("1235", (Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonobject.put("1234", (Object)"abc");
         this.jsonobject.put("1239", (Object)(new JSONObject()).put("1fd23", (Object)(new JSONObject())).put("12gfdgfg3", (Object)(new JSONObject()).put("f123", (Object)"123").put("12fgfg3", (Object)"abc")));
         assertEquals("{\n    \"1234\": \"abc\",\n    \"1235\": {\"abc\": \"123\"},\n    \"123\": \"123\",\n    \"1239\": {\n        \"1fd23\": {},\n        \"12gfdgfg3\": {\n            \"f123\": \"123\",\n            \"12fgfg3\": \"abc\"\n        }\n    }\n}", this.jsonobject.toString(4));
         assertEquals("{\"1gg23\": \"123\"}", (new JSONObject()).put("1gg23", (Object)"123").toString(4));
         assertEquals("{}", (new JSONObject()).toString(4));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_Exception() {
      try {
         this.jsonobject.put("abc", (Object)(new TestJSONObject.BadJsonString()));
         assertEquals((String)null, this.jsonobject.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public static void testNumberToString() {
      try {
         assertEquals("30.7", JSONObject.numberToString(30.7D));
         assertEquals("3.0E71", JSONObject.numberToString(3.0E71D));
      } catch (JSONException var1) {
         fail(var1.getMessage());
      }

   }

   public static void testNumberToString_Null() {
      try {
         JSONObject.numberToString((Number)null);
         fail("Should have thrown exception.");
      } catch (JSONException var1) {
         assertEquals("Null pointer", var1.getMessage());
      }

   }

   public void testWrap() {
      try {
         assertEquals(true, JSONObject.wrap(true));
         assertEquals(Integer.valueOf(35), JSONObject.wrap(Integer.valueOf(35)));
         assertEquals(35.5D, JSONObject.wrap(35.5D));
         assertEquals(56456456L, JSONObject.wrap(56456456L));
         assertEquals(JSONObject.NULL, JSONObject.wrap((Object)null));
         assertEquals(JSONObject.NULL, JSONObject.wrap(JSONObject.NULL));
         TestJSONObject.BadJsonString a = new TestJSONObject.BadJsonString();
         assertEquals(a, JSONObject.wrap(a));
         TestJSONObject.NullJsonString q = new TestJSONObject.NullJsonString();
         assertEquals(q, JSONObject.wrap(q));
         assertEquals(Short.valueOf((short)12), JSONObject.wrap(Short.valueOf((short)12)));
         assertEquals('a', JSONObject.wrap('a'));
         assertEquals(15, JSONObject.wrap(15));
         assertEquals(15.2F, JSONObject.wrap(15.2F));
         JSONObject b = (new JSONObject()).put("abc", (Object)"123");
         assertEquals(b, JSONObject.wrap(b));
         JSONArray c = (new JSONArray()).put((Object)"abc");
         assertEquals(c, JSONObject.wrap(c));
         Collection stringCol = new Stack();
         stringCol.add("string1");
         assertEquals("[\"string1\"]", JSONObject.wrap(stringCol).toString());
         assertEquals("[\"abc\",\"123\"]", JSONObject.wrap(new String[]{"abc", "123"}).toString());
         Map map = new HashMap();
         map.put("abc", "123");
         assertEquals("{\"abc\":\"123\"}", JSONObject.wrap(map).toString());
         assertEquals("javax.print.PrintException", JSONObject.wrap(new PrintException()));
         Class d = this.getClass();
         assertEquals("class org.json.tests.TestJSONObject", JSONObject.wrap(d));
         class testClass {
         }

         assertEquals("{}", JSONObject.wrap(new testClass()).toString());

         try {
            class BadCollection implements Collection {
               public int size() {
                  return 0;
               }

               public boolean isEmpty() {
                  return false;
               }

               public boolean contains(Object o) {
                  return false;
               }

               public Iterator iterator() {
                  Iterator[] i = new Iterator[0];
                  return i[1];
               }

               public Object[] toArray() {
                  return null;
               }

               public Object[] toArray(Object[] Type) {
                  return null;
               }

               public boolean add(Object e) {
                  return false;
               }

               public boolean remove(Object o) {
                  return false;
               }

               public boolean containsAll(Collection collection) {
                  return false;
               }

               public boolean addAll(Collection collection) {
                  return false;
               }

               public boolean removeAll(Collection collection) {
                  return false;
               }

               public boolean retainAll(Collection collection) {
                  return false;
               }

               public void clear() {
               }
            }

            JSONObject.wrap(new BadCollection()).toString();
            fail("Should have thrown exception.");
         } catch (Exception var9) {
            assertEquals((String)null, var9.getMessage());
         }
      } catch (JSONException var10) {
         fail(var10.getMessage());
      }

   }

   public void testWriter() {
      try {
         StringWriter sw = new StringWriter();
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1ghr23", (Object)"123");
         this.jsonobject.put("1ss23", (Object)"-12");
         this.jsonobject.put("1tr23", 45);
         this.jsonobject.put("1trt23", -98);
         this.jsonobject.put("1hh23", (Object)(new JSONObject()).put("123", (Object)"abc"));
         this.jsonobject.put("1er23", (Object)"-12");
         this.jsonobject.put("1re23", (Object)"abc");
         this.jsonobject.put("1fde23", (Object)"123");
         this.jsonobject.put("1fd23", (Object)"123");
         this.jsonobject.put("1ffdsf23", (Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonobject.put("fd123", (Object)"abc");
         this.jsonobject.put("12fdsf3", (Object)"123");
         this.jsonobject.write(sw);
         assertEquals("{\"1tr23\":45,\"1ss23\":\"-12\",\"1fd23\":\"123\",\"1trt23\":-98,\"1ffdsf23\":{\"abc\":\"123\"},\"1ghr23\":\"123\",\"1fde23\":\"123\",\"fd123\":\"abc\",\"12fdsf3\":\"123\",\"1hh23\":{\"123\":\"abc\"},\"1re23\":\"abc\",\"1er23\":\"-12\"}", sw.toString());
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
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1ghr23", (Object)"123");
         this.jsonobject.put("1ss23", (Object)"-12");
         this.jsonobject.put("1tr23", 45);
         this.jsonobject.put("1trt23", -98);
         this.jsonobject.put("1hh23", (Object)(new JSONObject()).put("123", (Object)"abc"));
         this.jsonobject.put("1er23", (Object)"-12");
         this.jsonobject.put("1re23", (Object)"abc");
         this.jsonobject.put("1fde23", (Object)"123");
         this.jsonobject.put("1fd23", (Object)"123");
         this.jsonobject.put("1ffdsf23", (Object)(new JSONObject()).put("abc", (Object)"123"));
         this.jsonobject.put("fd123", (Object)"abc");
         this.jsonobject.put("12fdsf3", (Object)"123");
         this.jsonobject.write(sw);
      } catch (JSONException var2) {
         assertEquals("Test Message From Bad Writer", var2.getMessage());
      }

   }

   public void testIncrement() {
      try {
         Map map = new HashMap();
         map.put("1ghr23", 30.56D);
         map.put("1ss23", -12.22D);
         map.put("1tr23", Integer.valueOf(45));
         map.put("1trt23", Integer.valueOf(-98));
         map.put("1hh23", 12.6F);
         map.put("1er23", -456.255F);
         map.put("1re23", 5543L);
         map.put("1fde23", -46546546L);
         this.jsonobject = new JSONObject(map);
         this.jsonobject.increment("1ghr23");
         this.jsonobject.increment("1ss23");
         this.jsonobject.increment("1tr23");
         this.jsonobject.increment("1trt23");
         this.jsonobject.increment("1hh23");
         this.jsonobject.increment("1er23");
         this.jsonobject.increment("1re23");
         this.jsonobject.increment("1fde23");
         assertEquals(31.56D, this.jsonobject.get("1ghr23"));
         assertEquals(-11.22D, this.jsonobject.get("1ss23"));
         assertEquals(Integer.valueOf(46), this.jsonobject.get("1tr23"));
         assertEquals(Integer.valueOf(-97), this.jsonobject.get("1trt23"));
         assertEquals(13.600000381469727D, ((Double)this.jsonobject.get("1hh23")).doubleValue(), this.eps);
         assertEquals(-455.2550048828125D, ((Double)this.jsonobject.get("1er23")).doubleValue(), this.eps);
         assertEquals(5544L, this.jsonobject.get("1re23"));
         assertEquals(-46546545L, this.jsonobject.get("1fde23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testIncrement_String() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"abc");
         this.jsonobject.increment("123");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unable to increment [\"123\"].", var2.getMessage());
      }

   }

   public void testGet_InvalidIndex() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("554", (Object)"123");
         this.jsonobject.get("abc");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"abc\"] not found.", var2.getMessage());
      }

   }

   public void testGet_ValidIndex() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"123");
         assertEquals("123", this.jsonobject.get("abc"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1gg23", (Object)"true");
         this.jsonobject.put("1dd23", (Object)"false");
         this.jsonobject.put("1ff23", true);
         this.jsonobject.put("1rr23", false);
         this.jsonobject.put("1hh23", (Object)"TRUE");
         this.jsonobject.put("1hhhgf23", (Object)"FALSE");
         assertTrue(this.jsonobject.getBoolean("1gg23"));
         assertFalse(this.jsonobject.getBoolean("1dd23"));
         assertTrue(this.jsonobject.getBoolean("1ff23"));
         assertFalse(this.jsonobject.getBoolean("1rr23"));
         assertTrue(this.jsonobject.getBoolean("1hh23"));
         assertFalse(this.jsonobject.getBoolean("1hhhgf23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetBoolean_NonBoolean() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.getBoolean("123");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"123\"] is not a Boolean.", var2.getMessage());
      }

   }

   public void testOptBoolean() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1gg23", (Object)"true");
         this.jsonobject.put("1dd23", (Object)"false");
         this.jsonobject.put("1ff23", true);
         this.jsonobject.put("1rr23", false);
         this.jsonobject.put("1hh23", (Object)"TRUE");
         this.jsonobject.put("1hhhgf23", (Object)"FALSE");
         assertTrue(this.jsonobject.optBoolean("1gg23"));
         assertFalse(this.jsonobject.optBoolean("1dd23"));
         assertTrue(this.jsonobject.optBoolean("1ff23"));
         assertFalse(this.jsonobject.optBoolean("1rr23"));
         assertTrue(this.jsonobject.optBoolean("1hh23"));
         assertFalse(this.jsonobject.optBoolean("1hhhgf23"));
         assertFalse(this.jsonobject.optBoolean("chicken"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetInt() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1gg23", (Object)"123");
         this.jsonobject.put("1g23", (Object)"-12");
         this.jsonobject.put("123", 45);
         this.jsonobject.put("1rr23", -98);
         assertEquals(123, this.jsonobject.getInt("1gg23"));
         assertEquals(-12, this.jsonobject.getInt("1g23"));
         assertEquals(45, this.jsonobject.getInt("123"));
         assertEquals(-98, this.jsonobject.getInt("1rr23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetInt_NonInteger() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"abc");
         this.jsonobject.getInt("123");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"123\"] is not an int.", var2.getMessage());
      }

   }

   public void testOptInt() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("1gg23", (Object)"123");
         this.jsonobject.put("1g23", (Object)"-12");
         this.jsonobject.put("123", 45);
         this.jsonobject.put("1rr23", -98);
         assertEquals(123, this.jsonobject.optInt("1gg23"));
         assertEquals(-12, this.jsonobject.optInt("1g23"));
         assertEquals(45, this.jsonobject.optInt("123"));
         assertEquals(-98, this.jsonobject.optInt("1rr23"));
         assertEquals(0, this.jsonobject.optInt("catfish"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetDouble() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("1fd23", (Object)"-12");
         this.jsonobject.put("1gfd23", 45);
         this.jsonobject.put("1gg23", -98);
         this.jsonobject.put("1f23", (Object)"123.5");
         this.jsonobject.put("1ss23", (Object)"-12.87");
         this.jsonobject.put("1ew23", 45.22D);
         this.jsonobject.put("1tt23", -98.18D);
         assertEquals(123.0D, this.jsonobject.getDouble("123"));
         assertEquals(-12.0D, this.jsonobject.getDouble("1fd23"));
         assertEquals(45.0D, this.jsonobject.getDouble("1gfd23"));
         assertEquals(-98.0D, this.jsonobject.getDouble("1gg23"));
         assertEquals(123.5D, this.jsonobject.getDouble("1f23"));
         assertEquals(-12.87D, this.jsonobject.getDouble("1ss23"));
         assertEquals(45.22D, this.jsonobject.getDouble("1ew23"));
         assertEquals(-98.18D, this.jsonobject.getDouble("1tt23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetDouble_NonDouble() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"abc");
         this.jsonobject.getDouble("123");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"123\"] is not a number.", var2.getMessage());
      }

   }

   public void testOptDouble() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("1fd23", (Object)"-12");
         this.jsonobject.put("1gfd23", 45);
         this.jsonobject.put("1gg23", -98);
         this.jsonobject.put("1f23", (Object)"123.5");
         this.jsonobject.put("1ss23", (Object)"-12.87");
         this.jsonobject.put("1ew23", 45.22D);
         this.jsonobject.put("1tt23", -98.18D);
         assertEquals(123.0D, this.jsonobject.optDouble("123"));
         assertEquals(-12.0D, this.jsonobject.optDouble("1fd23"));
         assertEquals(45.0D, this.jsonobject.optDouble("1gfd23"));
         assertEquals(-98.0D, this.jsonobject.optDouble("1gg23"));
         assertEquals(123.5D, this.jsonobject.optDouble("1f23"));
         assertEquals(-12.87D, this.jsonobject.optDouble("1ss23"));
         assertEquals(45.22D, this.jsonobject.optDouble("1ew23"));
         assertEquals(-98.18D, this.jsonobject.optDouble("1tt23"));
         assertEquals(Double.NaN, this.jsonobject.optDouble("rabbit"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetLong() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("12gh3", (Object)"-12");
         this.jsonobject.put("1f23", 45L);
         this.jsonobject.put("1re23", -98L);
         assertEquals(123L, this.jsonobject.getLong("123"));
         assertEquals(-12L, this.jsonobject.getLong("12gh3"));
         assertEquals(45L, this.jsonobject.getLong("1f23"));
         assertEquals(-98L, this.jsonobject.getLong("1re23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetLong_NonLong() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"abc");
         this.jsonobject.getLong("123");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"123\"] is not a long.", var2.getMessage());
      }

   }

   public void testOptLong() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("12gh3", (Object)"-12");
         this.jsonobject.put("1f23", 45L);
         this.jsonobject.put("1re23", -98L);
         assertEquals(123L, this.jsonobject.optLong("123"));
         assertEquals(-12L, this.jsonobject.optLong("12gh3"));
         assertEquals(45L, this.jsonobject.optLong("1f23"));
         assertEquals(-98L, this.jsonobject.optLong("1re23"));
         assertEquals(0L, this.jsonobject.optLong("randomString"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("12gf3", (Object)"-12");
         this.jsonobject.put("1fg23", (Object)"abc");
         this.jsonobject.put("1d23", (Object)"123");
         assertEquals("123", this.jsonobject.getString("123"));
         assertEquals("-12", this.jsonobject.getString("12gf3"));
         assertEquals("abc", this.jsonobject.getString("1fg23"));
         assertEquals("123", this.jsonobject.getString("1d23"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testGetString_NonString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", 123);
         this.jsonobject.getString("123");
      } catch (JSONException var2) {
         assertEquals("JSONObject[\"123\"] not a string.", var2.getMessage());
      }

   }

   public void testOptString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         this.jsonobject.put("12gf3", (Object)"-12");
         this.jsonobject.put("1fg23", (Object)"abc");
         this.jsonobject.put("1d23", (Object)"123");
         assertEquals("123", this.jsonobject.optString("123"));
         assertEquals("-12", this.jsonobject.optString("12gf3"));
         assertEquals("abc", this.jsonobject.optString("1fg23"));
         assertEquals("123", this.jsonobject.optString("1d23"));
         assertEquals("", this.jsonobject.optString("pandora"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOptJSONObject_SimpleObject() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)(new JSONObject()).put("abc", (Object)"123"));
         assertEquals("{\"abc\":\"123\"}", this.jsonobject.optJSONObject("123").toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOptJSONObject_NonJsonObject() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         assertEquals((Object)null, this.jsonobject.optJSONObject("123"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOptJSONArray() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)(new JSONArray()).put((Object)"abc"));
         assertEquals("[\"abc\"]", this.jsonobject.optJSONArray("123").toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testOptJSONArray_NonJsonArray() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         assertEquals((Object)null, this.jsonobject.optJSONArray("123"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testHas() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("123", (Object)"123");
         assertTrue(this.jsonobject.has("123"));
         assertFalse(this.jsonobject.has("124"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testAppend() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("string", (Object)"123");
         this.jsonobject.put("jsonarray", (Object)(new JSONArray()).put((Object)"abc"));
         this.jsonobject.append("jsonarray", "123");
         this.jsonobject.append("george", "def");

         try {
            this.jsonobject.append("string", "abc");
            fail("Should have thrown exception.");
         } catch (JSONException var2) {
            assertEquals("JSONObject[string] is not a JSONArray.", var2.getMessage());
         }

         assertEquals("{\"george\":[\"def\"],\"string\":\"123\",\"jsonarray\":[\"abc\",\"123\"]}", this.jsonobject.toString());
      } catch (JSONException var3) {
         fail(var3.getMessage());
      }

   }

   public void testConstuctor_Strings() {
      try {
         this.jsonobject = new JSONObject("123");
         fail("Should have thrown exception.");
      } catch (JSONException var5) {
         assertEquals("A JSONObject text must begin with '{' at 1 [character 2 line 1]", var5.getMessage());
      }

      try {
         this.jsonobject = new JSONObject("{\"123\":34");
         fail("Should have thrown exception.");
      } catch (JSONException var4) {
         assertEquals("Expected a ',' or '}' at 10 [character 11 line 1]", var4.getMessage());
      }

      try {
         this.jsonobject = new JSONObject("{\"123\",34}");
         fail("Should have thrown exception.");
      } catch (JSONException var3) {
         assertEquals("Expected a ':' after a key at 7 [character 8 line 1]", var3.getMessage());
      }

      try {
         this.jsonobject = new JSONObject("{");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("A JSONObject text must end with '}' at 2 [character 3 line 1]", var2.getMessage());
      }

   }

   public static void testTestValidity() {
      try {
         JSONObject.testValidity((Object)null);
         JSONObject.testValidity(50.4D);
         JSONObject.testValidity(70.8D);
         JSONObject.testValidity(50.4F);
         JSONObject.testValidity(70.8F);

         try {
            JSONObject.testValidity(Double.NaN);
            fail("Should have throw exception.");
         } catch (JSONException var6) {
            assertEquals("JSON does not allow non-finite numbers.", var6.getMessage());
         }

         try {
            JSONObject.testValidity(Double.NEGATIVE_INFINITY);
            fail("Should have throw exception.");
         } catch (JSONException var5) {
            assertEquals("JSON does not allow non-finite numbers.", var5.getMessage());
         }

         try {
            JSONObject.testValidity(Double.POSITIVE_INFINITY);
            fail("Should have throw exception.");
         } catch (JSONException var4) {
            assertEquals("JSON does not allow non-finite numbers.", var4.getMessage());
         }

         try {
            JSONObject.testValidity(Float.NaN);
            fail("Should have throw exception.");
         } catch (JSONException var3) {
            assertEquals("JSON does not allow non-finite numbers.", var3.getMessage());
         }

         try {
            JSONObject.testValidity(Float.NEGATIVE_INFINITY);
            fail("Should have throw exception.");
         } catch (JSONException var2) {
            assertEquals("JSON does not allow non-finite numbers.", var2.getMessage());
         }

         try {
            JSONObject.testValidity(Float.POSITIVE_INFINITY);
            fail("Should have throw exception.");
         } catch (JSONException var1) {
            assertEquals("JSON does not allow non-finite numbers.", var1.getMessage());
         }
      } catch (JSONException var7) {
         fail(var7.getMessage());
      }

   }

   public void testNames() {
      try {
         this.jsonobject = new JSONObject();
         assertEquals((Object)null, this.jsonobject.names());
         this.jsonobject.put("abc", (Object)"123");
         assertEquals("[\"abc\"]", this.jsonobject.names().toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testConstructor_CopySubset() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"123");
         this.jsonobject.put("abcd", (Object)"1234");
         this.jsonobject.put("abcde", (Object)"12345");
         this.jsonobject.put("abcdef", (Object)"123456");
         assertEquals("{\"abc\":\"123\",\"abcde\":\"12345\"}", (new JSONObject(this.jsonobject, new String[]{"abc", "abc", "abcde"})).toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testPutOnce() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.putOnce("abc", "123").putOnce("abcd", "1234").putOnce((String)null, "12345").putOnce("abcdef", (Object)null);
         assertEquals("{\"abc\":\"123\",\"abcd\":\"1234\"}", this.jsonobject.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"123");
         this.jsonobject.put("abcd", (Object)"1234");
         this.jsonobject.put("abcde", (Object)"12345");
         this.jsonobject.put("abcdef", (Object)"123456");
         assertEquals("[\"123\",\"123\",\"12345\"]", this.jsonobject.toJSONArray(new JSONArray(new String[]{"abc", "abc", "abcde"})).toString());
         assertEquals((Object)null, this.jsonobject.toJSONArray(new JSONArray()));
         assertEquals((Object)null, this.jsonobject.toJSONArray((JSONArray)null));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testValueToString() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"123");
         this.jsonobject.put("abcd", (Object)"1234");
         TestJSONObject.BadJsonString bjs = new TestJSONObject.BadJsonString();
         this.jsonobject.put("cd", (Object)bjs);
         this.jsonobject.put("acd", (Object)(new TestJSONObject.GoodJsonString()));
         TestJSONObject.NullJsonString q = new TestJSONObject.NullJsonString();
         this.jsonobject.put("zzz", (Object)q);
         Map map = new HashMap();
         map.put("abc", "123");
         Collection stringCol = new Stack();
         stringCol.add("string1");
         this.jsonobject.put("de", (Map)map);
         this.jsonobject.put("e", (Collection)stringCol);
         this.jsonobject.put("abcde", (Object)(new JSONArray()).put((Object)"123"));
         this.jsonobject.put("abcdef", (Object)(new JSONObject()).put("123", (Object)"123456"));
         JSONObject nulljo = null;
         JSONArray nullja = null;
         this.jsonobject.put("bcde", nulljo);
         this.jsonobject.put("bcdef", nullja);
         assertEquals("{\n    \"de\": {\"abc\": \"123\"},\n    \"abc\": \"123\",\n    \"e\": [\"string1\"],\n    \"zzz\": \"" + q.toString() + "\",\n    \"abcdef\": {\"123\": \"123456\"},\n    \"abcde\": [\"123\"],\n    \"acd\": jsonstring,\n    \"abcd\": \"1234\",\n    \"cd\": \"" + bjs.toString() + "\"\n}", this.jsonobject.toString(4));
      } catch (JSONException var7) {
         fail(var7.getMessage());
      }

   }

   public void testValueToString_Object() {
      try {
         Map map = new HashMap();
         map.put("abcd", "1234");
         Collection stringCol = new Stack();
         stringCol.add("string1");
         this.jsonobject = new JSONObject();
         this.jsonobject.put("abc", (Object)"123");
         assertEquals("{\"abc\":\"123\"}", JSONObject.valueToString(this.jsonobject));
         assertEquals("{\"abcd\":\"1234\"}", JSONObject.valueToString(map));
         assertEquals("[\"string1\"]", JSONObject.valueToString(stringCol));

         try {
            JSONObject.valueToString(new TestJSONObject.BadJsonString());
            fail("Should have thrown exception.");
         } catch (JSONException var5) {
            assertEquals("1", var5.getMessage());
         }

         try {
            JSONObject.valueToString(new TestJSONObject.NullJsonString());
            fail("Should have thrown exception.");
         } catch (JSONException var4) {
            assertEquals("Bad value from toJSONString: null", var4.getMessage());
         }

         assertEquals("jsonstring", JSONObject.valueToString(new TestJSONObject.GoodJsonString()));
         assertEquals("null", JSONObject.valueToString((Object)null));
         assertEquals("null", JSONObject.valueToString(JSONObject.NULL));
         assertEquals("[\"abc\",\"123\"]", JSONObject.valueToString(new String[]{"abc", "123"}));
      } catch (JSONException var6) {
         fail(var6.getMessage());
      }

   }

   public static void testDoubleToString() {
      assertEquals("10.66", JSONObject.doubleToString(10.66D));
      assertEquals("10", JSONObject.doubleToString(10.0D));
      assertEquals("null", JSONObject.doubleToString(Double.NaN));
      assertEquals("null", JSONObject.doubleToString(Double.NEGATIVE_INFINITY));
      assertEquals("null", JSONObject.doubleToString(Double.POSITIVE_INFINITY));
      assertEquals("1.0E89", JSONObject.doubleToString(1.0E89D));
      assertEquals("1.0E89", JSONObject.doubleToString(1.0E89D));
   }

   public class BadJsonString implements JSONString {
      public String toJSONString() {
         String[] arString = new String[]{"abc"};
         return arString[1];
      }
   }

   public class GoodJsonString implements JSONString {
      public String toJSONString() {
         return "jsonstring";
      }
   }

   public class NullJsonString implements JSONString {
      public String toJSONString() {
         return null;
      }
   }

   public class ObjectWithPrimatives {
      public int i = 3;
      private String nullString = null;
      private String j = "3";
      private double k = 10.03D;
      private long l = 5748548957230984584L;
      public boolean m = true;

      public int getI() {
         return this.i;
      }

      public String getJ() {
         return this.j;
      }

      public double getK() {
         return this.k;
      }

      public long getL() {
         return this.l;
      }

      public boolean getM() {
         return this.m;
      }

      public boolean getM(Boolean test) {
         return this.m;
      }

      public String getNullString() {
         return this.nullString;
      }

      public int getZERO() {
         return 0;
      }

      public int getone() {
         return 1;
      }

      public boolean isBig() {
         return false;
      }

      private boolean isSmall() {
         return true;
      }
   }

   public class ObjectWithPrimativesExtension extends TestJSONObject.ObjectWithPrimatives {
      public ObjectWithPrimativesExtension() {
         super();
      }
   }
}
