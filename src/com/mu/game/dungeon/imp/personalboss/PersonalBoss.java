package com.mu.game.dungeon.imp.personalboss;

import com.mu.game.dungeon.Dungeon;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;

public class PersonalBoss extends Dungeon {
   private int bossId;

   public PersonalBoss(int id, PersonalBossTemplate t, int bossId) {
      super(id, t);
      this.bossId = bossId;
   }

   public void initMap() {
      PersonalBossMap map = new PersonalBossMap(this.getBossInfo().getBossData().getMapId(), this);
      this.addMap(map);
   }

   public BossInfo getBossInfo() {
      return ((PersonalBossTemplate)this.getTemplate()).getBossInfo(this.bossId);
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }
}
