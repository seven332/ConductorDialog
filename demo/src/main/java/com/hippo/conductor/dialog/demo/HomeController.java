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

package com.hippo.conductor.dialog.demo;

/*
 * Created by Hippo on 4/2/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.hippo.conductor.dialog.DialogController;
import com.hippo.conductor.dialog.SimpleDialogChangeHandler;

public class HomeController extends Controller {

  @NonNull
  @Override
  protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, Bundle bundle) {
    View view = inflater.inflate(R.layout.controller_home, container, false);
    ListView list = (ListView) view.findViewById(R.id.list);
    list.setAdapter(new ArrayAdapter<>(
        inflater.getContext(), android.R.layout.simple_list_item_1,
        new String[] {
            "Not Cancelled",
            "Cancelled",
            "Cancelled on Touch Outside",
            "Custom Theme",
        }));
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Router router = getRouter();
        if (router == null) {
          return;
        }

        DialogController dialog;
        if (position <= 2) {
          dialog = new CustomDialog();
          switch (position) {
            case 0:
              dialog.setCancellable(false);
              dialog.setCancelledOnTouchOutside(false);
              break;
            case 1:
              dialog.setCancellable(true);
              dialog.setCancelledOnTouchOutside(false);
              break;
            case 2:
              dialog.setCancellable(true);
              dialog.setCancelledOnTouchOutside(true);
              break;
          }
        } else {
          dialog = new CustomDialog(R.style.CustomDialog);
        }
        router.pushController(
            RouterTransaction.with(dialog)
                .pushChangeHandler(new SimpleDialogChangeHandler())
                .popChangeHandler(new SimpleDialogChangeHandler())
        );
      }
    });
    return view;
  }
}
