package ir.nimbo.searchengine.util;

public enum PropertyType {
    HBASE_FAMILY_1("hbase.family1"),HBASE_FAMILY_2("hbase.family2"), HBASE_COLUMN_OUTLINKS("hbase.column.outlinks"), HBASE_COLUMN_PAGERANK("hbase.column.pagerank"),
    HBASE_TABLE("hbase.table");

    private String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
