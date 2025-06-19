package com.mu.game.model.unit.player;

import com.mu.config.BroadcastManager;
import com.mu.config.Global;
import com.mu.config.VariableConstant;
import com.mu.db.log.IngotChangeType;
import com.mu.db.log.global.GlobalRoleLog;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executor;
import com.mu.game.CenterManager;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.activity.ActivityLogs;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.dialog.options.DialogOptionTask;
import com.mu.game.model.drop.PlayerDropManager;
import com.mu.game.model.equip.external.EquipExternalType;
import com.mu.game.model.equip.external.EquipmentEffect;
import com.mu.game.model.equip.external.ExternalEntry;
import com.mu.game.model.financing.PlayerFinancingManager;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.friend.FriendManager;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.gift.GiftSnLogs;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.hallow.HallowManager;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.item.container.imp.Depot;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.container.imp.TreasureHouse;
import com.mu.game.model.item.operation.ItemManager;
import com.mu.game.model.mail.MailManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.packet.RolePacketService;
import com.mu.game.model.panda.Panda;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.rewardhall.online.PlayerOnlineManager;
import com.mu.game.model.rewardhall.sign.PlayerSignManager;
import com.mu.game.model.rewardhall.vitality.PlayerVitalityManager;
import com.mu.game.model.shield.PlayerShieldManager;
import com.mu.game.model.spiritOfWar.SpiritManager;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.tanxian.PlayerTanXianManager;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskState;
import com.mu.game.model.team.Team;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.transfer.Transfer;
import com.mu.game.model.transfer.TransferConfigManager;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.controller.CountdownCtller;
import com.mu.game.model.unit.controller.CountdownObject;
import com.mu.game.model.unit.player.achievement.AchievementManager;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLogManager;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.game.model.unit.player.fcm.FcmManager;
import com.mu.game.model.unit.player.hang.GameHang;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.player.pkMode.PKMode;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.game.model.unit.player.redpacket.RedPacketManager;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasure;
import com.mu.game.model.unit.player.shortcut.Shortcut;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.game.model.unit.service.EvilManager;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.unit.unitevent.imp.CountDownEvent;
import com.mu.game.model.unit.unitevent.imp.OnlineCheckEvent;
import com.mu.game.model.unit.unitevent.imp.PlayerAttackEvent;
import com.mu.game.model.unit.unitevent.imp.TransactionEvent;
import com.mu.game.model.vip.PlayerVIPManager;
import com.mu.game.model.vip.VIPConfigManager;
import com.mu.game.task.schedule.log.GlobalRoleLogTask;
import com.mu.game.task.specified.day.ZeroDailyTask;
import com.mu.game.top.TopRewardInfo;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.AttackCreature;
import com.mu.io.game.packet.imp.attack.CreatureDie;
import com.mu.io.game.packet.imp.attack.PlayerSelfDie;
import com.mu.io.game.packet.imp.buff.BuffUpdateDydata;
import com.mu.io.game.packet.imp.friend.FriendOnOrOffline;
import com.mu.io.game.packet.imp.map.UnitMove;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.OtherPlayerDetail;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import com.mu.io.game.packet.imp.player.PlayerRevival;
import com.mu.io.game.packet.imp.player.RidingChange;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.io.game.packet.imp.task.TaskInform;
import com.mu.io.game.packet.imp.transfer.TransferBroadcast;
import com.mu.io.game.packet.imp.transfer.TransferInform;
import com.mu.utils.RndNames;
import com.mu.utils.Tools;
import com.mu.utils.VarietalBase64;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import flexjson.JSONDeserializer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Player extends Creature {
   private Channel channel;
   private User user = null;
   private HashMap popupMap = null;
   private ArrayList roleList = null;
   private ConcurrentLinkedQueue waitPacketQueue = new ConcurrentLinkedQueue();
   private ConcurrentLinkedQueue processPacketQueue = new ConcurrentLinkedQueue();
   private HashMap hasPacketMap = new HashMap();
   private ScheduledFuture pushTaskContinueFuture = null;
   private HashSet popNoAgainSet = null;
   private RoleInfo roleInfo = null;
   private int popupID = 1;
   private int money = 0;
   private int bindIngot = 0;
   private int redeemPoints = 0;
   private long curExp = 0L;
   private int gender = 0;
   private int header = -1;
   private int proType = 0;
   private int proLevel = 1;
   private int potential = 0;
   private int poSTR = 0;
   private int poDEX = 0;
   private int poCON = 0;
   private int poINT = 0;
   private int warComment = 0;
   private long warCommentTime = 0L;
   private int finishPreview = -1;
   protected HashMap externals = null;
   private boolean isNew = true;
   private boolean isEnterMap = false;
   private boolean isGuider = false;
   private boolean isClient = false;
   private int destroyType = 1;
   private Point worldPoint = new Point(0, 0);
   private int worldLine = 0;
   private int worldMapID = 10001;
   private int clientDelay = 0;
   private long receiveHeartTime = 0L;
   private long heartSendTime = 0L;
   private long lastPacketCheckTime = System.currentTimeMillis();
   private int receivePacketSize = 0;
   private String remoteHost = Global.getLocalHost();
   private int remotePort = Global.getGamePort();
   private long logoutTime = -1L;
   private long loginTime = 0L;
   private int todayOnlineTime = 0;
   private int totalOnlineTime = 0;
   private int thisOnlineTime = 0;
   private long lastChatAroundTime;
   private long lastChatWorldTime;
   private long lastChatTime;
   private long lastMarketCallTime;
   private int turnTableCount;
   private int outOfRangeTimes = 0;
   private int evil = 0;
   private int vipLevel = 0;
   private int vipExp = 0;
   private PlayerVIPManager vipManager;
   private PlayerFinancingManager financingManager;
   private boolean inRiding = false;
   private long earliestRidingTime = System.currentTimeMillis();
   protected PlayerPetManager petManager;
   protected PlayerTanXianManager tanXianManager;
   private Panda panda;
   private PlayerSignManager signManager;
   private PlayerVitalityManager vitalityManager;
   private PlayerOnlineManager onlineManager;
   private Backpack backpack = null;
   private Storage depot = null;
   private Storage treasureHouse = null;
   private ItemManager itemManger = null;
   protected Equipment equipment = null;
   private PlayerTaskManager taskManager;
   protected PKMode pkMode = null;
   private Shortcut shortcut = null;
   private Team currentTeam = null;
   private PlayerDropManager dropManager = null;
   private MailManager mailManager = null;
   private GameHang gameHang = null;
   private SystemSetup systemSetup = null;
   private DunLogManager dunLogManager = null;
   private ArrowGuideManager arrowGuideManager = null;
   private PlayerTimeLimit timeLimit = null;
   private GiftSnLogs snLogs = null;
   private DailyReceiveLogManager dailyReceiveManager = null;
   protected PlayerShieldManager shieldManager;
   private ActivityLogs activityLogs = null;
   protected TitleManager titleManager = null;
   private ExtargetManager exManager = null;
   private AchievementManager acManager = null;
   private OfflineManager offlineManager = null;
   private HashMap functionOpenMap = new HashMap();
   private SevenDayTreasure sevenManager = null;
   private SpiritManager spiritManager = null;
   private HallowManager hallowsManager = null;
   private RedPacketManager redPacketManager = null;
   private FriendManager friendManager = null;
   private long lastSetPotential = 0L;
   private DialogOptionTask dot;
   private int initType;
   private long lastFightingTime = -1L;
   private int speedUpTimes = 0;
   private boolean needtoPopPKEvil = true;
   private boolean neddAutoTask = true;
   private int roleNumber = 0;
   private String remoteIp = "127.0.0.1";
   private static final Logger logger = LoggerFactory.getLogger(Player.class);

   public Player() {
      super(-1L, (Map)null);
   }

   public void initManager() {
      this.addGeneralMomentEvent();
      this.backpack = new Backpack(49);
      this.depot = new Depot(1);
      this.treasureHouse = new TreasureHouse();
      this.itemManger = new ItemManager(this);
      this.equipment = new Equipment(this);
      this.externals = new HashMap();
      this.pkMode = new PKMode();
      this.shortcut = new Shortcut(this);
      this.dropManager = new PlayerDropManager(this);
      this.mailManager = new MailManager(this);
      this.gameHang = new GameHang(this);
      this.vipManager = new PlayerVIPManager(this);
      this.petManager = new PlayerPetManager(this);
      this.tanXianManager = new PlayerTanXianManager(this);
      this.systemSetup = new SystemSetup();
      this.dunLogManager = new DunLogManager(this);
      this.taskManager = new PlayerTaskManager(this, this.getLogoutTime());
      this.arrowGuideManager = new ArrowGuideManager(this);
      this.signManager = new PlayerSignManager(this);
      this.vitalityManager = new PlayerVitalityManager(this);
      this.onlineManager = new PlayerOnlineManager(this);
      this.timeLimit = new PlayerTimeLimit();
      this.snLogs = new GiftSnLogs();
      this.dailyReceiveManager = new DailyReceiveLogManager(this);
      this.shieldManager = new PlayerShieldManager(this);
      this.financingManager = new PlayerFinancingManager(this);
      this.activityLogs = new ActivityLogs(this);
      this.titleManager = new TitleManager(this);
      this.exManager = new ExtargetManager(this);
      this.acManager = new AchievementManager(this);
      this.offlineManager = new OfflineManager(this);
      this.sevenManager = new SevenDayTreasure(this);
      this.spiritManager = new SpiritManager(this);
      this.hallowsManager = new HallowManager(this);
      this.redPacketManager = new RedPacketManager(this);
      this.friendManager = new FriendManager(this);
   }

   public boolean canTransfer() {
      Transfer transfer = TransferConfigManager.getWillTransfer(this.getProType(), this.getProLevel());
      return transfer != null && this.getLevel() >= transfer.getLevel();
   }

   public boolean canTransfer(int transferId) {
      Transfer transfer = TransferConfigManager.getWillTransfer(this.getProType(), this.getProLevel());
      return transfer != null && transferId == transfer.getId() && this.getLevel() >= transfer.getLevel();
   }

   public DailyReceiveLogManager getDailyReceiveManager() {
      return this.dailyReceiveManager;
   }

   public HallowManager getHallowsManager() {
      return this.hallowsManager;
   }

   public SpiritManager getSpiritManager() {
      return this.spiritManager;
   }

   public RedPacketManager getRedPacketManager() {
      return this.redPacketManager;
   }

   public OfflineManager getOffLineManager() {
      return this.offlineManager;
   }

   public ExtargetManager getExtargetManager() {
      return this.exManager;
   }

   public AchievementManager getAchievementManager() {
      return this.acManager;
   }

   public FriendManager getFriendManager() {
      return this.friendManager;
   }

   public void transfer() {
      try {
         Transfer transfer = TransferConfigManager.getWillTransfer(this.getProType(), this.getProLevel());
         if (transfer != null && this.getLevel() >= transfer.getLevel()) {
            int oldProfessinoId = this.getProfessionID();
            this.setProLevel(transfer.getId());
            this.setProfessionID(transfer.getJob2());
            int oldEveryPotentail = Profession.getPotentailByProID(oldProfessinoId);
            int everyEveryPotential = Profession.getPotentailByProID(this.getProfessionID()) - oldEveryPotentail;
            if (everyEveryPotential > 0 && this.getLevel() > transfer.getLevel()) {
               int addPotential = (this.getLevel() - transfer.getLevel()) * everyEveryPotential;
               this.addPotential(addPotential, true, false);
            }

            RolePacketService.noticeGatewayTransfer(this);
            this.getMap().sendPacketToAroundPlayer(new TransferBroadcast(this), this, true);
            DynamicMenuManager.operationTransferJob(this, transfer.getId());
            TransferInform.sendMsgTransferSuccess(this);
            this.beginTransfer();
            GlobalRoleLogTask.addRoleLog(GlobalRoleLog.createLog(this));
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public boolean aKeyCompleteTransfer() {
      Transfer transfer = TransferConfigManager.getWillTransfer(this.getProType(), this.getProLevel());
      if (transfer != null && this.getLevel() >= transfer.getLevel() && transfer.isAKeyComplete()) {
         if (1 != PlayerManager.reduceIngot(this, transfer.getAKeyCompleteExpend(), IngotChangeType.TRANSFER, String.valueOf(this.getProLevel()))) {
            SystemMessage.writeMessage(this, 1015);
            return false;
         } else {
            for(Task task = this.getTaskManager().getCurZZTask(); task != null; task = this.getTaskManager().getCurZZTask()) {
               task.forceComplete();
               if (!task.isAutoSubmit()) {
                  this.getTaskManager().submit(task.getId(), true);
               }

               if (task.getClazzNext() == null) {
                  break;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void beginTransfer() {
      Transfer transfer = TransferConfigManager.getWillTransfer(this.getProType(), this.getProLevel());
      if (transfer != null && this.getLevel() >= transfer.getLevel()) {
         Task task = this.getTaskManager().getCurZZTask();
         if (task == null) {
            TaskInform.sendMsgTaskData(this, transfer.getTaskList(), 0, transfer.getTaskList().size(), TaskState.TaskClientState.NEW);
            this.getTaskManager().accept(transfer.getTaskHead().getId(), true);
            DynamicMenuManager.operationTransferJob(this, transfer.getId());
         }
      }

   }

   private void addGeneralMomentEvent() {
      this.addMomentEvent(new OnlineCheckEvent(this));
   }

   public static void initExternal(HashMap externals, int proType) {
      HashSet types = EquipExternalType.getAllExternalType();
      Iterator var4 = types.iterator();

      while(var4.hasNext()) {
         Integer type = (Integer)var4.next();
         int deaultModel = EquipExternalType.getDefaulModelID(proType, type.intValue());
         ExternalEntry entry = new ExternalEntry(type.intValue(), deaultModel, EquipmentEffect.getExternalEffectID(0, 0));
         externals.put(type, entry);
      }

   }

   public final void setUser(User user) {
      this.user = user;
   }

   public String getWarCommentText() {
      TrialConfigs config = TrialConfigs.getConfig(this.getWarComment());
      return config == null ? "" : config.getName();
   }

   public int getWarCommentNameIcon() {
      TrialConfigs config = TrialConfigs.getConfig(this.getWarComment());
      return config == null ? -1 : config.getNameIcon();
   }

   public final User getUser() {
      return this.user;
   }

   public final String getUserName() {
      return this.user.getName();
   }

   public boolean isInHanging() {
      return this.gameHang.isInHanging();
   }

   public int getHeader() {
      return ((Profession)Profession.getProfessions().get(this.getProfessionID())).getHeader();
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public int getProfessionID() {
      return Profession.getProID(this.getProType(), this.getProLevel());
   }

   public int getProType() {
      return this.proType;
   }

   public void setProType(int proType) {
      this.proType = proType;
   }

   public int getProLevel() {
      return this.proLevel;
   }

   public void setProLevel(int proLevel) {
      this.proLevel = proLevel;
   }

   public int getPotential() {
      return this.potential;
   }

   public void setPotential(int potential) {
      this.potential = potential;
   }

   public void addPotential(int addValue, boolean sendToClient, boolean updateData) {
      this.potential = Math.max(this.getPotential() + addValue, 0);
      if (sendToClient) {
         PlayerAttributes.sendToClient(this, StatEnum.POTENTIAL);
      }

      if (updateData) {
         RolePacketService.noticeGatewayWhenPotentialChange(this);
      }

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

   public void setPoCON(int poCON) {
      this.poCON = poCON;
   }

   public int getPoINT() {
      return this.poINT;
   }

   public void setPoINT(int poINT) {
      this.poINT = poINT;
   }

   public int getEvil() {
      return this.evil;
   }

   public void setEvil(int evil) {
      this.evil = evil;
   }

   public final void setChannel(Channel channel) {
      this.channel = channel;
   }

   public final Channel getChannel() {
      return this.channel;
   }

   public final int getGender() {
      return this.gender;
   }

   public final void setGender(int gender) {
      this.gender = gender;
   }

   public final ArrayList getRoleList() {
      return this.roleList;
   }

   public final void setRoleList(ArrayList roleList) {
      if (this.roleList != null) {
         this.roleList.clear();
         this.roleList = null;
      }

      this.roleList = roleList;
      this.roleNumber = this.roleList.size();
   }

   public int getRoleNumber() {
      return this.roleNumber;
   }

   public int getWorldMapID() {
      return this.worldMapID;
   }

   public void setWorldMapID(int worldMapID) {
      this.worldMapID = worldMapID;
   }

   public int getClientDelay() {
      return this.clientDelay;
   }

   public boolean isInDungeon() {
      return this.getMap() != null && this.getMap().getMapType() == 2;
   }

   public Point getWorldMapPoint() {
      return this.isInDungeon() ? this.worldPoint : this.getPosition();
   }

   public void setWorldPoint(int x, int y) {
      this.worldPoint.x = x;
      this.worldPoint.y = y;
   }

   public void initBasisData(RoleInfo info) {
      PlayerLevelData data = PlayerLevelData.getLevelData(info.getProType(), info.getLevel());
      this.getProperty().playerInits(info.getBasisSTR(), info.getBasisDEX(), info.getBasisCON(), info.getBasisINT(), info.getBasisMinATK(), info.getBasisMaxATK(), info.getBasisDEF(), info.getBasisHit(), info.getBasisAVD(), info.getBasisMaxHp(), info.getBasisMaxMp(), info.getBasisMaxSD(), info.getBasisMaxAP(), true, data);
   }

   public void broadcastWhenStartMove() {
      Map map = this.getMap();
      if (map != null) {
         Point[] movePath = this.getMovePath();
         if (movePath != null) {
            UnitMove um = new UnitMove(this, movePath);
            map.sendPacketToAroundPlayer(um, this, false);
            um.destroy();
            um = null;
         }
      }

   }

   public void setHeartSendTime(long heartSendTime) {
      this.heartSendTime = heartSendTime;
   }

   public void setReceiveHeartTime(long now) {
      this.receiveHeartTime = now;
      this.clientDelay = (int)(this.receiveHeartTime - this.heartSendTime) / 2;
      if (this.clientDelay < 0 || this.clientDelay > 2000) {
         this.clientDelay = 0;
      }

   }

   public long getReceiveHeartTime() {
      return this.receiveHeartTime;
   }

   public void writePacket(WriteOnlyPacket packet) {
      try {
         if (this.channel != null) {
            this.channel.write(packet.toBuffer());
            if (packet.getWriteLength() > 819200) {
               logger.error("packet to large, code = {},mapID = {}", packet.getOpcode(), this.getMap().getID());
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void destroy() {
      try {
         if (this.isDestroy()) {
            return;
         }

         this.setDestroy(true);
         Global.getLoginParser().doQuit(this);
         if (!this.isNew()) {
            this.checkForOffLine();
            this.saveForDestroy();
         } else {
            CenterManager.removePlayer(this.getID(), this.getName(), this.getUserName(), this.getUser().getServerID(), this.getChannel());
            this.channel.close();
         }

         this.notifyDestroy();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void checkForOffLine() {
      try {
         this.getUser().setLastLogoutTime(System.currentTimeMillis());
         FcmManager.removePushInfo(this.getID());
         CenterManager.removePlayer(this.getID(), this.getName(), this.getUserName(), this.getUser().getServerID(), this.getChannel());
         RndNames.returnNameForLogout(this.getUser().getName(), this.getGender());
         this.setLogoutTime(System.currentTimeMillis());
         Gang gang = this.getGang();
         if (gang != null) {
            gang.memberOffline(this.getID(), this.getLogoutTime());
         }

         this.noticeFriendOffline();
         if (this.currentTeam != null) {
            this.currentTeam.playerOffline(this.getID());
         }

         DungeonMap dunMap = this.getDungeonMap();
         if (dunMap != null && dunMap.getDungeon() != null) {
            dunMap.getDungeon().exitForIntterupt(this);
         }

         this.cancelPushTaskFutre();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void noticeFriendOffline() {
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (p.getID() != this.getID()) {
            FriendManager manager = p.getFriendManager();
            if (manager != null) {
               if (manager.isFriend(this.getID())) {
                  FriendOnOrOffline.friendOffline(p, this.getID(), 0);
               }

               if (manager.isEnemy(this.getID())) {
                  FriendOnOrOffline.friendOffline(p, this.getID(), 1);
               }

               if (manager.isInBlack(this.getID())) {
                  FriendOnOrOffline.friendOffline(p, this.getID(), 2);
               }
            }
         }
      }

   }

   private void saveForDestroy() {
      if (this.getID() >= 0L) {
         TransactionManager.closeTransaction(this.getID());
         Map map = this.getMap();
         if (map != null) {
            map.doRemovePlayerForDestroy(this);
         }

         if (this.getPetManager().isOpen()) {
            this.getPetManager().dbReplacePet();
         }

         this.getOnlineManager().dbReplaceOnline();
         PlayerDBManager.saveForDestroy(this, this.channel);
         ActivityManager.checkPlayerOffLine(this.getID());
      }
   }

   protected void notifyDestroy() {
      String userName = null;
      long rid = this.getID();
      if (this.user != null) {
         userName = this.user.getName();
      }

      if (this.waitPacketQueue != null) {
         this.waitPacketQueue.clear();
      }

      if (this.processPacketQueue != null) {
         this.processPacketQueue.clear();
      }

      if (this.redPacketManager != null) {
         this.redPacketManager.destroy();
         this.redPacketManager = null;
      }

      if (this.hasPacketMap != null) {
         this.hasPacketMap.clear();
      }

      if (this.functionOpenMap != null) {
         this.functionOpenMap.clear();
         this.functionOpenMap = null;
      }

      if (this.popupMap != null) {
         this.popupMap.clear();
         this.popupMap = null;
      }

      if (this.acManager != null) {
         this.acManager.destroy();
         this.acManager = null;
      }

      if (this.offlineManager != null) {
         this.offlineManager.destroy();
         this.offlineManager = null;
      }

      if (this.arrowGuideManager != null) {
         this.arrowGuideManager.destroy();
         this.arrowGuideManager = null;
      }

      if (this.dailyReceiveManager != null) {
         this.dailyReceiveManager.destroy();
         this.dailyReceiveManager = null;
      }

      if (this.activityLogs != null) {
         this.activityLogs.destroy();
         this.activityLogs = null;
      }

      if (this.titleManager != null) {
         this.titleManager.destroy();
         this.titleManager = null;
      }

      if (this.friendManager != null) {
         this.friendManager.destroy();
      }

      if (this.exManager != null) {
         this.exManager.destroy();
         this.exManager = null;
      }

      if (this.equipment != null) {
         this.equipment.destroy();
         this.equipment = null;
      }

      if (this.backpack != null) {
         this.backpack.destroy();
         this.backpack = null;
      }

      if (this.depot != null) {
         this.depot.destroy();
         this.depot = null;
      }

      if (this.taskManager != null) {
         this.taskManager.destroy();
         this.taskManager = null;
      }

      if (this.petManager != null) {
         this.petManager.destroy();
         this.petManager = null;
      }

      if (this.vipManager != null) {
         this.vipManager.destroy();
         this.vipManager = null;
      }

      if (this.financingManager != null) {
         this.financingManager.destroy();
         this.financingManager = null;
      }

      if (this.signManager != null) {
         this.signManager.destroy();
         this.signManager = null;
      }

      if (this.vitalityManager != null) {
         this.vitalityManager.destroy();
         this.vitalityManager = null;
      }

      if (this.onlineManager != null) {
         this.onlineManager.destroy();
         this.onlineManager = null;
      }

      if (this.panda != null) {
         this.panda.destroy();
         this.panda = null;
      }

      if (this.itemManger != null) {
         this.itemManger.destroy();
         this.itemManger = null;
      }

      if (this.shortcut != null) {
         this.shortcut.destroy();
         this.shortcut = null;
      }

      if (this.dropManager != null) {
         this.dropManager.destroy();
         this.dropManager = null;
      }

      if (this.gameHang != null) {
         this.gameHang.destroy();
         this.gameHang = null;
      }

      this.channel = null;
      this.checkRelogin(userName, rid);
      this.doSuperDestroy();
   }

   protected void doSuperDestroy() {
      super.destroy();
   }

   public TitleManager getTitleManager() {
      return this.titleManager;
   }

   public void checkTime(long now) {
   }

   public final void waitToProcess(ReadAndWritePacket packet) {
      long now = System.currentTimeMillis();
      if (now - this.lastPacketCheckTime > 1000L) {
         this.lastPacketCheckTime = now;
         this.receivePacketSize = 0;
      }

      ++this.receivePacketSize;
      if (this.receivePacketSize > 30) {
         packet.destroy();
      } else {
         this.waitPacketQueue.add(packet);
      }
   }

   public final void setCurrentExp(long exp) {
      this.curExp = exp;
   }

   public final long getCurretntExp() {
      return this.curExp;
   }

   public PlayerTimeLimit getTimeLimit() {
      return this.timeLimit;
   }

   public GiftSnLogs getSnLogs() {
      return this.snLogs;
   }

   public int getType() {
      return 1;
   }

   public final void doPacket() {
      ReadAndWritePacket packet;
      while(!this.waitPacketQueue.isEmpty()) {
         packet = (ReadAndWritePacket)this.waitPacketQueue.poll();
         if (this.hasPacketMap.containsKey(packet.getOpcode())) {
            packet.destroy();
            packet = null;
         } else {
            this.processPacketQueue.add(packet);
            this.hasPacketMap.put(packet.getOpcode(), (Object)null);
         }
      }

      this.hasPacketMap.clear();

      while(!this.processPacketQueue.isEmpty()) {
         packet = (ReadAndWritePacket)this.processPacketQueue.poll();

         try {
            packet.process();
         } catch (Exception var6) {
            var6.printStackTrace();
         } finally {
            packet.destroy();
            packet = null;
         }
      }

   }

   public void addPopup(Popup pop) {
      if (this.popupMap == null) {
         this.popupMap = new HashMap();
      }

      this.popupMap.put(pop.getID(), pop);
   }

   public Popup getPopup(int id) {
      return this.popupMap == null ? null : (Popup)this.popupMap.get(id);
   }

   public Popup removePopup(int id) {
      return this.popupMap == null ? null : (Popup)this.popupMap.remove(id);
   }

   public int createPopupID() {
      return this.popupID++;
   }

   public void endTransaction() {
      Event transactionEvent = (Event)this.momentEvents.get(Status.TRANSACTION);
      if (transactionEvent != null) {
         transactionEvent.setEnd(true);
      }

   }

   public boolean isTransaction() {
      return TransactionManager.isTransaction(this.getID());
   }

   public void startTransAction(Player other) {
      this.addMomentEvent(new TransactionEvent(this, other));
   }

   public int getIngot() {
      return this.user.getIngot();
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public int getBindIngot() {
      return this.bindIngot;
   }

   public void setBindIngot(int bindIngot) {
      this.bindIngot = bindIngot;
   }

   public final boolean isNew() {
      return this.isNew;
   }

   public final void setNew(boolean isNew) {
      this.isNew = isNew;
   }

   public final boolean isGuider() {
      return this.isGuider;
   }

   public final void setGuider(boolean isGuider) {
      this.isGuider = isGuider;
   }

   public final boolean isClient() {
      return this.isClient;
   }

   public final void setClient(boolean isClient) {
      this.isClient = isClient;
   }

   public void setDestroyType(int type) {
      this.destroyType = type;
   }

   public int getDestroyType() {
      return this.destroyType;
   }

   public HashMap getExternals() {
      return this.externals;
   }

   public ArrayList getCurrentExternal() {
      ArrayList list = new ArrayList();
      Iterator var3 = this.externals.values().iterator();

      while(var3.hasNext()) {
         ExternalEntry entry = (ExternalEntry)var3.next();
         if (entry.getModelID() != 0) {
            list.add(entry);
         }
      }

      return list;
   }

   public void setExternals(HashMap externals) {
      this.externals = externals;
   }

   public ExternalEntry getExternal(int type) {
      return (ExternalEntry)this.externals.get(type);
   }

   public void addExternal(ExternalEntry entry) {
      this.externals.put(entry.getType(), entry);
   }

   public void changeExternal(int type, int modelID) {
      ExternalEntry entry = this.getExternal(type);
      if (entry == null) {
         entry = new ExternalEntry(type, modelID, 0);
         this.externals.put(type, entry);
      }

      entry.setModelID(modelID);
      Equipment.sendExternalChange(this);
   }

   public void setShouldDestroy(boolean shouldDestroy) {
      super.setShouldDestroy(shouldDestroy);
      if (this.isShouldDestroy() && (!this.isEnterMap || this.getMap() == null)) {
         this.destroy();
      }

   }

   public void checkRelogin(String userName, long roleID) {
      if (userName != null) {
         switch(this.destroyType) {
         case 2:
         case 3:
         case 4:
         }
      }

   }

   public void startMove(Point[] path, long moveTime) {
      if (path != null && path.length >= 2) {
         Point[] movePath = null;
         Point actualPosition = this.getActualPosition();
         if (path[0].x == actualPosition.x && path[0].y == actualPosition.y) {
            movePath = path;
         } else {
            movePath = new Point[path.length + 1];
            movePath[0] = new Point(actualPosition);
            System.arraycopy(path, 0, movePath, 1, path.length);
         }

         path = null;
         this.petManager.playerMove(movePath, moveTime);
         super.startMove(movePath, moveTime);
      } else {
         logger.error("error !!!!!!! path is null!!!!");
      }
   }

   public boolean isInTeam() {
      return this.currentTeam != null;
   }

   public RoleInfo getRoleInfo() {
      return this.roleInfo;
   }

   public RoleInfo getRoleInfoById(long id) {
      Iterator var4 = this.roleList.iterator();

      while(var4.hasNext()) {
         RoleInfo info = (RoleInfo)var4.next();
         if (info.getID() == id) {
            return info;
         }
      }

      return null;
   }

   public void setRoleInfo(RoleInfo roleInfo) {
      this.roleInfo = roleInfo;
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundPlayer(this, viewer);
   }

   public void switchArea(Rectangle newArea, Rectangle oldArea) {
      this.getMap().playerSwitchArea(this, newArea, oldArea);
   }

   public boolean switchMap(Map targetMap, Point position) {
      return this.isDestroy() ? false : this.getMap().switchMap(this, targetMap, position);
   }

   public boolean switchMap(int targetMapID, Point position) {
      if (this.isDestroy()) {
         return false;
      } else {
         Map targetMap = MapConfig.getLineMap(targetMapID).getMapByLine(this.worldLine);
         return targetMap != null ? this.getMap().switchMap(this, targetMap, position) : this.getMap().switchMap(this, targetMapID, position);
      }
   }

   public DungeonMap getDungeonMap() {
      if (this.getMap() != null && this.getMap().getMapType() == 2) {
         try {
            return (DungeonMap)this.getMap();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }

   public void toRemoteServer() {
      if (!Global.isInterServiceServer()) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               Player.this.toInterServer();
            }
         });
      } else {
         WriteOnlyPacket packet = Executor.RequestToRemoteServer.toPacket(false, this);
         this.writePacket(packet);
         packet.destroy();
      }

   }

   private void toInterServer() {
      try {
         List list = new ArrayList();
         list.add(new BasicNameValuePair("type", "2"));
         list.add(new BasicNameValuePair("version", Global.getVersion()));
         list.add(new BasicNameValuePair("sid", String.valueOf(Global.getServerID())));
         String[] re = Tools.getUrlPostContent(Global.getLoginServletPath(), list);
         if (re[0].equals("ok")) {
            String jsonData = new String(VarietalBase64.decode(re[1]));
            JSONDeserializer der = new JSONDeserializer();
            HashMap map = (HashMap)der.deserialize(jsonData);
            int result = ((Integer)map.get("result")).intValue();
            if (result != 1) {
               SystemMessage.writeMessage(this, result);
            } else {
               WriteOnlyPacket packet = Executor.RequestToRemoteServer.toPacket(true);
               this.writePacket(packet);
               packet.destroy();
            }

            map.clear();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         SystemMessage.writeMessage(this, 2);
      }

   }

   public boolean isEnterMap() {
      return this.isEnterMap;
   }

   public void setEnterMap(boolean isEnterMap) {
      this.isEnterMap = isEnterMap;
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

   public long getLastSetPotential() {
      return this.lastSetPotential;
   }

   public void setLastSetPotential(long lastSetPotential) {
      this.lastSetPotential = lastSetPotential;
   }

   public void setRemotePort(int port) {
      this.remotePort = port;
   }

   public int getFinishPreview() {
      return this.finishPreview;
   }

   public void setFinishPreview(int finishPreview) {
      this.finishPreview = finishPreview;
   }

   public boolean popNoAgain(int type) {
      return this.popNoAgainSet == null ? false : this.popNoAgainSet.contains(type);
   }

   public void addPopNoAgain(int type) {
      if (this.popNoAgainSet == null) {
         this.popNoAgainSet = new HashSet();
      }

      this.popNoAgainSet.add(type);
   }

   public void addSd(int value) {
      if (FunctionOpenManager.isOpen(this, 19)) {
         super.addSd(value);
      }
   }

   public boolean beAttacked(Creature attacker, AttackResult result) {
      if (!this.isEnterMap()) {
         return false;
      } else {
         this.getPetManager().playerBeAttacked(attacker, result);
         this.getEquipment().beingAttacked();
         if (attacker != null && (attacker.getType() == 1 || attacker.getType() == 4)) {
            this.setLastFightingTime(System.currentTimeMillis());
            Player other = null;
            if (attacker.getType() == 4) {
               other = ((Pet)attacker).getOwner();
            } else {
               other = (Player)attacker;
            }

            other.setLastFightingTime(System.currentTimeMillis());
         }

         return super.beAttacked(attacker, result);
      }
   }

   public boolean doAttack(Creature target, AttackResult result, boolean handleMotion) {
      boolean attackSuccess = target.beAttacked(this, result);
      if (attackSuccess) {
         target.hpReduceForDamage(this, result);
      }

      if (target != null && (target.getType() == 1 || target.getType() == 4)) {
         this.setLastFightingTime(System.currentTimeMillis());
      }

      return attackSuccess;
   }

   public int canBeAttackedByPlayer(Player attacker) {
      return AttackCreature.getPkCheckResult(attacker, this);
   }

   public boolean hasAttackedMarkForShow(Player observer) {
      return false;
   }

   public int getWorldLine() {
      return this.worldLine;
   }

   public void setWorldLine(int worldLine) {
      this.worldLine = worldLine;
   }

   public void loadEvil(int evil) {
      evil = Math.min(VariableConstant.Pk_Evil_Max, Math.max(0, evil));
      this.evil = evil;
   }

   public void changeEvil(int e) {
      int oldEvil = this.evil;
      this.loadEvil(e);
      if (this.evil != oldEvil) {
         if (this.evil < VariableConstant.PK_Evil_Red) {
            if (oldEvil >= VariableConstant.PK_Evil_Red) {
               this.getBuffManager().endBuff(80002, true);
            }
         } else {
            Buff buff = this.getBuffManager().getBuff(80002);
            if (buff != null) {
               BuffUpdateDydata.sendToClient(this, buff);
            } else {
               this.useBuff(80002, 1, new Object[0]);
            }
         }
      }

      PlayerAttributes.sendToClient(this, StatEnum.EVIL);
   }

   public EvilEnum getSelfEvilEnum() {
      if (this.getEvil() >= VariableConstant.PK_Evil_Red) {
         return EvilEnum.Evil_Red;
      } else {
         return this.getBuffManager().getBuff(80001) != null ? EvilEnum.Evil_Gray : EvilEnum.Evil_Orange;
      }
   }

   public EvilEnum getEvilEnumBeingSaw() {
      if (this.getEvil() >= VariableConstant.PK_Evil_Red) {
         return EvilEnum.Evil_Red;
      } else {
         return this.getBuffManager().getBuff(80001) != null ? EvilEnum.Evil_Gray : EvilEnum.Evil_White;
      }
   }

   public DunLogManager getDunLogsManager() {
      return this.dunLogManager;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      PlayerSelfDie.sendToClient(this, result);
      CreatureDie.sendToClient(this);
      Map map = this.getMap();
      if (map != null) {
         map.playerDie(this, attacker);
      }

      Player killer = null;
      if (attacker != null) {
         switch(attacker.getUnitType()) {
         case 1:
            killer = (Player)attacker;
         case 2:
         case 3:
         default:
            break;
         case 4:
            killer = ((Pet)attacker).getOwner();
         }
      }

      if (killer != null) {
         EvilManager.killPlayer(this, killer);
         Gang gang = this.getGang();
         if (gang != null) {
            if (gang.isMaster(this.getID()) || gang.isViceMaster(this.getID())) {
               BroadcastManager.broadcastGangMasterBeKilled(this, killer);
            }

            if (!this.isInDungeon()) {
               gang.memberBeKilled(this, killer);
            }
         }

         if ((killer.getUnitType() == 1 || killer.getUnitType() == 4) && this.getEvil() < 1 && this.getPkMode().getCurrentPKMode() == PkEnum.Mode_Peace) {
            this.getBuffManager().createAndStartBuff(this, 80003, 1, true, 0L, (List)null);
         }

         if (map.canBeEnemy()) {
            this.getFriendManager().addEnemy(killer);
         }
      }

      this.getPetManager().hide();
      this.setLastFightingTime(0L);
   }

   public Creature getAttackTarget() {
      if (this.getStatus() == Status.ATTACK) {
         PlayerAttackEvent event = (PlayerAttackEvent)this.getStatusEvent();
         return event.getTarget();
      } else {
         return null;
      }
   }

   public void attack(Creature target) {
      this.doStatusEvent(new PlayerAttackEvent(this, target));
      Pet pet = this.petManager.getActivePet();
      if (pet != null) {
         pet.setLastAttackTarget(target);
      }

   }

   public int getOutOfRangeTimes() {
      return this.outOfRangeTimes;
   }

   public void setOutOfRangeTimes(int outOfRangeTimes) {
      this.outOfRangeTimes = outOfRangeTimes;
   }

   public int getMovementType() {
      return this.equipment.getMovementType();
   }

   public boolean isInRiding() {
      return this.inRiding;
   }

   public void setInRiding(boolean inRiding) {
      this.inRiding = inRiding;
   }

   public long getEarliestRidingTime() {
      return this.earliestRidingTime;
   }

   private void setRidingStatus(boolean newInRiding) {
      boolean oldStatus = this.inRiding;
      this.setInRiding(newInRiding);
      if (oldStatus != newInRiding) {
         if (newInRiding) {
            StatChange.addStat(this, StatIdCreator.createHorseStatId(0), this.getEquipment().getRidingStats(), true);
         } else {
            StatChange.endStat(this, StatIdCreator.createHorseStatId(0), true);
         }
      }

   }

   public void handleUseSkill(boolean debenify) {
      super.handleUseSkill(debenify);
      this.earliestRidingTime = System.currentTimeMillis() + 5000L;
      if (debenify) {
         if (this.isInRiding()) {
            this.setRidingStatus(false);
            RidingChange.sendToClient(this);
         }
      }
   }

   public void handleMove(long now) {
      super.handleMove(now);
      if (this.getEarliestRidingTime() <= now) {
         if (!this.isInRiding()) {
            if (this.getEquipment().canRide()) {
               this.setRidingStatus(true);
               RidingChange.sendToClient(this);
            }

         }
      }
   }

   public void stopRidingStatus() {
      if (this.isInRiding()) {
         this.earliestRidingTime = System.currentTimeMillis() + 5000L;
         this.setInRiding(false);
         RidingChange.sendToClient(this);
      }
   }

   public Backpack getBackpack() {
      return this.backpack;
   }

   public Storage getDepot() {
      return this.depot;
   }

   public Container getContainer(int type) {
      switch(type) {
      case 0:
         return this.equipment;
      case 1:
         return this.backpack;
      case 4:
         return this.depot;
      case 14:
         return this.treasureHouse;
      default:
         return null;
      }
   }

   public Storage getStorage(int type) {
      switch(type) {
      case 1:
         return this.backpack;
      case 4:
         return this.depot;
      case 14:
         return this.treasureHouse;
      default:
         return null;
      }
   }

   public Storage getTreasureHouse() {
      return this.treasureHouse;
   }

   public ItemManager getItemManager() {
      return this.itemManger;
   }

   public Equipment getEquipment() {
      return this.equipment;
   }

   public MailManager getMailManager() {
      return this.mailManager;
   }

   public SystemSetup getSystemSetup() {
      return this.systemSetup;
   }

   public GameHang getGameHang() {
      return this.gameHang;
   }

   public long getLastChatWorldTime() {
      return this.lastChatWorldTime;
   }

   public void setLastChatWorldTime(long cur) {
      this.lastChatWorldTime = cur;
   }

   public long getLastChatAroundTime() {
      return this.lastChatAroundTime;
   }

   public void setLastChatAroundTime(long lastChatAroundTime) {
      this.lastChatAroundTime = lastChatAroundTime;
   }

   public PlayerTaskManager getTaskManager() {
      return this.taskManager;
   }

   public PKMode getPkMode() {
      return this.pkMode;
   }

   public int getPkModelIDToDB() {
      return this.pkMode.getModeToDB();
   }

   public void setPkMode(PKMode pkMode) {
      this.pkMode = pkMode;
   }

   public Shortcut getShortcut() {
      return this.shortcut;
   }

   public long getLogoutTime() {
      return this.logoutTime;
   }

   public void setLogoutTime(long logoutTime) {
      this.logoutTime = logoutTime;
   }

   public void setLoginTime(long time) {
      this.loginTime = time;
   }

   public long getLoginTime() {
      return this.loginTime;
   }

   public void stopCountDown() {
      if (this.getStatus() == Status.CountDown) {
         this.idle();
      }

   }

   public void startCountDown(CountdownObject obj) {
      if (obj.occupateStatus()) {
         this.doStatusEvent(new CountDownEvent(this, new CountdownCtller(obj)));
      } else {
         this.addMomentEvent(new CountDownEvent(this, new CountdownCtller(obj)));
      }

   }

   public Gang getGang() {
      GangMember member = GangManager.getMember(this.getID());
      return member == null ? null : GangManager.getGang(member.getGangId());
   }

   public long getGangId() {
      Gang gang = this.getGang();
      return gang == null ? -1L : gang.getId();
   }

   public Team getCurrentTeam() {
      return this.currentTeam;
   }

   public void setCurrentTeam(Team currentTeam) {
      this.currentTeam = currentTeam;
   }

   public PlayerDropManager getDropManager() {
      return this.dropManager;
   }

   public int getTodayOnlineTime() {
      return this.todayOnlineTime;
   }

   public void setTodayOnlineTime(int todayOnlineTime) {
      this.todayOnlineTime = todayOnlineTime;
   }

   public void addTodayOnlineTime(int time) {
      this.todayOnlineTime += time;
   }

   public void addThisOnlineTime(int time) {
      this.thisOnlineTime += time;
   }

   public void addTotalOnlineTime(int time) {
      this.totalOnlineTime += time;
   }

   public int getTotalOnlineTime() {
      return this.totalOnlineTime;
   }

   public int getThisOnlineTime() {
      return this.thisOnlineTime;
   }

   public void setThisOnlineTime(int totalOnlineTime) {
      this.totalOnlineTime = totalOnlineTime;
   }

   public void setTotalOnlineTime(int totalOnlineTime) {
      this.totalOnlineTime = totalOnlineTime;
   }

   public int getRedeemPoints() {
      return this.redeemPoints;
   }

   public void setRedeemPoints(int redeemPoints) {
      this.redeemPoints = redeemPoints;
   }

   public int getVIPExp() {
      return this.vipExp;
   }

   public void setVIPExp(int vipExp) {
      this.vipExp = Math.max(0, Math.min(this.getVIPManager().getMaxExp() - 1, vipExp));
   }

   public void setVIPLevel(int vipLevel) {
      this.vipLevel = Math.max(0, Math.min(VIPConfigManager.getLevelTail().getLevel(), vipLevel));
   }

   public int getVIPLevel() {
      return this.vipLevel;
   }

   public int getVipShowLevel() {
      if (this.vipManager != null) {
         return this.vipManager.isTimeOut() ? 0 : this.vipLevel;
      } else {
         return 0;
      }
   }

   public PlayerVIPManager getVIPManager() {
      return this.vipManager;
   }

   public PlayerPetManager getPetManager() {
      return this.petManager;
   }

   public int getWarComment() {
      return this.warComment;
   }

   public long getWarCommentTime() {
      return this.warCommentTime;
   }

   public void setWarCommentTime(long warCommentTime) {
      this.warCommentTime = warCommentTime;
   }

   public void setWarComment(int wc) {
      if (wc > TrialConfigs.getMaxLevel()) {
         wc = TrialConfigs.getMaxLevel();
      }

      this.warComment = wc;
   }

   public void revival() {
      super.revival();
      PlayerRevival.revivalCorrect(this);
   }

   public boolean isFighting() {
      return System.currentTimeMillis() - this.lastFightingTime < 10000L;
   }

   public long getLastFightingTime() {
      return this.lastFightingTime;
   }

   public void setLastFightingTime(long lastFightingTime) {
      this.lastFightingTime = lastFightingTime;
   }

   public int getInitType() {
      return this.initType;
   }

   public void setInitType(int initType) {
      this.initType = initType;
   }

   public boolean isNeedZeroClear() {
      return this.getLogoutTime() < ZeroDailyTask.lastClearTime;
   }

   public DialogOptionTask getDot() {
      return this.dot;
   }

   public void setDot(DialogOptionTask dot) {
      this.dot = dot;
   }

   public void setPushTaskFuture(ScheduledFuture future) {
      this.cancelPushTaskFutre();
      this.pushTaskContinueFuture = future;
   }

   public ScheduledFuture getPushTaskFuture() {
      return this.pushTaskContinueFuture;
   }

   public void cancelPushTaskFutre() {
      if (this.pushTaskContinueFuture != null && !this.pushTaskContinueFuture.isCancelled()) {
         this.pushTaskContinueFuture.cancel(true);
         this.pushTaskContinueFuture = null;
      }

   }

   public void clearSpeedUpTimes() {
      this.speedUpTimes = 0;
   }

   public int getSpeedUpTimes() {
      return this.speedUpTimes;
   }

   public void addSpeedUpTimes() {
      ++this.speedUpTimes;
   }

   public Panda getPanda() {
      return this.panda;
   }

   public void setPanda(Panda panda) {
      this.panda = panda;
   }

   public ArrowGuideManager getArrowGuideManager() {
      return this.arrowGuideManager;
   }

   public void useSkill(int skillID, Point cp, Creature target, boolean clientRequest) {
      super.useSkill(skillID, cp, target, clientRequest);
      Skill skill = this.getSkillManager().getSkill(skillID);
      if (skill != null && skill.isDeBenefiesSkill()) {
         Pet pet = this.petManager.getActivePet();
         if (pet != null) {
            pet.setLastAttackTarget(target);
         }
      }

   }

   public byte[] getView() {
      byte[] bytes = null;

      try {
         WriteOnlyPacket packet = new WriteOnlyPacket(-1);
         OtherPlayerDetail.writePlayer(this, packet);
         OtherPlayerDetail.writePlayerPet(this, packet);
         bytes = packet.toByteArray();
         packet.destroy();
         packet = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return bytes;
   }

   public PlayerSignManager getSignManager() {
      return this.signManager;
   }

   public PlayerVitalityManager getVitalityManager() {
      return this.vitalityManager;
   }

   public PlayerShieldManager getShieldManager() {
      return this.shieldManager;
   }

   public ActivityLogs getActivityLogs() {
      return this.activityLogs;
   }

   public PlayerFinancingManager getFinancingManager() {
      return this.financingManager;
   }

   public PlayerOnlineManager getOnlineManager() {
      return this.onlineManager;
   }

   public boolean isNeedtoPopPKEvil() {
      return this.needtoPopPKEvil;
   }

   public void setNeedtoPopPKEvil(boolean needtoPopPKEvil) {
      this.needtoPopPKEvil = needtoPopPKEvil;
   }

   public boolean isNeddAutoTask() {
      return this.neddAutoTask;
   }

   public long getLastChatTime() {
      return this.lastChatTime;
   }

   public void setLastChatTime(long lastChatTime) {
      this.lastChatTime = lastChatTime;
   }

   public void setNeddAutoTask(boolean neddAutoTask) {
      this.neddAutoTask = neddAutoTask;
   }

   public SevenDayTreasure getSevenManager() {
      return this.sevenManager;
   }

   public long getLastMarketCallTime() {
      return this.lastMarketCallTime;
   }

   public void setLastMarketCallTime(long lastMarketCallTime) {
      this.lastMarketCallTime = lastMarketCallTime;
   }

   public int getContribution() {
      GangMember member = GangManager.getMember(this.getID());
      return member == null ? 0 : member.getCurContribution();
   }

   public int getHisContribution() {
      GangMember member = GangManager.getMember(this.getID());
      return member == null ? 0 : member.getHisContribution();
   }

   public boolean isRobot() {
      return false;
   }

   public String getVipImgText() {
      return this.getUser().getBlueVip().getBlueIcon().getText();
   }

   public String getRemoteIp() {
      return this.remoteIp;
   }

   public void setRemoteIp(String remoteIp) {
      this.remoteIp = remoteIp;
   }

   public void addFunctionOpenTime(int id, long time) {
      this.functionOpenMap.put(id, time);
   }

   public long getFunctionOpenTime(int id) {
      Long time = (Long)this.functionOpenMap.get(id);
      return time == null ? 0L : time.longValue();
   }

   public int getTeamExpBonus() {
      Team team = this.getCurrentTeam();
      return team == null ? 0 : team.getMateExpBouns(this);
   }

   public void addTopProperties(TopRewardInfo info) {
      if (info != null) {
         ArrayList list = info.getAttrList();
         if (list != null && list.size() != 0) {
            ArrayList statList = new ArrayList();
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               int[] in = (int[])var5.next();
               StatEnum se = StatEnum.find(in[0]);
               FinalModify fm = new FinalModify(se, in[1], StatModifyPriority.fine(in[2]));
               statList.add(fm);
            }

            StatChange.addStat(this, StatIdCreator.createTopRewardID(info.getTopId()), statList, StatChange.isSendStat(this));
            statList.clear();
         }
      }
   }

   public void removeTopProperties(int topId) {
      StatChange.endStat(this, StatIdCreator.createTopRewardID(topId), true);
   }

   public int getTeamMateExpBonus(Player matePlayer) {
      try {
         if (matePlayer == null || matePlayer.getID() == this.getID()) {
            return 0;
         }

         if (this.isInDungeon()) {
            if (this.getMap().equals(matePlayer.getMap())) {
               return 5;
            }
         } else if (this.getMapID() == matePlayer.getMapID()) {
            return 5;
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return 0;
   }

   public int[] getVipIcons() {
      return this.getUser().getBlueVip().getBlueIcon().getIcons();
   }

   public PlayerTanXianManager getTanXianManager() {
      return this.tanXianManager;
   }

   public int getTurnTableCount() {
      return this.turnTableCount;
   }

   public void setTurnTableCount(int turnTableCount) {
      this.turnTableCount = turnTableCount;
   }
}
