package com.mu.io.game.packet.imp.monster;

import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.monster.Monster;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AroundMonster extends WriteOnlyPacket {
   public AroundMonster(Monster monster) {
      super(10300);

      try {
         this.writeShort(1);
         this.writeMonster(monster);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public AroundMonster(List list) {
      super(10300);

      try {
         if (list == null || list.isEmpty()) {
            this.writeShort(0);
            return;
         }

         this.writeShort(list.size());
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            Monster monster = (Monster)var3.next();
            this.writeMonster(monster);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void writeMonster(Monster monster) throws Exception {
      this.writeDouble((double)monster.getID());
      this.writeInt(monster.getTemplateId());
      this.writeUTF(monster.getName());
      this.writeInt(monster.getX());
      this.writeInt(monster.getY());
      this.writeShort(monster.getModelId());
      this.writeByte(monster.getFace()[0]);
      this.writeByte(monster.getFace()[1]);
      this.writeShort(monster.getTemplate().getScale());
      this.writeByte(monster.getBossRank());
      this.writeBoolean(monster.isCanBeAttacked());
      this.writeShort(monster.getTemplate().getAppearMusic());
      this.writeShort(monster.getTemplate().getAttackMusic());
      this.writeShort(monster.getTemplate().getIdleMusic());
      ArrayList list = monster.getTemplate().getEquipEntry();
      this.writeByte(list.size());
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var4.next();
         this.writeByte(entry.getType());
         this.writeShort(entry.getModelID());
         this.writeShort(entry.getEffectID());
      }

      List stats = StatList2Client.getMonsterStats();
      this.writeShort(stats.size());
      Iterator var5 = stats.iterator();

      while(var5.hasNext()) {
         StatEnum stat = (StatEnum)var5.next();
         int value = monster.getStatValue(stat);
         PlayerAttributes.writeStat(stat, (long)value, this);
      }

      AroundPlayer.writeBuffs(monster, true, this);
   }
}
