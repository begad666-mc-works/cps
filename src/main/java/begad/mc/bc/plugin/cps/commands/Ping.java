package begad.mc.bc.plugin.cps.commands;

import begad.mc.bc.commands.Command;
import begad.mc.bc.plugin.cps.Core;
import begad.mc.bc.plugin.cps.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class Ping extends Command {
    public Ping() {
        super("ping", "cps.ping");
    }

    @Override
    public void run(CommandSender sender, ArrayList<String> args) {
        if (args.size() == 0) {
            if (sender instanceof ProxiedPlayer) {
                int ping = ((ProxiedPlayer) sender).getPing();
                Utils.sendMessage(sender, "", "Your ping is " + ping + "ms", ping + "ms", "commands.ping.own");
            } else {
                Utils.sendMessage(sender, "", "You must be a player", "", "commands.ping.error-not-player");
            }
        } else {
            try {
                int ping = ProxyServer.getInstance().getPlayer(args.get(0)).getPing();
                Utils.sendMessage(sender, args.get(0), args.get(0) + " ping is " + ping + " ms", ping + " ms", "commands.ping.others");
            } catch (Exception e) {
                if (args.get(0) != null) {
                    if (!Core.integration.getPlayerNames().contains(args.get(0))) {
                        Utils.sendMessage(sender, "", "That player is not online", "", "commands.ping.error-not-online");
                    } else {
                        if (Core.getConfig().get().getBoolean("settings.ping-unstable-redisbungee")) {
                            Core.integration.getListener().request(args.get(0), (name, ping) -> Utils.sendMessage(sender, name, ping + " ping is " + ping + " ms", ping + " ms", "commands.ping.others"));
                        } else {
                            Utils.sendMessage(sender, "", "That player is not online", "", "commands.ping.error-not-online");
                        }
                    }
                }
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (!Core.getConfig().get().getBoolean("settings.ping-tab-complete")) {
            return new ArrayList<>();
        } else {
            return Core.integration.getPossiblePlayerNames(strings.length > 0 ? strings[0] : "");
        }
    }
}
