package com.mu.io.game.packet.imp.chat;

import com.mu.game.model.chat.ChatType;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Collection;

public class ForwardMessage extends WriteOnlyPacket {
   public ForwardMessage() {
      super(10502);
   }

   public static void forward(ChatType type, Player from, Player target, byte[] data) {
   }

   public static void forward(ChatType type, Player player, Collection targetCollection, byte[] data) {
   }
}
