package com.mu.game.model.unit.skill.levelData;

import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.skill.condition.Condition;
import com.mu.game.model.unit.skill.condition.ConditionManager;
import com.mu.game.model.unit.skill.consume.Consume;
import com.mu.game.model.unit.skill.learnCondition.LearnCondition;
import com.mu.game.model.unit.skill.learnCondition.LearnManager;
import com.mu.game.model.unit.skill.learnConsume.LearnConsume;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelDataManager {
   private static Logger logger = LoggerFactory.getLogger(LevelDataManager.class);

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int userLevel;
      for(int i = 2; i <= rows; ++i) {
         int skillID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int level = Tools.getCellIntValue(sheet.getCell("B" + i));
         userLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         int money = Tools.getCellIntValue(sheet.getCell("D" + i));
         String fstr = sheet.getCell("E" + i).getContents();
         HashMap frontSkills = StringTools.analyzeIntegerMap(fstr, ",");
         int itemID = Tools.getCellIntValue(sheet.getCell("F" + i));
         int count = Tools.getCellIntValue(sheet.getCell("G" + i));
         int passiveConsume = Tools.getCellIntValue(sheet.getCell("H" + i));
         int coolTime = Tools.getCellIntValue(sheet.getCell("I" + i));
         int mpConsume = Tools.getCellIntValue(sheet.getCell("J" + i));
         int aGConsume = Tools.getCellIntValue(sheet.getCell("K" + i));
         int effectCount = Tools.getCellIntValue(sheet.getCell("L" + i));
         int rangeAdd = Tools.getCellIntValue(sheet.getCell("M" + i));
         int buffTime = Tools.getCellIntValue(sheet.getCell("N" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("O" + i));
         int distance = Tools.getCellIntValue(sheet.getCell("P" + i));
         String otherStats = sheet.getCell("Q" + i).getContents();
         List stats = StringTools.analyzeArrayAttris(otherStats, ",");
         int quality = Tools.getCellIntValue(sheet.getCell("R" + i));
         float skillCorrection = Tools.getCellFloatValue(sheet.getCell("S" + i));
         float skillFactor = Tools.getCellFloatValue(sheet.getCell("T" + i));
         int domi = Tools.getCellIntValue(sheet.getCell("U" + i));
         boolean flag = true;
         Iterator condition = frontSkills.keySet().iterator();

         while(condition.hasNext()) {
            int fs = ((Integer)condition.next()).intValue();
            if (SkillModel.getModel(fs) == null) {
               logger.error("前置技能 不存在 skillID = {},level = {}", skillID, level);
               flag = false;
            }
         }

         if (itemID != -1) {
            if (ItemModel.getModel(itemID) == null) {
               logger.error("物品不存在 skillID = {}, level = {}", skillID, level);
               flag = false;
            } else if (count < 1) {
               logger.error("物品数量不合适 skillID = {}, level = {}", skillID, level);
               flag = false;
            }
         }

         if (skillFactor == 0.0F) {
            logger.error("技能系数=0 skillID = {}, level = {}", skillID, level);
            flag = false;
         }

         if (!flag) {
            throw new Exception("技能动态数据有错");
         }

         if (passiveConsume < 0) {
            throw new Exception(sheet.getName() + " - 被动技能消耗值填写错误 ，第 " + i + " 行");
         }

         if (domi < 0) {
            throw new Exception(sheet.getName() + "- 技能战斗力数据不合适，第" + i + " 行");
         }

         SkillLevelData levelData = new SkillLevelData(skillID, level);
         levelData.setCoolTime(coolTime);
         levelData.setMpConsume(mpConsume);
         levelData.setAGConsume(aGConsume);
         levelData.setEffectCount(effectCount);
         levelData.setRangeAdd(rangeAdd);
         levelData.setBuffTime(buffTime);
         levelData.setRate(rate);
         levelData.setDistance(distance);
         levelData.setStats(stats);
         levelData.setQuality(quality);
         levelData.setPassiveConsume(passiveConsume);
         levelData.setUserLevel(userLevel);
         levelData.setMoney(money);
         levelData.setItemID(itemID);
         levelData.setItemCount(count);
         levelData.setDomineering(domi);
         if (itemID != -1) {
            levelData.setNeedItem(ItemTools.createItem(itemID, count, 2));
         }

         levelData.setSkillCorrection(skillCorrection / 100000.0F);
         levelData.setSkillFactor(skillFactor);
         SkillLevelData.addLevelData(levelData);
         condition = null;
         LearnCondition consume;
         LearnConsume ls;
         if (money > 0) {
            consume = LearnManager.createLearnCondition(1, money);
            LearnManager.addLearnCondition(skillID, level, consume);
            ls = LearnManager.createConsume(1, money);
            LearnManager.addLearnConsume(skillID, level, ls);
         }

         if (itemID != -1) {
            consume = LearnManager.createLearnCondition(2, itemID, count);
            LearnManager.addLearnCondition(skillID, level, consume);
            ls = LearnManager.createConsume(2, itemID, count);
            LearnManager.addLearnConsume(skillID, level, ls);
         }

         if (frontSkills.size() > 0) {
            consume = LearnManager.createLearnCondition(3, fstr);
            LearnManager.addLearnCondition(skillID, level, consume);
         }

         if (userLevel > 1) {
            consume = LearnManager.createLearnCondition(4, userLevel);
            LearnManager.addLearnCondition(skillID, level, consume);
         }

         consume = null;
         //Condition condition;
         //Consume consume;
         if (mpConsume > 0) {
            Condition condition2 = ConditionManager.createCondition(2, mpConsume);
            ConditionManager.addUseCondition(skillID, level, condition2);
            Consume consume2 = ConditionManager.createConsume(2, mpConsume);
            ConditionManager.addConsume(skillID, level, consume2);
         }

         if (aGConsume > 0) {
            Condition condition2 = ConditionManager.createCondition(3, aGConsume);
            ConditionManager.addUseCondition(skillID, level, condition2);
            Consume consume2 = ConditionManager.createConsume(3, aGConsume);
            ConditionManager.addConsume(skillID, level, consume2);
         }
      }

      HashMap models = SkillModel.getAllModels();
      Iterator var33 = models.values().iterator();

      while(var33.hasNext()) {
         SkillModel model = (SkillModel)var33.next();

         for(userLevel = 1; userLevel <= model.getMaxLevel(); ++userLevel) {
            SkillLevelData levelData = SkillLevelData.getLevelData(model.getSkillId(), userLevel);
            if (levelData == null) {
               throw new Exception("技能 " + model.getName() + " 等级" + userLevel + "，没有动态数据");
            }
         }
      }

   }
}
