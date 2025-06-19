package com.mu.game.model.unit.skill.model;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.action.SkillActionFactory;
import com.mu.game.model.unit.skill.levelData.LevelDataManager;
import com.mu.game.model.unit.skill.passive.PassiveSkillData;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class SkillModel {
   private static HashMap models = new HashMap();
   private int skillId;
   private String name;
   private int iconId;
   private int smallIconId;
   private String des;
   private int type;
   private int statusType;
   private int groupType;
   private boolean isGain;
   private int coolTime;
   private boolean publicCoolTime;
   private int actionType;
   private SkillAction action;
   private int distance;
   private int datum;
   private int range;
   private int rangeType;
   private int angle;
   private int effectMaxCount;
   private int showBlongs;
   private int belongs;
   private int profession;
   private int skillEffect;
   private int releaseEffect;
   private int targetEffect;
   private int trajectory;
   private int order;
   private int userLevel;
   private boolean canLevelUp;
   private int maxLevel;
   private HashSet frontSkills = new HashSet();
   private int effectedObject;
   private HashSet effectedType = null;
   private List dataOrder = new ArrayList();
   private int deathDelayTime = 600;
   private String openDes;
   private boolean showInPanel = true;
   private StatEnum consumeStat;
   private boolean canHang;
   private StatEnum fightStat;
   private Item activeItem;
   private String activeDes;
   private String summaryDes;
   private int hangPriority;
   private float attackPlayerCoe;

   public SkillModel() {
      this.consumeStat = StatEnum.MP;
      this.canHang = false;
      this.fightStat = StatEnum.None;
      this.activeItem = null;
      this.activeDes = "";
      this.summaryDes = "";
      this.hangPriority = 0;
      this.attackPlayerCoe = 1.0F;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet modelSheet = wb.getSheet(1);
      initModels(modelSheet);
      Sheet levelDataSheet = wb.getSheet(2);
      LevelDataManager.init(levelDataSheet);
      Sheet showBelongsSheet = wb.getSheet(3);
      SkillShowBelongs.init(showBelongsSheet);
      Sheet professionSheet = wb.getSheet(4);
      ProfessionSkills.init(professionSheet);
      Sheet movementSheet = wb.getSheet(5);
      SkillMovement.init(movementSheet);
      PassiveSkillData.init(wb.getSheet(6));
   }

   public static void initModels(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         SkillModel model = new SkillModel();
         model.setSkillId(Tools.getCellIntValue(sheet.getCell("A" + i)));
         model.setName(Tools.getCellValue(sheet.getCell("B" + i)));
         model.setIconId(Tools.getCellIntValue(sheet.getCell("C" + i)));
         model.setSmallIconId(Tools.getCellIntValue(sheet.getCell("D" + i)));
         model.setDes(Tools.getCellValue(sheet.getCell("E" + i)));
         model.setGroupType(Tools.getCellIntValue(sheet.getCell("F" + i)));
         model.setStatusType(Tools.getCellIntValue(sheet.getCell("G" + i)));
         model.setType(Tools.getCellIntValue(sheet.getCell("H" + i)));
         model.setGain(Tools.getCellFloatValue(sheet.getCell("I" + i)) == 1.0F);
         model.setCoolTime(Tools.getCellIntValue(sheet.getCell("J" + i)));
         model.setPublicCoolTime(Tools.getCellFloatValue(sheet.getCell("K" + i)) == 1.0F);
         model.setDistance(Tools.getCellIntValue(sheet.getCell("L" + i)));
         model.setDatum(Tools.getCellIntValue(sheet.getCell("M" + i)));
         model.setRange(Tools.getCellIntValue(sheet.getCell("N" + i)));
         model.setRangeType(Tools.getCellIntValue(sheet.getCell("O" + i)));
         model.setAngle(Tools.getCellIntValue(sheet.getCell("P" + i)));
         model.setEffectMaxCount(Tools.getCellIntValue(sheet.getCell("Q" + i)));
         model.setShowBlongs(Tools.getCellIntValue(sheet.getCell("R" + i)));
         model.setBelongs(Tools.getCellIntValue(sheet.getCell("S" + i)));
         model.setProfession(Tools.getCellIntValue(sheet.getCell("T" + i)));
         model.setSkillEffect(Tools.getCellIntValue(sheet.getCell("U" + i)));
         model.setReleaseEffect(Tools.getCellIntValue(sheet.getCell("V" + i)));
         model.setTargetEffect(Tools.getCellIntValue(sheet.getCell("W" + i)));
         model.setTrajectory(Tools.getCellIntValue(sheet.getCell("X" + i)));
         model.setOrder(Tools.getCellIntValue(sheet.getCell("Y" + i)));
         model.setUserLevel(Tools.getCellIntValue(sheet.getCell("Z" + i)));
         model.setCanLevelUp(Tools.getCellIntValue(sheet.getCell("AA" + i)) == 1);
         model.setMaxLevel(Tools.getCellIntValue(sheet.getCell("AB" + i)));
         HashSet frontSkills = StringTools.analyzeIntegerHashset(sheet.getCell("AC" + i).getContents(), ",");
         model.setFrontSkills(frontSkills);
         model.setEffectedObject(Tools.getCellIntValue(sheet.getCell("AD" + i)));
         HashSet effectTypes = StringTools.analyzeIntegerHashset(Tools.getCellValue(sheet.getCell("AE" + i)), ",");
         model.setEffectedType(effectTypes);
         List dataOrder = StringTools.analyzeIntegerStatenum(Tools.getCellValue(sheet.getCell("AF" + i)), ",");
         model.setDataOrder(dataOrder);
         model.setDeathDelayTime(Tools.getCellIntValue(sheet.getCell("AG" + i)));
         model.setShowInPanel(Tools.getCellIntValue(sheet.getCell("AH" + i)) == 1);
         String actionID = Tools.getCellValue(sheet.getCell("AI" + i));
         String actionStr = Tools.getCellValue(sheet.getCell("AJ" + i));
         StatEnum consumeStat = StatEnum.find(Tools.getCellIntValue(sheet.getCell("AK" + i)));
         if (consumeStat == StatEnum.None) {
            consumeStat = StatEnum.MP;
         }

         model.setConsumeStat(consumeStat);
         model.setCanHang(Tools.getCellIntValue(sheet.getCell("AL" + i)) == 1);
         model.setHangPriority(Tools.getCellIntValue(sheet.getCell("AM" + i)));
         model.setFightStat(StatEnum.find(Tools.getCellIntValue(sheet.getCell("AN" + i))));
         String aDes = Tools.getCellValue(sheet.getCell("AO" + i));
         model.setActiveDes(aDes == null ? "" : aDes);
         int activeItemID = Tools.getCellIntValue(sheet.getCell("AP" + i));
         if (activeItemID != -1) {
            Item item = ItemTools.createItem(activeItemID, 1, 2);
            if (item == null) {
               throw new Exception("技能激活道具不存在 第 " + i + "行");
            }

            model.setActiveItem(item);
         }

         model.setSummaryDes(Tools.getCellValue(sheet.getCell("AQ" + i)));
         model.setAttackPlayerCoe(Tools.getCellFloatValue(sheet.getCell("AR" + i)));
         if (model.getDatum() == 4 && (model.getType() != 3 || model.getGroupType() != 1 || !model.isGain)) {
            throw new Exception("技能模板第" + i + "行 - 基点类型" + model.getDatum() + ",只能适用于单体且增益性技能");
         }

         SkillAction action = SkillActionFactory.createAction(actionID, actionStr);
         model.setAction(action);
         models.put(model.getSkillId(), model);
      }

   }

   public static void initCheck() throws Exception {
      Iterator var1 = models.values().iterator();

      while(var1.hasNext()) {
         SkillModel model = (SkillModel)var1.next();
         SkillAction action = model.getAction();
         if (action != null) {
            action.initCheck("技能ID{" + model.getSkillId() + "}");
         }

         if (model.isPassive()) {
            PassiveSkillData data = PassiveSkillData.getData(model.getSkillId());
            if (data == null) {
               throw new Exception("被动技能特殊说明不存在, skillID = " + model.getSkillId());
            }

            data.checkInit("技能ID  = " + model.getSkillId());
         }
      }

   }

   public static String getSkillName(int skillID) {
      return models.containsKey(skillID) ? ((SkillModel)models.get(skillID)).getName() : "";
   }

   public static SkillModel getModel(int skillID) {
      return (SkillModel)models.get(skillID);
   }

   public static boolean hasModel(int skillID) {
      return models.containsKey(skillID);
   }

   public static HashMap getAllModels() {
      return models;
   }

   public boolean isPassive() {
      return this.statusType == 0;
   }

   public int getSkillId() {
      return this.skillId;
   }

   public void setSkillId(int skillId) {
      this.skillId = skillId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isCanHang() {
      return this.canHang;
   }

   public void setCanHang(boolean canHang) {
      this.canHang = canHang;
   }

   public int getIconId() {
      return this.iconId;
   }

   public void setIconId(int iconId) {
      this.iconId = iconId;
   }

   public int getSmallIconId() {
      return this.smallIconId;
   }

   public void setSmallIconId(int smallIconId) {
      this.smallIconId = smallIconId;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getStatusType() {
      return this.statusType;
   }

   public StatEnum getConsumeStat() {
      return this.consumeStat;
   }

   public void setConsumeStat(StatEnum consumeStat) {
      this.consumeStat = consumeStat;
   }

   public void setStatusType(int statusType) {
      this.statusType = statusType;
   }

   public int getGroupType() {
      return this.groupType;
   }

   public void setGroupType(int groupType) {
      this.groupType = groupType;
   }

   public boolean isGain() {
      return this.isGain;
   }

   public void setGain(boolean isGain) {
      this.isGain = isGain;
   }

   public int getCoolTime() {
      return this.coolTime;
   }

   public void setCoolTime(int coolTime) {
      this.coolTime = coolTime;
   }

   public boolean isPublicCoolTime() {
      return this.publicCoolTime;
   }

   public void setPublicCoolTime(boolean publicCoolTime) {
      this.publicCoolTime = publicCoolTime;
   }

   public int getActionType() {
      return this.actionType;
   }

   public void setActionType(int actionType) {
      this.actionType = actionType;
   }

   public SkillAction getAction() {
      return this.action;
   }

   public void setAction(SkillAction action) {
      this.action = action;
   }

   public int getDistance() {
      return this.distance;
   }

   public void setDistance(int distance) {
      this.distance = distance;
   }

   public int getRange() {
      return this.range;
   }

   public void setRange(int range) {
      this.range = range;
   }

   public int getDatum() {
      return this.datum;
   }

   public void setDatum(int datum) {
      this.datum = datum;
   }

   public int getEffectMaxCount() {
      return this.effectMaxCount;
   }

   public void setEffectMaxCount(int effectMaxCount) {
      this.effectMaxCount = effectMaxCount;
   }

   public int getShowBlongs() {
      return this.showBlongs;
   }

   public void setShowBlongs(int showBlongs) {
      this.showBlongs = showBlongs;
   }

   public int getBelongs() {
      return this.belongs;
   }

   public void setBelongs(int belongs) {
      this.belongs = belongs;
   }

   public int getSkillEffect() {
      return this.skillEffect;
   }

   public void setSkillEffect(int skillEffect) {
      this.skillEffect = skillEffect;
   }

   public int getReleaseEffect() {
      return this.releaseEffect;
   }

   public void setReleaseEffect(int releaseEffect) {
      this.releaseEffect = releaseEffect;
   }

   public int getTargetEffect() {
      return this.targetEffect;
   }

   public void setTargetEffect(int targetEffect) {
      this.targetEffect = targetEffect;
   }

   public int getTrajectory() {
      return this.trajectory;
   }

   public void setTrajectory(int trajectory) {
      this.trajectory = trajectory;
   }

   public int getOrder() {
      return this.order;
   }

   public float getAttackPlayerCoe() {
      return this.attackPlayerCoe;
   }

   public void setAttackPlayerCoe(float attackPlayerCoe) {
      this.attackPlayerCoe = attackPlayerCoe;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public int getUserLevel() {
      return this.userLevel;
   }

   public void setUserLevel(int userLevel) {
      this.userLevel = userLevel;
   }

   public boolean isCanLevelUp() {
      return this.canLevelUp;
   }

   public void setCanLevelUp(boolean canLevelUp) {
      this.canLevelUp = canLevelUp;
   }

   public int getProfession() {
      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getMaxLevel() {
      return this.maxLevel;
   }

   public void setMaxLevel(int maxLevel) {
      this.maxLevel = maxLevel;
   }

   public HashSet getFrontSkills() {
      return this.frontSkills;
   }

   public void setFrontSkills(HashSet frontSkills) {
      this.frontSkills = frontSkills;
   }

   public int getEffectedObject() {
      return this.effectedObject;
   }

   public void setEffectedObject(int effectedObject) {
      this.effectedObject = effectedObject;
   }

   public HashSet getEffectedType() {
      return this.effectedType;
   }

   public void setEffectedType(HashSet effectedType) {
      this.effectedType = effectedType;
   }

   public List getDataOrder() {
      return this.dataOrder;
   }

   public void setDataOrder(List dataOrder) {
      this.dataOrder = dataOrder;
   }

   public int getDeathDelayTime() {
      return this.deathDelayTime;
   }

   public void setDeathDelayTime(int deathDelayTime) {
      this.deathDelayTime = deathDelayTime;
   }

   public String getOpenDes() {
      return this.openDes;
   }

   public void setOpenDes(String openDes) {
      this.openDes = openDes;
   }

   public int getRangeType() {
      return this.rangeType;
   }

   public void setRangeType(int rangeType) {
      this.rangeType = rangeType;
   }

   public int getAngle() {
      return this.angle;
   }

   public void setAngle(int angle) {
      this.angle = angle;
   }

   public boolean isShowInPanel() {
      return this.showInPanel;
   }

   public void setShowInPanel(boolean showInPanel) {
      this.showInPanel = showInPanel;
   }

   public StatEnum getFightStat() {
      return this.fightStat;
   }

   public void setFightStat(StatEnum fightStat) {
      this.fightStat = fightStat;
   }

   public Item getActiveItem() {
      return this.activeItem;
   }

   public void setActiveItem(Item activeItem) {
      this.activeItem = activeItem;
   }

   public String getActiveDes() {
      return this.activeDes;
   }

   public void setActiveDes(String activeDes) {
      this.activeDes = activeDes;
   }

   public String getSummaryDes() {
      return this.summaryDes == null ? "" : this.summaryDes;
   }

   public void setSummaryDes(String summaryDes) {
      this.summaryDes = summaryDes;
   }

   public int getHangPriority() {
      return this.hangPriority;
   }

   public void setHangPriority(int hangPriority) {
      this.hangPriority = hangPriority;
   }
}
