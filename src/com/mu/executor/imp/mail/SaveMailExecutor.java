package com.mu.executor.imp.mail;

import com.mu.config.Global;
import com.mu.db.manager.GameDBManager;
import com.mu.db.manager.MailDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.item.Item;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.mail.MailItem;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;
import java.util.Iterator;

public class SaveMailExecutor extends Executable {
   public SaveMailExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      MailDBManager.insertMail(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Mail mail = (Mail)obj[0];
      packet.writeLong(mail.getID());
      packet.writeLong(mail.getRoleId());
      packet.writeUTF(mail.getTitle());
      packet.writeUTF(mail.getContent());
      packet.writeLong(mail.getTime());
      packet.writeBoolean(mail.isRead());
      packet.writeLong(mail.getExpiredTime());
      ArrayList list = mail.getItemList();
      packet.writeByte(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         MailItem mi = (MailItem)var6.next();
         Item item = mi.getItem();
         packet.writeInt(item.getModelID());
         packet.writeByte(item.getQuality());
         packet.writeInt(item.getCount());
         packet.writeShort(item.getSlot());
         packet.writeByte(item.getContainerType());
         packet.writeByte(item.getStarLevel());
         packet.writeByte(item.getSocket());
         packet.writeBoolean(item.isBind());
         packet.writeInt(item.getMoney());
         packet.writeByte(item.getMoneyType());
         packet.writeInt(item.getStarUpTimes());
         packet.writeByte(item.getOnceMaxStarLevel());
         packet.writeLong(item.getExpireTime());
         packet.writeShort(item.getDurability());
         packet.writeUTF(item.getBasisStr());
         packet.writeUTF(item.getOtherStr());
         packet.writeUTF(item.getStoneStr());
         packet.writeUTF(item.getRuneStr());
         packet.writeByte(item.getZhuijiaLevel());
      }

      final long mailId = mail.getID();
      if (Global.isInterServiceServer()) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               GameDBManager.insertInterMaxId(2, mailId);
            }
         });
      }

   }
}
