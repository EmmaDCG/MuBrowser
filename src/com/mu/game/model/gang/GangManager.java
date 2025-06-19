package com.mu.game.model.gang;

import com.mu.config.Global;
import com.mu.config.MessageText;
import com.mu.db.manager.GangDBManager;
import com.mu.game.CenterManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.gang.ChangeApplyStatus;
import com.mu.io.game.packet.imp.gang.GangBattleInvitation;
import com.mu.io.game.packet.imp.gang.GetGangList;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import jxl.Sheet;
import jxl.Workbook;

public class GangManager {
    public static final int Post_Master = 2;
    public static final int Post_Vice_Master = 1;
    public static final int Post_Normal = 0;
    public static final int Castellan = 1;
    public static final int Vice_Castellan = 2;
    public static final int Elite = 3;
    public static final int Size_PerPage = 11;
    public static final int RedPacket_BindIngot = 0;
    public static final int RedPacket_Ingot = 1;
    private static long warVictorGang = -1L;
    private static final ConcurrentHashMap gangMap = new ConcurrentHashMap(16, 0.75F, 2);
    private static final ConcurrentHashMap memberMap = new ConcurrentHashMap(16, 0.75F, 2);
    private static final HashMap levelMap = new HashMap();
    private static final HashMap flagMap = new HashMap();
    private static final ConcurrentHashMap welfareReceiveMap = new ConcurrentHashMap(16, 0.75F, 2);
    private static final ConcurrentHashMap warReceiveMap = new ConcurrentHashMap(16, 0.75F, 2);
    private static final ConcurrentHashMap playerApplyMap = new ConcurrentHashMap(16, 0.75F, 2);
    private static HashMap warRankMap = new HashMap();
    private static HashMap warName = new HashMap();
    private static HashMap redPacketInfoMap = new HashMap();
    private static ArrayList bindRedList = new ArrayList();
    private static ArrayList ingotRedList = new ArrayList();
    private static int CreateNeedMoney = 10000;
    private static Item zmlItem;
    private static String zmlDes;
    private static int dailyMaxBindRedReceive = 10;
    private static String sendPacketNotice;
    private static int defaultFlag = -1;

    public static void initGangData(InputStream in) throws Exception {
        Workbook wb = Workbook.getWorkbook(in);
        initGangLevel(wb.getSheet(1));
        initFlag(wb.getSheet(2));
        initOther(wb.getSheet(3));
        initWarName(wb.getSheet(4));
        initRedPacket(wb.getSheet(5));
    }

