//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import net.coobird.thumbnailator.Thumbnails;
//
//public class UpdateImage {
//	public static void main(String[] args) throws IOException{
//		    String target = "/Users/ruixu/Desktop/1.png";//384 38
//	        String candidate = "/Users/ruixu/Desktop/2.png";//386 80
//	        
//	        String targetOuput = "/Users/ruixu/Desktop/output1.png";//384 38
//	        String candidateOuput = "/Users/ruixu/Desktop/output2.png";//386 80
//	        
//	        
//	        
//	        File picture = new File(candidate);
//	        BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture)); 
//	        System.out.println(String.format("%.1f",picture.length()/1024.0));// 源图大小
//	        System.out.println(sourceImg.getWidth()); // 源图宽度
//	        System.out.println(sourceImg.getHeight()); // 源图高度
// 
//	        
//	        
//		boolean forceSize = true;
//		 resizeImage(target, targetOuput, 384, 38, forceSize);
//		 resizeImage(candidate, candidateOuput, 384, 38, forceSize);
//	}
// 
//	public static void resizeImage (String srcPath, String destPath, int newWith, int newHeight, boolean forceSize) throws IOException   {
////		if (forceSize) {
////			Thumbnails.of(srcPath).forceSize(newWith, newHeight).toFile(destPath);
////		} else {
////			Thumbnails.of(srcPath).width(newWith).height(newHeight).toFile(destPath);
////		}
//		 
//	}
// 
//}