package com.starnetsecurity.common.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.starnetsecurity.common.exception.BizException;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;


public class MConditionParser {

    public static boolean matchVal(MRel rel, Object val) throws BizException {
        switch (rel) {
            case and:
            case all:
            case or:
                return true;
            case exists:
                if (val instanceof Boolean == false) {
                    throw new BizException("$exists only accept boolean val");
                }
                return true;
            case gt:
            case gte:
            case lt:
            case lte:
                if (val instanceof Long || val instanceof Integer || val instanceof Float || val instanceof Timestamp
                        || val instanceof Date || val instanceof java.util.Date) {
                    return true;
                } else {
                    throw new BizException(rel.getCondition() + " only accept number or time val");
                }
            case in:
            case not:
                if (val instanceof Collection || val instanceof Iterable || val instanceof Iterator) {
                    return true;
                } else {
                    throw new BizException(rel.getCondition() + " only accept collection or iterator val");
                }
            case regex:
                if(val instanceof Pattern || val instanceof String){
                    return true;
                }else{
                    throw new BizException(rel.getCondition() + " only accept pattern or string val");
                }
            case search:
                if( val instanceof String){
                    return true;
                }else{
                    throw new BizException(rel.getCondition() + " only accept pattern or string val");
                }
                default:
                    throw new BizException("unknown mrel");
        }
    }

    public static DBObject parseCondition(MRel rel,DBObject obj) throws BizException {
        switch (rel){
            case all:
                return obj;
            case and:
            case or:
                    ArrayList<DBObject> orRes = new ArrayList<DBObject>();
                    for(String key : obj.keySet()){
                        orRes.add(new BasicDBObject(key,obj.get(key)));
                    }
                    return new BasicDBObject(rel.getCondition(),orRes);
            case gt:
            case gte:
            case lt:
            case lte:
            case in:
            case not:
            case exists:
            case regex:
            case search:
                DBObject res = new BasicDBObject();
                Set<String> keySet = obj.keySet();
                for(String key : keySet){
                    res.put(key,new BasicDBObject(rel.getCondition(),obj.get(key)));
                }
                return res;
            default:
                throw new BizException("unknown mrel"+rel.getCondition());
        }
    }
}
