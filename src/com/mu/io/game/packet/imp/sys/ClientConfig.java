package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.equip.equipSet.EquipSetModel;
import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.item.model.EquipSlot;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.item.model.ItemCurrency;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.model.ItemSort;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.map.MapGroup;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.player.title.TitleInfo;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.vip.VIPInform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

public class ClientConfig extends WriteOnlyPacket {
   public ClientConfig() {
      super(112);
   }

   public static ClientConfig getConfig() {
      ClientConfig pc = new ClientConfig();

      try {
         writeMapGroup(pc);
         writeMapConfig(pc);
         writeWorldLink(pc);
         writeWorldTranspoint(pc);
         writeStatMessage(pc);
         writeItemModel(pc);
         writeItemColor(pc);
         writeItemCurrency(pc);
         writeItemSort(pc);
         writeSkillModel(pc);
         writePkEnum(pc);
         writeBuffModel(pc);
         writeForgingRule(pc);
         VIPInform.writeVIPConfig(pc);
         writeTitleInfo(pc);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return pc;
   }

   private static void writeMapGroup(ClientConfig pc) throws Exception {
      pc.writeByte(MapConfig.getGroupMap().size());
      Iterator it = MapConfig.getGroupMap().values().iterator();

      while(it.hasNext()) {
         MapGroup mg = (MapGroup)it.next();
         pc.writeByte(mg.getId());
         pc.writeUTF(mg.getName());
      }

   }

   private static void writeTitleInfo(ClientConfig pc) throws Exception {
      HashMap map = TitleManager.getTitleInfoMap();
      pc.writeByte(map.size());
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         TitleInfo ti = (TitleInfo)it.next();
         pc.writeByte(ti.getId());
         pc.writeUTF(ti.getName());
         pc.writeShort(ti.getIcon());
         pc.writeByte(ti.getIconType());
         pc.writeUTF(ti.getEquipDes());
         pc.writeUTF(ti.getLightDes());
         pc.writeUTF(ti.getDes());
         pc.writeShort(ti.getSort());
      }

   }

   private static void writeMapConfig(ClientConfig pc) throws Exception {
      pc.writeByte(MapConfig.getAllMapData().size());
      Iterator it = MapConfig.getAllMapData().values().iterator();

      while(it.hasNext()) {
         MapData data = (MapData)it.next();
         pc.writeShort(data.getMapID());
         pc.writeUTF(data.getMapName());
         pc.writeUTF(data.getGroupName());
         pc.writeShort(data.getModelID());
         pc.writeShort(data.getSmallID());
         pc.writeByte(data.getFindWayID());
         pc.writeByte(data.getGroupID());
         pc.writeShort(data.getIcons()[0]);
         pc.writeShort(data.getIcons()[1]);
         pc.writeShort(data.getIcons()[2]);
         pc.writeBoolean(data.getInterMapType() == 2);
         pc.writeShort(data.getBackMusic());
         pc.writeBoolean(data.isShow());
         int[] group = data.getMapGroup();
         if (group != null) {
            pc.writeByte(group.length);

            for(int i = 0; i < group.length; ++i) {
               pc.writeShort(group[i]);
            }
         } else {
            pc.writeByte(0);
         }

         pc.writeShort(data.getRecommendMinLevel());
         pc.writeShort(data.getRecommendLevel());
         pc.writeShort(data.getNameImage());
      }

   }

