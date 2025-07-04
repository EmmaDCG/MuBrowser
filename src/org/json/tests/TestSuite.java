package org.json.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestCDL.class, TestCookie.class, TestCookieList.class, TestHTTP.class, TestHTTPTokener.class, TestJSONArray.class, TestJSONException.class, TestJSONML.class, TestJSONObject.class, TestJSONStringer.class, TestJSONTokener.class, TestJSONWriter.class, TestXML.class, TestXMLTokener.class})
public class TestSuite {
}
