/* This class uses imageJ classes
 * 
 */
package imageIO;

import ij.ImagePlus;
public class ImageArray {
	public int[][] rbgArray(String pathname){
		ImagePlus img = new ImagePlus("pathname");
		int[][] arrayout = new int[img.getWidth()][img.getHeight()];
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int [] colorArray = img.getPixel(x,y);
                int redValue = colorArray[0];
                int greenValue = colorArray[1];
                int blueValue = colorArray[2];
                int rgb = redValue;
                rgb = (rgb << 8) + greenValue;
                rgb = (rgb << 8) + blueValue;
                arrayout[x][y] = rgb;
            }
        }
        return arrayout;
	}
}
