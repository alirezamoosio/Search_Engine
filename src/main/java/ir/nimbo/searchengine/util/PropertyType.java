package ir.nimbo.searchengine.util;

public enum PropertyType {
    HBASE_FAMILY("hbase.family"), HBASE_COLUMN_OUTLINKS("outLinks"), HBASE_COLUMN_PAGERANK("pageRank");

    private String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
