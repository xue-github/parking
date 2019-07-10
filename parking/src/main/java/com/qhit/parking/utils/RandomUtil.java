package com.qhit.parking.utils;

import java.util.Random;

/**
 * Created by Administrator on 2018/10/28 0028.
 */
public class RandomUtil {

    public static void main(String[] args) {
//        String username = generateUsername();
//        String name = generateName();
//        String addr = generateAddr();
        String phone = generatePhone();
//        String email = generateEamil();
        System.out.println(phone);
    }

    public static String generateEamil() {
        String result = "";
        Random random = new Random();
        String[] arr = {"0","1","2","3","4","5","6","7","8","9"};
        for(int i=1;i<=10;i++){
            result+=arr[random.nextInt(arr.length)];
        }
        return  result+"@qq.com";
    }

    public static String generatePhone() {
        String result = "139";
        Random random = new Random();
        String[] arr = {"0","1","2","3","4","5","6","7","8","9"};
        for(int i=1;i<=8;i++){
            result+=arr[random.nextInt(arr.length)];
        }
        return  result;
    }

    public static String generateAddr() {
        String[] arr = {"北京","上海","天津","重庆","黑龙江","吉林","辽宁","江苏","山东","安徽","河北","河南","湖北","湖南","江西","陕西","山西","四川","青海",
                "海南","广东","贵州","浙江","福建","甘肃","云南","内蒙古","宁夏","新疆","西藏","广西"};
        Random random = new Random();
        int x = random.nextInt(arr.length);
        return arr[x];
    }

    public static String generateName() {
        String[] xingarr = {"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张","孔","曹"};
        String[] mingarr = {"佳康","戈","刘涛","长江","圣洁","济森","万通","二康","俊成","文政","壮壮","哲","龙曦","玉峰","威杰","博","士煜","家豪","方为","起航","宇浩",
                            "彬彬","茂盛","志新","银星","俊辉","明洋","梦瑶","泗鑫","广慧","定宇","雯月","星辰","伟良","士纯","瑞超","淞丹","孟松","文迪","旗","留洋","文哲","耀威","俊宇",
                "庆伟","继光","祥顺","杰","光达","和璧","舞阳","小雨","静静","亚茹","欣","帅杰","于辉","永立","明春","利利","云梦","营营","少庆","年年","雨衡","天伟","迎阔","晨洁","俊燚","国鑫"};
        Random random = new Random();
        int x = random.nextInt(xingarr.length);
        int m = random.nextInt(mingarr.length);
        return xingarr[x]+mingarr[m];
    }

    //    4~8为的用户名 小写字母组成
    public static String generateUsername() {
        String[] arr = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        Random random = new Random();
        int num = random.nextInt(4)+4;
        String result = "";
        for(int i=1;i<=num;i++){
            int j = random.nextInt(arr.length);
            result+=arr[j];
        }
        return result;
    }





}
