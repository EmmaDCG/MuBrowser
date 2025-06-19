package com.mu.game.model.dialog.options;

import com.mu.game.model.dialog.DialogOption;
import com.mu.game.model.dialog.DialogOptionSee;
import com.mu.game.model.unit.npc.Npc;
import com.mu.game.model.unit.player.Player;

public class DialogOptionDungeon extends DialogOption {
   public DialogOptionSee canSee(Player player, Npc npc) {
      return DialogOptionSee.VISIBLE;
   }

   public void option(Player player, Npc npc) {
   }
}
