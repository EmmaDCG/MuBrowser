package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.star.StarInheritData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.Iterator;

public class RequestInherit extends ReadAndWritePacket {
   public RequestInherit(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long targetItemID = (long)this.readDouble();
      long divertItemID = (long)this.readDouble();
      Item item = StrengthEquipment.getForgingItem(player, targetItemID);
      Item divertItem = StrengthEquipment.getForgingItem(player, divertItemID);
      int result = this.canInherit(player, item, divertItem);
      this.writeDouble((double)targetItemID);
      this.writeBoolean(result == 1);
      if (result == 1) {
         Item newItem = item.cloneItem(2);
         if (divertItem.getRunes().size() > 0) {
            newItem.getRunes().clear();
            Iterator var11 = divertItem.getRunes().iterator();

            while(var11.hasNext()) {
               ItemRune rune = (ItemRune)var11.next();
               newItem.addRune(rune.cloneRune(), false);
            }
         }

         newItem.changeStarLevel(divertItem.getStarLevel());
         GetItemStats.writeItem(newItem, this);
         StarInheritData data = StarInheritData.getData(divertItem.getStarLevel());
         this.writeInt(data.getMoney());
         this.writeByte(data.getMoneyType());
      }

      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   private int canInherit(Player player, Item targetItem, Item divertItem) {
      if (targetItem != null && targetItem.getCount() >= 1) {
         if (divertItem != null && divertItem.getCount() >= 1) {
            if (targetItem.getID() == divertItem.getID()) {
               return 5031;
            } else {
               divertItem.getContainerType();
               if (!StarInheritData.canInherit(targetItem.getItemType())) {
                  return 5007;
               } else if (!StarInheritData.canInherit(divertItem.getItemType())) {
                  return 5008;
               } else if (divertItem.getStarLevel() < StarInheritData.getMinStar()) {
                  return 5012;
               } else if (targetItem.getStarLevel() >= divertItem.getStarLevel()) {
                  return 5011;
               } else {
                  return divertItem.getStones().size() > 0 ? 5009 : 1;
               }
            }
         } else {
            return 3002;
         }
      } else {
         return 3002;
      }
   }
}
