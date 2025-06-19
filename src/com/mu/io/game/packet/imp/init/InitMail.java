package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.MailDBManager;
import com.mu.executor.Executor;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitMail extends ReadAndWritePacket {
   public InitMail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitMail createInitMail(long rid) {
      InitMail im = new InitMail(10900, (byte[])null);
      ArrayList list = MailDBManager.getMailList(rid);

      try {
         im.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            Mail mail = (Mail)var5.next();
            im.writeLong(mail.getID());
            im.writeLong(mail.getRoleId());
            im.writeUTF(mail.getTitle());
            im.writeUTF(mail.getContent());
            im.writeLong(mail.getTime());
            im.writeBoolean(mail.isRead());
            im.writeLong(mail.getExpiredTime());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return im;
   }

   public void process() throws Exception {
      ArrayList expiredList = new ArrayList();
      long now = System.currentTimeMillis();
      Player player = this.getPlayer();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         Mail mail = new Mail(this.readLong());
         mail.setRoleId(this.readLong());
         mail.setTitle(this.readUTF());
         mail.setContent(this.readUTF());
         mail.setTime(this.readLong());
         mail.setRead(this.readBoolean());
         mail.setExpiredTime(this.readLong());
         if (mail.getExpiredTime() <= now - 60000L) {
            expiredList.add(mail.getID());
         } else {
            player.getMailManager().addMail(mail);
         }
      }

      if (expiredList.size() > 0) {
         WriteOnlyPacket packet = Executor.DeleteMail.toPacket(expiredList);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      }

      expiredList.clear();
      expiredList = null;
      player.getMailManager().initUnreadSize();
   }
}
