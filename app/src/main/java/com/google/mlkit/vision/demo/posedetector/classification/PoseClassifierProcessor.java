/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.posedetector.classification;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.WorkerThread;
import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts a stream of {@link Pose} for classification and check if movement is finished.
 */
public class PoseClassifierProcessor {
  private static final String TAG = "PoseClassifierProcessor";
  private static final String POSE_SAMPLES_FILE = "pose/fitness_pose_samples.csv";
  private final String file;
  private EMASmoothing emaSmoothing;
  private RepetitionCounter repCounter;
  private PoseClassifier poseClassifier;
  private boolean isFinished = false;
  @WorkerThread
  public PoseClassifierProcessor(Context context, String file) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    this.file = file;
    emaSmoothing = new EMASmoothing();
    loadPoseSamples(context);
  }

  private void loadPoseSamples(Context context) {
    List<PoseSample> poseSamples = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples.\n" + e);
    }
    poseClassifier = new PoseClassifier(poseSamples);

    repCounter = new RepetitionCounter(file);
  }

  /**
   * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
   * classification results.
   *
   * <p>Currently it returns up to 2 strings as following:
   * 0: PoseClass : X reps
   * 1: PoseClass : [0.0-1.0] confidence
   */
  @WorkerThread
  public String getPoseResult(Pose pose) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    ClassificationResult classification = poseClassifier.classify(pose);

    // Feed pose to smoothing even if no pose found.
    classification = emaSmoothing.getSmoothedResult(classification);

    // Return early without updating repCounter if no pose found.
    if (pose.getAllPoseLandmarks().isEmpty()) {
      return "";
    }

    int repsBefore = repCounter.getNumRepeats();
    int repsAfter = repCounter.addClassificationResult(classification);
    if (repsAfter > repsBefore) {
      this.setFinished(true);
    }



    // return maxConfidence class of current frame if pose is found.
    if (!pose.getAllPoseLandmarks().isEmpty()) {
      String maxConfidenceClass = classification.getMaxConfidenceClass();
      return String.valueOf((
              classification.getClassConfidence(maxConfidenceClass)
              / poseClassifier.confidenceRange()));
    }

    return "";
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }

  public boolean isFinished() {
    return isFinished;
  }

}
