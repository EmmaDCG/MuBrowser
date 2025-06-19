package com.mu.db.log.atom;

public class PayConfirmLog {
   public static final int maxConfirmTime = 3;
   private String openID;
   private String openKey;
   private String ts;
   private String payitem;
   private String token;
   private String billno;
   private String zoneid;
   private String provide_errno;
   private String amt;
   private String payamt_coins;
   private String sig;
   private int sid;
   private long logTime;
   private int confirmCount = 0;
   private String key;

   public PayConfirmLog(String openID, String openKey, String ts, String payitem, String token, String billno, String zoneid, String provide_errno, String amt, String payamt_coins, String sig, int sid) {
      this.openID = openID;
      this.openKey = openKey;
      this.ts = ts;
      this.payitem = payitem;
      this.token = token;
      this.billno = billno;
      this.zoneid = zoneid;
      this.provide_errno = provide_errno;
      this.amt = amt;
      this.payamt_coins = payamt_coins;
      this.sig = sig;
      this.sid = sid;
      this.key = creatKey(openID, billno);
      this.logTime = System.currentTimeMillis();
   }

   public static String creatKey(String openID, String billno) {
      String key = openID + "_" + billno;
      return key;
   }

   public void addConfirmCount() {
      ++this.confirmCount;
   }

   public boolean needToConfirm(long now) {
      if (this.confirmCount >= 3) {
         return false;
      } else {
         return now >= this.logTime + (long)(7000 * (this.confirmCount + 1));
      }
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public long getLogTime() {
      return this.logTime;
   }

   public void setLogTime(long logTime) {
      this.logTime = logTime;
   }

   public int getConfirmCount() {
      return this.confirmCount;
   }

   public void setConfirmCount(int confirmCount) {
      this.confirmCount = confirmCount;
   }

   public String getOpenID() {
      return this.openID;
   }

   public void setOpenID(String openID) {
      this.openID = openID;
   }

   public String getOpenKey() {
      return this.openKey;
   }

   public void setOpenKey(String openKey) {
      this.openKey = openKey;
   }

   public String getTs() {
      return this.ts;
   }

   public void setTs(String ts) {
      this.ts = ts;
   }

   public String getPayitem() {
      return this.payitem;
   }

   public void setPayitem(String payitem) {
      this.payitem = payitem;
   }

   public String getToken() {
      return this.token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public String getBillno() {
      return this.billno;
   }

   public void setBillno(String billno) {
      this.billno = billno;
   }

   public String getZoneid() {
      return this.zoneid;
   }

   public void setZoneid(String zoneid) {
      this.zoneid = zoneid;
   }

   public String getProvide_errno() {
      return this.provide_errno;
   }

   public void setProvide_errno(String provide_errno) {
      this.provide_errno = provide_errno;
   }

   public String getAmt() {
      return this.amt;
   }

   public void setAmt(String amt) {
      this.amt = amt;
   }

   public String getPayamt_coins() {
      return this.payamt_coins;
   }

   public void setPayamt_coins(String payamt_coins) {
      this.payamt_coins = payamt_coins;
   }

   public String getSig() {
      return this.sig;
   }

   public void setSig(String sig) {
      this.sig = sig;
   }

   public int getSid() {
      return this.sid;
   }

   public void setSid(int sid) {
      this.sid = sid;
   }
}
