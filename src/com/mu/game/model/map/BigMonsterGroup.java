package com.mu.game.model.map;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.utils.Tools;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class BigMonsterGroup {
   private String name;
   private int templateId;
   private int star;
   private int minLevel;
   private int maxLevel;
   private long revivalTime = 1000L;
   private int x;
   private int y;
   private int num;
   private int moveRadius;
   private float speed;
   private int bornRadius;
   private int giveUpDist;
   private int serachDist;
   private int maxMoveDist;
   private int attackDist;
   private int Ai;
   private int minAttackDist;
   private int[] face = null;
   private List skillList;
   private int bossRank = 0;

   public static void parseMonsterGroup(BigMonsterGroup mg, Sheet sheet, int index) throws Exception {
      mg.setName(Tools.getCellValue(sheet.getCell("A" + index)));
      mg.setTemplateId(Tools.getCellIntValue(sheet.getCell("B" + index)));
      if (!CreatureTemplate.hasTemplate(mg.getTemplateId())) {
         throw new Exception("template not found " + mg.getTemplateId());
      } else {
         mg.setAi(Tools.getCellIntValue(sheet.getCell("C" + index)));
         mg.setRevivalTime((long)Tools.getCellIntValue(sheet.getCell("D" + index)) * 1000L);
         mg.setMinLevel(Tools.getCellIntValue(sheet.getCell("E" + index)));
         mg.setMaxLevel(Tools.getCellIntValue(sheet.getCell("F" + index)));
         mg.setStar(Tools.getCellIntValue(sheet.getCell("G" + index)));
         mg.setX(Tools.getCellIntValue(sheet.getCell("H" + index)));
         mg.setY(Tools.getCellIntValue(sheet.getCell("I" + index)));
         mg.setNum(Tools.getCellIntValue(sheet.getCell("J" + index)));
         mg.setBornRadius(Tools.getCellIntValue(sheet.getCell("K" + index)));
         mg.setSpeed(Tools.getCellFloatValue(sheet.getCell("L" + index)) * 100.0F);
         mg.setMoveRadius(Tools.getCellIntValue(sheet.getCell("M" + index)));
         mg.setGiveUpDist(Tools.getCellIntValue(sheet.getCell("N" + index)));
         mg.setSerachDist(Tools.getCellIntValue(sheet.getCell("O" + index)));
         mg.setMaxMoveDist(Tools.getCellIntValue(sheet.getCell("P" + index)));
         mg.setAttackDist(Tools.getCellIntValue(sheet.getCell("Q" + index)));
         mg.setMinAttackDist(Tools.getCellIntValue(sheet.getCell("R" + index)));
         String skillIDStr = Tools.getCellValue(sheet.getCell("S" + index));
         List skillList = StringTools.analyzeIntegerList(skillIDStr, ",");
         if (skillList == null || skillList.size() < 1) {
            (new Exception("怪物技能没有配置")).printStackTrace();
         }

         Iterator var6 = skillList.iterator();

         while(var6.hasNext()) {
            Integer skillID = (Integer)var6.next();
            if (!SkillModel.hasModel(skillID.intValue())) {
               (new Exception("怪物 分布 - 技能id不存在")).printStackTrace();
            }
         }

         mg.setSkillList(skillList);
      }
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

   public int getModelId() {
      return CreatureTemplate.getTemplate(this.templateId).getModelId();
   }

   public int getStar() {
      return this.star;
   }

   public void setStar(int star) {
      this.star = star;
   }

   public int getMinLevel() {
      return this.minLevel;
   }

   public void setMinLevel(int minLevel) {
      this.minLevel = minLevel;
   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public List getSkillList() {
      return this.skillList;
   }

   public void setSkillList(List skillList) {
      this.skillList = skillList;
   }

   public void setMaxLevel(int maxLevel) {
      this.maxLevel = maxLevel;
   }

   public long getRevivalTime() {
      return this.revivalTime;
   }

   public void setRevivalTime(long revivalTime) {
      this.revivalTime = revivalTime;
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

   public int getNum() {
      return this.num;
   }

   public void setNum(int num) {
      this.num = num;
   }

   public float getSpeed() {
      return this.speed;
   }

   public void setSpeed(float speed) {
      this.speed = speed;
   }

   public int getBornRadius() {
      return this.bornRadius;
   }

   public void setBornRadius(int bornRadius) {
      this.bornRadius = bornRadius;
   }

   public int getGiveUpDist() {
      return this.giveUpDist;
   }

   public void setGiveUpDist(int giveUpDist) {
      this.giveUpDist = giveUpDist;
   }

   public int getSerachDist() {
      return this.serachDist;
   }

   public void setSerachDist(int serachDist) {
      this.serachDist = serachDist;
   }

   public int getMaxMoveDist() {
      return this.maxMoveDist;
   }

   public void setMaxMoveDist(int maxMoveDist) {
      this.maxMoveDist = maxMoveDist;
   }

   public int getAttackDist() {
      return this.attackDist;
   }

   public void setAttackDist(int attackDist) {
      this.attackDist = attackDist;
   }

   public int getAi() {
      return this.Ai;
   }

   public void setAi(int ai) {
      this.Ai = ai;
   }

   public int getMoveRadius() {
      return this.moveRadius;
   }

   public void setMoveRadius(int moveRadius) {
      this.moveRadius = moveRadius;
   }

   public int getMinAttackDist() {
      return this.minAttackDist;
   }

   public void setMinAttackDist(int minAttackDist) {
      this.minAttackDist = minAttackDist;
   }

   public int[] getFace() {
      return this.face;
   }

   public void setFace(int[] face) {
      this.face = face;
   }

   public int getBossRank() {
      return this.bossRank;
   }

   public void setBossRank(int bossRank) {
      this.bossRank = bossRank;
   }

   public static int[] parseFace(String str) {
      if (str != null && !str.trim().equals("")) {
         String[] faceStr = str.split(",");
         return new int[]{Integer.parseInt(faceStr[0]), Integer.parseInt(faceStr[1])};
      } else {
         return null;
      }
   }
}
