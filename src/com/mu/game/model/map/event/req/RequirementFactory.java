package com.mu.game.model.map.event.req;

import com.mu.game.model.map.Map;
import org.jdom.Element;

public class RequirementFactory {
   public static MapEventRequirement createRequirement(Map map, Element element) {
      try {
         String type = element.getAttributeValue("type").trim();
         if (type.equals("taskaccept")) {
            return createTaskAcceptRequirement(map, element);
         }

         if (type.equals("status")) {
            return createStatusRequirement(map, element);
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return null;
   }

   private static TaskAcceptRequirement createTaskAcceptRequirement(Map map, Element element) {
      try {
         String value = element.getAttributeValue("value").trim();
         TaskAcceptRequirement tr = new TaskAcceptRequirement(map, Integer.parseInt(value));
         return tr;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private static StatusRequirement createStatusRequirement(Map map, Element element) {
      String value = element.getAttributeValue("value").trim();
      StatusRequirement sr = new StatusRequirement(map, Integer.parseInt(value));
      return sr;
   }
}
