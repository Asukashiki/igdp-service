package com.inspur.common.utils.file;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inspur.common.config.SystemConfig;
import com.inspur.common.constant.Constants;
import com.inspur.common.utils.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 图片处理工具类
 *
 * @author liyunlong
 */
public class ImageUtils
{
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    public static byte[] getImage(String imagePath)
    {
        InputStream is = getFile(imagePath);
        try
        {
            return IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {
            log.error("图片加载异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath)
    {
        try
        {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        }
        catch (Exception e)
        {
            log.error("获取图片异常 {}", e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     * 
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url)
    {
        InputStream in = null;
        try
        {
            if (url.startsWith("http"))
            {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            }
            else
            {
                // 本机地址
                String localPath = SystemConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        }
        catch (Exception e)
        {
            log.error("获取文件路径异常 {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(in);
        }
    }


    /**
     *
     * 改变图片尺寸
     *
     */
    public static InputStream changeSize(InputStream imageStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedImage target = null;

        int maxWidth = 1334;
        int maxHeight = 750;

        try {
            BufferedImage bufferedImage = ImageIO.read(imageStream);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // 如果图片的尺寸没有超过规定的尺寸，直接进入图片大小的处理
            if (width <= maxWidth && height <= maxHeight) {
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                return compressSize(byteArrayOutputStream.toByteArray());
            }

            double widthScale = maxWidth * 1.0 / width;
            double heightScale = maxHeight * 1.0 / height;

            if (widthScale > heightScale) {
                widthScale = heightScale;
                maxWidth = (int) (widthScale * width);
            } else {
                heightScale = widthScale;
                maxHeight = (int) (heightScale * height);
            }
            int type = bufferedImage.getType();

            if (type == BufferedImage.TYPE_CUSTOM) {
                ColorModel colorModel = bufferedImage.getColorModel();
                WritableRaster writableRaster = colorModel.createCompatibleWritableRaster(maxWidth, maxHeight);
                boolean alphaPremultiplied = colorModel.isAlphaPremultiplied();
                target = new BufferedImage(colorModel, writableRaster, alphaPremultiplied, null);
            } else {
                target = new BufferedImage(maxWidth, maxHeight, type);
            }
            Graphics2D graphics2D = target.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.drawRenderedImage(bufferedImage, AffineTransform.getScaleInstance(widthScale, heightScale));
            graphics2D.dispose();

            ImageIO.write(target, "png", byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return compressSize(byteArrayOutputStream.toByteArray());
    }

    /**
     *
     * 改变图片大小
     *
     */
    public static InputStream compressSize(byte[] imageBytes) {
        long maxSize = 1024 * 1024;

        // 如果图片的大小没有超过规定的大小，则不做处理
        if (imageBytes.length <= 0 || imageBytes.length <= maxSize) {
            return new ByteArrayInputStream(imageBytes);
        }

        double accuracy = getAccuracy(imageBytes.length / 1024);

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream(imageBytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

            Thumbnails.of(byteArrayInputStream)
                    .scale(accuracy)
                    .outputQuality(accuracy)
                    .toOutputStream(byteArrayOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     *
     * 自动调节精度(经验数值)
     *
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }


    /**
     * 给图片添加水印、可设置水印图片旋转角度
     * @param iconPath   水印图片路径
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     * @param width      宽度(与左相比)
     * @param height     高度(与顶相比)
     * @param clarity    透明度(小于1的数)越接近0越透明
     */
    public static void waterMarkImageByIcon(String iconPath, String srcImgPath,
                                            String targerPath, Integer degree, Integer width, Integer height,
                                            float clarity) {
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));
            System.out.println("width:" + srcImg.getWidth(null));
            System.out.println("height:" + srcImg.getHeight(null));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);
            // 得到Image对象。
            Image img = imgIcon.getImage();
            // 透明度
            float alpha = clarity;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 表示水印图片的位置
            g.drawImage(img, width, height, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            os = Files.newOutputStream(Paths.get(targerPath));
            // 生成图片
            ImageIO.write(buffImg, "JPG", os);
            System.out.println("添加水印图片完成!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片添加水印、可设置水印图片旋转角度
     * @param logoText   水印文字
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     * @param degree     水印图片旋转角度
     * @param width      宽度(与左相比)
     * @param height     高度(与顶相比)
     * @param clarity    透明度(小于1的数)越接近0越透明
     */
    public static void waterMarkByText(String logoText, String srcImgPath,
                                       String targerPath, Integer degree, Integer width, Integer height,
                                       Float clarity) {
        // 主图片的路径
        InputStream is = null;
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            // Graphics g= buffImg.getGraphics();
            Graphics2D g = buffImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            // 设置颜色
            g.setColor(Color.red);
            // 设置 Font
            g.setFont(new Font("宋体", Font.BOLD, 30));
            float alpha = clarity;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
            g.drawString(logoText, width, height);
            g.dispose();
            os = Files.newOutputStream(Paths.get(targerPath));
            // 生成图片
            ImageIO.write(buffImg, "JPG", os);
            System.out.println("添加水印文字完成!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
