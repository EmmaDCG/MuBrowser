package com.mu.game.model.unit.skill.model;

public class SkillDBEntry {
   private int skillID;
   private int level;
   private int remainThawTime;
   private boolean selected;
   private int passiveConsume;

   public SkillDBEntry() {
   }

   public SkillDBEntry(int skillID, int level, int remainThawTime, boolean selected, int passiveConsume) {
      this.skillID = skillID;
      this.level = level;
      this.remainThawTime = remainThawTime;
      this.selected = selected;
      this.passiveConsume = passiveConsume;
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void setSkillID(int skillID) {
      this.skillID = skillID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getRemainThawTime() {
      return this.remainThawTime;
   }

   public void setRemainThawTime(int remainThawTime) {
      this.remainThawTime = remainThawTime;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public int getPassiveConsume() {
      return this.passiveConsume;
   }

   public void setPassiveConsume(int passiveConsume) {
      this.passiveConsume = passiveConsume;
   }
}
