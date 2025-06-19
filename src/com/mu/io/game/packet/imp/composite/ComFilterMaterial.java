package com.mu.io.game.packet.imp.composite;

import com.mu.game.model.equip.compositenew.MaterialModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ComFilterMaterial extends ReadAndWritePacket {
   public ComFilterMaterial(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int comID = this.readShort();
      int materialID = this.readInt();
      HashMap chooseMap = new HashMap();
      int size = this.readByte();

      int result;
      for(result = 0; result < size; ++result) {
         int mID = this.readInt();
         long itemID = (long)this.readDouble();
         chooseMap.put(itemID, mID);
      }

      result = this.check(player, materialID);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      } else {
         List allItems = player.getBackpack().getAllItems();
         List suitIDs = this.filter(allItems, chooseMap, MaterialModel.getMaterialModel(materialID));
         this.writeShort(comID);
         this.writeInt(materialID);
         this.writeShort(suitIDs.size());
         Iterator var10 = suitIDs.iterator();

         while(var10.hasNext()) {
            Long itemID = (Long)var10.next();
            this.writeDouble((double)itemID.longValue());
         }

         player.writePacket(this);
         suitIDs.clear();
         suitIDs = null;
         allItems.clear();
         allItems = null;
         chooseMap.clear();
         chooseMap = null;
      }
   }

   private int check(Player player, int materialID) {
      return MaterialModel.getMaterialModel(materialID) == null ? 5053 : 1;
   }

   public List filter(List items, HashMap chooseMap, MaterialModel materital) {
      List suitableList = new ArrayList();
      Iterator var6 = items.iterator();

      while(var6.hasNext()) {
         Item item = (Item)var6.next();
         if (!chooseMap.containsKey(item.getID())) {
            boolean suit = materital.suit(item);
            if (suit) {
               suitableList.add(item.getID());
            }
         }
      }

      return suitableList;
   }
}
