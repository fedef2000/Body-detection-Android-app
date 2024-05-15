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

package com.google.mlkit.vision.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.mlkit.vision.demo.BuildConfig;
import com.google.mlkit.vision.demo.R;

/** Chooser which allows you pick from all available poses. */
public final class ChooserActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {
  private static final Exercise[] EXERCISES =
          new Exercise[]{
                  new Exercise(R.drawable.squat_image,"SQUAT", "squats_down"),
                  new Exercise(R.drawable.pushup_image,"PUSH UP", "pushups_down"),
          };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
          new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
      StrictMode.setVmPolicy(
          new StrictMode.VmPolicy.Builder()
              .detectLeakedSqlLiteObjects()
              .detectLeakedClosableObjects()
              .penaltyLog()
              .build());
    }
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chooser);

    // Set up ListView and Adapter
    ListView listView = findViewById(R.id.test_activity_list_view);

    MyArrayAdapter adapter = new MyArrayAdapter(this, R.layout.simple_list_item_2, EXERCISES);

    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    String file = EXERCISES[position].getFile();
    Intent intent = new Intent(this, LivePreviewActivity.class);
    // Optionally, if you need to pass data to the new activity, you can use Intent extras
    intent.putExtra("file", file);
    // Start the new activity
    startActivity(intent);



  }

  private static class MyArrayAdapter extends ArrayAdapter<Exercise> {
    private final Context context;
    private final Exercise[] items;
    MyArrayAdapter(Context context, int resource, Exercise[] objects) {
      super(context, resource, objects);

      this.context = context;
      items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_2, parent, false);
      }
      ((TextView) convertView.findViewById(R.id.text1)).setText(items[position].getTitle());
      ((ImageView) convertView.findViewById(R.id.image)).setImageResource(items[position].getImage());

      return convertView;
    }
  }

  private static class Exercise {
    private String title;
    private int image;
    private String file;
    public Exercise(int image, String title, String file) {
      this.image = image;
      this.title = title;
      this.file = file;
    }

    public int getImage() {
      return image;
    }

    public String getTitle() {
      return title;
    }
    public String getFile() {return file;}
  }
}
