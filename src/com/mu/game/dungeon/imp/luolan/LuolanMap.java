package com.mu.game.dungeon.imp.luolan;

import com.mu.config.BroadcastManager;
import com.mu.game.IDFactory;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.chat.ChatProcess;
import com.mu.game.model.chat.newlink.NewChatLink;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.mail.SendBatchMailTask;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.controller.CountdownCtller;
import com.mu.game.model.unit.material.MaterialTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.game.model.unit.player.pkMode.PkEnum;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.unit.unitevent.imp.LeaveGearEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.chat.ForwardMessage;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DungeonInfoUpdate;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.map.ChangeBlocks;
import com.mu.io.game.packet.imp.pkModel.ChangePkMode;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.io.game.packet.imp.sys.CenterMessage;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class LuolanMap extends DungeonMap {
   private HashMap gateMap = new HashMap();
   private boolean isEnd;
   private boolean isBegin;
   private RevivalStatue revivalStatue = null;
   private Gear gear = null;
   private Crown crown = null;
   private Player gearMan = null;
   private Player markMan = null;
   private Gang victoryGang = null;

   public LuolanMap(int referMapID, Luolan d) {
      super(referMapID, d);
      this.setCanPk(true);
   }

   public void init() {
      HashMap lm = ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getGateMap();
      Iterator it = lm.values().iterator();

      while(it.hasNext()) {
         GateData gd = (GateData)it.next();
         LuolanGate lg = new LuolanGate(gd, this);
         this.addMonster(lg);
         this.gateMap.put(gd.getId(), lg);
      }

      HashMap mm = ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getMaterialMap();
      it = mm.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         int id = ((Integer)entry.getKey()).intValue();
         LuolanMaterialGroup group = (LuolanMaterialGroup)entry.getValue();
         MaterialTemplate mt = MaterialTemplate.getTemplate(group.getTemplateID());
         switch(id) {
         case 1:
            this.revivalStatue = new RevivalStatue(IDFactory.getTemporaryID(), this, group.getTemplateID(), mt.getName(), mt.getModelId(), group.getX(), group.getY(), group.getFace(), group.getFindwayPoint());
            this.revivalStatue.setCollectText(group.getCollectionText());
            this.addMaterial(this.revivalStatue);
            break;
         case 2:
            this.gear = new Gear(IDFactory.getTemporaryID(), this, group.getTemplateID(), mt.getName(), mt.getModelId(), group.getX(), group.getY(), group.getFace(), group.getFindwayPoint());
            this.gear.setCollectText(group.getCollectionText());
            this.addMaterial(this.gear);
            break;
         case 3:
            this.crown = new Crown(IDFactory.getTemporaryID(), this, group.getTemplateID(), mt.getName(), mt.getModelId(), group.getX(), group.getY(), group.getFace(), group.getFindwayPoint());
            this.crown.setCollectText(group.getCollectionText());
            this.addMaterial(this.crown);
         }
      }

   }

   public DungeonInfoUpdate getBattleInfo(long gid) {
      int top = ((Luolan)this.getDungeon()).getTop(gid);
      if (top == -1) {
         return null;
      } else {
         Gang gang = GangManager.getGang(gid);
         if (gang == null) {
            return null;
         } else {
            try {
               DungeonInfoUpdate gi = new DungeonInfoUpdate();
               gi.writeByte(9);
               LuolanGate gate = (LuolanGate)this.gateMap.get(top);
               gi.writeByte(4);
               gi.writeByte(gate.isDie() ? 1 : 0);
               gi.writeInt(gate.getFindwayPoint().x);
               gi.writeInt(gate.getFindwayPoint().y);
               gi.writeByte(this.gear.getOccupyGang() == gid ? 1 : 0);
               gi.writeInt(this.gear.getFindwayPoint().x);
               gi.writeInt(this.gear.getFindwayPoint().y);
               gi.writeByte(((Luolan)this.getDungeon()).getMarkTimes(gid));
               gi.writeInt(this.crown.getFindwayPoint().x);
               gi.writeInt(this.crown.getFindwayPoint().y);
               gi.writeByte(this.revivalStatue.getOccupyGang() == gid ? 1 : 0);
               gi.writeInt(this.revivalStatue.getFindwayPoint().x);
               gi.writeInt(this.revivalStatue.getFindwayPoint().y);
               ArrayList list = new ArrayList();
               ArrayList listMark = ((Luolan)this.getDungeon()).getMarkList();
               Iterator var10 = listMark.iterator();

               while(var10.hasNext()) {
                  MarkInfo mi = (MarkInfo)var10.next();
                  Gang tmpGang = GangManager.getGang(mi.getGangId());
                  if (tmpGang != null) {
                     list.add(new String[]{tmpGang.getName(), String.valueOf(mi.getTimes())});
                  }
               }

               gi.writeByte(list.size());
               var10 = list.iterator();

               while(var10.hasNext()) {
                  String[] strs = (String[])var10.next();
                  gi.writeUTF(strs[0]);
                  gi.writeByte(Integer.parseInt(strs[1]));
               }

               gi.writeByte(2);
               if (this.gearMan != null) {
                  gi.writeBoolean(true);
                  gi.writeDouble((double)this.gearMan.getID());
                  gi.writeUTF(this.gearMan.getName());
                  gi.writeShort(this.gearMan.getLevel());
                  gi.writeByte(this.gearMan.getProfessionID());
                  gi.writeUTF(this.gearMan.getWarCommentText());
               } else {
                  gi.writeBoolean(false);
               }

               if (this.markMan != null) {
                  gi.writeBoolean(true);
                  gi.writeDouble((double)this.markMan.getID());
                  gi.writeUTF(this.markMan.getName());
                  gi.writeShort(this.markMan.getLevel());
                  gi.writeByte(this.markMan.getProfessionID());
                  gi.writeUTF(this.markMan.getWarCommentText());
               } else {
                  gi.writeBoolean(false);
               }

               Gang rGang = GangManager.getGang(this.revivalStatue.getOccupyGang());
               gi.writeUTF(rGang == null ? "" : rGang.getName());
               gi.writeUTF(((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getRule());
               gi.writeInt(((Luolan)this.getDungeon()).getActiveTime());
               list.clear();
               listMark.clear();
               return gi;
            } catch (Exception var12) {
               var12.printStackTrace();
               return null;
            }
         }
      }
   }

   public boolean isPkPunishment() {
      return false;
   }

   public boolean canBeEnemy() {
      return false;
   }

   public boolean canChangePkMode() {
      return false;
   }

   public boolean isEnd() {
      return this.isEnd;
   }

   public void setEnd(boolean isEnd) {
      this.isEnd = isEnd;
      if (this.isEnd) {
         UpdateMenu.allPlayerUpdate(22);
      }

   }

   public void doEnterMapSpecil(Player player) {
      super.doEnterMapSpecil(player);
      player.getBuffManager().removeBuff(80003);
      ChangePkMode.change(player, PkEnum.Mode_Force.getModeID());
      Iterator it = this.gateMap.values().iterator();

      ChangeBlocks cb;
      while(it.hasNext()) {
         LuolanGate lg = (LuolanGate)it.next();
         if (!lg.isDie()) {
            cb = ChangeBlocks.getChangeBlocks(lg.getBlockList(), false);
            player.writePacket(cb);
            cb.destroy();
            cb = null;
         }
      }

      DungeonInfoUpdate update = this.getBattleInfo(player.getGangId());
      if (update != null) {
         player.writePacket(update);
         update.destroy();
         it = null;
      }

      if (!this.isBegin) {
         int tmp = ((Luolan)this.getDungeon()).getStartTimeLeft();
         if (tmp > 0) {
            try {
               CenterMessage cm = new CenterMessage();
               cm.writeUTF(((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getPrepareNotice().replace("%s%", String.valueOf(tmp)));
               player.writePacket(cm);
               cm.destroy();
               cb = null;
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }

      if (player.isInTeam()) {
         try {
            TeamManager.doOperation(5, player);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public boolean canInTeam() {
      return false;
   }

   public Player getGearMan() {
      return this.gearMan;
   }

   public void setGearMan(Player gearMan) {
      this.gearMan = gearMan;
   }

   public Point getBornPoint(Player player) {
      HashMap map = ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getBornMap();
      return (Point)map.get(Integer.valueOf(1));
   }

   public void lostGear() {
      if (this.gearMan != null) {
         Event gEvent = this.gearMan.getMomentEvent(Status.LEAVEGEAR);
         if (gEvent != null) {
            gEvent.setEnd(true);
         }
      }

      this.gearMan = null;
      this.gear.setOccupyGang(-1L);
   }

   public void markManDie() {
      this.markMan = null;
   }

   public boolean canGangOperation() {
      return false;
   }

   public void playerDie(Player player, Creature attacker) {
      super.playerDie(player, attacker);
      if (this.gearMan != null && this.gearMan.getID() == player.getID()) {
         this.lostGear();
         this.refreshBattleInfo();
      } else if (this.markMan != null && this.markMan.getID() == player.getID()) {
         this.markManDie();
         this.refreshBattleInfo();
      }

   }

   public Player removePlayer(Player player) {
      ChangePkMode.change(player, PkEnum.Mode_Peace.getModeID());
      if (this.gearMan != null && this.gearMan.getID() == player.getID()) {
         this.lostGear();
      }

      return super.removePlayer(player);
   }

   public void broadcastCenterAndSystem(String text) {
      CenterMessage cm = new CenterMessage(text);
      ForwardMessage fm = ChatProcess.createNewChatMessage(1, text, (NewChatLink[])null, true, (byte[])null);
      Iterator it = this.playerMap.values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (!player.isDestroy()) {
            player.writePacket(cm);
            player.writePacket(fm);
         }
      }

      cm.destroy();
      fm.destroy();
   }

   public void gateBeKilled(LuolanGate gate) {
      ChangeBlocks cb = ChangeBlocks.getChangeBlocks(gate.getBlockList(), true);
      this.broadcastPacket(cb);
      cb.destroy();
      cb = null;
      int gateId = gate.getGateId();
      Iterator it = ((Luolan)this.getDungeon()).getJoinGang().entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         long key = ((Long)entry.getKey()).longValue();
         int value = ((Integer)entry.getValue()).intValue();
         if (value == gateId) {
            DungeonInfoUpdate du = this.getBattleInfo(key);
            if (du != null) {
               this.broadcastGangPacket(du, key);
               du.destroy();
               du = null;
            }

            Gang gang = GangManager.getGang(key);
            if (gang != null) {
               String text = ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getBreakGate().replace("%s%", gang.getName());
               this.broadcastCenterAndSystem(text);
            }
            break;
         }
      }

   }

   public void broadcastGangPacket(WriteOnlyPacket packet, long gangId) {
      Iterator it = this.getPlayerMap().values().iterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (player.getGangId() == gangId) {
            player.writePacket(packet);
         }
      }

   }

   public void refreshBattleInfo() {
      Iterator it = ((Luolan)this.getDungeon()).getJoinGang().keySet().iterator();

      while(it.hasNext()) {
         long gangId = ((Long)it.next()).longValue();
         DungeonInfoUpdate du = this.getBattleInfo(gangId);
         if (du != null) {
            this.broadcastGangPacket(du, gangId);
            du.destroy();
            du = null;
         }
      }

   }

   public Point getDefaultPoint(Player player) {
      int top = ((Luolan)this.getDungeon()).getTop(player.getGangId());
      long rGangId = this.revivalStatue.getOccupyGang();
      Point defaultPoint = this.getDefaultPoint();
      if (top == -1) {
         return defaultPoint;
      } else {
         Point topPoint = ((Luolan)this.getDungeon()).getBorPoint(top);
         if (rGangId > 0L && rGangId == player.getGangId()) {
            Point point = ((Luolan)this.getDungeon()).getBorPoint(4);
            return point == null ? topPoint : point;
         } else {
            return topPoint;
         }
      }
   }

   public void start() {
      this.isBegin = true;

      ChangePkView cv;
      for(Iterator it = this.gateMap.values().iterator(); it.hasNext(); cv = null) {
         LuolanGate gate = (LuolanGate)it.next();
         gate.setCanBeAttacked(true);
         cv = new ChangePkView(gate, true);
         this.broadcastPacket(cv);
         cv.destroy();
      }

   }

   protected void doWork(long now) {
      super.doWork(now);
      if (!this.isEnd() && this.gearMan != null && !this.gearMan.isDie()) {
         Event gEvent;
         if (!((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getGearArea().contains(this.gearMan.getActualPosition())) {
            gEvent = this.gearMan.getMomentEvent(Status.LEAVEGEAR);
            if (gEvent == null) {
               LeaveGearEvent event = new LeaveGearEvent(this.gearMan, new CountdownCtller(new GearManCountDown(this, ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getgTime(), ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getgText())));
               this.gearMan.addMomentEvent(event);
            }
         } else {
            gEvent = this.gearMan.getMomentEvent(Status.LEAVEGEAR);
            if (gEvent != null) {
               gEvent.setEnd(true);
            }
         }
      }

   }

   private DungeonResult getReWard(boolean isVictory, ArrayList list) {
      try {
         DungeonResult ge = new DungeonResult();
         ge.writeByte(((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getTemplateID());
         ge.writeBoolean(isVictory);
         ge.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Item item = (Item)var5.next();
            GetItemStats.writeItem(item, ge);
         }

         return ge;
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   private void balance() {
      ArrayList list = new ArrayList();
      DungeonResult geVictory = this.getReWard(true, ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getVictoryItemList());
      DungeonResult geFail = this.getReWard(false, ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getFailItemList());
      Iterator it = this.playerMap.values().iterator();

      while(true) {
         while(true) {
            Player player;
            Gang gang;
            do {
               do {
                  do {
                     if (!it.hasNext()) {
                        if (list.size() > 0) {
                           SendBatchMailTask.sendMail(list, ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getFailMailTitle(), ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getFailMailContent(), ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getFailItemList());
                           list.clear();
                        }

                        list = null;
                        if (this.victoryGang != null) {
                           ArrayList listMaster = new ArrayList();
                           listMaster.add(this.victoryGang.getMasterId());
                           SendBatchMailTask.sendMail(listMaster, ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getVictoryMailTitle(), ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getVictoryMailContent(), ((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getVictoryItemList());
                           listMaster.clear();
                        }

                        return;
                     }

                     player = (Player)it.next();
                  } while(player.isDestroy());

                  gang = player.getGang();
               } while(gang == null);
            } while(!((Luolan)this.getDungeon()).hasJoin(gang.getId()));

            if (this.victoryGang != null && this.victoryGang.getId() == gang.getId()) {
               player.writePacket(geVictory);
            } else {
               list.add(player.getID());
               player.writePacket(geFail);
            }
         }
      }
   }

   public void timeIsOver() {
      if (!this.isEnd) {
         this.setEnd(true);
         ((Luolan)this.getDungeon()).setComplete(true);
         ArrayList list = ((Luolan)this.getDungeon()).getMarkList();
         if (list.size() == 0) {
            this.noVictoryGang();
         } else if (list.size() == 1) {
            this.decidedVictoryGang(GangManager.getGang(((MarkInfo)list.get(0)).getGangId()));
         } else {
            int l1 = ((MarkInfo)list.get(0)).getTimes();
            if (l1 == 0) {
               this.noVictoryGang();
            } else {
               this.decidedVictoryGang(GangManager.getGang(((MarkInfo)list.get(0)).getGangId()));
            }
         }

         this.balance();
      }
   }

   public void battleVictory(Gang gang) {
      this.setEnd(true);
      ((Luolan)this.getDungeon()).setComplete(true);
      this.decidedVictoryGang(gang);
      this.balance();
      this.readyQuit();
   }

   public void noVictoryGang() {
      BroadcastManager.broadcastSystemAndZmd(((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getNoVictory());
   }

   private void readyQuit() {
      ((Luolan)this.getDungeon()).setUniqueFuture(ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
         public void run() {
            ((Luolan)LuolanMap.this.getDungeon()).destroy();
         }
      }, 120000L));
   }

   private void decidedVictoryGang(Gang gang) {
      this.victoryGang = gang;
      this.lostGear();
      this.refreshBattleInfo();
      GangManager.warVictory(this.victoryGang.getId());
      this.victoryGang.warVictory();
      BroadcastManager.broadcastSystemAndZmd(((LuolanTemplate)((Luolan)this.getDungeon()).getTemplate()).getHasVictory().replace("%s%", gang.getName()));
   }

   public Gear getGear() {
      return this.gear;
   }

   public boolean isBegin() {
      return this.isBegin;
   }

   public void setBegin(boolean isBegin) {
      this.isBegin = isBegin;
   }

   public Player getMarkMan() {
      return this.markMan;
   }

   public void setMarkMan(Player markMan) {
      this.markMan = markMan;
   }

   public int getViewFontColor(Player player, Player viewer) {
      return player.getGangId() != -1L && viewer.getGangId() == player.getGangId() ? EvilEnum.Evil_White.getFont() : EvilEnum.Evil_Red.getFont();
   }
}
