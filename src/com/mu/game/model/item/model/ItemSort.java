package com.mu.game.model.item.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ItemSort {
   public static final int EQUIPMENT = 1;
   public static final int DRUG = 3;
   public static final int PROPS = 4;
   public static final int SKILLBOOK = 10;
   public static final int GEMSTONE = 12;
   public static final int RUNE = 16;
   public static final int REFINING = 17;
   public static final int TASK = 22;
   public static final int SPECIAL = 23;
   public static final int NONE = 0;
   public static HashMap itemSorts = new HashMap();
   public static HashMap sortAndTypes = new HashMap();
   public static HashMap itemTypes = new HashMap();

   public static void init(InputStream in) throws Exception {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(in);
      Element root = doc.getRootElement();
      initPackets(root);
   }

   public static void initPackets(Element element) {
      List childrean = element.getChildren("itemSort");

      for(int i = 0; i < childrean.size(); ++i) {
         try {
            Element child = (Element)childrean.get(i);
            int sortId = child.getAttribute("id").getIntValue();
            String sortName = child.getAttributeValue("name");
            itemSorts.put(sortId, sortName);
            List secondChildren = child.getChildren("itemType");
            HashMap types = new HashMap();

            for(int j = 0; j < secondChildren.size(); ++j) {
               Element typeChild = (Element)secondChildren.get(j);
               int typeId = typeChild.getAttribute("id").getIntValue();
               String typeName = typeChild.getAttributeValue("name");
               int wt = -1;
               if (typeChild.getAttribute("wt") != null) {
                  wt = typeChild.getAttribute("wt").getIntValue();
               }

               types.put(typeId, typeName);
               itemTypes.put(typeId, typeName);
               ItemType.addWeaponType(typeId, wt);
            }

            sortAndTypes.put(sortId, types);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

   }

   public static HashMap getItemSorts() {
      return itemSorts;
   }

   public static HashMap getItemTypes() {
      return itemTypes;
   }

   public static boolean hasItemType(int itemType) {
      return itemTypes.containsKey(itemType);
   }
}
