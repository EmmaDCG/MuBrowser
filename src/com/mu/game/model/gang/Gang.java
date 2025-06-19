package com.mu.game.model.gang;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.db.log.IngotChangeType;
import com.mu.db.manager.GangDBManager;
import com.mu.db.manager.TitleDBManager;
import com.mu.executor.Executor;
import com.mu.executor.imp.gang.SaveRedPacketExecutor;
import com.mu.executor.imp.gang.SaveSummonBossExecutor;
import com.mu.game.CenterManager;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.gangboss.GangBoss;
import com.mu.game.dungeon.imp.gangboss.GangBossGroup;
import com.mu.game.dungeon.imp.gangboss.GangBossManager;
import com.mu.game.dungeon.imp.gangboss.GangBossTemplate;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.SimpleChatInfo;
import com.mu.game.model.chat.newlink.NewCharactorLink;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.chat.newlink.NewOpenPanelLink;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.mail.SendBatchMailTask;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.PayPopup;
import com.mu.game.model.unit.player.popup.imp.RedPacketPopup;
import com.mu.game.model.unit.player.title.TitleInfo;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.game.task.schedule.SaveGangContributionTask;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.gang.AddOrDeleteApplys;
import com.mu.io.game.packet.imp.gang.ChangeApplyStatus;
import com.mu.io.game.packet.imp.gang.GangDonate;
import com.mu.io.game.packet.imp.gang.GangDynamicInfo;
import com.mu.io.game.packet.imp.gang.GangLevelUp;
import com.mu.io.game.packet.imp.gang.GangPlayerAttr;
import com.mu.io.game.packet.imp.gang.GangPlayerIn;
import com.mu.io.game.packet.imp.gang.GangWarPanelInfo;
import com.mu.io.game.packet.imp.gang.OpenRedPacket;
import com.mu.io.game.packet.imp.gang.PushGangAnnouncement;
import com.mu.io.game.packet.imp.gang.QuitGang;
import com.mu.io.game.packet.imp.gang.ReceiveWarDaily;
import com.mu.io.game.packet.imp.gang.ReceiveWelfare;
import com.mu.io.game.packet.imp.gang.RedPacketLeft;
import com.mu.io.game.packet.imp.gang.SendRedPacket;
import com.mu.io.game.packet.imp.gang.SomeOneSendPacket;
import com.mu.io.game.packet.imp.gang.SummonOrEnterBoss;
import com.mu.io.game.packet.imp.gang.TransferMaster;
import com.mu.io.game.packet.imp.gang.UpdateGangMember;
import com.mu.io.game.packet.imp.player.ChangePlayerGangName;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.BottomMessage;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.DFA;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jboss.netty.channel.Channel;

public class Gang implements Comparable {
   public static final int Operation_Apply = 1;
   public static final int Operation_Edit_description = 2;
   public static final int Operation_GetApplyList = 3;
   public static final int Operation_Agree_Apply = 4;
   public static final int Operation_Refuse_Apply = 5;
   public static final int Operation_Cancel_Apply = 6;
   public static final int Operation_Quit_Gang = 7;
   public static final int Operation_kick_out = 8;
   public static final int Operation_Receive_Walfare = 9;
   public static final int Operation_LevelUp = 10;
   public static final int Operation_AppointViceMaster = 11;
   public static final int Operation_DisAppointViceMaster = 12;
   public static final int Operation_TransferMaster = 13;
   public static final int Operation_ReceiveWarVictory = 14;
   public static final int Operation_AppointWarPost = 15;
   public static final int Operation_DisappointWarPost = 16;
   public static final int Operation_Donate = 17;
   public static final int Operation_SendPacket = 18;
   public static final int Operation_OpenPacket = 19;
   public static final int Operation_SummonOrEnterBoss = 20;
   private static final int Max_Message = 50;
   private long id;
   private String name;
   private int level = 0;
   private long masterId;
   private int flagId = 1;
   private long createTime = -1L;
   private long contribution = 0L;
   private long hisContribution = 0L;
   private String description = null;
   private long desEditor = -1L;
   private long desEditTime = -1L;
   private ConcurrentHashMap memberMap = new ConcurrentHashMap(16, 0.75F, 2);
   private ConcurrentHashMap applyMap = new ConcurrentHashMap(16, 0.75F, 2);
   private CopyOnWriteArrayList dynamicMsg = new CopyOnWriteArrayList();
   private ConcurrentHashMap redPacketMap = Tools.newConcurrentHashMap2();
   private ConcurrentHashMap summonBossMap = Tools.newConcurrentHashMap2();
   private ArrayList noticeList = new ArrayList(50);

   public Gang(long id, String name) {
      this.id = id;
      this.name = name;
   }

   public long getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public synchronized void doOperation(Player player, int type, Object... objects) {
      if (Global.isInterServiceServer()) {
         SystemMessage.writeMessage(player, 7);
      } else if (!FunctionOpenManager.isOpen(player, 14)) {
         SystemMessage.writeMessage(player, 9033);
      } else if (!player.getMap().canGangOperation()) {
         SystemMessage.writeMessage(player, 9035);
      } else {
         switch(type) {
         case 1:
            this.applyInGang(player);
            break;
         case 2:
            this.editDescription(player, (String)objects[0]);
         case 3:
         default:
            break;
         case 4:
            this.agreeApply(player, ((Long)objects[0]).longValue());
            break;
         case 5:
            this.refuseApply(player, ((Long)objects[0]).longValue());
            break;
         case 6:
            this.cancelApply(player);
            break;
         case 7:
            this.quitGang(player, ((Boolean)objects[0]).booleanValue());
            break;
         case 8:
            this.kickOut(player, ((Long)objects[0]).longValue());
            break;
         case 9:
            this.receiveWelfare(player);
            break;
         case 10:
            this.levelUpGang(player);
            break;
         case 11:
            this.appointViceMaster(player, ((Long)objects[0]).longValue());
            break;
         case 12:
            this.disappointViceMaster(player, ((Long)objects[0]).longValue());
            break;
         case 13:
            this.transferMaster(player, ((Long)objects[0]).longValue());
            break;
         case 14:
            this.receiveWarDaily(player);
            break;
         case 15:
            this.appointWarPost(player, (GangMember)objects[0], ((Integer)objects[1]).intValue());
            break;
         case 16:
            this.disappointWarPost(player, (GangMember)objects[0]);
            break;
         case 17:
            this.donate(player, ((Integer)objects[0]).intValue());
            break;
         case 18:
            this.sendRedPacket(player, ((Integer)objects[0]).intValue());
            break;
         case 19:
            this.openPacket(player, ((Long)objects[0]).longValue());
            break;
         case 20:
            this.doSummonOrEnterBoss(player, ((Integer)objects[0]).intValue());
         }

      }
   }

