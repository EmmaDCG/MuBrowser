package com.mu.game.model.map.event;

import com.mu.game.model.map.Map;
import org.jdom.Attribute;
import org.jdom.Element;

public class MapEventFactory {
   public static MapEvent createEvent(Map map, Element element) {
      try {
         MapEvent event = null;
         String type = element.getAttributeValue("type").trim();
         Attribute crAttr = element.getAttribute("checkrate");
         if (type.equals("area")) {
            event = createAreaCheckEvent(map, element);
         }

         if (crAttr != null) {
            event.setCheckRate(crAttr.getLongValue());
         }

         return event;
      } catch (Exception var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private static AreaCheckEvent createAreaCheckEvent(Map map, Element element) {
      try {
         AreaCheckEvent event = new AreaCheckEvent(map, element);
         return event;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
