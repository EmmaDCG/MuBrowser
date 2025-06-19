package com.mu.game.dungeon;

import com.mu.game.model.drop.model.MonsterDrop;
import java.util.ArrayList;
import java.util.List;

public class MonsterNumericalValue {
   private int id;
   private int hp;
   private int mp;
   private int minAttck;
   private int maxAttack;
   private int def;
   private int hit;
   private int avd;
   private int hitAbsolute;
   private int avdAbsolute;
   private int exp;
   private int dropLoopTimes;
   private String dropStr;
   private String firstKillTag;
   private String firstKillStr;
   private List drops = null;

   public MonsterNumericalValue(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public int getHp() {
      return this.hp;
   }

   public void setHp(int hp) {
      this.hp = hp;
   }

   public int getMp() {
      return this.mp;
   }

   public void setMp(int mp) {
      this.mp = mp;
   }

   public int getMinAttck() {
      return this.minAttck;
   }

   public void setMinAttck(int minAttck) {
      this.minAttck = minAttck;
   }

   public int getMaxAttack() {
      return this.maxAttack;
   }

   public void setMaxAttack(int maxAttack) {
      this.maxAttack = maxAttack;
   }

   public int getDef() {
      return this.def;
   }

   public void setDef(int def) {
      this.def = def;
   }

   public int getHit() {
      return this.hit;
   }

   public void setHit(int hit) {
      this.hit = hit;
   }

   public int getAvd() {
      return this.avd;
   }

   public void setAvd(int avd) {
      this.avd = avd;
   }

   public int getHitAbsolute() {
      return this.hitAbsolute;
   }

   public void setHitAbsolute(int hitAbsolute) {
      this.hitAbsolute = hitAbsolute;
   }

   public int getAvdAbsolute() {
      return this.avdAbsolute;
   }

   public void setAvdAbsolute(int avdAbsolute) {
      this.avdAbsolute = avdAbsolute;
   }

   public int getExp() {
      return this.exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }

   public int getDropLoopTimes() {
      return this.dropLoopTimes;
   }

   public void setDropLoopTimes(int dropLoopTimes) {
      this.dropLoopTimes = dropLoopTimes;
   }

   public String getDropStr() {
      return this.dropStr;
   }

   public void setDropStr(String dropStr) {
      this.dropStr = dropStr;
   }

   public String getFirstKillTag() {
      return this.firstKillTag;
   }

   public void setFirstKillTag(String firstKillTag) {
      this.firstKillTag = firstKillTag;
   }

   public String getFirstKillStr() {
      return this.firstKillStr;
   }

   public void setFirstKillStr(String firstKillStr) {
      this.firstKillStr = firstKillStr;
   }

   public void addMonsterDrop(MonsterDrop drop) {
      if (this.drops == null) {
         this.drops = new ArrayList();
      }

      this.drops.add(drop);
   }

   public List getDrops() {
      return this.drops;
   }
}
