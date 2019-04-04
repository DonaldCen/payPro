package yx.pay.common.utils;

import java.util.*;

/**
 * @Description
 * @Author jiangpei
 * @Date 2019/4/4
 * @Version 1.0.0
 */
public class PollUtil {

    //队列
    private static final List<String> list = new ArrayList<>();

    //当前下标
    private static int index = 0;

    //设置集合，先清空原来集合中的所有数据，再新增新的数据
    public static synchronized void setAll(List<String> srcList) {
        list.clear();
        list.addAll(srcList);
    }

    //添加单个数据
    public static synchronized void add(String data) {
        list.add(data);
    }

    //获取一个集合中的对象
    public static synchronized String next() {
        if (list.isEmpty()) {
            return null;
        }
        int tmpIndex = index;
        if(tmpIndex < list.size()-1) {
            index++;
        }
        else {
            index = 0;
        }
        return list.get(tmpIndex);
    }
}
