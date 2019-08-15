package com.starnetsecurity.common.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.starnetsecurity.common.exception.BizException;

import java.util.ArrayList;
import java.util.Set;

public class MCondition {
    private MRel rel;
    private ArrayList<DBObject> childCondition;

    public static MCondition create() {
        return new MCondition();
    }

    public static MCondition create(MRel rel) {
        return new MCondition(rel);
    }

    public static MCondition create(String key, String val) {
        return MCondition.create(MRel.all).append(key, val);
    }

    public MCondition() {
        this.rel = MRel.all;
        childCondition = new ArrayList();
    }

    public MCondition(MRel rel) {
        this.rel = rel;
        childCondition = new ArrayList();
    }

    public MRel getRel() {
        return rel;
    }

    public MCondition append(MCondition cc) {
        this.childCondition.add(cc.toDBObject());
        return this;
    }

    public MCondition append(String key, Object val) {
        this.childCondition.add(new BasicDBObject(key, val));
        return this;
    }


//    public DBObject toDBObject2() throws BizException {
//        DBObject conditions = new BasicDBObject();
//        for (Object cc : this.childCondition) {
//            if (cc instanceof BasicDBObject) {
//                BasicDBObject childCondition = (BasicDBObject) cc;
//                for (String key : childCondition.keySet()) {
//                    Object val = childCondition.get(key);
//                    if (MConditionParser.matchVal(this.rel, val)) {
//                        conditions.put(key, val);
//                    }
//                }
//                conditions.putAll(((BasicDBObject) cc).toMap());
//            } else if (cc instanceof MCondition) {
//                DBObject val = ((MCondition) cc).toDBObject();
//                conditions.putAll(val);
//            } else {
//                throw new BizException("unknown instanceof condition:" + cc.getClass());
//            }
//        }
//        if (this.rel.equals(MRel.all) == false) {
//            return MConditionParser.parseCondition(this.rel,conditions);
//        } else {
//           return conditions;
//        }
//    }

   public DBObject toDBObject(){
       switch (rel){
           case all:
           case and:
           case or:
               return new BasicDBObject(this.rel.getCondition(),this.childCondition);
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
               for(DBObject obj : this.childCondition){
                   Set<String> keySet = obj.keySet();
                   for(String key : keySet){
                       res.put(key,new BasicDBObject(rel.getCondition(),obj.get(key)));
                   }
               }
               return res;
           default:
               throw new BizException("unknown mrel"+rel.getCondition());
       }
   }


    public static void main(String[] args) throws Exception {
        MCondition mc = MCondition.create(MRel.or);
        mc.append("a","b");
        mc.append("b","1");
        System.out.printf(mc.toDBObject().toString());
    }
}
