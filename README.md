# Body-detection-Android-app

## Introduction
This Android app has been developed as thesis project for the bachelor's degree in computer engineering at the University of Modena and Reggio Emilia. It utilizes [Google's ML Kit](https://developers.google.com/ml-kit) to recognize a pair of movements made by the human body. It was created using [this app](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart) as a reference.
## How it works
On the main screen of the app all the recognizable movements are displayed. Once a movement is selected, the app activates the camera. If a human body is detected, ML Kit is used to track the user's pose, composed of 33 points in a three-dimensional space.

![image](https://developers.google.com/static/ml-kit/images/vision/pose-detection/landmarks-fixed.png)

For each movement there are 2 poses: a starting pose and an ending pose. Every pose has hundreds of sample images that have been analyzed and the results have been saved in a CSV file named [`fitness_pose_samples.csv`](https://github.com/fedef2000/Tesi-triennale/blob/main/app/src/main/assets/pose/fitness_pose_samples.csv).
Once the camera is launched and a human body is detected for every frame the pose is analyzed and a coefficient is calculated using the [k-nearest neighbors algorithm](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) (k-NN) to determine how closely the analyzed pose matches those in the CSV file corresponding to the selected pose. When the probability of the final pose class passes a given threshold for the first time the final pose is marked as entered. When the probability drops below the threshold, the algorithm marks that the final pose class has been exited and the movement is considered finished.
## Video

![](https://github.com/fedef2000/Body-detection-Android-app/blob/main/Il%20mio%20filmato.gif)
