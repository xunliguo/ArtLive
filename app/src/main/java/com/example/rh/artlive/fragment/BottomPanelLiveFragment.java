package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.live.InputPanel;

/**
 * Created by rh on 2018/1/2.
 */

public class BottomPanelLiveFragment extends Fragment {

    private static final String TAG = "BottomPanelLiveFragment";

    private ViewGroup buttonPanel;
    private ImageView btnInput;
    private InputPanel inputPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_bottombar, container);
        buttonPanel = (ViewGroup) view.findViewById(R.id.button_panel);
        btnInput = (ImageView) view.findViewById(R.id.btn_input);
        inputPanel = (InputPanel) view.findViewById(R.id.input_panel);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPanel.setVisibility(View.GONE);
                inputPanel.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    /**
     * back键或者空白区域点击事件处理
     *
     * @return 已处理true, 否则false
     */
    public boolean onBackAction() {
        if (inputPanel.onBackAction()) {
            return true;
        }
        if (buttonPanel.getVisibility() != View.VISIBLE) {
            inputPanel.setVisibility(View.GONE);
            buttonPanel.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public void setInputPanelListener(InputPanel.InputPanelListener l) {
        inputPanel.setPanelListener(l);
    }
}
