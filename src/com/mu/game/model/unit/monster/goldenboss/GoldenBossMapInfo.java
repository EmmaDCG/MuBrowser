package com.mu.game.model.unit.monster.goldenboss;

import java.util.ArrayList;

public class GoldenBossMapInfo {
   private int mapId;
   private String des;
   private String replaceStr = "";
   private int bossIcon = -1;
   private ArrayList dropList = null;
   private int reqLevel;
   private GoldenBossData smallBossData;
   private GoldenBossData bigBossData;

   public GoldenBossMapInfo(int mapId) {
      this.mapId = mapId;
   }

   public int getMapId() {
      return this.mapId;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public String getReplaceStr() {
      return this.replaceStr;
   }

   public void setReplaceStr(String replaceStr) {
      this.replaceStr = replaceStr;
   }

   public int getBossIcon() {
      return this.bossIcon;
   }

   public void setBossIcon(int bossIcon) {
      this.bossIcon = bossIcon;
   }

   public ArrayList getDropList() {
      return this.dropList;
   }

   public void setDropList(ArrayList dropList) {
      this.dropList = dropList;
   }

   public int getReqLevel() {
      return this.reqLevel;
   }

   public void setReqLevel(int reqLevel) {
      this.reqLevel = reqLevel;
   }

   public GoldenBossData getSmallBossData() {
      return this.smallBossData;
   }

   public void setSmallBossData(GoldenBossData smallBossData) {
      this.smallBossData = smallBossData;
   }

   public GoldenBossData getBigBossData() {
      return this.bigBossData;
   }

   public void setBigBossData(GoldenBossData bigBossData) {
      this.bigBossData = bigBossData;
   }
}
