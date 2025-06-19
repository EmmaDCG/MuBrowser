package com.mu.game.dungeon.imp.personalboss;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import java.awt.Point;
import java.util.Iterator;

public class PersonalBossMap extends DungeonMap implements MonsterDie {
   private boolean hasEnter = false;

   public PersonalBossMap(int referMapID, PersonalBoss d) {
      super(referMapID, d);
      this.setDefaultPoint(new Point(d.getBossInfo().getX(), d.getBossInfo().getY()));
      this.createBoss();
   }

   private void createBoss() {
      BossInfo info = ((PersonalBoss)this.getDungeon()).getBossInfo();
      Boss boss = new Boss(info, this);
      this.addMonster(boss);
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);

      try {
         BossInfo info = ((PersonalBoss)this.getDungeon()).getBossInfo();
         DungeonInfoUpdate du = new DungeonInfoUpdate();
         du.writeByte(7);
         du.writeUTF(info.getBossData().getName());
         player.writePacket(du);
         du.destroy();
         du = null;
         ArrowGuideManager.pushArrow(player, 21, (String)null);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void monsterBeKilled(Boss monster) {
      monster.setShouldDestroy(true);
      ((PersonalBoss)this.getDungeon()).setComplete(true);
      ((PersonalBoss)this.getDungeon()).setSuccess(true);
      Iterator it = this.playerMap.values().iterator();

      while(it.hasNext()) {
         ArrowGuideManager.pushArrow((Player)it.next(), 22, (String)null);
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
