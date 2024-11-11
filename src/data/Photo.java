package data;

import java.util.Arrays;

public class Photo {
    private byte[] imageData;
    private String filename;

    public Photo(byte[] imageData, String filename) {
        this.imageData = Arrays.copyOf(imageData, imageData.length);
        this.filename = filename;
    }

    public byte[] getImageData() {
        return Arrays.copyOf(imageData, imageData.length);
    }

    public String getFilename() {
        return filename;
    }
}