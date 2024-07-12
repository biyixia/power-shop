package com.bjpowernode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ApplicationTest {
    //注入redis模板类
    //RedisTemplate，操作类型是Object，以二进制的方式将数据存储到Redis中
    //StringRedisTemplate，操作类型是String，将数据存储到Redis中
    //五种数据类型：String List Hash Set ZSet
    //opsForValue opsForList opsForHash opsForSet opsForZSet

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        System.out.println(stringRedisTemplate);
    }

    @Test
    void testOpsString() {
        //获取String类型的ops对象
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        //新增数据
        operations.set("username", "zhangsan");
        operations.set("name", "lisi", 5, TimeUnit.SECONDS);
        //单独设置过期时间
        stringRedisTemplate.expire("username", 5, TimeUnit.SECONDS);
        //批量查询值
        operations.multiGet(
                Arrays.asList(
                        "name",
                        "username"
                )
        ).forEach(System.out::println);
        System.out.println(stringRedisTemplate.getExpire("username"));
        stringRedisTemplate.delete("name");
    }

    @Test
    void testOpsList() {
        //获取list的ops对象
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        //左圧栈，先进后出，后进先出
        listOperations.leftPushAll("nameList", "zhangsan", "lisi", "wangwu", "zhaoliu");
        //右压栈，先进先出，后进后出
        listOperations.rightPushAll("usernameList", "zhangsan", "lisi", "wangwu", "zhaoliu");
        //查询数据列表
        listOperations.range("nameList", 0, -1).forEach(System.out::println);
        listOperations.range("usernameList", 0, -1).forEach(System.out::println);
        //设置过期时间
        stringRedisTemplate.expire("nameList", 5, TimeUnit.MINUTES);
        System.out.println("--------------------------------------------------------------------");
        //删除指定元素
        listOperations.remove("nameList", 2, "zhangsan");
        //查询列表数据
        listOperations.range("nameList", 0, -1).forEach(System.out::println);
        listOperations.range("usernameList", 0, -1).forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------");
        //替换头部元素
        listOperations.set("nameList", 0, "zhangsan");
        //查询列表数据
        listOperations.range("nameList", 0, -1).forEach(System.out::println);
        listOperations.range("usernameList", 0, -1).forEach(System.out::println);
    }

    @Test
    public void testOpsHash() {
        //获取ops的hash对象
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        //设置hash的键值对
        hashOperations.put("user", "loginAct", "zhangsan");
        hashOperations.put("user", "loginPwd", "123123");
        hashOperations.put("user", "address", "深圳市");
        //查询列表数据并输出到控制台
        //查询所有key及value集合
        Set<Object> user = hashOperations.keys("user");
        user.forEach(
                field ->
                        System.out.println(
                                "field:" + field + ",value:" + hashOperations.get("user", field)
                        )
        );
        //获取所有的field和value的map集合
        hashOperations.entries("user").entrySet().forEach(
                entry -> System.out.println("field:" + entry.getKey() + ",value:" + entry.getValue())
        );
        //删除集合中的元素
        hashOperations.delete("user","address");
        //删除集合.
        stringRedisTemplate.delete("user");
    }

    @Test
    public void testOpsSet() {
        //获取set集合的ops对象
        //set集合是无序、唯一的
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        //新增操作
        setOperations.add("userList", "zhangsan", "lisi", "wangwu", "zhaoliu");
        //查询数据
        setOperations.members("userList").forEach(System.out::println);
        //弹出操作
        System.out.println(setOperations.pop("userList"));
        //查询集合长度
        System.out.println(setOperations.size("userList"));
        //查询value是否存在
        System.out.println(setOperations.isMember("userList", "lisi"));
        //随机返回集合中的元素（不删除该元素）
        System.out.println(setOperations.randomMember("userList"));
        //删除集合中的元素
        setOperations.remove("userList", "lisi");
    }

    @Test
    public void testOpsZSet() {
        //获取zset集合的ops对象
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> zSet = new HashSet<>();
        zSet.add(new DefaultTypedTuple<String>("zhangsan", 100.00));
        zSet.add(new DefaultTypedTuple<String>("lisi", 200.00));
        zSet.add(new DefaultTypedTuple<String>("wangwu", 300.00));
        zSet.add(new DefaultTypedTuple<String>("zhaoliu", 400.00));
        //新增操作
        zSetOperations.add("nameList", zSet);
        //查询并输出(升序)
        zSetOperations.range("nameList", 0, -1).forEach(System.out::println);
        //降序
        zSetOperations.reverseRange("nameList", 0, -1).forEach(System.out::println);
        System.out.println("---------------------------------");
        //查询并升序输出指定分数内的集合数据
        zSetOperations.rangeByScore("nameList", 100, 200).forEach(System.out::println);
        //查询并升序输出指定分数内的集合
        zSetOperations.rangeByScoreWithScores("nameList", 100, 200).forEach(stringTypedTuple -> System.out.println(stringTypedTuple.getValue() + "," + stringTypedTuple.getScore()));
        System.out.println("---------------------------------");
        //查询并降序输出指定分数内的集合数据
        zSetOperations.reverseRangeByScore("nameList", 100, 200).forEach(System.out::println);
        //查询并降序输出指定分数内的集合
        zSetOperations.reverseRangeByScoreWithScores("nameList", 100, 200).forEach(stringTypedTuple -> System.out.println(stringTypedTuple.getValue() + "," + stringTypedTuple.getScore()));
    }
}