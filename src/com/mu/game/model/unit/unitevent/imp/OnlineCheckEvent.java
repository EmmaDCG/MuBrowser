package com.mu.game.model.unit.unitevent.imp;

import com.mu.config.Global;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import com.mu.game.model.item.container.imp.Backpack;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.game.model.unit.player.fcm.FcmManager;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.mail.ServerDeleteMail;
import com.mu.io.game.packet.imp.player.FrameCheck;
import com.mu.io.game.packet.imp.storage.BackpackCount;
import java.util.ArrayList;
import java.util.Iterator;

public class OnlineCheckEvent extends Event {
   private static final int OnlineCheckRate = 1000;
   private static final int OnlineTimeRate = 5;
   private static final int CheckMailRate = 600;
   private static final int CheckTitle = 60;
   private static final int UserOnlineSecond = 1;
   private int checkNumber = 0;
   private int backpackRate = 5;
   private int blueVipValitCheck = 60;

   public OnlineCheckEvent(Player owner) {
      super(owner);
      this.checkrate = 1000;
   }

   public void work(long now) throws Exception {
      ++this.checkNumber;
      ((Player)this.getOwner()).getUser().addOnlineTime(1);
      if (Global.isFcm() && ((Player)this.getOwner()).getUser().isNeedAntiAddiction()) {
         this.doAntiAddiction(now);
      }

      if (this.checkNumber % 5 == 0) {
         ((Player)this.getOwner()).addTodayOnlineTime(5);
         ((Player)this.getOwner()).addTotalOnlineTime(5);
         ((Player)this.getOwner()).addThisOnlineTime(5);
         ((Player)this.getOwner()).getOnlineManager().onEventAddOnlineTime();
      }

      if (this.checkNumber % 600 == 0) {
         this.checkMail(now);
      }

      if (this.checkNumber % 60 == 0) {
         this.checkTitle(now);
      }

      if (this.checkNumber % this.backpackRate == 0) {
         Backpack backpack = ((Player)this.getOwner()).getBackpack();
         boolean send = backpack.checkTime(System.currentTimeMillis());
         if (send) {
            BackpackCount.sendToClient((Player)this.getOwner());
         }
      }

      if (this.checkNumber % this.blueVipValitCheck == 0) {
         this.checkBlueValid();
      }

      FrameCheck.writeHeartbeet((Player)this.getOwner());
   }

   private void doAntiAddiction(long now) {
      FcmManager.doPush((Player)this.getOwner(), now);
   }

   private void checkMail(long now) {
      ArrayList idList = new ArrayList();
      Iterator it = ((Player)this.getOwner()).getMailManager().getAllMails().values().iterator();

      while(it.hasNext()) {
         Mail mail = (Mail)it.next();
         if (now >= mail.getExpiredTime()) {
            idList.add(mail.getID());
         }
      }

      if (idList.size() > 0) {
         ServerDeleteMail.deleteMail((Player)this.getOwner(), idList);
         idList.clear();
         idList.clear();
      }

   }

   private void checkTitle(long now) {
      ((Player)this.getOwner()).getTitleManager().checkTime(now);
   }

   private void checkBlueValid() {
      BlueVip bv = ((Player)this.getOwner()).getUser().getBlueVip();
      if (bv != null && bv.isNeedCheckValid()) {
         long time = bv.getValidTime();
         ActivityBlueRenew renew = ActivityManager.getBlueRenew();
         if (Math.abs(time) < 259200L) {
            if (renew != null && renew.isOpen() && time > 0L && !renew.hasPlayer(((Player)this.getOwner()).getID()) && renew.getElement().getReceiveStatus((Player)this.getOwner()) != 2) {
               renew.addRole(((Player)this.getOwner()).getID());
               renew.refreshIcon((Player)this.getOwner());
            }
         } else if (time > 0L && renew.hasPlayer(((Player)this.getOwner()).getID())) {
            renew.removeRole(((Player)this.getOwner()).getID());
            renew.refreshIcon((Player)this.getOwner());
            bv.initCheckValid();
         }

      }
   }

   public Status getStatus() {
      return Status.ONLINECOUNT;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
