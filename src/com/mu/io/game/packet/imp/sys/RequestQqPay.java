package com.mu.io.game.packet.imp.sys;

import com.mu.config.Global;
import com.mu.game.model.unit.player.Player;
import com.mu.game.qq.pay.QqPayElement;
import com.mu.game.qq.pay.Qqpay;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.http.servlet.plat.qq.api.APIManager;
import com.mu.utils.concurrent.ThreadCachedPoolManager;
import java.util.ArrayList;

public class RequestQqPay extends ReadAndWritePacket {
   public RequestQqPay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      final Player player = this.getPlayer();
      final int id = this.readByte();
      ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
         public void run() {
            QqPayElement pe = Qqpay.getPayElement(id);
            if (pe != null) {
               ArrayList list = new ArrayList();
               String urlPara = APIManager.getBuyGoods(player, pe);
               if (urlPara != null) {
                  list.add(Qqpay.getFunctionName());
                  list.add(Global.appid);
                  list.add(urlPara);
                  list.add("");
                  list.add(Qqpay.getTitle());
                  PushScript.push(player, list);
                  list.clear();
               }
            }

         }
      });
   }
}
