package com.mu.game.dungeon.imp.luolan;

import com.mu.game.dungeon.DungeonTemplateFactory;
import java.util.TimerTask;

public class OpenTask extends TimerTask {
   public void run() {
      LuolanTemplate template = (LuolanTemplate)DungeonTemplateFactory.getTemplate(9);
      LuolanManager manager = template.getManager();
      manager.createLuolan(false);
   }
}
