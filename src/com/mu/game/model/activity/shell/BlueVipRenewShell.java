package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.tx.bluerenew.ActivityBlueRenew;
import java.io.InputStream;

public class BlueVipRenewShell extends ActivityShell {
   public int getMenuId() {
      return 28;
   }

   public int getShellId() {
      return 10;
   }

   public void init(InputStream in) throws Exception {
      ActivityBlueRenew ab = new ActivityBlueRenew();
      ActivityManager.addActivity(ab);
      ab.init(in);
   }
}
