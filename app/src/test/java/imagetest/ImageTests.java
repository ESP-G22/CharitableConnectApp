package imagetest;

import org.junit.Test;
import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import api.Util;
import layout.OutputPair;
import usertest.UserUnitTest;

public class ImageTests {
    @Test
    public void uploadImageCorrect() {
        // Image not mocked?
        Bitmap image = BitmapFactory.decodeFile("download.jpg");
        OutputPair out = Util.uploadImage(image, UserUnitTest.testToken);
        assertTrue(out.isSuccess());
    }
}
