package com.mu.io.game.packet.imp.composite;

import com.mu.game.model.equip.compositenew.CompositeGuideModel;
import com.mu.game.model.equip.compositenew.CompositeModel;
import com.mu.game.model.equip.compositenew.MaterialModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ComAKeyFilter extends ReadAndWritePacket {
   public ComAKeyFilter(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int comID = this.readShort();
      HashMap chooseItem = new HashMap();
      HashMap chooseMaterial = new HashMap();
      int size = this.readByte();

      int result;
      for(result = 0; result < size; ++result) {
         int mID = this.readInt();
         long itemID = (long)this.readDouble();
         chooseItem.put(itemID, mID);
         chooseMaterial.put(mID, itemID);
      }

      result = RequestComPreview.check(player, comID, chooseMaterial);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
         chooseItem.clear();
         chooseMaterial.clear();
      } else {
         List items = player.getBackpack().getAllItems();
         this.filter(items, comID, chooseItem, chooseMaterial);
         RequestComPreview.sendToClient(player, comID, chooseMaterial);
         CompositeGuideModel.arrowComposite(player, comID, items, chooseMaterial);
         chooseMaterial.clear();
         chooseMaterial = null;
         chooseItem.clear();
         chooseItem = null;
         items.clear();
         items = null;
      }
   }

   public void filter(List items, int comID, HashMap hasItem, HashMap hasMaterial) {
      CompositeModel model = CompositeModel.getModel(comID);
      List chooseMaterials = model.getChooseMaterial();
      Iterator var8 = chooseMaterials.iterator();

      while(true) {
         while(true) {
            MaterialModel mm;
            do {
               if (!var8.hasNext()) {
                  return;
               }

               mm = (MaterialModel)var8.next();
            } while(hasMaterial.containsKey(mm.getMaterialID()));

            Iterator var10 = items.iterator();

            while(var10.hasNext()) {
               Item item = (Item)var10.next();
               if (!hasItem.containsKey(item.getID())) {
                  boolean suit = mm.suit(item);
                  if (suit) {
                     hasMaterial.put(mm.getMaterialID(), item.getID());
                     hasItem.put(item.getID(), mm.getMaterialID());
                     break;
                  }
               }
            }
         }
      }
   }
}
