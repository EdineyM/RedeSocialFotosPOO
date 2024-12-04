package data;

import java.util.Arrays;

public class Photo {
    private static int id = 1;
    private byte[] imageData;
    private String filename;
    private int userId;

    public Photo(byte[] imageData, String filename, int userId) {
        this.imageData = Arrays.copyOf(imageData, imageData.length);
        this.filename = filename;
        this.userId = userId;
    }

    public Photo(byte[] imageData, String filename) {
        this(imageData, filename, 0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return Arrays.copyOf(imageData, imageData.length);
    }

    public String getFilename() {
        return filename;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}