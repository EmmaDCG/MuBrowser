package com.mu.executor.imp.player;

import com.mu.db.manager.FinancingDBManager;
import com.mu.db.manager.PetDBManager;
import com.mu.db.manager.RewardHallDBManager;
import com.mu.db.manager.ShieldDBManager;
import com.mu.db.manager.TanXianDBManager;
import com.mu.db.manager.TaskDBManager;
import com.mu.db.manager.VIPDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.achieve.InitAchiveement;
import com.mu.io.game.packet.imp.activity.InitActivityRoleLogs;
import com.mu.io.game.packet.imp.activity.InitActivityUserLogs;
import com.mu.io.game.packet.imp.dungeon.DungeonInitRecover;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game.packet.imp.extarget.ExTargetInitCollected;
import com.mu.io.game.packet.imp.extarget.ExTargetInitReceived;
import com.mu.io.game.packet.imp.friend.InitFriend;
import com.mu.io.game.packet.imp.guide.InitFunctionOpenTime;
import com.mu.io.game.packet.imp.init.InitArrowGuide;
import com.mu.io.game.packet.imp.init.InitBuffs;
import com.mu.io.game.packet.imp.init.InitDailyReceiveLogs;
import com.mu.io.game.packet.imp.init.InitDunLogs;
import com.mu.io.game.packet.imp.init.InitDunTotalLogs;
import com.mu.io.game.packet.imp.init.InitEnd;
import com.mu.io.game.packet.imp.init.InitFirstkill;
import com.mu.io.game.packet.imp.init.InitHallows;
import com.mu.io.game.packet.imp.init.InitHangset;
import com.mu.io.game.packet.imp.init.InitItemLimits;
import com.mu.io.game.packet.imp.init.InitItems;
import com.mu.io.game.packet.imp.init.InitLuckyTurnTable;
import com.mu.io.game.packet.imp.init.InitMail;
import com.mu.io.game.packet.imp.init.InitMailItem;
import com.mu.io.game.packet.imp.init.InitRedPacket;
import com.mu.io.game.packet.imp.init.InitRoleDropCounts;
import com.mu.io.game.packet.imp.init.InitShortcut;
import com.mu.io.game.packet.imp.init.InitSkills;
import com.mu.io.game.packet.imp.init.InitSnLogs;
import com.mu.io.game.packet.imp.init.InitSpirit;
import com.mu.io.game.packet.imp.init.InitStorage;
import com.mu.io.game.packet.imp.player.InitPay;
import com.mu.io.game.packet.imp.player.InitSevenDayTreasure;
import com.mu.io.game.packet.imp.sys.ListPacket;
import com.mu.io.game.packet.imp.title.InitTitle;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class InitPlayerSystem extends Executable {
   public InitPlayerSystem(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ListPacket lp = new ListPacket();
      long roleId = packet.readLong();
      int initType = packet.readByte();
      String userName = packet.readUTF();
      int serverId = packet.readInt();
      lp.addPacket(VIPDBManager.initRoleVIP(roleId));
      lp.addPacket(InitStorage.initStorages(roleId));
      lp.addPacket(InitItems.initItem(roleId));
      lp.addPacket(InitSkills.initSkill(roleId));
      lp.addPacket(InitShortcut.getInitShortcuts(roleId));
      InitBuffs.initBuffPackets(lp, roleId);
      InitFirstkill.addFirstKill(lp, roleId);
      InitRoleDropCounts.addRoleDropCount(lp, roleId);
      lp.addPacket(InitMail.createInitMail(roleId));
      lp.addPacket(InitMailItem.createInitMailItem(roleId));
      InitItemLimits.addItemLimitPacket(roleId, lp);
      lp.addPacket(PetDBManager.initRolePet(roleId));
      lp.addPacket(ShieldDBManager.initRoleShield(roleId));
      InitHangset.addHangset(lp, roleId);
      lp.addPacket(InitDunLogs.createLogs(roleId));
      lp.addPacket(InitDunTotalLogs.createLogs(roleId));
      lp.addPacket(DungeonInitRecover.createDungeonInitRecover(roleId));
      lp.addPacket(RewardHallDBManager.initSign(roleId));
      lp.addPacket(RewardHallDBManager.initVitality(roleId));
      lp.addPacket(RewardHallDBManager.initOnline(roleId));
      lp.addPacket(InitArrowGuide.createGuide(roleId));
      lp.addPacket(InitSnLogs.initSnLogs(roleId));
      lp.addPacket(TaskDBManager.initRoleTask(roleId));
      lp.addPacket(FinancingDBManager.initRoleFinancing(roleId));
      lp.addPacket(InitDailyReceiveLogs.createLogs(roleId));
      lp.addPacket(InitActivityRoleLogs.creategetRoleLogs(roleId));
      lp.addPacket(InitActivityUserLogs.creategetRoleLogs(userName, serverId));
      lp.addPacket(InitFunctionOpenTime.createFunctionOpenTime(roleId));
      lp.addPacket(InitPay.getPayList(userName, serverId));
      lp.addPacket(InitTitle.createInitTitle(roleId));
      lp.addPacket(InitFriend.createInitFriend(roleId));
      lp.addPacket(ExTargetInitCollected.createExTargetCollect(roleId));
      lp.addPacket(ExTargetInitReceived.createExTargetReceive(roleId));
      lp.addPacket(InitAchiveement.createInitAchiveement(roleId));
      lp.addPacket(InitSevenDayTreasure.initSevenDayTreasure(roleId));
      lp.addPacket(InitRedPacket.createInitRedPacket(roleId, userName, serverId));
      lp.addPacket(InitSpirit.getInitSpirit(roleId));
      lp.addPacket(InitHallows.getInitSpirit(roleId));
      lp.addPacket(InitLuckyTurnTable.getInitLuckyTurnTable(userName, serverId));
      lp.addPacket(TanXianDBManager.initRoleTanXian(roleId));
      lp.addPacket(InitEnd.initEnd(initType));
      packet.getG2sChannel().write(lp.toBuffer());
      lp.destroy();
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      long rid = ((Long)obj[0]).longValue();
      int initType = ((Integer)obj[1]).intValue();
      String userName = (String)obj[2];
      int serverId = ((Integer)obj[3]).intValue();

      try {
         packet.writeLong(rid);
         packet.writeByte(initType);
         packet.writeUTF(userName);
         packet.writeInt(serverId);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }
}
