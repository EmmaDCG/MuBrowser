package com.mu.game.model.item.model;

import com.mu.game.model.equip.equipSet.EquipSetModel;
import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.equip.external.ArmorEffectData;
import com.mu.game.model.equip.external.EquipExternalType;
import com.mu.game.model.equip.external.EquipmentEffect;
import com.mu.game.model.equip.external.WeaponEntry;
import com.mu.game.model.equip.external.WeaponStarExternal;
import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.action.ItemActionFactory;
import com.mu.game.model.item.amount.ItemAmount;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import jxl.Sheet;
import jxl.Workbook;

public class ItemModel {
   private static HashMap modelMap = new HashMap();
   private int id;
   private String name;
   private int icon;
   private int smallIcon;
   private String des;
   private int price;
   private int sellNpcPrice;
   private int maxStackCount;
   private int useNumber;
   private int level;
   private int quality;
   private int starLevel;
   private int useLevel;
   private int coolTime;
   private int duration;
   private int sort;
   private int itemType;
   private int compareType;
   private int durability;
   private int mendCount;
   private int sets = -1;
   private int gender;
   private HashSet profession;
   private int changeJob = 0;
   private boolean canExchange;
   private boolean canPutToWareHouse;
   private boolean canSellToNpc;
   private boolean canDiscard;
   private boolean isBind;
   private boolean canSplit;
   private boolean directly;
   private int skill;
   private ArrayList itemStats = null;
   private String itemStatSTr = "";
   private int actionID = -1;
   private ItemAction action = null;
   private boolean occupationCD = false;
   private int dropIcon;
   private boolean isMoney;
   private int roleExmGirl;
   private int externalModelMenRight;
   private int externalModelMenLeft;
   private int roleExmMen;
   private int external3D;
   private int nextLevelItem;
   private int timeTriggerType;
   private boolean timeEndDisappear = true;
   private int bindTriggerType = 0;
   private ArrayList dynData = new ArrayList();
   private int useType;
   private boolean canBatch;
   private String obtainDes;
   private String professionStr;
   private boolean isNumbericType;
   private SortedMap needBasicPro = new TreeMap();
   private SortedMap starActivation = new TreeMap();
   private int musicID;
   private boolean canRepair = true;
   private int domineering = 0;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      initCommonItem(sheet);
      initCommonItem(wb.getSheet(2));
      EquipSetModel.init(wb);
      ItemColor.init(wb.getSheet(5));
      WeaponEntry.init(wb.getSheet(6));
      WeaponStarExternal.init(wb.getSheet(7));
      ItemCurrency.init(wb.getSheet(8));
      EquipSlot.init(wb.getSheet(9));
      EquipExternalType.init(wb.getSheet(10));
      EquipmentEffect.init(wb.getSheet(11));
      ItemAmount.init(wb.getSheet(12));
      initStatSortName(wb.getSheet(13));
      ArmorEffectData.initData(wb.getSheet(14));
      ArmorEffectData.initAtomEffect(wb.getSheet(15));
      changeBatch(wb.getSheet(16));
   }

   private static void initStatSortName(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int sortID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int font = Tools.getCellIntValue(sheet.getCell("C" + i));
         ItemConstant.setStatSort(new EquipStatSort(sortID, name, font));
      }

   }

   public static void initStarActivation(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int starLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         String statStr = Tools.getCellValue(sheet.getCell("C" + i));
         if (statStr.trim().length() < 1) {
            throw new Exception(sheet.getName() + ",第" + i + "行，没有数据");
         }

         String[] splits = statStr.split(",");
         List statList = new ArrayList();
         String[] var11 = splits;
         int var10 = splits.length;

         for(int var9 = 0; var9 < var10; ++var9) {
            String s = var11[var9];
            EquipStat stat = EquipStat.getEquipStat(Integer.parseInt(s));
            statList.add(stat);
         }

         if (starLevel <= 0 || statList.size() < 1) {
            throw new Exception(sheet.getName() + ",第" + i + "行，填写错误");
         }

         ItemModel model = getModel(modelID);
         if (model == null) {
            throw new Exception(sheet.getName() + ",第" + i + "行，找不到相应的模板ID");
         }

         model.getStarActivation().put(starLevel, statList);
      }

   }

   public static void changeBatch(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         ItemModel model = getModel(modelID);
         if (model != null) {
            model.setCanBatch(true);
         }
      }

   }

   public static void initCommonItem(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int j = 2; j <= rows; ++j) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + j));
         ItemModel model = new ItemModel(id);
         model.setLevel(Tools.getCellIntValue(sheet.getCell("B" + j)));
         model.setSort(Tools.getCellIntValue(sheet.getCell("C" + j)));
         model.setQuality(Tools.getCellIntValue(sheet.getCell("D" + j)));
         model.setName(sheet.getCell("E" + j).getContents());
         model.setProfession(StringTools.analyzeIntegerHashset(Tools.getCellValue(sheet.getCell("F" + j)), ","));
         model.setItemType(Tools.getCellIntValue(sheet.getCell("G" + j)));
         model.setGender(Tools.getCellIntValue(sheet.getCell("H" + j)));
         model.setDurability(Tools.getCellIntValue(sheet.getCell("I" + j)));
         String skills = Tools.getCellValue(sheet.getCell("J" + j));
         model.setSkill(skills != null && skills.trim().length() >= 1 ? Integer.parseInt(skills) : -1);
         model.setUseLevel(Tools.getCellIntValue(sheet.getCell("K" + j)));
         model.setDirectly(Tools.getCellIntValue(sheet.getCell("M" + j)) == 1);
         model.setCoolTime(Tools.getCellIntValue(sheet.getCell("P" + j)));
         model.setDuration(Tools.getCellIntValue(sheet.getCell("Q" + j)));
         model.setMaxStackCount(Tools.getCellIntValue(sheet.getCell("R" + j)));
         model.setCanSplit(Tools.getCellIntValue(sheet.getCell("S" + j)) == 1);
         model.setMoney(Tools.getCellIntValue(sheet.getCell("T" + j)) == 1);
         model.setCanExchange(Tools.getCellIntValue(sheet.getCell("V" + j)) == 1);
         model.setCanDiscard(Tools.getCellIntValue(sheet.getCell("W" + j)) == 1);
         model.setCanSellToNpc(Tools.getCellIntValue(sheet.getCell("X" + j)) == 1);
         model.setPrice(Tools.getCellIntValue(sheet.getCell("Z" + j)));
         model.setSellNpcPrice(Tools.getCellIntValue(sheet.getCell("AA" + j)));
         model.setDropIcon(Tools.getCellIntValue(sheet.getCell("AB" + j)));
         model.setIcon(Tools.getCellIntValue(sheet.getCell("AC" + j)));
         model.setExternalModelMenRight(Tools.getCellIntValue(sheet.getCell("AD" + j)));
         model.setExternalModelMenLeft(Tools.getCellIntValue(sheet.getCell("AE" + j)));
         model.setDes(Tools.getCellValue(sheet.getCell("AH" + j)));
         String actionIDStr = sheet.getCell("AJ" + j).getContents();
         String needBasicStr;
         if (actionIDStr != null && actionIDStr.trim().length() >= 1) {
            int actionID = Tools.getCellIntValue(sheet.getCell("AJ" + j));
            needBasicStr = Tools.getCellValue(sheet.getCell("AK" + j));
            ItemAction action = ItemActionFactory.createAction(actionID, needBasicStr);
            model.setActionID(actionID);
            model.setAction(action);
         }

         model.setUseType(Tools.getCellIntValue(sheet.getCell("AL" + j)));
         model.setSmallIcon(Tools.getCellIntValue(sheet.getCell("AM" + j)));
         ArrayList modifies = StringTools.analyzeItemBasicModifies(Tools.getCellValue(sheet.getCell("AN" + j)));
         model.setItemStats(modifies);
         model.setObtainDes(Tools.getCellValue(sheet.getCell("AO" + j)));
         model.setProfessionStr(Tools.getCellValue(sheet.getCell("AP" + j)));
         model.setExternal3D(Tools.getCellIntValue(sheet.getCell("AQ" + j)));
         model.setCompareType(Tools.getCellIntValue(sheet.getCell("AR" + j)));
         model.setNumbericType(Tools.getCellIntValue(sheet.getCell("AT" + j)) == 1);
         model.setCanPutToWareHouse(Tools.getCellIntValue(sheet.getCell("AU" + j)) == 1);
         needBasicStr = sheet.getCell("AV" + j).getContents();
         model.setMusicID(Tools.getCellIntValue(sheet.getCell("AW" + j)));
         model.setCanRepair(Tools.getCellIntValue(sheet.getCell("AX" + j)) == 1);
         model.setDomineering(Tools.getCellIntValue(sheet.getCell("AZ" + j)));
         if (model.isnotStorage() && model.getAction() == null) {
            throw new Exception("itemmodel 直接使用的没有使用方式 modelID = " + id);
         }

         HashSet professions = model.getProfession();
         if (professions.contains(Integer.valueOf(-1))) {
            professions.clear();
         }

         HashMap needMap = StringTools.analyzeIntegerMap(needBasicStr, ",");
         Iterator it = needMap.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            StatEnum stat = StatEnum.find(((Integer)entry.getKey()).intValue());
            if (stat == StatEnum.None || ((Integer)entry.getValue()).intValue() <= 0) {
               throw new Exception(sheet.getName() + ",第" + j + "行，需求点填写错误");
            }

            model.getNeedBasicPro().put(stat, (Integer)entry.getValue());
         }

         addModel(model);
      }

   }

   public static void check() throws Exception {
      Iterator var1 = modelMap.values().iterator();

      while(var1.hasNext()) {
         ItemModel model = (ItemModel)var1.next();
         switch(model.getItemType()) {
         case 31:
         case 32:
            int actionID = model.getActionID();
            if (actionID != 0 && actionID != 1 && actionID != 2) {
               throw new Exception("物品ID = " + model.getID() + "功能配置错误： 自动吃药功能-只能是血或者蓝");
            }
         }

         if (model.getSort() == 12 && !StoneDataManager.hasStoneStat(model.getID())) {
            throw new Exception("物品ID = " + model.getID() + " - 宝石道具没有相应的属性");
         }

         ItemAction action = model.getAction();
         if (action != null) {
            action.initCheck("道具模板ID = " + model.getID());
         }
      }

   }

   public static ItemModel getModel(int id) {
      return (ItemModel)modelMap.get(id);
   }

   public static void addModel(ItemModel model) {
      if (model != null) {
         modelMap.put(model.getID(), model);
      }

   }

   public boolean isInProType(int proType) {
      HashSet proIds = this.getProfession();
      if (proIds.isEmpty()) {
         return true;
      } else {
         List idList = Profession.getIDList(proType);
         if (idList == null) {
            return false;
         } else {
            Iterator var5 = idList.iterator();

            while(var5.hasNext()) {
               Integer id = (Integer)var5.next();
               if (proIds.contains(id)) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   public static HashMap getModelMap() {
      return modelMap;
   }

   public static boolean hasItemModel(int modelID) {
      return modelMap.containsKey(modelID);
   }

   public List cloneItemStats() {
      List modifies = null;
      if (this.itemStats == null) {
         return modifies;
      } else {
         ItemModify nm;
         for(Iterator var3 = this.itemStats.iterator(); var3.hasNext(); modifies.add(nm)) {
            ItemModify modify = (ItemModify)var3.next();
            nm = modify.cloneModify();
            if (modifies == null) {
               modifies = new ArrayList();
            }
         }

         return modifies;
      }
   }

   public boolean isnotStorage() {
      return this.getUseType() == 1;
   }

   public boolean isCanBatch() {
      return this.canBatch;
   }

   public boolean isEquipment() {
      return this.getSort() == 1;
   }

   public boolean isEquipSet() {
      if (!this.isEquipment()) {
         return false;
      } else {
         return this.sets != -1;
      }
   }

   public String structrueStatDes() {
      StringBuffer sb = new StringBuffer();
      Iterator var3 = this.itemStats.iterator();

      while(var3.hasNext()) {
         ItemModify modify = (ItemModify)var3.next();
         if (modify.getStat() != StatEnum.WEAPON_MAX_ATK) {
            StatEnum stat = modify.getStat();
            sb.append(stat.getName() + " + ");
            sb.append(modify.getShowValue());
            switch($SWITCH_TABLE$com$mu$game$model$stats$StatEnum()[stat.ordinal()]) {
            case 35:
               sb.append(" - " + this.getFirstStatValue(StatEnum.WEAPON_MAX_ATK));
            }

            sb.append(modify.isShowPercent() ? "%" : "");
            sb.append("#n:{4}");
         }
      }

      return sb.toString();
   }

   public void setItemStats(ArrayList itemStats) {
      this.itemStats = itemStats;
      this.itemStatSTr = this.structrueStatDes();
   }

   public int getFirstStatValue(StatEnum stat) {
      if (this.itemStats == null) {
         return 0;
      } else {
         Iterator var3 = this.itemStats.iterator();

         while(var3.hasNext()) {
            ItemModify modify = (ItemModify)var3.next();
            if (modify.getStat() == stat) {
               return modify.getValue();
            }
         }

         return 0;
      }
   }

   public final int getExternalModelMenRight(int starLevel) {
      WeaponStarExternal external = WeaponStarExternal.getExternal(this.getID(), starLevel);
      return external != null ? external.getExternalModelMenRight() : this.getExternalModelMenRight();
   }

   public final int getExternalModelMenLeft(int starLevel) {
      WeaponStarExternal external = WeaponStarExternal.getExternal(this.getID(), starLevel);
      return external != null ? external.getExternalModelMenLeft() : this.getExternalModelMenLeft();
   }

   public ItemModel(int id) {
      this.id = id;
   }

   public String getItemStatSTr() {
      return this.itemStatSTr;
   }

   public final String getName() {
      return this.name;
   }

   public final void setName(String name) {
      this.name = name;
   }

   public int getDuration() {
      return this.duration;
   }

   public boolean isNumbericType() {
      return this.isNumbericType;
   }

   public void setNumbericType(boolean isNumbericType) {
      this.isNumbericType = isNumbericType;
   }

   public String getProfessionStr() {
      return this.professionStr;
   }

   public void setProfessionStr(String professionStr) {
      this.professionStr = professionStr;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public final int getID() {
      return this.id;
   }

   public final int getIcon() {
      return this.icon;
   }

   public int getSkill() {
      return this.skill;
   }

   public void setSkill(int skill) {
      this.skill = skill;
   }

   public final void setIcon(int icon) {
      this.icon = icon;
   }

   public final int getSmallIcon() {
      return this.smallIcon;
   }

   public final void setSmallIcon(int smallIcon) {
      this.smallIcon = smallIcon;
   }

   public final String getDes() {
      return this.des;
   }

   public final void setDes(String des) {
      this.des = des;
   }

   public final int getPrice() {
      return this.price;
   }

   public final void setPrice(int price) {
      this.price = price;
   }

   public final int getMaxStackCount() {
      return this.maxStackCount;
   }

   public final void setMaxStackCount(int maxStackCount) {
      this.maxStackCount = maxStackCount;
   }

   public final int getUseNumber() {
      return this.useNumber;
   }

   public final void setUseNumber(int useNumber) {
      this.useNumber = useNumber;
   }

   public final int getLevel() {
      return this.level;
   }

   public final void setLevel(int level) {
      this.level = level;
   }

   public final int getQuality() {
      return this.quality;
   }

   public int getSellNpcPrice() {
      return this.sellNpcPrice;
   }

   public void setSellNpcPrice(int sellNpcPrice) {
      this.sellNpcPrice = sellNpcPrice;
   }

   public final void setQuality(int quality) {
      this.quality = quality;
   }

   public final int getStarLevel() {
      return this.starLevel;
   }

   public final void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public final int getUseLevel() {
      return this.useLevel;
   }

   public final void setUseLevel(int useLevel) {
      this.useLevel = useLevel;
   }

   public final int getCoolTime() {
      return this.coolTime;
   }

   public final void setCoolTime(int coolTime) {
      this.coolTime = coolTime;
   }

   public final int getSort() {
      return this.sort;
   }

   public final void setSort(int sort) {
      this.sort = sort;
   }

   public final int getItemType() {
      return this.itemType;
   }

   public final void setItemType(int itemType) {
      this.itemType = itemType;
   }

   public final int getDurability() {
      return this.durability;
   }

   public final void setDurability(int durability) {
      this.durability = durability;
   }

   public final int getMendCount() {
      return this.mendCount;
   }

   public final void setMendCount(int mendCount) {
      this.mendCount = mendCount;
   }

   public final int getSets() {
      return this.sets;
   }

   public final void setSets(int sets) {
      this.sets = sets;
   }

   public final int getGender() {
      return this.gender;
   }

   public final void setGender(int gender) {
      this.gender = gender;
   }

   public HashSet getProfession() {
      return this.profession;
   }

   public void setProfession(HashSet profession) {
      this.profession = profession;
   }

   public final boolean isCanExchange() {
      return this.canExchange;
   }

   public final void setCanExchange(boolean canExchange) {
      this.canExchange = canExchange;
   }

   public final boolean isCanPutToWareHouse() {
      return this.canPutToWareHouse;
   }

   public final void setCanPutToWareHouse(boolean canPutToWareHouse) {
      this.canPutToWareHouse = canPutToWareHouse;
   }

   public final boolean isCanSellToNpc() {
      return this.canSellToNpc;
   }

   public final void setCanSellToNpc(boolean canSellToNpc) {
      this.canSellToNpc = canSellToNpc;
   }

   public final boolean isCanDiscard() {
      return this.canDiscard;
   }

   public final void setCanDiscard(boolean canDiscard) {
      this.canDiscard = canDiscard;
   }

   public final boolean isBind() {
      return this.isBind;
   }

   public final void setBind(boolean isBind) {
      this.isBind = isBind;
   }

   public final boolean isCanSplit() {
      return this.canSplit;
   }

   public final void setCanSplit(boolean canSplit) {
      this.canSplit = canSplit;
   }

   public final boolean isDirectly() {
      return this.directly;
   }

   public final void setDirectly(boolean directly) {
      this.directly = directly;
   }

   public final ItemAction getAction() {
      return this.action;
   }

   public final void setAction(ItemAction action) {
      this.action = action;
   }

   public final boolean isOccupationCD() {
      return this.occupationCD;
   }

   public final void setOccupationCD(boolean occupationCD) {
      this.occupationCD = occupationCD;
   }

   public final int getDropIcon() {
      return this.dropIcon;
   }

   public final void setDropIcon(int dropIcon) {
      this.dropIcon = dropIcon;
   }

   public final boolean isMoney() {
      return this.isMoney;
   }

   public final void setMoney(boolean isMoney) {
      this.isMoney = isMoney;
   }

   public final int getRoleExmGirl() {
      return this.roleExmGirl;
   }

   public final void setRoleExmGirl(int roleExmGirl) {
      this.roleExmGirl = roleExmGirl;
   }

   public int getActionID() {
      return this.actionID;
   }

   public void setActionID(int actionID) {
      this.actionID = actionID;
   }

   private final int getExternalModelMenRight() {
      return this.externalModelMenRight;
   }

   public final void setExternalModelMenRight(int externalModelMenRight) {
      this.externalModelMenRight = externalModelMenRight;
   }

   private final int getExternalModelMenLeft() {
      return this.externalModelMenLeft;
   }

   public final void setExternalModelMenLeft(int externalModelMenLeft) {
      this.externalModelMenLeft = externalModelMenLeft;
   }

   public final int getRoleExmMen() {
      return this.roleExmMen;
   }

   public final void setRoleExmMen(int roleExmMen) {
      this.roleExmMen = roleExmMen;
   }

   public final int getNextLevelItem() {
      return this.nextLevelItem;
   }

   public final void setNextLevelItem(int nextLevelItem) {
      this.nextLevelItem = nextLevelItem;
   }

   public final int getTimeTriggerType() {
      return this.timeTriggerType;
   }

   public final void setTimeTriggerType(int timeTriggerType) {
      this.timeTriggerType = timeTriggerType;
   }

   public final boolean isTimeEndDisappear() {
      return this.timeEndDisappear;
   }

   public final void setTimeEndDisappear(boolean timeEndDisappear) {
      this.timeEndDisappear = timeEndDisappear;
   }

   public final int getBindTriggerType() {
      return this.bindTriggerType;
   }

   public final void setBindTriggerType(int bindTriggerType) {
      this.bindTriggerType = bindTriggerType;
   }

   public final int getUseType() {
      return this.useType;
   }

   public final void setUseType(int useType) {
      this.useType = useType;
   }

   public final int getChangeJob() {
      return this.changeJob;
   }

   public final void setChangeJob(int changeJob) {
      this.changeJob = changeJob;
   }

   public ArrayList getItemStats() {
      return this.itemStats;
   }

   public ArrayList getDynData() {
      return this.dynData;
   }

   public void setDynData(ArrayList dynData) {
      this.dynData = dynData;
   }

   public void setCanBatch(boolean canBatch) {
      this.canBatch = canBatch;
   }

   public String getObtainDes() {
      return this.obtainDes;
   }

   public void setObtainDes(String obtainDes) {
      this.obtainDes = obtainDes;
   }

   public int getExternal3D() {
      return this.external3D;
   }

   public void setExternal3D(int external3d) {
      this.external3D = external3d;
   }

   public int getCompareType() {
      return this.compareType;
   }

   public void setCompareType(int compareType) {
      this.compareType = compareType;
   }

   public SortedMap getNeedBasicPro() {
      return this.needBasicPro;
   }

   public void setNeedBasicPro(SortedMap needBasicPro) {
      this.needBasicPro = needBasicPro;
   }

   public SortedMap getStarActivation() {
      return this.starActivation;
   }

   public void setStarActivation(SortedMap starActivation) {
      this.starActivation = starActivation;
   }

   public int getMusicID() {
      return this.musicID;
   }

   public void setMusicID(int musicID) {
      this.musicID = musicID;
   }

   public boolean isCanRepair() {
      return this.canRepair;
   }

   public void setCanRepair(boolean canRepair) {
      this.canRepair = canRepair;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$mu$game$model$stats$StatEnum() {
      int[] var10000 = $SWITCH_TABLE$com$mu$game$model$stats$StatEnum;
      if ($SWITCH_TABLE$com$mu$game$model$stats$StatEnum != null) {
         return var10000;
      } else {
         int[] var0 = new int[StatEnum.values().length];

         try {
            var0[StatEnum.ABSORB_AG.ordinal()] = 138;
         } catch (NoSuchFieldError var146) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_HP.ordinal()] = 136;
         } catch (NoSuchFieldError var145) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_MP.ordinal()] = 134;
         } catch (NoSuchFieldError var144) {
            ;
         }

         try {
            var0[StatEnum.ABSORB_SD.ordinal()] = 140;
         } catch (NoSuchFieldError var143) {
            ;
         }

         try {
            var0[StatEnum.AG.ordinal()] = 19;
         } catch (NoSuchFieldError var142) {
            ;
         }

         try {
            var0[StatEnum.AG_RECOVER.ordinal()] = 21;
         } catch (NoSuchFieldError var141) {
            ;
         }

         try {
            var0[StatEnum.AG_REC_KILL_MONSTER.ordinal()] = 22;
         } catch (NoSuchFieldError var140) {
            ;
         }

         try {
            var0[StatEnum.ALL_BASIS.ordinal()] = 6;
         } catch (NoSuchFieldError var139) {
            ;
         }

         try {
            var0[StatEnum.AP.ordinal()] = 23;
         } catch (NoSuchFieldError var138) {
            ;
         }

         try {
            var0[StatEnum.AP_RECOVER.ordinal()] = 25;
         } catch (NoSuchFieldError var137) {
            ;
         }

         try {
            var0[StatEnum.ATK.ordinal()] = 26;
         } catch (NoSuchFieldError var136) {
            ;
         }

         try {
            var0[StatEnum.ATK_AG_REC_RATE.ordinal()] = 81;
         } catch (NoSuchFieldError var135) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_DAM.ordinal()] = 90;
         } catch (NoSuchFieldError var134) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RATE.ordinal()] = 89;
         } catch (NoSuchFieldError var133) {
            ;
         }

         try {
            var0[StatEnum.ATK_EXCELLENT_RES.ordinal()] = 91;
         } catch (NoSuchFieldError var132) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_DAM.ordinal()] = 97;
         } catch (NoSuchFieldError var131) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RATE.ordinal()] = 95;
         } catch (NoSuchFieldError var130) {
            ;
         }

         try {
            var0[StatEnum.ATK_FATAL_RES.ordinal()] = 96;
         } catch (NoSuchFieldError var129) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_DAM.ordinal()] = 93;
         } catch (NoSuchFieldError var128) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RATE.ordinal()] = 92;
         } catch (NoSuchFieldError var127) {
            ;
         }

         try {
            var0[StatEnum.ATK_LUCKY_RES.ordinal()] = 94;
         } catch (NoSuchFieldError var126) {
            ;
         }

         try {
            var0[StatEnum.ATK_MAX.ordinal()] = 28;
         } catch (NoSuchFieldError var125) {
            ;
         }

         try {
            var0[StatEnum.ATK_MIN.ordinal()] = 27;
         } catch (NoSuchFieldError var124) {
            ;
         }

         try {
            var0[StatEnum.ATK_MP_REC_RATE.ordinal()] = 83;
         } catch (NoSuchFieldError var123) {
            ;
         }

         try {
            var0[StatEnum.ATK_SPEED.ordinal()] = 67;
         } catch (NoSuchFieldError var122) {
            ;
         }

         try {
            var0[StatEnum.ATTACK_CAPABILITY.ordinal()] = 117;
         } catch (NoSuchFieldError var121) {
            ;
         }

         try {
            var0[StatEnum.AVD.ordinal()] = 33;
         } catch (NoSuchFieldError var120) {
            ;
         }

         try {
            var0[StatEnum.AVD_ABSOLUTE.ordinal()] = 34;
         } catch (NoSuchFieldError var119) {
            ;
         }

         try {
            var0[StatEnum.All_Points.ordinal()] = 56;
         } catch (NoSuchFieldError var118) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_HP_REC_RATE.ordinal()] = 85;
         } catch (NoSuchFieldError var117) {
            ;
         }

         try {
            var0[StatEnum.BEINJURED_SD_REC_RATE.ordinal()] = 87;
         } catch (NoSuchFieldError var116) {
            ;
         }

         try {
            var0[StatEnum.BIND_INGOT.ordinal()] = 44;
         } catch (NoSuchFieldError var115) {
            ;
         }

         try {
            var0[StatEnum.BIND_MONEY.ordinal()] = 42;
         } catch (NoSuchFieldError var114) {
            ;
         }

         try {
            var0[StatEnum.CD_BONUS.ordinal()] = 102;
         } catch (NoSuchFieldError var113) {
            ;
         }

         try {
            var0[StatEnum.CON.ordinal()] = 5;
         } catch (NoSuchFieldError var112) {
            ;
         }

         try {
            var0[StatEnum.Contribution.ordinal()] = 63;
         } catch (NoSuchFieldError var111) {
            ;
         }

         try {
            var0[StatEnum.DAM_ABSORB.ordinal()] = 69;
         } catch (NoSuchFieldError var110) {
            ;
         }

         try {
            var0[StatEnum.DAM_FORCE.ordinal()] = 80;
         } catch (NoSuchFieldError var109) {
            ;
         }

         try {
            var0[StatEnum.DAM_IGNORE.ordinal()] = 78;
         } catch (NoSuchFieldError var108) {
            ;
         }

         try {
            var0[StatEnum.DAM_PVP.ordinal()] = 77;
         } catch (NoSuchFieldError var107) {
            ;
         }

         try {
            var0[StatEnum.DAM_REDUCE.ordinal()] = 70;
         } catch (NoSuchFieldError var106) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION.ordinal()] = 76;
         } catch (NoSuchFieldError var105) {
            ;
         }

         try {
            var0[StatEnum.DAM_REFLECTION_PRO.ordinal()] = 75;
         } catch (NoSuchFieldError var104) {
            ;
         }

         try {
            var0[StatEnum.DAM_STRENGTHEN.ordinal()] = 71;
         } catch (NoSuchFieldError var103) {
            ;
         }

         try {
            var0[StatEnum.DEF.ordinal()] = 29;
         } catch (NoSuchFieldError var102) {
            ;
         }

         try {
            var0[StatEnum.DEFENCE_CAPABILITY.ordinal()] = 118;
         } catch (NoSuchFieldError var101) {
            ;
         }

         try {
            var0[StatEnum.DEF_STRENGTH.ordinal()] = 30;
         } catch (NoSuchFieldError var100) {
            ;
         }

         try {
            var0[StatEnum.DEX.ordinal()] = 3;
         } catch (NoSuchFieldError var99) {
            ;
         }

         try {
            var0[StatEnum.DISARM.ordinal()] = 130;
         } catch (NoSuchFieldError var98) {
            ;
         }

         try {
            var0[StatEnum.DOMINEERING.ordinal()] = 65;
         } catch (NoSuchFieldError var97) {
            ;
         }

         try {
            var0[StatEnum.DOUBLE_ATK.ordinal()] = 68;
         } catch (NoSuchFieldError var96) {
            ;
         }

         try {
            var0[StatEnum.DROPRATE.ordinal()] = 62;
         } catch (NoSuchFieldError var95) {
            ;
         }

         try {
            var0[StatEnum.EVALUATION.ordinal()] = 57;
         } catch (NoSuchFieldError var94) {
            ;
         }

         try {
            var0[StatEnum.EVIL.ordinal()] = 48;
         } catch (NoSuchFieldError var93) {
            ;
         }

         try {
            var0[StatEnum.EXP.ordinal()] = 38;
         } catch (NoSuchFieldError var92) {
            ;
         }

         try {
            var0[StatEnum.EXP_BONUS.ordinal()] = 40;
         } catch (NoSuchFieldError var91) {
            ;
         }

         try {
            var0[StatEnum.FROST.ordinal()] = 122;
         } catch (NoSuchFieldError var90) {
            ;
         }

         try {
            var0[StatEnum.HIT.ordinal()] = 31;
         } catch (NoSuchFieldError var89) {
            ;
         }

         try {
            var0[StatEnum.HIT_ABSOLUTE.ordinal()] = 32;
         } catch (NoSuchFieldError var88) {
            ;
         }

         try {
            var0[StatEnum.HP.ordinal()] = 7;
         } catch (NoSuchFieldError var87) {
            ;
         }

         try {
            var0[StatEnum.HP_RECOVER.ordinal()] = 9;
         } catch (NoSuchFieldError var86) {
            ;
         }

         try {
            var0[StatEnum.HP_REC_KILL_MONSTER.ordinal()] = 10;
         } catch (NoSuchFieldError var85) {
            ;
         }

         try {
            var0[StatEnum.HisContribution.ordinal()] = 64;
         } catch (NoSuchFieldError var84) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF.ordinal()] = 79;
         } catch (NoSuchFieldError var83) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_DEF_PRO.ordinal()] = 73;
         } catch (NoSuchFieldError var82) {
            ;
         }

         try {
            var0[StatEnum.IGNORE_SD_PRO.ordinal()] = 74;
         } catch (NoSuchFieldError var81) {
            ;
         }

         try {
            var0[StatEnum.INGOT.ordinal()] = 43;
         } catch (NoSuchFieldError var80) {
            ;
         }

         try {
            var0[StatEnum.INT.ordinal()] = 4;
         } catch (NoSuchFieldError var79) {
            ;
         }

         try {
            var0[StatEnum.INVINCIBLE.ordinal()] = 142;
         } catch (NoSuchFieldError var78) {
            ;
         }

         try {
            var0[StatEnum.ITEM_USERLEVEL_DOWN.ordinal()] = 54;
         } catch (NoSuchFieldError var77) {
            ;
         }

         try {
            var0[StatEnum.LEVEL.ordinal()] = 37;
         } catch (NoSuchFieldError var76) {
            ;
         }

         try {
            var0[StatEnum.LEVELGAP.ordinal()] = 61;
         } catch (NoSuchFieldError var75) {
            ;
         }

         try {
            var0[StatEnum.LUCKY.ordinal()] = 46;
         } catch (NoSuchFieldError var74) {
            ;
         }

         try {
            var0[StatEnum.MASTER_SKILL.ordinal()] = 58;
         } catch (NoSuchFieldError var73) {
            ;
         }

         try {
            var0[StatEnum.MAX_AG.ordinal()] = 20;
         } catch (NoSuchFieldError var72) {
            ;
         }

         try {
            var0[StatEnum.MAX_AP.ordinal()] = 24;
         } catch (NoSuchFieldError var71) {
            ;
         }

         try {
            var0[StatEnum.MAX_EXP.ordinal()] = 39;
         } catch (NoSuchFieldError var70) {
            ;
         }

         try {
            var0[StatEnum.MAX_HP.ordinal()] = 8;
         } catch (NoSuchFieldError var69) {
            ;
         }

         try {
            var0[StatEnum.MAX_MP.ordinal()] = 12;
         } catch (NoSuchFieldError var68) {
            ;
         }

         try {
            var0[StatEnum.MAX_SD.ordinal()] = 16;
         } catch (NoSuchFieldError var67) {
            ;
         }

         try {
            var0[StatEnum.MONEY.ordinal()] = 41;
         } catch (NoSuchFieldError var66) {
            ;
         }

         try {
            var0[StatEnum.MONEY_ADD_WKM.ordinal()] = 145;
         } catch (NoSuchFieldError var65) {
            ;
         }

         try {
            var0[StatEnum.MP.ordinal()] = 11;
         } catch (NoSuchFieldError var64) {
            ;
         }

         try {
            var0[StatEnum.MP_RECOVER.ordinal()] = 13;
         } catch (NoSuchFieldError var63) {
            ;
         }

         try {
            var0[StatEnum.MP_REC_KILL_MONSTER.ordinal()] = 14;
         } catch (NoSuchFieldError var62) {
            ;
         }

         try {
            var0[StatEnum.None.ordinal()] = 1;
         } catch (NoSuchFieldError var61) {
            ;
         }

         try {
            var0[StatEnum.PARALYSIS.ordinal()] = 126;
         } catch (NoSuchFieldError var60) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_AG_REC.ordinal()] = 82;
         } catch (NoSuchFieldError var59) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_ATK_MP_REC.ordinal()] = 84;
         } catch (NoSuchFieldError var58) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_HP_REC.ordinal()] = 86;
         } catch (NoSuchFieldError var57) {
            ;
         }

         try {
            var0[StatEnum.PERCENT_BEINJURED_SD_REC.ordinal()] = 88;
         } catch (NoSuchFieldError var56) {
            ;
         }

         try {
            var0[StatEnum.PETRIFICATION.ordinal()] = 124;
         } catch (NoSuchFieldError var55) {
            ;
         }

         try {
            var0[StatEnum.PKMODE.ordinal()] = 55;
         } catch (NoSuchFieldError var54) {
            ;
         }

         try {
            var0[StatEnum.POISONING.ordinal()] = 120;
         } catch (NoSuchFieldError var53) {
            ;
         }

         try {
            var0[StatEnum.POISONING_ATK_HURT.ordinal()] = 111;
         } catch (NoSuchFieldError var52) {
            ;
         }

         try {
            var0[StatEnum.POTENTIAL.ordinal()] = 47;
         } catch (NoSuchFieldError var51) {
            ;
         }

         try {
            var0[StatEnum.PROBABILITY.ordinal()] = 49;
         } catch (NoSuchFieldError var50) {
            ;
         }

         try {
            var0[StatEnum.REDEEM_POINTS.ordinal()] = 53;
         } catch (NoSuchFieldError var49) {
            ;
         }

         try {
            var0[StatEnum.RESURRENTION.ordinal()] = 128;
         } catch (NoSuchFieldError var48) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_AG.ordinal()] = 139;
         } catch (NoSuchFieldError var47) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_HP.ordinal()] = 137;
         } catch (NoSuchFieldError var46) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_MP.ordinal()] = 135;
         } catch (NoSuchFieldError var45) {
            ;
         }

         try {
            var0[StatEnum.RES_ABSORB_SD.ordinal()] = 141;
         } catch (NoSuchFieldError var44) {
            ;
         }

         try {
            var0[StatEnum.RES_DISARM.ordinal()] = 131;
         } catch (NoSuchFieldError var43) {
            ;
         }

         try {
            var0[StatEnum.RES_FROST.ordinal()] = 123;
         } catch (NoSuchFieldError var42) {
            ;
         }

         try {
            var0[StatEnum.RES_PARALYSIS.ordinal()] = 127;
         } catch (NoSuchFieldError var41) {
            ;
         }

         try {
            var0[StatEnum.RES_PETRIFICATION.ordinal()] = 125;
         } catch (NoSuchFieldError var40) {
            ;
         }

         try {
            var0[StatEnum.RES_POISONING.ordinal()] = 121;
         } catch (NoSuchFieldError var39) {
            ;
         }

         try {
            var0[StatEnum.RES_RESURRENTION.ordinal()] = 129;
         } catch (NoSuchFieldError var38) {
            ;
         }

         try {
            var0[StatEnum.RES_SACKED.ordinal()] = 133;
         } catch (NoSuchFieldError var37) {
            ;
         }

         try {
            var0[StatEnum.RES_WIND.ordinal()] = 144;
         } catch (NoSuchFieldError var36) {
            ;
         }

         try {
            var0[StatEnum.RNG.ordinal()] = 66;
         } catch (NoSuchFieldError var35) {
            ;
         }

         try {
            var0[StatEnum.SACKED.ordinal()] = 132;
         } catch (NoSuchFieldError var34) {
            ;
         }

         try {
            var0[StatEnum.SD.ordinal()] = 15;
         } catch (NoSuchFieldError var33) {
            ;
         }

         try {
            var0[StatEnum.SD_RECOVER.ordinal()] = 17;
         } catch (NoSuchFieldError var32) {
            ;
         }

         try {
            var0[StatEnum.SD_REC_KILL_MONSTER.ordinal()] = 18;
         } catch (NoSuchFieldError var31) {
            ;
         }

         try {
            var0[StatEnum.SD_REDUCTION.ordinal()] = 72;
         } catch (NoSuchFieldError var30) {
            ;
         }

         try {
            var0[StatEnum.SKILL_AG_REDUCE.ordinal()] = 100;
         } catch (NoSuchFieldError var29) {
            ;
         }

         try {
            var0[StatEnum.SKILL_ATK.ordinal()] = 98;
         } catch (NoSuchFieldError var28) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CASTTIME.ordinal()] = 105;
         } catch (NoSuchFieldError var27) {
            ;
         }

         try {
            var0[StatEnum.SKILL_CD.ordinal()] = 101;
         } catch (NoSuchFieldError var26) {
            ;
         }

         try {
            var0[StatEnum.SKILL_COEFFICIENT.ordinal()] = 107;
         } catch (NoSuchFieldError var25) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA3.ordinal()] = 114;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA4.ordinal()] = 115;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA5.ordinal()] = 116;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_1.ordinal()] = 112;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DATA_2.ordinal()] = 113;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DEGREE.ordinal()] = 109;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            var0[StatEnum.SKILL_DISTANCE.ordinal()] = 108;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            var0[StatEnum.SKILL_EFFECT_NUMBER.ordinal()] = 103;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            var0[StatEnum.SKILL_MP_REDUCE.ordinal()] = 99;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            var0[StatEnum.SKILL_PASSIVE_STAT.ordinal()] = 146;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            var0[StatEnum.SKILL_RANGE.ordinal()] = 104;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            var0[StatEnum.SKILL_WEAPON_HURT.ordinal()] = 110;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var0[StatEnum.SPEED.ordinal()] = 45;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var0[StatEnum.STR.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_LUCKY.ordinal()] = 50;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NOBACK.ordinal()] = 51;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            var0[StatEnum.STRENGTH_NODESTROY.ordinal()] = 52;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[StatEnum.TIME.ordinal()] = 106;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[StatEnum.TRIGGER.ordinal()] = 119;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MAX_ATK.ordinal()] = 36;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[StatEnum.WEAPON_MIN_ATK.ordinal()] = 35;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[StatEnum.WIND.ordinal()] = 143;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[StatEnum.WORLDLEVEL.ordinal()] = 60;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[StatEnum.ZHUIJIA.ordinal()] = 59;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$mu$game$model$stats$StatEnum = var0;
         return var0;
      }
   }
}
