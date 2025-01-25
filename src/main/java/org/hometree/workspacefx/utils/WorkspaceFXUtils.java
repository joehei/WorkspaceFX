package org.hometree.workspacefx.utils;

import atlantafx.base.theme.Styles;
import org.apache.logging.log4j.spi.StandardLevel;
import org.hometree.workspacefx.plugin.logging.WSPLog4j2Utils;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignI;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;

public class WorkspaceFXUtils {

    private static int defaultIconSize = 16;

    private WorkspaceFXUtils() {
    }

    public static int getDefaultIconSize() {
        return defaultIconSize;
    }

    public static void setDefaultIconSize(int defaultIconSize) {
        WorkspaceFXUtils.defaultIconSize = defaultIconSize;
    }

    public static FontIcon getIcon(Ikon code, String color) {
        FontIcon icon = new FontIcon(code);
        Styles.appendStyle(icon, "-fx-fill", color);
        Styles.appendStyle(icon, "-fx-icon-color", color);
        return icon;
    }

    public static FontIcon infoIcon() {
        return getIcon(MaterialDesignI.INFORMATION, "#00e007");
    }

    public static FontIcon warnIcon() {
        return getIcon(MaterialDesignA.ALERT, "#d9ed00");
    }

    public static FontIcon errorIcon() {
        return getIcon(MaterialDesignA.ALERT_OCTAGON, "#ff7b7b");
    }

    public static FontIcon debugIcon() {
        return getIcon(MaterialDesignT.TEXT_BOX_SEARCH_OUTLINE, "lightgray");
    }

}
