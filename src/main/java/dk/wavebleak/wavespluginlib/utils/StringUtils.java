package dk.wavebleak.wavespluginlib.utils;

import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public class StringUtils {
    public static String formatTime(int seconds) {
        int minutes = (int) Math.floor((double) seconds / 60);
        int hours = (int) Math.floor((double) minutes / 60);
        int days = (int) Math.floor((double) hours / 24);

        if(days >= 1) {
            return days + " dag" + (days > 1 ? "e" : "");
        }
        if(hours >= 1) {
            return hours + " time" + (hours > 1 ? "r" : "");
        }
        if(minutes >= 1) {
            return minutes + " minut" + (minutes > 1 ? "ter" : "");
        }
        return seconds + " sekund" + (seconds > 1 ? "er" : "");
    }

    public static String line(int amount) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.DARK_GRAY).append(ChatColor.STRIKETHROUGH);
        for (int i = 0; i < amount; i++)
            builder.append(" ");
        return builder.toString();
    }

    public static String progressBar(char progressChar, ChatColor color1, ChatColor color2, float percentComplete, int totalChars) {
        StringBuilder progressBar = new StringBuilder();

        int progressChars = (int) (percentComplete/100.0 * totalChars);
        int blankChars = totalChars - progressChars;

        for(int i=0; i<progressChars; i++) {
            progressBar.append(color1).append((progressChar));
        }

        for(int i=0; i<blankChars; i++) {
            progressBar.append(color2).append((progressChar));
        }

        return progressBar.toString();
    }

    public static String beautify(String in) {
        in = in.replace('_', ' ');
        String pre = in.substring(0, 1);
        String post = in.substring(1);
        return pre.toUpperCase() + post.toLowerCase();
    }
}
