/*
 * Copyright (C) 2010 Daniel Nilsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syncworks.colorpickerview;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ColorPickerDialog extends AlertDialog implements
		ColorPickerView.OnColorChangedListener {

	private ColorPickerView mColorPicker;

	private ColorPanelView mNewColor;

	private ColorPickerView.OnColorChangedListener mListener;

	public ColorPickerDialog(Context context, int initialColor) {
		this(context, initialColor, null);

		init(initialColor);
	}
	
	public ColorPickerDialog(Context context, int initialColor, ColorPickerView.OnColorChangedListener listener) {
		super(context);
		mListener = listener;
		init(initialColor);
	}

	private void init(int color) {
		// To fight color branding.
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setUp(color);
	}

	private void setUp(int color) {
		boolean isLandscapeLayout = false;
		
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_color_picker, null);

		setView(layout);

		setTitle("Pick a Color");
		// setIcon(android.R.drawable.ic_dialog_info);
			
		LinearLayout landscapeLayout = (LinearLayout) layout.findViewById(R.id.dialog_color_picker_extra_layout_landscape);
		
		if(landscapeLayout != null) {
			isLandscapeLayout = true;
		}
		
				
		mColorPicker = (ColorPickerView) layout
				.findViewById(R.id.color_picker_view);
		mNewColor = (ColorPanelView) layout.findViewById(R.id.color_panel_new);


		if(!isLandscapeLayout) {
			
		}
		else {
			landscapeLayout.setPadding(0, 0, Math.round(mColorPicker.getDrawingOffset()), 0);
			setTitle(null);
		}

		mColorPicker.setOnColorChangedListener(this);

		mColorPicker.setColor(color, true);

	}

	@Override
	public void onColorChanged(int color) {
		mNewColor.setColor(color);

		if (mListener != null) {
			mListener.onColorChanged(color);
		}

	}

	public void setAlphaSliderVisible(boolean visible) {
		mColorPicker.setAlphaSliderVisible(visible);
	}

	public int getColor() {
		return mColorPicker.getColor();
	}

}
