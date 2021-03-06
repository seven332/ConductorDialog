/*
 * Copyright 2017 Hippo Seven
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

package com.hippo.conductor.dialog;

/*
 * Created by Hippo on 4/2/2017.
 */

import android.support.annotation.NonNull;
import android.view.View;

/**
 * The dialog root view.
 * The view of {@link DialogController} implements it.
 */
public interface DialogRoot {

  /**
   * Returns the dialog content.
   * <p>
   * The content is a view which uses {@link android.R.attr#windowBackground} as background.
   */
  @NonNull
  View getDialogContent();

  /**
   * Returns the background dim amount which is in a range from 0.0 to 1.0.
   * It's described as {@link android.R.attr#backgroundDimAmount}.
   */
  float getBackgroundDimAmount();
}
