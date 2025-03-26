package com.rpc.core.invoker.route.impl;

import com.rpc.core.register.entity.RegisterInstance;
import com.rpc.core.invoker.route.RpcLoadBalance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 一致性哈希算法
 */
public class RpcLoadBalanceConsistentHash extends RpcLoadBalance {

    //权重
    private int weight = 100;


    @Override
    public RegisterInstance route(String serviceKey, TreeSet<RegisterInstance> instanceTreeSet) {
        return doRoute(serviceKey,instanceTreeSet);
    }

    public RegisterInstance doRoute(String serviceKey, TreeSet<RegisterInstance> instanceTreeSet) {
        //计算虚拟节点的hash与实例的映射关系,初始化100个
        TreeMap<Long, RegisterInstance> addressRing = new TreeMap<>();
        for (RegisterInstance instance : instanceTreeSet) {
            for (int i = 0; i < weight; i++) {
                long addressHash = hash("SHARD-" + instance.getUniqueKey() + "-NODE-" + i);
                addressRing.put(addressHash, instance);
            }
        }

        long serviceKeyHash = hash(serviceKey);
        SortedMap<Long, RegisterInstance> lastRing = addressRing.tailMap(serviceKeyHash);
        if (!lastRing.isEmpty()) {
            return lastRing.get(lastRing.firstKey());
        }

        return addressRing.firstEntry().getValue();
    }


    /**
     * md5散列的方式计算hash值
     */
    private long hash(String key) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }

        md5.reset();
        byte[] keyBytes;
        keyBytes = key.getBytes(StandardCharsets.UTF_8);
        md5.update(keyBytes);
        byte[] digest = md5.digest();

        long hashCode = ((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF);

        return hashCode & 0xffffffffL;
    }
}
