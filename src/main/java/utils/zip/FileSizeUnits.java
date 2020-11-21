package utils.zip;

public enum FileSizeUnits {
    B(1),
    KB(1024),
    MB(1048576),
    GB(1073741824);

    private long unitSize;

    FileSizeUnits(long unitSize) {
        this.unitSize = unitSize;
    }

    public long getUnitSize() {
        return unitSize;
    }
}
