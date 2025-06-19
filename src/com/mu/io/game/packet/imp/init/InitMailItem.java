package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.MailDBManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemSaveAide;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.mail.Mail;
import com.mu.game.model.mail.MailItem;
import com.mu.game.model.mail.MailItemData;
import com.mu.game.model.mail.MailManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitMailItem extends ReadAndWritePacket {
   public InitMailItem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitMailItem createInitMailItem(long rid) {
      InitMailItem mi = new InitMailItem(10901, (byte[])null);
      ArrayList list = MailDBManager.getMailItemList(rid);

      try {
         mi.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            MailItemData data = (MailItemData)var5.next();
            mi.writeLong(data.getMailId());
            mi.writeByte(data.getIndex());
            InitItems.writeItemSaveAide(mi, data);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return mi;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      MailManager manager = player.getMailManager();
      if (manager != null) {
         int size = this.readUnsignedShort();

         for(int i = 0; i < size; ++i) {
            long mailId = this.readLong();
            int index = this.readByte();
            ItemSaveAide isa = ItemSaveAide.createSaveAide((ReadAndWritePacket)this);
            Item item = ItemTools.loadItem(isa);
            Mail mail = manager.getMail(mailId);
            if (mail != null) {
               MailItem mi = new MailItem(index);
               mi.setItem(item);
               mail.addItem(mi);
            }
         }

      }
   }
}
