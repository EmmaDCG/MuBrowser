package com.mu.io.game.packet.imp.gang;

import com.mu.config.MessageText;
import com.mu.config.VariableConstant;
import com.mu.db.manager.GangDBManager;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.DFA;
import com.mu.utils.concurrent.ThreadCachedPoolManager;

public class CreateGang extends ReadAndWritePacket {
    public CreateGang(int code, byte[] readBuf) {
        super(code, readBuf);
    }

    public CreateGang(boolean isSuccess) {
        super(10608, (byte[]) null);

        try {
            this.writeBoolean(isSuccess);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void process() throws Exception {
        final Player player = this.getPlayer();
        final String name = this.readUTF().trim().toLowerCase();
        int flag = this.readShort();
        boolean b = true;
        if (player.getGang() != null) {
            SystemMessage.writeMessage(player, 9001);
            b = false;
        } else if (name.length() > VariableConstant.MaxGangNameSize) {
            SystemMessage.writeMessage(player, 9007);
            b = false;
        } else if (!DFA.hasKeyWords(name) && !name.equals("")) {
            if (player.getMoney() < GangManager.getCreateNeedMoney()) {
                SystemMessage.writeMessage(player, MessageText.getText(9023).replace("%s%", String.valueOf(GangManager.getCreateNeedMoney())), 9023);
                b = false;
            }
        } else {
            SystemMessage.writeMessage(player, 9008);
            b = false;
        }

        if (!b) {
            this.writeBoolean(false);
            player.writePacket(this);
        } else {
            if (!GangManager.hasFlag(flag)) {
                flag = GangManager.getDefaultFlag();
            }

            int finalFlag = flag;
            ThreadCachedPoolManager.DB_SHORT.execute(new Runnable() {
                public void run() {
                    CreateGang cg = new CreateGang(GangDBManager.createGang(player, "s" + player.getUser().getServerID() + "." + name, finalFlag));
                    player.writePacket(cg);
                    cg.destroy();
                    cg = null;
                }
            });
        }
    }
}
