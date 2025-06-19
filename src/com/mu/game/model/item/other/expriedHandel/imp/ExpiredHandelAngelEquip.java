package com.mu.game.model.item.other.expriedHandel.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.imp.EquipItem;
import com.mu.game.model.item.model.ItemType;
import com.mu.game.model.item.other.expriedHandel.ExpiredHandel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.AngelPopup;
import com.mu.game.model.unit.player.tips.SystemFunctionTipConfig;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import java.util.Iterator;
import java.util.List;

public class ExpiredHandelAngelEquip extends ExpiredHandel {
   public ExpiredHandelAngelEquip(int modelID, int type) {
      super(modelID, type);
   }

   public boolean doubleClickHandel(Player player, Item item) {
      return false;
   }

   public void instantCheckHandel(Player player, Item item, boolean firstEnterMap) {
      if (item.getContainerType() == 0) {
         int slot = item.getSlot();
         List itemList = player.getBackpack().getItemsBySort(1);
         Item useItem = null;
         long now = System.currentTimeMillis();
         Iterator var10 = itemList.iterator();

         while(true) {
            Item tmpItem;
            do {
               do {
                  if (!var10.hasNext()) {
                     if (useItem != null) {
                        SystemFunctionTip.sendToClient(player, 1, useItem.getID());
                     } else {
                        AngelPopup popup = new AngelPopup(player.createPopupID(), item);
                        ShowPopup.open(player, popup);
                     }

                     itemList.clear();
                     itemList = null;
                     return;
                  }

                  tmpItem = (Item)var10.next();
               } while(EquipItem.canEquipItem(tmpItem, player) != 1);
            } while(tmpItem.isTimeExpired(now));

            boolean flag = false;
            List slots = ItemType.getEquipSlot(tmpItem.getItemType());
            Iterator var14 = slots.iterator();

            while(var14.hasNext()) {
               Integer tmpSlot = (Integer)var14.next();
               if (tmpSlot.intValue() == slot) {
                  flag = true;
                  break;
               }
            }

            slots.clear();
            slots = null;
            if (flag) {
               if (useItem == null) {
                  useItem = tmpItem;
               } else if (SystemFunctionTipConfig.isPriority(useItem, tmpItem)) {
                  useItem = tmpItem;
               }
            }
         }
      }
   }

   public void initCheck() throws Exception {
   }
}
