package com.mu.game.dungeon.imp.molian;

import com.mu.game.dungeon.DungeonLevel;
import java.util.ArrayList;

public class MoLianleLevel extends DungeonLevel {
   private int contribution;
   private String name;
   private int mapId;
   private int defaultX;
   private int defaultY;
   private ArrayList monsterList = new ArrayList();

   public int getContribution() {
      return this.contribution;
   }

   public void setContribution(int contribution) {
      this.contribution = contribution;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void addMonsterGroup(MoLianMonsterGroup group) {
      this.monsterList.add(group);
   }

   public ArrayList getMonsterList() {
      return this.monsterList;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public int getDefaultX() {
      return this.defaultX;
   }

   public void setDefaultX(int defaultX) {
      this.defaultX = defaultX;
   }

   public int getDefaultY() {
      return this.defaultY;
   }

   public void setDefaultY(int defaultY) {
      this.defaultY = defaultY;
   }
}
