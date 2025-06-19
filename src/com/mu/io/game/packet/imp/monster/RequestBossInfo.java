package com.mu.io.game.packet.imp.monster;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.personalboss.BossInfo;
import com.mu.game.dungeon.imp.personalboss.PersonalBossTemplate;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.monster.worldboss.WorldBoss;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RequestBossInfo extends ReadAndWritePacket {
   public RequestBossInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RequestBossInfo() {
      super(10302, (byte[])null);
   }

   public static void writeInfo(Player player) {
      try {
         RequestBossInfo pi = new RequestBossInfo();
         writeInfo(player, pi);
         pi.destroy();
         pi = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void writeInfo(Player player, WriteOnlyPacket packet) throws Exception {
      ArrayList canKillList = new ArrayList();
      ArrayList worldBossList = new ArrayList();
      ArrayList towerList = new ArrayList();
      ArrayList personalList = new ArrayList();
      PersonalBossTemplate template = (PersonalBossTemplate)DungeonTemplateFactory.getTemplate(7);
      HashMap map = WorldBossManager.getBossDataMap();
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         WorldBossData data = (WorldBossData)it.next();
         if (data.getBossType() == 3) {
            BossInfo info = template.getBossInfo(data.getBossId());
            if (info != null) {
               personalList.add(info);
            }
         } else {
            if (data.getBossType() == 1) {
               worldBossList.add(data);
            } else {
               towerList.add(data);
            }

            WorldBoss boss = WorldBossManager.getBoss(data.getBossId());
            long nextTime = (long)boss.getNextRevivalTime();
            if (nextTime <= 0L) {
               canKillList.add(data);
            }
         }
      }

      ArrayList groupList = WorldBossManager.getGroupList();
      packet.writeByte(4);
      Iterator var16 = groupList.iterator();

      while(var16.hasNext()) {
         String[] s = (String[])var16.next();
         int groupId = Integer.parseInt(s[0]);
         String name = s[1];
         packet.writeByte(groupId);
         packet.writeUTF(name);
         switch(groupId) {
         case 0:
            writeWorldBoss(packet, canKillList, player);
            break;
         case 1:
            writeWorldBoss(packet, worldBossList, player);
            break;
         case 2:
            writeWorldBoss(packet, towerList, player);
            break;
         case 3:
            writePersonalBoss(packet, personalList, player);
         }
      }

      player.writePacket(packet);
      canKillList.clear();
      canKillList = null;
      worldBossList.clear();
      worldBossList = null;
      towerList.clear();
      towerList = null;
      personalList.clear();
      personalList = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      writeInfo(player, this);
   }

   public static void writeWorldBoss(WriteOnlyPacket packet, ArrayList list, Player player) throws Exception {
      packet.writeByte(list.size());
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         WorldBossData data = (WorldBossData)var4.next();
         writeWorldBossDetail(data, packet, player);
      }

   }

   public static void writeWorldBossDetail(WorldBossData data, WriteOnlyPacket packet, Player player) throws Exception {
      packet.writeShort(data.getBossId());
      packet.writeUTF(data.getName());
      packet.writeShort(data.getModelId());
      packet.writeByte(data.getZoom());
      packet.writeShort(data.getMinLevel());
      packet.writeShort(data.getReqLevel());
      packet.writeUTF(data.getMapName());
      packet.writeBoolean(false);
      ArrayList dropList = data.getDropList();
      packet.writeByte(dropList.size());
      Iterator var5 = dropList.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(0);
      packet.writeByte(0);
      WorldBoss boss = WorldBossManager.getBoss(data.getBossId());
      long nextTime = boss.getNextRevivalTimeLong();
      if (nextTime <= 0L) {
         packet.writeBoolean(true);
      } else {
         packet.writeBoolean(false);
         packet.writeDouble((double)nextTime);
      }

      ArrayList list = CreatureTemplate.getTemplate(data.getTemplateId()).getEquipEntry();
      packet.writeByte(list.size());
      Iterator var9 = list.iterator();

      while(var9.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var9.next();
         packet.writeByte(entry.getType());
         packet.writeShort(entry.getModelID());
         packet.writeShort(entry.getEffectID());
      }

   }

   public static void writePersonalBoss(WriteOnlyPacket packet, ArrayList list, Player player) throws Exception {
      packet.writeByte(list.size());
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         BossInfo info = (BossInfo)var4.next();
         writePersonalBossData(info, packet, player);
      }

   }

   public static void writePersonalBossData(BossInfo info, WriteOnlyPacket packet, Player player) throws Exception {
      WorldBossData data = info.getBossData();
      packet.writeShort(data.getBossId());
      packet.writeUTF(data.getName());
      packet.writeShort(data.getModelId());
      packet.writeByte(data.getZoom());
      packet.writeShort(data.getMinLevel());
      packet.writeShort(info.getLevelLimit());
      packet.writeUTF(data.getMapName());
      Item ticket = info.getTicketItem();
      if (ticket == null) {
         packet.writeBoolean(false);
      } else {
         packet.writeBoolean(true);
         GetItemStats.writeItem(ticket, packet);
      }

      ArrayList dropList = data.getDropList();
      packet.writeByte(dropList.size());
      Iterator var7 = dropList.iterator();

      while(var7.hasNext()) {
         Item item = (Item)var7.next();
         GetItemStats.writeItem(item, packet);
      }

      DunLogs log = player.getDunLogsManager().getLog(7, data.getBossId());
      packet.writeByte(log == null ? 0 : log.getFinishTimes());
      packet.writeByte(info.getPlayerTimeLimit(player));
      packet.writeBoolean(true);
      ArrayList list = CreatureTemplate.getTemplate(data.getTemplateId()).getEquipEntry();
      packet.writeByte(list.size());
      Iterator var9 = list.iterator();

      while(var9.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var9.next();
         packet.writeByte(entry.getType());
         packet.writeShort(entry.getModelID());
         packet.writeShort(entry.getEffectID());
      }

   }
}
