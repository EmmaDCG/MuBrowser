package com.mu.game.dungeon.imp.gangboss;

import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.item.Item;
import com.mu.game.model.map.BigMonsterGroup;
import java.util.ArrayList;

public class GangBossGroup extends BigMonsterGroup {
   private int bossId;
   private int header = -1;
   private int mapId;
   private int summonLevel = 100;
   private int summonNumber = 1;
   private int contributionReq = 10;
   private ArrayList dropShowList = new ArrayList();
   private ArrayList drops = new ArrayList();

   public int getBossId() {
      return this.bossId;
   }

   public void setBossId(int bossId) {
      this.bossId = bossId;
   }

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public int getSummonLevel() {
      return this.summonLevel;
   }

   public ArrayList getDropShowList() {
      return this.dropShowList;
   }

   public void addDropShowItem(Item item) {
      this.dropShowList.add(item);
   }

   public void setSummonLevel(int summonLevel) {
      this.summonLevel = summonLevel;
   }

   public int getContributionReq() {
      return this.contributionReq;
   }

   public void setContributionReq(int contributionReq) {
      this.contributionReq = contributionReq;
   }

   public int getSummonNumber() {
      return this.summonNumber;
   }

   public void addDrops(MonsterDrop drop) {
      this.drops.add(drop);
   }

   public ArrayList getDrops() {
      return this.drops;
   }

   public void setSummonNumber(int summonNumber) {
      this.summonNumber = summonNumber;
   }
}
