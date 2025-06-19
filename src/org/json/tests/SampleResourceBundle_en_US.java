package org.json.tests;

import java.util.ListResourceBundle;

public class SampleResourceBundle_en_US extends ListResourceBundle {
   private Object[][] contents = new Object[][]{{"ASCII", "American Standard Code for Information Interchange"}, {"JAVA.desc", "Just Another Vague Acronym"}, {"JAVA.data", "Sweet language"}, {"JSON", "JavaScript Object Notation"}};

   public Object[][] getContents() {
      return this.contents;
   }
}
