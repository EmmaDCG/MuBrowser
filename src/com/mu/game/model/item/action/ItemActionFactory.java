package com.mu.game.model.item.action;

import com.mu.game.model.item.action.imp.AddBasicProperty;
import com.mu.game.model.item.action.imp.AddExp;
import com.mu.game.model.item.action.imp.AddHp;
import com.mu.game.model.item.action.imp.AddMoney;
import com.mu.game.model.item.action.imp.AddMp;
import com.mu.game.model.item.action.imp.AddNumber;
import com.mu.game.model.item.action.imp.AddSkillPassiveConsume;
import com.mu.game.model.item.action.imp.BuffAccept;
import com.mu.game.model.item.action.imp.EquipItem;
import com.mu.game.model.item.action.imp.ExpBonus;
import com.mu.game.model.item.action.imp.GetTitle;
import com.mu.game.model.item.action.imp.LearnSkill;
import com.mu.game.model.item.action.imp.OpenBox;
import com.mu.game.model.item.action.imp.OpenFunctionPanel;
import com.mu.game.model.item.action.imp.PetLevelUpOrAddItem;
import com.mu.game.model.item.action.imp.ReduceEvil;
import com.mu.game.model.item.action.imp.RoleLevelUp;
import com.mu.game.model.item.action.imp.SpiritItem;
import com.mu.game.model.item.action.imp.UseVIPAction;
import com.mu.game.model.item.action.imp.WashProperty;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.StatEnum;
import java.util.HashMap;

public class ItemActionFactory {
   public static final int AddHp = 1;
   public static final int AddMp = 2;
   public static final int TaskAccept = 3;
   public static final int BuffAccept = 4;
   public static final int AddExp = 5;
   public static final int EquipItem = 7;
   public static final int AddMoney = 8;
   public static final int LearnSkill = 9;
   public static final int ReduceEvil_ = 10;
   public static final int AddBasicProperty = 12;
   public static final int OpenBox_ = 13;
   public static final int WashProperty_ = 14;
   public static final int ExpBonus_ = 15;
   public static final int AddSkillPassiveConsume_ = 16;
   public static final int UseVIP = 17;
   public static final int OpenPanel_ = 18;
   public static final int GetTitle = 19;
   public static final int PetLevelUpOrAddItem_ = 20;
   public static final int RoleLevelUp_ = 21;
   public static final int SpiritItem_ = 22;

   public static ItemAction createAction(int actionID, String value) {
      try {
         switch(actionID) {
         case 1:
            return createAddHpAction(value);
         case 2:
            return createAddMpAction(value);
         case 3:
         case 6:
         case 11:
         default:
            break;
         case 4:
            return createBuffAccept(value);
         case 5:
            return createAddExpAction(value);
         case 7:
            return createEquipItemAction(value);
         case 8:
            return createAddMoneyAction(value);
         case 9:
            return createLearnSkillAction(value);
         case 10:
            return new ReduceEvil();
         case 12:
            return createAddBasicProperty(value);
         case 13:
            return createOpenBox(value);
         case 14:
            return createWashProperty(value);
         case 15:
            return createExpBonus(value);
         case 16:
            return createAddSkillPassiveConsume(value);
         case 17:
            return new UseVIPAction(Integer.parseInt(value));
         case 18:
            return createOpenFunctionPanel(value);
         case 19:
            return new GetTitle(Integer.parseInt(value));
         case 20:
            return createPetLevelUpOrAddItem(value);
         case 21:
            return createRoleLevelUp(value);
         case 22:
            return new SpiritItem(Integer.parseInt(value));
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return null;
   }

   public static RoleLevelUp createRoleLevelUp(String value) throws Exception {
      RoleLevelUp rlu = new RoleLevelUp(Integer.parseInt(value));
      return rlu;
   }

   private static PetLevelUpOrAddItem createPetLevelUpOrAddItem(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 3) {
         throw new Exception("物品操作方式- 冰后精魂- 数据不正确  value = " + value);
      } else {
         int targetLevel = Integer.parseInt(splits[0]);
         int itemModelID = Integer.parseInt(splits[1]);
         int count = Integer.parseInt(splits[2]);
         PetLevelUpOrAddItem ofp = new PetLevelUpOrAddItem(targetLevel, itemModelID, count);
         return ofp;
      }
   }

   private static OpenFunctionPanel createOpenFunctionPanel(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 3) {
         throw new Exception("物品操作方式 - Buff开启  value = " + value);
      } else {
         int functionID = Integer.parseInt(splits[0]);
         int bigPanelID = Integer.parseInt(splits[1]);
         int smallPanelID = Integer.parseInt(splits[2]);
         OpenFunctionPanel ofp = new OpenFunctionPanel(functionID, bigPanelID, smallPanelID);
         return ofp;
      }
   }

