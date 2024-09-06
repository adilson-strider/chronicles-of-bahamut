package tools;

import main.GameManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class ResourceLoader {

    public static BufferedImage loadImage(final String path) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(ResourceLoader.class.getResource(path)));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image: " + path);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Image not found: " + path);
        }

        return image;
    }

    public static String loadTextFile(final String path) {
        String content = "";

        InputStream byteInput = ResourceLoader.class.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteInput));

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteInput != null) {
                    byteInput.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return content;
    }

    public static Font loadFont(String path) {
        Font font = null;

        try {
            InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(inputStream));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        font = font.deriveFont(12f);

        return font;
    }

    public static Clip loadSound(final String path) {
        Clip clip = null;

        try {
            InputStream is = ClassLoader.class.getResourceAsStream(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clip;
    }

    public static Clip loadSoundAdjustVolume(final String path, final float volumeReductionDecibels) {
        Clip clip = null;

        try {
            InputStream is = ClassLoader.class.getResourceAsStream(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-volumeReductionDecibels);
            //CAUTION DOES NOT WORK WITH OPENJDK AND PULSEAUDIO ON UBUNTU CORES
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clip;
    }

    public static int getXForCenterOfText(String text, Graphics2D graphics2D) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return GameManager.screenWidth / 2 - length / 2;
    }

    public static int getXForAlightToRightOfText(String text, int tailX, Graphics2D graphics2D) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return tailX - length;
    }


}

