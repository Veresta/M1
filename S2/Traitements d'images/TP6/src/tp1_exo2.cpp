#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>

using namespace cv;
using namespace std;

cv::Mat greyImage(cv::Mat image){
    cv::Mat gray_image;
    cvtColor( image, gray_image, cv::COLOR_BGR2GRAY);
    // display an image
    cv::imshow("une grey image 1", gray_image);
    return gray_image;
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

  image = greyImage(image);
  int morph_size = 10;
  auto element = getStructuringElement(
            MORPH_RECT, Size(2 * morph_size + 1,
                             2 * morph_size + 1),
            Point(morph_size, morph_size));

  auto element2 = getStructuringElement(
        MORPH_ELLIPSE, Size(2 * morph_size + 1,
                         2 * morph_size + 1),
        Point(morph_size, morph_size));

  Mat dil,erod, test;

  dilate(image,dil, element2, Point(-1,-1), 1);
  erode(image, erod, element2, Point(-1,-1), 1);

  dilate(image,test, element2, Point(-1,-1), 1);
  dilate(test,test, element2, Point(-1,-1), 1);



  imshow("une image", image);
  imshow("Dilate", dil);
  imshow("Erode", erod);
  imshow("Idempotent", test);
  //cv::imshow("une grey image 2", greyMat);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output-dill-3.jpg",dil);
  cv::imwrite("output-erode-3.jpg",erod);

  return 1;
}
