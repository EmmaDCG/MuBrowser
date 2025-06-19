package com.mu.game.model.rewardhall.vitality;

import com.mu.utils.CommonRegPattern;
import java.util.regex.Matcher;

public class VitalityTaskData {
   private int id;
   private String name;
   private int vitality;
   private VitalityTaskType targetType;
   private int targetId;
   private int targetMaxRate;
   private boolean hasEnter;
   private int enterFunction;
   private int enterType;
   private int enterPanelId1;
   private int enterPanelId2;

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

   public int getVitality() {
      return this.vitality;
   }

   public void setVitality(int vitality) {
      this.vitality = vitality;
   }

   public void setTargetStr(String targetStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(targetStr);
      m.find();
      this.targetType = VitalityTaskType.valueOf(Integer.parseInt(m.group()));
      m.find();
      this.targetId = Integer.parseInt(m.group());
      m.find();
      this.targetMaxRate = Integer.parseInt(m.group());
   }

   public void setEnterStr(String enterStr) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(enterStr);
      m.find();
      this.enterType = Integer.parseInt(m.group());
      switch(this.enterType) {
      case 1:
         m.find();
         this.enterPanelId1 = Integer.parseInt(m.group());
         m.find();
         this.enterPanelId2 = Integer.parseInt(m.group());
      default:
      }
   }

   public VitalityTaskType getTargetType() {
      return this.targetType;
   }

   public int getTargetId() {
      return this.targetId;
   }

   public int getTargetMaxRate() {
      return this.targetMaxRate;
   }

   public boolean isHasEnter() {
      return this.hasEnter;
   }

   public void setHasEnter(boolean hasEnter) {
      this.hasEnter = hasEnter;
   }

   public int getEnterFunction() {
      return this.enterFunction;
   }

   public void setEnterFunction(int enterFunction) {
      this.enterFunction = enterFunction;
   }

   public int getEnterType() {
      return this.enterType;
   }

   public int getEnterPanelId1() {
      return this.enterPanelId1;
   }

   public int getEnterPanelId2() {
      return this.enterPanelId2;
   }
}
