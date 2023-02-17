#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>

cv::Mat drawSquare(int size, cv::Mat image){
    for(auto col = image.cols/2 - size; col < image.cols/2 + size; col++ ){
        for(auto line = image.rows/2 - size; line < image.rows/2 + size; line++ ){
            image.at<cv::Vec3b>(line, col)[0] = 0;
            image.at<cv::Vec3b>(line, col)[1] = 255;
            image.at<cv::Vec3b>(line, col)[2] = 0;
        }
    }
    return image;
}

cv::Mat drawFaded(int size, cv::Mat image){
    auto scale = 255 / size;
    auto tmp = 0;
    for(auto col = image.cols/2 - size; col < image.cols/2 + size; col++ ){
        for(auto line = image.rows/2 - size; line < image.rows/2 + size; line++ ){
            image.at<cv::Vec3b>(line, col)[0] = tmp;
            image.at<cv::Vec3b>(line, col)[1] = tmp;
            image.at<cv::Vec3b>(line, col)[2] = tmp;
        }
        tmp+=scale / 2;
    }
    return image;
}

cv::Mat drawCircle(int size, cv::Mat image){
    cv::Point center(image.cols/2, image.rows/2);//Declaring the center point
    int radius = size; //Declaring the radius
    cv::Scalar line_Color(0, 255, 255);//Color of the circle
    int thickness = 3;//thickens of the line
    cv::circle(image, center,radius, line_Color, thickness);//Using circle()function to draw the line//
    return image;
}



//////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char ** argv)
{
  // check arguments
  if(argc != 2){
    std::cout << "usage: " << argv[0] << " image" << std::endl;
    return -1;
  }

  cv::Mat image = cv::imread(argv[1]);
  // load the input image
  std::cout << "load image ..." << std::endl;
  if(image.empty()){
    std::cout << "error loading " << argv[1] << std::endl;
    return -1;
  }
  std::cout << "image size : " << image.cols << " x " << image.rows << std::endl;

  //image = drawSquare(20, image);
  image = drawFaded(80, image);

  image = drawCircle(40, image);


  // display an image
  cv::imshow("une image", image);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output/tp1ex3.jpg",image);

  return 1;
}
