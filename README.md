# Bachelor's Thesis in Computer Engineering

## Introduction

This Android app utilizes [Google's ML Kit](https://developers.google.com/ml-kit) to recognize various movements made by the human body. It was developed based on [this app](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart).

## How it works

On the main screen of the app, all the movements that can be recognized are displayed. Once a movement is selected, the app activates the camera. If a human body is detected, ML Kit is employed to track the user's pose, composed of 33 points in a three-dimensional space.

![image | 600](https://developers.google.com/static/ml-kit/images/vision/pose-detection/landmarks-fixed.png)

For all the recognizable poses, hundreds of sample images are analyzed and saved in a CSV file named `fitness_pose_samples.csv` and for each line there's a valute to indicates which pose it represents. Once the camera is launched and a human body is detected, the pose is analyzed for each frame, and a coefficient is calculated using the [k-nearest neighbors algorithm](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) (k-NN) to determine how closely the analyzed pose matches those in the CSV file corresponding to the selected pose. If the similarity score exceeds a certain threshold, the result is displayed on the screen.

## Video

Click the image below to watch a visual demonstration on YouTube:

[![IMAGE ALT TEXT HERE](https://i9.ytimg.com/vi/o_zFg-RxnzE/mqdefault.jpg?sqp=COy3mLIG-oaymwEoCMACELQB8quKqQMcGADwAQH4Ab4EgAKACooCDAgAEAEYZSBlKGUwDw==&rs=AOn4CLDCCGCIIaksVvNSJRiP9jWOMrQTeA)](https://www.youtube.com/shorts/o_zFg-RxnzE)


