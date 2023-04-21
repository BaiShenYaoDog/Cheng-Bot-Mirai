package cn.ChengZhiYa.Listener;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.utils.ExternalResource;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.FormatUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import static cn.ChengZhiYa.ChengBot.INSTANCE;
import static cn.ChengZhiYa.multi.*;

public class FriendMessage implements ListenerHost {
    @EventHandler
    public void onEvent(GroupMessageEvent event) throws InterruptedException, IOException {
        if (event.getMessage().contentToString().equals("菜单")) {
            try {
                File BackgroundImgFile = new File(INSTANCE.getDataFolder() + "\\功能列表底图.png");
                BufferedImage Img = ImageIO.read(BackgroundImgFile);
                Graphics2D graphics2D = Img.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);
                graphics2D.setColor(new Color(60,144,255));
                graphics2D.setFont(new Font("微软雅黑", Font.BOLD, 27));
                graphics2D.drawString(RandomText(),50,440);
                graphics2D.drawString(getDate() + " " + getDay() + " " + getTime(),50,480);
                graphics2D.dispose();
                ByteArrayOutputStream ImgByte = new ByteArrayOutputStream();
                ImageIO.write(Img,"png",ImgByte);
                ExternalResource ImgRes = ExternalResource.create(ImgByte.toByteArray());
                event.getSubject().sendMessage(event.getSubject().uploadImage(ImgRes));
                ImgRes.close();
                ImgByte.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getMessage().contentToString().equals("运行状态") || event.getMessage().contentToString().equals("机器人状态")) {
            SystemInfo systemInfo = new SystemInfo();
            int Mem = Integer.parseInt(new DecimalFormat("#").format(Double.parseDouble(FormatUtil.formatBytes((systemInfo.getHardware().getMemory().getTotal()-systemInfo.getHardware().getMemory().getAvailable())).replaceAll(" GiB","").replaceAll(" MiB",""))/Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()).replaceAll(" GiB","").replaceAll(" MiB","")) * 100));
            int PageMem = Integer.parseInt(new DecimalFormat("#").format(100 - Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()-systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()).replaceAll(" GiB","").replaceAll(" MiB",""))/Double.parseDouble(FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()).replaceAll(" GiB","").replaceAll(" MiB","")) * 100));
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            TimeUnit.SECONDS.sleep(1);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                    - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                    - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                    - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                    - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
            int CPUUsage = Math.round(Float.parseFloat(new DecimalFormat("#.##").format(100 - idle * 1.0 / totalCpu * 100)));
            if (CPUUsage > 100) {
                CPUUsage = 100;
            }
            if (Mem > 100) {
                Mem = 100;
            }
            if (PageMem > 100) {
                PageMem = 100;
            }
            try {
                File BackgroundImgFile = new File(INSTANCE.getDataFolder() + "\\运行状态底图.png");
                BufferedImage Img = ImageIO.read(BackgroundImgFile);
                Graphics2D graphics2D = Img.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);

                graphics2D.setColor(new Color(0,0,0));
                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                graphics2D.drawString("Cheng-Bot | " + getHitokoto(),300,155);
                graphics2D.drawString(getDate() + " " + getDay() + " " + getTime(),300,175);

                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 25));
                graphics2D.drawString("CPU",120,360);
                graphics2D.drawString("物理内存",90,408);
                graphics2D.drawString("虚拟内存",90,458);

                graphics2D.setColor(new Color(237,90,101));
                graphics2D.fillRect(201,336,CPUUsage * 9, 28);
                graphics2D.fillRect(201,386,Mem * 9, 28);
                graphics2D.fillRect(201,436,PageMem * 9, 28);

                graphics2D.setColor(new Color(92,34,35));
                graphics2D.drawString(CPUUsage + "%",621,359);
                graphics2D.drawString(Mem + "%",621,409);
                graphics2D.drawString(PageMem + "%",621,459);

                graphics2D.setColor(new Color(237,90,101));
                graphics2D.setFont(new Font("微软雅黑", Font.PLAIN, 18));

                graphics2D.drawString("CPU:",130,500);
                graphics2D.drawString("CPU频率: 5.0GHz", 130,520);

                graphics2D.drawString("物理内存:",480,500);
                graphics2D.drawString("总内存: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()), 480,520);
                graphics2D.drawString("内存占用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getTotal()-systemInfo.getHardware().getMemory().getAvailable()), 480,540);
                graphics2D.drawString("内存可用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getAvailable()), 480,560);

                graphics2D.drawString("虚拟内存:",930,500);
                graphics2D.drawString("总内存: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()), 930,520);
                graphics2D.drawString("内存占用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()), 930,540);
                graphics2D.drawString("内存可用: " + FormatUtil.formatBytes(systemInfo.getHardware().getMemory().getVirtualMemory().getSwapTotal()-systemInfo.getHardware().getMemory().getVirtualMemory().getSwapUsed()), 930,560);

                graphics2D.dispose();
                ByteArrayOutputStream ImgByte = new ByteArrayOutputStream();
                ImageIO.write(Img,"png",ImgByte);
                ExternalResource ImgRes = ExternalResource.create(ImgByte.toByteArray());
                event.getSubject().sendMessage(event.getSubject().uploadImage(ImgRes));
                ImgRes.close();
                ImgByte.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event.getMessage().contentToString().equals("二次元图片")) {
            URL url = new URL("https://app.zichen.zone/api/acg.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            ByteArrayOutputStream ImgByte = new ByteArrayOutputStream();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = conn.getInputStream().read(bytes)) != -1) {
                ImgByte.write(bytes, 0, len);
            }
            ExternalResource ImgRes = ExternalResource.create(ImgByte.toByteArray());
            event.getSubject().sendMessage(event.getSubject().uploadImage(ImgRes));
            ImgRes.close();
        }
        if (event.getMessage().contentToString().equals("一言")) {
            String Hitokoto = getHitokoto();
            event.getSubject().sendMessage("[橙汁吖 | 一言]\n" + Hitokoto);
        }
        if (event.getMessage().contentToString().equals("服务器状态")) {
            if (event.getGroup().getId() == 801329619 || event.getGroup().getId() == 152848071) {
                try {
                    File BackgroundImgFile = new File(INSTANCE.getDataFolder() + "\\夜之粉服务器状态底图.png");
                    BufferedImage Img = ImageIO.read(BackgroundImgFile);
                    Graphics2D graphics2D = Img.createGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                    graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);
                    graphics2D.setFont(new Font("微软雅黑", Font.BOLD, 30));
                    String ServerJSON = ServerJSON("xiaoyeya.top");
                    if (getServerOnline(ServerJSON)) {
                        graphics2D.setColor(new Color(0,255,107));
                        graphics2D.drawString("[在线]",980,110);

                        graphics2D.setColor(new Color(241,68,29));
                        graphics2D.drawString("[" + GetServerOnlinePlayer(ServerJSON)[0] + "/" + GetServerOnlinePlayer(ServerJSON)[1] + "]",450,106);
                    }else {
                        graphics2D.setColor(new Color(240,128,128));
                        graphics2D.drawString("[离线]",980,110);
                    }

                    ByteArrayOutputStream ImgByte = new ByteArrayOutputStream();
                    ImageIO.write(Img,"png",ImgByte);
                    ExternalResource ImgRes = ExternalResource.create(ImgByte.toByteArray());
                    event.getSubject().sendMessage(event.getSubject().uploadImage(ImgRes));
                    ImgRes.close();
                    ImgByte.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (event.getGroup().getId() == 476308572 || event.getGroup().getId() == 801409169) {
                try {
                    File BackgroundImgFile = new File(INSTANCE.getDataFolder() + "\\梦回东方服务器状态底图.png");
                    BufferedImage Img = ImageIO.read(BackgroundImgFile);
                    Graphics2D graphics2D = Img.createGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
                    graphics2D.drawImage(Img, 0, 0, Img.getWidth(null), Img.getHeight(null) ,null);
                    graphics2D.setFont(new Font("微软雅黑", Font.BOLD, 30));
                    graphics2D.setColor(new Color(0,255,107));
                    String ServerJSON = ServerJSON("mcxy.love");
                    if (getServerOnline(ServerJSON)) {
                        graphics2D.setColor(new Color(0,255,107));
                        graphics2D.drawString("[在线]",980,110);

                        graphics2D.setColor(new Color(241,68,29));
                        graphics2D.drawString("[" + GetServerOnlinePlayer(ServerJSON)[0] + "/" + GetServerOnlinePlayer(ServerJSON)[1] + "]",570,106);
                    }else {
                        graphics2D.setColor(new Color(240,128,128));
                        graphics2D.drawString("[离线]",980,110);
                    }

                    ByteArrayOutputStream ImgByte = new ByteArrayOutputStream();
                    ImageIO.write(Img,"png",ImgByte);
                    ExternalResource ImgRes = ExternalResource.create(ImgByte.toByteArray());
                    event.getSubject().sendMessage(event.getSubject().uploadImage(ImgRes));
                    ImgRes.close();
                    ImgByte.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getGroup().getId() == 801329619 || event.getGroup().getId() == 152848071) {
            if (event.getSender().getId() != 292200693) {
                if (CheckBlackWord(event.getMessage().contentToString())) {
                    MessageSource.recall(event.getMessage());
                    event.getSender().mute(300);
                }
            }
        }
    }
}
