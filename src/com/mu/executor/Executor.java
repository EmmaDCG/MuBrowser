package com.mu.executor;

import com.mu.executor.imp.activity.SaveRoleActivityReceiveExecutor;
import com.mu.executor.imp.activity.SaveUserActivityReceiveExecutor;
import com.mu.executor.imp.buff.DeleteBuffExecutor;
import com.mu.executor.imp.buff.SaveBuffWhenOffLineExecutor;
import com.mu.executor.imp.drop.DropDayCountExecutor;
import com.mu.executor.imp.drop.FirstKillExecutor;
import com.mu.executor.imp.drop.KillNumberExecutor;
import com.mu.executor.imp.dun.ReceiveDunExecutor;
import com.mu.executor.imp.dun.SaveDunLogExecutor;
import com.mu.executor.imp.dun.SaveDunTotalLogExecutor;
import com.mu.executor.imp.dun.SaveRecoverExecutor;
import com.mu.executor.imp.dun.UpdateBigDevilTopExecutor;
import com.mu.executor.imp.dun.UpdateDunUnreceiveExecutor;
import com.mu.executor.imp.extarget.ExtargetCollectExecutor;
import com.mu.executor.imp.extarget.ExtargetReceiveExecutor;
import com.mu.executor.imp.financing.ReplaceFinancingItemExecutor;
import com.mu.executor.imp.financing.ReplaceFinancingRewardExecutor;
import com.mu.executor.imp.friend.AddFriendExecutor;
import com.mu.executor.imp.friend.DelFriendExecutor;
import com.mu.executor.imp.friend.DeleteAllRelationshipExecutor;
import com.mu.executor.imp.friend.SaveBlessRecordExecutor;
import com.mu.executor.imp.friend.UpdateBlessTimeExecutor;
import com.mu.executor.imp.friend.UpdateFriendExecutor;
import com.mu.executor.imp.friend.UpdateLuckyExecutor;
import com.mu.executor.imp.friend.UpdateReceiveDayExecutor;
import com.mu.executor.imp.gang.GangBroadcastToGameExecutor;
import com.mu.executor.imp.gang.GangUpdateVipTagExecutor;
import com.mu.executor.imp.gang.SaveRedPacketExecutor;
import com.mu.executor.imp.gang.SaveRedPacketReceiveExecutor;
import com.mu.executor.imp.gang.SaveSummonBossExecutor;
import com.mu.executor.imp.gang.UpdateRedPacketExecutor;
import com.mu.executor.imp.item.AddItemExecutor;
import com.mu.executor.imp.item.DeleteItemExecutor;
import com.mu.executor.imp.item.UpdateItemExecutor;
import com.mu.executor.imp.item.UpdateItemLimitsExecutor;
import com.mu.executor.imp.item.UpdateStoragePageExecutor;
import com.mu.executor.imp.log.AddBindIngotLogExecutor;
import com.mu.executor.imp.log.AddIngotLogExecutor;
import com.mu.executor.imp.log.ItemForgingExecutor;
import com.mu.executor.imp.log.ItemLogExecutor;
import com.mu.executor.imp.log.ReduceBindIngotLogExecutor;
import com.mu.executor.imp.log.ReduceIngotLogExecutor;
import com.mu.executor.imp.mail.DeleteMailExecutor;
import com.mu.executor.imp.mail.DeleteMailItemExecutor;
import com.mu.executor.imp.mail.ReadMailExecutor;
import com.mu.executor.imp.mail.SaveMailExecutor;
import com.mu.executor.imp.pet.ReplacePetAttributeExecutor;
import com.mu.executor.imp.pet.ReplacePetExecutor;
import com.mu.executor.imp.player.DeleteEnemyExecutor;
import com.mu.executor.imp.player.InitOfflineExecutor;
import com.mu.executor.imp.player.InitPlayerSystem;
import com.mu.executor.imp.player.OnlinePlayerExecutor;
import com.mu.executor.imp.player.PotentialChangeExecutor;
import com.mu.executor.imp.player.SaveAchievementExecutor;
import com.mu.executor.imp.player.SaveBindIngotExector;
import com.mu.executor.imp.player.SaveDailyLogsExecutor;
import com.mu.executor.imp.player.SaveEnemyExecutor;
import com.mu.executor.imp.player.SaveFunctionOpenExecutor;
import com.mu.executor.imp.player.SaveGuideExecutor;
import com.mu.executor.imp.player.SaveHallowsExecutor;
import com.mu.executor.imp.player.SaveHangset;
import com.mu.executor.imp.player.SaveIngotExecutor;
import com.mu.executor.imp.player.SaveLevelExecutor;
import com.mu.executor.imp.player.SaveLogInOutExeCutor;
import com.mu.executor.imp.player.SaveLuckyTurnExecutor;
import com.mu.executor.imp.player.SavePayLogExecutor;
import com.mu.executor.imp.player.SavePreviewExecutor;
import com.mu.executor.imp.player.SaveRedeemPointExector;
import com.mu.executor.imp.player.SaveSetupExecutor;
import com.mu.executor.imp.player.SaveSevenDayTreasureExecutor;
import com.mu.executor.imp.player.SaveShortcutExecutor;
import com.mu.executor.imp.player.SaveSnReceiveExecutor;
import com.mu.executor.imp.player.SaveSpiritExecutor;
import com.mu.executor.imp.player.SaveTransferExecutor;
import com.mu.executor.imp.player.SaveUserExecutor;
import com.mu.executor.imp.player.SaveViewExecutor;
import com.mu.executor.imp.player.SaveWarCommentExecutor;
import com.mu.executor.imp.rewardhall.ClearVitalityTaskExecutor;
import com.mu.executor.imp.rewardhall.ReplaceOnlineExecutor;
import com.mu.executor.imp.rewardhall.ReplaceSignExecutor;
import com.mu.executor.imp.rewardhall.ReplaceVitalityExecutor;
import com.mu.executor.imp.rewardhall.ReplaceVitalityTaskExecutor;
import com.mu.executor.imp.shield.ReplaceShieldExecutor;
import com.mu.executor.imp.skill.SaveSkillWhenOffLineExecutor;
import com.mu.executor.imp.skill.UpdateSkillExector;
import com.mu.executor.imp.sys.CloseGameChannelExecutor;
import com.mu.executor.imp.sys.InterServerReLogin;
import com.mu.executor.imp.sys.RemoteServerRequest;
import com.mu.executor.imp.sys.SwitchToRemoteServer;
import com.mu.executor.imp.tanxian.UpdateTanXianExecutor;
import com.mu.executor.imp.task.ClearTaskXSCountExecutor;
import com.mu.executor.imp.task.DeleteTaskClazzExecutor;
import com.mu.executor.imp.task.DeleteTaskExecutor;
import com.mu.executor.imp.task.InsertTaskExecutor;
import com.mu.executor.imp.task.ReplaceTaskInformExecutor;
import com.mu.executor.imp.task.ReplaceTaskXSCountExecutor;
import com.mu.executor.imp.task.UpdateTaskExecutor;
import com.mu.executor.imp.title.DeleteTitleExecutor;
import com.mu.executor.imp.title.InsertTitleExecutor;
import com.mu.executor.imp.title.UpdateTitleEquipExecutor;
import com.mu.executor.imp.vip.ReplaceVIPExecutor;
import com.mu.executor.imp.vip.UpdateVIPLevelExecutor;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.io.game2gateway.packet.imp.GS_DefaultPacket;
import java.util.HashMap;

