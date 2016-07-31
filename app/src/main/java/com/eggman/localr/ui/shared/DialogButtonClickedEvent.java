package com.eggman.localr.ui.shared;

import android.support.annotation.IntDef;

/**
 * Created by mharris on 7/31/16.
 * eggmanapps.
 */
public class DialogButtonClickedEvent {

    @IntDef({CONFIRM_CLICKED, CANCEL_CLICKED})
    public @interface ButtonAction {}
    public static final int CONFIRM_CLICKED = 0;
    public static final int CANCEL_CLICKED = 1;

    @ButtonAction
    int selectedAction;
    int tag;

    public DialogButtonClickedEvent(int dialogTag, @ButtonAction int  buttonSelection) {
        tag = dialogTag;
        selectedAction = buttonSelection;
    }

    @ButtonAction
    public int getSelectedAction() {
        return selectedAction;
    }

    public int getDialogTag() {
        return tag;
    }
}
