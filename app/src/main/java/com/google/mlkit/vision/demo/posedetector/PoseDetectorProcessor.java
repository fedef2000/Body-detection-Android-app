package com.google.mlkit.vision.demo.posedetector;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.odml.image.MlImage;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.demo.cameraGraphic.GraphicOverlay;
import com.google.mlkit.vision.demo.activities.ShowResultsActivity;
import com.google.mlkit.vision.demo.posedetector.classification.PoseClassifierProcessor;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** A processor to run pose detector. */
public class PoseDetectorProcessor
    extends VisionProcessorBase<PoseDetectorProcessor.PoseWithClassification> {
  private static final String TAG = "PoseDetectorProcessor";
  private float bestPose = 1;
  private final PoseDetector detector;
  private final Context context;
  private final Executor classificationExecutor;

  private PoseClassifierProcessor poseClassifierProcessor;

  private final String file;

  /** Internal class to hold Pose and classification results. */
  protected static class PoseWithClassification {
    private final Pose pose;
    private final String classificationResult;


    public PoseWithClassification(Pose pose, String classificationResult) {
      this.pose = pose;
      this.classificationResult = classificationResult;
    }

    public Pose getPose() {
      return pose;
    }

    public String getClassificationResult() {
      return classificationResult;
    }
  }

  public PoseDetectorProcessor(Context context, PoseDetectorOptionsBase options, String file) {
    super(context);

    detector = PoseDetection.getClient(options);
    this.context = context;
    this.file = file;
    classificationExecutor = Executors.newSingleThreadExecutor();
  }

  @Override
  public void stop() {
    super.stop();
    detector.close();
  }

  @Override
  protected Task<PoseWithClassification> detectInImage(InputImage image) {
    return detector
        .process(image)
        .continueWith(
            classificationExecutor,
            task -> {
              Pose pose = task.getResult();
              if (poseClassifierProcessor == null) {
                poseClassifierProcessor = new PoseClassifierProcessor(context, file);
              }
              String classificationResult = poseClassifierProcessor.getPoseResult(pose);
              return new PoseWithClassification(pose, classificationResult);
            });
  }

  @Override
  protected Task<PoseWithClassification> detectInImage(MlImage image) {
    return detector
        .process(image)
        .continueWith(
            classificationExecutor,
            task -> {
              Pose pose = task.getResult();
                if (poseClassifierProcessor == null) {
                  poseClassifierProcessor = new PoseClassifierProcessor(context, file);
                }
                String classificationResult = poseClassifierProcessor.getPoseResult(pose);

                if(!classificationResult.equals("")){
                  if(Float.parseFloat(classificationResult) < bestPose){
                    bestPose = Float.parseFloat(classificationResult);
                  };

                  if(poseClassifierProcessor.isFinished()){
                    Intent intent = new Intent(context, ShowResultsActivity.class);
                    // Optionally, if you need to pass data to the new activity, you can use Intent extras
                    intent.putExtra("classification", String.valueOf(bestPose));
                    // Start the new activity
                    context.startActivity(intent);
                    return new PoseWithClassification(pose, classificationResult);
                  }

                }
              return new PoseWithClassification(pose, classificationResult);
            });


  }

  @Override
  protected void onSuccess(
      @NonNull PoseWithClassification poseWithClassification,
      @NonNull GraphicOverlay graphicOverlay) {
    List<String> l = new ArrayList<>();
    l.add(poseWithClassification.classificationResult);
    graphicOverlay.add(
        new PoseGraphic(
            graphicOverlay,
            poseWithClassification.pose,
            false,
            true,
            true,
            l));
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.e(TAG, "Pose detection failed!", e);
  }

  @Override
  protected boolean isMlImageEnabled(Context context) {
    // Use MlImage in Pose Detection by default, change it to OFF to switch to InputImage.
    return true;
  }

  public float getBestPose() {
    return bestPose;
  }

}
