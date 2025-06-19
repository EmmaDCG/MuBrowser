package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class OpenPersonalBoss extends ReadAndWritePacket {
   public OpenPersonalBoss(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      PlayerTaskManager manager = player.getTaskManager();
      Task task = manager.getCurZJTask();
      if (task != null && task.getId() == ArrowGuideManager.getArrowInfo(20).getTaskId() && !task.isComplete() && !player.getArrowGuideManager().isFinished(20)) {
         ArrowGuideManager.pushArrow(player, 20, (String)null);
      }

   }
}
