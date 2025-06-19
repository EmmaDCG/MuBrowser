package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.utils.geom.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionEvent extends Event {
   private Player target = null;
   private static Logger logger = LoggerFactory.getLogger(TransactionEvent.class);

   public TransactionEvent(Player owner, Player target) {
      super(owner);
      this.target = target;
      this.checkrate = 1000;
   }

   public Status getStatus() {
      return Status.TRANSACTION;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.TRANSACTION;
   }

   public void destroy() {
      if (!this.isDestroyed()) {
         logger.debug("player {} tarnsaction closed", ((Player)this.getOwner()).getName());
         this.target = null;
         super.destroy();
      }
   }

   public void work(long now) throws Exception {
      if (!((Player)this.getOwner()).getMap().equals(this.target.getMap()) || MathUtil.getDistance(((Player)this.getOwner()).getPosition(), this.target.getPosition()) > 10000) {
         TransactionManager.closeTransaction(((Player)this.getOwner()).getID());
      }

   }
}
