package com.terwergreen.model.control;

/**
 * 用于绑定数据项的键值对集合
 *
 * @name: KeyValueItem
 * @author: terwer
 * @date: 2022-03-05 14:19
 **/
public class KeyValueItem<K, V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
