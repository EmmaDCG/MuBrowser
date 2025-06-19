package com.mu.io.game.packet.imp.activity;

import com.mu.config.Global;
import com.mu.game.CenterManager;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.PushScript;
import com.mu.io.http.servlet.plat.qq.api.APIManager;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;

public class RequestBlueRenew extends ReadAndWritePacket {
   public RequestBlueRenew(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player p = this.getPlayer();
      final long id = p.getID();
      ActivityBlueRenew renew = ActivityManager.getBlueRenew();
      if (renew != null && renew.isOpen() && renew.getElement().getReceiveStatus(p) != 2) {
         ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
            public void run() {
               Player player = CenterManager.getPlayerByRoleID(id);
               if (player != null && !player.isDestroy()) {
                  String token = APIManager.getPayToken(player, 1);
                  if (token != null) {
                     ArrayList list = new ArrayList();
                     list.add("GameAPI.NewGameVIPAction.show");
                     list.add(Global.appid);
                     list.add(APIManager.blueVipActivityID);
                     list.add(token);
                     list.add(String.valueOf(player.getUser().getServerID()));
                     list.add(player.getUser().getOpenId());
                     list.add("v3");
                     list.add("close_cb");
                     PushScript.push(player, list);
                     list.clear();
                  }
               }

            }
         });
      }
   }
}
