package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TempleMap extends DungeonMap {
   private HashMap bossMap = new HashMap();

   public TempleMap(Temple d) {
      super(((TempleTemplate)d.getTemplate()).getDefaultMapID(), d);
      this.setDefaultPoint(new Point(((TempleTemplate)d.getTemplate()).getBornX(), ((TempleTemplate)d.getTemplate()).getBornY()));
      this.setCanPk(true);
   }

   public void init() {
      TempleLevel tl = ((Temple)this.getDungeon()).getTempleLevel();
      ArrayList list = tl.getMonsterList();
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         TempleMonsterGroup group = (TempleMonsterGroup)var4.next();
         int num = group.getNum();

         for(int i = 0; i < num; ++i) {
            TempleMonster monster = new TempleMonster(group, this);
            this.addMonster(monster);
         }
      }

      ArrayList bossList = tl.getBossList();
      Iterator var10 = bossList.iterator();

      while(var10.hasNext()) {
         TempleMonsterGroup group = (TempleMonsterGroup)var10.next();
         TempleMonster monster = new TempleMonster(group, this);
         this.addMonster(monster);
         this.bossMap.put(monster.getBossId(), monster);
         monster.setBossRank(2);
      }

   }

   public boolean isPkPunishment() {
      return false;
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      DungeonInfoUpdate packet = this.getSchdulePacket();
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   public void broadcastSchdule() {
      DungeonInfoUpdate packet = this.getSchdulePacket();
      this.broadcastPacket(packet);
      packet.destroy();
      packet = null;
   }

   public TempleMonster getBoss(int bossSn) {
      return (TempleMonster)this.bossMap.get(bossSn);
   }

   public HashMap getBossMap() {
      return this.bossMap;
   }

   public DungeonInfoUpdate getSchdulePacket() {
      DungeonInfoUpdate packet = new DungeonInfoUpdate();

      try {
         packet.writeByte(((TempleTemplate)((Temple)this.getDungeon()).getTemplate()).getTemplateID());
         packet.writeByte(this.bossMap.size());
         Iterator it = this.bossMap.values().iterator();

         while(it.hasNext()) {
            TempleMonster monster = (TempleMonster)it.next();
            packet.writeShort(monster.getLevel());
            packet.writeUTF(monster.getName());
            packet.writeShort(monster.getHeader());
            packet.writeInt(monster.getNextRevivalTime() - 1);
            packet.writeInt(monster.getBornPoint().x);
            packet.writeInt(monster.getBornPoint().y);
         }

         packet.writeUTF(((Temple)this.getDungeon()).getTempleLevel().getPanelDes());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return packet;
   }

   public boolean hasBoss(int bossId) {
      return this.bossMap.containsKey(bossId);
   }

   public Map getSafeRevivalMap() {
      return MapConfig.getDefaultMap(10001);
   }
}
