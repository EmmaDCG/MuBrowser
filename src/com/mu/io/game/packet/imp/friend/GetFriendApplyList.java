package com.mu.io.game.packet.imp.friend;

import com.mu.game.model.friend.Friend;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class GetFriendApplyList extends ReadAndWritePacket {
   public GetFriendApplyList(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ConcurrentHashMap map = player.getFriendManager().getApplyMap();
      this.writeByte(map.size());
      Iterator it = map.values().iterator();

      while(it.hasNext()) {
         Friend friend = (Friend)it.next();
         this.writeDouble((double)friend.getId());
         this.writeUTF(friend.getName());
         this.writeDouble((double)friend.getAddTime());
      }

      player.writePacket(this);
   }
}
