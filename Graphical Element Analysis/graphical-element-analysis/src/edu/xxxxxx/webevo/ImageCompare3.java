package edu.xxxxxx.webevo;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FileUtils;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.utils.Converters;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
/*
 * FP: actually not changed, api say it does change, found candidate, it does not change
 * FN: actually changed, api say it does not change, location changed, name changed etc.., found candidate, 
 * 
 * (A) if target text is no empty
 * 1. look for same text,compare result,require less smility, if no, look for similar text
 * 2. look for similar pic in those simliar text
 * 
 * (B) if target text is empty, look for all similiar pic with empty text
 * 1.compare pics
 * */


public class ImageCompare3 {	
	
	 static List<String> allXpaths = new ArrayList<>();
     static List<String> allTypes =new ArrayList<>();
     static List<String> allResults=new ArrayList<>();
     static String newURL="";
     static String oldURL="";
     static String newImage="";
     static String oldImage="";
     static String targetText;
     static String targetTag;
     static int targetX;
     static int targetY;
     static int targetW;
     static int targetH;
     static String candidateLocation;
     static String finalCandidate;
     static String targetXpath;
     static int targetName;
     static Boolean isPresent;
     static String targetLocation;
     static int TotalChange;
     static int FpChange;
     static int FnChange;
     static List<Integer> eleWithText=new ArrayList<>();
     static List<Integer> eleWithOutText=new ArrayList<>();
     static Map<Integer,Double> elementsSim=new HashMap<>();
    public static void main(String[] args) 
    {
    	Date startDate = new Date(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String start = sdf.format(startDate); 
        System.out.println("Batch insert file start::::::::::::::::"+start);

//        System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
//        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
//        WebDriver driver = new ChromeDriver();
          
          //FS added on 8/19/2020
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\geckodriver.exe");
        DesiredCapabilities dc = new DesiredCapabilities();
	      dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);	//accept popup automatically
	      WebDriver driver = new FirefoxDriver(dc);
	      
	      
	      newURL="file://C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\2020.html";
	      oldURL="file://C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\2019.html";
	      newImage="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\new.png";
	      oldImage="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\old.png";
	      String csvPath="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\ds1_results.csv";
          
//          newURL="file:///home/feishao/WebEvo/cases/homedepot/2020.html";  
//          oldURL="file:///home/feishao/WebEvo/cases/homedepot/2018.html";
//          newImage="/home/feishao/WebEvo/cases/homedepot/new.png";
//          oldImage="/home/feishao/WebEvo/cases/homedepot/old.png";
//          String csvPath="/home/feishao/WebEvo/cases/homedepot/ds1_results.csv";
          
        //(2)read csv
    	readCSV(csvPath);
    	System.out.println("finished read csv file.");
    	System.out.println();
    	
    	//(4)Calculate accuracy without algorithm
    	Map<String,Integer> map=new HashMap<>();
    	for(int i=0;i<allResults.size();i++) {
    		if(allResults.get(i).equals("TP")) {map.put("TP", map.getOrDefault("TP", 0)+1);}
    		if(allResults.get(i).equals("TN")) {map.put("TN", map.getOrDefault("TN", 0)+1);}
    		if(allResults.get(i).equals("FP")) {map.put("FP", map.getOrDefault("FP", 0)+1);}
    		if(allResults.get(i).equals("FN")) {map.put("FN", map.getOrDefault("FN", 0)+1);}
    	}
     	int TP=map.get("TP")!=null?map.get("TP"):15;
     	int TN=map.get("TN")!=null?map.get("TN"):0;
     	int FP=map.get("FP")!=null?map.get("FP"):0;
     	int FN=map.get("FN")!=null?map.get("FN"):0;
     //	System.out.println("Accuracy before algorithm: "+((double)(TP+TN)/(TP+TN+FP+FN)));
        System.out.println("TP | TN | FP | FN | ACCURACY | PRECISION | RECALL | F1");
        double PRECISION=(double)(TP)/(TP+FP);
        double RECALL=(double)(TP)/(TP+FN);
        double F1=2*(double)(PRECISION*RECALL)/(PRECISION+RECALL);
        System.out.println(TP+" | "+TN+" | "+FP+" | "+FN+" | "+(double)(TP+TN)/(TP+TN+FP+FN)+" | "+PRECISION+" | "+RECALL+" | "+F1);
    	 try {
             FileUtils.deleteDirectory(new File(targetLocation));
             new File(targetLocation).mkdirs();
             }catch(Exception e) {
             	//System.out.println(e);
             }
    	for(int i=0;i<allResults.size();i++) {
    		targetName=i;
    		
      	  if(allResults.get(i).equals("FN")||allResults.get(i).equals("FP")||allResults.get(i).equals("TN")||allResults.get(i).equals("TP")) {
      		System.out.println("*************************CASE "+i+" START**************************");
      		//(3)processing target image
         // 	driver.get(oldURL);
      		System.out.println("Current Case is "+allResults.get(i));
          	targetXpath=allXpaths.get(i);
          	String targetURL="";
          	if(isXpathExist(targetXpath,driver,oldURL)){
          		//System.out.println(isXpathExist(targetXpath,driver,oldURL));
          		targetURL=oldURL;
          	}else if(isXpathExist(targetXpath,driver,newURL)){//new node added in the new webpage
          		//System.out.println(isXpathExist(targetXpath,driver,newURL));
          		targetURL=newURL;
          	}else {
          		System.out.println("the xpath does not exist neither in old url nor new url");
          		continue;
          	}
          	System.out.println("++++++++++++++++++++++++++++++++++++++");
          	System.out.println("target xpath is "+targetXpath);
          	System.out.println("target url is "+targetURL);
          	try {
          	driver.get(targetURL);
          	}catch(TimeoutException e) {
          		continue;
          	}
           
            targetLocation="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\target_img\\";
//            targetLocation="/home/feishao/WebEvo/cases/homedepot/target_img";
            if(targetURL==oldURL) {
            	 processingTargetImage(driver,targetXpath,targetLocation,oldImage,i);
            }else {
            	 processingTargetImage(driver,targetXpath,targetLocation,newImage,i);
            }
           
            System.out.println("Saved target image in "+targetLocation);
            System.out.println("++++++++++++++++++++++++++++++++++++++");
            
            if(isPresent) {
            	 //(4)processing candidate images
            	String candidateURL="";
            	if(targetURL==oldURL) {
            		candidateURL=newURL;
            	}else {
            		candidateURL=oldURL;
            	}
            	//driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
            	try {
            		driver.get(candidateURL);
            	}catch(TimeoutException e) {
            		continue;
            		//System.out.println("adaptation system time outed!!");
            	}
                driver.manage().window().maximize();
                candidateLocation="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\candidate_img\\";
//                candidateLocation = "/home/feishao/WebEvo/cases/homedepot/candidate_img";
                new File(candidateLocation+"target"+i+"_folder").mkdirs();
                if(candidateURL==oldURL) {
                	processingCandidatesImage(driver,candidateLocation+"target"+i+"_folder/",oldImage);
                }else {
                	processingCandidatesImage(driver,candidateLocation+"target"+i+"_folder/",newImage);
                }
                
          
                if(finalCandidate.equals("")) {
                	System.out.println("no similar pic found");
                }
                
                System.out.println();
               
                //(5)Comparing target with candidates
//       	     System.out.println("starting comparing: ");
//       	     finalCandidate="";
//       	   //  compareImage();
       	     if(!finalCandidate.equals("")) {
       	    	 if(allResults.get(i).contentEquals("FN")) {
       	    		FnChange++;
       	    	 }else if(allResults.get(i).contentEquals("FP")) {
       	    		FpChange++;
       	    	 }
       	    	TotalChange++;
       	    	 System.out.println("Found Result for "+allXpaths.get(i)+"!");
       	     }else {
       	    	 System.out.println("No similar pic found!");
       	     }
       	     
       	     
            }
            
   	      System.out.println("*************************CASE "+i+" END**************************");
      	  }
    
        }
//  
    	
    	Date endDate = new Date(); 
		String end = sdf.format(endDate); 
        System.out.println("Batch insert file end::::::::::::::::"+end);
    	
    	 System.out.println("TP | TN | FP | FN | ACCURACY | PRECISION | RECALL | F1");
         PRECISION=(double)(TP+FpChange)/(TP+FP);
         RECALL=(double)(TP+FpChange)/(TP+FN+FpChange-FnChange);
         F1=2*(double)(PRECISION*RECALL)/(PRECISION+RECALL);
         System.out.println((TP+FpChange)+" | "+(TN+FnChange)+" | "+(FP-FpChange)+" | "+(FN-FnChange)+" | "+(double)(TP+TN+TotalChange)/(TP+TN+FP+FN)+" | "+PRECISION+" | "+RECALL+" | "+F1);
    	 
         driver.quit();
       
    }
    
    
    public static void processingTargetImage(WebDriver driver,String xpath,String location,String image,int index) {
    	driver.manage().window().maximize();
    	 try {
//    	FileUtils.deleteDirectory(new File(location));
//        new File(location).mkdirs();
        isPresent = driver.findElements(By.xpath(xpath)).size() > 0;
        System.out.println("is current xpath exist?: "+isPresent);
    	 if(!isPresent) {
    		 return;
    	 }
    	 WebElement element=driver.findElement(By.xpath(xpath));
    	 
         driver.manage().window().maximize();
         targetText=element.getText();
         targetX=element.getLocation().x;
         targetY=element.getLocation().y;
         targetW=element.getSize().getWidth();
         targetH=element.getSize().getHeight();
         targetTag=element.getTagName();
      //   System.out.println(targetText+","+targetTag+","+xpath);
         
         System.out.println("starting saving target image...");
         System.out.println("Current target element: "+targetText+","+targetX+","+targetY+","+targetW+","+targetH);
         //saveTarget
	      
	       cutJPG(new FileInputStream(image),  
	                  new FileOutputStream(location+"target"+index+".png"), targetX,targetY,targetW,targetH); 
	       }catch(Exception e) {
	    	   System.out.println("errors when finding target image!");
	       }

    	 
      
   }
    