   public void addDynamicMsg(SimpleChatInfo info) {
      this.dynamicMsg.add(info);
      if (this.dynamicMsg.size() > 50) {
         this.dynamicMsg.remove(0);
      }

   }

   private void openPacket(Player player, long packeId) {
      RedPacket rp = this.getRedPacket(packeId);
      if (rp == null) {
         SystemMessage.writeMessage(player, 9083);
      } else {
         int result = rp.doReceive(player);
         OpenRedPacket.writePacketInfo(rp, player);
         if (result == 1) {
            RedPacketLeft.pushLeft(this, rp.getPacketId(), rp.getLeftSize());
            if (rp.isOver()) {
               this.redPacketMap.remove(packeId);
               this.broadcastRedPacketSize();
            } else {
               ActivityChangeDigital.pushDigital(player, 11, this.getCanReceivePacketSize(player));
            }
         } else if (result != 9074) {
            SystemMessage.writeMessage(player, result);
         }

      }
   }

   private void sendRedPacket(Player player, int id) {
      RedPacketInfo info = GangManager.getRedPacketInfo(id);
      if (info != null) {
         if (info.getType() == 0) {
            if (player.getRedPacketManager().getBindPacketLeftTimes(info) == 0) {
               SystemMessage.writeMessage(player, 9072);
               return;
            }

            ShowPopup.open(player, new RedPacketPopup(player.createPopupID(), info));
         } else if (player.getIngot() < info.getIngotReq()) {
            ShowPopup.open(player, new PayPopup(player.createPopupID()));
         } else {
            ShowPopup.open(player, new RedPacketPopup(player.createPopupID(), info));
         }

      }
   }

   private void doSummonOrEnterBoss(Player player, int bossId) {
      GangBossManager manager = GangBossManager.getManager();
      GangBoss gangBoss = manager.getGangBoss(this.id);
      if (gangBoss == null) {
         this.doSummonBoss(player, bossId);
      } else if (bossId == gangBoss.getBossGroup().getBossId()) {
         this.doEnterBoss(player, bossId);
      } else {
         SystemMessage.writeMessage(player, 9092);
      }

   }

   private void doEnterBoss(Player player, int bossId) {
      GangBoss gangBoss = GangBossManager.getManager().getGangBoss(this.id);
      if (gangBoss != null) {
         DungeonManager.createAndEnterDungeon(player, 10, gangBoss);
      }

   }

   private void doSummonBoss(Player player, int bossId) {
      GangMember member = this.getMember(player.getID());
      if (member != null) {
         if (member.getPost() != 2 && member.getPost() != 1) {
            SystemMessage.writeMessage(player, 9006);
         } else {
            GangBossTemplate template = (GangBossTemplate)DungeonTemplateFactory.getTemplate(10);
            GangBossGroup group = template.getBossInfo(bossId);
            if (group != null) {
               if (player.getLevel() < group.getSummonLevel()) {
                  SystemMessage.writeMessage(player, 9088);
               } else if (this.contribution < (long)group.getContributionReq()) {
                  SystemMessage.writeMessage(player, 9091);
               } else if (group.getSummonNumber() <= this.getSummonBossTimes(bossId)) {
                  SystemMessage.writeMessage(player, 9090);
               } else {
                  GangBoss gangBoss = GangBossManager.getManager().summonBoss(this.id, bossId);
                  this.reduceContribution(group.getContributionReq());
                  SaveGangContributionTask.addGang(this.id, this.contribution, this.hisContribution);
                  this.addSummonTimes(bossId, 1);
                  SaveSummonBossExecutor.saveSummon(player, this.getId(), bossId, this.getSummonBossTimes(bossId), Time.getDayLong());
                  SummonOrEnterBoss.writeSummon(this, bossId, this.getLeftSummonTimes(group), gangBoss.getActiveTime());
                  GangDonate.writeSuccess(this);
                  DunTimingPanel dp = template.getTimingPanel();
                  this.broadcast(dp);
                  dp.destroy();
                  dp = null;
               }
            }
         }
      }
   }

   public int getLeftSummonTimes(GangBossGroup group) {
      int tmp = group.getSummonNumber() - this.getSummonBossTimes(group.getBossId());
      return tmp < 0 ? 0 : tmp;
   }

   public void addSummonTimes(int bossId, int times) {
      Integer in = (Integer)this.summonBossMap.get(bossId);
      if (in == null) {
         this.summonBossMap.put(bossId, times);
      } else {
         this.summonBossMap.put(bossId, in.intValue() + times);
      }

   }

   public void doSendRedPacket(Player player, RedPacketInfo info) {
      if (info.getType() == 0) {
         if (player.getRedPacketManager().getBindPacketLeftTimes(info) == 0) {
            SystemMessage.writeMessage(player, 9072);
            return;
         }

         RedPacket rp = RedPacket.createRedPacket(info, this.getId(), player.getID(), player.getName(), player.getUserName(), player.getUser().getServerID());
         this.addRedPacket(rp);
         player.getRedPacketManager().addBindSendTimes(info.getId(), 1);
         SaveRedPacketExecutor.saveRedPacket(player, rp);
         SendRedPacket.sendResult(player, info.getId(), player.getRedPacketManager().getBindPacketLeftTimes(info));
         SystemMessage.writeMessage(player, 9082);
         SomeOneSendPacket.send(this, rp.getPacketId());
         this.sendPacketNotice(player, 0);
      } else {
         int result = PlayerManager.reduceIngot(player, info.getIngotReq(), IngotChangeType.RedPacket, "");
         if (result != 1) {
            SystemMessage.writeMessage(player, result);
         } else {
            RedPacket rp = RedPacket.createRedPacket(info, this.getId(), player.getID(), player.getName(), player.getUserName(), player.getUser().getServerID());
            this.addRedPacket(rp);
            SaveRedPacketExecutor.saveRedPacket(player, rp);
            SendRedPacket.sendResult(player, info.getId(), -1);
            SystemMessage.writeMessage(player, 9082);
            SomeOneSendPacket.send(this, rp.getPacketId());
            this.sendPacketNotice(player, 1);
            if (info.isBroadcast()) {
               BroadcastManager.broadcastRedPacket(player, this.getName(), info.getBroadcastContent());
            }
         }
      }

   }

