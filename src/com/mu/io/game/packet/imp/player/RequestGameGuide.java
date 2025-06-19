package com.mu.io.game.packet.imp.player;

import com.mu.game.model.gameguide.GameGuide;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class RequestGameGuide extends ReadAndWritePacket {
   public RequestGameGuide(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      ArrayList list = GameGuide.getGuideList();
      this.writeByte(list.size());
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         GameGuide guide = (GameGuide)var3.next();
         ArrayList children = guide.getChildren();
         this.writeUTF(guide.getName());
         this.writeShort(guide.getBackground());
         this.writeByte(children.size());
         Iterator var6 = children.iterator();

         while(var6.hasNext()) {
            String[] str = (String[])var6.next();
            this.writeUTF(str[0]);
            this.writeUTF(str[1]);
         }
      }

      this.getPlayer().writePacket(this);
   }
}
