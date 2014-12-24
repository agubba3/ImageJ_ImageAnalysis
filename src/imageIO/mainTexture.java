package imageIO;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.ImageReader;
import ij.plugin.GaussianBlur3D;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class mainTexture {
	public static void main(String[] args){
		 String pathname = "housewithtree.jpg";
		 String message = "Ani is a procrastinator";
		 ImagePlus img = new ImagePlus(pathname);
		 int dot = pathname.indexOf(".");
		 String outputString = pathname.substring(0,dot) + "-Out2puttext.png";
		 GaussianBlur3D.blur(img, 3, 3, 3);
		 double threshold = 1;
		 double[][] varianceArray = new double[img.getWidth()][img.getHeight()];
		 
		 //img.show();

         //write file
         FileWriter fstream = null;
		try {
			fstream = new FileWriter("log.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         BufferedWriter out = new BufferedWriter(fstream);
         BufferedImage a = new BufferedImage(img.getWidth(), img.getHeight(),BufferedImage.TYPE_INT_RGB);
         //find cyan pixels
         for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {

                 int [] colorArray = img.getPixel(x,y);

                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];

                 try {
					out.write(x + "," + y + " r: " + redValue + " g: " + greenValue + " b: " + blueValue);
					out.newLine();
				 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
                 int rgb = redValue;
                 rgb = (rgb << 8) + greenValue;
                 rgb = (rgb << 8) + blueValue;
                 boolean corner = false;
                 double var = 0;
                 int boxSize = 1;
                 if (x-boxSize>= 0 && y - boxSize >= 0 && y + boxSize < img.getHeight() && x+boxSize < img.getWidth()){
                	 double tot = 0;
                	 for (int i = 0; i < boxSize*2; i++){
                		 for (int j = 0; j < boxSize*2; j++){
                			 int [] ca = img.getPixel(x-boxSize+i,y-boxSize+i);
                			  int rv = ca[0];
                			  int gv = ca[1];
                			  int bv = ca[2];
                			  double rgb2 = (rv + gv + bv) / 3.0;
                			  tot += rgb2;
                		 }
                	 }
                	 double avg = tot/(boxSize*2*boxSize*2);
                	 var = 0;
                	 for (int i = 0; i < boxSize*2; i++){
                		 for (int j = 0; j < boxSize*2; j++){
                			 int [] ca = img.getPixel(x-boxSize+i,y-boxSize+i);
                			  int rv = ca[0];
                			  int gv = ca[1];
                			  int bv = ca[2];
                			  double rgb2 = (rv + gv + bv) / 3.0;
                			  var += (rgb2 - avg)*(rgb2 - avg)/(boxSize*2*boxSize*2*threshold);
                		 }
                	 }
                	 

                	 
                 }
                	 int Edit = (int)(var);
                	 rgb = Edit;
                     rgb = (rgb << 8) + Edit; 
                     rgb = (rgb << 8) + Edit;
                 varianceArray[x][y] = var;
                 /*if(varianceArray[x][y] > 255){
                	 System.out.println(x + " " + y);
                 }*/
                 
                 a.setRGB(x, y, rgb);
                 /*if (x-img.getWidth()/2 >= 0){
                	 a.setRGB(x-img.getWidth()/2, y, rgb);
                 } else {
                	 a.setRGB(img.getWidth()-x-1, y, rgb);
                 }*/
             }
         }
         
         for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {
            	 
                 int boxSize = 1;
                 double texture = 0;
            	 if (x-boxSize>= 0 && y - boxSize >= 0 && y + boxSize < img.getHeight() && x+boxSize < img.getWidth()){
                	 double tot = 0;
                	 for (int i = 0; i < boxSize*2; i++){
                		 for (int j = 0; j < boxSize*2; j++){
                			  double rgb2 = varianceArray[x-boxSize+i][y-boxSize+i];;
                			  tot += rgb2;
                		 }
                	 }
                	 double avg = tot/(boxSize*2*boxSize*2);
                	 texture = 0;
                	 for (int i = 0; i < boxSize*2; i++){
                		 for (int j = 0; j < boxSize*2; j++){
                			  double rgb2 = varianceArray[x-boxSize+i][y-boxSize+i];
                			  texture += (rgb2 - avg)*(rgb2 - avg)/(boxSize*2*boxSize*2*threshold);
                		 }
                	 }
                	 
                 }
            	 int Edit = (int)(texture) - (int) message.charAt(x%message.length())/10;
            	 if(Edit < 0){
            		 Edit = 0;
            	 }
            	 int rgb = Edit;
                 rgb = (rgb << 8) + Edit; 
                 rgb = (rgb << 8) + Edit;
            	 a.setRGB(x, y, rgb);
             }
         }
         
         boolean[][]focus = new boolean[img.getWidth()][img.getHeight()];
         
         try {
        	File outputfile = new File(outputString);
        	ImageIO.write(a, "png", outputfile);
         } catch (IOException e) {

        }
         edgedetect(img);
         DecimalFormat df = new DecimalFormat("###.##");
         
         String pathnameText = "MihirTextOut.txt";
         
         File f = new File(pathnameText);
         PrintWriter pw = null;
         try {
			pw = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        	for (int i = 0; i<varianceArray.length; i++){
        		for (int j = 0; j<varianceArray[i].length;j++){
        			//System.out.print(varianceArray[i][j]);
        			//if(varianceArray[i][j]>1){
        				//pw.print(" " + focus[i][j]);
        			/*} if (varianceArray[i][j]<=1){
        				pw.print(0 + " ");
        			}*/
        				if(focus[i][j]){
        					pw.print(222+ " ");
        					
        				}else {
        					pw.print(0+ " ");
        				}
        			
        			
        		}
        		//System.out.println();
        		pw.println();
        	}
        	pw.close();
	}

	
	public static void edgedetect(ImagePlus img) {
		 CannyEdgeDetector detector = new CannyEdgeDetector();
		 detector.setLowThreshold(2f);
		 detector.setHighThreshold(20f);
		 //apply it to an image
		 BufferedImage frame = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		 for (int y = 0; y < img.getHeight(); y++) {
             for (int x = 0; x < img.getWidth(); x++) {
            	 int [] colorArray = img.getPixel(x,y);

                 int redValue = colorArray[0];
                 int greenValue = colorArray[1];
                 int blueValue = colorArray[2];
                 int rgb = redValue;
                 rgb = (rgb << 8) + greenValue;
                 rgb = (rgb << 8) + blueValue;
            	 frame.setRGB(x, y, rgb);
             }
		 }
		 detector.setSourceImage(frame);
		 detector.process();
		 BufferedImage edges = detector.getEdgesImage();
		 File outputfile = new File("marylandgenassem-out2");
		 try {
			ImageIO.write(edges, "png", outputfile);
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }

	}
	
}
