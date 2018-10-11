package application;

public class Item {

    private int columnNo;
    private String columnName;

    public Item(int columnNo, String columnName) {
        this.columnNo = columnNo;
        this.columnName = columnName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return columnNo == item.columnNo;
    }

    @Override
    public int hashCode() {
        return columnNo;
    }

    @Override
    public String toString() {
        return columnName;
    }
}
