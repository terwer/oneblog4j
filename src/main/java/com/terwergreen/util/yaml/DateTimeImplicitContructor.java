package com.terwergreen.util.yaml;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * @name: DatetimeConstructor
 * @author: terwer
 * @date: 2022-07-11 20:12
 **/
public class DateTimeImplicitContructor extends Constructor {
    public DateTimeImplicitContructor() {
        this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructDateTime());
    }
}
