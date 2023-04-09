package org.nowfow.marx;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
            MarkerSet markerSet = dynmapCommonAPI.getMarkerAPI().getMarkerSet("markers"); // название набора меток
            int randomId = new Random().nextInt(9999); // генерируем случайный ID метки от 0 до 9999
            String markerId = String.format("%04d", randomId); // форматируем ID метки, чтобы он содержал четыре цифры (например, если случайное число равно 12, то ID будет равен "0012")
            String markerLabel = args[1];
            String MarkerDesc = args[3];

            String world = location.getWorld().getName();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();

            MarkerIcon icon;
                icon = dynmapCommonAPI.getMarkerAPI().getMarkerIcon(args[2]);
                if (icon == null) {
                    player.sendMessage(ChatColor.RED + "Иконка " + args[2] + " не найдена!");
                    return true;

            } else {
                icon = dynmapCommonAPI.getMarkerAPI().getMarkerIcon("redflag"); // иконка метки по умолчанию
            }
            markerSet.setMarkerSetLabel("Markers");
            Marker marker = markerSet.createMarker(markerId, markerLabel, true, world, x, y, z, icon, true);
            player.sendMessage(ChatColor.GREEN + "Метка " + markerLabel + " создана с ID " + markerId + "!");
            marker.setDescription(MarkerDesc);
        }


        else if (args[0].equalsIgnoreCase("remove")) {
            String markerId = args[1];
            MarkerSet markerSet = dynmapCommonAPI.getMarkerAPI().getMarkerSet("markers"); // название набора меток
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
        else if (args[0].equalsIgnoreCase("list")) {
            MarkerSet markerSet = dynmapCommonAPI.getMarkerAPI().getMarkerSet("markers"); // Название набора меток
            if (markerSet != null) {
                sender.sendMessage(ChatColor.YELLOW + "Список меток:");
                Collection<Marker> markers = markerSet.getMarkers(); // Получаем список всех меток в наборе меток
                for (Marker marker : markers) {
                    String markerId = marker.getMarkerID();
                    String markerLabel = marker.getLabel();
                    String world = marker.getWorld();
                    double x = marker.getX();
                    double y = marker.getY();
                    double z = marker.getZ();
                    sender.sendMessage(ChatColor.YELLOW + "- ID: " + markerId + ", Название: " + markerLabel + ", Координаты: X=" + x + ", Y=" + y + ", Z=" + z);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Набор меток не найден!");


            }
        }
        return true;
    }
}