package com.mu.io.game.packet.imp.equip;

import com.mu.game.model.equip.durability.DurabilityManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DuraRepair extends ReadAndWritePacket {
   int money = 0;

   public DuraRepair(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readByte();
      List itemIDList = new ArrayList();

      for(int i = 0; i < size; ++i) {
         long itemID = (long)this.readDouble();
         itemIDList.add(itemID);
      }

      int[] result = repariItem(player, itemIDList);
      if (result[0] != 1) {
         SystemMessage.writeMessage(player, result[0]);
      } else {
         SystemMessage.writeMessage(player, 5070);
         this.writeByte(size);
         player.writePacket(this);
      }

      itemIDList.clear();
      itemIDList = null;
   }

   public static int[] repariItem(Player player, List itemIDList) {
      HashMap itemMap = new HashMap();
      int[] results = canRepair(player, itemIDList, itemMap, -1L);
      int result = results[0];
      int money = results[1];
      if (result == 1) {
         result = PlayerManager.reduceMoney(player, money);
      }

      if (result == 1) {
         player.getItemManager().updateItemDurability(itemMap);
      }

      itemMap.clear();
      itemMap = null;
      return results;
   }

   public static int[] canRepair(Player player, List itemIDList, HashMap itemMap, long targetItemID) {
      int[] results = new int[]{1, 0, 0};
      int result = 1;
      int money = 0;
      Iterator var9 = itemIDList.iterator();

      while(var9.hasNext()) {
         Long itemID = (Long)var9.next();
         Item item = player.getEquipment().getItemByID(itemID.longValue());
         if (item == null || item.getCount() < 1) {
            result = 3002;
            break;
         }

         if (item.isEquipment() && item.getModel().isCanRepair() && DurabilityManager.canRepair(item.getItemType())) {
            if (item.getMaxDurability() < 1) {
               result = 3042;
               break;
            }

            itemMap.put(item, item.getMaxDurability() - item.getDurability());
            int tmpMoney = DurabilityManager.getRepairNeedMoney(item);
            if (itemID.longValue() == targetItemID) {
               results[2] = tmpMoney;
            }

            money += tmpMoney;
         }
      }

      results[0] = result;
      results[1] = money;
      return results;
   }
}
