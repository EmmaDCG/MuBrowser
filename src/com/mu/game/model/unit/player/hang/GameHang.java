package com.mu.game.model.unit.player.hang;

import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.hangset.HangSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class GameHang {
   private int hpPercent;
   private boolean autoHp;
   private int mpPercent;
   private boolean autoMp;
   private boolean autoBuyDrug;
   private boolean autoRevive;
   private boolean pickupHorse;
   private boolean pickupJewelry;
   private int hangMode;
   private int hangRange;
   private boolean autoPickupOther;
   private List pickupOtherSorts;
   private boolean autoPickupEquip;
   private List equipQuality;
   private boolean autoMend;
   private boolean autoSale;
   private List hangSkillList;
   private int skillProgramIndex;
   private HangSale hangSale;
   private Player owner;
   private int hangX;
   private int hangY;
   private boolean inHanging;
   private boolean update;

   public GameHang(Player owner) {
      this.hpPercent = HangConstant.Default_Medication_HP_Percent;
      this.autoHp = true;
      this.mpPercent = HangConstant.Default_Medication_MP_Percent;
      this.autoMp = true;
      this.autoBuyDrug = true;
      this.autoRevive = false;
      this.pickupHorse = true;
      this.pickupJewelry = true;
      this.hangMode = 0;
      this.hangRange = 10;
      this.autoPickupOther = true;
      this.pickupOtherSorts = new ArrayList();
      this.autoPickupEquip = true;
      this.equipQuality = new ArrayList();
      this.autoMend = true;
      this.autoSale = true;
      this.hangSkillList = new ArrayList();
      this.skillProgramIndex = 0;
      this.hangSale = new HangSale();
      this.inHanging = false;
      this.update = false;
      this.owner = owner;
      this.pickupOtherSorts.add(Integer.valueOf(23));
      this.pickupOtherSorts.add(Integer.valueOf(17));
      this.pickupOtherSorts.add(Integer.valueOf(3));
      this.pickupOtherSorts.add(Integer.valueOf(-1));
      List colors = ItemColor.getAllColors();
      Iterator var4 = colors.iterator();

      while(var4.hasNext()) {
         ItemColor color = (ItemColor)var4.next();
         this.equipQuality.add(color.getIdentity());
      }

      for(int i = 0; i < 3; ++i) {
         this.hangSkillList.add(new HangSkill(owner));
      }

   }

   public void load(String cs, String pickupOtherStr, String equipQualityStr, String hangSkillStr, String hangSales) {
      String[] splits;
      int i;
      if (cs != null && cs.length() > 0) {
         try {
            splits = cs.split(",");

            for(i = 0; i < splits.length; ++i) {
               this.setHangbyIndex(i, Integer.parseInt(splits[i]));
            }
         } catch (Exception var14) {
            this.setUpdate(true);
            var14.printStackTrace();
         }
      }

      List qualitys;
      try {
         qualitys = StringTools.analyzeIntegerList(pickupOtherStr, ",");
         this.setPickupOtherSorts(qualitys);
      } catch (Exception var12) {
         this.setUpdate(true);
         var12.printStackTrace();
      }

      try {
         qualitys = StringTools.analyzeIntegerList(equipQualityStr, ",");
         this.setEquipQuality(qualitys);
      } catch (Exception var11) {
         this.setUpdate(true);
         var11.printStackTrace();
      }

      if (hangSkillStr != null && hangSkillStr.length() > 0) {
         try {
            splits = hangSkillStr.split(";");

            for(i = 0; i < splits.length && i < 3; ++i) {
               List skills = StringTools.analyzeIntegerList(splits[i], ",");
               HangSkill hangSkill = this.getHangSkill(i);
               hangSkill.setHangSkills(skills);
            }
         } catch (Exception var13) {
            this.setUpdate(true);
            var13.printStackTrace();
         }
      }

      if (hangSales != null && hangSales.length() > 0) {
         try {
            this.getHangSale().loadSale(hangSales);
         } catch (Exception var10) {
            this.setUpdate(true);
            var10.printStackTrace();
         }
      }

   }

   public HangSkill getHangSkill(int index) {
      if (index >= this.hangSkillList.size()) {
         index = 0;
      }

      index = Math.max(0, index);
      return (HangSkill)this.hangSkillList.get(index);
   }

   public void addSkillWhenLevelUp(int skillID) {
      boolean flag = false;
      Iterator var4 = this.hangSkillList.iterator();

      while(var4.hasNext()) {
         HangSkill hangSkill = (HangSkill)var4.next();
         if (hangSkill.addSkillWhenLevelUp(skillID)) {
            flag = true;
         }
      }

      if (flag) {
         this.setUpdate(true);
         HangSet.sendToClient(this.getOwner(), false);
      }

   }

   public void setHangSkills(List hangSkills) {
      for(Integer i = Integer.valueOf(0); i.intValue() < hangSkills.size(); i = i.intValue() + 1) {
         HangSkill hangSkill = this.getHangSkill(i.intValue());
         hangSkill.setHangSkills((List)hangSkills.get(i.intValue()));
      }

   }

   public List getCurrentAssistSkills() {
      HangSkill hangSkill = this.getHangSkill(this.skillProgramIndex);
      return hangSkill.getAssistSkills();
   }

   public HashSet getCurrentAttackSkills() {
      HangSkill hangSkill = this.getHangSkill(this.skillProgramIndex);
      return hangSkill.getAttackSkills();
   }

   public String getHangSkillStr() {
      String s = "";

      HangSkill hangSkill;
      for(Iterator var3 = this.hangSkillList.iterator(); var3.hasNext(); s = s + hangSkill.getHangSkillStr() + ";") {
         hangSkill = (HangSkill)var3.next();
      }

      return s;
   }

   public String getHangSaleStr() {
      return this.getHangSale().getHangSaleStr();
   }

   public void setSkillProgramIndex(int skillProgramIndex) {
      skillProgramIndex = Math.max(0, Math.min(skillProgramIndex, 2));
      this.skillProgramIndex = skillProgramIndex;
   }

   private void setHangbyIndex(int index, int value) {
      switch(index) {
      case 0:
         this.setAutoHp(value != 0);
         break;
      case 1:
         this.setHpPercent(value);
         break;
      case 2:
         this.setAutoMp(value != 0);
         break;
      case 3:
         this.setMpPercent(value);
         break;
      case 4:
         this.setAutoBuyDrug(value != 0);
         break;
      case 5:
         this.setAutoRevive(value != 0);
         break;
      case 6:
         this.setHangRange(value);
         break;
      case 7:
         this.setAutoPickupOther(value != 0);
         break;
      case 8:
         this.setAutoPickupEquip(value != 0);
         break;
      case 9:
         this.setAutoMend(value != 0);
         break;
      case 10:
         this.setHangMode(value);
         break;
      case 11:
         this.setAutoSale(value != 0);
         break;
      case 12:
         this.setSkillProgramIndex(value);
         break;
      case 13:
         this.setPickupJewelry(value != 0);
         break;
      case 14:
         this.setPickupHorse(value != 0);
      }

   }

   public void setPickupOtherSorts(List pickupOtherSorts) {
      this.pickupOtherSorts.clear();
      Iterator var3 = pickupOtherSorts.iterator();

      while(var3.hasNext()) {
         Integer otherOption = (Integer)var3.next();
         this.setPickohterOption(otherOption.intValue());
      }

   }

   private void setPickohterOption(int otherOption) {
      switch(otherOption) {
      case -1:
      case 3:
      case 17:
      case 23:
         this.pickupOtherSorts.add(otherOption);
      default:
      }
   }

   public void setEquipQuality(List equipQuality) {
      this.equipQuality.clear();
      Iterator var3 = equipQuality.iterator();

      while(var3.hasNext()) {
         Integer qualityID = (Integer)var3.next();
         this.setQualityOption(qualityID.intValue());
      }

   }

   private void setQualityOption(int qualityID) {
      ItemColor color = ItemColor.find(qualityID);
      if (color.getIdentity() == qualityID) {
         this.equipQuality.add(qualityID);
      }
   }

   public String getCycleStr() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.isAutoHp() ? 1 : 0);
      sb.append(",");
      sb.append(this.getHpPercent());
      sb.append(",");
      sb.append(this.isAutoMp() ? 1 : 0);
      sb.append(",");
      sb.append(this.getMpPercent());
      sb.append(",");
      sb.append(this.isAutoBuyDrug() ? 1 : 0);
      sb.append(",");
      sb.append(this.isAutoRevive() ? 1 : 0);
      sb.append(",");
      sb.append(this.getHangRange());
      sb.append(",");
      sb.append(this.isAutoPickupOther() ? 1 : 0);
      sb.append(",");
      sb.append(this.isAutoPickupEquip() ? 1 : 0);
      sb.append(",");
      sb.append(this.isAutoMend() ? 1 : 0);
      sb.append(",");
      sb.append(this.getHangMode());
      sb.append(",");
      sb.append(this.isAutoSale() ? 1 : 0);
      sb.append(",");
      sb.append(this.getSkillProgramIndex());
      sb.append(",");
      sb.append(this.isPickupJewelry() ? 1 : 0);
      sb.append(",");
      sb.append(this.isPickupHorse() ? 1 : 0);
      return sb.toString();
   }

   public String getPickupOtherStr() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < this.pickupOtherSorts.size(); ++i) {
         sb.append(this.pickupOtherSorts.get(i));
         if (i < this.pickupOtherSorts.size() - 1) {
            sb.append(",");
         }
      }

      return sb.toString();
   }

   public String getEquipQualityStr() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < this.equipQuality.size(); ++i) {
         sb.append(this.equipQuality.get(i));
         if (i < this.equipQuality.size() - 1) {
            sb.append(",");
         }
      }

      return sb.toString();
   }

   public boolean isUpdate() {
      return this.update;
   }

   public void setUpdate(boolean update) {
      this.update = update;
   }

   public boolean isInHanging() {
      return this.inHanging;
   }

   public void setInHanging(boolean inHanging) {
      this.inHanging = inHanging;
   }

   public boolean isAssistedMode() {
      return this.hangMode == 1;
   }

   public int getHangMode() {
      return this.hangMode;
   }

   public void setHangMode(int hangMode) {
      if (hangMode != 1 && hangMode != 0) {
         hangMode = 0;
      }

      this.hangMode = hangMode;
   }

   public HangSale getHangSale() {
      return this.hangSale;
   }

   public void setHangSale(HangSale hangSale) {
      this.hangSale = hangSale;
   }

   public int getHangX() {
      return this.hangX;
   }

   public void setHangX(int hangX) {
      this.hangX = hangX;
   }

   public int getHangY() {
      return this.hangY;
   }

   public void setHangY(int hangY) {
      this.hangY = hangY;
   }

   public int getHpPercent() {
      return this.hpPercent;
   }

   public void setHpPercent(int hpPercent) {
      this.hpPercent = Math.max(1, Math.min(100, hpPercent));
   }

   public boolean isAutoHp() {
      return this.autoHp;
   }

   public void setAutoHp(boolean autoHp) {
      this.autoHp = true;
   }

   public int getMpPercent() {
      return this.mpPercent;
   }

   public void setMpPercent(int mpPercent) {
      this.mpPercent = Math.min(100, Math.max(1, mpPercent));
   }

   public boolean isAutoMp() {
      return this.autoMp;
   }

   public void setAutoMp(boolean autoMp) {
      this.autoMp = true;
   }

   public boolean isAutoBuyDrug() {
      return this.autoBuyDrug;
   }

   public void setAutoBuyDrug(boolean autoBuyDrug) {
      this.autoBuyDrug = autoBuyDrug;
   }

   public boolean isAutoRevive() {
      return this.autoRevive;
   }

   public void setAutoRevive(boolean autoRevive) {
      this.autoRevive = autoRevive;
   }

   public int getHangRange() {
      return this.hangRange;
   }

   public void setHangRange(int hangRange) {
      this.hangRange = Math.max(1, Math.min(hangRange, 20));
   }

   public boolean isAutoPickupOther() {
      return this.autoPickupOther;
   }

   public void setAutoPickupOther(boolean autoPickupOther) {
      this.autoPickupOther = autoPickupOther;
   }

   public boolean isAutoPickupEquip() {
      return this.autoPickupEquip;
   }

   public void setAutoPickupEquip(boolean autoPickupEquip) {
      this.autoPickupEquip = autoPickupEquip;
   }

   public boolean isAutoMend() {
      return this.autoMend;
   }

   public void setAutoMend(boolean autoMend) {
      this.autoMend = autoMend;
   }

   public List getPickupOtherSorts() {
      return this.pickupOtherSorts;
   }

   public List getEquipQuality() {
      return this.equipQuality;
   }

   public boolean isAutoSale() {
      return this.autoSale;
   }

   public void setAutoSale(boolean autoSale) {
      this.autoSale = autoSale;
   }

   public List getHangSkillList() {
      return this.hangSkillList;
   }

   public int getSkillProgramIndex() {
      return this.skillProgramIndex;
   }

   public boolean isPickupHorse() {
      return this.pickupHorse;
   }

   public void setPickupHorse(boolean pickupHorse) {
      this.pickupHorse = pickupHorse;
   }

   public boolean isPickupJewelry() {
      return this.pickupJewelry;
   }

   public void setPickupJewelry(boolean pickupJewelry) {
      this.pickupJewelry = pickupJewelry;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void setOwner(Player owner) {
      this.owner = owner;
   }

   public void destroy() {
      this.owner = null;
      if (this.pickupOtherSorts != null) {
         this.pickupOtherSorts.clear();
         this.pickupOtherSorts = null;
      }

      if (this.equipQuality != null) {
         this.equipQuality.clear();
         this.equipQuality = null;
      }

      if (this.hangSkillList != null) {
         Iterator var2 = this.hangSkillList.iterator();

         while(var2.hasNext()) {
            HangSkill hs = (HangSkill)var2.next();
            hs.destroy();
         }

         this.hangSkillList.clear();
         this.hangSkillList = null;
      }

   }
}
