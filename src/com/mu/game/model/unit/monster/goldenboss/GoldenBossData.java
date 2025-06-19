package com.mu.game.model.unit.monster.goldenboss;

import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.map.BigMonsterGroup;
import java.util.ArrayList;

public class GoldenBossData extends BigMonsterGroup {
   private int mapId;
   private int goldenBossType = 1;
   private ArrayList pointList = new ArrayList();
   private ArrayList drops = new ArrayList();
   private ArrayList lastAttackDrops = new ArrayList();

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public int getGoldenBossType() {
      return this.goldenBossType;
   }

   public void setGoldenBossType(int goldenBossType) {
      this.goldenBossType = goldenBossType;
   }

   public void addPoint(int[] in) {
      this.pointList.add(in);
   }

   public void addDrops(MonsterDrop drop) {
      this.drops.add(drop);
   }

   public ArrayList getDrops() {
      return this.drops;
   }

   public void addLastAttackDrops(MonsterDrop drop) {
      this.lastAttackDrops.add(drop);
   }

   public ArrayList getLastAttackDrops() {
      return this.lastAttackDrops;
   }
}
