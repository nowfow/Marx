package org.nowfow.marx.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.*;
import org.jetbrains.annotations.NotNull;
import org.nowfow.marx.Markers;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MarxCommand  implements CommandExecutor {
    private final Markers plugin;
    private final DynmapCommonAPI dynmapCommonAPI;
    private final MarkerAPI markerAPI;

    public MarxCommand(Markers instance) {
        this.plugin = instance;
        this.dynmapCommonAPI = (DynmapCommonAPI) plugin.getServer().getPluginManager().getPlugin("dynmap");
        markerAPI = dynmapCommonAPI.getMarkerAPI();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equalsIgnoreCase("add")) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            MarkerSet markerSet = dynmapCommonAPI.getMarkerAPI().getMarkerSet("45431"); // название набора меток
            if (markerSet == null) {
                markerSet = dynmapCommonAPI.getMarkerAPI().createMarkerSet("45431", "Marx", null, false); // название, метка, иконка, скрытость
            }
            int randomId = new Random().nextInt(9999); // генерируем случайный ID метки от 0 до 9999
            String markerId = String.format("%04d", randomId); // форматируем ID метки, чтобы он содержал четыре цифры (например, если случайное число равно 12, то ID будет равен "0012")
            String markerLabel = args[1];

            String world = location.getWorld().getName();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();

            MarkerIcon icon = null;
            if (args.length > 2) {
                icon = dynmapCommonAPI.getMarkerAPI().getMarkerIcon(args[2]);
                if (icon == null) {
                    player.sendMessage(ChatColor.RED + "Иконка " + args[2] + " не найдена!");
                    return true;
                }
            } else {
                icon = dynmapCommonAPI.getMarkerAPI().getMarkerIcon("redflag"); // иконка метки по умолчанию
            }
            Marker marker = markerSet.createMarker(markerId, markerLabel, world, x, y, z, icon, false); // ID метки, название метки, название набора меток, иконка метки, скрытость
            markerSet.setMarkerSetLabel("45431"); // название набора меток
            player.sendMessage(ChatColor.GREEN + "Метка " + markerLabel + " создана с ID " + markerId + "!");
        }
        if (args[0].equalsIgnoreCase("list")) {
            for (MarkerSet ms : markerAPI.getMarkerSets()) {
                List<Marker> sortedMarkers = ms.getMarkers().stream().sorted(Comparator.comparing(GenericMarker::getMarkerID)).toList();
                for (Marker m : sortedMarkers) {
                    sender.sendMessage(ChatColor.GOLD + m.getMarkerID() + ": " + ChatColor.WHITE + m.getLabel() + ChatColor.AQUA + " [" + m.getWorld() + ": " + m.getX() + "," + m.getY() + "," + m.getZ() + "]");
                }
                //есть проблемы с показом меток по какой то причине после перезапуска в лист не попадают новые метки.
            }
        }
        if (args[0].equalsIgnoreCase("remove")) {
            String markerId = args[1];
            MarkerSet markerSet = dynmapCommonAPI.getMarkerAPI().getMarkerSet("marx"); // название набора меток
            if (markerSet != null) {
                Marker marker = markerSet.findMarker(markerId);
                if (marker != null) {
                    marker.deleteMarker();
                    sender.sendMessage(ChatColor.GREEN + "Метка с ID " + markerId + " удалена!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Метка с ID " + markerId + " не найдена!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Набор меток не найден!");
            }
        }
    return true;
    }

}