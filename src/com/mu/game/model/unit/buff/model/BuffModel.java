package com.mu.game.model.unit.buff.model;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.click.ClickPopup;
import com.mu.game.model.unit.buff.requirement.EffectReCreator;
import com.mu.game.model.unit.buff.requirement.EffectRequirement;
import com.mu.game.model.unit.unitevent.OperationLimitManager;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class BuffModel {
   private static HashMap models = new HashMap();
   private int modelID;
   private String name;
   private int buffType;
   private int icon;
   private int smallIcon;
   private String des;
   private int effecteID;
   private int duration;
   private int intervalTime;
   private int rate;
   private boolean manulControl;
   private boolean show;
   private boolean dieAway;
   private boolean logouAway;
   private boolean levelByCreator;
   private boolean calOfflineTime;
   private boolean overlap;
   private int maxOverlap;
   private int overlapType;
   private boolean isDebuff;
   private int serverStatusID = -1;
   private int clientStatusID = -1;
   private List dataOrder;
   private int order;
   private EffectRequirement requirement;
   private int priorClickPopup = -1;
   private int afterClickPopup = -1;
   private boolean otherCanSee = true;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet modelSheet = wb.getSheet(1);
      parseModel(modelSheet);
      Sheet dySheet = wb.getSheet(2);
      BuffDynamicData.init(dySheet);
      Sheet bgSheet = wb.getSheet(3);
      BuffGroup.init(bgSheet);
      Sheet cpsSheet = wb.getSheet(5);
      ClientPerformStatus.init(cpsSheet);
      Sheet sstSheett = wb.getSheet(6);
      OperationLimitManager.init(sstSheett);
      ClickPopup.init(wb.getSheet(7));
   }

   private static void parseModel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         BuffModel model = new BuffModel();
         model.setModelID(Tools.getCellIntValue(sheet.getCell("A" + i)));
         model.setName(Tools.getCellValue(sheet.getCell("B" + i)));
         model.setBuffType(Tools.getCellIntValue(sheet.getCell("C" + i)));
         model.setIcon(Tools.getCellIntValue(sheet.getCell("D" + i)));
         model.setSmallIcon(Tools.getCellIntValue(sheet.getCell("E" + i)));
         model.setDes(Tools.getCellValue(sheet.getCell("F" + i)));
         model.setEffecteID(Tools.getCellIntValue(sheet.getCell("G" + i)));
         model.setDuration(Tools.getCellIntValue(sheet.getCell("H" + i)));
         model.setIntervalTime(Tools.getCellIntValue(sheet.getCell("I" + i)));
         model.setRate(Tools.getCellIntValue(sheet.getCell("J" + i)));
         model.setManulControl(Tools.getCellIntValue(sheet.getCell("K" + i)) == 1);
         model.setShow(Tools.getCellIntValue(sheet.getCell("L" + i)) == 1);
         model.setDieAway(Tools.getCellIntValue(sheet.getCell("M" + i)) == 1);
         model.setLogouAway(Tools.getCellIntValue(sheet.getCell("N" + i)) == 1);
         model.setLevelByCreator(Tools.getCellIntValue(sheet.getCell("O" + i)) == 1);
         model.setCalOfflineTime(Tools.getCellIntValue(sheet.getCell("P" + i)) == 1);
         model.setOverlap(Tools.getCellIntValue(sheet.getCell("Q" + i)) == 1);
         model.setMaxOverlap(Tools.getCellIntValue(sheet.getCell("R" + i)));
         model.setOverlapType(Tools.getCellIntValue(sheet.getCell("S" + i)));
         model.setDebuff(Tools.getCellIntValue(sheet.getCell("T" + i)) == 1);
         model.setServerStatusID(Tools.getCellIntValue(sheet.getCell("U" + i)));
         List dataOrder = StringTools.analyzeIntegerStatenum(Tools.getCellValue(sheet.getCell("V" + i)), ",");
         model.setDataOrder(dataOrder);
         String requestType = Tools.getCellValue(sheet.getCell("W" + i));
         String requestValue = Tools.getCellValue(sheet.getCell("X" + i));
         EffectRequirement request = EffectReCreator.createRequirement(requestType, requestValue);
         model.setRequirement(request);
         model.setPriorClickPopup(Tools.getCellIntValue(sheet.getCell("Y" + i)));
         model.setAfterClickPopup(Tools.getCellIntValue(sheet.getCell("Z" + i)));
         model.setOtherCanSee(Tools.getCellIntValue(sheet.getCell("AA" + i)) == 1);
         model.setClientStatusID(Tools.getCellIntValue(sheet.getCell("AB" + i)));
         model.setOrder(Tools.getCellIntValue(sheet.getCell("AC" + i)));
         models.put(model.getModelID(), model);
      }

   }

   public boolean isClickFunction() {
      return this.priorClickPopup != -1 || this.afterClickPopup != -1;
   }

   public static HashMap getModels() {
      return models;
   }

   public static BuffModel getModel(int modelID) {
      return (BuffModel)models.get(modelID);
   }

   public static boolean hasModel(int modelID) {
      return models.containsKey(modelID);
   }

   public static boolean showForOther(Creature owner, int modelID) {
      if (owner.getType() != 1) {
         return true;
      } else {
         BuffModel model = getModel(modelID);
         return model != null ? model.isOtherCanSee() : true;
      }
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getBuffType() {
      return this.buffType;
   }

   public void setBuffType(int buffType) {
      this.buffType = buffType;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getSmallIcon() {
      return this.smallIcon;
   }

   public int getClientStatusID() {
      return this.clientStatusID;
   }

   public void setClientStatusID(int clientStatusID) {
      this.clientStatusID = clientStatusID;
   }

   public void setSmallIcon(int smallIcon) {
      this.smallIcon = smallIcon;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getEffecteID() {
      return this.effecteID;
   }

   public void setEffecteID(int effecteID) {
      this.effecteID = effecteID;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public int getIntervalTime() {
      return this.intervalTime;
   }

   public void setIntervalTime(int intervalTime) {
      this.intervalTime = intervalTime;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public boolean isManulControl() {
      return this.manulControl;
   }

   public void setManulControl(boolean manulControl) {
      this.manulControl = manulControl;
   }

   public boolean isShow() {
      return this.show;
   }

   public void setShow(boolean show) {
      this.show = show;
   }

   public boolean isDieAway() {
      return this.dieAway;
   }

   public void setDieAway(boolean dieAway) {
      this.dieAway = dieAway;
   }

   public boolean isLogouAway() {
      return this.logouAway;
   }

   public void setLogouAway(boolean logouAway) {
      this.logouAway = logouAway;
   }

   public boolean isLevelByCreator() {
      return this.levelByCreator;
   }

   public void setLevelByCreator(boolean levelByCreator) {
      this.levelByCreator = levelByCreator;
   }

   public boolean isCalOfflineTime() {
      return this.calOfflineTime;
   }

   public void setCalOfflineTime(boolean calOfflineTime) {
      this.calOfflineTime = calOfflineTime;
   }

   public boolean isOverlap() {
      return this.overlap;
   }

   public void setOverlap(boolean overlap) {
      this.overlap = overlap;
   }

   public int getMaxOverlap() {
      return this.maxOverlap;
   }

   public void setMaxOverlap(int maxOverlap) {
      this.maxOverlap = maxOverlap;
   }

   public boolean isDebuff() {
      return this.isDebuff;
   }

   public void setDebuff(boolean isDebuff) {
      this.isDebuff = isDebuff;
   }

   public List getDataOrder() {
      return this.dataOrder;
   }

   public void setDataOrder(List dataOrder) {
      this.dataOrder = dataOrder;
   }

   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public EffectRequirement getRequirement() {
      return this.requirement;
   }

   public void setRequirement(EffectRequirement requirement) {
      this.requirement = requirement;
   }

   public int getPriorClickPopup() {
      return this.priorClickPopup;
   }

   public void setPriorClickPopup(int priorClickPopup) {
      this.priorClickPopup = priorClickPopup;
   }

   public int getAfterClickPopup() {
      return this.afterClickPopup;
   }

   public void setAfterClickPopup(int afterClickPopup) {
      this.afterClickPopup = afterClickPopup;
   }

   public boolean isOtherCanSee() {
      return this.otherCanSee;
   }

   public void setOtherCanSee(boolean otherCanSee) {
      this.otherCanSee = otherCanSee;
   }

   public int getOverlapType() {
      return this.overlapType;
   }

   public void setOverlapType(int overlapType) {
      this.overlapType = overlapType;
   }

   public int getServerStatusID() {
      return this.serverStatusID;
   }

   public void setServerStatusID(int serverStatusID) {
      this.serverStatusID = serverStatusID;
   }
}
