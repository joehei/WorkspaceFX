package org.hometree.workspacefx.utils;

import atlantafx.base.theme.Styles;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
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
        FontIcon icon = new FontIcon(MaterialDesignI.INFORMATION);
        icon.getStyleClass().add(Styles.ACCENT);
        return icon;
    }

    public static FontIcon successIcon() {
        return getIcon(MaterialDesignC.CHECK_CIRCLE, "#00e007");
    }

    public static FontIcon warnIcon() {
        FontIcon icon = new FontIcon(MaterialDesignA.ALERT);
        icon.getStyleClass().add(Styles.WARNING);
        return icon;
//        return getIcon(MaterialDesignA.ALERT, "#d9ed00");
    }

    public static FontIcon errorIcon() {
        return getIcon(MaterialDesignA.ALERT_OCTAGON, "#ff7b7b");
    }

    public static FontIcon debugIcon() {
        return getIcon(MaterialDesignT.TEXT_BOX_SEARCH_OUTLINE, "lightgray");
    }

    /**
     * copy the given image to a writeable image
     *
     * @param image The image to copy
     * @return a writeable image
     */
    public static WritableImage copyImage(Image image) {
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);
            }
        }
        return writableImage;
    }

}
