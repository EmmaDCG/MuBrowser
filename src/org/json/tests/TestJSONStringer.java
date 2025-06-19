package org.json.tests;

import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

public class TestJSONStringer extends TestCase {
   JSONObject jsonobject;
   JSONStringer jsonstringer;
   String string;
   JSONArray jsonarray;

   public void testJsonString() {
      try {
         TestJSONStringer.Beany beanie = new TestJSONStringer.Beany("A beany object", 42.0D, true);
         this.jsonstringer = new JSONStringer();
         this.string = this.jsonstringer.object().key("single").value("MARIE HAA'S").key("Johnny").value("MARIE HAA\\'S").key("foo").value("bar").key("baz").array().object().key("quux").value("Thanks, Josh!").endObject().endArray().key("obj keys").value(JSONObject.getNames((Object)beanie)).endObject().toString();
         assertEquals("{\"single\":\"MARIE HAA'S\",\"Johnny\":\"MARIE HAA\\\\'S\",\"foo\":\"bar\",\"baz\":[{\"quux\":\"Thanks, Josh!\"}],\"obj keys\":[\"aString\",\"aNumber\",\"aBoolean\"]}", this.string);
         assertEquals("{\"a\":[[[\"b\"]]]}", (new JSONStringer()).object().key("a").array().array().array().value("b").endArray().endArray().endArray().endObject().toString());
         this.jsonstringer = new JSONStringer();
         this.jsonstringer.array();
         this.jsonstringer.value(1L);
         this.jsonstringer.array();
         this.jsonstringer.value((Object)null);
         this.jsonstringer.array();
         this.jsonstringer.object();
         this.jsonstringer.key("empty-array").array().endArray();
         this.jsonstringer.key("answer").value(42L);
         this.jsonstringer.key("null").value((Object)null);
         this.jsonstringer.key("false").value(false);
         this.jsonstringer.key("true").value(true);
         this.jsonstringer.key("big").value(1.23456789E96D);
         this.jsonstringer.key("small").value(1.23456789E-80D);
         this.jsonstringer.key("empty-object").object().endObject();
         this.jsonstringer.key("long");
         this.jsonstringer.value(Long.MAX_VALUE);
         this.jsonstringer.endObject();
         this.jsonstringer.value("two");
         this.jsonstringer.endArray();
         this.jsonstringer.value(true);
         this.jsonstringer.endArray();
         this.jsonstringer.value(98.6D);
         this.jsonstringer.value(-100.0D);
         this.jsonstringer.object();
         this.jsonstringer.endObject();
         this.jsonstringer.object();
         this.jsonstringer.key("one");
         this.jsonstringer.value(1.0D);
         this.jsonstringer.endObject();
         this.jsonstringer.value(beanie);
         this.jsonstringer.endArray();
         assertEquals("[1,[null,[{\"empty-array\":[],\"answer\":42,\"null\":null,\"false\":false,\"true\":true,\"big\":1.23456789E96,\"small\":1.23456789E-80,\"empty-object\":{},\"long\":9223372036854775807},\"two\"],true],98.6,-100,{},{\"one\":1},{\"A beany object\":42}]", this.jsonstringer.toString());
         assertEquals("[\n    1,\n    [\n        null,\n        [\n            {\n                \"empty-array\": [],\n                \"empty-object\": {},\n                \"answer\": 42,\n                \"true\": true,\n                \"false\": false,\n                \"long\": 9223372036854775807,\n                \"big\": 1.23456789E96,\n                \"small\": 1.23456789E-80,\n                \"null\": null\n            },\n            \"two\"\n        ],\n        true\n    ],\n    98.6,\n    -100,\n    {},\n    {\"one\": 1},\n    {\"A beany object\": 42}\n]", (new JSONArray(this.jsonstringer.toString())).toString(4));
         String[] sa = new String[]{"aString", "aNumber", "aBoolean"};
         this.jsonobject = new JSONObject(beanie, sa);
         this.jsonobject.put("Testing JSONString interface", (Object)beanie);
         assertEquals("{\n    \"aBoolean\": true,\n    \"aNumber\": 42,\n    \"aString\": \"A beany object\",\n    \"Testing JSONString interface\": {\"A beany object\":42}\n}", this.jsonobject.toString(4));
      } catch (Exception var3) {
         fail(var3.toString());
      }

   }

   public void testToString_DuplicateKeys() {
      try {
         JSONStringer jj = new JSONStringer();
         this.string = jj.object().key("bosanda").value("MARIE HAA'S").key("bosanda").value("MARIE HAA\\'S").endObject().toString();
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Duplicate key \"bosanda\"", var2.getMessage());
      }

   }

   public void testArray_ObjectAndArray() {
      try {
         this.jsonobject = new JSONObject("{ fun => with non-standard forms ; forgiving => This package can be used to parse formats that are similar to but not stricting conforming to JSON; why=To make it easier to migrate existing data to JSON,one = [[1.00]]; uno=[[{1=>1}]];'+':+6e66 ;pluses=+++;empty = '' , 'double':0.666,true: TRUE, false: FALSE, null=NULL;[true] = [[!,@;*]]; string=>  o. k. ; \r oct=0666; hex=0x666; dec=666; o=0999; noh=0x0x}");
         this.jsonarray = new JSONArray(" [\"<escape>\", next is an implied null , , ok,] ");
         this.jsonobject = new JSONObject(this.jsonobject, new String[]{"dec", "oct", "hex", "missing"});
         assertEquals("{\n \"oct\": 666,\n \"dec\": 666,\n \"hex\": \"0x666\"\n}", this.jsonobject.toString(1));
         assertEquals("[[\"<escape>\",\"next is an implied null\",null,\"ok\"],{\"oct\":666,\"dec\":666,\"hex\":\"0x666\"}]", (new JSONStringer()).array().value(this.jsonarray).value(this.jsonobject).endArray().toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public static void testToString_EmptyStringer() {
      assertEquals((String)null, (new JSONStringer()).toString());
   }

   public class Beany implements JSONString {
      public String aString;
      public double aNumber;
      public boolean aBoolean;

      public Beany(String String, double d, boolean b) {
         this.aString = String;
         this.aNumber = d;
         this.aBoolean = b;
      }

      public double getNumber() {
         return this.aNumber;
      }

      public String getString() {
         return this.aString;
      }

      public boolean isBoolean() {
         return this.aBoolean;
      }

      public String getBENT() {
         return "All uppercase key";
      }

      public String getX() {
         return "x";
      }

      public String toJSONString() {
         return "{" + JSONObject.quote(this.aString) + ":" + JSONObject.doubleToString(this.aNumber) + "}";
      }

      public String toString() {
         return this.getString() + " " + this.getNumber() + " " + this.isBoolean() + "." + this.getBENT() + " " + this.getX();
      }
   }
}
