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
 * Created by Hippo on 4/1/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.util.TypedValue;

class ResourcesUtils {
  private ResourcesUtils() {}

  /** Lock object used to protect access to {@link #tmpValue}. */
  private static final Object tmpValueLock = new Object();
  /** Single-item pool used to minimize TypedValue allocations. */
  private static TypedValue tmpValue = new TypedValue();

  /**
   * Returns a TypedValue suitable for temporary use. The obtained TypedValue
   * should be released using {@link #releaseTempTypedValue(TypedValue)}.
   *
   * @return a typed value suitable for temporary use
   */
  private static TypedValue obtainTempTypedValue() {
    TypedValue tmpValue = null;
    synchronized (tmpValueLock) {
      if (ResourcesUtils.tmpValue != null) {
        tmpValue = ResourcesUtils.tmpValue;
        ResourcesUtils.tmpValue = null;
      }
    }
    if (tmpValue == null) {
      return new TypedValue();
    }
    return tmpValue;
  }

  /**
   * Returns a TypedValue to the pool. After calling this method, the
   * specified TypedValue should no longer be accessed.
   *
   * @param value the typed value to return to the pool
   */
  private static void releaseTempTypedValue(TypedValue value) {
    synchronized (tmpValueLock) {
      if (tmpValue == null) {
        tmpValue = value;
      }
    }
  }

  /**
   * Resolve a attribute value for a particular attribute ID.
   *
   * @param context the context to resolve from
   * @param attrId the desired attribute identifier
   * @param value the value container
   * @throws Resources.NotFoundException if can't resolve the given ID
   */
  private static void resolveAttribute(
      Context context, int attrId, TypedValue value, boolean resolveRefs)
      throws Resources.NotFoundException {
    if (!context.getTheme().resolveAttribute(attrId, value, resolveRefs)) {
      throw new Resources.NotFoundException(
          "Can't resolve attribute ID #0x" + Integer.toHexString(attrId));
    }
  }

  /**
   * Resolve an floating-point associated with a particular attribute ID.
   *
   * @param context the context to resolve from
   * @param id the desired attribute identifier
   * @return the floating-point value
   * @throws Resources.NotFoundException if the given ID does not exist
   */
  public static float getAttrFloat(@NonNull Context context, @AttrRes int id)
      throws Resources.NotFoundException {
    final TypedValue value = obtainTempTypedValue();
    try {
      resolveAttribute(context, id, value, true);
      if (value.type == TypedValue.TYPE_FLOAT) {
        return value.getFloat();
      }
      throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id)
          + " type #0x" + Integer.toHexString(value.type) + " is not valid");
    } finally {
      releaseTempTypedValue(value);
    }
  }

  /**
   * Resolve a dimensional for a particular attribute ID. Unit
   * conversions are based on the current {@link android.util.DisplayMetrics}
   * associated with the content.
   *
   * @param context the context to resolve from
   * @param id the desired attribute identifier
   * @return resource dimension value multiplied by the appropriate
   * @throws Resources.NotFoundException if the given ID does not exist
   */
  public static float getAttrDimension(@NonNull Context context, @AttrRes int id)
      throws Resources.NotFoundException {
    final TypedValue value = obtainTempTypedValue();
    try {
      resolveAttribute(context, id, value, true);
      if (value.type == TypedValue.TYPE_DIMENSION) {
        return TypedValue.complexToDimension(
            value.data, context.getResources().getDisplayMetrics());
      }
      throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id)
          + " type #0x" + Integer.toHexString(value.type) + " is not valid");
    } finally {
      releaseTempTypedValue(value);
    }
  }

  /**
   * Resolve a drawable object associated with a particular attribute ID.
   *
   * @param context the context to resolve from
   * @param id the desired attribute identifier
   * @return Drawable An object that can be used to draw this resource.
   * @throws Resources.NotFoundException if the given ID does not exist
   */
  public static Drawable getAttrDrawable(@NonNull Context context, @AttrRes int id)
      throws Resources.NotFoundException {
    final TypedValue value = obtainTempTypedValue();
    try {
      resolveAttribute(context, id, value, false);
      if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT
          && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
        return new ColorDrawable(value.data);
      } else if (value.type == TypedValue.TYPE_REFERENCE) {
        return AppCompatResources.getDrawable(context, value.data);
      }
      throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id)
          + " type #0x" + Integer.toHexString(value.type) + " is not valid");
    } finally {
      releaseTempTypedValue(value);
    }
  }
}
