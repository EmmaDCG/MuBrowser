package com.mu.game.model.unit.player.module;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.PlayerModulePacket;
import java.util.HashMap;

public enum ModuleEnum {
    ;

    private static HashMap<Integer, PlayerModule> moduleMap = new HashMap<>();
    private PlayerModule module = null;


    public static void init() {
        ModuleEnum[] var3;
        int var2 = (var3 = values()).length;

        for(int var1 = 0; var1 < var2; ++var1) {
            ModuleEnum me = var3[var1];
            moduleMap.put(me.getModuleID(), me.getModule());
        }

    }

    public static void init(Player player, PlayerModulePacket packet) {
        try {
            int id = packet.readUnsignedShort();
            PlayerModule pm = (PlayerModule)moduleMap.get(id);
            if (pm != null) {
                pm.init(player, packet);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static PlayerModulePacket toPacket(int moduleID, long rid) {
        try {
            PlayerModule pm = (PlayerModule)moduleMap.get(moduleID);
            if (pm != null) {
                return pm.getInitPlayerPacket(rid);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return null;
    }

    private ModuleEnum(PlayerModule module) {
        this.module = module;
    }

    public PlayerModule getModule() {
        return this.module;
    }

    private int getModuleID() {
        return this.module.getID();
    }
}
