package com.mu.game.model.unit.action;

import com.mu.game.model.unit.player.Player;
import com.mu.utils.concurrent.ThreadFixedPoolManager;

public class DelayAction {
   private long delayTime;
   private Action action;
   private boolean destroyAfterExcute;

   public DelayAction(Action action) {
      this.delayTime = 0L;
      this.action = null;
      this.destroyAfterExcute = false;
      this.action = action;
   }

   public DelayAction(Action action, long delayTime) {
      this(action);
      this.delayTime = delayTime;
   }

   public DelayAction(Action action, boolean destroyAfterExcute) {
      this(action);
      this.destroyAfterExcute = destroyAfterExcute;
   }

   public void setDestroyAfterExcute(boolean destroyAfterExcute) {
      this.destroyAfterExcute = destroyAfterExcute;
   }

   public void setDelayTime(long delayTime) {
      this.delayTime = delayTime;
   }

   private void excuteAction(Player player) {
      if (this.action != null) {
         this.action.doAction(player);
         if (this.destroyAfterExcute) {
            this.destroy();
         }
      }

   }

   public void doAction(final Player player) {
      if (this.delayTime > 0L) {
         ThreadFixedPoolManager.POOL_OTHER.schedule(new Runnable() {
            public void run() {
               DelayAction.this.excuteAction(player);
            }
         }, this.delayTime);
      } else {
         this.excuteAction(player);
      }

   }

   public void destroy() {
      if (this.action != null) {
         this.action.destroy();
         this.action = null;
      }

   }
}
