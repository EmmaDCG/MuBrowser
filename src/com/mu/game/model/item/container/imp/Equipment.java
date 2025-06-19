package com.mu.game.model.item.container.imp;

import com.mu.config.Constant;
import com.mu.game.IDFactory;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.equip.durability.DurabilityData;
import com.mu.game.model.equip.durability.DurabilityManager;
import com.mu.game.model.equip.equipSet.EquipSet;
import com.mu.game.model.equip.excellent.ExcellentCountEffect;
import com.mu.game.model.equip.external.ArmorEffectAtom;
import com.mu.game.model.equip.external.ArmorEffectData;
import com.mu.game.model.equip.external.EquipExternalType;
import com.mu.game.model.equip.external.EquipmentEffect;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.equip.external.WeaponEntry;
import com.mu.game.model.equip.newStone.StoneSet;
import com.mu.game.model.equip.rune.RuneSet;
import com.mu.game.model.equip.star.StarSetModel;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemRune;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.item.action.imp.EquipItem;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.model.EquipSlot;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.model.ItemType;
import com.mu.game.model.item.model.MovementType;
import com.mu.game.model.item.model.WeaponType;
import com.mu.game.model.panda.Panda;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.listener.EquipmentListener;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.attack.AttackConstant;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.tips.SystemFunctionTipConfig;
import com.mu.io.game.packet.imp.equip.DurabilityPrompt;
import com.mu.io.game.packet.imp.equip.EquipEffect;
import com.mu.io.game.packet.imp.equip.ExcellentCountDes;
import com.mu.io.game.packet.imp.equip.RequestForgingEquip;
import com.mu.io.game.packet.imp.equip.RuneSetDes;
import com.mu.io.game.packet.imp.equip.StarSetDes;
import com.mu.io.game.packet.imp.equip.StoneSetDes;
import com.mu.io.game.packet.imp.item.DeleteItem;
import com.mu.io.game.packet.imp.item.UpdateItemStat;
import com.mu.io.game.packet.imp.panda.ExistPanda;
import com.mu.io.game.packet.imp.player.ExternalChange;
import com.mu.io.game.packet.imp.player.SelfExternal;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Equipment extends Container {
   public static final int OperType_Active = 1;
   public static final int OperType_Load = 2;
   public static final int OperType_Forging = 3;
   private static Logger logger = LoggerFactory.getLogger(Equipment.class);
   private Player owner;
   private SortedMap equipment = new TreeMap();
   private HashMap itemStats = new HashMap();
   private HashMap sets = new HashMap();
   private HashSet lastChangeSets = new HashSet();
   private HashMap runeSets = new HashMap();
   private int movementType;
   private long lastDuraChangeTime;
   private HashMap unEffectMap;
   private HashMap stoneMap;
   private List ridingStats;
   private boolean needToMend;
   private int totalStar;
   private int totalExcellentCount;
   private int totalSetStatCount;
   private int totalZhuijia;
   private int totalStoneSize;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType;

   public Equipment(Player player) {
      this.movementType = MovementType.None.getType();
      this.lastDuraChangeTime = System.currentTimeMillis();
      this.unEffectMap = new HashMap();
      this.stoneMap = new HashMap();
      this.ridingStats = new ArrayList();
      this.needToMend = false;
      this.totalStar = 0;
      this.totalExcellentCount = 0;
      this.totalSetStatCount = 0;
      this.totalZhuijia = 0;
      this.totalStoneSize = 0;
      this.owner = player;
   }

   public void loadItem(Item item) {
      if (this.equipment.containsKey(item.getSlot())) {
         logger.debug("装备位置出现错误[{}]", item.getSlot());
      } else {
         this.equipOperation(item, item.getSlot(), 2);
      }
   }

   public void onLoadApplyEquipmentStats() {
      this.balanceAccounts(false);
      Item horseItem = this.getItemBySlot(13);
      if (horseItem != null) {
         this.changeItemStats(horseItem, horseItem.getSlot(), true);
      }

      this.balanceAccounts(false);
   }

   public int equipItem(Item item, int wantedSlot, int operType) {
      int result = EquipItem.canEquipItem(item, this.getOwner());
      if (result != 1) {
         return result;
      } else {
         int slotToEquip = this.getActualEquipSlot(item, wantedSlot);
         if (slotToEquip == -1) {
            return 5001;
         } else {
            HashSet needToUnEquipSlots = this.getNeedToUnEquipSlot(item.getItemType(), slotToEquip);
            int needGrids = needToUnEquipSlots.size();
            Iterator var9 = needToUnEquipSlots.iterator();

            while(var9.hasNext()) {
               Integer nSlot = (Integer)var9.next();
               if (this.getItemBySlot(nSlot.intValue()) == null) {
                  --needGrids;
               }
            }

            if (item.isInBackpack()) {
               --needGrids;
            }

            if (needGrids > 0 && this.getOwner().getBackpack().getVacantSize() < needGrids) {
               return 2004;
            } else {
               if (item.isInBackpack()) {
                  this.getOwner().getBackpack().moveAwayfromContainer(item);
               }

               Item unItem = (Item)this.equipment.get(slotToEquip);
               if (unItem != null) {
                  this.unEquipOperation(slotToEquip, -1, unItem.getID() != item.getID());
               }

               Iterator var10 = needToUnEquipSlots.iterator();

               while(var10.hasNext()) {
                  Integer nslot = (Integer)var10.next();
                  if (nslot.intValue() != slotToEquip) {
                     Item tmpItem = (Item)this.equipment.get(nslot);
                     if (tmpItem != null) {
                        this.unEquipOperation(nslot.intValue(), -1, true);
                     }
                  }
               }

               needToUnEquipSlots.clear();
               this.equipOperation(item, slotToEquip, operType);
               this.equipResult(item, 1, slotToEquip);
               this.balanceAccounts(true);
               if (item.getItemType() == 24 && this.getOwner().isInRiding()) {
                  this.changeHorseStat(true);
               }

               this.checkTask();
               this.checkActivity(item);
               return 1;
            }
         }
      }
   }

   private void changeHorseStat(boolean send) {
      StatChange.addStat(this.getOwner(), StatIdCreator.createHorseStatId(0), this.getRidingStats(), send);
   }

   public void checkTask() {
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.SUM_QiangHua);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.SUM_ZhuoYue);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.SUM_BaoShi);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.SUM_ZhuiJia);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.MAX_ZhuiJia);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.MAX_TaoZhuang);
      this.getOwner().getTaskManager().onEventCheckValue(TargetType.ValueType.MAX_QiangHua);
      this.getOwner().getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.COUNT_ZhuoYue);
      this.getOwner().getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.COUNT_XinYun);
      this.getOwner().getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.ZB_Jie_Count);
      this.getOwner().getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.ZB_Jie_Jia_Count);
      this.getOwner().getTaskManager().onEventCheckMoreSpecify(TargetType.MoreSpecifyType.Equiment_Item);
      this.getOwner().getTaskManager().onEventCheckMoreSpecify(TargetType.MoreSpecifyType.Equiment_Position);
   }

   public void checkActivity(Item item) {
      Activity activity = ActivityManager.getActivity(7);
      if (activity.isOpen()) {
         activity.refreshIcon(this.getOwner());
      }

   }

   public int getActualEquipSlot(Item targetItem, int wantedSlot) {
      int slotToEquip = -1;
      List slots = ItemType.getEquipSlot(targetItem.getItemType());
      if (slots != null && slots.size() >= 1) {
         if (wantedSlot != -1) {
            boolean suit = false;
            Iterator var14 = slots.iterator();

            while(var14.hasNext()) {
               Integer slot = (Integer)var14.next();
               if (slot.intValue() == wantedSlot) {
                  suit = true;
                  break;
               }
            }

            return !suit ? -1 : wantedSlot;
         } else {
            Iterator var6 = slots.iterator();

            while(var6.hasNext()) {
               Integer slot = (Integer)var6.next();
               Item slotItem = (Item)this.equipment.get(slot);
               if (slotItem == null) {
                  slotToEquip = slot.intValue();
                  break;
               }

               if (slot.intValue() == 0 && ItemType.getWeaponType(slotItem.getItemType()) == WeaponType.BothHands) {
                  slotToEquip = slot.intValue();
                  break;
               }
            }

            if (slotToEquip == -1) {
               slotToEquip = ((Integer)slots.get(0)).intValue();
               Item tmpItem = (Item)this.equipment.get(slotToEquip);

               for(int i = 1; i < slots.size(); ++i) {
                  int tmpSlot = ((Integer)slots.get(i)).intValue();
                  Item eItem = (Item)this.equipment.get(tmpSlot);
                  if (!SystemFunctionTipConfig.isPriority(tmpItem, eItem)) {
                     tmpItem = eItem;
                     slotToEquip = tmpSlot;
                  }
               }
            }

            slots = null;
            return slotToEquip;
         }
      } else {
         return -1;
      }
   }

   private HashSet getNeedToUnEquipSlot(int itemType, int equipSlot) {
      HashSet slots = new HashSet();
      slots.add(equipSlot);
      WeaponType wt = ItemType.getWeaponType(itemType);
      Item item = null;
      switch($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType()[wt.ordinal()]) {
      case 2:
         if (equipSlot != 0) {
            item = this.getItemBySlot(0);
            if (item != null && WeaponType.fine(item.getItemType()) == WeaponType.BothHands) {
               slots.add(Integer.valueOf(0));
            }
         }
         break;
      case 3:
         slots.add(Integer.valueOf(0));
         slots.add(Integer.valueOf(1));
         break;
      case 4:
         item = this.getItemBySlot(0);
         if (item != null && WeaponType.fine(item.getItemType()) == WeaponType.BothHands) {
            slots.add(Integer.valueOf(0));
         }
      }

      if (equipSlot == 1) {
         Item weaponItem = this.getItemBySlot(0);
         if (weaponItem != null && ItemType.getWeaponType(weaponItem.getItemType()) == WeaponType.BothHands) {
            slots.add(Integer.valueOf(0));
         }
      }

      return slots;
   }

   private void equipResult(Item item, int result, int slot) {
      if (slot == 13) {
         this.getOwner().stopRidingStatus();
      }

      this.externalChange(item, slot);
   }

   private void externalChange(Item item, int slot) {
      List externalType = ItemType.getEquipExternalType(item.getItemType(), slot);
      if (externalType.size() > 0 && this.getOwner().getMap().canChangeExteranl()) {
         sendExternalChange(this.getOwner());
      }

      externalType.clear();
      externalType = null;
   }

   public static void sendExternalChange(Player player) {
      SelfExternal.sendToClient(player);
      ExternalChange.sendToClient(player);
   }

   private Item unEquipOperation(int slot, int targetSlot, boolean moveToBackpack) {
      Item item = (Item)this.equipment.get(slot);
      if (moveToBackpack) {
         if (this.getOwner().getBackpack().getItemBySlot(targetSlot) != null) {
            targetSlot = -1;
         }

         this.getOwner().getItemManager().updateItemContainer(item, 1, targetSlot);
         this.unEffectMap.remove(item.getID());
      } else {
         this.equipment.remove(slot);
      }

      this.checkSets(item, false);
      this.checkExternal(item, slot, false);
      this.checkArmorEffect(item, false);
      this.changeItemStats(item, slot, false);
      this.checkPanda(item, false, 1);
      return item;
   }

   private void equipOperation(Item item, int slot, int operType) {
      switch(operType) {
      case 1:
         this.getOwner().getItemManager().updateItemContainer(item, this.getType(), slot);
         break;
      default:
         this.putToContainerBySlot(item, slot);
      }

      this.checkSets(item, true);
      this.changeItemStats(item, slot, true);
      this.checkExternal(item, slot, true);
      this.checkArmorEffect(item, true);
      this.checkPanda(item, true, operType);
   }

   private void checkPanda(Item item, boolean equip, int operType) {
      if (item.getItemType() == 20) {
         if (equip) {
            int templateId = item.getModel().getExternalModelMenRight(item.getStarLevel());
            Player player = this.getOwner();
            Panda panda = new Panda(IDFactory.getTemporaryID(), player.getMap(), player);
            panda.setModelId(templateId);
            player.setPanda(panda);
            if (operType == 1) {
               this.getOwner().getMap().addPanda(panda);
               this.getOwner().getMap().pandaEnterMapSuccess(panda);
               ExistPanda.sendMsgPandaExist(player, true);
            }
         } else {
            Player player = this.getOwner();
            Panda panda = player.getPanda();
            if (panda != null) {
               panda.hide();
               player.setPanda((Panda)null);
               panda.destroy();
               ExistPanda.sendMsgPandaExist(player, false);
            }
         }
      }

   }

   private void checkSets(Item item, boolean equip) {
      int setID = item.getSetID();
      if (setID != -1) {
         this.lastChangeSets.add(setID);
         EquipSet set = (EquipSet)this.sets.get(setID);
         if (equip) {
            if (set == null) {
               set = new EquipSet(setID);
               this.sets.put(setID, set);
            }

            set.addEquip(item);
         } else if (set != null) {
            set.delEquip(item);
            if (set.getEquipModelIDs().size() < 1) {
               this.sets.remove(setID);
               set.destroy();
            }
         }

      }
   }

   private void checkRuneCounts(Item item, boolean equip) {
      HashMap tmpMap = new HashMap();
      Iterator var5 = this.equipment.values().iterator();

      while(var5.hasNext()) {
         Item tmpItem = (Item)var5.next();
         Iterator var7 = tmpItem.getRunes().iterator();

         while(var7.hasNext()) {
            ItemRune rune = (ItemRune)var7.next();
            Integer count = (Integer)tmpMap.get(rune.getModelID());
            if (count == null) {
               tmpMap.put(rune.getModelID(), Integer.valueOf(1));
            } else {
               tmpMap.put(rune.getModelID(), count.intValue() + 1);
            }
         }
      }

      var5 = this.runeSets.values().iterator();

      while(var5.hasNext()) {
         RuneSet set = (RuneSet)var5.next();
         int trueCount = tmpMap.containsKey(set.getModelID()) ? ((Integer)tmpMap.get(set.getModelID())).intValue() : 0;
         if (set.getCount() > trueCount) {
            set.removeRune(set.getCount() - trueCount);
         } else if (set.getCount() < trueCount) {
            set.addRune(trueCount - set.getCount());
         }
      }

      Iterator it = tmpMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         if (!this.runeSets.containsKey(entry.getKey())) {
            RuneSet set = new RuneSet(((Integer)entry.getKey()).intValue());
            set.addRune(((Integer)entry.getValue()).intValue());
            this.runeSets.put(set.getModelID(), set);
         }
      }

      tmpMap.clear();
      tmpMap = null;
   }

   private void checkArmorEffect(Item item, boolean equip) {
      boolean canChange = ArmorEffectData.inEffectData(item.getModelID());
      if (canChange) {
         ExternalEntry chestEntry = this.getOwner().getExternal(13);
         ExternalEntry footEntry = this.getOwner().getExternal(14);
         if (chestEntry == null) {
            chestEntry = new ExternalEntry(13, 0, 0);
            this.getOwner().addExternal(chestEntry);
         }

         if (footEntry == null) {
            footEntry = new ExternalEntry(14, 0, 0);
            this.getOwner().addExternal(footEntry);
         }

         if (!equip) {
            chestEntry.setModelID(0);
            footEntry.setModelID(0);
         } else {
            ArmorEffectAtom atom = ArmorEffectData.getEffectAtom(item, this.equipment);
            int chestID = atom == null ? 0 : atom.getChestID();
            int footID = atom == null ? 0 : atom.getFootID();
            chestEntry.setModelID(chestID);
            footEntry.setModelID(footID);
         }
      }

   }

   private void checkExternal(Item item, int equipSlot, boolean equip) {
      List externalTypes = ItemType.getEquipExternalType(item.getItemType(), equipSlot);
      if (externalTypes.size() > 0) {
         Iterator var6 = externalTypes.iterator();

         while(var6.hasNext()) {
            int type = ((Integer)var6.next()).intValue();
            ExternalEntry entry = this.getOwner().getExternal(type);
            int modelID;
            int effectID;
            if (equip) {
               modelID = item.getModel().getExternalModelMenRight(item.getStarLevel());
               effectID = EquipmentEffect.getExternalEffectID(item.getModelID(), item.getStarLevel());
               WeaponEntry we;
               switch(type) {
               case 6:
                  modelID = item.getModel().getExternalModelMenLeft(item.getStarLevel());
                  if (item.getItemType() == 3) {
                     we = WeaponEntry.getEntry(modelID);
                     if (we != null) {
                        this.movementType = we.getMoveType();
                     }
                  }
                  break;
               case 7:
                  if (item.getItemType() != 3) {
                     we = WeaponEntry.getEntry(modelID);
                     if (we != null) {
                        this.movementType = we.getMoveType();
                     }
                  }
               }

               if (entry == null) {
                  entry = new ExternalEntry(type, modelID, effectID);
                  this.getOwner().addExternal(entry);
               } else {
                  entry.setEffectID(effectID);
                  entry.setModelID(modelID);
               }
            } else {
               modelID = EquipExternalType.getDefaulModelID(this.getOwner().getProType(), type);
               effectID = EquipmentEffect.getExternalEffectID(0, 0);
               switch(type) {
               case 6:
                  if (item.getItemType() == 3) {
                     this.movementType = MovementType.None.getType();
                  }
                  break;
               case 7:
                  this.movementType = MovementType.None.getType();
               }

               if (entry != null) {
                  entry.setEffectID(effectID);
                  entry.setModelID(modelID);
               } else {
                  entry = new ExternalEntry(type, modelID, effectID);
                  this.getOwner().addExternal(entry);
               }
            }
         }
      }

      externalTypes.clear();
      externalTypes = null;
   }

   private void checkSkill(Item item, boolean equip) {
   }

   public int unEquipItem(long itemID, int slot, boolean remove) {
      int result = this.canUnEquip(itemID, remove);
      if (result != 1) {
         return result;
      } else {
         Item itemToUnEquip = this.getItemByID(itemID);
         int oldSlot = itemToUnEquip.getSlot();
         this.unEquipOperation(oldSlot, slot, !remove);
         this.unEquipResult(itemID, result, oldSlot, itemToUnEquip.getSlot());
         this.equipResult(itemToUnEquip, 1, oldSlot);
         if (remove) {
            DeleteItem.sendToClient(this.getOwner(), itemID, this.getType());
         }

         this.balanceAccounts(true);
         if (itemToUnEquip.getItemType() == 24) {
            StatChange.endStat(this.getOwner(), StatIdCreator.createHorseStatId(0), true);
         }

         this.checkActivity(itemToUnEquip);
         return 1;
      }
   }

   private int canUnEquip(long itemID, boolean remove) {
      Item item = this.getItemByID(itemID);
      if (item == null) {
         return 3002;
      } else {
         return !remove && this.getOwner().getBackpack().isFull() ? 2004 : 1;
      }
   }

   private void unEquipResult(long itemID, int result, int oldSlot, int newSlot) {
      if (oldSlot == 13) {
         this.getOwner().stopRidingStatus();
      }

   }

   private void removeItemStats(long itemID, int operationSlot) {
      List fms = (List)this.itemStats.remove(itemID);
      if (fms != null) {
         fms.clear();
         fms = null;
      }

      if (operationSlot == 13) {
         this.ridingStats.clear();
      }

   }

   private void addItemStats(Item item, float coeff) {
      this.removeItemStats(item.getID(), item.getSlot());
      List statList = EquipmentListener.getItemFinalStat(item, coeff, this.ridingStats);
      this.itemStats.put(item.getID(), statList);
   }

   private void changeItemStats(Item item, int operationSlot, boolean equip) {
      if (!equip) {
         this.removeItemStats(item.getID(), operationSlot);
      }

      WeaponType wt = ItemType.getWeaponType(item.getItemType());
      switch($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType()[wt.ordinal()]) {
      case 1:
         if (equip) {
            this.addItemStats(item, AttackConstant.Common_Attack);
         }
         break;
      case 2:
      default:
         if (operationSlot != 0 && operationSlot != 1) {
            if (equip) {
               this.addItemStats(item, AttackConstant.Common_Attack);
            }
         } else {
            int otherSlot = operationSlot == 0 ? 1 : 0;
            Item otherItem = this.getItemBySlot(otherSlot);
            if (!equip) {
               if (otherItem != null) {
                  this.addItemStats(otherItem, AttackConstant.OneHand_Attack);
               }
            } else if (otherItem == null) {
               this.addItemStats(item, AttackConstant.OneHand_Attack);
            } else {
               WeaponType owt = ItemType.getWeaponType(otherItem.getItemType());
               if (owt == WeaponType.SecondaryHand) {
                  this.addItemStats(item, AttackConstant.OneHand_Attack);
               } else if (operationSlot == 0) {
                  this.addItemStats(item, AttackConstant.MainHand_Attack);
                  this.addItemStats(otherItem, AttackConstant.SecondaryHand_Attack);
               } else {
                  this.addItemStats(item, AttackConstant.SecondaryHand_Attack);
                  this.addItemStats(otherItem, AttackConstant.MainHand_Attack);
               }
            }
         }
         break;
      case 3:
         if (equip) {
            this.addItemStats(item, AttackConstant.BothHand_Attack);
         }
         break;
      case 4:
         if (equip) {
            this.addItemStats(item, AttackConstant.OneHand_Attack);
            Item otherItem_1 = this.getItemBySlot(0);
            if (otherItem_1 != null) {
               this.addItemStats(otherItem_1, AttackConstant.OneHand_Attack);
            }
         }
      }

   }

   public void beingAttacked() {
      int rnd = Rnd.get(100000);
      if (System.currentTimeMillis() - this.lastDuraChangeTime >= (long)DurabilityManager.getIntevalTime()) {
         if (rnd <= DurabilityManager.getConsumeRate()) {
            HashMap itemMap = null;
            Iterator var4 = this.equipment.values().iterator();

            while(var4.hasNext()) {
               Item item = (Item)var4.next();
               if (item.getDurability() > 0) {
                  if (itemMap == null) {
                     itemMap = new HashMap();
                  }

                  itemMap.put(item, Integer.valueOf(-1));
               }
            }

            this.lastDuraChangeTime = System.currentTimeMillis();
            this.getOwner().getItemManager().updateItemDurability(itemMap);
            if (itemMap != null) {
               itemMap.clear();
               itemMap = null;
            }

         }
      }
   }

   public void ownerDie() {
      HashMap itemMap = null;
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (item.getDurability() > 0) {
            if (itemMap == null) {
               itemMap = new HashMap();
            }

            int dura = Constant.getPercentValue(item.getMaxDurability(), 10000);
            itemMap.put(item, dura);
         }
      }

      this.lastDuraChangeTime = System.currentTimeMillis();
      this.getOwner().getItemManager().updateItemDurability(itemMap);
      if (itemMap != null) {
         itemMap.clear();
         itemMap = null;
      }

   }

   public void doDuraChange(HashMap itemMap) {
      if (itemMap != null && itemMap.size() >= 1) {
         boolean recalculated = false;
         boolean alert = false;
         Iterator it = itemMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            Item item = (Item)entry.getKey();
            int oldDura = ((Integer)entry.getValue()).intValue();
            DurabilityData data;
            if (item.getDurability() < oldDura) {
               data = DurabilityManager.getDurabilityData(item.getItemType());
               if (item.getDurability() <= 0) {
                  if (data == null) {
                     this.getOwner().getItemManager().deleteItem(item, 25);
                  } else {
                     recalculated = true;
                     if (!data.isStatOnly()) {
                        this.checkExternal(item, item.getSlot(), false);
                        this.checkArmorEffect(item, false);
                        this.checkSkill(item, false);
                        this.externalChange(item, item.getSlot());
                     }

                     alert = true;
                  }
               } else if (this.alert(item.getDurability(), oldDura, item.getMaxDurability())) {
                  alert = true;
               }
            } else {
               data = DurabilityManager.getDurabilityData(item.getItemType());
               if (oldDura < 1) {
                  recalculated = true;
                  if (!data.isStatOnly()) {
                     this.checkExternal(item, item.getSlot(), true);
                     this.checkSkill(item, true);
                     this.externalChange(item, item.getSlot());
                  }
               }

               if (this.alert(item.getDurability(), oldDura, item.getMaxDurability())) {
                  alert = true;
               }
            }
         }

         if (alert) {
            boolean show = this.needToSendDurability();
            this.needToMend = this.needToSendDurability();
            DurabilityPrompt.sendToClient(this.getOwner(), show);
         }

         if (recalculated) {
            this.balanceAccounts(true);
         }

      }
   }

   private void checkNeedToAutoMend() {
      this.needToMend = false;
      Iterator var2 = this.equipment.values().iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         this.needToMend = this.needMend(item);
         if (this.needToMend) {
            break;
         }
      }

   }

   public List getAutoMendItemList() {
      List itemList = new ArrayList();
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         boolean needMend = this.needMend(item);
         if (needMend) {
            itemList.add(item.getID());
         }
      }

      return itemList;
   }

   private boolean needMend(Item item) {
      DurabilityData data = DurabilityManager.getDurabilityData(item.getItemType());
      if (data == null) {
         return false;
      } else if (item.getMaxDurability() < 1) {
         return false;
      } else {
         int value = Constant.getPercent(item.getDurability(), item.getMaxDurability());
         return value <= 30000;
      }
   }

   public boolean needToSendDurability() {
      Iterator var2 = this.equipment.values().iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         boolean needSend = this.needMend(item);
         if (needSend) {
            return true;
         }
      }

      return false;
   }

   private boolean alert(int nowDura, int oldDura, int maxDura) {
      int oldAlert = DurabilityManager.getAlertType(oldDura, maxDura);
      int newAlert = DurabilityManager.getAlertType(nowDura, maxDura);
      return oldAlert != newAlert;
   }

   public boolean isEffect(Item item) {
      if (item.getDurability() <= 0 && item.getMaxDurability() > 0) {
         return false;
      } else if (item.isTimeExpired(System.currentTimeMillis())) {
         return false;
      } else {
         SortedMap needMap = item.getModel().getNeedBasicPro();
         Iterator it = needMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            if (this.owner.getStatValue((StatEnum)entry.getKey()) < ((Integer)entry.getValue()).intValue()) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean calWhenBasicPropertyChange() {
      boolean change = false;
      Iterator var3 = this.itemStats.keySet().iterator();

      while(var3.hasNext()) {
         Long itemID = (Long)var3.next();
         Item item = this.getItemByID(itemID.longValue());
         boolean effect = this.isEffect(item);
         if (effect) {
            if (this.unEffectMap.containsKey(itemID)) {
               change = true;
               break;
            }
         } else if (!this.unEffectMap.containsKey(itemID)) {
            change = true;
            break;
         }
      }

      if (change) {
         this.balanceAccounts(true);
      }

      return change;
   }

   private int getTotalValue(HashMap map) {
      int total = 0;

      Integer value;
      for(Iterator var4 = map.values().iterator(); var4.hasNext(); total += value.intValue()) {
         value = (Integer)var4.next();
      }

      return total;
   }

   public void balanceAccounts(boolean send) {
      List allStats = new ArrayList();
      HashMap effectChangeMap = new HashMap();
      int tmpStarLevel = 0;
      int tmpExcellentCount = 0;
      this.totalSetStatCount = 0;
      this.totalZhuijia = 0;
      this.totalStoneSize = 0;
      int domi = 0;
      int tmpStoneCount = this.getTotalValue(this.stoneMap);
      this.stoneMap.clear();
      Iterator var9 = this.itemStats.keySet().iterator();

      while(var9.hasNext()) {
         Long itemID = (Long)var9.next();
         Item item = this.getItemByID(itemID.longValue());
         boolean effect = this.isEffect(item);
         this.totalZhuijia += item.getZhuijiaLevel();
         this.totalStoneSize += item.getStones().size();
         if (effect) {
            allStats.addAll((Collection)this.itemStats.get(itemID));
            tmpStarLevel += item.getStarLevel();
            tmpExcellentCount += item.getBonusStatSize(3);
            if (this.unEffectMap.containsKey(item.getID())) {
               this.unEffectMap.remove(item.getID());
               effectChangeMap.put(item.getID(), true);
            }

            domi += item.getDomineering();
         } else {
            if (!this.unEffectMap.containsKey(item.getID())) {
               effectChangeMap.put(item.getID(), false);
            }

            this.unEffectMap.put(item.getID(), false);
         }

         List stoneList = item.getStones();
         Iterator var14 = stoneList.iterator();

         while(var14.hasNext()) {
            ItemStone stone = (ItemStone)var14.next();
            int type = ItemModel.getModel(stone.getModelID()).getItemType();
            int count = this.stoneMap.containsKey(type) ? ((Integer)this.stoneMap.get(type)).intValue() : 0;
            ++count;
            this.stoneMap.put(type, count);
         }
      }

      EquipSet set;
      for(var9 = this.sets.values().iterator(); var9.hasNext(); domi += set.getDomineering()) {
         set = (EquipSet)var9.next();
         allStats.addAll(set.getStats());
      }

      List setSends = null;

      EquipSet set2;
      Iterator var21;
      for(var21 = this.sets.values().iterator(); var21.hasNext(); this.totalSetStatCount += set2.getStatsCount()) {
         set2 = (EquipSet)var21.next();
         if (this.lastChangeSets.contains(set2.getSetID())) {
            int setItemSize = set2.getItemSize();
            Iterator var27 = this.equipment.values().iterator();

            while(var27.hasNext()) {
               Item item = (Item)var27.next();
               if (set2.hasModelID(item.getModelID())) {
                  item.setSetItemSize(setItemSize);
                  if (send) {
                     if (setSends == null) {
                        setSends = new ArrayList();
                     }

                     setSends.remove(item);
                     setSends.add(item);
                  }
               }
            }
         }
      }

      if (setSends != null) {
         if (send) {
            UpdateItemStat.sendToClient(this.getOwner(), setSends, this.getType());
         }

         setSends.clear();
      }

      this.checkRuneCounts((Item)null, true);
      var21 = this.runeSets.values().iterator();

      while(var21.hasNext()) {
         RuneSet set3 = (RuneSet)var21.next();
         if (set3.isEffect()) {
            allStats.addAll(set3.getStats());
         }
      }

      var21 = StoneSet.getSetList().iterator();

      while(var21.hasNext()) {
         StoneSet ss = (StoneSet)var21.next();
         if (ss.isEffect(this.stoneMap)) {
            allStats.addAll(ss.getStatList());
            domi += ss.getDomineering();
         }
      }

      StarSetModel curModel = StarSetModel.getActiveModel(tmpStarLevel);
      if (curModel.getStarLevel() <= tmpStarLevel) {
         allStats.addAll(curModel.getStats());
         domi += curModel.getDomineering();
      }

      ExcellentCountEffect curEffect = ExcellentCountEffect.getActiveEffect(tmpExcellentCount);
      if (curEffect.getCount() <= tmpExcellentCount) {
         allStats.addAll(curEffect.getStats());
         domi += curEffect.getDomineering();
      }

      this.lastChangeSets.clear();
      allStats.add(new FinalModify(StatEnum.DOMINEERING, domi, StatModifyPriority.ADD));
      StatChange.addStat(this.getOwner(), StatIdCreator.createEquipmentID(), allStats, send);
      if (send) {
         RuneSetDes.sendToClient(this.getOwner());
         if (tmpStarLevel != this.getTotalStar()) {
            this.setTotalStar(tmpStarLevel);
            StarSetDes.sendToClient(this.getOwner());
         }

         if (tmpExcellentCount != this.getTotalExcellentCount()) {
            this.setTotalExcellentCount(tmpExcellentCount);
            ExcellentCountDes.sendToClient(this.getOwner());
         }

         if (tmpStoneCount != this.getTotalValue(this.stoneMap)) {
            StoneSetDes.sendToClient(this.getOwner());
         }

         EquipEffect.sendToClient(this.getOwner(), effectChangeMap);
      }

      this.setTotalStar(tmpStarLevel);
      this.setTotalExcellentCount(tmpExcellentCount);
      effectChangeMap.clear();
      effectChangeMap = null;
      this.checkNeedToAutoMend();
   }

   public void updateItem(Item item) {
      this.unEquipOperation(item.getSlot(), -1, false);
      this.equipItem(item, item.getSlot(), 3);
   }

   public int getMainWeaponModelID() {
      return this.equipment.containsKey(Integer.valueOf(0)) ? ((Item)this.equipment.get(Integer.valueOf(0))).getModelID() : 0;
   }

   public boolean canRide() {
      Item item = (Item)this.equipment.get(Integer.valueOf(13));
      if (item == null) {
         return false;
      } else {
         return item.getDurability() > 0;
      }
   }

   public ArrayList getAllItems() {
      ArrayList itemList = new ArrayList();
      itemList.addAll(this.equipment.values());
      return itemList;
   }

   public void filterForgingItem(List itemList, int forgingType) {
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (RequestForgingEquip.filter(item, forgingType)) {
            itemList.add(item);
         }
      }

   }

   public int getZhuijiaItemCount(int level, int zhuijiaLevel) {
      int count = 0;
      Iterator var5 = this.equipment.values().iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         if (item.getLevel() >= level && item.getZhuijiaLevel() >= zhuijiaLevel) {
            ++count;
         }
      }

      return count;
   }

   public int getItemCountByLevel(int level) {
      int count = 0;
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item.getLevel() >= level) {
            ++count;
         }
      }

      return count;
   }

   public boolean hasASetByLevelAndStar(int level, int star) {
      Iterator var4 = EquipSlot.getEquipCheckSet().iterator();

      Item item;
      do {
         if (!var4.hasNext()) {
            return true;
         }

         Integer slot = (Integer)var4.next();
         item = this.getItemBySlot(slot.intValue());
         if (item == null) {
            return false;
         }

         if (item.getLevel() < level || item.getStarLevel() < star) {
            return false;
         }
      } while(item.getBonusStatSize(3) >= 1);

      return false;
   }

   public boolean hasASetByLevel(int level) {
      Iterator var3 = EquipSlot.getEquipCheckSet().iterator();

      Item item;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         Integer slot = (Integer)var3.next();
         item = this.getItemBySlot(slot.intValue());
         if (item == null) {
            return false;
         }

         if (item.getLevel() != level) {
            return false;
         }
      } while(item.getBonusStatSize(3) >= 1);

      return false;
   }

   public int getExcellCountByLevel(int level) {
      int count = 0;
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         switch(item.getSlot()) {
         case 13:
            break;
         default:
            if (item.getLevel() >= level && item.getBonusStatSize(3) > 0) {
               ++count;
            }
         }
      }

      return count;
   }

   public int getLuckyCountByLevel(int level) {
      int count = 0;
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item.getLevel() >= level) {
            int luckyCount = item.getBonusStatSize(2);
            if (luckyCount > 0) {
               ++count;
            }
         }
      }

      return count;
   }

   public int getVacantSize() {
      return 0;
   }

   public boolean putToContainerBySlot(Item item, int slot) {
      this.equipment.put(slot, item);
      item.setContainerType(0);
      item.setSlot(slot);
      return true;
   }

   public int getType() {
      return 0;
   }

   public boolean isFull() {
      return false;
   }

   public int reduceItemCount(Item item, int count) {
      if (item == null) {
         return 3002;
      } else {
         int result = this.unEquipItem(item.getID(), -1, true);
         if (result == 1) {
            item.setCount(0);
         }

         return result;
      }
   }

   public List getItemsByModelID(int modelID, boolean isBind) {
      List itemList = new ArrayList();
      Iterator var5 = this.equipment.values().iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         if (item.getModelID() == modelID && item.isBind() == isBind) {
            itemList.add(item);
         }
      }

      return itemList;
   }

   public boolean hasItem(long itemID) {
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item.getID() == itemID) {
            return true;
         }
      }

      return false;
   }

   public Item getItemByID(long itemID) {
      Iterator var4 = this.equipment.values().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item.getID() == itemID) {
            return item;
         }
      }

      return null;
   }

   public Item getItemBySlot(int slot) {
      return (Item)this.equipment.get(slot);
   }

   public int getNextSlot() {
      return -1;
   }

   public void moveAwayfromContainer(Item item) {
      int tslot = -1;
      Iterator var4 = this.equipment.keySet().iterator();

      while(var4.hasNext()) {
         Integer slot = (Integer)var4.next();
         Item tmpItem = (Item)this.equipment.get(slot);
         if (tmpItem.getID() == item.getID()) {
            tslot = slot.intValue();
            break;
         }
      }

      this.equipment.remove(tslot);
   }

   public boolean hasItemByModel(int modelID) {
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (item.getModelID() == modelID) {
            return true;
         }
      }

      return false;
   }

   public int getZhuijiaCount() {
      return this.getTotalZhuijia();
   }

   public int getStoneCount() {
      return this.getTotalStoneSize();
   }

   public int getMaxZhuijia() {
      int count = 0;
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (count < item.getZhuijiaLevel()) {
            count = item.getZhuijiaLevel();
         }
      }

      return count;
   }

   public int getMaxStarLevel() {
      int count = 0;
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (count < item.getStarLevel()) {
            count = item.getStarLevel();
         }
      }

      return count;
   }

   public int getTotalSetStatCount() {
      return this.totalSetStatCount;
   }

   public void searchAllExpiredItems(List items) {
      Iterator var3 = this.equipment.values().iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         if (item.isTimeExpired(System.currentTimeMillis())) {
            items.add(item);
         }
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public int getMovementType() {
      return this.movementType;
   }

   public HashMap getRuneSets() {
      return this.runeSets;
   }

   public int getTotalStar() {
      return this.totalStar;
   }

   public void setTotalStar(int totalStar) {
      this.totalStar = totalStar;
   }

   public HashMap getUnEffectMap() {
      return this.unEffectMap;
   }

   public List getRidingStats() {
      return this.ridingStats;
   }

   public boolean isNeedToMend() {
      return this.needToMend;
   }

   public void setNeedToMend(boolean needToMend) {
      this.needToMend = needToMend;
   }

   public int getTotalExcellentCount() {
      return this.totalExcellentCount;
   }

   public void setTotalExcellentCount(int excellentCount) {
      this.totalExcellentCount = excellentCount;
   }

   public HashMap getStoneMap() {
      return this.stoneMap;
   }

   public int getTotalZhuijia() {
      return this.totalZhuijia;
   }

   public void setTotalZhuijia(int totalZhuijia) {
      this.totalZhuijia = totalZhuijia;
   }

   public int getTotalStoneSize() {
      return this.totalStoneSize;
   }

   public void setTotalStoneSize(int totalStoneSize) {
      this.totalStoneSize = totalStoneSize;
   }

   public void destroy() {
      Iterator var2;
      if (this.equipment != null) {
         var2 = this.equipment.values().iterator();

         while(var2.hasNext()) {
            Item item = (Item)var2.next();
            item.destroy();
         }

         this.equipment.clear();
         this.equipment = null;
      }

      if (this.itemStats != null) {
         var2 = this.itemStats.values().iterator();

         while(var2.hasNext()) {
            List stats = (List)var2.next();
            stats.clear();
         }

         this.itemStats.clear();
         this.itemStats = null;
      }

      if (this.sets != null) {
         this.sets.clear();
         this.sets = null;
      }

      if (this.lastChangeSets != null) {
         this.lastChangeSets.clear();
         this.lastChangeSets = null;
      }

      if (this.runeSets != null) {
         this.runeSets.clear();
         this.runeSets = null;
      }

      if (this.unEffectMap != null) {
         this.unEffectMap.clear();
         this.unEffectMap = null;
      }

      if (this.stoneMap != null) {
         this.stoneMap.clear();
         this.stoneMap = null;
      }

      if (this.ridingStats != null) {
         this.ridingStats.clear();
         this.ridingStats = null;
      }

   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType;
      if ($SWITCH_TABLE$com$mu$game$model$item$model$WeaponType != null) {
         return var10000;
      } else {
         int[] var0 = new int[WeaponType.values().length];

         try {
            var0[WeaponType.BothHands.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[WeaponType.MainHand.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[WeaponType.None.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[WeaponType.OneHand.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[WeaponType.SecondaryHand.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$item$model$WeaponType = var0;
         return var0;
      }
   }
}