public enum Executor {
   SaveOnlinePlayer(new OnlinePlayerExecutor(1)),
   SavePotentialChange(new PotentialChangeExecutor(2)),
   SaveShortcut(new SaveShortcutExecutor(3)),
   SaveLevelChange(new SaveLevelExecutor(6)),
   SaveIngot(new SaveIngotExecutor(7)),
   SaveUser(new SaveUserExecutor(8)),
   SaveSetup(new SaveSetupExecutor(9)),
   SaveHangset(new SaveHangset(10)),
   SaveTransfer(new SaveTransferExecutor(11)),
   SaveWarComment(new SaveWarCommentExecutor(12)),
   SaveBindIngot(new SaveBindIngotExector(13)),
   SaveRedeemPoint(new SaveRedeemPointExector(14)),
   SaveArrowGuide(new SaveGuideExecutor(16)),
   SaveSnReceive(new SaveSnReceiveExecutor(17)),
   SavePlayerView(new SaveViewExecutor(18)),
   SavePreview(new SavePreviewExecutor(19)),
   SaveDailyLogs(new SaveDailyLogsExecutor(20)),
   SaveEnemy(new SaveEnemyExecutor(22)),
   DelEnemy(new DeleteEnemyExecutor(23)),
   SaveLogInOut(new SaveLogInOutExeCutor(26)),
   SaveAchievement(new SaveAchievementExecutor(27)),
   InitOffLine(new InitOfflineExecutor(28)),
   SaveFunctionOpen(new SaveFunctionOpenExecutor(29)),
   SavePayLog(new SavePayLogExecutor(30)),
   RequestToRemoteServer(new RemoteServerRequest(996)),
   GetPlayerSystem(new InitPlayerSystem(997)),
   ToRemoteServer(new SwitchToRemoteServer(998)),
   CloseGameChannel(new CloseGameChannelExecutor(999)),
   ReLoginInterServer(new InterServerReLogin(1000)),
   AddItem(new AddItemExecutor(2001)),
   UpdateStoragePage(new UpdateStoragePageExecutor(2002)),
   DeleteItem(new DeleteItemExecutor(2003)),
   UpdateItem(new UpdateItemExecutor(2004)),
   UpdateItemLimit(new UpdateItemLimitsExecutor(2005)),
   AddFriend(new AddFriendExecutor(2100)),
   DelRelationship(new DeleteAllRelationshipExecutor(2101)),
   DelFriend(new DelFriendExecutor(2102)),
   UpdateFriend(new UpdateFriendExecutor(2103)),
   UpdateBlessTimes(new UpdateBlessTimeExecutor(2104)),
   UpdateLucky(new UpdateLuckyExecutor(2105)),
   UpdateReceiveWish(new UpdateReceiveDayExecutor(2106)),
   saveBlessRecord(new SaveBlessRecordExecutor(2107)),
   InsertTask(new InsertTaskExecutor(3001)),
   UpdateTask(new UpdateTaskExecutor(3002)),
   DeleteTask(new DeleteTaskExecutor(3003)),
   DeleteTaskClazz(new DeleteTaskClazzExecutor(3004)),
   ReplaceTaskInform(new ReplaceTaskInformExecutor(3007)),
   ReplaceTaskXSCount(new ReplaceTaskXSCountExecutor(3008)),
   ClearTaskXSCount(new ClearTaskXSCountExecutor(3009)),
   UpdateTanXian(new UpdateTanXianExecutor(3010)),
   ReplaceVIP(new ReplaceVIPExecutor(3101)),
   UpdateVIPLevel(new UpdateVIPLevelExecutor(3102)),
   ReplaceFinancingItem(new ReplaceFinancingItemExecutor(3201)),
   ReplaceFinancingReward(new ReplaceFinancingRewardExecutor(3202)),
   ReplacePet(new ReplacePetExecutor(3301)),
   ReplacePetAttribute(new ReplacePetAttributeExecutor(3302)),
   ReplaceSign(new ReplaceSignExecutor(3401)),
   ReplaceVitality(new ReplaceVitalityExecutor(3402)),
   ReplaceVitalityTask(new ReplaceVitalityTaskExecutor(3403)),
   ClearVitalityTask(new ClearVitalityTaskExecutor(3404)),
   ReplaceOnline(new ReplaceOnlineExecutor(3405)),
   ReplaceShield(new ReplaceShieldExecutor(3501)),
   ExtargetCollect(new ExtargetCollectExecutor(3511)),
   ExtargetReceive(new ExtargetReceiveExecutor(3512)),
   SaveRoleActivityReceive(new SaveRoleActivityReceiveExecutor(3601)),
   SaveUserActivityReceive(new SaveUserActivityReceiveExecutor(3602)),
   DeleteTitle(new DeleteTitleExecutor(3701)),
   SaveTitle(new InsertTitleExecutor(3702)),
   UpdateTitleEquip(new UpdateTitleEquipExecutor(3703)),
   SaveSevenDayTreasure(new SaveSevenDayTreasureExecutor(3801)),
   SaveSpirit(new SaveSpiritExecutor(3811)),
   SaveHallow(new SaveHallowsExecutor(3821)),
   SaveLuckyTurn(new SaveLuckyTurnExecutor(3822)),
   UpdateSkill(new UpdateSkillExector(4001)),
   SaveSkillWhenOffline(new SaveSkillWhenOffLineExecutor(4002)),
   DeleteBuff(new DeleteBuffExecutor(4005)),
   SaveBuffWhenOffLine(new SaveBuffWhenOffLineExecutor(4006)),
   GangBroadcastToGame(new GangBroadcastToGameExecutor(5001)),
   GangUpdateVipTag(new GangUpdateVipTagExecutor(5002)),
   GangSaveRedPacket(new SaveRedPacketExecutor(5003)),
   GangUpdateRedPacket(new UpdateRedPacketExecutor(5004)),
   GangSaveRedPacketRecord(new SaveRedPacketReceiveExecutor(5005)),
   GangSaveSummonBoss(new SaveSummonBossExecutor(5006)),
   DropDayCount(new DropDayCountExecutor(6001)),
   FirstKill(new FirstKillExecutor(6002)),
   KillNumber(new KillNumberExecutor(6003)),
   SaveMail(new SaveMailExecutor(7000)),
   ReadMail(new ReadMailExecutor(7001)),
   DeleteMail(new DeleteMailExecutor(7002)),
   DeleteMailItem(new DeleteMailItemExecutor(7003)),
   SaveDunLogs(new SaveDunLogExecutor(8000)),
   UpdateDunReceived(new ReceiveDunExecutor(8001)),
   UpdateDunUnReceived(new UpdateDunUnreceiveExecutor(8002)),
   UpdateBigDevilTop(new UpdateBigDevilTopExecutor(8003)),
   SaveDunTotalLogs(new SaveDunTotalLogExecutor(8004)),
   SaveDunRecover(new SaveRecoverExecutor(8005)),
   ItemLog(new ItemLogExecutor(9001)),
   ItemForgingLog(new ItemForgingExecutor(9002)),
   AddIngotLog(new AddIngotLogExecutor(9003)),
   ReduceIngotLog(new ReduceIngotLogExecutor(9004)),
   AddBindIngotLog(new AddBindIngotLogExecutor(9005)),
   ReduceBindIngotLog(new ReduceBindIngotLogExecutor(9006));

