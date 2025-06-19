package com.mu.game.model.tanxian;

import com.mu.game.model.unit.material.MaterialTemplate;
import java.util.ArrayList;
import java.util.HashMap;

public class ClueInfo {
   private int tanxianLevel = 0;
   private int taskId = 0;
   private int x = 0;
   private int y = 0;
   private String name = "";
   private int templateId = -1;
   private int dunRate = 1;
   private HashMap dunPerRateMap = new HashMap();
   private int mapId = 0;
   private ArrayList unitList = new ArrayList();
   private ArrayList itemList = new ArrayList();

   public int getTanxianLevel() {
      return this.tanxianLevel;
   }

   public void setTanxianLevel(int tanxianLevel) {
      this.tanxianLevel = tanxianLevel;
   }

   public int getTaskId() {
      return this.taskId;
   }

   public void setTaskId(int taskId) {
      this.taskId = taskId;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getTemplateId() {
      return this.templateId;
   }

   public void setTemplateId(int templateId) {
      this.templateId = templateId;
   }

   public int getDunRate() {
      return this.dunRate;
   }

   public void setDunRate(int dunRate) {
      this.dunRate = dunRate;
   }

   public HashMap getDunPerRateMap() {
      return this.dunPerRateMap;
   }

   public void setDunPerRateMap(HashMap dunPerRateMap) {
      this.dunPerRateMap = dunPerRateMap;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public ArrayList getUnitList() {
      return this.unitList;
   }

   public void setUnitList(ArrayList unitList) {
      this.unitList = unitList;
   }

   public MaterialTemplate getTemplate() {
      return MaterialTemplate.getTemplate(this.templateId);
   }

   public int getModelId() {
      return this.getTemplate().getModelId();
   }

   public ArrayList getItemList() {
      return this.itemList;
   }

   public void setItemList(ArrayList itemList) {
      this.itemList = itemList;
   }
}