   public void addRedPacket(RedPacket rp) {
      this.redPacketMap.put(rp.getPacketId(), rp);
   }

   public CopyOnWriteArrayList getDynamicMsgList() {
      return this.dynamicMsg;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void addNoticeFromDB(GangNotice notice) {
      this.noticeList.add(notice);
   }

   private boolean donate(Player player, int num) {
      if (num == 0) {
         return false;
      } else {
         boolean success = false;
         OperationResult or = player.getItemManager().deleteItemByModel(GangManager.getZmlItem().getModelID(), num, 39);
         if (or.getResult() != 1) {
            SystemMessage.writeMessage(player, or.getResult());
            GangDonate.writeFail(player);
         } else {
            this.addContribution(num);
            this.addHisContribution(num);
            GangMember member = this.getMember(player.getID());
            member.addCurContribution(num);
            member.addHisContribution(num);
            SaveGangContributionTask.addGang(this.id, this.contribution, this.hisContribution);
            SaveGangContributionTask.addMember(player.getID(), member.getCurContribution(), member.getHisContribution());
            UpdateGangMember.updateGangMember(player.getID(), this);
            GangDonate.writeSuccess(this);
            SimpleChatInfo si = this.getDonateMessage(member, num);
            if (si != null) {
               this.addDynamicMsg(si);
               GangDynamicInfo.pushOneMsg(this, si);
            }
         }

         return success;
      }
   }

   private void disappointWarPost(Player player, GangMember member) {
      this.clearWarTitleAndBuff(member);
      member.setWarPost(0);
      this.gainTitleAndBuff(member);
      GangDBManager.updateWarPost(member.getId(), 0);
      GangWarPanelInfo.pushPanel(player);
   }

   private void appointWarPost(Player player, GangMember member, int post) {
      if (post == 2) {
         Iterator it = this.memberMap.values().iterator();

         while(it.hasNext()) {
            GangMember m = (GangMember)it.next();
            if (m.getWarPost() == 2) {
               SystemMessage.writeMessage(player, 9056);
               return;
            }
         }

         this.clearWarTitleAndBuff(member);
         member.setWarPost(2);
         GangDBManager.updateWarPost(member.getId(), 2);
         this.gainTitleAndBuff(member);
         GangWarPanelInfo.pushPanel(player);
      } else {
         int num = 0;
         Iterator it = this.memberMap.values().iterator();

         while(it.hasNext()) {
            GangMember m = (GangMember)it.next();
            if (m.getWarPost() == 3) {
               ++num;
            }
         }

         if (num >= 3) {
            SystemMessage.writeMessage(player, 9057);
            return;
         }

         this.clearWarTitleAndBuff(member);
         member.setWarPost(3);
         this.gainTitleAndBuff(member);
         GangDBManager.updateWarPost(member.getId(), 3);
         GangWarPanelInfo.pushPanel(player);
      }

   }

   public GangMember getMemberByName(String name) {
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         if (member.getName().equals(name)) {
            return member;
         }
      }

      return null;
   }

