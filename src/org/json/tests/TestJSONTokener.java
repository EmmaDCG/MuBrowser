package org.json.tests;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TestJSONTokener extends TestCase {
   JSONTokener jsontokener;
   JSONObject jsonobject = new JSONObject();

   public void testConstructor_InputStream() {
      String string = "{\"abc\":\"123\"}";
      byte[] buf = string.getBytes();
      ByteArrayInputStream is = new ByteArrayInputStream(buf);

      try {
         this.jsontokener = new JSONTokener(is);
         assertEquals('{', this.jsontokener.next());
         assertEquals("abc", this.jsontokener.nextValue());
         assertEquals(':', this.jsontokener.next());
         assertEquals("123", this.jsontokener.nextValue());
         assertEquals('}', this.jsontokener.next());
      } catch (JSONException var5) {
         fail(var5.toString());
      }

   }

   public void testBack() {
      String string = "{\"abc\":\"123\"}";
      byte[] buf = string.getBytes();
      ByteArrayInputStream is = new ByteArrayInputStream(buf);

      try {
         this.jsontokener = new JSONTokener(is);
         assertEquals('{', this.jsontokener.next());
         assertEquals("abc", this.jsontokener.nextValue());
         assertEquals(':', this.jsontokener.next());
         this.jsontokener.back();
         assertEquals(':', this.jsontokener.next());
         assertEquals("123", this.jsontokener.nextValue());
         assertEquals('}', this.jsontokener.next());
      } catch (JSONException var5) {
         fail(var5.toString());
      }

   }

   public void testBack_FailsIfUsedTwice() {
      String string = "{\"abc\":\"123\"}";
      byte[] buf = string.getBytes();
      ByteArrayInputStream is = new ByteArrayInputStream(buf);

      try {
         this.jsontokener = new JSONTokener(is);
         assertEquals('{', this.jsontokener.next());
         assertEquals("abc", this.jsontokener.nextValue());
         assertEquals(':', this.jsontokener.next());
         this.jsontokener.back();
         this.jsontokener.back();
      } catch (JSONException var5) {
         assertEquals("Stepping back two steps is not supported", var5.getMessage());
      }

   }

   public void testNext_FakeInputStreamToTestIoexception() {
      try {
         this.jsontokener = new JSONTokener(new TestJSONTokener.MockInputStreamThrowsExceptionOnFourthRead());
         assertEquals('a', this.jsontokener.next());
         assertEquals('a', this.jsontokener.next());
         assertEquals('a', this.jsontokener.next());
         assertEquals('a', this.jsontokener.next());
         assertEquals('a', this.jsontokener.next());
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("Mock IOException thrown from read", var2.getMessage());
      }

   }

   public void testNext_EmptyStream() {
      String string = "";
      byte[] buf = string.getBytes();
      ByteArrayInputStream is = new ByteArrayInputStream(buf);

      try {
         this.jsontokener = new JSONTokener(is);
         assertEquals(0, this.jsontokener.next());
      } catch (JSONException var5) {
         fail(var5.toString());
      }

   }

   public void testNext_LineIncrementsOnNewLine() {
      this.jsontokener = new JSONTokener("abc\n123");

      try {
         this.jsontokener.next();
         this.jsontokener.next();
         this.jsontokener.next();
         assertEquals(" at 3 [character 4 line 1]", this.jsontokener.toString());
         this.jsontokener.next();
         assertEquals(" at 4 [character 0 line 2]", this.jsontokener.toString());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNext_LineIncrementsOnCarriageReturn() {
      this.jsontokener = new JSONTokener("abc\r123");

      try {
         this.jsontokener.next();
         this.jsontokener.next();
         this.jsontokener.next();
         assertEquals(" at 3 [character 4 line 1]", this.jsontokener.toString());
         this.jsontokener.next();
         this.jsontokener.next();
         assertEquals(" at 5 [character 1 line 2]", this.jsontokener.toString());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNext_LineIncrementsOnCarriageReturnAndNewLine() {
      this.jsontokener = new JSONTokener("abc\r\n123");

      try {
         this.jsontokener.next();
         this.jsontokener.next();
         this.jsontokener.next();
         assertEquals(" at 3 [character 4 line 1]", this.jsontokener.toString());
         this.jsontokener.next();
         this.jsontokener.next();
         assertEquals(" at 5 [character 0 line 2]", this.jsontokener.toString());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testSkipTo() {
      String string = "{\"abc\":\"123\",\"wer\":\"rty\"}";
      byte[] buf = string.getBytes();
      ByteArrayInputStream is = new ByteArrayInputStream(buf);

      try {
         this.jsontokener = new JSONTokener(is);
         assertEquals('{', this.jsontokener.next());
         assertEquals("abc", this.jsontokener.nextValue());
         assertEquals(':', this.jsontokener.next());
         assertEquals("123", this.jsontokener.nextValue());
         assertEquals(',', this.jsontokener.next());
         assertEquals(0, this.jsontokener.skipTo('g'));
         assertEquals('"', this.jsontokener.next());
         assertEquals('t', this.jsontokener.skipTo('t'));
         assertEquals('t', this.jsontokener.next());
         assertEquals('y', this.jsontokener.next());
      } catch (JSONException var5) {
         fail(var5.toString());
      }

   }

   public void testSkipTo_FakeInputStreamToTestIoexception() {
      this.jsontokener = new JSONTokener(new TestJSONTokener.MockInputStreamThrowsExceptionOnReset(new StringReader("123")));

      try {
         this.jsontokener.skipTo('l');
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Mock IOException thrown from reset", var2.getMessage());
      }

   }

   public void testEnd() {
      this.jsontokener = new JSONTokener("a");

      try {
         assertFalse(this.jsontokener.end());
         this.jsontokener.next();
         this.jsontokener.next();
         assertTrue(this.jsontokener.end());
         this.jsontokener.back();
         assertFalse(this.jsontokener.end());
         this.jsontokener.next();
         this.jsontokener.next();
         assertTrue(this.jsontokener.end());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testMore() {
      this.jsontokener = new JSONTokener("a");

      try {
         assertTrue(this.jsontokener.more());
         this.jsontokener.next();
         assertFalse(this.jsontokener.more());
         this.jsontokener.back();
         assertTrue(this.jsontokener.more());
         this.jsontokener.next();
         assertFalse(this.jsontokener.more());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextValue_NiceString() {
      this.jsontokener = new JSONTokener("abc");

      try {
         assertEquals("abc", this.jsontokener.nextValue());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextValue_StringWithNewLine() {
      this.jsontokener = new JSONTokener("abc\n123");

      try {
         assertEquals("abc", this.jsontokener.nextValue());
         assertEquals(Integer.valueOf(123), this.jsontokener.nextValue());
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextValue_JsonObjectString() {
      JSONObject jo = new JSONObject();

      try {
         jo.put("abc", (Object)"123");
         this.jsontokener = new JSONTokener(jo.toString());
         assertEquals(jo.toString(), this.jsontokener.nextValue().toString());
      } catch (JSONException var3) {
         fail(var3.toString());
      }

   }

   public void testNextValue_IndentedJsonObjectString() {
      JSONObject jo = new JSONObject();

      try {
         jo.put("abc", (Object)"123");
         this.jsontokener = new JSONTokener(jo.toString(4));
         assertEquals(jo.toString(), this.jsontokener.nextValue().toString());
      } catch (JSONException var3) {
         fail(var3.toString());
      }

   }

   public void testNextValue_JsonArrayString() {
      JSONArray ja = new JSONArray();

      try {
         ja.put((Object)"abc");
         ja.put((Object)"123");
         this.jsontokener = new JSONTokener(ja.toString());
         assertEquals(ja.toString(), this.jsontokener.nextValue().toString());
      } catch (JSONException var3) {
         fail(var3.toString());
      }

   }

   public void testNextValue_IndentedJsonArrayString() {
      JSONArray ja = new JSONArray();

      try {
         ja.put((Object)"abc");
         ja.put((Object)"123");
         this.jsontokener = new JSONTokener(ja.toString(4));
         assertEquals(ja.toString(), this.jsontokener.nextValue().toString());
      } catch (JSONException var3) {
         fail(var3.toString());
      }

   }

   public void testNextValue_EmptyString() {
      this.jsontokener = new JSONTokener("");

      try {
         this.jsontokener.nextValue();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Missing value at 0 [character 1 line 1]", var2.getMessage());
      }

   }

   public void testNext_ExpectedChar() {
      this.jsontokener = new JSONTokener("abc");
      char expectedA = 0;

      try {
         expectedA = this.jsontokener.next('a');
         this.jsontokener.next('c');
         fail("Should have thrown exception.");
      } catch (JSONException var3) {
         assertEquals('a', expectedA);
         assertEquals("Expected 'c' and instead saw 'b' at 2 [character 3 line 1]", var3.getMessage());
      }

   }

   public void testNext_ExpectedNumberOfCharacters() {
      this.jsontokener = new JSONTokener("abc123");
      String expectedAbc = "";
      String expectedBlank = "";

      try {
         expectedAbc = this.jsontokener.next((int)3);
         expectedBlank = this.jsontokener.next((int)0);
         this.jsontokener.next((int)7);
         fail("Should have thrown exception.");
      } catch (JSONException var4) {
         assertEquals("abc", expectedAbc);
         assertEquals("", expectedBlank);
         assertEquals("Substring bounds error at 7 [character 8 line 1]", var4.getMessage());
      }

   }

   public void testNextTo_Character() {
      this.jsontokener = new JSONTokener("abc123,test\ntestString1\rsecondString\r\nthird String");

      try {
         assertEquals("abc123", this.jsontokener.nextTo(','));
         this.jsontokener.next(',');
         assertEquals("test", this.jsontokener.nextTo(','));
         this.jsontokener.next('\n');
         assertEquals("testString1", this.jsontokener.nextTo(','));
         this.jsontokener.next('\r');
         assertEquals("secondString", this.jsontokener.nextTo(','));
         this.jsontokener.next('\r');
         this.jsontokener.next('\n');
         assertEquals("third String", this.jsontokener.nextTo(','));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextTo_String() {
      this.jsontokener = new JSONTokener("abc123,test\ntestString1\rsecondString\r\nthird String");

      try {
         assertEquals("abc", this.jsontokener.nextTo("1,"));
         assertEquals("123", this.jsontokener.nextTo("abc,"));
         this.jsontokener.next(',');
         assertEquals("te", this.jsontokener.nextTo("sabc"));
         assertEquals("st", this.jsontokener.nextTo("ring"));
         this.jsontokener.next('\n');
         assertEquals("testSt", this.jsontokener.nextTo("r"));
         assertEquals("ring1", this.jsontokener.nextTo("qw"));
         this.jsontokener.next('\r');
         assertEquals("second", this.jsontokener.nextTo("bhS"));
         assertEquals("String", this.jsontokener.nextTo("dbh"));
         this.jsontokener.next('\r');
         this.jsontokener.next('\n');
         assertEquals("third", this.jsontokener.nextTo(" ng"));
         assertEquals("String", this.jsontokener.nextTo("qwhdab"));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextString() {
      this.jsontokener = new JSONTokener("'abc'\"1\\\"2\\\"3\"'a\\u1111b\\fc\\trhd\\bdd\\r\\ngghhj'\"hghghgjfjf\"");

      try {
         this.jsontokener.next('\'');
         assertEquals("abc", this.jsontokener.nextString('\''));
         this.jsontokener.next('"');
         assertEquals("1\"2\"3", this.jsontokener.nextString('"'));
         this.jsontokener.next('\'');
         assertEquals("aᄑb\fc\trhd\bdd\r\ngghhj", this.jsontokener.nextString('\''));
         this.jsontokener.next('"');
         assertEquals("hghghgjfjf", this.jsontokener.nextString('"'));
      } catch (JSONException var2) {
         fail(var2.toString());
      }

   }

   public void testNextString_IllegalEscape() {
      this.jsontokener = new JSONTokener("'ab\\\tc'");

      try {
         this.jsontokener.next('\'');
         this.jsontokener.nextString('\'');
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("Illegal escape. at 5 [character 6 line 1]", var2.getMessage());
      }

   }

   public void testNextString_UnterminatedString() {
      this.jsontokener = new JSONTokener("'abc");

      try {
         this.jsontokener.next('\'');
         this.jsontokener.nextString('\'');
         fail("Should have thrown exception");
      } catch (JSONException var2) {
         assertEquals("Unterminated string at 5 [character 6 line 1]", var2.getMessage());
      }

   }

   public static void testDehexChar() {
      char i = '0';

      int j;
      for(j = 0; i <= '9'; ++j) {
         assertEquals(j, JSONTokener.dehexchar(i));
         ++i;
      }

      for(i = 'A'; i <= 'F'; ++j) {
         assertEquals(j, JSONTokener.dehexchar(i));
         ++i;
      }

      j = 10;

      for(i = 'a'; i <= 'f'; ++j) {
         assertEquals(j, JSONTokener.dehexchar(i));
         ++i;
      }

      assertEquals(-1, JSONTokener.dehexchar('$'));
      assertEquals(-1, JSONTokener.dehexchar('g'));
      assertEquals(-1, JSONTokener.dehexchar('G'));
      assertEquals(-1, JSONTokener.dehexchar('z'));
      assertEquals(-1, JSONTokener.dehexchar('Z'));
   }

   public void testMultipleThings() {
      try {
         this.jsontokener = new JSONTokener("{op:'test', to:'session', pre:1}{op:'test', to:'session', pre:2}");
         this.jsonobject = new JSONObject(this.jsontokener);
         assertEquals("{\"to\":\"session\",\"op\":\"test\",\"pre\":1}", this.jsonobject.toString());
         assertEquals(1, this.jsonobject.optInt("pre"));
         int i = this.jsontokener.skipTo('{');
         assertEquals(123, i);
         this.jsonobject = new JSONObject(this.jsontokener);
         assertEquals("{\"to\":\"session\",\"op\":\"test\",\"pre\":2}", this.jsonobject.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   class MockInputStreamThrowsExceptionOnFourthRead extends InputStream {
      int position = 0;

      public int read() throws IOException {
         if (this.position < 3) {
            ++this.position;
            return 97;
         } else {
            throw new IOException("Mock IOException thrown from read");
         }
      }
   }

   class MockInputStreamThrowsExceptionOnReset extends BufferedReader {
      public MockInputStreamThrowsExceptionOnReset(Reader in) {
         super(in);
      }

      public int read() throws IOException {
         return 0;
      }

      public void reset() throws IOException {
         throw new IOException("Mock IOException thrown from reset");
      }
   }
}
