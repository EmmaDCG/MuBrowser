package com.mu.game.model.unit.buff.model.click.imp;

import com.mu.game.model.unit.buff.Buff;
import com.mu.game.model.unit.buff.model.click.ClickPopup;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.OpenUrl;

public class OpenWebpageClickPopup extends ClickPopup {
   private String url;

   public OpenWebpageClickPopup(int id, int type, String url) {
      super(id, type);
      this.url = url;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void doClick(Player player, Buff buff) {
      OpenUrl.open(player, this.url);
   }
}
