package com.mu.io.game.packet.imp.composite;

import com.mu.config.BroadcastManager;
import com.mu.game.model.equip.compositenew.CompositeGuideModel;
import com.mu.game.model.equip.compositenew.CompositeModel;
import com.mu.game.model.equip.compositenew.CompositeRate;
import com.mu.game.model.equip.compositenew.MaterialModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.CompositePopup;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class CompositeItem extends ReadAndWritePacket {
   public CompositeItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public CompositeItem() {
      super(20209, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int comID = this.readShort();
      HashMap chooseMaterial = new HashMap();
      HashMap chooseItem = new HashMap();
      int size = this.readByte();

      for(int i = 0; i < size; ++i) {
         int materialID = this.readInt();
         long itemID = (long)this.readDouble();
         chooseMaterial.put(materialID, itemID);
         HashSet matSet = (HashSet)chooseItem.get(itemID);
         if (matSet == null) {
            matSet = new HashSet();
            chooseItem.put(itemID, matSet);
         }

         matSet.add(materialID);
      }

      doComposite(player, comID, chooseMaterial, chooseItem, false);
   }

   public static void doComposite(Player player, int comID, HashMap chooseMaterial, HashMap chooseItem, boolean hasPop) {
      HashMap actualMap = new HashMap();
      HashMap failMap = new HashMap();
      int[] targetAtom = new int[]{-1, -1};
      int[] results = check(player, comID, chooseMaterial, chooseItem, actualMap, failMap);
      int result = results[0];
      boolean done = true;
      if (result == 1) {
         if (results[3] == 1 && !hasPop) {
            CompositePopup pop = new CompositePopup(player.createPopupID(), comID, chooseMaterial, chooseItem);
            ShowPopup.open(player, pop);
            done = false;
         } else {
            CompositeModel model = CompositeModel.getModel(comID);
            HashMap autoConsume = model.getAutoConsume();
            int rate = results[1];
            rate = Math.min(model.getMaxRate(), rate);
            if (CompositeGuideModel.certainlySuccess(player, comID)) {
               rate = 100000;
            }

            int rnd = Rnd.get(100000);
            if (rnd < rate) {
               targetAtom = model.getRndTarget();
               ItemDataUnit data = model.getUnit(1, false, targetAtom[0]);
               data.setBind(results[3] == 1);
               OperationResult or = player.getItemManager().compositeItem(autoConsume, actualMap, data);
               result = or.getResult();
               PlayerManager.reduceMoney(player, results[2]);
               if (result == 1 && model.getBroadcast() != null) {
                  Item item = player.getBackpack().getItemByID(or.getItemID());
                  if (item != null) {
                     BroadcastManager.broadcastComposite(player, item, model.getBroadcast());
                  }
               }
            } else {
               result = player.getItemManager().compositeItem(autoConsume, failMap, (ItemDataUnit)null).getResult();
               if (result == 1) {
                  result = 5058;
                  PlayerManager.reduceMoney(player, results[2]);
               }
            }
         }
      }

      if (done) {
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         } else {
            CompositeGuideModel.finishComposite(player, comID);
         }

         player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.HC_COUNT, comID);
         sendToClient(player, comID, result, targetAtom[1]);
         chooseItem.clear();
         chooseItem = null;
         chooseMaterial.clear();
         chooseMaterial = null;
      }

      actualMap.clear();
      actualMap = null;
      failMap.clear();
      failMap = null;
   }

   public static int check(Player player, int comID, HashMap chooseMap) {
      CompositeModel model = CompositeModel.getModel(comID);
      if (model == null) {
         return 5018;
      } else {
         Iterator it = chooseMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            if (!model.hasChooseMaterial(((Integer)entry.getKey()).intValue())) {
               return 5053;
            }

            if (player.getBackpack().getItemByID(((Long)entry.getValue()).longValue()) == null) {
               return 5054;
            }
         }

         return 1;
      }
   }

   private static int[] check(Player player, int comID, HashMap chooseMaterial, HashMap chooseItem, HashMap actualMap, HashMap failMap) {
      CompositeModel model = CompositeModel.getModel(comID);
      int result = check(player, comID, chooseMaterial);
      int[] results = new int[]{result, 0, 0, 0};
      if (result != 1) {
         return results;
      } else {
         boolean bind = false;
         int rate = model.getBasicRate();
         if (player.getBackpack().isFull()) {
            results[0] = 2004;
            return results;
         } else {
            HashMap needMap = model.getAutoConsume();
            Iterator var13 = model.getChooseMaterial().iterator();

            while(var13.hasNext()) {
               MaterialModel mm = (MaterialModel)var13.next();
               if (mm.isNeed() && !chooseMaterial.containsKey(mm.getMaterialID())) {
                  results[0] = 5057;
                  return results;
               }
            }

            Iterator it = chooseItem.entrySet().iterator();

            Entry entry;
            while(it.hasNext()) {
               entry = (Entry)it.next();
               Item item = player.getBackpack().getItemByID(((Long)entry.getKey()).longValue());
               HashSet mIDs = (HashSet)entry.getValue();
               int mCount = 0;
               int failCount = 0;
               if (item.isBind()) {
                  bind = true;
               }

               if (item.getModel().getMaxStackCount() == 1 && mIDs.size() > 1) {
                  results[0] = 5056;
                  return results;
               }

               MaterialModel mm;
               for(Iterator var19 = mIDs.iterator(); var19.hasNext(); rate += CompositeRate.getRate(item, mm.getRateType())) {
                  Integer materialID = (Integer)var19.next();
                  mm = MaterialModel.getMaterialModel(materialID.intValue());
                  mCount += mm.getCount();
                  if (mm.isFailConsume()) {
                     failCount += mm.getCount();
                  }
               }

               if (item.getCount() < mCount) {
                  results[0] = 5057;
                  return results;
               }

               if (needMap.containsKey(item.getModelID())) {
                  needMap.put(item.getModelID(), ((Integer)needMap.get(item.getModelID())).intValue() + mCount);
               }

               actualMap.put(item.getID(), mCount);
               if (failCount > 0) {
                  failMap.put(item.getID(), failCount);
               }
            }

            it = needMap.entrySet().iterator();

            while(it.hasNext()) {
               entry = (Entry)it.next();
               if (!player.getBackpack().hasEnoughItem(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue())) {
                  results[0] = 5057;
                  return results;
               }

               if (!bind && player.getBackpack().getItemCountByModelId(((Integer)entry.getKey()).intValue(), false) < ((Integer)entry.getValue()).intValue()) {
                  bind = true;
               }
            }

            needMap.clear();
            needMap = null;
            if (!PlayerManager.hasEnoughMoney(player, model.getMoney())) {
               results[0] = 1011;
               return results;
            } else if (player.getIngot() < model.getIngot()) {
               results[0] = 1015;
               return results;
            } else {
               results[1] = rate;
               results[2] = model.getMoney();
               results[3] = bind ? 1 : 0;
               return results;
            }
         }
      }
   }

   public static void sendToClient(Player player, int comID, int reuslt, int targetID) {
      try {
         CompositeItem ci = new CompositeItem();
         ci.writeShort(comID);
         ci.writeBoolean(reuslt == 1);
         if (reuslt == 1) {
            ci.writeByte(targetID);
         }

         player.writePacket(ci);
         ci.destroy();
         ci = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
