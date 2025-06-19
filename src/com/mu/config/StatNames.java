package com.mu.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class StatNames {
   private static HashMap allMessages = new HashMap();

   public static String[] getMessage(int messageId) {
      String[] msg = (String[])allMessages.get(messageId);
      return msg != null ? msg : new String[]{"", "", "1", "0"};
   }

   public static void init(InputStream in) throws Exception {
      SAXBuilder sb = new SAXBuilder();
      Document doc = sb.build(in);
      Element root = doc.getRootElement();
      List childrenMessage = root.getChildren("type");
      Iterator var6 = childrenMessage.iterator();

      while(var6.hasNext()) {
         Element typeElement = (Element)var6.next();
         int id = typeElement.getAttribute("id").getIntValue();
         String name = typeElement.getAttributeValue("name");
         String des = typeElement.getAttributeValue("des");
         int changePrompt = typeElement.getAttribute("prompt").getIntValue();
         int excellent = 0;
         if (typeElement.getAttribute("excellent") != null) {
            excellent = typeElement.getAttribute("excellent").getIntValue();
         }

         String[] msg = new String[]{name, des, String.valueOf(changePrompt), String.valueOf(excellent)};
         allMessages.put(id, msg);
      }

   }
}
