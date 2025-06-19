package com.mu.io.game.packet.imp.sys;

import com.mu.config.Global;
import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.imp.tx.bluevip.ActivityBlueVip;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;

public class RequestBlueVipKt extends ReadAndWritePacket {
   private static ArrayList blueList = null;

   static {
      blueList = new ArrayList();
      ActivityBlueVip bv = (ActivityBlueVip)ActivityManager.getActivity(10);
      blueList.add(bv.getFuntionName());
      blueList.add(Global.appid);
      blueList.add(bv.getCallBackName());
      blueList.add(Global.blueOpenAid);
   }

   public RequestBlueVipKt(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      PushScript.push(this.getPlayer(), blueList);
   }
}