    public static void processingCandidatesImage(WebDriver driver,String location,String image) {
    	driver.manage().window().maximize();

        int count=1;
        finalCandidate="";
        eleWithText=new ArrayList<>();
        eleWithOutText=new ArrayList<>();
        elementsSim=new HashMap<>();
        String targetPath="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\target_img\\target"+targetName+".png";
       
//       String targetPath = "/home/feishao/WebEvo/cases/homedepot/target_img/target"+targetName+".png";
        System.out.println("target text:|"+targetText+"|");
        List<WebElement> eletest=new ArrayList<>();
        String[] tags= {"div","li","a","img","iframe","h1","h2","h3","h4","h5","h6","span","p"};
        try {
        	 
        	for(int i=0;i<tags.length;i++) {
        		
        		eletest.addAll(driver.findElements(By.tagName(tags[i])));
        	}
            System.out.println("total "+eletest.size()+" elements!");  
           
         
            //with text
            
            //with no text
            
            for(int i=0;i<eletest.size();i++) {
            //	System.out.println("test:"+eletest.get(i).getText());
                if(eletest.get(i).getText().equals("")) {
                	eleWithOutText.add(i);
                }
                if(!eletest.get(i).getText().equals("")) {
                	eleWithText.add(i);
                }
            }
            System.out.println("total ele with text: "+eleWithText.size());
            System.out.println("total ele without text: "+eleWithOutText.size());
            
            if(targetText.equals("")) {
            	System.out.println("Current target text is empty, comparing it with candidate with empty text...");
            	for(int j:eleWithOutText) {
            		
            		int candidateX=eletest.get(j).getLocation().x;
       	           int candidateY=eletest.get(j).getLocation().y;
       	           int candidateW=eletest.get(j).getSize().getWidth();
       	           int candidateH=eletest.get(j).getSize().getHeight();
       	           
       	          ScreenPNG.cutJPG(new FileInputStream(image),
             			   new FileOutputStream(location+"candidate"+(count)+".png"), candidateX,candidateY,candidateW,candidateH); 
       	       
             	   String tempTargetPath="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\target_img\\target"+targetName+"_"+count+".png";
//             	   String tempTargetPath = "/home/feishao/WebEvo/cases/homedepot/target_img/target"+targetName+"_"+count+".png";
             	   copyFile(targetPath,tempTargetPath);
              
             	   if(new File(tempTargetPath).length()!=0) {
             		  compareImage(tempTargetPath,location+"candidate"+(count)+".png");
             	   }
             	   
              
             	   if(!finalCandidate.equals("")) {
             		System.out.println("final candidate is: "+eletest.get(j).getText());
                    	break;
                   }
             	   File file = new File(tempTargetPath); 
                    
                    file.delete();
                    
             	   count++;
            		
            		
            	}
            }else {
            	System.out.println("current target text is not empty, comparing...");
            	for(int j:eleWithText) {
            		double currentStringSim=StringSimilarity.getSimilarityRatio(eletest.get(j).getText(),targetText);
            		
            		//Text prior 
//            		if(currentStringSim >= 0.88) {
//            			finalCandidate = "Found final candidate.";
//            			break;
//            		}
            		
            		elementsSim.put(j,currentStringSim);
            	}
            	 
            	Map<Integer, Double> hm1 = sortByValue(elementsSim);
            	List<Integer> hm2=new ArrayList<>();
            	int countC=0;
            	for(int key:hm1.keySet()) {
            		System.out.println(key+", "+hm1.get(key));
            		System.out.println(eletest.get(key).getText());
            		hm2.add(key);
            		countC++;
            		if(countC>=5) {
            			break;
            		}
            	}
            	
            	/**************/
            	for(int key:hm2){
            	int candidateX=eletest.get(key).getLocation().x;
  	           int candidateY=eletest.get(key).getLocation().y;
  	           int candidateW=eletest.get(key).getSize().getWidth();
  	           int candidateH=eletest.get(key).getSize().getHeight();
  	           
  	          ScreenPNG.cutJPG(new FileInputStream(image),
        			   new FileOutputStream(location+"candidate"+(count)+".png"), candidateX,candidateY,candidateW,candidateH); 
  	       
        	   String tempTargetPath="C:\\Users\\fs\\Desktop\\PhD\\Rui Xu\\cases_new\\homedepot\\target_img\\target"+targetName+"_"+count+".png";
//  	          String tempTargetPath="/home/feishao/WebEvo/cases/homedepot/target_img/target"+targetName+"_"+count+".png";
        	   copyFile(targetPath,tempTargetPath);
         
        	   if (new File(tempTargetPath).length()!=0) {
        		   compareImage(tempTargetPath,location+"candidate"+(count)+".png");
        	   }
        	   
         
        	   if(!finalCandidate.equals("")) {
        		System.out.println("final candidate is: "+eletest.get(key).getText());
               	break;
              }
        	   File file = new File(tempTargetPath); 
               
               file.delete();
               
        	   count++;
            	}
        	   /**************/

            }
	        
        }catch(Exception e) {
       	 System.out.println("errors when finding candidates images!");
        }
	       System.out.println("got all candidates");
	       System.out.println("Saved all candidates images!");
    	
    }
   
   
    
    
    public static void compareImage(String targetPath,String candidatePath) {
    	if(!new File(candidatePath).canRead()||new File(candidatePath).length()==0){
	   		 return;
	   	}
    	System.out.println("target path: "+targetPath);
    	System.out.println("candidate path: "+candidatePath);
    	int totalCandidate=new File(candidateLocation).list().length;
	    try {
 
	        System.out.println("Comparing two pics...");
	        FingerPrint fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
	        FingerPrint fp2;
	        fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
            	 
			  double fingerRes=fp1.compare(fp2);
			//  double cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
              System.out.println("Compaing current two images...");
            //  double temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }
	        System.out.println("original images similaity: "+fingerRes);
	        //grey scale
	        System.out.println("Grey Scaling the target img and candidate img...");
	        converToGrey.convert(targetPath,targetPath);
	        converToGrey.convert(candidatePath,candidatePath);
	        System.out.println("Done Grey scale for target and candiate");
	        
	        
	        fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
	        fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
            	 
			 fingerRes=fp1.compare(fp2);
		//	   cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
              System.out.println("Compaing current two images...");
         //      temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }
              System.out.println("Current simility is "+fingerRes+", not consider as same pics");
              System.out.println("Trying reverse colors for candidate...");
              ImgInverse.imgeInverse(candidatePath, candidatePath);
              System.out.println("Done reverse color for candidate");
              
              //reverse color 
              System.out.println("Comparing two pics...");
              fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
  	          fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
              fingerRes=fp1.compare(fp2);
		//	  cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
		//	  temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }  
              System.out.println("reversed images similaity: "+fingerRes);
              
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NullPointerException e1) {
        	System.out.println("can not read target image!");
        }
    }  
    public static void compareImageWithSameText(String targetPath,String candidatePath) {
    	if(!new File(candidatePath).canRead()||new File(candidatePath).length()==0){
	   		 return;
	   	}
    	System.out.println("target path: "+targetPath);
    	System.out.println("candidate path: "+candidatePath);
    	int totalCandidate=new File(candidateLocation).list().length;
	    try {
 
	        System.out.println("Comparing two pics...");
	        FingerPrint fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
	        FingerPrint fp2;
	        fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
            	 
			  double fingerRes=fp1.compare(fp2);
			//  double cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
              System.out.println("Compaing current two images...");
            //  double temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }
	        System.out.println("original images similaity: "+fingerRes);
	        //grey scale
	        System.out.println("Grey Scaling the target img and candidate img...");
	        converToGrey.convert(targetPath,targetPath);
	        converToGrey.convert(candidatePath,candidatePath);
	        System.out.println("Done Grey scale for target and candiate");
	        
	        
	        fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
	        fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
            	 
			 fingerRes=fp1.compare(fp2);
		//	   cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
           //   System.out.println("Compaing current two images...");
         //      temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }
              System.out.println("Current simility is "+fingerRes+", not consider as same pics");
              System.out.println("Trying reverse colors for candidate...");
              ImgInverse.imgeInverse(candidatePath, candidatePath);
              System.out.println("Done reverse color for candidate");
              
              //reverse color 
           //   System.out.println("Comparing two pics...");
              fp1 = new FingerPrint(ImageIO.read(new File(targetPath)));
  	          fp2 = new FingerPrint(ImageIO.read(new File(candidatePath)));
              fingerRes=fp1.compare(fp2);
		//	  cvRes=OpenCVImageCompare.CompareAndMarkDiff(targetPath, candidatePath);
		//	  temp=fingerRes>cvRes?fingerRes:cvRes;;
              if(fingerRes>=0.60) {
        
            		//  System.out.println(temp);
            		  System.out.println("the most similiar picture is: "+"candidate "+candidatePath+": "+fingerRes);
            		  finalCandidate="candidate "+candidatePath;
            		  return;
              }  
              System.out.println("reversed images similaity: "+fingerRes);
              
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NullPointerException e1) {
        	System.out.println("can not read target image!");
        }
    } 
    public static void readCSV(String path) {
    	File csv = new File(path);   
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String xpath = "";
        String type="";
        String res="";
       
        try {
            while ((line = br.readLine()) != null) 
            {
            	String[] temp=line.split(",");
            	
                xpath=temp[0];
                type=temp[1];
                res=temp[6];
                
              //  System.out.println(xpath+","+type+","+res);
              
                allXpaths.add(xpath);
                allTypes.add(type);
                allResults.add(res);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    public static void cutJPG(InputStream input, OutputStream out, int x,  
            int y, int width, int height) throws IOException {  
        ImageInputStream imageStream = null;  
        try {  
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");  
            ImageReader reader = readers.next();  
            imageStream = ImageIO.createImageInputStream(input);  
            reader.setInput(imageStream, true);  
            ImageReadParam param = reader.getDefaultReadParam(); 
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, "png", out);  
        } finally {  
            imageStream.close();  
        }  
    }  
    public static boolean isXpathExist(String xpath,WebDriver driver,String url) {
    	driver.manage().window().maximize();
    	
    	try {
    		driver.get(url);
    	}catch(TimeoutException e) {
    		return false;
    	}
    	
    	try {driver.findElements(By.xpath(xpath));}
    	catch(InvalidSelectorException e) {
    		return false;
    	}
    	
    	return driver.findElements(By.xpath(xpath)).size() > 0;
    }
    public static void copyFile(String src,String target)
	{	
		File srcFile = new File(src);  
		   File targetFile = new File(target);  
		   try {  
		       InputStream in = new FileInputStream(srcFile);   
		       OutputStream out = new FileOutputStream(targetFile);  
		       byte[] bytes = new byte[1024];  
		       int len = -1;  
		       while((len=in.read(bytes))!=-1)
		       {  
		           out.write(bytes, 0, len);  
		       }  
		       in.close();  
		       out.close();  
		   } catch (FileNotFoundException e) {  
		       e.printStackTrace();  
		   } catch (IOException e) {  
		       e.printStackTrace();  
		   }  
//		   System.out.println("succeed copied pic"); 


	}
    public static HashMap<Integer, Double> sortByValue(Map<Integer, Double> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<Integer, Double> > list = 
               new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() { 
            public int compare(Map.Entry<Integer, Double> o1,  
                               Map.Entry<Integer, Double> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>(); 
        for (Map.Entry<Integer, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
 
}


