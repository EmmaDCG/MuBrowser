package com.mu.game.model.drop;

import com.mu.game.model.drop.model.DropModel;
import com.mu.game.model.drop.model.KillRecord;
import com.mu.game.model.packet.RolePacketService;
import com.mu.game.model.unit.player.Player;
import java.util.HashMap;

public class PlayerDropManager {
   private Player owner;
   private HashMap personDayDropMap = new HashMap();
   private HashMap killRecordMap = new HashMap();
   private boolean change = false;

   public PlayerDropManager(Player owner) {
      this.owner = owner;
   }

   public void loadKillRecord(KillRecord killRecord) {
      this.killRecordMap.put(killRecord.getTemplateID(), killRecord);
   }

   public void loadPersonDrop(int dropID, int count) {
      this.personDayDropMap.put(dropID, count);
   }

   public void addWorldCount(DropModel model, int count) {
      int value = 0;
      int dropID = model.getDropID();
      if (model.getPersonMaxCountPerDay() != -1) {
         if (this.personDayDropMap.containsKey(dropID)) {
            value = ((Integer)this.personDayDropMap.get(dropID)).intValue();
         }

         value += count;
         this.personDayDropMap.put(dropID, value);
         this.change = true;
      }

   }

   public int getPersonDayCount(int dropID) {
      return this.personDayDropMap.containsKey(dropID) ? ((Integer)this.personDayDropMap.get(dropID)).intValue() : 0;
   }

   public HashMap getPersonDayDropMap() {
      return this.personDayDropMap;
   }

   public boolean hasKill(int templateID) {
      return this.killRecordMap.containsKey(templateID);
   }

   public int getKillNumber(int templateID) {
      return this.hasKill(templateID) ? ((KillRecord)this.killRecordMap.get(templateID)).getNumber() : 0;
   }

   public void addKillNumber(int templateID) {
      KillRecord record = (KillRecord)this.killRecordMap.get(templateID);
      boolean flag = true;
      if (record == null) {
         record = new KillRecord(templateID, 1);
      } else {
         flag = false;
         record.addNumber();
         record.setChange(true);
      }

      this.killRecordMap.put(record.getTemplateID(), record);
      if (flag) {
         RolePacketService.noticeGatewaySaveFirstKill(this.getOwner(), templateID);
      }

   }

   public Player getOwner() {
      return this.owner;
   }

   public boolean isChange() {
      return this.change;
   }

   public void destroy() {
      this.owner = null;
      if (this.killRecordMap != null) {
         this.killRecordMap.clear();
         this.killRecordMap = null;
      }

      if (this.personDayDropMap != null) {
         this.personDayDropMap.clear();
         this.personDayDropMap = null;
      }

   }

   public HashMap getKillRecordMap() {
      return this.killRecordMap;
   }
}
