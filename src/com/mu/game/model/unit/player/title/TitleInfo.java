package com.mu.game.model.unit.player.title;

import java.util.ArrayList;
import java.util.HashSet;

public class TitleInfo {
   private int id;
   private String name;
   private int icon;
   private int iconType = 1;
   private String equipDes = "";
   private String lightDes = "";
   private String des = "";
   private ArrayList equipAttr = new ArrayList();
   private ArrayList lightAttr = new ArrayList();
   private long activeTime = -1L;
   private int sort = 0;
   private int titleType = 1;
   private HashSet mutexSet = new HashSet();
   private VipRequirement gearRequirement = null;
   private boolean saveDB = true;
   private int zdl = 0;
   private int equipZdl = 0;

   public TitleInfo(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getIconType() {
      return this.iconType;
   }

   public void setIconType(int iconType) {
      this.iconType = iconType;
   }

   public String getEquipDes() {
      return this.equipDes;
   }

   public int getZdl() {
      return this.zdl;
   }

   public void setZdl(int zdl) {
      this.zdl = zdl;
   }

   public int getEquipZdl() {
      return this.equipZdl;
   }

   public void setEquipZdl(int equipZdl) {
      this.equipZdl = equipZdl;
   }

   public void setEquipDes(String equipDes) {
      this.equipDes = equipDes;
   }

   public String getLightDes() {
      return this.lightDes;
   }

   public void setLightDes(String lightDes) {
      this.lightDes = lightDes;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public ArrayList getEquipAttr() {
      return this.equipAttr;
   }

   public ArrayList getLightAttr() {
      return this.lightAttr;
   }

   public long getActiveTime() {
      return this.activeTime;
   }

   public void setActiveTime(long activeTime) {
      this.activeTime = activeTime;
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public int getTitleType() {
      return this.titleType;
   }

   public void setTitleType(int titleType) {
      this.titleType = titleType;
   }

   public VipRequirement getGearRequirement() {
      return this.gearRequirement;
   }

   public void setGearRequirement(VipRequirement gearRequirement) {
      this.gearRequirement = gearRequirement;
   }

   public HashSet getMutexSet() {
      return this.mutexSet;
   }

   public boolean isSaveDB() {
      return this.saveDB;
   }

   public void setSaveDB(boolean saveDB) {
      this.saveDB = saveDB;
   }

   public void addMutexId(int id) {
      this.mutexSet.add(id);
   }
}
