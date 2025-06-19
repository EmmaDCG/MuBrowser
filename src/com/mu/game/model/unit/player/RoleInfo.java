package com.mu.game.model.unit.player;

import com.mu.config.Global;
import com.mu.game.model.equip.external.EquipmentEffect;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.equip.external.WeaponEntry;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemType;
import com.mu.game.model.item.model.MovementType;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RoleInfo {
   private long id;
   private String name;
   private int gender;
   private int proType;
   private int proLevel = 0;
   private int level;
   private int header = 0;
   private int mapID = 0;
   private int x;
   private int y;
   private long currentExp;
   private int currentHp;
   private int currentMp;
   private int currentAp;
   private int currentAg;
   private int currentSd;
   private int vipLevel;
   private int vipExp;
   private int pkStatus;
   private int evil;
   private String remoteHost;
   private int remotePort;
   private int todayOnlineTime;
   private int totalOnlineTime;
   private int todayOnlineClearTime;
   private long logoutTime;
   private long loginTime;
   private int money;
   private int bindIngot;
   private int basisSTR;
   private int basisDEX;
   private int basisCON;
   private int basisINT;
   private int basisMaxHp;
   private int basisMaxMp;
   private int basisMaxSD;
   private int basisMaxAG;
   private int basisMaxAP;
   private int basisMaxATK;
   private int basisMinATK;
   private int basisDEF;
   private int basisHit;
   private int basisAVD;
   private int potential;
   private int poSTR;
   private int poDEX;
   private int poCON;
   private int poINT;
   private int warComment;
   private long warCommentTime;
   private int redeemPoint;
   private long levelupTime;
   private int finishPreivew;
   private HashMap entries;
   private int movementType;
   private byte[] systemSetup;

   public RoleInfo() {
      this.pkStatus = PkEnum.Mode_Peace.getModeID();
      this.remoteHost = Global.getLocalHost();
      this.remotePort = Global.getGamePort();
      this.todayOnlineTime = 0;
      this.totalOnlineTime = 0;
      this.todayOnlineClearTime = 0;
      this.logoutTime = 0L;
      this.loginTime = 0L;
      this.money = 0;
      this.bindIngot = 0;
      this.warComment = 0;
      this.warCommentTime = 0L;
      this.redeemPoint = 0;
      this.levelupTime = 0L;
      this.finishPreivew = -1;
      this.entries = new HashMap();
      this.movementType = MovementType.None.getType();
      this.systemSetup = null;
   }

   public final long getID() {
      return this.id;
   }

   public final void setID(long id) {
      this.id = id;
   }

   public final String getName() {
      return this.name;
   }

   public final void setName(String name) {
      this.name = name;
   }

   public int getMoney() {
      return this.money;
   }

   public final void setMoney(int money) {
      this.money = money;
   }

   public final int getGender() {
      return this.gender;
   }

   public final void setGender(int gender) {
      this.gender = gender;
   }

   public void setSetup(byte[] bytes) {
      this.systemSetup = bytes;
   }

   public byte[] getSetup() {
      return this.systemSetup;
   }

   public final int getProType() {
      return this.proType;
   }

   public final void setProType(int proType) {
      this.proType = proType;
      Player.initExternal(this.entries, proType);
   }

   public int getRedeemPoint() {
      return this.redeemPoint;
   }

   public void setRedeemPoint(int redeemPoint) {
      this.redeemPoint = redeemPoint;
   }

   public final int getProLevel() {
      return this.proLevel;
   }

   public final void setProLevel(int proLevel) {
      proLevel = Math.min(proLevel, Profession.professionMaxLevel);
      proLevel = Math.max(0, proLevel);
      this.proLevel = proLevel;
   }

   public final int getLevel() {
      return this.level;
   }

   public final void setLevel(int level) {
      this.level = level;
   }

   public int getMapID() {
      return this.mapID;
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

   public int getPkStatus() {
      return this.pkStatus;
   }

   public void setPkStatus(int pkStatus) {
      this.pkStatus = pkStatus;
   }

   public int getEvil() {
      return this.evil;
   }

   public void setEvil(int evil) {
      this.evil = evil;
   }

   public long getCurrentExp() {
      return this.currentExp;
   }

   public void setCurrentExp(long currentExp) {
      this.currentExp = currentExp;
   }

   public int getCurrentHp() {
      return this.currentHp;
   }

   public void setCurrentHp(int currentHp) {
      this.currentHp = currentHp;
   }

   public int getCurrentMp() {
      return this.currentMp;
   }

   public void setCurrentMp(int currentMp) {
      this.currentMp = currentMp;
   }

   public void setMapID(int mapID) {
      this.mapID = mapID;
   }

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public String getRemoteHost() {
      return this.remoteHost;
   }

   public void setRemoteHost(String host) {
      this.remoteHost = host;
   }

   public int getRemotePort() {
      return this.remotePort;
   }

   public void setRemotePort(int port) {
      this.remotePort = port;
   }

   public int getBasisSTR() {
      return this.basisSTR;
   }

   public void setBasisSTR(int basisSTR) {
      this.basisSTR = basisSTR;
   }

   public int getBasisDEX() {
      return this.basisDEX;
   }

   public void setBasisDEX(int basisDEX) {
      this.basisDEX = basisDEX;
   }

   public int getBasisCON() {
      return this.basisCON;
   }

   public void setBasisCON(int basisCON) {
      this.basisCON = basisCON;
   }

   public int getBasisINT() {
      return this.basisINT;
   }

   public void setBasisINT(int basisINT) {
      this.basisINT = basisINT;
   }

   public int getBasisMaxHp() {
      return this.basisMaxHp;
   }

   public void setBasisMaxHp(int basisMaxHp) {
      this.basisMaxHp = basisMaxHp;
   }

   public int getBasisMaxMp() {
      return this.basisMaxMp;
   }

   public void setBasisMaxMp(int basisMaxMp) {
      this.basisMaxMp = basisMaxMp;
   }

   public int getBasisMaxSD() {
      return this.basisMaxSD;
   }

   public void setBasisMaxSD(int basisMaxSD) {
      this.basisMaxSD = basisMaxSD;
   }

   public int getBasisMaxAG() {
      return this.basisMaxAG;
   }

   public void setBasisMaxAG(int basisMaxAG) {
      this.basisMaxAG = basisMaxAG;
   }

   public int getBasisMaxAP() {
      return this.basisMaxAP;
   }

   public void setBasisMaxAP(int basisMaxAP) {
      this.basisMaxAP = basisMaxAP;
   }

   public int getBasisMaxATK() {
      return this.basisMaxATK;
   }

   public void setBasisMaxATK(int basisMaxATK) {
      this.basisMaxATK = basisMaxATK;
   }

   public int getBasisMinATK() {
      return this.basisMinATK;
   }

   public void setBasisMinATK(int basisMinATK) {
      this.basisMinATK = basisMinATK;
   }

   public int getBasisDEF() {
      return this.basisDEF;
   }

   public void setBasisDEF(int basisDEF) {
      this.basisDEF = basisDEF;
   }

   public int getBasisHit() {
      return this.basisHit;
   }

   public void setBasisHit(int basisHit) {
      this.basisHit = basisHit;
   }

   public int getBasisAVD() {
      return this.basisAVD;
   }

   public void setBasisAVD(int basisAVD) {
      this.basisAVD = basisAVD;
   }

   public int getPotential() {
      return this.potential;
   }

   public void setPotential(int potential) {
      this.potential = potential;
   }

   public int getPoSTR() {
      return this.poSTR;
   }

   public void setPoSTR(int poSTR) {
      this.poSTR = poSTR;
   }

   public int getPoDEX() {
      return this.poDEX;
   }

   public void setPoDEX(int poDEX) {
      this.poDEX = poDEX;
   }

   public int getPoCON() {
      return this.poCON;
   }

   public int getCurrentAp() {
      return this.currentAp;
   }

   public void setCurrentAp(int currentAp) {
      this.currentAp = currentAp;
   }

   public int getCurrentAg() {
      return this.currentAg;
   }

   public void setCurrentAg(int currentAg) {
      this.currentAg = currentAg;
   }

   public int getCurrentSd() {
      return this.currentSd;
   }

   public void setCurrentSd(int currentSd) {
      this.currentSd = currentSd;
   }

   public void setPoCON(int poCON) {
      this.poCON = poCON;
   }

   public int getPoINT() {
      return this.poINT;
   }

   public void setPoINT(int poINT) {
      this.poINT = poINT;
   }

   public int getTodayOnlineTime() {
      return this.todayOnlineTime;
   }

   public void setTodayOnlineTime(int todayOnlineTime) {
      this.todayOnlineTime = todayOnlineTime;
   }

   public int getTotalOnlineTime() {
      return this.totalOnlineTime;
   }

   public void setTotalOnlineTime(int time) {
      this.totalOnlineTime = time;
   }

   public long getLogoutTime() {
      return this.logoutTime;
   }

   public void setLogoutTime(long logoutTime) {
      this.logoutTime = logoutTime;
   }

   public long getLoginTime() {
      return this.loginTime;
   }

   public void setLoginTime(long loginTime) {
      this.loginTime = loginTime;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public int getVipExp() {
      return this.vipExp;
   }

   public void setVipExp(int vipExp) {
      this.vipExp = vipExp;
   }

   public int getWarComment() {
      return this.warComment;
   }

   public void setWarComment(int warComment) {
      this.warComment = warComment;
   }

   public long getWarCommentTime() {
      return this.warCommentTime;
   }

   public void setWarCommentTime(long warCommentTime) {
      this.warCommentTime = warCommentTime;
   }

   public void calExternal(Item item) {
      List externalTypes = ItemType.getEquipExternalType(item.getItemType(), item.getSlot());
      if (externalTypes.size() > 0) {
         Iterator var4 = externalTypes.iterator();

         while(var4.hasNext()) {
            int type = ((Integer)var4.next()).intValue();
            ExternalEntry entry = (ExternalEntry)this.entries.get(type);
            int modelID = item.getModel().getExternalModelMenRight(item.getStarLevel());
            int effectID = EquipmentEffect.getExternalEffectID(item.getModelID(), item.getStarLevel());
            WeaponEntry we;
            switch(type) {
            case 6:
               modelID = item.getModel().getExternalModelMenLeft(item.getStarLevel());
               if (item.getItemType() == 3) {
                  we = WeaponEntry.getEntry(modelID);
                  if (we != null) {
                     this.movementType = we.getMoveType();
                  }
               }
               break;
            case 7:
               if (item.getItemType() != 3) {
                  we = WeaponEntry.getEntry(modelID);
                  if (we != null) {
                     this.movementType = we.getMoveType();
                  }
               }
            }

            if (entry == null) {
               entry = new ExternalEntry(type, modelID, effectID);
               this.entries.put(type, entry);
            } else {
               entry.setEffectID(effectID);
               entry.setModelID(modelID);
            }
         }
      }

      externalTypes.clear();
      externalTypes = null;
   }

   public HashMap getExternalEntries() {
      return this.entries;
   }

   public ArrayList getExternal() {
      ArrayList list = new ArrayList();
      Iterator var3 = this.entries.values().iterator();

      while(var3.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var3.next();
         if (entry.getModelID() != 0) {
            list.add(entry);
         }
      }

      return list;
   }

   public int getMovementType() {
      return this.movementType;
   }

   public int getBindIngot() {
      return this.bindIngot;
   }

   public void setBindIngot(int bindIngot) {
      this.bindIngot = bindIngot;
   }

   public long getLevelupTime() {
      return this.levelupTime;
   }

   public void setLevelupTime(long levelupTime) {
      this.levelupTime = levelupTime;
   }

   public int getFinishPreivew() {
      return this.finishPreivew;
   }

   public void setFinishPreivew(int finishPreivew) {
      this.finishPreivew = finishPreivew;
   }

   public int getTodayOnlineClearTime() {
      return this.todayOnlineClearTime;
   }

   public void setTodayOnlineClearTime(int todayOnlineClearTime) {
      this.todayOnlineClearTime = todayOnlineClearTime;
   }
}
