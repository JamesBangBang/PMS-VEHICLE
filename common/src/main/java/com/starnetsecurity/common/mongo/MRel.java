package com.starnetsecurity.common.mongo;

public enum  MRel {
    and("$and"),
    exists("$exists"),
    or("$or"),
    in("$in"),
    all("$all"),
    lt("$lt"),
    lte("$lte"),
    gt("$gt"),
    gte("$gte"),
    not("$not"),
    regex("$regex"),
    search("$search");
    private String condition;
    MRel(String condition) {
        this.condition = condition;
    }
    public String getCondition() {
        return condition;
    }
}