   private static BuffAccept createBuffAccept(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - Buff开启  value = " + value);
      } else {
         int buffID = Integer.parseInt(splits[0]);
         int level = Integer.parseInt(splits[1]);
         BuffAccept bc = new BuffAccept(buffID, level);
         return bc;
      }
   }

   private static ExpBonus createExpBonus(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - Buff开启  value = " + value);
      } else {
         int buffID = Integer.parseInt(splits[0]);
         int level = Integer.parseInt(splits[1]);
         ExpBonus bc = new ExpBonus(buffID, level);
         return bc;
      }
   }

   private static AddHp createAddHpAction(String value) throws Exception {
      String[] splits = value.split(",");
      int num = 0;
      boolean percent = false;
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - 添加HP  value = " + value);
      } else {
         num = Integer.parseInt(splits[0]);
         percent = Integer.parseInt(splits[1]) == 1;
         AddHp ah = new AddHp(num, percent);
         return ah;
      }
   }

   private static AddMp createAddMpAction(String value) throws Exception {
      String[] splits = value.split(",");
      int num = 0;
      boolean percent = false;
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - 添加MP  value = " + value);
      } else {
         num = Integer.parseInt(splits[0]);
         percent = Integer.parseInt(splits[1]) == 1;
         AddMp ah = new AddMp(num, percent);
         return ah;
      }
   }

   private static EquipItem createEquipItemAction(String value) throws Exception {
      EquipItem ei = new EquipItem();
      return ei;
   }

   private static AddMoney createAddMoneyAction(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - 金钱  value = " + value);
      } else {
         AddMoney am = new AddMoney(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]));
         if (am.getValue() < 1) {
            throw new Exception("物品操作方式 - 金钱 - 数值  < 1 ,vaule = " + value);
         } else {
            return am;
         }
      }
   }

   private static AddExp createAddExpAction(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - 经验  value = " + value);
      } else {
         AddExp am = new AddExp(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]) == 1);
         if (am.getValue() < 1) {
            throw new Exception("物品操作方式 - 经验- 数值  < 1 ,vaule = " + value);
         } else {
            return am;
         }
      }
   }

   private static AddSkillPassiveConsume createAddSkillPassiveConsume(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式 - 添加技能被动消耗 value = " + value);
      } else {
         AddSkillPassiveConsume am = new AddSkillPassiveConsume(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]));
         return am;
      }
   }

   private static AddBasicProperty createAddBasicProperty(String value) throws Exception {
      String[] splits = value.split(";");
      HashMap basicMap = new HashMap();
      String[] var6 = splits;
      int var5 = splits.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         String firstStr = var6[var4];
         String[] secSplits = firstStr.split(",");
         if (secSplits.length < 3) {
            throw new Exception("物品操作方式-果实-数值有错");
         }

         StatEnum stat = StatEnum.find(Integer.parseInt(secSplits[0]));
         int minCount = Integer.parseInt(secSplits[1]);
         int maxCount = Integer.parseInt(secSplits[2]);
         AddNumber number = new AddNumber(stat, minCount, maxCount);
         basicMap.put(stat, number);
      }

      return new AddBasicProperty(basicMap);
   }

   private static WashProperty createWashProperty(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 2) {
         throw new Exception("物品操作方式-洗点，配置错误");
      } else {
         StatEnum stat = StatEnum.find(Integer.parseInt(splits[0]));
         int washValue = Integer.parseInt(splits[1]);
         return new WashProperty(stat, washValue);
      }
   }

   private static OpenBox createOpenBox(String value) throws Exception {
      HashMap boxIDMap = StringTools.analyzeIntegerMap(value, ",");
      if (boxIDMap.size() < 1) {
         throw new Exception("物品操作方式-开箱子-数值为空");
      } else {
         return new OpenBox(boxIDMap);
      }
   }

   private static LearnSkill createLearnSkillAction(String value) throws Exception {
      int skillID = Integer.parseInt(value);
      LearnSkill ls = new LearnSkill(skillID);
      return ls;
   }
}
