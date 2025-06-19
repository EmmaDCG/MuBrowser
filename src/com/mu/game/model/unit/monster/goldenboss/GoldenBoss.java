package com.mu.game.model.unit.monster.goldenboss;

import com.mu.game.CenterManager;
import com.mu.game.IDFactory;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.utils.Rnd;
import com.mu.utils.Tools;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GoldenBoss extends Monster {
   private ArrayList dropList;
   private ArrayList lastAttackDropList;
   private boolean hasDoLastAttackDrop = false;
   private int goldenType;
   private ConcurrentHashMap actorMap = Tools.newConcurrentHashMap2();

   public GoldenBoss(GoldenBossData md, Map map, int[] position) {
      super(IDFactory.getTemporaryID(), map);
      this.setAttackDistance(md.getAttackDist());
      this.setLevel(md.getMinLevel());
      this.setBornPoint(new Point(position[0], position[1]));
      this.setFace(Rnd.get(-100, 100), Rnd.get(-100, 100));
      this.setMaxMoveDistance(md.getMaxMoveDist());
      this.setMinAttackDistance(md.getMinAttackDist());
      this.setModelId(md.getModelId());
      this.setMoveRadius(md.getMoveRadius());
      this.setName(md.getName());
      this.setPosition(this.getBornPoint());
      this.setRevivalTime(md.getRevivalTime());
      this.setSearchDistance(md.getSerachDist());
      this.setTemplateId(md.getTemplateId());
      this.setBossRank(md.getBossRank());
      MonsterStar monsterStar = MonsterStar.getMonsterStar(md.getStar(), md.getMinLevel());
      this.setRewardExp(monsterStar.getExp());
      this.getProperty().inits(monsterStar.getHp(), monsterStar.getMp(), monsterStar.getMinAtt(), monsterStar.getMaxAtt(), monsterStar.getDef(), monsterStar.getHit(), monsterStar.getAvd(), (int)md.getSpeed(), monsterStar.getOtherStats());
      this.setHp(monsterStar.getHp());
      this.setMp(monsterStar.getMp());
      if (md.getSkillList() != null) {
         Iterator var6 = md.getSkillList().iterator();

         while(var6.hasNext()) {
            Integer skillID = (Integer)var6.next();
            SkillDBEntry entry = new SkillDBEntry(skillID.intValue(), 1, 0, false, 0);
            this.getSkillManager().loadSkill(entry);
         }
      }

      this.setAI(md.getAi());
      this.goldenType = md.getGoldenBossType();
      this.dropList = md.getDrops();
      this.lastAttackDropList = md.getLastAttackDrops();
   }

   public void dropItem(Player player, int delay) {
      if (this.goldenType == 1) {
         super.dropItem(player, delay);
      } else {
         super.dropItem(player, delay);
         this.hasDoLastAttackDrop = true;
         Iterator it = this.actorMap.keySet().iterator();

         while(it.hasNext()) {
            long rid = ((Long)it.next()).longValue();
            Player p = CenterManager.getPlayerByRoleID(rid);
            if (p != null) {
               SystemDropManager.dropWhenMonsterBeKill(this, p, delay, true);
            }
         }
      }

   }

   public boolean beAttacked(Creature attacker, AttackResult result) {
      boolean b = super.beAttacked(attacker, result);
      Player player = null;
      if (attacker.getUnitType() == 1) {
         player = (Player)attacker;
      } else if (attacker.getUnitType() == 4) {
         player = ((Pet)attacker).getOwner();
      }

      if (player != null) {
         this.actorMap.put(player.getID(), true);
      }

      return b;
   }

   public void destroy() {
      super.destroy();
      this.actorMap.clear();
   }

   public List getDrops() {
      return this.goldenType == 2 && !this.hasDoLastAttackDrop ? this.lastAttackDropList : this.dropList;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
   }
}