   private static void writeWorldLink(ClientConfig pc) throws Exception {
      HashMap linkMap = MapConfig.getWorldLinkMap();
      pc.writeByte(linkMap.size());
      Iterator it = linkMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         pc.writeByte(((Integer)entry.getKey()).intValue());
         pc.writeUTF((String)entry.getValue());
      }

   }

   private static void writeWorldTranspoint(ClientConfig pc) throws Exception {
      HashMap tpMap = MapConfig.getWorldPointMap();
      pc.writeByte(tpMap.size());
      Iterator it = tpMap.values().iterator();

      while(it.hasNext()) {
         String[] s = (String[])it.next();
         pc.writeUTF(s[0]);
         pc.writeShort(Integer.parseInt(s[1]));
         pc.writeShort(Integer.parseInt(s[2]));
      }

   }

   private static void writeStatMessage(ClientConfig pc) throws Exception {
      List statList = StatEnum.getStatAndNames();
      pc.writeShort(statList.size());
      Iterator var3 = statList.iterator();

      while(var3.hasNext()) {
         StatEnum stat = (StatEnum)var3.next();
         pc.writeShort(stat.getStatId());
         pc.writeUTF(stat.getName());
         pc.writeUTF(stat.getDes());
      }

   }

   private static void writeItemColor(ClientConfig pc) throws Exception {
      List colors = ItemColor.getAllColors();
      pc.writeByte(colors.size());
      Iterator var3 = colors.iterator();

      while(var3.hasNext()) {
         ItemColor color = (ItemColor)var3.next();
         pc.writeByte(color.getIdentity());
         pc.writeUTF(color.getName());
         pc.writeByte(color.getFont());
      }

   }

   private static void writeItemCurrency(ClientConfig pc) throws Exception {
      HashMap currencies = ItemCurrency.getAllCurrencies();
      pc.writeByte(currencies.size());
      Iterator var3 = currencies.values().iterator();

      while(var3.hasNext()) {
         ItemCurrency currency = (ItemCurrency)var3.next();
         pc.writeByte(currency.getMoneyType());
         pc.writeUTF(currency.getMoneyName());
         pc.writeShort(currency.getIcon());
         pc.writeShort(currency.getStatID());
      }

   }

   private static void writeItemSort(ClientConfig pc) throws Exception {
      HashMap sorts = ItemSort.getItemSorts();
      pc.writeByte(sorts.size());
      Iterator var3 = sorts.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         pc.writeByte(((Integer)entry.getKey()).intValue());
         pc.writeUTF((String)entry.getValue());
      }

      HashMap types = ItemSort.getItemTypes();
      pc.writeByte(types.size());
      Iterator var4 = types.entrySet().iterator();

      while(var4.hasNext()) {
         Entry entry = (Entry)var4.next();
         int typeID = ((Integer)entry.getKey()).intValue();
         String name = (String)entry.getValue();
         pc.writeByte(typeID);
         pc.writeUTF(name);
      }

   }

   protected static void writeEquipSlot(ClientConfig pc) throws Exception {
      HashMap slots = EquipSlot.getAllSlots();
      pc.writeByte(slots.size());
      Iterator var3 = slots.values().iterator();

      while(var3.hasNext()) {
         EquipSlot slot = (EquipSlot)var3.next();
         pc.writeByte(slot.getSlotID());
         pc.writeUTF(slot.getSlotName());
         pc.writeShort(slot.getDefaultIcon());
      }

   }

   protected static void writeEquipSetModel(ClientConfig pc) throws Exception {
      HashMap modelMap = EquipSetModel.getModelMap();
      pc.writeByte(modelMap.size());
      Iterator var3 = modelMap.values().iterator();

      while(var3.hasNext()) {
         EquipSetModel model = (EquipSetModel)var3.next();
         pc.writeByte(model.getSetID());
         pc.writeUTF(model.getName());
         HashSet equipIDs = model.getEquipIDs();
         pc.writeByte(equipIDs.size());
         Iterator var6 = equipIDs.iterator();

         while(var6.hasNext()) {
            Integer equipID = (Integer)var6.next();
            ItemModel im = ItemModel.getModel(equipID.intValue());
            pc.writeUTF(im.getName());
         }

         HashMap statMap = model.getSetStats();
         int statCount = 0;

         Iterator var8;
         ArrayList statLis;
         for(var8 = statMap.values().iterator(); var8.hasNext(); statCount += statLis.size()) {
            statLis = (ArrayList)var8.next();
         }

         pc.writeByte(statCount);
         var8 = statMap.entrySet().iterator();

         while(var8.hasNext()) {
            Entry entry = (Entry)var8.next();
            int effectCount = ((Integer)entry.getKey()).intValue();
            ArrayList modifies = (ArrayList)entry.getValue();
            Iterator var12 = modifies.iterator();

            while(var12.hasNext()) {
               FinalModify modify = (FinalModify)var12.next();
               PlayerAttributes.writeStat(modify.getStat(), (long)modify.getValue(), modify.isPercent(), pc);
               pc.writeByte(effectCount);
            }
         }
      }

   }

   private static void writeItemModel(ClientConfig pc) throws Exception {
      HashMap models = ItemModel.getModelMap();
      pc.writeShort(models.size());
      Iterator var3 = models.values().iterator();

      while(var3.hasNext()) {
         ItemModel model = (ItemModel)var3.next();
         pc.writeInt(model.getID());
         pc.writeUTF(model.getName());
         pc.writeShort(model.getIcon());
         pc.writeShort(model.getSmallIcon());
         pc.writeUTF(model.getDes());
         pc.writeByte(model.getSort());
         pc.writeByte(model.getItemType());
         pc.writeInt(model.getMaxStackCount());
         pc.writeBoolean(model.isCanSplit());
         pc.writeBoolean(model.isCanBatch());
         pc.writeByte(model.getGender());
         HashSet professtion = model.getProfession();
         pc.writeByte(professtion.size());
         Iterator it = professtion.iterator();

         while(it.hasNext()) {
            Integer pro = (Integer)it.next();
            pc.writeByte(pro.intValue());
         }

         pc.writeShort(model.getUseLevel());
         pc.writeByte(model.getLevel());
         pc.writeUTF(model.getProfessionStr());
         pc.writeShort(model.getExternal3D());
         pc.writeByte(model.getCompareType());
         pc.writeBoolean(model.isNumbericType());
         SortedMap needMap = model.getNeedBasicPro();
         pc.writeByte(needMap.size());
         it = needMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            pc.writeShort(((StatEnum)entry.getKey()).getStatId());
            pc.writeInt(((Integer)entry.getValue()).intValue());
         }

         pc.writeShort(model.getMusicID());
         pc.writeBoolean(model.isCanRepair());
         pc.writeBoolean(model.isEquipSet());
         pc.writeBoolean(model.isDirectly());
      }

   }

   private static void writeSkillModel(ClientConfig pc) throws Exception {
      HashMap models = SkillModel.getAllModels();
      pc.writeShort(models.size());

      SkillModel model;
      for(Iterator var3 = models.values().iterator(); var3.hasNext(); pc.writeUTF(model.getActiveDes())) {
         model = (SkillModel)var3.next();
         pc.writeInt(model.getSkillId());
         pc.writeUTF(model.getName());
         pc.writeByte(model.getGroupType());
         pc.writeByte(model.getStatusType());
         pc.writeByte(model.getDatum());
         pc.writeShort(model.getIconId());
         pc.writeShort(model.getSmallIconId());
         pc.writeByte(model.getMaxLevel());
         pc.writeShort(model.getSkillEffect());
         pc.writeShort(model.getReleaseEffect());
         pc.writeShort(model.getTargetEffect());
         pc.writeShort(model.getOrder());
         pc.writeUTF(model.getDes());
         pc.writeShort(model.getConsumeStat().getStatId());
         pc.writeBoolean(model.getActiveItem() != null);
         if (model.getActiveItem() != null) {
            GetItemStats.writeItem(model.getActiveItem(), pc);
         }
      }

   }

   private static void writeBuffModel(ClientConfig pc) throws Exception {
      HashMap models = BuffModel.getModels();
      pc.writeShort(models.size());
      Iterator var3 = models.values().iterator();

      while(var3.hasNext()) {
         BuffModel model = (BuffModel)var3.next();
         pc.writeInt(model.getModelID());
         pc.writeUTF(model.getName());
         pc.writeShort(model.getIcon());
         pc.writeUTF(model.getDes());
         pc.writeBoolean(model.isDebuff());
         pc.writeShort(model.getOrder());
         pc.writeShort(model.getEffecteID());
         pc.writeBoolean(model.isShow());
         pc.writeBoolean(model.isClickFunction());
      }

   }

   private static void writeForgingRule(ClientConfig pc) throws Exception {
      pc.writeByte(RuneForgingData.getMinStarLevel());
   }

   private static void writeStoneSet(ClientConfig pc) throws Exception {
      pc.writeByte(0);
   }

   private static void writePkEnum(ClientConfig pc) throws Exception {
      List pks = PkEnum.getAllPkEnums();
      pc.writeByte(pks.size());
      Iterator var3 = pks.iterator();

      while(var3.hasNext()) {
         PkEnum pk = (PkEnum)var3.next();
         pc.writeByte(pk.getModeID());
         pc.writeUTF(pk.getName());
         pc.writeShort(pk.getIcon());
         pc.writeUTF(pk.getDes());
      }

   }
}
