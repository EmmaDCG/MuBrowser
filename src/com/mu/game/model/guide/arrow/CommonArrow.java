package com.mu.game.model.guide.arrow;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.guide.PushRoleArrow;

public class CommonArrow extends ArrowGuide {
   protected int pushId;

   public CommonArrow(int arrowId) {
      super(arrowId);
      this.pushId = arrowId;
   }

   public void pushArrow(Player player, String content) {
      ArrowInfo info = ArrowGuideManager.getArrowInfo(this.getArrowId());
      if (info != null) {
         ArrowGuideManager manager = player.getArrowGuideManager();
         if (info.isInDb()) {
            if (info.isSaveMore()) {
               if (info.getTimes() < manager.getGuideTimes(this.arrowId)) {
                  return;
               }
            } else if (info.getTimes() <= manager.getGuideTimes(this.arrowId)) {
               return;
            }
         }

         String tmp = info.getContent();
         if (content != null && !content.trim().equals("")) {
            tmp = content;
         }

         if (!info.isSaveMore()) {
            PushRoleArrow.pushArrow(player, this.pushId, tmp);
         } else if (info.getTimes() > manager.getGuideTimes(this.arrowId)) {
            PushRoleArrow.pushArrow(player, this.pushId, tmp);
         }

         if (info.isInDb()) {
            manager.addGuide(this.arrowId);
         }

      }
   }
}
