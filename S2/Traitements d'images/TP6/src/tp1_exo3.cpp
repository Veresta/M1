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

  Mat open,close,som;

  morphologyEx(image, open, MORPH_OPEN,element2, Point(-1,-1), 1);
  morphologyEx(image, close, MORPH_CLOSE,element2, Point(-1,-1), 1);

  morphologyEx(image, som, MORPH_CLOSE,element2, Point(-1,-1), 1);
  som = 255 - som;
  image = image - som;
  //erode(som,som, element2, Point(-1,-1), 1);
  //morphologyEx(som, som, MORPH_OPEN,element2, Point(-1,-1), 1);
  //dilate(som,som, element2, Point(-1,-1), 1);
  //erode(som, som, element2, Point(-1,-1), 1);
  //morphologyEx(som, som, MORPH_OPEN,element2, Point(-1,-1), 1);


  imshow("une image", image);
  imshow("ouverture", open);
  imshow("fermeture", close);
  imshow("idempotent", som);
  //imshow("Erode", erod);
  //cv::imshow("une grey image 2", greyMat);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output-close-quizz.jpg",open);
  cv::imwrite("output-close-quizz.jpg",close);

  return 1;
}
