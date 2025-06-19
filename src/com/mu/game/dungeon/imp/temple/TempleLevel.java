package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.DungeonLevel;
import java.util.ArrayList;

public class TempleLevel extends DungeonLevel {
   private String panelDes;
   private ArrayList monsterList = new ArrayList();
   private ArrayList bossList = new ArrayList();

   public String getPanelDes() {
      return this.panelDes;
   }

   public void setPanelDes(String panelDes) {
      this.panelDes = panelDes;
   }

   public ArrayList getMonsterList() {
      return this.monsterList;
   }

   public ArrayList getBossList() {
      return this.bossList;
   }

   public void addMonster(TempleMonsterGroup gp) {
      this.monsterList.add(gp);
   }

   public void addBoss(TempleMonsterGroup gp) {
      this.bossList.add(gp);
   }
}
