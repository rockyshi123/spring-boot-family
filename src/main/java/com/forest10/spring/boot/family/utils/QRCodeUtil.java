package com.forest10.spring.boot.family.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Forest10
 * @date 2019-02-14 18:29
 */
public class QRCodeUtil {

    private static final String CHARSET = Charset.defaultCharset().name();
    private static final String FORMAT = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int LOGO_WIDTH = 60;
    // LOGO高度
    private static final int LOGO_HEIGHT = 60;

    private static BufferedImage createImage(String content, String logoPath)
        throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter()
            .encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (StringUtils.isBlank(logoPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, logoPath);
        return image;
    }

    /**
     * 插入LOGO
     *
     * @param source 二维码图片
     * @param logoPath LOGO图片地址
     */
    private static void insertImage(BufferedImage source, String logoPath)
        throws Exception {
        File file = new File(logoPath);
        if (!file.exists()) {
            throw new Exception("logo file not found.");
        }
        Image src = ImageIO.read(new File(logoPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        //压缩logo
        if (width > LOGO_WIDTH) {
            width = LOGO_WIDTH;
        }
        if (height > LOGO_HEIGHT) {
            height = LOGO_HEIGHT;
        }
        Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        src = image;
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO) 二维码文件名随机
     *
     * @param content 内容
     * @param logoPath LOGO地址
     * @param destPath 存放目录
     */
    public static String encode(String content, String logoPath, String destPath) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath);
        mkdirs(destPath);
        String fileName = UUID.randomUUID().toString();
        File file = new File(destPath + "/" + fileName);
        ImageIO.write(image, FORMAT, file);
        return file.getAbsolutePath().concat(".").concat(FORMAT);
    }

    /**
     * 生成二维码(内嵌LOGO) 调用者指定二维码文件名
     *
     * @param content 内容
     * @param logoPath LOGO地址
     * @param destPath 存放目录
     * @param fileName 二维码文件名
     */
    private static String encode(String content, String logoPath, String destPath, String fileName)
        throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath);
        mkdirs(destPath);
        fileName = fileName
            .substring(0, fileName.indexOf(".") > 0 ? fileName.indexOf(".") : fileName.length())
            + "." + FORMAT.toLowerCase();
        File file = new File(destPath + "/" + fileName);
        ImageIO.write(image, FORMAT, file);
        return file.getAbsolutePath();
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir． (mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    private static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }


    /**
     * 生成二维码
     *
     * @param content 内容
     * @param destPath 存储地址
     */
    public static String encode(String content, String destPath) throws Exception {
        return QRCodeUtil.encode(content, null, destPath);
    }


    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content 内容
     * @param logoPath LOGO地址
     * @param output 输出流
     */
    public static void encodeWithLogo(String content, String logoPath, OutputStream output)
        throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoPath);
        ImageIO.write(image, FORMAT, output);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param output 输出流
     */
    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encodeWithLogo(content, StringUtils.EMPTY, output);
    }

    /**
     * 解析二维码
     *
     * @param filePath 二维码图片地址
     */
    public static String decode(String filePath) throws Exception {
        return QRCodeUtil.decode(new File(filePath));
    }


    /**
     * 解析二维码
     *
     * @param file 二维码图片
     */
    private static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return StringUtils.EMPTY;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }


}
