package proj3;

/***************************************************************************
 * Project 3 - Image Recognition
 * Name: 	     Ying Zhang
 * Class: 	     CMSC 471
 * Instructor:  Prof. Morawski
 * Description: The purpose of this program is to train a set of images, and
 * 				predict which category does this image belong.
 * 
 * Please read README for instruction to run this program.
****************************************************************************/

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.ml.Ml;
import org.opencv.ml.SVM;

public class Proj3 {

	public static void main(String[] args) {
		
		int numOfImgs = 65;
		int numOfSets = 5;
		int imgSize = 100 * 100;
		int totalImgs = numOfImgs * numOfSets;
		int row = 0;
		int labelsArray[] = new int[325];

		String imgPath = args[0];
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    Mat trainingSet = new Mat(totalImgs, imgSize, CvType.CV_32FC1);
	    Mat testImg = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	    Mat img = new Mat();
	    Mat labels = new Mat(totalImgs, 1, CvType.CV_32SC1);
	    Mat testMat = new Mat(1, testImg.rows()*testImg.cols(), CvType.CV_32FC1);
		
	    // putting training images to a Mat object
	    for(int i = 1; i <= numOfSets; i++){
	    	for(int j = 1; j <= numOfImgs; j++){
	    		if (j<10){
	    			img = Imgcodecs.imread("Data/0"+i+"/0"+j+".jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	    		}
	    		else{
	    			img = Imgcodecs.imread("Data/0,"+i+"/"+j+".jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	    		}
	    		
	    		int position = 0;
	    		for(int k = 0; k < img.rows(); k++){
	    			for(int h = 0; h < img.cols(); h++){
	    				trainingSet.put(row, position++, img.get(k, h));
	    			}
	    		}
	    		row++;
	    	}
	    }
	    
	    // assigning labels to each of the training images
	    for (int i = 0; i < labelsArray.length; i++){
	    	if (i < 65){
	    		labelsArray[i] = 1;
 	    	}
	    	else if (i >= 65 && i < 130){
	    		labelsArray[i] = 2;
	    	}
	    	else if (i >= 130 && i < 195){
	    		labelsArray[i] = 3;
	    	}
	    	else if (i >= 195 && i < 260){
	    		labelsArray[i] = 4;
	    	}
	    	else if (i >= 260 && i < 325){
	    		labelsArray[i] = 5;
	    	}
	    }

	    labels.put(0,0,labelsArray);
	    
	    // put testing image into a Mat object
		int testPosition = 0;
		for(int i = 0; i < testImg.rows(); i++){
			for(int j = 0; j < testImg.cols(); j++){
				testMat.put(0, testPosition++, testImg.get(i, j));
			}
		}
		
		// set parameter for SVM for training and prediction
	    SVM svm = SVM.create();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
	    svm.setKernel(1);
	    svm.setType(100);
	    svm.setDegree(3);
	    svm.setGamma(3);
	    svm.setTermCriteria(criteria);
	    svm.train(trainingSet, Ml.ROW_SAMPLE, labels);
	    
	    int label = (int)svm.predict(testMat);
	    
	    if (label == 1){
	    	System.out.println("Smiley");
	    }
	    else if (label == 2){
	    	System.out.println("Hat");
	    }
	    else if (label == 3){
	    	System.out.println("Hash");
	    }
	    else if (label == 4){
	    	System.out.println("Heart");
	    }
	    else{
	    	System.out.println("Dollar");
	    }
	}

}
	