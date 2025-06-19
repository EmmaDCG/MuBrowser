package com.mu.game.dungeon.imp.personalboss;

import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;

public class Boss extends DungeonMonster {
   public Boss(BossInfo info, PersonalBossMap map) {
      super(info.getBossData(), map);
      this.setBossRank(2);
      this.setRevivalTime(-1L);
   }

   public PersonalBossMap getPersonalBossMap() {
      return (PersonalBossMap)this.getMap();
   }

   public boolean hasDropPunish() {
      return false;
   }

   public int decreaseHp(Creature attacker, AttackResult result) {
      return ((PersonalBoss)this.getPersonalBossMap().getDungeon()).isComplete() ? 0 : super.decreaseHp(attacker, result);
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      this.getPersonalBossMap().monsterBeKilled(this);
      Player player = null;
      if (attacker.getUnitType() == 1) {
         player = (Player)attacker;
      } else if (attacker.getUnitType() == 4) {
         player = ((Pet)attacker).getOwner();
      }

      if (player != null) {
         player.getTaskManager().onEventCheckCount(TargetType.CountType.KillPersonalBoss);
      }

   }
}
