package com.mu.game.model.activity;

import com.mu.db.manager.ActivityDBManager;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.activity.ActivityReceive;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.List;

public abstract class ActivityElement {
   private int id;
   private String name = "";
   private Activity father;
   private ArrayList unitList = new ArrayList();
   private ArrayList rewardList = new ArrayList();
   private int serverLimit = 1000000;
   private boolean hasServerLimit = false;
   private int numerical = 0;
   private int maxReceiveTimes = 1;
   private int receiveType = 1;
   private int incidentallyNumber = -1;

   public ActivityElement(int id, Activity father) {
      this.id = id;
      this.father = father;
   }

   public int getId() {
      return this.id;
   }

   public Activity getFather() {
      return this.father;
   }

   public ArrayList getUnitList() {
      return this.unitList;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUnitList(ArrayList unitList) {
      this.unitList = unitList;
   }

   public ArrayList getRewardList() {
      return this.rewardList;
   }

   public void setRewardList(ArrayList rewardList) {
      this.rewardList = rewardList;
   }

   public int getServerLimit() {
      return this.serverLimit;
   }

   public void setServerLimit(int serverLimit) {
      this.serverLimit = serverLimit;
   }

   public int getNumerical() {
      return this.numerical;
   }

   public int getMaxReceiveTimes() {
      return this.maxReceiveTimes;
   }

   public void setMaxReceiveTimes(int maxReceiveTimes) {
      this.maxReceiveTimes = maxReceiveTimes;
   }

   public void setNumerical(int numerical) {
      this.numerical = numerical;
   }

   public int getReceiveType() {
      return this.receiveType;
   }

   public void setReceiveType(int receiveType) {
      this.receiveType = receiveType;
   }

   public boolean hasServerLimit() {
      return this.hasServerLimit;
   }

   public void setHasServerLimit(boolean hasServerLimt) {
      this.hasServerLimit = hasServerLimt;
   }

   public int getServerLeft() {
      int tmp = ActivityManager.getServerLeft(this.id);
      return tmp != -1 ? Math.min(this.serverLimit, tmp) : this.serverLimit;
   }

   public int getIncidentallyNumber(Player player) {
      return this.hasServerLimit() ? this.getServerLeft() : this.incidentallyNumber;
   }

   public synchronized boolean receive(Player player) {
      if (this.canReceive(player, true)) {
         ArrayList rList = this.getItemUnitList(player);
         OperationResult or = player.getItemManager().addItem((List)rList);
         if (or.isOk()) {
            player.getActivityLogs().received(this.getReceiveType(), player.getUser().getServerID(), this.getId(), Time.getTimeStr());
            if (this.hasServerLimit()) {
               int tmp = this.getServerLeft();
               final int left = tmp - 1;
               final int eid = this.id;
               ActivityManager.putServerLeft(eid, left);
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     ActivityDBManager.saveServerLimit(eid, left);
                  }
               });
            }

            this.getFather().refreshIcon(player);
            return true;
         }

         SystemMessage.writeMessage(player, or.getResult());
      }

      return false;
   }

   public ArrayList getItemUnitList(Player player) {
      return this.unitList;
   }

   public abstract void writeDetail(Player var1, WriteOnlyPacket var2) throws Exception;

   public boolean canReceive(Player player, boolean notice) {
      if (!this.getFather().isOpen()) {
         return false;
      } else if (this.receiveOverload(player)) {
         return false;
      } else if (this.hasServerLimit() && this.getServerLeft() < 1) {
         if (notice) {
            SystemMessage.writeMessage(player, 25101);
         }

         return false;
      } else {
         return true;
      }
   }

   public abstract int getReceiveStatus(Player var1);

   public int getReceiveTimes(Player player) {
      switch(this.getReceiveType()) {
      case 1:
         return player.getActivityLogs().getReceiveTimesByRoleNotDaily(this.getId(), this.getFather().getOpenDate(), this.getFather().getCloseDate(), this.getFather().isLoop());
      case 2:
         return player.getActivityLogs().getReceiveTimesByRoleDaily(Time.getDayLong(), this.getId());
      case 3:
         return player.getActivityLogs().getReceiveTimesByUserNotDaily(this.getId(), this.getFather().getOpenDate(), this.getFather().getCloseDate(), this.getFather().isLoop());
      case 4:
         return player.getActivityLogs().getReceiveTimesByUserDaily(Time.getDayLong(), this.getId());
      default:
         return 0;
      }
   }

   public boolean receiveOverload(Player player) {
      switch(this.getReceiveType()) {
      case 1:
         if (player.getActivityLogs().getReceiveTimesByRoleNotDaily(this.getId(), this.getFather().getOpenDate(), this.getFather().getCloseDate(), this.getFather().isLoop()) >= this.getMaxReceiveTimes()) {
            return true;
         }

         return false;
      case 2:
         if (player.getActivityLogs().getReceiveTimesByRoleDaily(Time.getDayLong(), this.getId()) >= this.getMaxReceiveTimes()) {
            return true;
         }

         return false;
      case 3:
         if (player.getActivityLogs().getReceiveTimesByUserNotDaily(this.getId(), this.getFather().getOpenDate(), this.getFather().getCloseDate(), this.getFather().isLoop()) >= this.getMaxReceiveTimes()) {
            return true;
         }

         return false;
      case 4:
         if (player.getActivityLogs().getReceiveTimesByUserDaily(Time.getDayLong(), this.getId()) >= this.getMaxReceiveTimes()) {
            return true;
         }

         return false;
      default:
         return true;
      }
   }

   public boolean doReceive(Player player) {
      boolean b = false;

      try {
         ActivityReceive ar = new ActivityReceive();
         b = this.receive(player);
         ar.writeShort(this.id);
         ar.writeBoolean(b);
         ar.writeByte(this.getReceiveStatus(player));
         ar.writeInt(this.getIncidentallyNumber(player));
         player.writePacket(ar);
         ar.destroy();
         ar = null;
         if (b && this.getFather().getDigitalRelationId() > 0) {
            int num = this.getFather().getCanReceiveNumber(player);
            ActivityChangeDigital.pushDigital(player, this.getFather().getDigitalRelationId(), num);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return b;
   }

   public void destroy() {
      try {
         if (this.unitList != null) {
            this.unitList.clear();
            this.unitList = null;
         }

         if (this.rewardList != null) {
            this.rewardList.clear();
            this.rewardList = null;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
