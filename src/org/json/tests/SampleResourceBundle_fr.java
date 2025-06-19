package org.json.tests;

import java.util.ListResourceBundle;

public class SampleResourceBundle_fr extends ListResourceBundle {
   private Object[][] contents = new Object[][]{{"ASCII", "Number that represent chraracters"}, {"JAVA", "The language you are running to see this"}, {"JSON", "What are we testing?"}};

   public Object[][] getContents() {
      return this.contents;
   }
}