   public void levelUpGang(Player player) {
      GangMember member = this.getMember(player.getID());
      if (member.getPost() != 2) {
         SystemMessage.writeMessage(player, 9026);
         GangLevelUp.pushLevelUpFail(this, player);
      } else {
         GangLevelData data = GangManager.getLevelData(this.level);
         if (data.getLevelUpNeedIngot() != -1 && data.getLevelUpNeedMoney() != -1) {
            if (player.getMoney() < data.getLevelUpNeedMoney()) {
               SystemMessage.writeMessage(player, 9024);
               GangLevelUp.pushLevelUpFail(this, player);
            } else if (player.getIngot() < data.getLevelUpNeedIngot()) {
               SystemMessage.writeMessage(player, 9025);
               GangLevelUp.pushLevelUpFail(this, player);
            } else {
               PlayerManager.reduceMoney(player, data.getLevelUpNeedMoney());
               if (data.getLevelUpNeedIngot() > 0) {
                  PlayerManager.reduceIngot(player, data.getLevelUpNeedIngot(), IngotChangeType.GANGLEVELUP, String.valueOf(this.level));
               }

               ++this.level;
               final int tl = this.level;
               final long ti = this.id;
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     GangDBManager.updateGangLevel(ti, tl);
                  }
               });
               GangLevelUp.pushLevelUpSuccess(this);
               SimpleChatInfo si = this.getLevelUpMessage();
               if (si != null) {
                  this.addDynamicMsg(si);
                  GangDynamicInfo.pushOneMsg(this, si);
               }

               BroadcastManager.broadcastGangLevelUp(this);
            }
         } else {
            SystemMessage.writeMessage(player, 9027);
            GangLevelUp.pushLevelUpFail(this, player);
         }
      }
   }

   public long getViceMaster() {
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         if (member.getPost() == 1) {
            return member.getId();
         }
      }

      return -1L;
   }

   public void receiveWelfare(Player player) {
      if (GangManager.hasReceiveWelfare(player.getID())) {
         SystemMessage.writeMessage(player, 9022);
         ReceiveWelfare.receiveResult(player, true);
      } else {
         GangLevelData data = GangManager.getLevelData(this.level);
         PlayerManager.addMoney(player, data.getMoney());
         PlayerManager.addBindIngot(player, data.getBindIngot(), IngotChangeType.GangWelfare.getType());
         GangManager.addWelfareReceiveLog(player.getID(), Time.getDayLong());
         ReceiveWelfare.receiveResult(player, true);
         final long rid = player.getID();
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               GangDBManager.insertWelfareLog(rid);
            }
         });
      }
   }

   public void receiveWarDaily(Player player) {
      if (!this.isWinner()) {
         SystemMessage.writeMessage(player, 9055);
      } else if (GangManager.hasWarRerceive(player.getID())) {
         SystemMessage.writeMessage(player, 9054);
         ReceiveWarDaily.receiveResult(player, false, 2);
      } else {
         ArrayList rList = DungeonManager.getLuolanManager().getTemplate().getDailyUnitList();
         OperationResult or = player.getItemManager().addItem((List)rList);
         if (or.isOk()) {
            GangManager.addWarReceiveLog(player.getID(), Time.getDayLong());
            ReceiveWarDaily.receiveResult(player, true, 2);
            final long rid = player.getID();
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.insertWarReceiveLog(rid);
               }
            });
         } else {
            SystemMessage.writeMessage(player, or.getResult());
            ReceiveWarDaily.receiveResult(player, false, 1);
         }

      }
   }

   public void addNotice(final GangNotice notice) {
      if (!Global.isInterServiceServer()) {
         ArrayList var2 = this.noticeList;
         synchronized(this.noticeList) {
            this.noticeList.add(notice);
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.insertNotice(notice);
               }
            });
            int size = this.noticeList.size();
            int tmp = size - 50;
            GangNotice tmpNotice = (GangNotice)this.noticeList.get(tmp - 1);
            if (tmp > 0) {
               for(int i = 0; i < tmp; ++i) {
                  this.noticeList.remove(i);
               }
            }

            final long fgid = this.id;
            final long ftime = tmpNotice.getTime();
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.deleteNotice(fgid, ftime);
               }
            });
         }
      }
   }

   public long getCreateTime() {
      return this.createTime;
   }

   public String getMasterName() {
      GangMember member = this.getMember(this.masterId);
      return member == null ? "无" : member.getName();
   }

   public void setCreateTime(long createTime) {
      this.createTime = createTime;
   }

   public void addMember(GangMember member) {
      this.memberMap.put(member.getId(), member);
   }

   public GangMember removeMember(long id) {
      GangMember member = this.getMember(id);
      if (member == null) {
         return null;
      } else {
         if (this.isWinner()) {
            this.clearWarTitleAndBuff(member);
         }

         Player player = member.getPlayer();
         if (player != null) {
            ChangePlayerGangName.clearName(player);
         }

         return (GangMember)this.memberMap.remove(id);
      }
   }

   public GangMember getMember(long id) {
      return (GangMember)this.memberMap.get(id);
   }

   public long getMasterId() {
      return this.masterId;
   }

   public void setMasterId(long masterId) {
      this.masterId = masterId;
   }

   public String getDescription() {
      return this.description == null ? "" : this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void addApplyInfo(ApplyInfo info) {
      this.applyMap.put(info.getRoleId(), info);
   }

   public int getMemberSize() {
      return this.memberMap.size();
   }

   public boolean hasApply(long rid) {
      return this.applyMap.containsKey(rid);
   }

   public int compareTo(Gang o) {
      return this.contribution < o.getContribution() ? 1 : -1;
   }

   public void kickOut(Player player, long rid) {
      this.doKickOut(player, rid);
   }

   private void disappointViceMaster(Player player, final long rid) {
      GangMember member = this.getMember(player.getID());
      GangMember otherMember = this.getMember(rid);
      if (member != null) {
         if (member.getPost() != 2) {
            SystemMessage.writeMessage(player, 9006);
         } else if (player.getID() == rid) {
            SystemMessage.writeMessage(player, 9015);
         } else if (otherMember.getPost() != 1) {
            SystemMessage.writeMessage(player, 9030);
         } else {
            GangMember oldVmMember = this.getMember(rid);
            oldVmMember.setPost(0);
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.updatePost(rid, 0);
               }
            });
            Player oldVm = oldVmMember.getPlayer();
            if (oldVm != null) {
               GangPlayerAttr.pushAttr(oldVm);
               ActivityChangeDigital.pushDigital(oldVm, 10, 0);
            }

            UpdateGangMember.updateGangMember(rid, this);
         }
      }
   }

   private void transferMaster(Player player, final long rid) {
      GangMember member = this.getMember(player.getID());
      GangMember otherMember = this.getMember(rid);
      if (member != null && otherMember != null) {
         if (member.getPost() != 2) {
            SystemMessage.writeMessage(player, 9006);
         } else if (player.getID() == rid) {
            SystemMessage.writeMessage(player, 9015);
         } else {
            member.setPost(0);
            final long oid = member.getId();
            GangPlayerAttr.pushAttr(player);
            UpdateGangMember.updateGangMember(oid, this);
            ActivityChangeDigital.pushDigital(player, 10, 0);
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.updatePost(oid, 0);
               }
            });
            otherMember.setPost(2);
            this.masterId = rid;
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.updatePost(rid, 2);
               }
            });
            Player newVm = otherMember.getPlayer();
            UpdateGangMember.updateGangMember(rid, this);
            if (newVm != null) {
               GangPlayerAttr.pushAttr(newVm);
               ActivityChangeDigital.pushDigital(newVm, 10, this.getApplySize());
            }

            TransferMaster tm = new TransferMaster(rid, otherMember.getName());
            this.broadcast(tm);
            if (this.isWinner()) {
               if (member.getWarPost() != 0) {
                  member.setWarPost(0);
                  GangDBManager.updateWarPost(member.getId(), 0);
               }

               if (otherMember.getWarPost() != 0) {
                  otherMember.setWarPost(0);
                  GangDBManager.updateWarPost(otherMember.getId(), 0);
               }

               this.clearWarTitleAndBuff(member);
               this.gainTitleAndBuff(member);
               this.clearWarTitleAndBuff(otherMember);
               this.gainTitleAndBuff(otherMember);
               GangWarPanelInfo.pushPanel(player);
            }

         }
      }
   }

   private void appointViceMaster(Player player, long rid) {
      GangMember member = this.getMember(player.getID());
      GangMember otherMember = this.getMember(rid);
      if (member != null) {
         if (member.getPost() != 2) {
            SystemMessage.writeMessage(player, 9006);
         } else if (player.getID() == rid) {
            SystemMessage.writeMessage(player, 9015);
         } else if (otherMember.getPost() == 1) {
            SystemMessage.writeMessage(player, 9029);
         } else {
            final long oldVmId = this.getViceMaster();
            if (oldVmId != -1L) {
               GangMember oldVmMember = this.getMember(oldVmId);
               oldVmMember.setPost(0);
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     GangDBManager.updatePost(oldVmId, 0);
                  }
               });
               Player oldVm = oldVmMember.getPlayer();
               if (oldVm != null) {
                  GangPlayerAttr.pushAttr(oldVm);
                  ActivityChangeDigital.pushDigital(oldVm, 10, 0);
               }

               UpdateGangMember.updateGangMember(oldVmId, this);
            }

            otherMember.setPost(1);
            final long newId = otherMember.getId();
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.updatePost(newId, 1);
               }
            });
            Player newVm = otherMember.getPlayer();
            if (newVm != null) {
               GangPlayerAttr.pushAttr(newVm);
               ActivityChangeDigital.pushDigital(newVm, 10, this.getApplySize());
            }

            UpdateGangMember.updateGangMember(otherMember.getId(), this);
         }
      }
   }

   private boolean doKickOut(Player player, final long rid) {
      GangMember member = this.getMember(player.getID());
      if (member == null) {
         return false;
      } else if (member.getPost() != 2 && member.getPost() != 1) {
         SystemMessage.writeMessage(player, 9006);
         return false;
      } else if (player.getID() == rid) {
         SystemMessage.writeMessage(player, 9015);
         return false;
      } else if (rid == this.masterId) {
         SystemMessage.writeMessage(player, 9006);
         return false;
      } else {
         GangMember otherMember = this.getMember(rid);
         if (otherMember == null) {
            SystemMessage.writeMessage(player, 9014);
            return false;
         } else if (otherMember.getPost() == 1 && member.getPost() != 2) {
            SystemMessage.writeMessage(player, 9006);
            return false;
         } else {
            Player otherPlayer = otherMember.getPlayer();
            if (otherPlayer != null) {
               if (!otherPlayer.getMap().canLeaveGang()) {
                  SystemMessage.writeMessage(player, 9096);
                  return false;
               }

               if (otherMember.getPost() == 1) {
                  ActivityChangeDigital.pushDigital(otherPlayer, 10, 0);
               }
            }

            this.removeMember(rid);
            GangManager.removeMember(rid);
            QuitGang qg = new QuitGang(rid);
            if (otherPlayer != null) {
               otherPlayer.writePacket(qg);
               GangPlayerAttr.pushAttr(otherPlayer);
               this.pushInfoWhenLeaveGang(otherPlayer);
            }

            this.broadcast(qg);
            qg.destroy();
            qg = null;
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.deleteMember(rid);
               }
            });
            return true;
         }
      }
   }

   public void quitGang(Player player, boolean isDisbind) {
      final long roleId = player.getID();
      GangMember member = this.getMember(roleId);
      if (member != null) {
         if (!player.getMap().canLeaveGang()) {
            SystemMessage.writeMessage(player, 9095);
         } else if (member.getPost() == 2) {
            if (Time.getDayLong() == DungeonManager.getLuolanManager().getOpenDay()) {
               SystemMessage.writeMessage(player, 9045);
            } else if (this.isWinner()) {
               SystemMessage.writeMessage(player, 9046);
            } else {
               this.disBindGang();
            }
         } else {
            if (member.getPost() == 1) {
               member.setPost(0);
               ActivityChangeDigital.pushDigital(player, 10, 0);
            }

            SimpleChatInfo si = this.getOutGangMessage(member);
            if (si != null) {
               this.addDynamicMsg(si);
               GangDynamicInfo.pushOneMsg(this, si);
            }

            this.removeMember(roleId);
            GangManager.removeMember(roleId);
            QuitGang qg = new QuitGang(roleId);
            this.broadcast(qg);
            player.writePacket(qg);
            qg.destroy();
            qg = null;
            this.pushInfoWhenLeaveGang(player);
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.deleteMember(roleId);
               }
            });
            GangPlayerAttr.pushAttr(player);
         }
      }
   }

   public void disBindGang() {
      if (this.isWinner()) {
         GangManager.clearVictoryGang();
      }

      GangManager.removeGang(this.id);
      final ArrayList list = new ArrayList();
      Iterator it = this.memberMap.values().iterator();

      while(true) {
         GangMember member;
         Player player;
         do {
            if (!it.hasNext()) {
               final String gName = this.getName();
               final long gid = this.id;
               ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                  public void run() {
                     SendBatchMailTask.sendMail(list, MessageText.getText(9047), MessageText.getText(9048).replace("%s%", gName), (ArrayList)null);
                     GangDBManager.deleteMember(list);
                     GangDBManager.deleteGang(gid);
                  }
               });
               this.memberMap.clear();
               return;
            }

            member = (GangMember)it.next();
            if (this.isWinner()) {
               this.clearWarTitleAndBuff(member);
            }

            list.add(member.getId());
            GangManager.removeMember(member.getId());
            player = member.getPlayer();
         } while(player == null);

         QuitGang qg = new QuitGang(player.getID());
         ChangePlayerGangName.clearName(player);
         GangPlayerAttr.pushAttr(player);
         player.writePacket(qg);
         qg.destroy();
         qg = null;
         if (member.getPost() == 2 || member.getPost() == 1) {
            ActivityChangeDigital.pushDigital(player, 10, 0);
         }

         member.setCurContribution(0);
         member.setHisContribution(0);
         this.pushInfoWhenLeaveGang(player);
      }
   }

   public void editDescription(Player player, final String des) {
      GangMember member = this.getMember(player.getID());
      if (member != null) {
         if (member.getPost() != 2 && member.getPost() != 1) {
            SystemMessage.writeMessage(player, 9006);
         } else if (DFA.hasKeyWords(des)) {
            SystemMessage.writeMessage(player, 9016);
         } else {
            final long editTime = System.currentTimeMillis();
            this.setDescription(des);
            this.setDesEditor(member.getId());
            this.setDesEditTime(editTime);
            final long tmpGangId = this.id;
            final long tmpEditorId = player.getID();
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
               public void run() {
                  GangDBManager.updateGangDes(tmpGangId, des, tmpEditorId, editTime);
               }
            });
            PushGangAnnouncement pa = new PushGangAnnouncement(this.description, player.getName(), editTime);
            this.broadcast(pa);
            pa.destroy();
            pa = null;
         }
      }
   }

   private void refuseAllApply(Player player) {
      ArrayList list = new ArrayList();
      long rid = -1L;

      ApplyInfo ai;
      for(Iterator it = this.applyMap.values().iterator(); it.hasNext(); rid = ai.getRoleId()) {
         ai = (ApplyInfo)it.next();
         list.add(ai.getRoleId());
      }

      if (list.size() != 0) {
         this.applyMap.clear();
         AddOrDeleteApplys.applyChanged(this, rid);
         GangDBManager.deletApply(list, this.getId());
         list.clear();
      }
   }

   private void refuseApply(Player player, long rid) {
      if (this.applyMap.size() != 0) {
         GangMember member = this.getMember(player.getID());
         if (member != null) {
            if (member.getPost() != 2 && member.getPost() != 1) {
               SystemMessage.writeMessage(player, 9006);
            } else {
               if (rid == -1L) {
                  this.refuseAllApply(player);
               } else {
                  this.removeApplyInfo(rid, false);
                  GangDBManager.deletApply(rid, this.id);
                  AddOrDeleteApplys.applyChanged(this, rid);
               }

            }
         }
      }
   }

   private void cancelApply(Player player) {
      this.removeApplyInfo(player.getID(), true);
   }

   public void removeApplyInfo(final long rid, boolean isJoin) {
      this.applyMap.remove(rid);
      if (isJoin) {
         GangManager.removePlayerOneGangApply(rid, this.id);
         final long tmpGangId = this.id;
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               GangDBManager.deletApply(rid, tmpGangId);
            }
         });
         Player player = CenterManager.getPlayerByRoleID(rid);
         if (player != null) {
            ChangeApplyStatus ap = new ChangeApplyStatus(this.id, false);
            player.writePacket(ap);
            ap.destroy();
            ap = null;
         }
      }

   }

   private void doAgreeAll(Player player) {
      if (this.getMemberSize() + this.getApplySize() > GangManager.getLevelData(this.level).getMaxMember()) {
         SystemMessage.writeMessage(player, 9010);
      } else {
         Iterator it = this.applyMap.values().iterator();

         while(it.hasNext()) {
            ApplyInfo info = (ApplyInfo)it.next();
            this.agreeApply(player, info);
         }

      }
   }

   private void agreeApply(Player player, ApplyInfo info) {
      GangMember otherMember = GangManager.getMember(info.getRoleId());
      if (otherMember != null) {
         SystemMessage.writeMessage(player, 9009);
      } else {
         otherMember = new GangMember(info.getRoleId(), this.id);
         otherMember.setName(info.getName());
         otherMember.setPost(0);
         otherMember.setProfession(info.getProfession());
         otherMember.setUserName(info.getUserName());
         otherMember.setBlueTag(info.getVipTag());
         Player p = otherMember.getPlayer();
         GangManager.addMember(otherMember);
         this.addMember(otherMember);
         if (p != null) {
            otherMember.setLevel(p.getLevel());
         } else {
            otherMember.setLevel(info.getLevel());
         }

         SimpleChatInfo si = this.getInGangMessage(otherMember);
         if (si != null) {
            this.addDynamicMsg(si);
            GangDynamicInfo.pushOneMsg(this, si);
         }

         if (p != null) {
            ChangePlayerGangName.change(p);
            GangPlayerAttr.pushAttr(p);
            p.getTaskManager().onEventCheckValue(TargetType.ValueType.JiaRuZhanMeng);
         }

         GangManager.removeApplyForJoinGang(otherMember.getId());
         GangDBManager.inertMember(otherMember);
         GangPlayerIn.pushPlayerIn(this, otherMember.getId());
      }

   }

   private void agreeApply(Player player, long rid) {
      GangMember member = this.getMember(player.getID());
      if (member != null) {
         if (member.getPost() != 2 && member.getPost() != 1) {
            SystemMessage.writeMessage(player, 9006);
         } else if (rid == -1L) {
            this.doAgreeAll(player);
         } else {
            ApplyInfo info = (ApplyInfo)this.applyMap.get(rid);
            if (info == null) {
               SystemMessage.writeMessage(player, 9065);
            } else {
               if (this.getMemberSize() >= GangManager.getLevelData(this.level).getMaxMember()) {
                  SystemMessage.writeMessage(player, 9010);
               } else {
                  this.agreeApply(player, info);
               }

            }
         }
      }
   }

   public void masterDeleteApplyInfo(long rid) {
      AddOrDeleteApplys.applyChanged(this, rid);
   }

   private SimpleChatInfo getInGangMessage(GangMember member) {
      String msg = MessageText.getText(9061);
      NewCharactorLink link = new NewCharactorLink(0, member.getId(), member.getName(), member.getVipImgText(), true);
      String time = Time.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm");
      msg = msg.replace("%s%", link.getContent());

      try {
         SimpleChatInfo info = new SimpleChatInfo();
         info.setMsg(msg);
         NewCharactorLink[] links = new NewCharactorLink[]{link};
         info.setLinks(links);
         info.setTime(time);
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, links, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
         return info;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   private SimpleChatInfo getLevelUpMessage() {
      String msg = MessageText.getText(9094);
      String time = Time.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm");
      GangLevelData gd = GangManager.getLevelData(this.level);
      msg = msg.replace("%s%", gd.getName());

      try {
         SimpleChatInfo info = new SimpleChatInfo();
         info.setMsg(msg);
         info.setTime(time);
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, (NewChatLink[])null, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
         return info;
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   private SimpleChatInfo getDonateMessage(GangMember member, int con) {
      String msg = MessageText.getText(9079);
      NewCharactorLink link = new NewCharactorLink(0, member.getId(), member.getName(), member.getVipImgText(), true);
      String time = Time.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm");
      msg = msg.replace("%s%", link.getContent()).replace("%m%", String.valueOf(con));

      try {
         SimpleChatInfo info = new SimpleChatInfo();
         info.setMsg(msg);
         NewCharactorLink[] links = new NewCharactorLink[]{link};
         info.setLinks(links);
         info.setTime(time);
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, links, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
         return info;
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   private SimpleChatInfo getOnlineOfflineMessage(GangMember member, boolean onLine) {
      String msg = MessageText.getText(9080);
      if (!onLine) {
         msg = MessageText.getText(9081);
      }

      NewCharactorLink link = new NewCharactorLink(0, member.getId(), member.getName(), member.getVipImgText(), true);
      msg = msg.replace("%s%", link.getContent());

      try {
         NewCharactorLink[] links = new NewCharactorLink[]{link};
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, links, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return null;
   }

   private SimpleChatInfo getOutGangMessage(GangMember member) {
      String msg = MessageText.getText(9062);
      NewCharactorLink link = new NewCharactorLink(0, member.getId(), member.getName(), member.getVipImgText(), true);
      String time = Time.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm");
      msg = msg.replace("%s%", link.getContent());

      try {
         SimpleChatInfo info = new SimpleChatInfo();
         info.setMsg(msg);
         NewCharactorLink[] links = new NewCharactorLink[]{link};
         info.setLinks(links);
         info.setTime(time);
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, links, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
         return info;
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public int getApplySize() {
      return this.applyMap.size();
   }

   public ArrayList getApplyList() {
      ArrayList list = new ArrayList();
      Iterator it = this.applyMap.values().iterator();

      while(it.hasNext()) {
         list.add((ApplyInfo)it.next());
      }

      return list;
   }

   private void applyInGang(Player player) {
      if (player.getGang() != null) {
         SystemMessage.writeMessage(player, 9001);
      } else if (GangManager.getPlayerApplyTimes(player.getID()) >= 5) {
         SystemMessage.writeMessage(player, 9003);
      } else if (this.applyMap.size() >= 30) {
         SystemMessage.writeMessage(player, 9004);
      } else if (this.getMemberSize() >= GangManager.getLevelData(this.level).getMaxMember()) {
         SystemMessage.writeMessage(player, 9010);
      } else if (this.applyMap.containsKey(player.getID())) {
         SystemMessage.writeMessage(player, 9002);
      } else {
         ApplyInfo info = new ApplyInfo();
         info.setApplyTime(System.currentTimeMillis());
         info.setLevel(player.getLevel());
         info.setName(player.getName());
         info.setProfession(player.getProfessionID());
         info.setRoleId(player.getID());
         info.setUserName(player.getUserName());
         info.setVipTag(player.getUser().getBlueVip().getTag());
         this.addApplyInfo(info);
         GangManager.addApplyGang(player.getID(), this.id);
         String msg = MessageText.getText(9032).replace("%s%", this.getName());
         BottomMessage.pushMessage(player, msg, 9032);
         final long frid = info.getRoleId();
         final long fgid = this.id;
         final long time = info.getApplyTime();
         final String vipTag = player.getUser().getBlueVip().getTag();
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               GangDBManager.insertApply(frid, fgid, time, vipTag);
            }
         });
         ChangeApplyStatus ag = new ChangeApplyStatus(this.id, true);
         player.writePacket(ag);
         ag.destroy();
         ag = null;
         AddOrDeleteApplys.applyChanged(this, player.getID());
      }
   }

   public void broadcast(WriteOnlyPacket packet) {
      if (!Global.isInterServiceServer()) {
         this.doGameBroadcast(packet);
      } else {
         this.doInterBroadcast(packet);
      }

   }

   private void doGameBroadcast(WriteOnlyPacket packet) {
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         Player player = member.getPlayer();
         if (player != null && player.isEnterMap()) {
            player.writePacket(packet);
         }
      }

   }

   public void doBroadcastOnlyGame(WriteOnlyPacket packet) {
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         Player player = member.getPlayer();
         if (player != null) {
            player.writePacket(packet);
         }
      }

   }

   private void doInterBroadcast(WriteOnlyPacket packet) {
      Channel toGameServerChannel = null;
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         Player player = member.getPlayer();
         if (player != null) {
            player.writePacket(packet);
            if (toGameServerChannel == null && player.getChannel() != null && player.getChannel().isWritable()) {
               toGameServerChannel = player.getChannel();
            }
         }
      }

      if (toGameServerChannel != null) {
         WriteOnlyPacket wp = Executor.GangBroadcastToGame.toPacket(this.id, packet);
         toGameServerChannel.write(packet.toBuffer());
         wp.destroy();
         it = null;
      }

   }

   public boolean isViceMaster(long rid) {
      GangMember member = this.getMember(rid);
      return member != null && member.getPost() == 1;
   }

   public boolean isMaster(long rid) {
      GangMember member = this.getMember(rid);
      return member != null && member.getPost() == 2;
   }

   public int getFlagId() {
      return this.flagId;
   }

   public GangMember getDesEditor() {
      return this.getMember(this.desEditor);
   }

   public String getEditorName() {
      GangMember member = this.getDesEditor();
      return member == null ? "" : member.getName();
   }

   public void setDesEditor(long editorId) {
      this.desEditor = editorId;
   }

   public long getDesEditTime() {
      return this.desEditTime < 0L ? this.createTime : this.desEditTime;
   }

   public void setDesEditTime(long desEditTime) {
      this.desEditTime = desEditTime;
   }

   public void setFlagId(int flagId) {
      this.flagId = flagId;
   }

   public ConcurrentHashMap getMemberMap() {
      return this.memberMap;
   }

   public Player getViceMasterPlayer() {
      return CenterManager.getPlayerByRoleID(this.getViceMaster());
   }

   public Player getMasterPlayer() {
      return CenterManager.getPlayerByRoleID(this.masterId);
   }

   public GangMember getMasterMember() {
      return this.getMember(this.masterId);
   }

   public boolean isWinner() {
      return this.getId() == GangManager.getWarVictorGang();
   }

   public int getMemberTotalLevel() {
      int totalLevel = 0;

      for(Iterator it = this.memberMap.values().iterator(); it.hasNext(); totalLevel += ((GangMember)it.next()).getLevel()) {
         ;
      }

      return totalLevel;
   }

   public void gainTitleAndBuff(GangMember member) {
      if (this.isWinner()) {
         Player player = member.getPlayer();
         Integer tid = DungeonManager.getLuolanManager().getTemplate().getTitleId(member.getWarPost());
         if (player != null) {
            this.addWarVictoryBuff(player, member.getWarPost());
            if (tid != null) {
               player.getTitleManager().gainATitle(tid.intValue());
            }
         } else if (tid != null) {
            TitleInfo ti = TitleManager.getTitleInfo(tid.intValue());
            if (ti != null) {
               TitleDBManager.insertTitle(member.getId(), tid.intValue(), -1L, false);
            }
         }

      }
   }

   public void addWarVictoryBuff(Player player, int warPost) {
      int[] buff = DungeonManager.getLuolanManager().getTemplate().getBuff(warPost);
      if (buff != null) {
         player.getBuffManager().createAndStartBuff(player, buff[0], buff[1], true, 0L, (List)null);
      }

   }

   public void clearWarTitleAndBuff(GangMember member) {
      Player player = member.getPlayer();
      if (player != null) {
         HashMap buffMap = DungeonManager.getLuolanManager().getTemplate().getBuffMap();
         Iterator it = buffMap.values().iterator();

         while(it.hasNext()) {
            int[] buff = (int[])it.next();
            player.getBuffManager().endBuff(buff[0], true);
         }

         HashMap titleMap = DungeonManager.getLuolanManager().getTemplate().getAllTitle();
         Iterator it2 = titleMap.values().iterator();

         while(it2.hasNext()) {
            int tid = ((Integer)it2.next()).intValue();
            if (player.getTitleManager().getTitle(tid) != null) {
               player.getTitleManager().deletTitle(tid);
            }
         }

      }
   }

   public void warVictory() {
      Iterator it = this.memberMap.values().iterator();

      while(it.hasNext()) {
         GangMember member = (GangMember)it.next();
         if (member != null) {
            this.gainTitleAndBuff(member);
            Player player = member.getPlayer();
            if (player != null) {
               ChangePlayerGangName.change(player);
            }
         }
      }

   }

   public void sendPacketNotice(Player player, int redType) {
      NewCharactorLink linkPlayer = new NewCharactorLink(0, player.getID(), player.getName(), player.getVipImgText(), true);
      NewOpenPanelLink linkReceive = new NewOpenPanelLink(1, MessageText.getText(9093), 59, 3);
      String msg = GangManager.getSendPacketNotice().replace("%s%", linkPlayer.getContent()).replace("%n%", linkReceive.getContent());
      if (redType == 1) {
         msg = msg.replace("%m%", MessageText.getText(3058));
      } else {
         msg = msg.replace("%m%", MessageText.getText(3059));
      }

      ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, new NewChatLink[]{linkPlayer, linkReceive}, false, (byte[])null);
      this.broadcast(fm);
      fm.destroy();
      fm = null;
   }

   public void memberBeKilled(Player member, Player killer) {
      if (member != null) {
         NewCharactorLink linkMember = new NewCharactorLink(0, member.getID(), member.getName(), member.getVipImgText(), true);
         NewCharactorLink linkKiller = new NewCharactorLink(1, killer.getID(), killer.getName(), killer.getVipImgText(), true);
         String msg = MessageText.getText(9063).replace("%s%", linkMember.getContent()).replace("%k%", linkKiller.getContent()).replace("%m%", member.getMap().getName());
         SimpleChatInfo si = new SimpleChatInfo();
         si.setMsg(msg);
         si.setLinks(new NewChatLink[]{linkMember, linkKiller});
         si.setTime(Time.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm"));
         this.addDynamicMsg(si);
         GangDynamicInfo.pushOneMsg(this, si);
         ForwardMessage fm = ChatProcess.createNewChatMessage(4, msg, new NewChatLink[]{linkMember, linkKiller}, false, (byte[])null);
         this.broadcast(fm);
         fm.destroy();
         fm = null;
      }
   }

   public long getContribution() {
      return this.contribution;
   }

   public void setContribution(long contribution) {
      this.contribution = contribution;
   }

   public void addContribution(int con) {
      this.setContribution(this.contribution + (long)con);
   }

   public void reduceContribution(int con) {
      this.contribution -= (long)con;
      if (this.contribution < 0L) {
         this.contribution = 0L;
      }

   }

   public void addHisContribution(int con) {
      this.setHisContribution(this.hisContribution + (long)con);
   }

   public RedPacket getRedPacket(long id) {
      return (RedPacket)this.redPacketMap.get(id);
   }

   public ConcurrentHashMap getPacketMap() {
      return this.redPacketMap;
   }

   public long getHisContribution() {
      return this.hisContribution;
   }

   public void setHisContribution(long hisContribution) {
      this.hisContribution = hisContribution;
   }

   public void memberOnline(long rid) {
      GangMember member = this.getMember(rid);
      if (member != null) {
         UpdateGangMember.updateGangMember(rid, this);
         SimpleChatInfo si = this.getOnlineOfflineMessage(member, true);
         if (si != null) {
            this.addDynamicMsg(si);
            GangDynamicInfo.pushOneMsg(this, si);
         }
      }

   }

   public void memberOffline(long rid, long time) {
      GangMember member = this.getMember(rid);
      if (member != null) {
         member.setOfflineTime(time);
         UpdateGangMember.updateGangMember(rid, this);
         SimpleChatInfo si = this.getOnlineOfflineMessage(member, false);
         if (si != null) {
            this.addDynamicMsg(si);
            GangDynamicInfo.pushOneMsg(this, si);
         }
      }

   }

   public int getRedPacketSize() {
      return this.redPacketMap.size();
   }

   public int getCanReceivePacketSize(Player player) {
      int num = 0;
      Iterator itRp = this.redPacketMap.values().iterator();

      while(itRp.hasNext()) {
         RedPacket rp = (RedPacket)itRp.next();
         if (!rp.isOver() && !rp.hasReceived(player)) {
            ++num;
         }
      }

      return num;
   }

   public void broadcastRedPacketSize() {
      Iterator it = this.memberMap.values().iterator();

      while(true) {
         Player player;
         do {
            if (!it.hasNext()) {
               return;
            }

            GangMember member = (GangMember)it.next();
            player = member.getPlayer();
         } while(player == null);

         int num = 0;
         Iterator itRp = this.redPacketMap.values().iterator();

         while(itRp.hasNext()) {
            RedPacket rp = (RedPacket)itRp.next();
            if (!rp.isOver() && !rp.hasReceived(player)) {
               ++num;
            }
         }

         ActivityChangeDigital.pushDigital(player, 11, num);
      }
   }

   public void checkRedpacket() {
      int removeNum = 0;
      long now = System.currentTimeMillis();
      Iterator it = this.redPacketMap.values().iterator();

      while(it.hasNext()) {
         RedPacket rp = (RedPacket)it.next();
         if (rp.isOver() && now - rp.getLastReceiveTime() >= 3600000L) {
            it.remove();
            ++removeNum;
         }
      }

      if (removeNum > 0) {
         this.broadcastRedPacketSize();
      }

   }

   public void pushInfoWhenLeaveGang(Player player) {
      ActivityChangeDigital.pushDigital(player, 11, 0);
      PlayerAttributes.sendToClient(player, StatEnum.Contribution);
      PlayerAttributes.sendToClient(player, StatEnum.HisContribution);
   }

   public int getSummonBossTimes(int bossId) {
      Integer in = (Integer)this.summonBossMap.get(bossId);
      return in == null ? 0 : in.intValue();
   }

   public GangMember[] getWarPanelList() {
      GangMember[] members = new GangMember[5];
      int index = 2;
      if (this.getId() == GangManager.getWarVictorGang()) {
         Iterator it = this.memberMap.values().iterator();

         while(it.hasNext()) {
            GangMember member = (GangMember)it.next();
            int wp = member.getWarPost();
            if (wp == 1) {
               members[0] = member;
            } else if (wp == 2) {
               members[1] = member;
            } else if (wp == 3) {
               members[index++] = member;
            }
         }
      }

      return members;
   }

   public void dayChanged() {
      this.summonBossMap.clear();
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
