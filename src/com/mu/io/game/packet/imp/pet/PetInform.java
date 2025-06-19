package com.mu.io.game.packet.imp.pet;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.map.Map;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.pet.PetAttribute;
import com.mu.game.model.pet.PetAttributeData;
import com.mu.game.model.pet.PetConfigManager;
import com.mu.game.model.pet.PetPropertyData;
import com.mu.game.model.pet.PetRank;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.manager.SkillFactory;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.skill.AddSkill;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PetInform extends WriteOnlyPacket {
   public PetInform(int code) {
      super(code);
   }

   public static void sendMsgConfig(Player player, boolean open, int r) {
      try {
         PetInform packet = new PetInform(43002);
         packet.writeByte(PetConfigManager.getRankSize());

         PetRank rank;
         int level;
         PetPropertyData propertyData;
         for(rank = PetConfigManager.getRankHead(); rank != null; rank = rank.getNext()) {
            packet.writeByte(rank.getRank());
            packet.writeUTF(rank.getName());
            packet.writeShort(rank.getModel());
            packet.writeShort(rank.getIcon());
            packet.writeByte(rank.getScale());
            level = player.getPetManager().getLevel();
            propertyData = rank.getPropertyData(level);
            packet.writeInt(propertyData.getPetZDL());
            packet.writeInt(rank.getPlayerZDL());
            Skill skill = SkillFactory.createSkill(rank.getSkill(), 1, player);
            AddSkill.writeSkillDetail(skill, packet);
            packet.writeShort(rank.getRiseLevel());
            ArrayList list = CreatureTemplate.getTemplate(rank.getTemplateId()).getEquipEntry();
            packet.writeByte(list.size());
            Iterator var10 = list.iterator();

            while(var10.hasNext()) {
               ExternalEntry entry = (ExternalEntry)var10.next();
               packet.writeByte(entry.getType());
               packet.writeShort(entry.getModelID());
               packet.writeShort(entry.getEffectID());
            }
         }

         rank = PetConfigManager.getRankHead();
         level = player.getPetManager().getLevel();
         propertyData = rank.getPropertyData(level);
         LinkedHashMap propertyList = propertyData.getPropertyList();
         packet.writeShort(propertyList.size());
         Iterator it = propertyList.values().iterator();

         while(it.hasNext()) {
            FinalModify att = (FinalModify)it.next();
            PlayerAttributes.writeStat(att.getStat(), (long)att.getValue(), att.isPercent(), packet);
         }

         packet.writeShort(1);
         PlayerAttributes.writeStat(StatEnum.DOMINEERING, (long)propertyData.getPetZDL(), false, packet);
         LinkedHashMap propertiesList = rank.getProperties();
         packet.writeShort(propertiesList.size() + PetConfigManager.getAttributeSize());
         it = propertiesList.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            PlayerAttributes.writeStat((StatEnum)entry.getKey(), (long)((Integer)entry.getValue()).intValue(), false, packet);
         }

         it = PetConfigManager.getAttributeIterator();

         PetAttributeData data;
         while(it.hasNext()) {
            data = (PetAttributeData)it.next();
            PlayerAttributes.writeStat(data.getStat(), (long)data.getFirstLevel().getAttributeValue(), false, packet);
         }

         packet.writeShort(1);
         PlayerAttributes.writeStat(StatEnum.DOMINEERING, (long)rank.getPlayerZDL(), false, packet);
         packet.writeByte(PetConfigManager.getAttributeSize());
         it = PetConfigManager.getAttributeIterator();

         while(it.hasNext()) {
            data = (PetAttributeData)it.next();
            packet.writeShort(data.getStat().getStatId());
            packet.writeUTF(data.getOpenDesc());
         }

         packet.writeBoolean(open);
         packet.writeByte(r);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var11) {
         var11.printStackTrace();
      }

   }

   public static void sendMsgState(Player player, boolean show, Pet pet) {
      try {
         PetInform packet = new PetInform(43003);
         packet.writeByte(pet.getRank());
         packet.writeBoolean(show);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendPetProperties(Player player, Pet pet, StatEnum... statEnums) {
      try {
         PetInform packet = new PetInform(43004);
         packet.writeShort(statEnums.length);

         for(int i = 0; i < statEnums.length; ++i) {
            PlayerAttributes.writeStat(statEnums[i], pet.getArributeValue(statEnums[i]), packet);
         }

         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendPetProperties2(Player player, ConcurrentHashMap joinAttributeMap) {
      try {
         PetInform packet = new PetInform(43005);
         packet.writeShort(joinAttributeMap.size());
         Iterator it = joinAttributeMap.keySet().iterator();

         while(it.hasNext()) {
            StatEnum se = (StatEnum)it.next();
            PlayerAttributes.writeStat(se, (long)((Integer)joinAttributeMap.get(se)).intValue(), packet);
         }

         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendPetLevel(Player player, int level, long exp, long maxExp) {
      try {
         PetInform packet = new PetInform(43016);
         packet.writeShort(level);
         packet.writeDouble((double)exp);
         packet.writeDouble((double)maxExp);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public static void sendPetLevelEffect(Player player) {
      try {
         PetInform packet = new PetInform(43014);
         packet.writeBoolean(true);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void sendPetLevelBroadcast(Player player, Pet pet) {
      try {
         Map map = pet.getMap();
         if (map != null) {
            PetInform packet = new PetInform(43015);
            packet.writeDouble((double)pet.getID());
            map.sendPacketToAroundPlayer(packet, player, true);
            packet.destroy();
            packet = null;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendMsgAttributeOnLogin(Player player, java.util.Map attributeMap, PetRank rank) {
      try {
         PetInform packet = new PetInform(43008);
         packet.writeByte(PetConfigManager.getAttributeSize());
         Iterator iterator = PetConfigManager.getAttributeIterator();

         while(iterator.hasNext()) {
            PetAttributeData data = (PetAttributeData)iterator.next();
            PetAttribute attribute = (PetAttribute)attributeMap.get(data.getStat());
            boolean active = attribute != null;
            PetAttributeData.Level level = active ? attribute.getLevelData() : data.getFirstLevel();
            packet.writeShort(data.getStat().getStatId());
            packet.writeInt(active ? level.getLevel() : 0);
            packet.writeInt(rank.getAttributeLimit(data));
            GetItemStats.writeItem(level.getExpendItem().getItem(), packet);
            packet.writeUTF(level.getAttributeStr());
            packet.writeBoolean(active);
         }

         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static void sendMsgAttribute(Player player, PetAttribute attribute, PetRank rank) {
      try {
         PetInform packet = new PetInform(43008);
         packet.writeByte(1);
         PetAttributeData data = attribute.getData();
         PetAttributeData.Level level = attribute.getLevelData();
         packet.writeShort(data.getStat().getStatId());
         packet.writeInt(attribute.getLevel());
         packet.writeInt(rank.getAttributeLimit(data));
         GetItemStats.writeItem(level.getExpendItem().getItem(), packet);
         packet.writeUTF(level.getAttributeStr());
         packet.writeBoolean(true);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void sendMsgRiseResult(Player player, boolean autoRise, boolean riseSuccess) {
      try {
         PetInform packet = new PetInform(43010);
         packet.writeBoolean(autoRise);
         packet.writeBoolean(riseSuccess);
         player.writePacket(packet);
         packet.destroy();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
