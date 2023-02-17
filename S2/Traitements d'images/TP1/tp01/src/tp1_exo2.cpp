#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>
#include <algorithm>


int clamp(int x){
    return std::min((std::max(x,0)), 255);
}

void redLine(cv::Mat image){
    //Put all pixel in red row 42
    for(auto col = 0 ; col < image.cols; col++) {
        image.at<cv::Vec3b>(42, col)[0] = 0;
        image.at<cv::Vec3b>(42, col)[1] = 0;
        image.at<cv::Vec3b>(42, col)[2] = 255;
    }
}

void PlusCinquante(cv::Mat image){
    //Add 50 to all pixel composants without check if the result value is > to 255
    for(auto line = 0; line < image.rows; line++){
        for(auto col = 0; col < image.cols; col++){
            /*image.at<cv::Vec3b>(line,col)[0] += 50;
            image.at<cv::Vec3b>(line,col)[1] += 50;
            image.at<cv::Vec3b>(line,col)[2] += 50;*/
            image.at<cv::Vec3b>(line,col)[0] = clamp(image.at<cv::Vec3b>(line,col)[0]+50);
            image.at<cv::Vec3b>(line,col)[1] = clamp(image.at<cv::Vec3b>(line,col)[1]+50);
            image.at<cv::Vec3b>(line,col)[2] = clamp(image.at<cv::Vec3b>(line,col)[2]+50);
        }
    }
}

void negatif(cv::Mat image){
    //Negatif
    for(auto line = 0; line < image.rows; line++){
        for(auto col = 0; col < image.cols; col++){
            image.at<cv::Vec3b>(line,col)[0] = 255 - image.at<cv::Vec3b>(line,col)[0];
            image.at<cv::Vec3b>(line,col)[1] = 255 - image.at<cv::Vec3b>(line,col)[1];
            image.at<cv::Vec3b>(line,col)[2] =  255 - image.at<cv::Vec3b>(line,col)[2];
        }
    }
}

cv::Mat greyImage(cv::Mat image){
    cv::Mat gray_image;
    cvtColor( image, gray_image, cv::COLOR_BGR2GRAY);
    // display an image
    cv::imshow("une grey image 1", gray_image);
    return gray_image;
}

void whiteNblack(const int seuil, cv::Mat greyMat){
    for(auto line = 0; line < greyMat.rows; line++) {
        for (auto col = 0; col < greyMat.cols; col++) {
            greyMat.at<cv::Vec3b>(line,col)[0] = (greyMat.at<cv::Vec3b>(line,col)[0] < seuil) ? 0 : 255;
            greyMat.at<cv::Vec3b>(line,col)[1] = (greyMat.at<cv::Vec3b>(line,col)[1] < seuil) ? 0 : 255;
            greyMat.at<cv::Vec3b>(line,col)[2] = (greyMat.at<cv::Vec3b>(line,col)[2] < seuil) ? 0 : 255;
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char ** argv)
{
  // check arguments
  if(argc != 2){
    std::cout << "usage: " << argv[0] << " image" << std::endl;
    return -1;
  }

  // load the input image
  std::cout << "load image ..." << std::endl;
  cv::Mat image = cv::imread(argv[1]);
  if(image.empty()){
    std::cout << "error loading " << argv[1] << std::endl;
    return -1;
  }
  std::cout << "image size : " << image.cols << " x " << image.rows << std::endl;


  // do something
  // ...

  std::cout << "Bleu " <<(int) image.at<cv::Vec3b>(50,100)[0] << " Vert " << (float) image.at<cv::Vec3b>(50,100)[1]  << " Rouge " << (float) image.at<cv::Vec3b>(50,100)[2] << std::endl;

  //Put pixel 10,20 in red
  image.at<cv::Vec3b>(10,20)[0] = 0;
  image.at<cv::Vec3b>(10,20)[1] = 0;
  image.at<cv::Vec3b>(10,20)[2] = 255;

  //negatif(image);
  auto greyMat = greyImage(image);
  whiteNblack(128, greyMat);

  cv::imshow("une image", image);
  cv::imshow("une grey image 2", greyMat);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output/tp1ex2.jpg",image);

  return 1;
}
