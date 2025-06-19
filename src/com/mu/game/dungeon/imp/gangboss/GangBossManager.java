package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.utils.Tools;
import java.util.concurrent.ConcurrentHashMap;

public class GangBossManager {
   private GangBossTemplate template;
   private ConcurrentHashMap dunMap = Tools.newConcurrentHashMap2();

   public GangBossManager(GangBossTemplate t) {
      this.template = t;
   }

   public GangBoss getGangBoss(long gangId) {
      return (GangBoss)this.dunMap.get(gangId);
   }

   public GangBoss summonBoss(long gangId, int bossId) {
      GangBossGroup gb = this.template.getBossInfo(bossId);
      GangBoss gangBoss = new GangBoss(DungeonManager.getID(), this.template, gb, gangId);
      DungeonManager.addDungeon(gangBoss);
      gangBoss.init();
      this.dunMap.put(gangId, gangBoss);
      return gangBoss;
   }

   public static GangBossManager getManager() {
      GangBossTemplate template = (GangBossTemplate)DungeonTemplateFactory.getTemplate(10);
      return template.getManager();
   }

   public GangBoss removeGangBoss(long gangId) {
      return (GangBoss)this.dunMap.remove(gangId);
   }
}
