package com.mu.game.model.item.other.expriedHandel.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemType;
import com.mu.game.model.item.other.ExpiredItemManager;
import com.mu.game.model.item.other.expriedHandel.ExpiredHandel;
import com.mu.game.model.mall.ShortcutBuyPanel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import java.util.Iterator;
import java.util.List;

public class ExpiredHandelPanda extends ExpiredHandel {
   private int systemFunctioinID;
   private int shortcutBuyID;
   private String shortcutBuyDes;

   public ExpiredHandelPanda(int modelID, int type, String data, String shortcutBuyDes) throws Exception {
      super(modelID, type);
      this.shortcutBuyDes = shortcutBuyDes;
      String[] splits = data.split(",");
      this.systemFunctioinID = Integer.parseInt(splits[0]);
      this.shortcutBuyID = Integer.parseInt(splits[1]);
   }

   public boolean doubleClickHandel(Player player, Item item) {
      this.doHandle(player, item);
      return true;
   }

   private void doHandle(Player player, Item item) {
      SystemFunctionTip.sendToClient(player, this.getSystemFunctioinID(), Integer.valueOf(-1), this.shortcutBuyDes);
   }

   public void instantCheckHandel(Player player, Item item, boolean firstEnterMap) {
      boolean needToHandel = false;
      List list;
      Iterator var7;
      if (firstEnterMap && item.getContainerType() == 1) {
         list = ItemType.getEquipSlot(item.getItemType());
         var7 = list.iterator();

         label52: {
            Item tmpItem;
            do {
               if (!var7.hasNext()) {
                  break label52;
               }

               Integer slot = (Integer)var7.next();
               tmpItem = player.getEquipment().getItemBySlot(slot.intValue());
            } while(tmpItem != null && !tmpItem.isTimeExpired(System.currentTimeMillis()));

            needToHandel = true;
         }

         if (!needToHandel) {
            return;
         }

         list.clear();
         list = null;
      }

      if (item.getContainerType() != 4) {
         needToHandel = true;
         List itemList = player.getBackpack().getAllItems();
         var7 = itemList.iterator();

         while(var7.hasNext()) {
            Item tmpItem = (Item)var7.next();
            if (tmpItem.getID() != item.getID() && tmpItem.getItemType() == item.getItemType() && !tmpItem.isTimeExpired(System.currentTimeMillis())) {
               needToHandel = false;
               SystemFunctionTip.sendToClient(player, 1, tmpItem.getID());
               break;
            }
         }

         itemList.clear();
         list = null;
         if (needToHandel) {
            this.doHandle(player, item);
         }
      }
   }

   public void initCheck() throws Exception {
      if (!ShortcutBuyPanel.hasShortcutBuy(this.shortcutBuyID)) {
         throw new Exception("过期处理 - 熊猫类处理-快捷购买ID不存在");
      } else {
         ExpiredItemManager.addShortcut(this.getSystemFunctioinID(), this.getShortcutBuyID());
      }
   }

   public int getShortcutBuyID() {
      return this.shortcutBuyID;
   }

   public void setShortcutBuyID(int shortcutBuyID) {
      this.shortcutBuyID = shortcutBuyID;
   }

   public String getShortcutBuyDes() {
      return this.shortcutBuyDes;
   }

   public void setShortcutBuyDes(String shortcutBuyDes) {
      this.shortcutBuyDes = shortcutBuyDes;
   }

   public int getSystemFunctioinID() {
      return this.systemFunctioinID;
   }

   public void setSystemFunctioinID(int systemFunctioinID) {
      this.systemFunctioinID = systemFunctioinID;
   }
}
