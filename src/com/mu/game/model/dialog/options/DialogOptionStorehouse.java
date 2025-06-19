package com.mu.game.model.dialog.options;

import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.storage.DeportOpen;

public class DialogOptionStorehouse extends DialogOption {
   public DialogOptionSee canSee(Player p, Npc npc) {
      return DialogOptionSee.VISIBLE;
   }

   public void option(Player player, Npc npc) {
      DeportOpen.openDeport(player);
   }
}