   private static HashMap executorMap = null;
   private Executable executor = null;

   static {
      init();
   }

   private static void init() {
      executorMap = new HashMap();
      Executor[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Executor exe = var3[var1];
         executorMap.put(exe.getType(), exe);
      }

   }

   public static Executor getExecutor(int type) {
      return (Executor)executorMap.get(type);
   }

   public static void execute(int type, Game2GatewayPacket packet) {
      Executor exe = getExecutor(type);
      if (exe != null) {
         exe.execute(packet);
      }

   }

   private Executor(Executable executor) {
      this.executor = executor;
   }

   public Executable getExecutor() {
      return this.executor;
   }

   public int getType() {
      return this.executor.getType();
   }

   public WriteOnlyPacket toPacket(Object... obj) {
      ExecutePacket packet = new ExecutePacket();

      try {
         packet.writeShort(this.executor.getType());
         this.executor.toPacket(packet, obj);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return packet;
   }

   public void execute(Game2GatewayPacket packet) {
      try {
         this.executor.execute(packet);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static GS_DefaultPacket getDefualtExecutorPacket(WriteOnlyPacket packet) throws Exception {
      byte[] srcBytes = packet.toByteArray();
      byte[] bytes = new byte[srcBytes.length - 2];
      System.arraycopy(srcBytes, 2, bytes, 0, bytes.length);
      GS_DefaultPacket tmpPacket = new GS_DefaultPacket(packet.getOpcode(), bytes);
      return tmpPacket;
   }
}
