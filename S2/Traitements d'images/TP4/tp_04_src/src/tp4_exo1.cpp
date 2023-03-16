#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>

cv::Mat q9(){
    cv::Mat kernel(11, 11, CV_64F, cv::Scalar(0));
    kernel.at<double>(1,1) = 5.0;
    kernel.at<double>(0,1) = -1.0;
    kernel.at<double>(2,1) = -1.0;
    kernel.at<double>(1,0) = -1.0;
    kernel.at<double>(1,2) = -1.0;
    kernel.at<double>(7,9) = -10.0;
    kernel.at<double>(3,4) = -5.0;
    kernel.at<double>(4,3) = 8.0;
    kernel.at<double>(9,2) = 5.0;
    return kernel;
}
///////////////////////////////////
cv::Mat laplacian(){
    cv::Mat kernel(3, 3, CV_64F, cv::Scalar(0));
    kernel.at<double>(1,1) = -4.0;
    kernel.at<double>(0,1) = 1.0;
    kernel.at<double>(2,1) = 1.0;
    kernel.at<double>(1,0) = 1.0;
    kernel.at<double>(1,2) = 1.0;
    return kernel;
}
////////////////////////////////////////////////////////////////////////////////
cv::Mat derivative_y(){
    cv::Mat kernel(3, 3, CV_64F, cv::Scalar(0));
    kernel.at<double>(0,0) = -1.0;
    kernel.at<double>(1,0) = -2.0;
    kernel.at<double>(2,0) = -1.0;
    kernel.at<double>(0,2) = 1.0;
    kernel.at<double>(1,2) = 2.0;
    kernel.at<double>(2,2) = 1.0;
    return kernel;
}


///////////////////////////////////////////////////////////////////////////////
cv::Mat derivative_x()
{
  // create and initialize and 1d array of integers
  cv::Mat kernel(3,3,CV_64F, cv::Scalar(0));
  // build your kernel
  kernel.at<double>(1,1) = -1.0;
  kernel.at<double>(1,2) = 1.0;
  return kernel;
}


///////////////////////////////////////////////////////////////////////////////
void apply_convolution(const cv::Mat& src, cv::Mat& dst, const cv::Mat &kernel, const unsigned char offset)
{
  dst = cv::Mat(src.size(), CV_8UC1, cv::Scalar(0));

  for(int i=kernel.rows/2; i<dst.rows - (kernel.rows/2); ++i)
    for(int j=kernel.cols/2; j<dst.cols - (kernel.cols/2); ++j){
      double value = 0;
      for(int u=0; u<kernel.rows; ++u)
        for(int v=0; v<kernel.rows; ++v){
          int pos_i = i -(kernel.rows/2) + u;
          int pos_j = j -(kernel.cols/2) + v;
          value += double(src.at<unsigned char>(pos_i,pos_j) * kernel.at<double>(u,v));
        }
      dst.at<unsigned char>(i,j) = std::clamp(value + offset, 0.0, 255.0);
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

  // convert the image to grayscale
  cvtColor(image,image,cv::COLOR_BGR2GRAY);

  // display an image
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::imshow("image", image);
  cv::waitKey();


  // do something here ... ////////
  cv::Mat filtered_image1;
  cv::Mat filtered_image2;

  // derivative x
  apply_convolution(image, filtered_image1, laplacian(), 128);
  apply_convolution(image, filtered_image2, q9(), 0);

  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::imshow("image", filtered_image2);
  cv::waitKey();
  cv::imshow("image", filtered_image1);
  cv::waitKey();

  ////////////////////////////////


  // save images
  cv::imwrite("output/image.jpg",image);


  return 1;
}
