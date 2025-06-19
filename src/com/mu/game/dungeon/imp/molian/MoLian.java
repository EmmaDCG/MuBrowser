package com.mu.game.dungeon.imp.molian;

import com.mu.config.MessageText;
import com.mu.game.dungeon.Dungeon;
import com.mu.game.dungeon.DungeonPlayerInfo;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.gang.GangMember;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.imp.DungeonQuitPopup;
import com.mu.game.task.schedule.SaveGangContributionTask;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import java.awt.Point;

public class MoLian extends Dungeon {
   private MoLianleLevel molianLevel = null;
   private int playerTimeLimit = 1800;

   public MoLian(int id, MoLianTemplate t, MoLianleLevel level) {
      super(id, t);
      this.molianLevel = level;
      this.timeLimit = 1073741823;
      this.timeLeft = this.timeLimit;
      this.playerTimeLimit = t.getTimeLimit();
   }

   public boolean canDestoryWhenEnter() {
      if (!this.isPlayerInDungeon()) {
         return false;
      } else {
         if (this.getPlayerMap().size() == 0) {
            if (this.lastDorpTime <= 0L) {
               return true;
            }

            if (System.currentTimeMillis() - this.lastDorpTime >= 300000L) {
               return true;
            }
         }

         return false;
      }
   }

   public void initMap() {
      MoLianMap map = new MoLianMap(this.molianLevel.getMapId(), this);
      this.addMap(map);
      map.setDefaultPoint(new Point(this.molianLevel.getDefaultX(), this.molianLevel.getDefaultY()));
      map.initMonster();
   }

   public int getPlayerTimeLimit() {
      return this.playerTimeLimit;
   }

   public MoLianleLevel getMolianLevel() {
      return this.molianLevel;
   }

   public int getMoneyInspireFullMsg() {
      return 14053;
   }

   public int getPlayerTimeLeft(Player player) {
      DungeonPlayerInfo info = this.getDungeonPlayerInfo(player.getID());
      return this.playerTimeLimit - info.getCostTime();
   }

   public int checkMoneyInspire(Player player) {
      GangMember member = GangManager.getMember(player.getID());
      if (member != null && member.getCurContribution() >= ((MoLianTemplate)this.getTemplate()).getInspireMoney()) {
         member.reduceContribution(((MoLianTemplate)this.getTemplate()).getInspireMoney());
         SaveGangContributionTask.addMember(member.getId(), member.getCurContribution(), member.getHisContribution());
         return 1;
      } else {
         return 14052;
      }
   }

   public synchronized void destroy() {
      ((MoLianTemplate)this.getTemplate()).getManager().removeMolian(this.getID());
      super.destroy();
   }

   public MoLianMap getMoLianMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (MoLianMap)map;
   }

   public DungeonResult createSuccessPacket() {
      return null;
   }

   public void exitForInitiative(Player player, boolean force) {
      try {
         if (!force && this.getPlayerTimeLeft(player) > 0) {
            DungeonQuitPopup pop = new DungeonQuitPopup(player.createPopupID());
            pop.setContent(MessageText.getText(4031));
            ShowPopup.open(player, pop);
            return;
         }

         super.exitForInitiative(player, force);
         this.removePlayerInfo(player.getID());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void checkTime() {
      super.checkTime();
   }
}
