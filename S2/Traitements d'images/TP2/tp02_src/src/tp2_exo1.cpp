#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>
#include <cmath>


void get_rotation(cv::Mat &R, const float angle_in_degree){
    auto radian = angle_in_degree * (M_PI/180);
    R.at<float>(0, 0) = cos(radian);
    R.at<float>(0, 1) = -sin(radian);
    R.at<float>(1, 1) = cos(radian);
    R.at<float>(1, 0) = sin(radian);
    R.at<float>(2, 2) = 1;
}

cv::Mat rotate(cv::Mat &image, cv::Mat &R){
    cv::warpPerspective(image, image, R, image.size());
    return image;
}

void get_translation(cv::Mat &T, const float t_x, const float t_y){
    T.at<float>(0,0) = 1;
    T.at<float>(1,1) = 1;
    T.at<float>(2,2) = 1;
    T.at<float>(0,2) = t_x;
    T.at<float>(1,2) = t_y;
}

cv::Mat translate(cv::Mat &image, cv::Mat &R){
    cv::warpPerspective(image, image, R, image.size());
    return image;
}

void rotate_image_around_point(cv::Mat &image, const float angle_in_degree , const float center_x , const float center_y){
    auto R = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
    auto M = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
    auto T = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
    get_translation(R, center_x, center_y);
    get_rotation(M, angle_in_degree);
    get_translation(T, -center_x, -center_y);
    cv::warpPerspective(image, image, R*M*T, image.size());
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

  // display the image
  cv::namedWindow("une image");
  //cv::moveWindow("une image", 2000,20);
  cv::imshow("une image", image);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();
  // do something here ...
  //auto R = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
  auto T = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
  //get_rotation(R, 30);
  //image = rotate(image, R);
  //get_translation(T,90, 20);
  //image = translate(image, T);

  rotate_image_around_point(image,30,image.cols/2, image.rows/2);
  /*auto R = cv::Mat(3, 3, CV_32FC1, cv::Scalar(0.0f));
  for(auto i = 0; i <20; i++){
      get_rotation(R, 10);
      image = rotate(image, R);
  }

    for(auto i = 0; i <20; i++){
        get_rotation(R, -10);
        image = rotate(image, R);
    }*/

  // display the image
  cv::imshow("une image", image);
  std::cout << "appuyer sur une touche ..." << std::endl;
  cv::waitKey();

  // save the image
  cv::imwrite("output/tp2ex1.jpg",image);

  return 1;
}
