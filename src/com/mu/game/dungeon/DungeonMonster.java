package com.mu.game.dungeon;

import com.mu.game.IDFactory;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.BigMonsterGroup;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.monster.MonsterStar;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.utils.Rnd;
import java.awt.Point;
import java.util.Iterator;

public class DungeonMonster extends Monster {
   public DungeonMonster(BigMonsterGroup md, DungeonMap map) {
      super(IDFactory.getTemporaryID(), map);
      this.setAttackDistance(md.getAttackDist());
      this.setLevel(Rnd.get(md.getMinLevel(), md.getMaxLevel()));
      if (md.getNum() > 1) {
         int minX = md.getX() - md.getBornRadius();
         int maxX = md.getX() + md.getBornRadius();
         int minY = md.getY() - md.getBornRadius();
         int maxY = md.getY() + md.getBornRadius();
         this.setBornPoint(map.searchFeasiblePoint(Rnd.get(minX, maxX), Rnd.get(minY, maxY)));
      } else {
         this.setBornPoint(new Point(md.getX(), md.getY()));
      }

      if (md.getFace() != null) {
         this.setFace(md.getFace()[0], md.getFace()[1]);
      } else {
         this.setFace(Rnd.get(-100, 100), Rnd.get(-100, 100));
      }

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
      MonsterStar monsterStar = map.getDungeon().getTemplate().getMonsterStar(md.getStar(), this.getLevel());
      this.setRewardExp(monsterStar.getExp());
      this.getProperty().inits(monsterStar.getHp(), monsterStar.getMp(), monsterStar.getMinAtt(), monsterStar.getMaxAtt(), monsterStar.getDef(), monsterStar.getHit(), monsterStar.getAvd(), (int)md.getSpeed(), monsterStar.getOtherStats());
      this.setHp(monsterStar.getHp());
      this.setMp(monsterStar.getMp());
      this.addDrops(monsterStar.getDrops());
      if (md.getSkillList() != null) {
         Iterator var9 = md.getSkillList().iterator();

         while(var9.hasNext()) {
            Integer skillID = (Integer)var9.next();
            SkillDBEntry entry = new SkillDBEntry(skillID.intValue(), 1, 0, false, 0);
            this.getSkillManager().loadSkill(entry);
         }
      }

      this.setAI(md.getAi());
   }
}
