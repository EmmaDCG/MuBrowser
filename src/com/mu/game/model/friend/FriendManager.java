package com.mu.game.model.friend;

import com.mu.config.MessageText;
import com.mu.executor.imp.friend.AddFriendExecutor;
import com.mu.executor.imp.friend.DelFriendExecutor;
import com.mu.executor.imp.friend.SaveBlessRecordExecutor;
import com.mu.executor.imp.friend.UpdateBlessTimeExecutor;
import com.mu.executor.imp.friend.UpdateFriendExecutor;
import com.mu.executor.imp.friend.UpdateLuckyExecutor;
import com.mu.executor.imp.friend.UpdateReceiveDayExecutor;
import com.mu.game.CenterManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.DeleteFriendPopup;
import com.mu.io.game.packet.imp.friend.AddFriend;
import com.mu.io.game.packet.imp.friend.BekilledTimesChanged;
import com.mu.io.game.packet.imp.friend.DeleteFriend;
import com.mu.io.game.packet.imp.friend.FriendAgreeOrRefuseApply;
import com.mu.io.game.packet.imp.friend.FriendApplyNotice;
import com.mu.io.game.packet.imp.friend.FriendDegreeChanged;
import com.mu.io.game.packet.imp.friend.GetBlessInfo;
import com.mu.io.game.packet.imp.friend.GetWishPoolInfo;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;
import jxl.Workbook;

