package fileTypes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFiles extends FileType{
    private int width;
    private int height;

    public ImageFiles(String[] fileInfo){
        super(fileInfo);
        setImageInfo();
        getInfo();
    }

    private void setImageInfo(){

        try {
            File file = new File(getName());

            BufferedImage image = ImageIO.read(file);
            height = image.getHeight();
            width = image.getWidth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void getInfo() {
        String modifiedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getModifiedTime()));
        String createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(getCreatedTime()));
        String INFO = String.format("""
                File type: %s
                Created: %s
                Modified: %s
                Width: %s
                Height: %s
                """,getExtension(),modifiedTime,createdTime,width,height);
        System.out.println(INFO);
    }
}
