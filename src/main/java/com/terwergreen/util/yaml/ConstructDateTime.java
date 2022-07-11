package com.terwergreen.util.yaml;

import com.terwergreen.constant.Constants;
import com.terwergreen.util.DateUtil;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @name: ConstructDateTime
 * @author: terwer
 * @date: 2022-07-11 21:01
 **/
public class ConstructDateTime extends SafeConstructor.ConstructYamlTimestamp {

    public Object construct(Node node) {
        Date date = (Date) super.construct(node);
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_CN_24);
        String datestr = sdf.format(date);

//        Date sdfDate = null;
//        try {
//            sdfDate = sdf.parse(datestr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return sdfDate;
        return datestr;
    }
}
