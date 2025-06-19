package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.durability.DurabilityManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RepairAllItem extends ReadAndWritePacket {
   public RepairAllItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int containerType = this.readByte();
      List itemList = null;
      switch(containerType) {
      case 0:
         itemList = player.getEquipment().getAllItems();
         break;
      case 1:
         itemList = player.getBackpack().getAllItems();
         break;
      default:
         SystemMessage.writeMessage(player, 5028);
         return;
      }

      HashMap itemMap = null;
      int money = 0;
      Iterator var7 = itemList.iterator();

      while(var7.hasNext()) {
         Item item = (Item)var7.next();
         if (DurabilityManager.canRepair(item.getItemType()) && item.getItemType() != 24 && item.getDurability() < item.getMaxDurability()) {
            if (itemMap == null) {
               itemMap = new HashMap();
            }

            itemMap.put(item, item.getMaxDurability() - item.getDurability());
            money += DurabilityManager.getRepairNeedMoney(item);
         }
      }

      itemList.clear();
      if (itemMap != null && itemMap.size() >= 1) {
         if (!PlayerManager.hasEnoughMoney(player, money)) {
            itemMap.clear();
            SystemMessage.writeMessage(player, 1011);
         } else {
            player.getItemManager().updateItemDurability(itemMap);
            PlayerManager.reduceMoney(player, money);
            DurabilityPrompt.sendToClient(player, false);
            itemMap.clear();
            itemMap = null;
         }
      } else {
         SystemMessage.writeMessage(player, 5029);
      }
   }
}
