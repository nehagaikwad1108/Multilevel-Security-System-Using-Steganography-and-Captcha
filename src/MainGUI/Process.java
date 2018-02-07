package MainGUI;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Process {

    int a, r, g, b, t, hi, wi, i, j, c;
    int length, ilen, h, w, size = 0, ext;
    int pixels[], red[], green[], blue[], alpha[], buf[], ipixels[], ia[], ir[], ig[], ib[], data[];
    Image img;
    ColorModel cm;
    String key, pdkey, extension;
    BufferedImage image;
    Runtime runtime = Runtime.getRuntime();
    public static boolean process = true;

    public Process(ImageIcon ico) {

        h = ico.getIconHeight();
        w = ico.getIconWidth();
        img = ico.getImage();

        pixels = new int[w * h];
        alpha = new int[w * h];
        red = new int[w * h];
        green = new int[w * h];
        blue = new int[w * h];

        try {
            PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, true);

            if (pg.grabPixels() == true) {
                cm = pg.getColorModel();

                size = 0;
                pixels = (int[]) pg.getPixels();
                for (j = 0; j < h; j++) {
                    for (i = 0; i < w; i++) {
                        alpha[size] = ((pixels[j * w + i] >> 24) & 0xff);
                        red[size] = ((pixels[j * w + i] >> 16) & 0xff);
                        green[size] = ((pixels[j * w + i] >> 8) & 0xff);
                        blue[size] = ((pixels[j * w + i]) & 0xff);
                        size++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Process Error::" + e.getMessage());
        }
        System.out.println("Total Memory : " + runtime.totalMemory() / 1024 + "KB");

    }

    public boolean setTextBuffer(int arr[]) {
        buf = arr;

        if (buf.length > red.length) {
            JOptionPane.showMessageDialog(new JFrame(), "Cannot Encrypt\n No Enough Space To Store Data ", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            key = GUI.txtkey2.getText();

            for (i = 0; i < 8; i++) {
                buf[i + 8] = key.charAt(i);
            }

            for (i = 0; i < buf.length; i++) {
                t = buf[i];
                r = t % 8;
                t = t / 8;
                g = t % 7;
                b = t / 7;

                red[i] = r + (red[i] - red[i] % 8);
                green[i] = g + (green[i] - green[i] % 8);
                blue[i] = b + (blue[i] - blue[i] % 8);
            }
            return true;
        }
    }

    public void setImage(ImageIcon ico, String extension) {
        int ext = 10;
        hi = ico.getIconHeight();
        wi = ico.getIconWidth();
        Process p = new Process(ico);
        length = buf.length;
        ilen = hi * wi;

        data = new int[5];

        if (extension.equalsIgnoreCase("jpg")) {
            ext = 15;
        }
        if (extension.equalsIgnoreCase("gif")) {
            ext = 20;
        }
        if (extension.equalsIgnoreCase("png")) {
            ext = 25;
        }

        int w1, w2, h1, h2;
        h1 = hi / 100;
        h2 = hi - h1 * 100;
        w1 = wi / 100;
        w2 = wi - w1 * 100;

        data[0] = ext;
        data[1] = h1;
        data[2] = h2;
        data[3] = w1;
        data[4] = w2;

        int ARGB[][] = p.getARGB();
        ia = ARGB[0];
        ir = ARGB[1];
        ig = ARGB[2];
        ib = ARGB[3];

        System.out.println("Length:  " + wi * hi + "   Height: " + hi + "   Width: " + wi + "   Extension: " + ext + "  " + extension);

        try {
            for (i = 3; i < 8; i++) {

                t = data[i - 3];
                r = t % 8;
                t = t / 8;
                g = t % 7;
                b = t / 7;

                red[i] = r + (red[i] - red[i] % 8);
                green[i] = g + (green[i] - green[i] % 8);
                blue[i] = b + (blue[i] - blue[i] % 8);
            }

            if ((ilen + length) < (red.length / 3)) {
                System.out.println("High Quality");
                c = 0;
                for (j = length; j < (length + ilen * 3); j = j + 3) {
                    for (i = 0; i < 3; i++) {
                        t = ARGB[i + 1][c];

                        r = t % 8;
                        t = t / 8;
                        g = t % 7;
                        b = t / 7;

                        red[i + j] = r + (red[i + j] - red[i + j] % 8);
                        green[i + j] = g + (green[i + j] - green[i + j] % 8);
                        blue[i + j] = b + (blue[i + j] - blue[i + j] % 8);
                    }
                    c++;
                }
            } else if ((ilen + length) < (red.length / 2) && ext == 20) {
                JOptionPane.showMessageDialog(new JFrame(), "You may loss Information !\n select smaller Image", "Medium Quality", JOptionPane.WARNING_MESSAGE);
                System.out.println("Medium Quality");
                String sal, sre, sgr, sbl;
                double da, dr, dg, db;
                int t = 0;

                c = 0;

                for (j = length; j < (length + ilen * 2); j = j + 2) {

                    for (i = 0; i < 2; i++) {
                        if (i > 0) {
                            t = 2;
                        } else {
                            t = 0;
                        }
                        //System.out.print(i+"   "+ir[c]+"   "+ig[c]+"   "+ib[c]+"         ");

                        da = (double) ia[c] / 32;
                        dr = (double) ir[c] / 32;
                        dg = (double) ig[c] / 32;
                        db = (double) ib[c] / 32;

                        //System.out.print(i+"   "+dr+"   "+dg+"   "+db+"         ");
                        sal = "" + da;
                        sre = "" + dr;
                        sgr = "" + dg;
                        sbl = "" + db;

                        //System.out.print(sre+"   "+sgr+"   "+sbl+"        ");
                        a = Integer.parseInt("" + sal.charAt(t));
                        r = Integer.parseInt("" + sre.charAt(t));
                        g = Integer.parseInt("" + sgr.charAt(t));
                        b = Integer.parseInt("" + sbl.charAt(t));

                        /*
                         a=((int)(sal.charAt(t)))-48;
                         r=((int)(sre.charAt(t)))-48;
                         g=((int)(sgr.charAt(t)))-48;
                         b=((int)(sbl.charAt(t)))-48;
                         */
                        alpha[j + i] = (alpha[j + i] - (alpha[j + i] % 8)) + a;
                        red[j + i] = (red[j + i] - (red[j + i] % 8)) + r;
                        green[j + i] = (green[j + i] - (green[j + i] % 8)) + g;
                        blue[j + i] = (blue[j + i] - (blue[j + i] % 8)) + b;

                        if (alpha[j + i] > 255) {
                            alpha[j + i] = 255;
                        }

                        if (red[j + i] > 255) {
                            red[j + i] = 255;
                        }

                        if (green[j + i] > 255) {
                            green[j + i] = 255;
                        }

                        if (blue[j + i] > 255) {
                            blue[j + i] = 255;
                        }
                        //System.out.println(t+"  "+r+"   "+g+"   "+b);
                    }
                    c++;
                }

            } else if ((ilen + length) < (red.length) && ext == 20) {

                JOptionPane.showMessageDialog(new JFrame(), "You may loss Information !\n select smaller Image", "Low Quality", JOptionPane.WARNING_MESSAGE);
                System.out.println("Low Quality");

                for (i = length; i < ir.length + length; i++) {

                    a = (int) ((ia[i - length]) / 32);
                    r = (int) ((ir[i - length]) / 32);
                    g = (int) ((ig[i - length]) / 32);
                    b = (int) ((ib[i - length]) / 32);

                    //System.out.println(ia[i-length]+"  "+ir[i-length]+"  "+ig[i-length]+"  "+ib[i-length]);
                    alpha[i] = a + (alpha[i] - alpha[i] % 8);
                    red[i] = r + (red[i] - red[i] % 8);
                    green[i] = g + (green[i] - green[i] % 8);
                    blue[i] = b + (blue[i] - blue[i] % 8);
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Cannot Encrypt\n No Enough Space To Store Image ", "Error", JOptionPane.ERROR_MESSAGE);
                process = false;
            }
        } catch (HeadlessException | NumberFormatException e) {
            System.out.println("Encrypt Error: " + e.getMessage());
        }
        runtime.gc();
        long l = runtime.freeMemory();
        System.out.println("Free Memory " + l / 1024 + "KB");
    }

    public ImageIcon getImage() {

        int ext;
        int data[] = new int[8];

        for (i = 0; i < 8; i++) {
            r = red[i] % 8;
            g = green[i] % 8;
            b = blue[i] % 8;
            data[i] = (((b * 7) + g) * 8) + r;
        }
        length = (data[0] * 10000 + data[1] * 100 + data[2]) + 16;

        ext = data[3];

        if (ext == 15) {
            extension = "jpg";
        }
        if (ext == 20) {
            extension = "gif";
        }
        if (ext == 25) {
            extension = "png";
        }

        hi = data[4] * 100 + data[5];
        wi = data[6] * 100 + data[7];

        ilen = hi * wi;

        System.out.println("Length: " + length + "Height: " + hi + "Width: " + wi + "Extension: " + ext + "  " + extension + "  ImageBufferLength" + wi * hi);

        ia = new int[hi * wi];
        ir = new int[hi * wi];
        ig = new int[hi * wi];
        ib = new int[hi * wi];
        ipixels = new int[hi * wi];

        if ((length + ilen) < (red.length / 3)) {
            System.out.println("High Quality");

            c = 0;
            int temp[][] = new int[3][ilen];

            for (i = length; i < (ilen * 3 + length); i = i + 3) {
                for (j = 0; j < 3; j++) {
                    r = red[i + j] % 8;
                    g = green[i + j] % 8;
                    b = blue[i + j] % 8;

                    temp[j][c] = (((b * 7) + g) * 8) + r;
                }
                ia[c] = 255;
                c++;
            }
            ir = temp[0];
            ig = temp[1];
            ib = temp[2];

        } else if ((hi * wi) + length < (h * w / 2)) {

            System.out.println("Medium Quality");

            c = 0;

            for (i = length; i < (length + ilen * 2); i = i + 2) {

                double da, dr, dg, db;
                int a1, r1, g1, b1, a2, r2, g2, b2;

                a1 = alpha[i] % 8;
                r1 = red[i] % 8;
                g1 = green[i] % 8;
                b1 = blue[i] % 8;

                a2 = alpha[i + 1] % 8;
                r2 = red[i + 1] % 8;
                g2 = green[i + 1] % 8;
                b2 = blue[i + 1] % 8;

                da = (a1 + a2 * 0.1) * 32;
                dr = (r1 + r2 * 0.1) * 32;
                dg = (g1 + g2 * 0.1) * 32;
                db = (b1 + b2 * 0.1) * 32;

                ia[c] = (int) da;
                ir[c] = (int) dr;
                ig[c] = (int) dg;
                ib[c] = (int) db;

                c++;
            }

        } else {
            System.out.println("Low Quality");
            for (i = length; i < (length + ilen); i++) {

                a = alpha[i] % 8;
                r = red[i] % 8;
                g = green[i] % 8;
                b = blue[i] % 8;

                a = ((a + 1) * 32) - 1;
                r = ((r + 1) * 32) - 1;
                g = ((g + 1) * 32) - 1;
                b = ((b + 1) * 32) - 1;

                if (a < 33) {
                    a = 0;
                }
                if (r < 33) {
                    r = 0;
                }
                if (g < 33) {
                    g = 0;
                }
                if (b < 33) {
                    b = 0;
                }

                ia[i - length] = a;
                ir[i - length] = r;
                ig[i - length] = g;
                ib[i - length] = b;
            }
        }

        size = 0;

        for (j = 0; j < hi; j++) {
            for (i = 0; i < wi; i++) {
                int w = (ia[size] << 24) & 0xff000000;
                int x = (ir[size] << 16) & 0x00ff0000;
                int y = (ig[size] << 8) & 0x0000ff00;
                int z = (ib[size]) & 0x000000ff;
                ipixels[j * wi + i] = w | x | y | z;
                size++;
            }
        }
        BufferedImage bufimg = new BufferedImage(wi, hi, BufferedImage.TYPE_INT_ARGB);
        bufimg.setRGB(0, 0, wi, hi, ipixels, 0, wi);
        ImageIcon icon = new ImageIcon(bufimg);
        return icon;
    }

    public String getExtension() {
        return extension;
    }

    public ImageIcon mergeData() {
        size = 0;

        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                a = (alpha[size] << 24) & 0xff000000;
                r = (red[size] << 16) & 0x00ff0000;
                g = (green[size] << 8) & 0x0000ff00;
                b = (blue[size]) & 0x000000ff;
                pixels[j * w + i] = a | r | g | b;
                size++;
            }
        }
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, w, h, pixels, 0, w);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }

    public int[] getBuffer() {
        char str;
        String key = new String();
        int len;
        int ch;
        int x[] = new int[3];

        for (i = 0; i < 3; i++) {
            r = red[i] - (red[i] - red[i] % 8);
            g = green[i] - (green[i] - green[i] % 8);
            b = blue[i] - (blue[i] - blue[i] % 8);
            x[i] = (((b * 7) + g) * 8) + r;
        }
        len = x[0] * 10000 + x[1] * 100 + x[2];

        System.out.println("len : " + len);
        int chbuf[] = new int[len - 16];

        for (i = 8; i < 16; i++) {
            r = red[i] - (red[i] - red[i] % 8);
            g = green[i] - (green[i] - green[i] % 8);
            b = blue[i] - (blue[i] - blue[i] % 8);
            ch = (((b * 7) + g) * 8) + r;
            str = (char) ch;
            key = key + str;
        }

        pdkey = GUI.txtkey1.getText();
        pdkey = pdkey.substring(0, 8);

        if (pdkey.equalsIgnoreCase(key)) {
            for (i = 16; i < len; i++) {

                r = red[i] - (red[i] - red[i] % 8);
                g = green[i] - (green[i] - green[i] % 8);
                b = blue[i] - (blue[i] - blue[i] % 8);

                ch = (((b * 7) + g) * 8) + r;
                chbuf[i - 16] = ch;
            }
            return chbuf;
        } else {
            return null;
        }
    }

    public int[] getpixels() {
        return pixels;
    }

    public int[][] getARGB() {
        int[][] ARGB = new int[4][w * h];
        ARGB[0] = alpha;
        ARGB[1] = red;
        ARGB[2] = green;
        ARGB[3] = blue;

        return ARGB;
    }

    public void saveImage(ImageIcon ico, String path, String type) {
        try {
            File file = new File(path);

            file = new File(path + ".png");

            Image img = ico.getImage();

            if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("gif")) {
                image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            } else {
                image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            }

            if (type.equalsIgnoreCase("gif")) {
                type = "jpg";
            }

            image.setRGB(0, 0, w, h, pixels, 0, w);
            ImageIO.write(image, type, file);

            /*				
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
             JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
             Graphics2D graphics = image.createGraphics();
             graphics.drawImage(img, 0, 0, w, h, null);
             encoder.encode(image);
             */
            runtime.gc();
            long l = runtime.freeMemory();
            System.out.println("Free Memory " + l / 1024 + "KB");
        } catch (IOException io) {
            System.out.println("Save ERROR:" + io.getMessage());
        }
    }
}
