package com.mu.game.task.schedule;

import com.mu.db.log.atom.PayConfirmLog;
import com.mu.db.manager.PayDBManager;
import com.mu.io.http.servlet.plat.qq.api.APIManager;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadFixedPoolManager;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class PayConfirmTask extends ScheduleTask {
   private static final int expiredInteval = 1000;
   private static ConcurrentHashMap confirmMap = Tools.newConcurrentHashMap2();

   public void startTask() {
      this.task = ThreadFixedPoolManager.POOL_OTHER.scheduleTask(this, 1000L, 1000L);
   }

   public static void addPayConfirm(PayConfirmLog log) {
      confirmMap.put(log.getKey(), log);
   }

   public void doConfirm() {
      long now = System.currentTimeMillis();
      Iterator it = confirmMap.values().iterator();

      while(it.hasNext()) {
         PayConfirmLog log = (PayConfirmLog)it.next();
         if (log.needToConfirm(now)) {
            final String openID = log.getOpenID();
            final String openKey = log.getOpenKey();
            final String ts = log.getTs();
            final String payitem = log.getPayitem();
            final String token = log.getToken();
            final String billno = log.getBillno();
            final String zoneid = log.getZoneid();
            final String provide_errno = log.getProvide_errno();
            final String amt = log.getAmt();
            final String payamt_coins = log.getPayamt_coins();
            final String sig = log.getSig();
            ThreadFixedPoolManager.POOL_OTHER.execute(new Runnable() {
               public void run() {
                  APIManager.confirmDelivery(openID, openKey, ts, payitem, token, billno, zoneid, provide_errno, amt, payamt_coins, sig);
               }
            });
            log.addConfirmCount();
         }
      }

   }

   public static void doByResult(int ret, String openID, String billno) {
      PayConfirmLog log = getLog(openID, billno);
      if (log != null) {
         switch(ret) {
         case 1062:
         case 1099:
            if (log.getConfirmCount() < 3) {
               return;
            }
         default:
            if (ret != 0) {
               PayDBManager.saveConfirmLog(log, ret);
            }

            confirmMap.remove(log.getKey());
         }
      }
   }

   private static PayConfirmLog getLog(String openID, String billno) {
      String key = PayConfirmLog.creatKey(openID, billno);
      return (PayConfirmLog)confirmMap.get(key);
   }

   public void doLocalTask() {
      this.doConfirm();
   }

   public void doInterTask() {
      this.doLocalTask();
   }
}
