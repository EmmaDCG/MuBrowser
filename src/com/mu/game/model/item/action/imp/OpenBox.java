package com.mu.game.model.item.action.imp;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.drop.model.WellDropManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.box.BoxItem;
import com.mu.game.model.item.box.BoxManager;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.Profession;
import com.mu.io.game.packet.imp.player.OpenFirecrackerEffect;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class OpenBox extends ItemAction {
   private HashMap boxIDMap = null;

   public OpenBox(HashMap boxIDMap) {
      this.boxIDMap = boxIDMap;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      Integer boxID = this.getBoxID(player);
      if (boxID == null) {
         return 3040;
      } else {
         int result = BoxManager.canOpenBox(player, boxID.intValue());
         if (result != 1) {
            return result;
         } else {
            BoxItem boxItem = BoxManager.getBoxItem(boxID.intValue());
            List itemDataList = boxItem.getItemData();
            if (itemDataList == null) {
               return 3040;
            } else {
               OperationResult or = null;
               if (boxItem.getOpenNeedItemId() != -1) {
                  HashMap delMap = new HashMap();
                  delMap.put(boxItem.getOpenNeedItemId(), boxItem.getOpenNeedCount());
                  or = player.getItemManager().deleteAndAddModel(delMap, 28, true, itemDataList);
                  delMap.clear();
                  delMap = null;
               } else {
                  or = player.getItemManager().addItem(itemDataList);
               }

               itemDataList.clear();
               itemDataList = null;
               result = or.getResult();
               if (result == 1) {
                  player.getItemManager().deleteItem(item, 1, 20);
                  if (boxItem.getIngot() > 0) {
                     PlayerManager.reduceIngot(player, boxItem.getIngot(), IngotChangeType.OpenBox, IngotChangeType.getItemLogDetail(item.getModelID()));
                  }

                  if (boxItem.isFirecracker()) {
                     OpenFirecrackerEffect.pushEffect(player);
                     SystemFunctionTip.sendToClient(player, 7, or.getItemID());
                  } else if (boxItem.isTip()) {
                     SystemFunctionTip.sendToClient(player, 7, or.getItemID());
                  }

                  Item newItem = player.getBackpack().getItemByID(or.getItemID());
                  if (newItem != null) {
                     WellDropManager.broadcastOpenBox(newItem, player, WellDropManager.spellOpenBoxReason(item), WellDropManager.spellOpenBoxNewReason(item));
                  }
               }

               return result;
            }
         }
      }
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return 1;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   private int getBoxID(Player player) {
      return this.boxIDMap.containsKey(Integer.valueOf(-1)) ? ((Integer)this.boxIDMap.get(Integer.valueOf(-1))).intValue() : ((Integer)this.boxIDMap.get(player.getProType())).intValue();
   }

   public void initCheck(String des) throws Exception {
      if (this.boxIDMap != null && this.boxIDMap.size() >= 1) {
         if (!this.boxIDMap.containsKey(Integer.valueOf(-1))) {
            int[] var5 = Profession.proTypes;
            int var4 = Profession.proTypes.length;

            for(int var3 = 0; var3 < var4; ++var3) {
               int proType = var5[var3];
               if (!this.boxIDMap.containsKey(proType)) {
                  throw new Exception(des + " : 开箱子-没有填写全部职业数据");
               }
            }
         }

         Iterator var7 = this.boxIDMap.values().iterator();

         while(var7.hasNext()) {
            Integer boxID = (Integer)var7.next();
            if (!BoxManager.hasBoxItem(boxID.intValue())) {
               throw new Exception(des + ": 开箱子 - 箱子ID不存在");
            }
         }

      } else {
         throw new Exception(des + " : 开箱子数据填写不完整");
      }
   }
}
