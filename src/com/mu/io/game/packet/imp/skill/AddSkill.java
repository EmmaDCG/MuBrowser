package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.levelData.SkillLevelData;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AddSkill extends WriteOnlyPacket {
   public AddSkill(HashMap skillMap) {
      super(30001);

      try {
         this.writeByte(skillMap.size());
         Iterator var3 = skillMap.values().iterator();

         while(var3.hasNext()) {
            Skill skill = (Skill)var3.next();
            writeSkillDetail(skill, this);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      HashMap skillMap = player.getSkillManager().getSkillMap();
      AddSkill as = new AddSkill(skillMap);
      player.writePacket(as);
      as.destroy();
      as = null;
   }

   public static void writeSkillDetail(Skill skill, WriteOnlyPacket packet) {
      try {
         int level = skill.getLevel();
         packet.writeInt(skill.getSkillID());
         packet.writeByte(level);
         int nextLevel = level + 1;
         if (nextLevel > skill.getModel().getMaxLevel()) {
            nextLevel = level;
         }

         SkillLevelData levelData = SkillLevelData.getLevelData(skill.getSkillID(), level);
         SkillLevelData nextLevedaData = SkillLevelData.getLevelData(skill.getSkillID(), nextLevel);
         int consume = 0;
         int money = 0;
         Item needItem = null;
         int nextLevelConsume = 0;
         if (levelData != null) {
            consume = levelData.getConsume(skill.getModel().getConsumeStat());
            consume = skill.getConsumByStat(skill.getModel().getConsumeStat(), consume);
         }

         if (nextLevedaData != null) {
            nextLevelConsume = nextLevedaData.getConsume(skill.getModel().getConsumeStat());
            nextLevelConsume = skill.getConsumByStat(skill.getModel().getConsumeStat(), nextLevelConsume);
            money = nextLevedaData.getMoney();
            needItem = nextLevedaData.getNeedItem();
         }

         packet.writeInt(consume);
         packet.writeInt(nextLevelConsume);
         packet.writeInt(skill.getDistance());
         packet.writeInt(skill.getCoolTime());
         packet.writeInt(skill.getRange());
         packet.writeInt(money);
         packet.writeBoolean(needItem != null);
         if (needItem != null) {
            GetItemStats.writeItem(needItem, packet);
         }

         List dyDatas = skill.getDynamicData(level);
         packet.writeByte(dyDatas.size());
         Iterator var12 = dyDatas.iterator();

         Double data;
         while(var12.hasNext()) {
            data = (Double)var12.next();
            packet.writeUTF(getDyString(data.doubleValue()));
         }

         if (nextLevel != level) {
            dyDatas = skill.getDynamicData(nextLevel);
         }

         packet.writeByte(dyDatas.size());
         var12 = dyDatas.iterator();

         while(var12.hasNext()) {
            data = (Double)var12.next();
            packet.writeUTF(getDyString(data.doubleValue()));
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public static String getDyString(double data) {
      double value = Math.abs(data);
      int tmpValue = (int)value;
      return value - (double)tmpValue > 0.0D ? String.valueOf(data) : String.valueOf((int)data);
   }

   public static void main(String[] args) {
      System.out.println(getDyString(-1.9D));
   }
}
