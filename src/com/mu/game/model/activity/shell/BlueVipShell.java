package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.tx.bluevip.ActivityBlueVip;
import java.io.InputStream;

public class BlueVipShell extends ActivityShell {
   public int getMenuId() {
      return 26;
   }

   public int getShellId() {
      return 8;
   }

   public void init(InputStream in) throws Exception {
      ActivityBlueVip ab = new ActivityBlueVip();
      ActivityManager.addActivity(ab);
      ab.init(in);
   }
}