public class FriendManager {
   public static final int Type_Friend = 0;
   public static final int Type_Enemy = 1;
   public static final int Type_Black = 2;
   public static final int MaxFriend = 50;
   public static final int MaxEnemy = 50;
   public static final int MaxBlack = 50;
   private static HashMap friendlyInfoMap = new HashMap();
   private static HashMap wishMap = new HashMap();
   private static int maxWishLevel = 1;
   private static int canBlessTimes = 20;
   private static int canBeBlessTimes = 20;
   private static int maxFriendlyLevel = 1;
   private static int minFriendlyLevel = 1;
   private static String friendTipsDes = "";
   private static String delFriendDes = "";
   private static String friendToBlackDes = "";
   private Player owner;
   private ConcurrentHashMap friendMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap enemyMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap blackMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap applyMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap friendBlessMap = Tools.newConcurrentHashMap2();
   private int lucky = 0;
   private boolean hasReceived = false;
   private int blessTimes = 0;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initFriendlyInfo(wb.getSheet(1));
      initDes(wb.getSheet(2));
      initWishInfo(wb.getSheet(3));
   }

   private static void initDes(Sheet sheet) throws Exception {
      friendTipsDes = Tools.getCellValue(sheet.getCell("B2"));
      delFriendDes = Tools.getCellValue(sheet.getCell("B3"));
      friendToBlackDes = Tools.getCellValue(sheet.getCell("B4"));
      canBlessTimes = Tools.getCellIntValue(sheet.getCell("B5"));
      canBeBlessTimes = Tools.getCellIntValue(sheet.getCell("B6"));
   }

   private static void initWishInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int level = Tools.getCellIntValue(sheet.getCell("A" + i));
         int money = Tools.getCellIntValue(sheet.getCell("B" + i));
         int exp = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (level > maxWishLevel) {
            maxWishLevel = level;
         }

         WishInfo info = new WishInfo();
         info.setLevel(level);
         info.setExp(exp);
         info.setMoney(money);
         wishMap.put(level, info);
      }

   }

   private static void initFriendlyInfo(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int level = Tools.getCellIntValue(sheet.getCell("A" + i));
         int minExp = Tools.getCellIntValue(sheet.getCell("B" + i));
         int maxExp = Tools.getCellIntValue(sheet.getCell("C" + i));
         int expBouns = Tools.getCellIntValue(sheet.getCell("D" + i));
         if (level > maxFriendlyLevel) {
            maxFriendlyLevel = level;
         }

         if (level <= minFriendlyLevel) {
            minFriendlyLevel = level;
         }

         FriendlyInfo info = new FriendlyInfo();
         info.setExpBouns(expBouns);
         info.setLevel(level);
         info.setMinExp(minExp);
         info.setMaxExp(maxExp);
         friendlyInfoMap.put(level, info);
      }

   }

   public static synchronized void bless(Player player, Player other) {
      if (other == null) {
         SystemMessage.writeMessage(player, 1021);
      } else if (!player.getFriendManager().isFriend(other.getID())) {
         SystemMessage.writeMessage(player, 1069);
      } else if (!other.getFriendManager().isFriend(player.getID())) {
         SystemMessage.writeMessage(player, 1070);
      } else if (other.getFriendManager().luckyIsFull()) {
         SystemMessage.writeMessage(player, 1071);
      } else if (player.getFriendManager().getBlessTimes() >= canBlessTimes) {
         SystemMessage.writeMessage(player, 1072);
      } else if (other.getFriendManager().hasBeBlessed(player.getID())) {
         SystemMessage.writeMessage(player, 1073);
      } else {
         other.getFriendManager().beBless(player);
      }
   }

   public static WishInfo getWishInfo(int level) {
      WishInfo info = (WishInfo)wishMap.get(level);
      return info == null ? (WishInfo)wishMap.get(maxWishLevel) : info;
   }

   public static String getFriendTipsDes() {
      return friendTipsDes;
   }

   public static int getCanBlessTimes() {
      return canBlessTimes;
   }

   public static int getCanBeBlessTimes() {
      return canBeBlessTimes;
   }

   public static String getDelFriendDes() {
      return delFriendDes;
   }

   public static String getFriendToBlackDes() {
      return friendToBlackDes;
   }

   public static int getMaxFriendlyLevel() {
      return maxFriendlyLevel;
   }

   public static int getMinFriendlyLevel() {
      return minFriendlyLevel;
   }

   public static FriendlyInfo getCurrentFriendly(Friend friend) {
      FriendlyInfo maxInfo = (FriendlyInfo)friendlyInfoMap.get(maxFriendlyLevel);
      if (friend.getFriendDegree() >= maxInfo.getMinExp()) {
         return maxInfo;
      } else {
         Iterator it = friendlyInfoMap.values().iterator();

         FriendlyInfo info;
         do {
            if (!it.hasNext()) {
               return (FriendlyInfo)friendlyInfoMap.get(minFriendlyLevel);
            }

            info = (FriendlyInfo)it.next();
         } while(friend.getFriendDegree() < info.getMinExp() || friend.getFriendDegree() > info.getMaxExp());

         return info;
      }
   }

   public static FriendlyInfo getNextFriendlyInfo(Friend friend) {
      FriendlyInfo curInfo = getCurrentFriendly(friend);
      if (curInfo == null) {
         return (FriendlyInfo)friendlyInfoMap.get(minFriendlyLevel);
      } else {
         return curInfo.getLevel() == maxFriendlyLevel ? null : (FriendlyInfo)friendlyInfoMap.get(curInfo.getLevel() + 1);
      }
   }

   public FriendManager(Player owner) {
      this.owner = owner;
   }

   public void addFriend(Player targetPlayer) {
      if (targetPlayer.getFriendManager().isInBlack(this.owner.getID())) {
         SystemMessage.writeMessage(this.owner, 1065);
      } else {
         int result = this.canAddFriend(targetPlayer.getID());
         if (result != 1) {
            SystemMessage.writeMessage(this.owner, result);
         } else {
            Friend blackFriend = (Friend)this.blackMap.remove(targetPlayer.getID());
            if (blackFriend != null) {
               DelFriendExecutor.delFriend(this.owner, targetPlayer.getID(), 2);
               DeleteFriend.deleteSuccess(this.owner, targetPlayer.getID(), 2);
            }

            Friend friend = new Friend(targetPlayer, 0);
            this.friendMap.put(targetPlayer.getID(), friend);
            AddFriendExecutor.saveFriend(this.owner, targetPlayer.getID(), 0, targetPlayer.getUser().getServerID(), targetPlayer.getName(), 0);
            AddFriend.addSuccess(this.owner, targetPlayer.getID(), 0);
            BottomMessage.pushMessage(this.owner, MessageText.getText(1075).replace("%s%", targetPlayer.getName()), 1075);
            if (!targetPlayer.getFriendManager().isFriend(this.owner.getID())) {
               FriendApplyNotice.notice(targetPlayer, this.owner.getID());
               Friend otherFriend = new Friend(this.owner, 0);
               otherFriend.setAddTime(System.currentTimeMillis());
               targetPlayer.getFriendManager().addApply(otherFriend);
            }

         }
      }
   }

   public void addEnemy(Player killer) {
      if (killer.getID() != this.owner.getID()) {
         Friend friend = (Friend)this.enemyMap.get(killer.getID());
         if (friend != null) {
            friend.addBekilledTimes();
            UpdateFriendExecutor.updateFriend(this.owner, friend);
            BekilledTimesChanged.changed(this.owner, friend.getId(), friend.getBeKilledTimes());
         } else {
            if (this.enemyMap.size() >= 50) {
               return;
            }

            friend = new Friend(killer, 1);
            friend.setBeKilledTimes(1);
            this.enemyMap.put(friend.getId(), friend);
            AddFriendExecutor.saveFriend(this.owner, killer.getID(), 1, killer.getUser().getServerID(), killer.getName(), 1);
            AddFriend.addSuccess(this.owner, killer.getID(), 1);
         }

      }
   }

   public boolean isInBlack(long rid) {
      return this.blackMap.containsKey(rid);
   }

   public boolean isEnemy(long rid) {
      return this.enemyMap.containsKey(rid);
   }

   public void agreeApply(long rid) {
      if (rid == -1L) {
         this.agreeAll();
         FriendAgreeOrRefuseApply.writeResult(this.owner, -1L);
      } else {
         int result = this.agreeOneApply(rid);
         if (result != 1) {
            SystemMessage.writeMessage(this.owner, result);
            return;
         }

         FriendAgreeOrRefuseApply.writeResult(this.owner, rid);
         SystemMessage.writeMessage(this.owner, 1061);
      }

   }

   private void agreeAll() {
      ArrayList addList = new ArrayList();
      Iterator it = this.applyMap.values().iterator();

      while(it.hasNext()) {
         if (this.friendMap.size() >= 50) {
            return;
         }

         Friend friend = (Friend)it.next();
         if (this.agreeOneApply(friend.getId()) == 1) {
            addList.add(friend);
         }
      }

      if (addList.size() > 0) {
         SystemMessage.writeMessage(this.owner, MessageText.getText(1064).replace("%s%", ((Friend)addList.get(0)).getName()), 1064);
      }

      addList.clear();
   }

   private int canAddFriend(long rid) {
      if (rid == this.owner.getID()) {
         return 1051;
      } else if (this.friendMap.containsKey(rid)) {
         return 1052;
      } else {
         return this.friendMap.size() >= 50 ? 1054 : 1;
      }
   }

   private int agreeOneApply(long rid) {
      int result = this.canAddFriend(rid);
      if (result == 1) {
         Friend friend = (Friend)this.applyMap.remove(rid);
         if (friend == null) {
            return 1062;
         } else {
            this.friendMap.put(rid, friend);
            AddFriendExecutor.saveFriend(this.owner, rid, 0, friend.getServerId(), friend.getName(), 0);
            AddFriend.addSuccess(this.owner, rid, 0);
            Friend blackFriend = (Friend)this.blackMap.remove(rid);
            if (blackFriend != null) {
               DelFriendExecutor.delFriend(this.owner, rid, 2);
               DeleteFriend.deleteSuccess(this.owner, rid, 2);
            }

            return 1;
         }
      } else {
         return result;
      }
   }

   public void refuseApply(long rid) {
      if (rid == -1L) {
         this.refuseAll();
      } else {
         this.refuseOneApply(rid);
      }

   }

   private void refuseAll() {
      this.applyMap.clear();
      FriendAgreeOrRefuseApply.writeResult(this.owner, -1L);
   }

   private void refuseOneApply(long rid) {
      this.applyMap.remove(rid);
      FriendAgreeOrRefuseApply.writeResult(this.owner, rid);
   }

   public void addBlack(Player other) {
      if (other == null) {
         SystemMessage.writeMessage(this.owner, 1021);
      } else if (other.getID() == this.owner.getID()) {
         SystemMessage.writeMessage(this.owner, 1056);
      } else if (this.blackMap.containsKey(other.getID())) {
         SystemMessage.writeMessage(this.owner, 1053);
      } else if (this.blackMap.size() >= 50) {
         SystemMessage.writeMessage(this.owner, 1055);
      } else {
         if (this.friendMap.containsKey(other.getID())) {
            this.friendMap.remove(other.getID());
            DelFriendExecutor.delFriend(this.owner, other.getID(), 0);
            DeleteFriend.deleteSuccess(this.owner, other.getID(), 0);
            if (other.getFriendManager().isFriend(this.owner.getID())) {
               other.getFriendManager().removeFriendByServer(this.owner.getID());
            }
         }

         Friend friend = new Friend(other, 2);
         this.blackMap.put(other.getID(), friend);
         AddFriendExecutor.saveFriend(this.owner, other.getID(), 2, other.getUser().getServerID(), other.getName(), 0);
         AddFriend.addSuccess(this.owner, other.getID(), 2);
         SystemMessage.writeMessage(this.owner, 1077);
      }
   }

   public void deleteFriend(long rid, int type) {
      Friend friend = null;
      switch(type) {
      case 0:
         friend = (Friend)this.friendMap.get(rid);
         break;
      case 1:
         friend = (Friend)this.enemyMap.get(rid);
         break;
      case 2:
         friend = (Friend)this.blackMap.get(rid);
      }

      if (friend == null) {
         SystemMessage.writeMessage(this.owner, 1060);
      } else {
         ShowPopup.open(this.owner, new DeleteFriendPopup(this.owner.createPopupID(), friend));
      }
   }

   public void doDelFriend(long rid, int type) {
      Friend friend = null;
      switch(type) {
      case 0:
         friend = (Friend)this.friendMap.remove(rid);
         break;
      case 1:
         friend = (Friend)this.enemyMap.remove(rid);
         break;
      case 2:
         friend = (Friend)this.blackMap.remove(rid);
      }

      if (friend != null) {
         DelFriendExecutor.delFriend(this.owner, rid, type);
      }

      DeleteFriend.deleteSuccess(this.owner, rid, type);
      if (type == 0) {
         Player other = CenterManager.getPlayerByRoleID(rid);
         if (other != null) {
            other.getFriendManager().removeFriendByServer(this.owner.getID());
         }
      }

   }

   public void addFriendByInit(Friend friend) {
      switch(friend.getType()) {
      case 0:
         this.friendMap.put(friend.getId(), friend);
         break;
      case 1:
         this.enemyMap.put(friend.getId(), friend);
         break;
      case 2:
         this.blackMap.put(friend.getId(), friend);
      }

   }

   public ArrayList getAllFriend() {
      ArrayList list = new ArrayList();
      Iterator it = this.friendMap.values().iterator();

      while(it.hasNext()) {
         list.add((Friend)it.next());
      }

      it = this.enemyMap.values().iterator();

      while(it.hasNext()) {
         list.add((Friend)it.next());
      }

      it = this.blackMap.values().iterator();

      while(it.hasNext()) {
         list.add((Friend)it.next());
      }

      return list;
   }

   public Friend getFriend(long rid, int type) {
      Friend friend = null;
      switch(type) {
      case 0:
         friend = (Friend)this.friendMap.get(rid);
         break;
      case 1:
         friend = (Friend)this.enemyMap.get(rid);
         break;
      case 2:
         friend = (Friend)this.blackMap.get(rid);
      }

      return friend;
   }

   public void destroy() {
      this.applyMap.clear();
      this.blackMap.clear();
      this.enemyMap.clear();
      this.friendMap.clear();
      this.friendBlessMap.clear();
   }

   public boolean isFriend(long rid) {
      return this.friendMap.containsKey(rid);
   }

   public void removeFriendByServer(long fid) {
      this.friendMap.remove(fid);
      DeleteFriend.deleteSuccess(this.owner, fid, 0);
      DelFriendExecutor.delFriend(this.owner, fid, 0);
   }

   public void addApply(Friend friend) {
      if (this.applyMap.size() <= 100) {
         this.applyMap.put(friend.getId(), friend);
      }
   }

   public ConcurrentHashMap getApplyMap() {
      return this.applyMap;
   }

   public void dayChanged() {
      this.lucky = 0;
      this.hasReceived = false;
      this.blessTimes = 0;
      this.friendBlessMap.clear();
   }

   public int getLucky() {
      return this.lucky;
   }

   public void setLucky(int lucky) {
      this.lucky = lucky;
   }

   public boolean isHasReceived() {
      return this.hasReceived;
   }

   public void setHasReceived(boolean hasReceived) {
      this.hasReceived = hasReceived;
   }

   public int canWish() {
      if (this.lucky < getCanBeBlessTimes()) {
         return 1066;
      } else {
         return this.hasReceived ? 1067 : 1;
      }
   }

   public int getBlessTimes() {
      return this.blessTimes;
   }

   public void setBlessTimes(int blessTimes) {
      this.blessTimes = blessTimes;
   }

   public boolean luckyIsFull() {
      return this.lucky >= canBeBlessTimes;
   }

   private void beBless(Player player) {
      Friend friend = this.getFriend(player.getID(), 0);
      if (friend != null) {
         friend.addDegree();
         ++this.lucky;
         FriendBlessInfo fb = new FriendBlessInfo();
         fb.setId(player.getID());
         fb.setName(player.getName());
         fb.setTime(System.currentTimeMillis());
         this.addBlessRecord(fb);
         UpdateFriendExecutor.updateFriend(this.owner, friend);
         UpdateLuckyExecutor.updateLucky(this.owner);
         GetWishPoolInfo.writeWishPoolInfo(this.owner);
         GetBlessInfo.writeOneInfo(this.owner, fb);
         FriendDegreeChanged.degreeChange(this.owner, friend.getId(), friend.getFriendDegree(), player.getFriendManager().hasBeBlessed(this.owner.getID()));
         Friend otherFriend = player.getFriendManager().getFriend(this.owner.getID(), 0);
         if (otherFriend != null) {
            otherFriend.addDegree();
            player.getFriendManager().addBlessTime();
            UpdateFriendExecutor.updateFriend(player, otherFriend);
            FriendDegreeChanged.degreeChange(player, otherFriend.getId(), otherFriend.getFriendDegree(), this.hasBeBlessed(player.getID()));
            UpdateBlessTimeExecutor.updateBlessTimes(player);
            SaveBlessRecordExecutor.saveRecord(player, this.owner.getID());
            WishInfo wf = getWishInfo(player.getLevel());
            SystemMessage.writeMessage(player, MessageText.getText(1074).replace("%s%", String.valueOf(wf.getMoney())), 1074);
            GetWishPoolInfo.writeWishPoolInfo(player);
         }

      }
   }

   public void addBlessRecord(FriendBlessInfo info) {
      this.friendBlessMap.put(info.getId(), info);
   }

   public boolean hasBeBlessed(long rid) {
      return this.friendBlessMap.containsKey(rid);
   }

   public void wish() {
      if (!this.luckyIsFull()) {
         SystemMessage.writeMessage(this.owner, 1066);
      } else if (this.hasReceived) {
         SystemMessage.writeMessage(this.owner, 1067);
      } else {
         WishInfo info = getWishInfo(this.owner.getLevel());
         PlayerManager.addExp(this.owner, (long)info.getExp(), -1L);
         this.hasReceived = true;
         UpdateReceiveDayExecutor.updateReceiveDay(this.owner);
         GetWishPoolInfo.writeWishPoolInfo(this.owner);
         SystemMessage.writeMessage(this.owner, MessageText.getText(1078).replace("%s%", String.valueOf(info.getExp())), 1078);
      }
   }

   public void addBlessTime() {
      ++this.blessTimes;
   }

   public String getCanWishDes() {
      WishInfo info = getWishInfo(this.owner.getLevel());
      return MessageText.getText(1068).replace("%s%", String.valueOf(info.getExp()));
   }

   public ConcurrentHashMap getBlessMap() {
      return this.friendBlessMap;
   }
}
