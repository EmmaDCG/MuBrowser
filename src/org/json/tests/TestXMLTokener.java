package org.json.tests;

import junit.framework.TestCase;
import org.json.JSONException;
import org.json.XMLTokener;

public class TestXMLTokener extends TestCase {
   private XMLTokener xmltokener;

   public void testNextContent() {
      try {
         this.xmltokener = new XMLTokener("< abc><de&nbsp;f/></abc>");
         assertEquals('<', this.xmltokener.nextContent());
         assertEquals("abc>", this.xmltokener.nextContent());
         assertEquals('<', this.xmltokener.nextContent());
         assertEquals("de&nbsp;f/>", this.xmltokener.nextContent());
         assertEquals('<', this.xmltokener.nextContent());
         assertEquals("/abc>", this.xmltokener.nextContent());
         assertEquals((Object)null, this.xmltokener.nextContent());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextCdata() {
      try {
         this.xmltokener = new XMLTokener("<[CDATA[<abc/>]]>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("CDATA", this.xmltokener.nextToken());
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("<abc/>", this.xmltokener.nextCDATA());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextCdata_BrokenCdata1() {
      try {
         this.xmltokener = new XMLTokener("<[CDATA[<abc/>]><abc>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("CDATA", this.xmltokener.nextToken());
         assertEquals('[', this.xmltokener.next('['));
         this.xmltokener.nextCDATA();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unclosed CDATA at 22 [character 23 line 1]", var2.getMessage());
      }

   }

   public void testNextCdata_BrokenCdata2() {
      try {
         this.xmltokener = new XMLTokener("<[CDATA[<abc/>]]<abc>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("CDATA", this.xmltokener.nextToken());
         assertEquals('[', this.xmltokener.next('['));
         this.xmltokener.nextCDATA();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unclosed CDATA at 22 [character 23 line 1]", var2.getMessage());
      }

   }

   public void testNextCdata_BrokenCdata3() {
      try {
         this.xmltokener = new XMLTokener("<[CDATA[<abc/>]]<abc>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("CDATA", this.xmltokener.nextToken());
         assertEquals('[', this.xmltokener.next('['));
         this.xmltokener.nextCDATA();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unclosed CDATA at 22 [character 23 line 1]", var2.getMessage());
      }

   }

   public void testNextCdata_BrokenCdata4() {
      try {
         this.xmltokener = new XMLTokener("<[CDATA[<abc/>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('[', this.xmltokener.next('['));
         assertEquals("CDATA", this.xmltokener.nextToken());
         assertEquals('[', this.xmltokener.next('['));
         this.xmltokener.nextCDATA();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unclosed CDATA at 15 [character 16 line 1]", var2.getMessage());
      }

   }

   public void testNextEntity_Ampersand() {
      try {
         this.xmltokener = new XMLTokener("<&amp;>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('&', this.xmltokener.next('&'));
         assertEquals('&', this.xmltokener.nextEntity('&'));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextEntity_NumberEntity() {
      try {
         this.xmltokener = new XMLTokener("<&#60;>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('&', this.xmltokener.next('&'));
         assertEquals("&#60;", this.xmltokener.nextEntity('&'));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextEntity_BrokenEntity() {
      try {
         this.xmltokener = new XMLTokener("<&nbsp");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('&', this.xmltokener.next('&'));
         assertEquals("&#60;", this.xmltokener.nextEntity('&'));
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Missing ';' in XML entity: &nbsp at 7 [character 8 line 1]", var2.getMessage());
      }

   }

   public void testNextMeta_String() {
      try {
         this.xmltokener = new XMLTokener("<! \"metaString\">");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         assertEquals(true, this.xmltokener.nextMeta());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextMeta_OpenString() {
      try {
         this.xmltokener = new XMLTokener("<! \"metaString>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         this.xmltokener.nextMeta();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unterminated string at 16 [character 17 line 1]", var2.getMessage());
      }

   }

   public void testNextMeta_Symbols() {
      try {
         this.xmltokener = new XMLTokener("<! <>/=!?>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         assertEquals('<', this.xmltokener.nextMeta());
         assertEquals('>', this.xmltokener.nextMeta());
         assertEquals('/', this.xmltokener.nextMeta());
         assertEquals('=', this.xmltokener.nextMeta());
         assertEquals('!', this.xmltokener.nextMeta());
         assertEquals('?', this.xmltokener.nextMeta());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextMeta_Misshaped() {
      try {
         this.xmltokener = new XMLTokener("<!");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         this.xmltokener.nextMeta();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped meta tag at 3 [character 4 line 1]", var2.getMessage());
      }

   }

   public void testNextMeta_EndingWithBang() {
      try {
         this.xmltokener = new XMLTokener("<!data!");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         assertEquals(true, this.xmltokener.nextMeta());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextMeta_EndingWithSpace() {
      try {
         this.xmltokener = new XMLTokener("<!data ");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('!', this.xmltokener.next('!'));
         assertEquals(true, this.xmltokener.nextMeta());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_NormalTag() {
      try {
         this.xmltokener = new XMLTokener("<da ta>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals("da", this.xmltokener.nextToken());
         assertEquals("ta", this.xmltokener.nextToken());
         assertEquals('>', this.xmltokener.nextToken());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_TagWithBadCharacter() {
      try {
         this.xmltokener = new XMLTokener("<da<ta>");
         assertEquals('<', this.xmltokener.next('<'));
         this.xmltokener.nextToken();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Bad character in a name at 4 [character 5 line 1]", var2.getMessage());
      }

   }

   public void testNextToken_TagWithMisplacedLessThan() {
      try {
         this.xmltokener = new XMLTokener("<<data>");
         assertEquals('<', this.xmltokener.next('<'));
         this.xmltokener.nextToken();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misplaced '<' at 2 [character 3 line 1]", var2.getMessage());
      }

   }

   public void testNextToken_MisshapedElement() {
      try {
         this.xmltokener = new XMLTokener("<");
         assertEquals('<', this.xmltokener.next('<'));
         this.xmltokener.nextToken();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped element at 2 [character 3 line 1]", var2.getMessage());
      }

   }

   public void testNextToken_Symbols() {
      try {
         this.xmltokener = new XMLTokener("< /=!?");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals('/', this.xmltokener.nextToken());
         assertEquals('=', this.xmltokener.nextToken());
         assertEquals('!', this.xmltokener.nextToken());
         assertEquals('?', this.xmltokener.nextToken());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_String() {
      try {
         this.xmltokener = new XMLTokener("<\"abc&amp;123\">");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals("abc&123", this.xmltokener.nextToken());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_NoGreaterThan() {
      try {
         this.xmltokener = new XMLTokener("<abc123");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals("abc123", this.xmltokener.nextToken());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testNextToken_UnterminatedString() {
      try {
         this.xmltokener = new XMLTokener("<\"abc123>");
         assertEquals('<', this.xmltokener.next('<'));
         this.xmltokener.nextToken();
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Unterminated string at 10 [character 11 line 1]", var2.getMessage());
      }

   }

   public void testSkipTo() {
      try {
         this.xmltokener = new XMLTokener("<abc123>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals(true, this.xmltokener.skipPast("c1"));
         assertEquals('2', this.xmltokener.next('2'));
         assertEquals(false, this.xmltokener.skipPast("b1"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testSkipTo_LongParameter() {
      try {
         this.xmltokener = new XMLTokener("<abc>");
         assertEquals('<', this.xmltokener.next('<'));
         assertEquals(false, this.xmltokener.skipPast("abcdefghi"));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }
}