    private static void initWarName(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            warName.put(id, name);
        }

    }

    private static void initRedPacket(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            int type = Tools.getCellIntValue(sheet.getCell("B" + i));
            int ingotReq = Tools.getCellIntValue(sheet.getCell("C" + i));
            int ingot = Tools.getCellIntValue(sheet.getCell("D" + i));
            String detailStr = Tools.getCellValue(sheet.getCell("E" + i));
            String name = Tools.getCellValue(sheet.getCell("F" + i));
            String des = Tools.getCellValue(sheet.getCell("G" + i));
            boolean isBroadcast = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
            RedPacketInfo info = new RedPacketInfo();
            info.setId(id);
            info.setIngot(ingot);
            info.setIngotReq(ingotReq);
            info.setType(type);
            info.setName(name);
            info.setDes(des);
            info.setBroadcast(isBroadcast);
            if (isBroadcast) {
                info.setBroadcastContent(Tools.getCellValue(sheet.getCell("I" + i)));
            }

            String[] str = detailStr.split(",");
            int[] in = new int[str.length];

            for (int j = 0; j < str.length; ++j) {
                in[j] = Integer.parseInt(str[j]);
            }

            info.setDetail(in);
            redPacketInfoMap.put(id, info);
            if (type == 0) {
                bindRedList.add(new int[]{id, ingotReq});
            } else {
                ingotRedList.add(new int[]{id, ingotReq});
            }
        }

        Collections.sort(bindRedList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((int[])o2)[1] - ((int[])o2)[1];
            }
        });
    }

    public static String getPostName(int post) {
        String name = (String) warName.get(post);
        return name == null ? (String) warName.get(Integer.valueOf(0)) : name;
    }

    public static ConcurrentHashMap getGangMap() {
        return gangMap;
    }

    private static void initFlag(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int id = Tools.getCellIntValue(sheet.getCell("A" + i));
            int icon = Tools.getCellIntValue(sheet.getCell("B" + i));
            if (defaultFlag == -1) {
                defaultFlag = icon;
            }

            flagMap.put(icon, id);
        }

    }

    public static RedPacketInfo getRedPacketInfo(int id) {
        return (RedPacketInfo) redPacketInfoMap.get(id);
    }

    public static ArrayList getBindRedList() {
        return bindRedList;
    }

    public static ArrayList getIngotRedList() {
        return ingotRedList;
    }

    public static long getWarVictorGang() {
        return warVictorGang;
    }

    public static void setWarVictorGang(long gid) {
        warVictorGang = gid;
    }

    public static void clearVictoryGang() {
        if (warVictorGang >= 0L) {
            GangDBManager.updateGangWinner(warVictorGang, false);
            warVictorGang = -1L;
        }
    }

    public static void warVictory(long gangId) {
        warVictorGang = gangId;
        GangDBManager.updateGangWinner(warVictorGang, true);
    }

    public static int getDefaultFlag() {
        return defaultFlag;
    }

    public static boolean hasFlag(int flag) {
        return flagMap.containsKey(flag);
    }

    private static void initOther(Sheet sheet) {
        CreateNeedMoney = Tools.getCellIntValue(sheet.getCell("B2"));
        int modelId = Tools.getCellIntValue(sheet.getCell("B3"));
        zmlItem = ItemTools.createItem(modelId, 1, 2);
        zmlDes = Tools.getCellValue(sheet.getCell("B4"));
        dailyMaxBindRedReceive = Tools.getCellIntValue(sheet.getCell("B5"));
        sendPacketNotice = Tools.getCellValue(sheet.getCell("B6"));
    }

    public static int getDailyMaxBindRedReceive() {
        return dailyMaxBindRedReceive;
    }

    public static String getZmlDes() {
        return zmlDes;
    }

    public static Item getZmlItem() {
        return zmlItem;
    }

    private static void initGangLevel(Sheet sheet) throws Exception {
        int rows = sheet.getRows();

        for (int i = 2; i <= rows; ++i) {
            int level = Tools.getCellIntValue(sheet.getCell("A" + i));
            String name = Tools.getCellValue(sheet.getCell("B" + i));
            int maxMemberSize = Tools.getCellIntValue(sheet.getCell("C" + i));
            int money = Tools.getCellIntValue(sheet.getCell("D" + i));
            int bindIngot = Tools.getCellIntValue(sheet.getCell("E" + i));
            int levelUpNeedMoney = Tools.getCellIntValue(sheet.getCell("F" + i));
            int levelUpNeedIngot = Tools.getCellIntValue(sheet.getCell("G" + i));
            int flag = Tools.getCellIntValue(sheet.getCell("H" + i));
            GangLevelData gd = new GangLevelData(level);
            gd.setMaxMember(maxMemberSize);
            gd.setName(name);
            gd.setMoney(money);
            gd.setBindIngot(bindIngot);
            gd.setFlag(flag);
            gd.setLevelUpNeedIngot(levelUpNeedIngot);
            gd.setLevelUpNeedMoney(levelUpNeedMoney);
            levelMap.put(level, gd);
        }

    }

    public static int getCreateNeedMoney() {
        return CreateNeedMoney;
    }

    public static void addWarReceiveLog(long id, long day) {
        warReceiveMap.put(id, day);
    }

    public static boolean hasWarRerceive(long id) {
        Long day = (Long) warReceiveMap.get(id);
        return day != null && day.longValue() == Time.getDayLong();
    }

    public static void addWelfareReceiveLog(long id, long day) {
        welfareReceiveMap.put(id, day);
    }

    public static boolean hasReceiveWelfare(long rid) {
        Long time = (Long) welfareReceiveMap.get(rid);
        return time != null && time.longValue() == Time.getDayLong();
    }

    public static HashMap getFlagMap() {
        return flagMap;
    }

    public static void addApplyGang(long rid, long gangId) {
        HashSet set = (HashSet) playerApplyMap.get(rid);
        if (set == null) {
            set = new HashSet();
            playerApplyMap.put(rid, set);
        }

        set.add(gangId);
    }

    public static void removeApplyForJoinGang(final long rid) {
        HashSet set = (HashSet) playerApplyMap.remove(rid);
        Player player = CenterManager.getPlayerByRoleID(rid);
        if (set != null && set.size() > 0) {
            Iterator var6 = set.iterator();

            while (var6.hasNext()) {
                long gangId = ((Long) var6.next()).longValue();
                Gang gang = getGang(gangId);
                if (gang != null) {
                    gang.removeApplyInfo(rid, true);
                    gang.masterDeleteApplyInfo(rid);
                    if (player != null) {
                        ChangeApplyStatus.changeStatus(player, gangId, false);
                    }
                }
            }

            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                public void run() {
                    GangDBManager.clearPlayerApply(rid);
                }
            });
            set.clear();
        }

    }

    public static void removeGang(long gangId) {
        gangMap.remove(gangId);
    }

    public static void removePlayerOneGangApply(long rid, long gangId) {
        HashSet set = (HashSet) playerApplyMap.get(rid);
        if (set != null) {
            set.remove(gangId);
            if (set.size() == 0) {
                playerApplyMap.remove(rid);
            }
        }

    }

    public static int getPlayerApplyTimes(long rid) {
        HashSet set = (HashSet) playerApplyMap.get(rid);
        return set == null ? 0 : set.size();
    }

    public static GangLevelData getLevelData(int level) {
        return (GangLevelData) levelMap.get(level);
    }

    public static GangLevelData getNextLevelData(int curLevel) {
        return getLevelData(curLevel + 1);
    }

    public static boolean inSameGang(long roleID, long otherRole) {
        GangMember m1 = getMember(roleID);
        GangMember m2 = getMember(otherRole);
        return m1 != null && m2 != null && m1.getGangId() == m2.getGangId();
    }

    public static void addGang(Gang gang) {
        gangMap.put(gang.getId(), gang);
    }

    public static Gang getGang(long id) {
        return (Gang) gangMap.get(id);
    }

    public static void addMember(GangMember member) {
        memberMap.put(member.getId(), member);
    }

    public static GangMember getMember(long id) {
        return (GangMember) memberMap.get(id);
    }

    public static int createGang(Player player, String name) {
        if (Global.isInterServiceServer()) {
            return 7;
        } else {
            return player.getGang() != null ? 9001 : 1;
        }
    }

    public static void removeMember(long rid) {
        memberMap.remove(rid);
    }

    private static void pushGangList(Player player, int page, ArrayList list) {
        GetGangList gl = new GetGangList();
        int size = list.size();
        int residue = size % 11;
        int maxPage = size / 11;
        if (residue != 0 || size == 0) {
            ++maxPage;
        }

        int tmpPage = page;
        if (page < 1) {
            tmpPage = 1;
        } else if (page > maxPage) {
            tmpPage = maxPage;
        }

        int begin = 11 * (tmpPage - 1);
        int end = begin + 11;
        if (end > size) {
            end = size;
        }

        try {
            ArrayList gangList = new ArrayList();

            for (int i = begin; i < end; ++i) {
                GangSortInfo info = (GangSortInfo) list.get(i);
                Gang gang = getGang(info.getGangId());
                if (gang != null) {
                    gangList.add(gang);
                }
            }

            gl.writeShort(tmpPage);
            gl.writeShort(maxPage);
            gl.writeByte(gangList.size());
            Iterator var22 = gangList.iterator();

            while (var22.hasNext()) {
                Gang gang = (Gang) var22.next();
                GangLevelData gd = getLevelData(gang.getLevel());
                gl.writeDouble((double) gang.getId());
                gl.writeUTF(gang.getName());
                gl.writeByte(gang.getLevel());
                gl.writeShort(gang.getFlagId());
                gl.writeDouble((double) gang.getContribution());
                GangMember master = gang.getMasterMember();
                if (master != null) {
                    gl.writeDouble((double) master.getId());
                    gl.writeUTF(master.getName());
                    int[] blueIcons = master.getBlueIcons();
                    gl.writeShort(blueIcons[0]);
                    gl.writeShort(blueIcons[1]);
                } else {
                    gl.writeDouble(-1.0D);
                    gl.writeUTF("");
                    gl.writeShort(-1);
                    gl.writeShort(-1);
                }

                gl.writeByte(gang.getMemberSize());
                gl.writeByte(gd.getMaxMember());
                gl.writeBoolean(gang.hasApply(player.getID()));
            }

            player.writePacket(gl);
            gangList.clear();
        } catch (Exception var19) {
            var19.printStackTrace();
        } finally {
            gl.destroy();
            gl = null;
        }

    }

    public static ArrayList getWarTopGang() {
        ArrayList list = new ArrayList();
        Iterator it = gangMap.values().iterator();

        while (it.hasNext()) {
            list.add((Gang) it.next());
        }

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Gang)o2).getMemberTotalLevel() - ((Gang)o2).getMemberTotalLevel();
            }
        });

        return list;
    }

    public static void confirmRank() {
        long day = Time.getDayLong();
        ArrayList list = getWarTopGang();
        int max = Math.min(3, list.size());
        ArrayList rList = new ArrayList();
        long[] gangs = new long[4];

        Gang gang;
        for (int i = 0; i < max; ++i) {
            gang = (Gang) list.get(i);
            GangWarRankInfo info = new GangWarRankInfo();
            info.setId(gang.getId());
            info.setName(gang.getName());
            info.setFlag(gang.getFlagId());
            info.setTotalLevel(gang.getMemberTotalLevel());
            rList.add(info);
            gangs[i] = gang.getId();
        }

        GangDBManager.insertGangWarQualification(day, gangs);
        warRankMap.put(day, rList);
        GangBattleInvitation gi = GangBattleInvitation.getGangBattleInvitation();
        Iterator var13 = list.iterator();

        while (var13.hasNext()) {
            gang = (Gang) var13.next();
            Iterator it = gang.getMemberMap().values().iterator();

            while (it.hasNext()) {
                GangMember member = (GangMember) it.next();
                Player player = member.getPlayer();
                if (player != null) {
                    player.writePacket(gi);
                }
            }
        }

        gi.destroy();
        gi = null;
        list.clear();
    }

    public static GangWarRankInfo createGangWarRankInfo(long gangId) {
        Gang gang = getGang(gangId);
        if (gang == null) {
            return null;
        } else {
            GangWarRankInfo info = new GangWarRankInfo();
            info.setId(gang.getId());
            info.setName(gang.getName());
            info.setFlag(gang.getFlagId());
            info.setTotalLevel(gang.getMemberTotalLevel());
            return info;
        }
    }

    public static ArrayList getRealtimeInfoList() {
        ArrayList list = getWarTopGang();
        ArrayList iList = new ArrayList();
        int i = 0;
        Iterator var4 = list.iterator();

        while (var4.hasNext()) {
            Gang gang = (Gang) var4.next();
            GangWarRankInfo info = new GangWarRankInfo();
            info.setId(gang.getId());
            info.setName(gang.getName());
            info.setFlag(gang.getFlagId());
            info.setTotalLevel(gang.getMemberTotalLevel());
            iList.add(info);
            ++i;
            if (i >= 3) {
                break;
            }
        }

        list.clear();
        list = null;
        return iList;
    }

    public static ArrayList getGangWarRankInfoList(long day) {
        return (ArrayList) warRankMap.get(day);
    }

    public static int getWarDailyReceiveStatus(Player player) {
        Gang gang = player.getGang();
        if (gang != null && gang.getId() == warVictorGang) {
            return hasWarRerceive(player.getID()) ? 2 : 1;
        } else {
            return 0;
        }
    }

    public static boolean isCastellan(Player player) {
        Gang gang = player.getGang();
        return gang != null && gang.isWinner() && gang.getMasterId() == player.getID();
    }

    public static String getViewGangName(Gang gang) {
        if (gang == null) {
            return "";
        } else {
            String gName = gang.getName();
            if (gang.isWinner()) {
                gName = MessageText.getText(9058).replace("%s%", gang.getName()).replace("<", "&lt;").replace(">", "&gt;");
            } else {
                gName = MessageText.getText(9059).replace("%s%", gang.getName()).replace("<", "&lt;").replace(">", "&gt;");
            }

            return gName;
        }
    }

    public static String getSendPacketNotice() {
        return sendPacketNotice;
    }

    public static void pushGangList(Player player, int page, String key) {
        ArrayList list = new ArrayList();
        Iterator it;
        Gang gang;
        GangSortInfo info;
        if (key.trim().equals("")) {
            for (it = gangMap.values().iterator(); it.hasNext(); list.add(info)) {
                gang = (Gang) it.next();
                info = new GangSortInfo(gang.getId());
                info.setContribution(gang.getContribution());
                if (gang.hasApply(player.getID()) && player.getGang() == null) {
                    info.setApplyStatus(1);
                }
            }
        } else {
            it = gangMap.values().iterator();

            while (it.hasNext()) {
                gang = (Gang) it.next();
                if (gang.getName().indexOf(key) != -1) {
                    info = new GangSortInfo(gang.getId());
                    info.setContribution(gang.getContribution());
                    if (gang.hasApply(player.getID()) && player.getGang() == null) {
                        info.setApplyStatus(1);
                    }

                    list.add(info);
                }
            }
        }

        Collections.sort(list);
        pushGangList(player, page, list);
        list.clear();
        list = null;
    }
}
