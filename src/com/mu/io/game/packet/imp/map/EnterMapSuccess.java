package com.mu.io.game.packet.imp.map;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.executor.Executor;
import com.mu.executor.imp.friend.FriendSimpleInfo;
import com.mu.game.CenterManager;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonManager;
import com.mu.game.dungeon.imp.luolan.Luolan;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.gang.GangWarRankInfo;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.panda.Panda;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.top.WorldLevelManager;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.unit.action.DelayAction;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.imp.AutoAssSkillEvent;
import com.mu.game.model.unit.unitevent.imp.RecoveryEvent;
import com.mu.game.top.TopManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityBaseInfo;
import com.mu.io.game.packet.imp.activity.ActivityChangeDigital;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.dm.AddMenu;
import com.mu.io.game.packet.imp.dungeon.DunTimingPanel;
import com.mu.io.game.packet.imp.dungeon.EnterDungeon;
import com.mu.io.game.packet.imp.friend.FriendOnOrOffline;
import com.mu.io.game.packet.imp.gang.GangBattleInvitation;
import com.mu.io.game.packet.imp.gang.GangPlayerAttr;
import com.mu.io.game.packet.imp.guide.FunctionPreviewUpdate;
import com.mu.io.game.packet.imp.mail.AddMail;
import com.mu.io.game.packet.imp.monster.RequestBossInfo;
import com.mu.io.game.packet.imp.panda.ExistPanda;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.io.game.packet.imp.sys.RequestPay;
import com.mu.io.game.packet.imp.title.AllTitleInfo;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnterMapSuccess extends ReadAndWritePacket {
   private static Logger logger = LoggerFactory.getLogger(EnterMapSuccess.class);
   private static ConcurrentHashMap firstEnterMapActionMap = new ConcurrentHashMap(8, 0.075F, 2);
   private static ConcurrentHashMap globalEnterMapActionMap = new ConcurrentHashMap(8, 0.075F, 2);

   public static void addFirstEnterMapAction(DelayAction action) {
      firstEnterMapActionMap.put(action, true);
   }

   public static void removeFirstEnterMapAction(DelayAction actin) {
      firstEnterMapActionMap.remove(actin);
   }

   public static void addGlobalEnterMapAction(DelayAction action) {
      globalEnterMapActionMap.put(action, true);
   }

   public static void removeGlobalEnterMapAction(DelayAction actin) {
      globalEnterMapActionMap.remove(actin);
   }

   public EnterMapSuccess(int code, byte[] readData) {
      super(code, readData);
   }

   public EnterMapSuccess() {
      super(10104, (byte[])null);
   }

   public void process() throws Exception {
      try {
         logger.debug("EnterMapSuccess ");
         Player player = this.getPlayer();
         if (logger.isDebugEnabled()) {
            logger.debug("player entermap Success name is {},map is {}", player.getName(), player.getMapID());
         }

         player.setEnterMap(true);
         if (player.isNew()) {
            FirstEnterMap.sendToClient(player);
            player.setNew(false);
            AddMail.pushAllMail(player);
            TeamManager.playerOnline(player);
            ActivityManager.checkPlayerOnLine(player);
            player.getPetManager().onLogin();
            player.getSignManager().onLogin();
            player.getVitalityManager().onLogin();
            player.addMomentEvent(new RecoveryEvent(player));
            player.addMomentEvent(new AutoAssSkillEvent(player));
            PlayerAttributes.sendToClient(player);
            AddMenu.pushAllMenu(player);
            DungeonMap dunMap = player.getDungeonMap();
            if (dunMap != null) {
               Dungeon dun = dunMap.getDungeon();
               if (dun != null) {
                  this.doEnterDungeon(player, dun);
               }
            }

            Iterator it = firstEnterMapActionMap.keySet().iterator();

            while(it.hasNext()) {
               DelayAction action = (DelayAction)it.next();
               action.doAction(player);
            }

            ExistPanda.sendMsgPandaExist(player, player.getPanda() != null);
            if (!player.getTaskManager().isTaskOver(TaskConfigManager.getZJTaskHeader().getId())) {
               this.pushWelcom(player);
            }

            this.checkBuff(player);
            RequestBossInfo.writeInfo(player);
            FunctionPreviewUpdate.pushPreview(player, false);
            FriendSimpleInfo.pushSimple(player);
            ActivityBaseInfo.writeBase(player);
            this.checkAutoTask(player);
            this.checkTitle(player);
            this.doHealth(player);
            this.checkLuolan(player);
            RequestPay.pushPayUrl(player);
            this.checkOffLine(player);
            this.pushAboutGang(player);
            this.noticeFriendOnline(player);
            this.saveLog(player);
            this.checkDrug(player);
            if (Global.getLoginParser().needPostWhenLogin()) {
               Global.getLoginParser().doLogin(player);
            }

            TopManager.checkTopWhenOnline(player);
         }

         this.doEnterMapSuccess(player);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void checkDrug(Player player) {
      if (player.getLevel() >= 80) {
         boolean hasDrup = player.getBackpack().hasCanUseDrugItem(31, player.getLevel()) && player.getBackpack().hasCanUseDrugItem(32, player.getLevel());
         if (!hasDrup) {
            SystemFunctionTip.sendToClient(player, 6, Integer.valueOf(0));
         }

      }
   }

   private void saveLog(Player player) {
      WriteOnlyPacket packet = Executor.SaveLogInOut.toPacket(player, Integer.valueOf(2));
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private void checkOffLine(Player player) {
      WriteOnlyPacket packet = Executor.InitOffLine.toPacket(player.getID(), System.currentTimeMillis());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }

   private void checkTitle(Player player) {
      AllTitleInfo.pushInfo(player);
   }

   private void checkBuff(Player player) {
      TrialConfigs config = TrialConfigs.getConfig(player.getWarComment());
      if (config != null) {
         player.getBuffManager().createAndStartBuff(player, config.getBuffId(), config.getBuffLevel(), true, 0L, (List)null);
      }

      WorldLevelManager.createWorldLevelBuff(player);
      Gang gang = player.getGang();
      if (gang != null && gang.isWinner()) {
         GangMember member = gang.getMember(player.getID());
         if (member != null) {
            gang.addWarVictoryBuff(player, member.getWarPost());
         }
      }

   }

   private void checkLuolan(Player player) {
      Luolan luolan = DungeonManager.getLuolanManager().getLuoLan();
      if (luolan != null) {
         if (luolan.getTop(player.getGangId()) > 0) {
            DunTimingPanel ap = DungeonManager.getLuolanManager().getTemplate().getTimingPanel();
            player.writePacket(ap);
            ap.destroy();
            ap = null;
         }
      } else {
         ArrayList list = GangManager.getGangWarRankInfoList(Time.getDayLong());
         if (list != null) {
            Date now = Calendar.getInstance().getTime();
            Date openDate = DungeonManager.getLuolanManager().getOpenDate();
            if (now.before(openDate) && Time.getDayLong(now) == Time.getDayLong(openDate)) {
               Iterator var7 = list.iterator();

               while(var7.hasNext()) {
                  GangWarRankInfo info = (GangWarRankInfo)var7.next();
                  if (player.getGangId() == info.getId()) {
                     GangBattleInvitation.push(player);
                     break;
                  }
               }
            }
         }
      }

      if (GangManager.isCastellan(player)) {
         BroadcastManager.broadcastLuolanMasterOnline(player);
      }

   }

   private void doHealth(Player player) {
      String channelStr = BroadcastManager.createChannelMessage(1, MessageText.getText(17008), true);
      ForwardMessage fm = BroadcastManager.createNoneLinkMessage(1, channelStr);
      player.writePacket(fm);
      fm.destroy();
      fm = null;
   }

   private void checkAutoTask(Player player) {
      Task task = player.getTaskManager().getCurZJTask();
      if (task != null) {
         if (task.getData().getClazzIndex() >= TaskConfigManager.TASK_TRACE_ZJ_INDEX) {
            player.setNeddAutoTask(false);
         }

      }
   }

   private void pushWelcom(Player player) {
      Task task = player.getTaskManager().getCurZJTask();
      if (task != null) {
         ArrowGuideManager.pushArrow(player, 1, (String)null);
         SystemFunctionTip.sendToClient(player, 9, task.getId());
      }
   }

   private void doEnterDungeon(Player player, Dungeon dun) {
      try {
         EnterDungeon.enterDungeon(player, dun.getTemplate().getTemplateID(), dun.getTemplate().showDynamicMenu());
         if (dun.getTemplate().isCanInspire()) {
            dun.createBuffWhenEnter(player);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void pushAboutGang(Player player) {
      GangPlayerAttr.pushAttr(player);
      GangMember member = GangManager.getMember(player.getID());
      if (member != null) {
         String blueTag = player.getUser().getBlueVip().getTag();
         if (!blueTag.equals(member.getBlueTag())) {
            member.setBlueTag(blueTag);
            WriteOnlyPacket packet = Executor.GangUpdateVipTag.toPacket(player.getID(), blueTag);
            player.writePacket(packet);
            packet.destroy();
            packet = null;
         }

         Gang gang = player.getGang();
         int packetSize;
         if (member.getPost() == 2 || member.getPost() == 1) {
            packetSize = gang.getApplySize();
            if (packetSize > 0) {
               ActivityChangeDigital.pushDigital(player, 10, packetSize);
            }
         }

         packetSize = gang.getCanReceivePacketSize(player);
         if (packetSize > 0) {
            ActivityChangeDigital.pushDigital(player, 11, packetSize);
         }

         gang.memberOnline(player.getID());
      }

   }

   public void noticeFriendOnline(Player player) {
      int[] icons = player.getUser().getBlueVip().getBlueIcon().getIcons();
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (p.getID() != player.getID()) {
            FriendManager manager = p.getFriendManager();
            if (manager.isFriend(player.getID())) {
               FriendOnOrOffline.friendOnline(p, player.getID(), 0, icons);
            }

            if (manager.isEnemy(player.getID())) {
               FriendOnOrOffline.friendOnline(p, player.getID(), 1, icons);
            }

            if (manager.isInBlack(player.getID())) {
               FriendOnOrOffline.friendOnline(p, player.getID(), 2, icons);
            }
         }
      }

   }

   public void doEnterMapSuccess(Player player) {
      try {
         Map map = player.getMap();
         if (map == null) {
            return;
         }

         if (logger.isDebugEnabled()) {
            logger.debug("player doEnterMapSuccess name is {},map is {}", player.getName(), map.getID());
         }

         String name = map.getName();
         this.writeUTF(name == null ? "" : name);
         this.writeByte(map.getFindWayID());
         this.writeByte(map.getLine());
         player.writePacket(this);
         map.playerEnterMapSuccess(player);
         map.doEnterMapSpecil(player);
         if (player.getPetManager().isShow()) {
            Pet pet = player.getPetManager().getPet();
            if (pet != null) {
               map.addPet(pet);
               pet.idle();
               map.petEnterMapSuccess(pet);
            }
         }

         Panda panda = player.getPanda();
         if (panda != null) {
            map.addPanda(panda);
            panda.idle();
            map.pandaEnterMapSuccess(panda);
         }

         Iterator it = globalEnterMapActionMap.keySet().iterator();

         while(it.hasNext()) {
            DelayAction action = (DelayAction)it.next();
            action.doAction(player);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
