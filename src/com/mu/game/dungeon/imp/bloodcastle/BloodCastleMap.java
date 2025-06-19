package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.IDFactory;
import com.mu.game.dungeon.DungeonMonster;
import com.mu.game.dungeon.MonsterDie;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.drop.DropItem;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.robot.RobotInfo;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.map.ChangeBlocks;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.io.game.packet.imp.monster.ForceAttackMonster;
import com.mu.io.game.packet.imp.npc.ForceChatWithNpc;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.io.game.packet.imp.player.AroundPlayer;
import com.mu.io.game.packet.imp.player.hangset.ChangeDunHangRange;
import com.mu.io.game.packet.imp.player.hangset.StartHang;
import com.mu.utils.Rnd;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BloodCastleMap extends DungeonMap implements MonsterDie {
   private int schedule = 1;
   private int curCounter = 0;
   private BloodCastleMonster gateMonster = null;
   private BloodCastleMonster coffinMonster = null;
   private BloodCastleMonster bloodAngel = null;
   private Archangel angel = null;
   private List angelWeapoinList = null;
   private int curRank;
   private boolean gateIsBreak = false;
   private BloodCastleRobot robot = null;
   private boolean checkDrop = false;

   public BloodCastleMap(BloodCastle d) {
      super(((BloodCastleTemplate)d.getTemplate()).getDefaultMapID(), d);
      this.setDefaultPoint(new Point(((BloodCastleTemplate)d.getTemplate()).getBornX(), ((BloodCastleTemplate)d.getTemplate()).getBornY()));
      this.curRank = ((BloodCastleTemplate)d.getTemplate()).getMaxRank();
   }

   public void start() {
      BloodCastleLevel level = ((BloodCastle)this.getDungeon()).getBloodCastleLevel();
      ArrayList list = level.getGroupBridge();
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         BloodCastleMonsterGroup group = (BloodCastleMonsterGroup)var4.next();
         int num = group.getNum();
         for(num = 0; num < num; ++num) {
            BloodCastleMonster monster = new BloodCastleMonster(group, this);
            this.addMonster(monster);
         }
      }

      ArrayList listWizard = level.getGroupWizard();
      Iterator var11 = listWizard.iterator();

      BloodCastleMonsterGroup group;
      while(var11.hasNext()) {
         group = (BloodCastleMonsterGroup)var11.next();
         int num = group.getNum();

         for(int i = 0; i < num; ++i) {
            BloodCastleMonster monster = new BloodCastleMonster(group, this);
            this.addMonster(monster);
         }
      }

      group = level.getGroupGate();
      BloodCastleMonsterGroup coffinGroup = level.getGroupCoffin();
      this.gateMonster = new BloodCastleMonster(group, this);
      this.addMonster(this.gateMonster);
      this.gateMonster.setCanBeAttacked(false);
      this.coffinMonster = new BloodCastleMonster(coffinGroup, this);
      this.addMonster(this.coffinMonster);
      this.coffinMonster.setCanBeAttacked(false);
      this.angel = new Archangel(IDFactory.getTemporaryID(), ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getAngelInfo(), this);
      this.addNpc(this.angel);
   }

   protected void doWork(long now) {
      super.doWork(now);
      if (this.checkDrop && this.getDropItemSize() == 0 && this.schedule >= 6) {
         this.checkDrop = false;
         ((BloodCastle)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               ForceChatWithNpc cn = new ForceChatWithNpc(BloodCastleMap.this.angel.getID());
               BloodCastleMap.this.broadcastPacket(cn);
               cn.destroy();
               cn = null;
            }
         }, 500L));
      }

   }

   public ScheduleConditions getCurrentConditions() {
      return ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getScheduleConditions(this.schedule);
   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      ChangeDunHangRange.change(player, 1000);
      this.pushSchedule();
      ArrowGuideManager.pushArrow(player, 7, (String)null);
      if (!this.gateIsBreak) {
         ChangeBlocks.changeMapBlocks(player, ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getGateBlockList(), false);
      }

      if (player.getDunLogsManager().getTotalNumber(1) <= ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getRobotTimes() && this.robot == null) {
         this.createRobot(player);
      }

   }

   private void createRobot(Player player) {
      RobotInfo info = ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getRobotInfo(player.getProType());
      if (info != null) {
         this.robot = new BloodCastleRobot(info, this);
         this.robot.setFindWay(true);
         int minX = player.getX() - 3000;
         int maxX = player.getX() + 3000;
         int minY = player.getY() - 3000;
         int maxY = player.getY() + 3000;
         Point p = this.searchFeasiblePoint(Rnd.get(minX, maxX), Rnd.get(minY, maxY));
         if (p == null) {
            p = new Point(player.getX(), player.getY());
         }

         this.robot.setPosition(p);
         this.robot.setBornPoint(p);
         this.addPlayer(this.robot, p);
         this.robot.setEnterMap(true);
         AroundPlayer ap = new AroundPlayer(this.robot, player);
         this.broadcastPacket(ap);
         ap.destroy();
         ap = null;
      }

   }

   public Player removePlayer(Player player) {
      super.removePlayer(player);
      ChangeDunHangRange.change(player, 0);
      ChangeBlocks.changeMapBlocks(player, ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getGateBlockList(), true);
      if (this.robot != null && !this.robot.isDestroy()) {
         this.robot.setShouldDestroy(true);
         this.robot = null;
      }

      return player;
   }

   public void destroyRobot() {
      if (this.robot != null) {
         this.playerMap.remove(this.robot.getID());
         this.robot.destroy();
         this.robot = null;
      }

   }

   public List getAngelWeapoinList() {
      return this.angelWeapoinList;
   }

   private void refreshBloodAngel() {
      this.bloodAngel = new BloodCastleMonster(((BloodCastle)this.getDungeon()).getBloodCastleLevel().getGroupAngel(), this);
      this.addMonster(this.bloodAngel);
      AroundMonster am = new AroundMonster(this.bloodAngel);
      this.broadcastPacket(am);
      am.destroy();
      am = null;
   }

   public boolean pickUpItem(DropItem di, Player player) {
      int modelId = di.getItem().getModelID();
      boolean b = super.pickUpItem(di, player);
      if (this.angelWeapoinList != null) {
         Iterator var6 = this.angelWeapoinList.iterator();

         while(var6.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var6.next();
            if (modelId == unit.getModelID()) {
               this.schedule = 6;
               this.curCounter = 0;
               this.refreshBloodAngel();
               ForceAttackMonster fm = new ForceAttackMonster(this.bloodAngel.getTemplateId());
               this.broadcastPacket(fm);
               fm.destroy();
               fm = null;
            }
         }
      }

      return b;
   }

   private void deleteAllAngelWeapons() {
      Iterator it = this.getPlayerMap().values().iterator();

      while(true) {
         Player p;
         do {
            do {
               if (!it.hasNext()) {
                  return;
               }

               p = (Player)it.next();
            } while(p.isRobot());
         } while(this.angelWeapoinList == null);

         Iterator var4 = this.angelWeapoinList.iterator();

         while(var4.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var4.next();
            int count = p.getBackpack().getItemCount(unit.getModelID());
            if (count > 0) {
               p.getItemManager().deleteItemByModel(unit.getModelID(), count, 31);
            }
         }
      }
   }

   public void doSuccess() {
      ((BloodCastle)this.getDungeon()).setSuccess(true);
      this.schedule = 7;
      this.pushSchedule();
      this.deleteAllAngelWeapons();
      ArrayList list = new ArrayList();
      Iterator ru = this.monsterMap.values().iterator();

      while(ru.hasNext()) {
         Monster m = (Monster)ru.next();
         m.setShouldDestroy(true);
         list.add(m);
      }

      RemoveUnit ru2 = new RemoveUnit(list);
      this.broadcastPacket(ru2);
      ru2.destroy();
      ru = null;
      list.clear();
      list = null;
      ((BloodCastle)this.getDungeon()).doSuccess();
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (!player.isRobot()) {
            ((BloodCastle)this.getDungeon()).doReceive(player);
            player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.FB_Appraise, ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getTemplateID(), this.curRank);
         }
      }

      ((BloodCastle)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            ((BloodCastle)BloodCastleMap.this.getDungeon()).destroy();
         }
      }, 15000L));
   }

   private int getPercent(BloodCastleRank rank) {
      int base = (((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getMaxRank() - rank.getRank()) * 250;
      int curPer = (int)((float)((BloodCastle)this.getDungeon()).getCurRankTimeCost() * 1.0F / (float)rank.getActualTime() * 1.0F * 250.0F);
      int tmp = 1000 - (base + curPer);
      return tmp < 0 ? 0 : tmp;
   }

   public void pushSchedule() {
      BloodCastleTemplate template = (BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate();
      BloodCastleRank rank = template.getRankByLeftTime(((BloodCastle)this.getDungeon()).getActiveTime());
      if (this.curRank != rank.getRank()) {
         this.curRank = rank.getRank();
         ((BloodCastle)this.getDungeon()).setCurRankTimeCost(0);
      }

      ScheduleConditions con = this.getCurrentConditions();

      try {
         DungeonInfoUpdate du = new DungeonInfoUpdate();
         du.writeByte(template.getTemplateID());
         du.writeInt(((BloodCastle)this.getDungeon()).getTimeCost());
         du.writeByte(this.schedule);
         du.writeByte(7);
         du.writeUTF(con.getExplain().replace("%s%", String.valueOf(this.curCounter)).replace("%m%", String.valueOf(con.getMaxNumber())));
         du.writeByte(rank.getRank());
         du.writeShort(this.getPercent(rank));
         du.writeShort(1000);
         du.writeUTF(rank.getTarget());
         du.writeInt(((BloodCastle)this.getDungeon()).getActiveTime());
         this.broadcastPacket(du);
         du.destroy();
         du = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void docCoffinDrop() {
      this.angelWeapoinList = ((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getForceDrop(this.getCurrentConditions().getTarget());
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player p = (Player)it.next();
         if (!p.isRobot()) {
            SystemDropManager.forceDrop(this.angelWeapoinList, p, new Point(((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getDropPoint()));
         }
      }

   }

   public BloodCastleMonster getGateMonster() {
      return this.gateMonster;
   }

   public BloodCastleMonster getCoffinMonster() {
      return this.coffinMonster;
   }

   public BloodCastleMonster getBloodAngel() {
      return this.bloodAngel;
   }

   public void monsterBeKilled(BloodCastleMonster monster) {
      int batch = monster.getBatch();
      ScheduleConditions con = this.getCurrentConditions();
      ArrayList list;
      Iterator it;
      ChangePkView cv;
      ForceAttackMonster fm;
      switch(this.schedule) {
      case 1:
         if (batch == 1 && con.contains(monster.getTemplateId())) {
            ++this.curCounter;
            if (this.curCounter == con.getMaxNumber()) {
               this.schedule = 2;
               this.gateMonster.setCanBeAttacked(true);
               cv = new ChangePkView(this.gateMonster, true);
               this.broadcastPacket(cv);
               cv.destroy();
               list = null;
               this.curCounter = 0;
               fm = new ForceAttackMonster(this.gateMonster.getTemplateId());
               this.broadcastPacket(fm);
               fm.destroy();
               it = null;
            }
         }
         break;
      case 2:
         if (monster.getID() == this.gateMonster.getID()) {
            ++this.curCounter;
            this.gateIsBreak = true;
            ChangeBlocks cb = ChangeBlocks.getChangeBlocks(((BloodCastleTemplate)((BloodCastle)this.getDungeon()).getTemplate()).getGateBlockList(), true);
            this.broadcastPacket(cb);
            cb.destroy();
            list = null;
            this.schedule = 3;
            this.gateMonster.setShouldDestroy(true);
            this.curCounter = 0;
            fm = new ForceAttackMonster(this.getCurrentConditions().getTarget()[0]);
            this.broadcastPacket(fm);
            fm.destroy();
            it = null;
         }
         break;
      case 3:
         if (batch == 3 && con.contains(monster.getTemplateId())) {
            ++this.curCounter;
            if (this.curCounter == con.getMaxNumber()) {
               this.schedule = 4;
               this.coffinMonster.setCanBeAttacked(true);
               cv = new ChangePkView(this.coffinMonster, true);
               this.broadcastPacket(cv);
               cv.destroy();
               list = null;
               this.curCounter = 0;
               fm = new ForceAttackMonster(this.coffinMonster.getTemplateId());
               this.broadcastPacket(fm);
               fm.destroy();
               it = null;
            }
         }
         break;
      case 4:
         if (batch == 4 && con.contains(monster.getTemplateId())) {
            ++this.curCounter;
            if (this.curCounter == con.getMaxNumber()) {
               this.schedule = 5;
               this.docCoffinDrop();
               this.coffinMonster.setShouldDestroy(true);
               this.curCounter = 0;
               list = new ArrayList();
               it = this.monsterMap.values().iterator();

               while(it.hasNext()) {
                  Monster m = (Monster)it.next();
                  m.setShouldDestroy(true);
                  list.add(m);
               }

               if (list.size() > 0) {
                  RemoveUnit ru = new RemoveUnit(list);
                  this.broadcastPacket(ru);
                  ru.destroy();
                  it = null;
               }

               list.clear();
               list = null;
            }
         }
      case 5:
      default:
         break;
      case 6:
         if (con.contains(monster.getTemplateId())) {
            ++this.curCounter;
            ++this.schedule;
            this.curCounter = 0;
            this.checkDrop = true;
            ((BloodCastle)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
               public void run() {
                  ForceChatWithNpc cn = new ForceChatWithNpc(BloodCastleMap.this.angel.getID());
                  BloodCastleMap.this.broadcastPacket(cn);
                  cn.destroy();
                  cn = null;
               }
            }, 6000L));
         }
      }

   }

   public void playerDie(Player player, Creature attacker) {
      DungeonResult.faild(player, 1);
      ((BloodCastle)this.getDungeon()).setFaild(true);
   }

   public int getSchedule() {
      return this.schedule;
   }

   public void setSchedule(int s) {
      this.schedule = s;
   }

   public void doNext(Player player) {
      WriteOnlyPacket packet = null;
      switch(this.getSchedule()) {
      case 1:
      case 5:
         StartHang.start(player);
         break;
      case 2:
         packet = new ForceAttackMonster(this.gateMonster.getTemplateId());
         break;
      case 3:
         packet = new ForceAttackMonster(this.getCurrentConditions().getTarget()[0]);
         break;
      case 4:
         packet = new ForceAttackMonster(this.coffinMonster.getTemplateId());
         break;
      case 6:
         packet = new ForceAttackMonster(this.bloodAngel.getTemplateId());
         break;
      case 7:
         packet = new ForceChatWithNpc(this.angel.getID());
      }

      if (packet != null) {
         player.writePacket((WriteOnlyPacket)packet);
         ((WriteOnlyPacket)packet).destroy();
         packet = null;
      }

   }

   public void checkIdle(Player player) {
      this.doNext(player);
   }

   public synchronized void destroy() {
      super.destroy();
      this.gateMonster = null;
      this.coffinMonster = null;
      this.bloodAngel = null;
      this.angel = null;
      if (this.angelWeapoinList != null) {
         this.angelWeapoinList.clear();
         this.angelWeapoinList = null;
      }

      if (this.robot != null && !this.robot.isDestroy()) {
         this.robot.destroy();
         this.robot = null;
      }

   }

   @Override
   public void monsterBeKilled(DungeonMonster var1) {

   }
}
