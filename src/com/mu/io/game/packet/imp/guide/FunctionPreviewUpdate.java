package com.mu.io.game.packet.imp.guide;

import com.mu.game.model.fp.FunctionPreview;
import com.mu.game.model.fp.FunctionPreviewManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class FunctionPreviewUpdate extends WriteOnlyPacket {
   public FunctionPreviewUpdate() {
      super(10029);
   }

   public static void pushPreview(Player player, boolean noticeDisappear) {
      FunctionPreview curPreview = FunctionPreviewManager.getCurrentPreview(player);
      if (curPreview != null) {
         try {
            int tmpevel = curPreview.getReceiveLevel();
            FunctionPreviewUpdate fu = new FunctionPreviewUpdate();
            fu.writeBoolean(true);
            fu.writeShort(curPreview.getHeader(player));
            fu.writeUTF(curPreview.getName());
            fu.writeShort(tmpevel);
            fu.writeUTF(curPreview.getDes1());
            fu.writeUTF(curPreview.getDes2());
            fu.writeBoolean(tmpevel <= player.getLevel());
            player.writePacket(fu);
            fu.destroy();
            fu = null;
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      } else if (noticeDisappear) {
         try {
            FunctionPreviewUpdate fu = new FunctionPreviewUpdate();
            fu.writeBoolean(false);
            player.writePacket(fu);
            fu.destroy();
            fu = null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }
}
