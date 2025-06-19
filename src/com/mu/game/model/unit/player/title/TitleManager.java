package com.mu.game.model.unit.player.title;

import com.mu.executor.Executor;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.title.AllTitleInfo;
import com.mu.io.game.packet.imp.title.PlayerChangeTitle;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;

public class TitleManager {
   public static final int Type_Normal = 1;
   public static final int Type_Vip = 2;
   private static HashMap titleInfoMap = new HashMap();
   private static HashMap vipTitleInfoMap = new HashMap();
   private static HashMap attrLimitMap = new HashMap();
   private ConcurrentHashMap titleMap = Tools.newConcurrentHashMap2();
   private Player owner = null;
   private int equipId = -1;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initAttrLimit(wb.getSheet(2));
      initTitleInfo(wb.getSheet(1));
   }

   private static void initTitleInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         int iconType = Tools.getCellIntValue(sheet.getCell("D" + i));
         String eDes = Tools.getCellValue(sheet.getCell("E" + i));
         String lDes = Tools.getCellValue(sheet.getCell("F" + i));
         String des = Tools.getCellValue(sheet.getCell("G" + i));
         boolean save = Tools.getCellIntValue(sheet.getCell("Q" + i)) == 1;
         TitleInfo ti = new TitleInfo(id);
         ti.setName(name);
         ti.setIcon(icon);
         ti.setIconType(iconType);
         ti.setEquipDes(eDes);
         ti.setLightDes(lDes);
         ti.setDes(des);
         ti.setSaveDB(save);
         parseAttr(ti.getEquipAttr(), Tools.getCellValue(sheet.getCell("H" + i)));
         parseAttr(ti.getLightAttr(), Tools.getCellValue(sheet.getCell("I" + i)));
         long time = Long.parseLong(Tools.getCellValue(sheet.getCell("J" + i)));
         ti.setActiveTime(time < 0L ? -1L : time * 60L * 1000L * 1L);
         ti.setSort(Tools.getCellIntValue(sheet.getCell("K" + i)));
         int type = Tools.getCellIntValue(sheet.getCell("L" + i));
         String reqStr = Tools.getCellValue(sheet.getCell("M" + i));
         String[] mutexStr = Tools.getCellValue(sheet.getCell("N" + i)).split(",");
         ti.setTitleType(type);
         ti.setGearRequirement(parseRequirement(type, reqStr));

         int equipZdl;
         int zdl;
         for(equipZdl = 0; equipZdl < mutexStr.length; ++equipZdl) {
            zdl = Integer.parseInt(mutexStr[equipZdl]);
            if (zdl > 0) {
               ti.addMutexId(Integer.parseInt(mutexStr[equipZdl]));
            }
         }

         equipZdl = Tools.getCellIntValue(sheet.getCell("O" + i));
         zdl = Tools.getCellIntValue(sheet.getCell("P" + i));
         if (equipZdl < 0 || zdl < 0) {
            throw new Exception(sheet.getName() + " - 战斗力数值出错");
         }

         ti.setEquipZdl(equipZdl);
         ti.setZdl(zdl);
         titleInfoMap.put(id, ti);
         if (type == 2) {
            vipTitleInfoMap.put(id, ti);
         }
      }

   }

   private static VipRequirement parseRequirement(int type, String value) {
      switch(type) {
      case 2:
         return createVipRequirement(value);
      default:
         return null;
      }
   }

   public static HashMap getVipTitleInfoMap() {
      return vipTitleInfoMap;
   }

   private static TitleInfo getVipTitleInfo(Player player) {
      if (player.getVipShowLevel() < 1) {
         return null;
      } else {
         Iterator it = vipTitleInfoMap.values().iterator();

         while(it.hasNext()) {
            TitleInfo ti = (TitleInfo)it.next();
            if (ti.getGearRequirement().math(player)) {
               return ti;
            }
         }

         return null;
      }
   }

   private static VipRequirement createVipRequirement(String value) {
      String[] str = value.split(",");
      VipRequirement req = new VipRequirement(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
      return req;
   }

   public static TitleInfo getTitleInfo(int id) {
      return (TitleInfo)titleInfoMap.get(id);
   }

   public int getEquipId() {
      return this.equipId;
   }

   public void setEquipId(int equipId) {
      this.equipId = equipId;
   }

   public static HashMap getTitleInfoMap() {
      return titleInfoMap;
   }

   private static void initAttrLimit(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      if (rows != 7) {
         throw new Exception("称号属性条数不对");
      } else {
         for(int i = 2; i <= rows; ++i) {
            attrLimitMap.put(Tools.getCellIntValue(sheet.getCell("A" + i)), true);
         }

      }
   }

   public void equipTitle(int tid, boolean equip) {
      if (!equip) {
         this.unEquip(tid, true);
      } else {
         if (this.getEquipId() == tid) {
            return;
         }

         this.unEquip(this.getEquipId(), false);
         Title title = this.getTitle(tid);
         if (title == null) {
            return;
         }

         title.setEquip(true);
         this.setEquipId(tid);
         this.resetProperties();
         AllTitleInfo.pushInfo(this.owner);
         PlayerChangeTitle.change(this.owner, tid);
         WriteOnlyPacket packet = Executor.UpdateTitleEquip.toPacket(this.owner.getID(), tid, true);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      }

   }

   private void unEquip(int tid, boolean reset) {
      if (this.getEquipId() != -1 && this.getEquipId() == tid) {
         this.setEquipId(-1);
         Title title = this.getTitle(tid);
         if (title != null) {
            title.setEquip(false);
            if (reset) {
               this.resetProperties();
               AllTitleInfo.pushInfo(this.owner);
               PlayerChangeTitle.change(this.owner, -1);
            }

            WriteOnlyPacket packet = Executor.UpdateTitleEquip.toPacket(this.owner.getID(), tid, false);
            this.owner.writePacket(packet);
            packet.destroy();
            packet = null;
         }

      }
   }

   private static void parseAttr(ArrayList list, String str) {
      if (str != null && !str.trim().equals("")) {
         String[] tmp = str.split(";");

         for(int i = 0; i < tmp.length; ++i) {
            String[] tmp2 = tmp[i].split(",");
            int aid = Integer.parseInt(tmp2[0]);
            if (!attrLimitMap.containsKey(aid)) {
               System.err.println("称号增加属性不在列表中");
            } else {
               list.add(new int[]{aid, Integer.parseInt(tmp2[1]), Integer.parseInt(tmp2[2])});
            }
         }

      }
   }

   public TitleManager(Player owner) {
      this.owner = owner;
   }

   public void addTitle(Title title) {
      this.titleMap.put(title.getId(), title);
   }

   public boolean gainATitle(int titleId) {
      TitleInfo ti = getTitleInfo(titleId);
      if (ti == null) {
         SystemMessage.writeMessage(this.owner, 25202);
         return false;
      } else {
         Title title = null;
         if (this.titleMap.containsKey(titleId)) {
            if (ti.getActiveTime() == -1L) {
               SystemMessage.writeMessage(this.owner, 25201);
               return false;
            }

            title = this.getTitle(titleId);
            title.setExpiredTime(title.getExpiredTime() + ti.getActiveTime());
         } else {
            title = new Title(titleId);
            title.setExpiredTime(ti.getActiveTime() < 0L ? -1L : System.currentTimeMillis() + ti.getActiveTime());
            if (this.equipId == -1) {
               this.equipId = titleId;
               title.setEquip(true);
               PlayerChangeTitle.change(this.owner, titleId);
            }

            this.addTitle(title);
         }

         this.resetProperties();
         AllTitleInfo.pushInfo(this.owner);
         this.insertTitle(titleId, title.getExpiredTime(), title.isEquip());
         return true;
      }
   }

   private void insertTitle(int titleId, long expiredTime, boolean isEquip) {
      TitleInfo ti = getTitleInfo(titleId);
      if (ti != null && ti.isSaveDB()) {
         WriteOnlyPacket packet = Executor.SaveTitle.toPacket(this.owner.getID(), titleId, expiredTime, isEquip);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      }
   }

   public void checkTime(long now) {
      Iterator it = this.titleMap.values().iterator();

      while(it.hasNext()) {
         Title title = (Title)it.next();
         if (title.getExpiredTime() != -1L && title.getExpiredTime() <= now) {
            this.deletTitle(title.getId());
         }
      }

   }

   public void deletTitle(int tid) {
      TitleInfo ti = getTitleInfo(tid);
      if (ti != null) {
         Title title = this.removeTitle(tid);
         if (title != null) {
            if (tid == this.equipId) {
               this.equipId = -1;
               PlayerChangeTitle.change(this.owner, -1);
            }

            this.resetProperties();
            AllTitleInfo.pushInfo(this.owner);
            this.deleteFromDb(tid);
         }
      }
   }

   public void deleteFromDb(int tid) {
      TitleInfo ti = getTitleInfo(tid);
      if (ti != null && ti.isSaveDB()) {
         WriteOnlyPacket packet = Executor.DeleteTitle.toPacket(this.owner.getID(), tid);
         this.owner.writePacket(packet);
         packet.destroy();
         packet = null;
      }
   }

   public void resetProperties() {
      ArrayList statList = new ArrayList();
      Iterator it = this.titleMap.values().iterator();

      while(true) {
         TitleInfo ti;
         do {
            if (!it.hasNext()) {
               if (this.getEquipId() != -1) {
                  TitleInfo ti2 = getTitleInfo(this.getEquipId());
                  if (ti2 != null) {
                     ArrayList equipList = ti2.getEquipAttr();
                     Iterator var13 = equipList.iterator();

                     while(var13.hasNext()) {
                        int[] in = (int[])var13.next();
                        StatEnum se = StatEnum.find(in[0]);
                        FinalModify fm = new FinalModify(se, in[1], StatModifyPriority.fine(in[2]));
                        statList.add(fm);
                     }

                     statList.add(new FinalModify(StatEnum.DOMINEERING, ti2.getZdl(), StatModifyPriority.ADD));
                  }
               }

               StatChange.addStat(this.owner, StatIdCreator.createTitleID(), statList, StatChange.isSendStat(this.owner));
               statList.clear();
               return;
            }

            Title title = (Title)it.next();
            ti = getTitleInfo(title.getId());
         } while(ti == null);

         ArrayList lightList = ti.getLightAttr();
         Iterator var7 = lightList.iterator();

         while(var7.hasNext()) {
            int[] in = (int[])var7.next();
            StatEnum se = StatEnum.find(in[0]);
            FinalModify fm = new FinalModify(se, in[1], StatModifyPriority.fine(in[2]));
            statList.add(fm);
         }

         statList.add(new FinalModify(StatEnum.DOMINEERING, ti.getZdl(), StatModifyPriority.ADD));
      }
   }

   public HashMap getAllLightAttr() {
      HashMap map = new HashMap();
      Iterator it = attrLimitMap.keySet().iterator();

      while(it.hasNext()) {
         int aid = ((Integer)it.next()).intValue();
         StatEnum se = StatEnum.find(aid);
         int[] all = new int[]{aid, 0, se.isPercent() ? 1 : 0};
         map.put(aid, all);
      }

      it = this.titleMap.values().iterator();

      while(true) {
         TitleInfo ti;
         do {
            if (!it.hasNext()) {
               return map;
            }

            Title title = (Title)it.next();
            ti = getTitleInfo(title.getId());
         } while(ti == null);

         ArrayList lightList = ti.getLightAttr();
         Iterator var7 = lightList.iterator();

         while(var7.hasNext()) {
            int[] in = (int[])var7.next();
            int[] tmp = (int[])map.get(in[0]);
            if (tmp != null) {
               tmp[1] += in[1];
            }
         }
      }
   }

   public ConcurrentHashMap getTitleMap() {
      return this.titleMap;
   }

   public Title getTitle(int tid) {
      return (Title)this.titleMap.get(tid);
   }

   public Title removeTitle(int tid) {
      return (Title)this.titleMap.remove(tid);
   }

   public void vipLevelChanged() {
      TitleInfo ti = getVipTitleInfo(this.owner);
      Title nowVipTitle = this.getOwnerVipTitle();
      if (ti == null) {
         if (nowVipTitle != null) {
            this.deletTitle(nowVipTitle.getId());
         }
      } else if (nowVipTitle != null) {
         if (ti.getId() != nowVipTitle.getId()) {
            boolean oldIsEquip = nowVipTitle.isEquip();
            this.deletTitle(nowVipTitle.getId());
            this.gainATitle(ti.getId());
            if (this.getEquipId() != ti.getId() && oldIsEquip) {
               this.equipTitle(ti.getId(), true);
            }
         }
      } else {
         this.gainATitle(ti.getId());
      }

   }

   public void checkVipTitleOnEterGame() {
      TitleInfo ti = getVipTitleInfo(this.owner);
      Title nowVipTitle = this.getOwnerVipTitle();
      if (ti == null) {
         if (nowVipTitle != null) {
            this.removeTitle(nowVipTitle.getId());
            this.deleteFromDb(nowVipTitle.getId());
         }
      } else {
         Title title;
         if (nowVipTitle != null) {
            if (ti.getId() != nowVipTitle.getId()) {
               this.removeTitle(nowVipTitle.getId());
               this.deleteFromDb(nowVipTitle.getId());
               title = new Title(ti.getId());
               if (nowVipTitle.isEquip()) {
                  title.setEquip(true);
                  this.equipId = title.getId();
               }

               this.addTitle(title);
               this.insertTitle(title.getId(), title.getExpiredTime(), title.isEquip());
            }
         } else {
            title = new Title(ti.getId());
            if (this.equipId < 1) {
               title.setEquip(true);
               this.equipId = title.getId();
            }

            this.addTitle(title);
            this.insertTitle(title.getId(), title.getExpiredTime(), title.isEquip());
         }
      }

   }

   private Title getOwnerVipTitle() {
      Iterator it = vipTitleInfoMap.values().iterator();

      while(it.hasNext()) {
         TitleInfo ti = (TitleInfo)it.next();
         Title title = this.getTitle(ti.getId());
         if (title != null) {
            return title;
         }
      }

      return null;
   }

   public void destroy() {
      if (this.titleMap != null) {
         this.titleMap.clear();
         this.titleMap = null;
      }

      this.owner = null;
   }
}
