package com.mu.game.dungeon.imp.trial;

import com.mu.game.dungeon.Dungeon;
import com.mu.game.model.map.Map;
import com.mu.game.model.trial.TrialConfigs;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.dungeon.DungeonResult;

public class Trial extends Dungeon {
   private int trialLevel = 0;

   public Trial(int id, TrialTemplate t, int trialLevel) {
      super(id, t);
      this.trialLevel = trialLevel;
      this.saveWhenInterrupt = true;
   }

   public void exitForInitiative(Player player, boolean force) {
      super.exitForInitiative(player, true);
      if (!this.isComplete()) {
         this.setComplete(true);
         player.getDunLogsManager().finishDungeon(((TrialTemplate)this.getTemplate()).getTemplateID(), 0);
         UpdateMenu.updateDungeonMenu(player, ((TrialTemplate)this.getTemplate()).getTemplateID());
      }

   }

   public void initMap() {
      TrialMap map = new TrialMap(this);
      this.addMap(map);
   }

   public TrialMap getTrialMap() {
      Map map = this.getFirstMap();
      return map == null ? null : (TrialMap)map;
   }

   public void checkTime() {
      super.checkTime();
      TrialMap map = this.getTrialMap();
      if (map != null) {
         if (this.getTimeCost() >= this.getTimeLimit() && !this.isComplete()) {
            map.doFaild();
         }

         if (!this.isComplete()) {
            map.pushSchedule();
         }

      }
   }

   public int getTrialLevel() {
      return this.trialLevel;
   }

   public DungeonResult createSuccessPacket() {
      try {
         DungeonResult ds = new DungeonResult();
         ds.writeByte(3);
         ds.writeBoolean(true);
         TrialConfigs tc = TrialConfigs.getConfig(this.getTrialLevel());
         ds.writeShort(tc.getLevel());
         ds.writeShort(tc.getIcon());
         ds.writeUTF(tc.getSuccessDes());
         return ds;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
