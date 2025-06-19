package com.mu.game.model.fo;

import com.mu.executor.imp.player.SaveFunctionOpenExecutor;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;
import com.mu.io.game.packet.imp.guide.FunctionOpenPacket;
import com.mu.io.game.packet.imp.pet.PetOpen;

public class FunctionOpen {
   private int id;
   private String name;
   private String notOpenDes;
   private int icon;
   private int type;
   private int value;

   public FunctionOpen(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNotOpenDes() {
      return this.notOpenDes;
   }

   public void setNotOpenDes(String notOpenDes) {
      this.notOpenDes = notOpenDes;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public int getId() {
      return this.id;
   }

   public boolean isOpen(Player player) {
      switch(this.type) {
      case 1:
         if (player.getLevel() >= this.value) {
            return true;
         }

         return false;
      case 2:
         return player.getTaskManager().isTaskOver(this.value);
      case 3:
      default:
         return false;
      }
   }

   public void doOpen(Player player) {
      switch(this.id) {
      case 2:
         UpdateMenu.updateDungeonMenu(player, 1);
         break;
      case 3:
         UpdateMenu.updateDungeonMenu(player, 2);
         break;
      case 4:
         if (player.getTaskManager().getCurRCTask() == null) {
            player.getTaskManager().randomRCTask();
         }
      case 5:
      case 6:
      case 7:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 17:
      case 18:
      default:
         break;
      case 8:
         UpdateMenu.update(player, 9);
         break;
      case 9:
         UpdateMenu.updateDungeonMenu(player, 4);
         break;
      case 10:
         UpdateMenu.updateDungeonMenu(player, 3);
         break;
      case 16:
         PlayerPetManager ppm = player.getPetManager();
         if (!ppm.isOpen()) {
            ppm.openByServer(true);
            PetOpen.sendPetOpen(player);
         }
         break;
      case 19:
         player.getShieldManager().addStat();
         break;
      case 20:
         UpdateMenu.updateDungeonMenu(player, 6);
         break;
      case 21:
         UpdateMenu.update(player, 30);
         break;
      case 22:
         player.getSevenManager().openFunction();
         break;
      case 23:
         player.getSpiritManager().openFunction();
         break;
      case 24:
         player.getHallowsManager().openFunction();
      }

      FunctionOpenPacket.open(this, player);
      SaveFunctionOpenExecutor.saveFunctionOpen(player, this.id, System.currentTimeMillis());
   }
}
