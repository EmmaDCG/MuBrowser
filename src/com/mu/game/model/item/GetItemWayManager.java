package com.mu.game.model.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemWay;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class GetItemWayManager {
   private static HashMap wayMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(in);
      Element root = doc.getRootElement();
      List children = root.getChildren("item");
      Iterator var6 = children.iterator();

      while(var6.hasNext()) {
         Element element = (Element)var6.next();
         int itemId = element.getAttribute("id").getIntValue();
         String text = element.getChildText("text");
         GetItemWay gw = new GetItemWay();
         gw.writeUTF(text);
         List linkChildren = element.getChild("links").getChildren("link");
         gw.writeByte(linkChildren.size());

         for(int i = 0; i < linkChildren.size(); ++i) {
            Element linkElement = (Element)linkChildren.get(i);
            String value = linkElement.getAttributeValue("value");
            int type = linkElement.getAttribute("type").getIntValue();
            writeLinkDetail(gw, type, i, value);
         }

         wayMap.put(itemId, gw);
      }

   }

   public static void writeLinkDetail(WriteOnlyPacket packet, int type, int index, String value) throws Exception {
      packet.writeByte(type);
      String[] values;
      switch(type) {
      case 0:
         values = value.split(",");
         packet.writeShort(Integer.parseInt(values[0]));
         packet.writeInt(Integer.parseInt(values[1]));
         packet.writeInt(Integer.parseInt(values[2]));
      case 1:
      case 3:
      case 4:
      case 5:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      default:
         break;
      case 2:
         values = value.split(",");
         packet.writeShort(Integer.parseInt(values[0]));
         packet.writeInt(Integer.parseInt(values[1]));
         packet.writeInt(Integer.parseInt(values[2]));
         packet.writeInt(Integer.parseInt(values[3]));
         break;
      case 6:
         values = value.split(",");
         packet.writeShort(Integer.parseInt(values[0]));
         packet.writeByte(Integer.parseInt(values[1]));
         break;
      case 7:
         packet.writeByte(Integer.parseInt(value));
         break;
      case 13:
         values = value.split(",");
         packet.writeByte(Integer.parseInt(values[0]));
         packet.writeShort(Integer.parseInt(values[1]));
         break;
      case 14:
         values = value.split(",");
         packet.writeByte(Integer.parseInt(values[0]));
         packet.writeByte(Integer.parseInt(values[1]));
         packet.writeShort(Integer.parseInt(values[2]));
      }

   }

   public static void writeWay(Player player, int itemId) {
      GetItemWay gw = (GetItemWay)wayMap.get(itemId);
      if (gw != null) {
         player.writePacket(gw);
      }

   }
}
