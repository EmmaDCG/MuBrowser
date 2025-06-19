package com.mu.game.model.drop.model.control;

import com.mu.game.model.drop.model.DropControl;
import com.mu.game.model.unit.player.Player;

public class FirstKillControl extends DropControl {
   private int templateID;
   private int killNumber;

   public FirstKillControl(int controlID, int type, int targetID, int killNumber) {
      super(controlID, type);
      this.templateID = targetID;
      this.killNumber = killNumber;
   }

   public boolean checkDrop(Player player, int templateID) {
      if (templateID != this.getTemplateID()) {
         return false;
      } else {
         int killNumbers = player.getDropManager().getKillNumber(templateID);
         return killNumbers == this.killNumber;
      }
   }

   public int getCanDropCount(Player player, int itemModelID) {
      return 2147483646;
   }

   public static FirstKillControl createFirstKillControl(int controlID, int type, int number, String value) throws Exception {
      int templateID = Integer.parseInt(value);
      if (number < 1) {
         throw new Exception("掉落控制模板ID = " + controlID + ",数据出错");
      } else {
         FirstKillControl kc = new FirstKillControl(controlID, type, templateID, number);
         return kc;
      }
   }

   public int getTemplateID() {
      return this.templateID;
   }

   public void setTemplateID(int templateID) {
      this.templateID = templateID;
   }
}
