package cn.ChengZhiYa;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class multi {
    public static String RandomText() {
        List<String> StringList = new ArrayList<>();
        StringList.add("5x8=35");
        StringList.add("5x8=42");
        StringList.add("3x5=16");
        StringList.add("7x9=null");
        StringList.add("5x6=35");
        StringList.add("2x9=12");
        StringList.add("4x9=54");
        StringList.add("5x9=35");
        StringList.add("平板打快板，这个好欸");
        StringList.add("「我不玩了！」");
        StringList.add("「我真的服了！」");
        StringList.add("「我要把你们都sa了！」");
        StringList.add("飞了飞了");
        StringList.add("笑死了，什么V什么粉");
        StringList.add("小皮面板比你那破宝塔好用多了");
        StringList.add("1000Mbps带宽跑不满50Mbps上行");
        StringList.add("v我50");
        Random random = new Random();
        return StringList.get(random.nextInt(StringList.size()));
    }
    public static String getDate() {
        LocalDate Date = LocalDate.now();
        return Date.getYear() + "年" + Date.getMonthValue() + "月" + Date.getDayOfMonth() + "日";
    }
    public static String getDay() {
        LocalDate Date = LocalDate.now();
        String Day = "";
        switch (Date.getDayOfWeek().getValue()) {
            case 1:
                Day = "星期一";
                break;
            case 2:
                Day = "星期二";
                break;
            case 3:
                Day = "星期三";
                break;
            case 4:
                Day = "星期四";
                break;
            case 5:
                Day = "星期五";
                break;
            case 6:
                Day = "星期六";
                break;
            case 7:
                Day = "星期日";
                break;
        }
        return Day;
    }
    public static String getTime() {
        LocalDateTime Time = LocalDateTime.now();
        String Hour = String.valueOf(Time.getHour());
        String Minute = String.valueOf(Time.getMinute());
        if (Hour.length() == 1) {
            Hour = "0" + Hour;
        }
        if (Minute.length() == 1) {
            Minute = "0" + Minute;
        }
        return  Hour + ":" + Minute;
    }
    public static String getHitokoto(){
        try {
            URL url = new URL("https://v1.hitokoto.cn/");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JSONObject Json = JSONObject.parseObject(reader.readLine());
            return Json.getString("hitokoto");
        } catch (Exception e) {
            return "呜呜呜,一言服务器炸了!";
        }
    }
    public static String ServerJSON(String IP) {
        try {
            URL url = new URL("https://api.serendipityr.cn/mcapi/?host=" + IP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean getServerOnline(String ServerJSON) {
        try {
            JSONObject Json = JSONObject.parseObject(ServerJSON);
            return Json.getString("msg") == null;
        } catch (Exception e) {
            return false;
        }
    }
    public static int[] GetServerOnlinePlayer(String ServerJSON) {
        try {
            JSONObject Json = JSONObject.parseObject(JSONObject.parseObject(ServerJSON).getString("players"));
            return new int[]{Json.getIntValue("online"),Json.getIntValue("max")};
        } catch (Exception e) {
            return new int[]{0,0};
        }
    }
    public static boolean CheckBlackWord(String Message) {
        List<String> BlackWordList = new ArrayList<>();
        BlackWordList.add("傻逼");
        BlackWordList.add("滚");
        BlackWordList.add("辱骂");
        BlackWordList.add("SB");
        BlackWordList.add("妈");
        BlackWordList.add("爸");
        BlackWordList.add("死");
        BlackWordList.add("nm");
        BlackWordList.add("辣鸡");
        BlackWordList.add("导管");
        BlackWordList.add("钢管");
        BlackWordList.add("XXOO");
        BlackWordList.add("OO");
        BlackWordList.add("牛子");
        BlackWordList.add("精液");
        BlackWordList.add("射出");
        BlackWordList.add("射精");
        BlackWordList.add("操");
        BlackWordList.add("艹");
        BlackWordList.add("颜色");
        BlackWordList.add("涉黄");
        BlackWordList.add("黄色");
        BlackWordList.add("泡");
        BlackWordList.add("妞");
        BlackWordList.add("买");
        BlackWordList.add("卖");
        BlackWordList.add("配置");
        BlackWordList.add("领奖");
        BlackWordList.add("获奖");
        BlackWordList.add("免单");
        BlackWordList.add("点击有惊喜");
        BlackWordList.add("点击领取");
        BlackWordList.add("秒杀");
        BlackWordList.add("抢爆");
        BlackWordList.add("疯抢");
        BlackWordList.add("国家");
        BlackWordList.add("航天级");
        BlackWordList.add("法律");
        BlackWordList.add("行政");
        BlackWordList.add("手机号");
        BlackWordList.add("验证码");
        BlackWordList.add("杀");
        BlackWordList.add("交易");
        BlackWordList.add("账号");
        BlackWordList.add("密码");
        for (String BlackWord : BlackWordList) {
            if (Message.contains(BlackWord)) {
                return true;
            }
        }
        return false;
    }
}
