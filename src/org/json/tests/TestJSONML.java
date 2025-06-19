package org.json.tests;

import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;

public class TestJSONML extends TestCase {
   private JSONObject jsonobject;
   private JSONArray jsonarray;
   private String string;

   public void testToJsonArray_OpenXmlTag() {
      try {
         this.string = "<xml";
         this.jsonarray = JSONML.toJSONArray(this.string);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Misshaped element at 6 [character 7 line 1]", var2.getMessage());
      }

   }

   public void testToJsonArray_MismatchedTags() {
      try {
         this.string = "<right></wrong>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Mismatched 'right' and 'wrong' at 15 [character 16 line 1]", var2.getMessage());
      }

   }

   public void testToJsonArray_TextString() {
      try {
         this.string = "This ain't XML.";
         this.jsonarray = JSONML.toJSONArray(this.string);
         fail("expecting JSONException here.");
      } catch (JSONException var2) {
         assertEquals("Bad XML at 17 [character 18 line 1]", var2.getMessage());
      }

   }

   public void testToString_XmlRecipeAsJsonObject() {
      try {
         this.string = "<recipe name=\"bread\" prep_time=\"5 mins\" cook_time=\"3 hours\"> <title>Basic bread</title> <ingredient amount=\"8\" unit=\"dL\">Flour</ingredient> <ingredient amount=\"10\" unit=\"grams\">Yeast</ingredient> <ingredient amount=\"4\" unit=\"dL\" state=\"warm\">Water</ingredient> <ingredient amount=\"1\" unit=\"teaspoon\">Salt</ingredient> <instructions> <step>Mix all ingredients together.</step> <step>Knead thoroughly.</step> <step>Cover with a cloth, and leave for one hour in warm room.</step> <step>Knead again.</step> <step>Place in a bread baking tin.</step> <step>Cover with a cloth, and leave for one hour in warm room.</step> <step>Bake in the oven at 180(degrees)C for 30 minutes.</step> </instructions> </recipe> ";
         this.jsonobject = JSONML.toJSONObject(this.string);
         assertEquals("{\"cook_time\":\"3 hours\",\"name\":\"bread\",\"tagName\":\"recipe\",\"childNodes\":[{\"tagName\":\"title\",\"childNodes\":[\"Basic bread\"]},{\"amount\":8,\"unit\":\"dL\",\"tagName\":\"ingredient\",\"childNodes\":[\"Flour\"]},{\"amount\":10,\"unit\":\"grams\",\"tagName\":\"ingredient\",\"childNodes\":[\"Yeast\"]},{\"amount\":4,\"unit\":\"dL\",\"tagName\":\"ingredient\",\"state\":\"warm\",\"childNodes\":[\"Water\"]},{\"amount\":1,\"unit\":\"teaspoon\",\"tagName\":\"ingredient\",\"childNodes\":[\"Salt\"]},{\"tagName\":\"instructions\",\"childNodes\":[{\"tagName\":\"step\",\"childNodes\":[\"Mix all ingredients together.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Knead thoroughly.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Cover with a cloth, and leave for one hour in warm room.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Knead again.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Place in a bread baking tin.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Cover with a cloth, and leave for one hour in warm room.\"]},{\"tagName\":\"step\",\"childNodes\":[\"Bake in the oven at 180(degrees)C for 30 minutes.\"]}]}],\"prep_time\":\"5 mins\"}", this.jsonobject.toString());
         assertEquals("<recipe cook_time=\"3 hours\" name=\"bread\" prep_time=\"5 mins\"><title>Basic bread</title><ingredient amount=\"8\" unit=\"dL\">Flour</ingredient><ingredient amount=\"10\" unit=\"grams\">Yeast</ingredient><ingredient amount=\"4\" unit=\"dL\" state=\"warm\">Water</ingredient><ingredient amount=\"1\" unit=\"teaspoon\">Salt</ingredient><instructions><step>Mix all ingredients together.</step><step>Knead thoroughly.</step><step>Cover with a cloth, and leave for one hour in warm room.</step><step>Knead again.</step><step>Place in a bread baking tin.</step><step>Cover with a cloth, and leave for one hour in warm room.</step><step>Bake in the oven at 180(degrees)C for 30 minutes.</step></instructions></recipe>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_XmlRecipeAsJsonArray() {
      try {
         this.string = "<recipe name=\"bread\" prep_time=\"5 mins\" cook_time=\"3 hours\"> <title>Basic bread</title> <ingredient amount=\"8\" unit=\"dL\">Flour</ingredient> <ingredient amount=\"10\" unit=\"grams\">Yeast</ingredient> <ingredient amount=\"4\" unit=\"dL\" state=\"warm\">Water</ingredient> <ingredient amount=\"1\" unit=\"teaspoon\">Salt</ingredient> <instructions> <step>Mix all ingredients together.</step> <step>Knead thoroughly.</step> <step>Cover with a cloth, and leave for one hour in warm room.</step> <step>Knead again.</step> <step>Place in a bread baking tin.</step> <step>Cover with a cloth, and leave for one hour in warm room.</step> <step>Bake in the oven at 180(degrees)C for 30 minutes.</step> </instructions> </recipe> ";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"recipe\",\n    {\n        \"cook_time\": \"3 hours\",\n        \"name\": \"bread\",\n        \"prep_time\": \"5 mins\"\n    },\n    [\n        \"title\",\n        \"Basic bread\"\n    ],\n    [\n        \"ingredient\",\n        {\n            \"amount\": 8,\n            \"unit\": \"dL\"\n        },\n        \"Flour\"\n    ],\n    [\n        \"ingredient\",\n        {\n            \"amount\": 10,\n            \"unit\": \"grams\"\n        },\n        \"Yeast\"\n    ],\n    [\n        \"ingredient\",\n        {\n            \"amount\": 4,\n            \"unit\": \"dL\",\n            \"state\": \"warm\"\n        },\n        \"Water\"\n    ],\n    [\n        \"ingredient\",\n        {\n            \"amount\": 1,\n            \"unit\": \"teaspoon\"\n        },\n        \"Salt\"\n    ],\n    [\n        \"instructions\",\n        [\n            \"step\",\n            \"Mix all ingredients together.\"\n        ],\n        [\n            \"step\",\n            \"Knead thoroughly.\"\n        ],\n        [\n            \"step\",\n            \"Cover with a cloth, and leave for one hour in warm room.\"\n        ],\n        [\n            \"step\",\n            \"Knead again.\"\n        ],\n        [\n            \"step\",\n            \"Place in a bread baking tin.\"\n        ],\n        [\n            \"step\",\n            \"Cover with a cloth, and leave for one hour in warm room.\"\n        ],\n        [\n            \"step\",\n            \"Bake in the oven at 180(degrees)C for 30 minutes.\"\n        ]\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<recipe cook_time=\"3 hours\" name=\"bread\" prep_time=\"5 mins\"><title>Basic bread</title><ingredient amount=\"8\" unit=\"dL\">Flour</ingredient><ingredient amount=\"10\" unit=\"grams\">Yeast</ingredient><ingredient amount=\"4\" unit=\"dL\" state=\"warm\">Water</ingredient><ingredient amount=\"1\" unit=\"teaspoon\">Salt</ingredient><instructions><step>Mix all ingredients together.</step><step>Knead thoroughly.</step><step>Cover with a cloth, and leave for one hour in warm room.</step><step>Knead again.</step><step>Place in a bread baking tin.</step><step>Cover with a cloth, and leave for one hour in warm room.</step><step>Bake in the oven at 180(degrees)C for 30 minutes.</step></instructions></recipe>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONObjectHtml() {
      try {
         this.string = "<div id=\"demo\" class=\"JSONML\"><p>JSONML is a transformation between <b>JSON</b> and <b>XML</b> that preserves ordering of document features.</p><p>JSONML can work with JSON arrays or JSON objects.</p><p>Three<br/>little<br/>words</p></div>";
         this.jsonobject = JSONML.toJSONObject(this.string);
         assertEquals("{\n    \"id\": \"demo\",\n    \"tagName\": \"div\",\n    \"class\": \"JSONML\",\n    \"childNodes\": [\n        {\n            \"tagName\": \"p\",\n            \"childNodes\": [\n                \"JSONML is a transformation between\",\n                {\n                    \"tagName\": \"b\",\n                    \"childNodes\": [\"JSON\"]\n                },\n                \"and\",\n                {\n                    \"tagName\": \"b\",\n                    \"childNodes\": [\"XML\"]\n                },\n                \"that preserves ordering of document features.\"\n            ]\n        },\n        {\n            \"tagName\": \"p\",\n            \"childNodes\": [\"JSONML can work with JSON arrays or JSON objects.\"]\n        },\n        {\n            \"tagName\": \"p\",\n            \"childNodes\": [\n                \"Three\",\n                {\"tagName\": \"br\"},\n                \"little\",\n                {\"tagName\": \"br\"},\n                \"words\"\n            ]\n        }\n    ]\n}", this.jsonobject.toString(4));
         assertEquals("<div id=\"demo\" class=\"JSONML\"><p>JSONML is a transformation between<b>JSON</b>and<b>XML</b>that preserves ordering of document features.</p><p>JSONML can work with JSON arrays or JSON objects.</p><p>Three<br/>little<br/>words</p></div>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArrayHtml() {
      try {
         this.string = "<div id=\"demo\" class=\"JSONML\"><p>JSONML is a transformation between <b>JSON</b> and <b>XML</b> that preserves ordering of document features.</p><p>JSONML can work with JSON arrays or JSON objects.</p><p>Three<br/>little<br/>words</p></div>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"div\",\n    {\n        \"id\": \"demo\",\n        \"class\": \"JSONML\"\n    },\n    [\n        \"p\",\n        \"JSONML is a transformation between\",\n        [\n            \"b\",\n            \"JSON\"\n        ],\n        \"and\",\n        [\n            \"b\",\n            \"XML\"\n        ],\n        \"that preserves ordering of document features.\"\n    ],\n    [\n        \"p\",\n        \"JSONML can work with JSON arrays or JSON objects.\"\n    ],\n    [\n        \"p\",\n        \"Three\",\n        [\"br\"],\n        \"little\",\n        [\"br\"],\n        \"words\"\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<div id=\"demo\" class=\"JSONML\"><p>JSONML is a transformation between<b>JSON</b>and<b>XML</b>that preserves ordering of document features.</p><p>JSONML can work with JSON arrays or JSON objects.</p><p>Three<br/>little<br/>words</p></div>", JSONML.toString(this.jsonarray));
         this.string = "{\"xmlns:soap\":\"http://www.w3.org/2003/05/soap-envelope\",\"tagName\":\"soap:Envelope\",\"childNodes\":[{\"tagName\":\"soap:Header\"},{\"tagName\":\"soap:Body\",\"childNodes\":[{\"tagName\":\"ws:listProducts\",\"childNodes\":[{\"tagName\":\"ws:delay\",\"childNodes\":[1]}]}]}],\"xmlns:ws\":\"http://warehouse.acme.com/ws\"}";
         this.jsonobject = new JSONObject(this.string);
         assertEquals("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ws=\"http://warehouse.acme.com/ws\"><soap:Header/><soap:Body><ws:listProducts><ws:delay>1</ws:delay></ws:listProducts></soap:Body></soap:Envelope>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray_JsonInformation() {
      try {
         this.string = "<xml one = 1 two=' \"2\" '><five></five>First \t&lt;content&gt;<five></five> This is \"content\". <three>  3  </three>JSON does not preserve the sequencing of elements and contents.<three>  III  </three>  <three>  T H R E E</three><four/>Content text is an implied structure in XML. <six content=\"6\"/>JSON does not have implied structure:<seven>7</seven>everything is explicit.<![CDATA[CDATA blocks<are><supported>!]]></xml>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"xml\",\n    {\n        \"two\": \" \\\"2\\\" \",\n        \"one\": 1\n    },\n    [\"five\"],\n    \"First \\t<content>\",\n    [\"five\"],\n    \"This is \\\"content\\\".\",\n    [\n        \"three\",\n        3\n    ],\n    \"JSON does not preserve the sequencing of elements and contents.\",\n    [\n        \"three\",\n        \"III\"\n    ],\n    [\n        \"three\",\n        \"T H R E E\"\n    ],\n    [\"four\"],\n    \"Content text is an implied structure in XML.\",\n    [\n        \"six\",\n        {\"content\": 6}\n    ],\n    \"JSON does not have implied structure:\",\n    [\n        \"seven\",\n        7\n    ],\n    \"everything is explicit.\",\n    \"CDATA blocks<are><supported>!\"\n]", this.jsonarray.toString(4));
         assertEquals("<xml two=\" &quot;2&quot; \" one=\"1\"><five/>First \t&lt;content&gt;<five/>This is &quot;content&quot;.<three></three>JSON does not preserve the sequencing of elements and contents.<three>III</three><three>T H R E E</three><four/>Content text is an implied structure in XML.<six content=\"6\"/>JSON does not have implied structure:<seven></seven>everything is explicit.CDATA blocks&lt;are&gt;&lt;supported&gt;!</xml>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray_SpanishNumbers() {
      try {
         this.string = "<xml do='0'>uno<a re='1' mi='2'>dos<b fa='3'/>tres<c>true</c>quatro</a>cinqo<d>seis<e/></d></xml>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"xml\",\n    {\"do\": 0},\n    \"uno\",\n    [\n        \"a\",\n        {\n            \"re\": 1,\n            \"mi\": 2\n        },\n        \"dos\",\n        [\n            \"b\",\n            {\"fa\": 3}\n        ],\n        \"tres\",\n        [\n            \"c\",\n            true\n        ],\n        \"quatro\"\n    ],\n    \"cinqo\",\n    [\n        \"d\",\n        \"seis\",\n        [\"e\"]\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<xml do=\"0\">uno<a re=\"1\" mi=\"2\">dos<b fa=\"3\"/>tres<c></c>quatro</a>cinqo<d>seis<e/></d></xml>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray_MusicNotes() {
      try {
         this.string = "<a ichi='1' ni='2'><b>The content of b</b> and <c san='3'>The content of c</c><d>do</d><e></e><d>re</d><f/><d>mi</d></a>";
         this.jsonobject = XML.toJSONObject(this.string);
         assertEquals("{\"a\":{\"f\":\"\",\"content\":\"and\",\"d\":[\"do\",\"re\",\"mi\"],\"ichi\":1,\"e\":\"\",\"b\":\"The content of b\",\"c\":{\"content\":\"The content of c\",\"san\":3},\"ni\":2}}", this.jsonobject.toString());
         assertEquals("<a><f/>and<d>do</d><d>re</d><d>mi</d><ichi>1</ichi><e/><b>The content of b</b><c>The content of c<san>3</san></c><ni>2</ni></a>", XML.toString(this.jsonobject));
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"a\",\n    {\n        \"ichi\": 1,\n        \"ni\": 2\n    },\n    [\n        \"b\",\n        \"The content of b\"\n    ],\n    \"and\",\n    [\n        \"c\",\n        {\"san\": 3},\n        \"The content of c\"\n    ],\n    [\n        \"d\",\n        \"do\"\n    ],\n    [\"e\"],\n    [\n        \"d\",\n        \"re\"\n    ],\n    [\"f\"],\n    [\n        \"d\",\n        \"mi\"\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<a ichi=\"1\" ni=\"2\"><b>The content of b</b>and<c san=\"3\">The content of c</c><d>do</d><e/><d>re</d><f/><d>mi</d></a>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray_TableOfContents() {
      try {
         this.string = "<book><chapter>Content of the first chapter</chapter><chapter>Content of the second chapter      <chapter>Content of the first subchapter</chapter>      <chapter>Content of the second subchapter</chapter></chapter><chapter>Third Chapter</chapter></book>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"book\",\n    [\n        \"chapter\",\n        \"Content of the first chapter\"\n    ],\n    [\n        \"chapter\",\n        \"Content of the second chapter\",\n        [\n            \"chapter\",\n            \"Content of the first subchapter\"\n        ],\n        [\n            \"chapter\",\n            \"Content of the second subchapter\"\n        ]\n    ],\n    [\n        \"chapter\",\n        \"Third Chapter\"\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<book><chapter>Content of the first chapter</chapter><chapter>Content of the second chapter<chapter>Content of the first subchapter</chapter><chapter>Content of the second subchapter</chapter></chapter><chapter>Third Chapter</chapter></book>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonObject_MessageXml() {
      try {
         this.string = "<Root><MsgType type=\"node\"><BatchType type=\"string\">111111111111111</BatchType></MsgType></Root>";
         this.jsonobject = JSONML.toJSONObject(this.string);
         assertEquals("{\"tagName\":\"Root\",\"childNodes\":[{\"tagName\":\"MsgType\",\"childNodes\":[{\"tagName\":\"BatchType\",\"childNodes\":[111111111111111],\"type\":\"string\"}],\"type\":\"node\"}]}", this.jsonobject.toString());
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\"Root\",[\"MsgType\",{\"type\":\"node\"},[\"BatchType\",{\"type\":\"string\"},111111111111111]]]", this.jsonarray.toString());
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJsonArray_TableMapping() {
      try {
         this.string = "<mapping><empty/>   <class name = \"Customer\">      <field name = \"ID\" type = \"string\">         <bind-xml name=\"ID\" node=\"attribute\"/>      </field>      <field name = \"FirstName\" type = \"FirstName\"/>      <field name = \"MI\" type = \"MI\"/>      <field name = \"LastName\" type = \"LastName\"/>   </class>   <class name = \"FirstName\">      <field name = \"text\">         <bind-xml name = \"text\" node = \"text\"/>      </field>   </class>   <class name = \"MI\">      <field name = \"text\">         <bind-xml name = \"text\" node = \"text\"/>      </field>   </class>   <class name = \"LastName\">      <field name = \"text\">         <bind-xml name = \"text\" node = \"text\"/>      </field>   </class></mapping>";
         this.jsonarray = JSONML.toJSONArray(this.string);
         assertEquals("[\n    \"mapping\",\n    [\"empty\"],\n    [\n        \"class\",\n        {\"name\": \"Customer\"},\n        [\n            \"field\",\n            {\n                \"name\": \"ID\",\n                \"type\": \"string\"\n            },\n            [\n                \"bind-xml\",\n                {\n                    \"node\": \"attribute\",\n                    \"name\": \"ID\"\n                }\n            ]\n        ],\n        [\n            \"field\",\n            {\n                \"name\": \"FirstName\",\n                \"type\": \"FirstName\"\n            }\n        ],\n        [\n            \"field\",\n            {\n                \"name\": \"MI\",\n                \"type\": \"MI\"\n            }\n        ],\n        [\n            \"field\",\n            {\n                \"name\": \"LastName\",\n                \"type\": \"LastName\"\n            }\n        ]\n    ],\n    [\n        \"class\",\n        {\"name\": \"FirstName\"},\n        [\n            \"field\",\n            {\"name\": \"text\"},\n            [\n                \"bind-xml\",\n                {\n                    \"node\": \"text\",\n                    \"name\": \"text\"\n                }\n            ]\n        ]\n    ],\n    [\n        \"class\",\n        {\"name\": \"MI\"},\n        [\n            \"field\",\n            {\"name\": \"text\"},\n            [\n                \"bind-xml\",\n                {\n                    \"node\": \"text\",\n                    \"name\": \"text\"\n                }\n            ]\n        ]\n    ],\n    [\n        \"class\",\n        {\"name\": \"LastName\"},\n        [\n            \"field\",\n            {\"name\": \"text\"},\n            [\n                \"bind-xml\",\n                {\n                    \"node\": \"text\",\n                    \"name\": \"text\"\n                }\n            ]\n        ]\n    ]\n]", this.jsonarray.toString(4));
         assertEquals("<mapping><empty/><class name=\"Customer\"><field name=\"ID\" type=\"string\"><bind-xml node=\"attribute\" name=\"ID\"/></field><field name=\"FirstName\" type=\"FirstName\"/><field name=\"MI\" type=\"MI\"/><field name=\"LastName\" type=\"LastName\"/></class><class name=\"FirstName\"><field name=\"text\"><bind-xml node=\"text\" name=\"text\"/></field></class><class name=\"MI\"><field name=\"text\"><bind-xml node=\"text\" name=\"text\"/></field></class><class name=\"LastName\"><field name=\"text\"><bind-xml node=\"text\" name=\"text\"/></field></class></mapping>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public static void testConstructor() {
      JSONML jsonml = new JSONML();
      assertEquals("JSONML", jsonml.getClass().getSimpleName());
   }

   public void testToJSONArray_EmptyClosingTag() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc></>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Expected a closing name instead of '>'.", var2.getMessage());
      }

   }

   public void testToJSONArray_ClosingTagWithQuestion() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc></abc?>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped close tag at 11 [character 12 line 1]", var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithTwoDashes() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><!--abc--></abc>");
         assertEquals("[\"abc\",\">\"]", this.jsonarray.toString());
         assertEquals("<abc>&gt;</abc>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithOneDash() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><!-abc--></abc>");
         assertEquals("[\"abc\",\"abc-->\"]", this.jsonarray.toString());
         assertEquals("<abc>abc--&gt;</abc>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithCdata() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><![CDATA[<abc></abc>]]></abc>");
         assertEquals("[\"abc\",\"<abc><\\/abc>\"]", this.jsonarray.toString());
         assertEquals("<abc>&lt;abc&gt;&lt;/abc&gt;</abc>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithBadCdata() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><![CDATA[<abc></abc>?]></abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var4) {
         assertEquals("Unclosed CDATA at 35 [character 36 line 1]", var4.getMessage());
      }

      try {
         this.jsonarray = JSONML.toJSONArray("<abc><![CDATA[<abc></abc>]?></abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var3) {
         assertEquals("Unclosed CDATA at 35 [character 36 line 1]", var3.getMessage());
      }

      try {
         this.jsonarray = JSONML.toJSONArray("<abc><![CDAT[<abc></abc>]]></abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Expected 'CDATA[' at 12 [character 13 line 1]", var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithCdataOnly() {
      try {
         this.jsonarray = JSONML.toJSONArray("<![CDATA[<abc></abc>]]>");
         assertEquals("[\"abc\"]", this.jsonarray.toString());
         assertEquals("<abc/>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_MetaTagWithBrokenCdata() {
      try {
         this.jsonarray = JSONML.toJSONArray("<!CDATA[<abc></abc>]]>");
         fail("Should have thrown exception.");
      } catch (JSONException var3) {
         assertEquals("Bad XML at 23 [character 24 line 1]", var3.getMessage());
      }

      try {
         this.jsonarray = JSONML.toJSONArray("<![CDATA?[abc]]>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Expected 'CDATA[' at 9 [character 10 line 1]", var2.getMessage());
      }

   }

   public void testToJSONArray_PhpTag() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><?abcde?></abc>");
         assertEquals("[\"abc\"]", this.jsonarray.toString());
         assertEquals("<abc/>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_MisshapedTag() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc><=abcde?></abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped tag at 7 [character 8 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_ReservedAttributeTagName() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc tagName=\"theName\">def</abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Reserved attribute. at 12 [character 13 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_ReservedAttributeChildNode() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc childNode=\"theChild\">def</abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Reserved attribute. at 14 [character 15 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_NoValueAttribute() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc novalue>def</abc>");
         assertEquals("{\"novalue\":\"\",\"tagName\":\"abc\",\"childNodes\":[\"def\"]}", this.jsonobject.toString());
         assertEquals("<abc novalue=\"\">def</abc>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONObject_NoValueAttributeWithEquals() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc novalue=>def</abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Missing value at 14 [character 15 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_EmptyTag() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc/>");
         assertEquals("{\"tagName\":\"abc\"}", this.jsonobject.toString());
         assertEquals("<abc/>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONArray_EmptyTag() {
      try {
         this.jsonarray = JSONML.toJSONArray("<abc/>");
         assertEquals("[\"abc\"]", this.jsonarray.toString());
         assertEquals("<abc/>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToJSONObject_BrokenEmptyTag() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc><def/?>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped tag at 11 [character 12 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_MisshapedTag() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc?");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped tag at 5 [character 6 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_NoCloseTag() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Bad XML at 6 [character 7 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_NoNameTag() {
      try {
         this.jsonobject = JSONML.toJSONObject("<>");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Misshaped tag at 2 [character 3 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_Space() {
      try {
         this.jsonobject = JSONML.toJSONObject(" ");
         fail("Should have thrown exception.");
      } catch (JSONException var2) {
         assertEquals("Bad XML at 3 [character 4 line 1]", var2.getMessage());
      }

   }

   public void testToJSONObject_SpaceContent() {
      try {
         this.jsonobject = JSONML.toJSONObject("<abc> </abc>");
         assertEquals("{\"tagName\":\"abc\"}", this.jsonobject.toString());
         assertEquals("<abc/>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_JsonArrayOfJsonObjects() {
      try {
         this.jsonarray = new JSONArray();
         this.jsonarray.put((Object)"tagName");
         this.jsonarray.put((Object)(new JSONObject()).put("tagName", (Object)"myName"));
         this.jsonarray.put((Object)(new JSONObject()).put("tagName", (Object)"otherName"));
         assertEquals("<tagName tagName=\"myName\"><otherName/></tagName>", JSONML.toString(this.jsonarray));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }

   public void testToString_JsonObjectOfJsonArrays() {
      try {
         this.jsonobject = new JSONObject();
         this.jsonobject.put("tagName", (Object)"MyName");
         this.jsonobject.put("childNodes", (Object)(new JSONArray()).put((Object)"abc").put((Object)(new JSONArray()).put((Object)"def")));
         this.jsonobject.put("123", (Object)(new JSONArray("[\"abc\"]")));
         assertEquals("<MyName 123=\"[&quot;abc&quot;]\">abc<def/></MyName>", JSONML.toString(this.jsonobject));
      } catch (JSONException var2) {
         fail(var2.getMessage());
      }

   }
}
