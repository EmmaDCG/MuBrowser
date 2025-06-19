package com.mu.game.model.dialog.options;

import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;

public class DialogOptionNone extends DialogOption {
   public DialogOptionSee canSee(Player p, Npc npc) {
      return DialogOptionSee.VISIBLE;
   }

   public void option(Player p, Npc npc) {
   }
}
