package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.controller.CountdownCtller;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.player.StartCountDown;

public class CountDownEvent extends Event {
   private CountdownCtller cdCtller;

   public CountDownEvent(Player owner, CountdownCtller cc) {
      super(owner);
      this.cdCtller = cc;
   }

   public CountdownCtller getCountdownCtller() {
      return this.cdCtller;
   }

   public Status getStatus() {
      return Status.CountDown;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.COUNTDOWN;
   }

   public synchronized void destroy() {
      if (!this.destroyed) {
         this.cdCtller.stopCountDown((Player)this.getOwner());
         this.cdCtller.destroy();
         this.cdCtller = null;
         super.destroy();
      }
   }

   public void work(long now) throws Exception {
      if (!this.cdCtller.isStart()) {
         this.cdCtller.reset(System.currentTimeMillis());
         this.cdCtller.setStart(true);
         StartCountDown.start((Player)this.getOwner(), this.cdCtller.getCountdownObject());
      } else if (this.cdCtller.countdownEnd(System.currentTimeMillis())) {
         this.cdCtller.endCountDown((Player)this.getOwner());
         this.setEnd(true);
         if (this.cdCtller != null && this.cdCtller.getCountdownObject().occupateStatus()) {
            ((Player)this.getOwner()).idle();
         }
      }

   }
}
