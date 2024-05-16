# Tesi triennale ingegneria informatica 

## Introduction
This Android app uses [Google's ML kit](https://developers.google.com/ml-kit) to recognize various movements made by a human body. It was developed from [this app](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart).

## How it works
In the app main screen are displayed all the movements that can be recognized. Once a movement has been selected, the app starts the camera and if there is a human body, it uses ML Kit to track its pose, which is made up of 33 points in a three-dimensional space.

![image | 600](https://developers.google.com/static/ml-kit/images/vision/pose-detection/landmarks-fixed.png)
All the poses that can be recognized are saved in a csv file named `fitness_pose_samples.csv` and foreach line there's a attribute that indicates which pose is.
Once the camera is launched and a human body is detected, for each frame the pose is analyzed and using the [k-nearest neighbors](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) algorithm (k-NN) is calculated a coefficient of how much the analyzed pose is close to the ones of the selected pose included in the csv file. If the movement is over a certain treshold the result is shown on the screen.

## Some examples
https://www.youtube.com/shorts/o_zFg-RxnzE

[![IMAGE ALT TEXT HERE](https://i9.ytimg.com/vi/o_zFg-RxnzE/mqdefault.jpg?sqp=COy3mLIG-oaymwEoCMACELQB8quKqQMcGADwAQH4Ab4EgAKACooCDAgAEAEYZSBlKGUwDw==&rs=AOn4CLDCCGCIIaksVvNSJRiP9jWOMrQTeA)](https://www.youtube.com/shorts/o_zFg-RxnzE)
