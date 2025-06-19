package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.shop.Goods;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.hang.GameHang;
import com.mu.game.model.unit.player.hang.HangConfig;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.vip.effect.VIPEffectType;
import com.mu.io.game.packet.imp.equip.DuraRepair;
import com.mu.io.game.packet.imp.item.BuyNpcGood;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HangsetEvent extends Event {
   private long saleItemTime = System.currentTimeMillis();
   private long mendEquipTime = System.currentTimeMillis();
   public static final int AutoVipLevel = 1;

   public HangsetEvent(Player owner) {
      super(owner);
      this.checkrate = 1000;
   }

   public void work(long now) throws Exception {
      if (!((Player)this.getOwner()).isDie() && !((Player)this.getOwner()).isDestroy()) {
         GameHang hang = ((Player)this.getOwner()).getGameHang();
         if (hang.isAutoSale() && hang.isInHanging() && now - this.saleItemTime >= 10000L && ((Player)this.getOwner()).getBackpack().getVacantSize() <= 5) {
            this.autoSale();
            this.saleItemTime = now;
         }

         if (((Player)this.getOwner()).getVIPManager().getEffectBooleanValue(VIPEffectType.VE_21) && hang.isAutoBuyDrug() && ((Player)this.getOwner()).isInHanging() && !((Player)this.getOwner()).getBackpack().isFull()) {
            boolean hasDrup = ((Player)this.getOwner()).getBackpack().hasCanUseDrugItem(31, ((Player)this.getOwner()).getLevel());
            Goods goods;
            if (!hasDrup) {
               goods = HangConfig.getHpDrug(((Player)this.getOwner()).getLevel());
               BuyNpcGood.buyGoods((Player)this.getOwner(), goods, 10000, HangConfig.DrugBuyNumber, true);
            }

            hasDrup = ((Player)this.getOwner()).getBackpack().hasCanUseDrugItem(32, ((Player)this.getOwner()).getLevel());
            if (!hasDrup) {
               goods = HangConfig.getMpDrug(((Player)this.getOwner()).getLevel());
               BuyNpcGood.buyGoods((Player)this.getOwner(), goods, 10000, HangConfig.DrugBuyNumber, true);
            }
         }

         int mpPercent;
         if (hang.isAutoHp() && !((Player)this.getOwner()).getItemManager().hasInCDTime(31)) {
            mpPercent = ((Player)this.getOwner()).getGameHang().getHpPercent();
            if (mpPercent > 0 && ((Player)this.getOwner()).getHp() * 100 / ((Player)this.getOwner()).getMaxHp() <= mpPercent) {
               this.useItem(true);
            }
         }

         if (((Player)this.getOwner()).getGameHang().isAutoMp() && !((Player)this.getOwner()).getItemManager().hasInCDTime(32)) {
            mpPercent = ((Player)this.getOwner()).getGameHang().getMpPercent();
            if (mpPercent > 0 && ((Player)this.getOwner()).getMp() * 100 / ((Player)this.getOwner()).getMaxMp() <= mpPercent) {
               this.useItem(false);
            }
         }

         this.autoMend(hang, now);
      }
   }

   private void autoSale() {
      List itemList = new ArrayList();
      Player player = (Player)this.getOwner();
      player.getBackpack().getQuickSaleEquip((Player)this.getOwner(), itemList, -1, player.getGameHang().getHangSale());
      if (itemList.size() > 1) {
         int money = 0;

         Item item;
         for(Iterator var5 = itemList.iterator(); var5.hasNext(); money += item.getMoney()) {
            item = (Item)var5.next();
         }

         int result = player.getItemManager().deleteItemList(itemList, 18).getResult();
         if (result == 1) {
            PlayerManager.addMoney(player, money);
         }
      }

      itemList.clear();
      itemList = null;
   }

   private void autoMend(GameHang hang, long now) {
      if (((Player)this.getOwner()).getVIPManager().getEffectBooleanValue(VIPEffectType.VE_21)) {
         if (hang.isAutoMend() && this.mendEquipTime + 10000L < now) {
            if (((Player)this.getOwner()).getEquipment().isNeedToMend()) {
               List itemliList = ((Player)this.getOwner()).getEquipment().getAutoMendItemList();
               if (itemliList.size() > 0) {
                  DuraRepair.repariItem((Player)this.getOwner(), itemliList);
               }

               itemliList.clear();
               itemliList = null;
            }

            this.mendEquipTime = now;
         }

      }
   }

   private void useItem(boolean hp) {
      ArrayList itemList = new ArrayList();
      int drugType = 31;
      if (!hp) {
         drugType = 32;
      }

      ((Player)this.getOwner()).getBackpack().getItemByItemType(drugType, true, ((Player)this.getOwner()).getLevel(), -1, itemList);
      if (itemList.size() >= 1) {
         Item item = (Item)itemList.get(0);
         if (((Player)this.getOwner()).getItemManager().canUseItem(item)) {
            ((Player)this.getOwner()).getItemManager().useItem((Item)itemList.get(0), 1, true);
         }

      }
   }

   public Status getStatus() {
      return Status.Hangset;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
