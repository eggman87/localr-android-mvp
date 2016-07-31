package com.eggman.localr.ui.shared;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eggman.localr.LocalApplication;
import com.eggman.localr.R;
import com.eggman.localr.databinding.FragmentDialogAlertBinding;
import com.eggman.localr.utils.RxBus;

import javax.inject.Inject;

/**
 * Created by mharris on 7/31/16.
 * eggmanapps.
 */
public class AlertDialogFragment extends DialogFragment {

    @Inject
    protected RxBus bus;

    private FragmentDialogAlertBinding binding;
    private int bodyRes;
    private int confirmRes;
    private int cancelRes;
    private int dialogTag;

    private static final String ARGS_BODY_TEXT = "args_body_text";
    private static final String ARGS_CONFIRM_TEXT = "args_confirm_text";
    private static final String ARGS_CANCEL_TEXT = "args_cancel_text";
    private static final String ARGS_DIALOG_TAG = "args_dialog_tag";

    public static AlertDialogFragment newInstance(int tag, @StringRes int bodyRes, @StringRes int confirmRes, @StringRes int cancelRes) {
        AlertDialogFragment fragment = new AlertDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_DIALOG_TAG, tag);
        args.putInt(ARGS_BODY_TEXT, bodyRes);
        args.putInt(ARGS_CONFIRM_TEXT, confirmRes);
        args.putInt(ARGS_CANCEL_TEXT, cancelRes);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((LocalApplication) getActivity().getApplication()).component().inject(this);

        setStyle(STYLE_NO_TITLE, R.style.AlertDialog_Floating);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_alert, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        processArguments();
        setDataToView();
        attachListeners();
    }

    private void processArguments() {
        Bundle args = getArguments();
        if (args != null) {
            bodyRes = args.getInt(ARGS_BODY_TEXT);
            confirmRes = args.getInt(ARGS_CONFIRM_TEXT);
            cancelRes = args.getInt(ARGS_CANCEL_TEXT);
            dialogTag = args.getInt(ARGS_DIALOG_TAG);
        }
    }

    private void setDataToView() {
        binding.fragDialogPromptTvBody.setText(bodyRes);
        binding.fragDialogPromptBtnConfirm.setText(confirmRes);
        binding.fragDialogPromptBtnCancel.setText(cancelRes);
    }

    private void attachListeners() {
        binding.fragDialogPromptBtnCancel.setOnClickListener(view ->
                sendEvent(new DialogButtonClickedEvent(dialogTag, DialogButtonClickedEvent.CANCEL_CLICKED)));
        binding.fragDialogPromptBtnConfirm.setOnClickListener(view ->
                sendEvent(new DialogButtonClickedEvent(dialogTag, DialogButtonClickedEvent.CONFIRM_CLICKED)));
    }

    private void sendEvent(DialogButtonClickedEvent event) {
        bus.send(event);
        dismiss();
    }
}
