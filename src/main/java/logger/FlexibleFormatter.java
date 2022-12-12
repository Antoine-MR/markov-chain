package fr.equipe1b.logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * @author Antoine Maïstre-Rice
 */
public class FlexibleFormatter extends Formatter {

    public static HashMap<Logger, Integer> formatted = new HashMap<>();
    public static HashMap<Logger, FlexibleFormatter> loggersAndFormatters = new HashMap<>();
    private Color color = Color.BLUE;
    private boolean levelBool = false;
    private boolean nameBool = false;
    private boolean dateBool = false;

    private static void formatFirst(Logger l) {
        l.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        FlexibleFormatter formatter = new FlexibleFormatter();
        handler.setFormatter(formatter);
        l.addHandler(handler);

        FlexibleFormatter.loggersAndFormatters.put(l, formatter);
    }

    private static void saveData(Logger l) {
        if (formatted.containsKey(l)) {
            Integer temp = formatted.get(l);
            formatted.put(l, temp + 1);
        } else {
            formatted.put(l, 0);
        }
    }

    public static void setLogger(Logger l) {

        if (FlexibleFormatter.formatted.containsKey(l))
            return;

        l.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        FlexibleFormatter formatter = new FlexibleFormatter();
        handler.setFormatter(formatter);
        l.addHandler(handler);

        FlexibleFormatter.loggersAndFormatters.put(l, formatter);

        saveData(l);
    }

    private static FlexibleFormatter find(Logger l) {
        FlexibleFormatter ff;
        try {
            ff = FlexibleFormatter.loggersAndFormatters.get(l);
        } catch (Exception e) {
            throw new Error("le logger n'a pas été formatté par un FlexibleFormatter");
        }
        return ff;
    }

    public static void activateDate(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.activateDate();
    }

    public static void desactivateDate(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.desactivateDate();
    }

    public static void activateName(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.activateName();
    }

    public static void desactivateName(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.desactivateName();
    }

    public static void activateLevel(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.activateLevel();
    }

    public static void desactivateLevel(Logger l) {
        FlexibleFormatter ff = find(l);
        ff.desactivateLevel();
    }

    public static void selectColor(Logger l, Color c) {
        FlexibleFormatter ff = find(l);
        ff.selectColor(c);
    }

    private static String getANSIFromColor(Color c) {
        return switch (c) {
            case BLUE -> "\u001B[34m";
            case GREEN -> "\u001B[32m";
            case PURPLE -> "\u001B[35m";
            case RED -> "\u001B[31m";
            case WHITE -> "\u001B[37m";
            case YELLOW -> "\u001B[33m";
        };
    }

    private String name(LogRecord l) {
        if (nameBool)
            return "[" + l.getLoggerName() + "]";
        return "";
    }

    private String level(LogRecord l) {
        if (levelBool)
            return "[" + l.getLevel().toString() + "]";
        return "";
    }

    private String date() {
        if (dateBool)
            return "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "]";
        return "";
    }

    public String format(LogRecord content) {

        String date = this.date();
        String name = this.name(content);
        String level = this.level(content);
        String message = content.getMessage();

        List<String> values = new ArrayList<>() {{
            add(date);
            add(name);
            add(level);
        }};

        StringBuilder result = new StringBuilder();
        result.append(FlexibleFormatter.getANSIFromColor(this.color));
        for (String i : values) {
            boolean activated = i.length() > 0;
            if (activated) {
                result.append(i).append(" ");
            }
        }
        result.append(message).append("\n");
        return result.toString();
    }

    public void activateDate() {
        this.dateBool = true;
    }

    public void desactivateDate() {
        this.dateBool = false;
    }

    public void selectColor(Color c) {
        this.color = c;
    }

    public void activateLevel() {
        this.levelBool = true;
    }

    public void desactivateLevel() {
        this.levelBool = false;
    }

    public void activateName() {
        this.nameBool = true;
    }

    public void desactivateName() {
        this.nameBool = false;
    }


    public enum Color {BLUE, GREEN, PURPLE, RED, WHITE, YELLOW}
}
