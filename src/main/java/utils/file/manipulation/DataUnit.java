package utils.file.manipulation;

public enum DataUnit {
    BYTES(1),
    KILOBYTES(1024),
    MEGABYTES(1024L * 1024),
    GIGABYTES(1024L * 1024 * 1024),
    TERABYTES(1024L * 1024 * 1024 * 1024),
    PETABYTES(1024L * 1024 * 1024 * 1024 * 1024),
    EXABYTES(1024L * 1024 * 1024 * 1024 * 1024 * 1024);

    private final long bytes;

    DataUnit(long bytes) {
        this.bytes = bytes;
    }

    public long getBytes() {
        return bytes;
    }
}
