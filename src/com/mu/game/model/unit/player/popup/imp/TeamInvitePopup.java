package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.CenterManager;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.TeamManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class TeamInvitePopup extends Popup {
   private Team team = null;
   private Player invitor = null;

   public TeamInvitePopup(int id, Team team, Player invitor) {
      super(id);
      this.team = team;
      this.invitor = invitor;
   }

   public String getTitle() {
      return MessageText.getText(19013);
   }

   public String getContent() {
      return MessageText.getText(19012).replace("%n%", this.invitor.getName());
   }

   public String getLeftButtonContent() {
      return MessageText.getText(19010);
   }

   public String getRightButtonContent() {
      return MessageText.getText(19011);
   }

   public void dealLeftClick(Player player) {
      if (!TeamManager.hasTeam(this.team.getId())) {
         SystemMessage.writeMessage(player, 19009);
      } else {
         this.team.agreeInvite(player);
      }

   }

   public void dealRightClick(Player player) {
      if (CenterManager.isOnline(this.invitor.getID())) {
         SystemMessage.writeMessage(this.invitor, MessageText.getText(19014).replace("%n%", player.getName()), 19014);
      }

   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 5;
   }

   public void destroy() {
      super.destroy();
      this.team = null;
      this.invitor = null;
   }
}
