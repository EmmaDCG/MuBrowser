package com.mu.game;

import com.mu.config.Global;

public class IDFactory {
   private static long temporaryID = 1000000000L;
   private static final int ServerReserved = 100000;
   public static final int PlatReserved = 100000000;
   private static long initialRoleID = 0L;
   private static long initialItemID = 0L;
   private static long initialGangID = 0L;
   private static long initialMailID = 0L;
   private static long initialRedPacketID = 0L;
   public static final int ID_TYPE_ITEM = 1;
   public static final int ID_TYPE_MAIL = 2;

   public static final synchronized long getRoleID() {
      return getServerUniqueID(++initialRoleID);
   }

   public static final synchronized long getItemID() {
      return getServerUniqueID(++initialItemID);
   }

   public static final synchronized long getMailID() {
      return getServerUniqueID(++initialMailID);
   }

   public static final synchronized long getRedPacketID() {
      return getServerUniqueID(++initialRedPacketID);
   }

   public static final synchronized long getGangID() {
      return getServerUniqueID(++initialGangID);
   }

   public static final synchronized long getTemporaryID() {
      return ++temporaryID;
   }

   private static synchronized long getServerUniqueID(long id) {
      return id * 100000000L + (long)Global.getPlatID() * 1L * 100000L + (long)Global.getServerID();
   }

   public static final void setInitialRoleID(long id) {
      initialRoleID = id;
   }

   public static final void setInitialItemID(long id) {
      initialItemID = id;
   }

   public static final void setInitialGangID(long id) {
      initialGangID = id;
   }

   public static final void setInitialMailID(long id) {
      initialMailID = id;
   }

   public static final void setInitialRedPacketID(long id) {
      initialRedPacketID = id;
   }
}
