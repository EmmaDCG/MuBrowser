package com.mu.game.model.gang;

import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.executor.imp.gang.SaveRedPacketReceiveExecutor;
import com.mu.executor.imp.gang.UpdateRedPacketExecutor;
import com.mu.game.IDFactory;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Rnd;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RedPacket {
   private long packetId;
   private long roleId;
   private String roleName;
   private long sendTime;
   private int redId;
   private int redType;
   private long gangId;
   private boolean isOver = false;
   private String userName;
   private int serverId = 0;
   private long lastReceiveTime = 0L;
   private CopyOnWriteArrayList leftIngotList = new CopyOnWriteArrayList();
   private ConcurrentHashMap recordMap = Tools.newConcurrentHashMap2();
   private int totalIngot = 0;

   public static RedPacket createRedPacket(RedPacketInfo info, long gangId, long rid, String roleName, String userName, int serverId) {
      RedPacket packet = new RedPacket();
      packet.setPacketId(IDFactory.getRedPacketID());
      packet.setRedId(info.getId());
      packet.setRedType(info.getType());
      packet.setRoleId(rid);
      packet.setGangId(gangId);
      packet.setRoleName(roleName);
      packet.setSendTime(System.currentTimeMillis());
      packet.setTotalIngot(info.getIngot());
      packet.setUserName(userName);
      packet.setServerId(serverId);
      int[] var12;
      int var11 = (var12 = info.getDetail()).length;

      for(int var10 = 0; var10 < var11; ++var10) {
         Integer in = var12[var10];
         packet.addLeftIngit(in.intValue());
      }

      return packet;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public int getServerId() {
      return this.serverId;
   }

   public void setServerId(int serverId) {
      this.serverId = serverId;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public int getTotalIngot() {
      return this.totalIngot;
   }

   public void setTotalIngot(int totalIngot) {
      this.totalIngot = totalIngot;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public long getGangId() {
      return this.gangId;
   }

   public void setGangId(long gangId) {
      this.gangId = gangId;
   }

   public long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(long packetId) {
      this.packetId = packetId;
   }

   public long getRoleId() {
      return this.roleId;
   }

   public void setRoleId(long roleId) {
      this.roleId = roleId;
   }

   public long getSendTime() {
      return this.sendTime;
   }

   public void setSendTime(long sendTime) {
      this.sendTime = sendTime;
   }

   public int getRedId() {
      return this.redId;
   }

   public void setRedId(int redId) {
      this.redId = redId;
   }

   public int getRedType() {
      return this.redType;
   }

   public void setRedType(int redType) {
      this.redType = redType;
   }

   public void addLeftIngit(int ingot) {
      this.leftIngotList.add(ingot);
   }

   public void addRecord(RedPacketReceiveRecord record) {
      this.recordMap.put(record.getRid(), record);
   }

   public ConcurrentHashMap getRecordMap() {
      return this.recordMap;
   }

   public synchronized int doReceive(Player player) {
      if (this.isOver) {
         return 9076;
      } else if (this.recordMap.containsKey(player.getID())) {
         return 9074;
      } else if (this.redType == 0 && player.getRedPacketManager().getTodayBindRedReceive() >= GangManager.getDailyMaxBindRedReceive()) {
         return 9075;
      } else {
         int index = Rnd.get(this.leftIngotList.size());
         int ingot = ((Integer)this.leftIngotList.get(index)).intValue();
         this.leftIngotList.remove(index);
         if (this.leftIngotList.size() == 0) {
            this.isOver = true;
         }

         this.lastReceiveTime = System.currentTimeMillis();
         RedPacketReceiveRecord record = new RedPacketReceiveRecord();
         record.setPacketId(this.packetId);
         record.setReceiveDay(Time.getDayLong(this.lastReceiveTime));
         record.setReceiveIngot(ingot);
         record.setReceiveTime(this.lastReceiveTime);
         record.setRid(player.getID());
         record.setRoleName(player.getName());
         if (this.redType == 1) {
            PlayerManager.addIngot(player, ingot, IngotChangeType.ReceiveRedPacket.getType());
            SystemMessage.writeMessage(player, MessageText.getText(9084).replace("%s%", String.valueOf(ingot)), 9084);
         } else {
            PlayerManager.addBindIngot(player, ingot, IngotChangeType.ReceiveRedPacket.getType());
            player.getRedPacketManager().addTodayBindRedReceive(1);
            SystemMessage.writeMessage(player, MessageText.getText(9085).replace("%s%", String.valueOf(ingot)), 9085);
         }

         this.addRecord(record);
         UpdateRedPacketExecutor.updateRedPacket(player, this);
         SaveRedPacketReceiveExecutor.saveReceive(player, record, this.getRedId(), this.getRedType());
         return 1;
      }
   }

   public boolean isOver() {
      return this.isOver;
   }

   public long getLastReceiveTime() {
      return this.lastReceiveTime;
   }

   public int getLeftSize() {
      return this.leftIngotList.size();
   }

   public String getLeftIngotStr() {
      if (this.leftIngotList != null && this.leftIngotList.size() != 0) {
         StringBuffer sb = new StringBuffer();

         for(int i = 0; i < this.leftIngotList.size(); ++i) {
            sb.append(this.leftIngotList.get(i));
            if (i != this.leftIngotList.size() - 1) {
               sb.append(",");
            }
         }

         return sb.toString();
      } else {
         return "0";
      }
   }

   public boolean hasReceived(Player player) {
      return this.recordMap.containsKey(player.getID());
   }

   public void destroy() {
      this.leftIngotList.clear();
      this.recordMap.clear();
   }
}
