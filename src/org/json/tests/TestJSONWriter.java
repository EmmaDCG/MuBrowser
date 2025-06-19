package org.json.tests;

import java.io.IOException;
import java.io.Writer;
import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class TestJSONWriter extends TestCase {
   JSONWriter jsonwriter;

   public void testKey() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.object().key("abc").value("123").key("abc2").value(60L).key("abc3").value(20.98D).key("abc4").value(true).endObject().toString();
         assertEquals("{\"abc\":\"123\",\"abc2\":60,\"abc3\":20.98,\"abc4\":true}", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testValue() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.array().value("123").value(10L).value(30.45D).value(false).endArray().toString();
         assertEquals("[\"123\",10,30.45,false]", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testObject_StopsAtMaxDepth() {
      try {
         this.jsonwriter = new JSONStringer();

         for(int i = 0; i < 201; ++i) {
            this.jsonwriter.object().key("123");
         }

         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Nesting too deep.", var2.getMessage());
      }

   }

   public void testArray_StopsAtMaxDepth() {
      try {
         this.jsonwriter = new JSONStringer();

         for(int i = 0; i < 201; ++i) {
            this.jsonwriter.array();
         }

         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Nesting too deep.", var2.getMessage());
      }

   }

   public void testValue_OutOfSequence() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.value(true);
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Value out of sequence.", var2.getMessage());
      }

   }

   public void testObject_OutOfSequence() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.object().object();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced object.", var2.getMessage());
      }

   }

   public void testObject_TwoObjectsWithinArray() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.array().object().endObject().object().endObject().endArray().toString();
         assertEquals("[{},{}]", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testObject_TwoStringsAndAnIntWithinObject() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.object().key("string1").value("abc").key("int").value(35L).key("string2").value("123").endObject().toString();
         assertEquals("{\"string1\":\"abc\",\"int\":35,\"string2\":\"123\"}", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testKey_MisplacedKey() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.key("123");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced key.", var2.getMessage());
      }

   }

   public void testKey_CatchesIoexception() {
      try {
         this.jsonwriter = new JSONWriter(new TestJSONWriter.BadWriterThrowsOnNonBrace());
         this.jsonwriter.object().key("123");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Test Message From Non-Brace", var2.getMessage());
      }

   }

   public void testObject_CatchesIoexception() {
      try {
         this.jsonwriter = new JSONWriter(new TestJSONWriter.BadWriterThrowsOnLeftBrace());
         this.jsonwriter.object();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Test Message From Left Brace", var2.getMessage());
      }

   }

   public void testKey_NullKey() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.key((String)null);
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Null key.", var2.getMessage());
      }

   }

   public void testArray_TwoArraysWithinObject() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.object().key("123").array().endArray().key("1234").array().endArray().endObject().toString();
         assertEquals("{\"123\":[],\"1234\":[]}", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testObject_TwoObjectsWithinObject() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.object().key("123").object().endObject().key("1234").object().endObject().endObject().toString();
         assertEquals("{\"123\":{},\"1234\":{}}", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testArray_TwoArraysWithinArray() {
      try {
         this.jsonwriter = new JSONStringer();
         String result = this.jsonwriter.array().array().endArray().array().endArray().endArray().toString();
         assertEquals("[[],[]]", result);
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testArray_MisplacedArray() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.object().array();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced array.", var2.getMessage());
      }

   }

   public void testEndArray_MisplacedEndArray() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.endArray();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced endArray.", var2.getMessage());
      }

   }

   public void testEndObject_MisplacedEndObject() {
      try {
         this.jsonwriter = new JSONStringer();
         this.jsonwriter.endObject();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced endObject.", var2.getMessage());
      }

   }

   public void testEndObject_CatchesIoexception() {
      try {
         this.jsonwriter = new JSONWriter(new TestJSONWriter.BadWriterThrowsOnRightBrace());
         this.jsonwriter.object().endObject();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Test Message From Right Brace", var2.getMessage());
      }

   }

   public void testPop_BadExtensionThatCausesNestingError1() {
      try {
         TestJSONWriter.BadExtensionThatCausesNestingError betcnw = new TestJSONWriter.BadExtensionThatCausesNestingError();
         betcnw.object().endObject();
         betcnw.changeMode('k').endObject();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Nesting error.", var2.getMessage());
      }

   }

   public void testPop_BadExtensionThatCausesNestingError2() {
      try {
         TestJSONWriter.BadExtensionThatCausesNestingError betcnw = new TestJSONWriter.BadExtensionThatCausesNestingError();
         betcnw.array();
         betcnw.changeMode('k').endObject();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Nesting error.", var2.getMessage());
      }

   }

   class BadExtensionThatCausesNestingError extends JSONStringer {
      public JSONWriter changeMode(char c) {
         this.mode = c;
         return this;
      }
   }

   class BadWriterThrowsOnLeftBrace extends Writer {
      public void write(char[] cbuf, int off, int len) throws IOException {
         if (cbuf[0] == '{') {
            throw new IOException("Test Message From Left Brace");
         }
      }

      public void flush() throws IOException {
      }

      public void close() throws IOException {
      }
   }

   class BadWriterThrowsOnNonBrace extends Writer {
      public void write(char[] cbuf, int off, int len) throws IOException {
         if (cbuf[0] != '{') {
            throw new IOException("Test Message From Non-Brace");
         }
      }

      public void flush() throws IOException {
      }

      public void close() throws IOException {
      }
   }

   class BadWriterThrowsOnRightBrace extends Writer {
      public void write(char[] cbuf, int off, int len) throws IOException {
         if (cbuf[0] == '}') {
            throw new IOException("Test Message From Right Brace");
         }
      }

      public void flush() throws IOException {
      }

      public void close() throws IOException {
      }
   }
}
