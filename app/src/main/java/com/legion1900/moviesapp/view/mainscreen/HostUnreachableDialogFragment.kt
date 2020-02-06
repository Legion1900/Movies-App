package com.legion1900.moviesapp.view.mainscreen

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class HostUnreachableDialogFragment : DialogFragment() {

    @StringRes
    private var msg = 0

    @StringRes
    private var btnText = 0

    private var isDisplayed = false

    private lateinit var positiveCallback: PositiveCallback

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments!!.apply {
            msg = getInt(KEY_MSG)
            btnText = getInt(KEY_BTN)
            positiveCallback = getParcelable(KEY_CALLBACK)
                ?: throw RuntimeException("${PositiveCallback::class} must be set")
        }
        return AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(btnText, positiveCallback::onPositiveClick)
            .create()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isDisplayed) {
            isDisplayed = true
            super.show(manager, tag)
        }
    }

    override fun dismiss() {
        isDisplayed = false
        super.dismiss()
    }

    abstract class PositiveCallback : Parcelable {
        abstract fun onPositiveClick(dialog: DialogInterface, which: Int)

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            /*
            * Nothing to do here.
            * */
        }

        override fun describeContents(): Int = 0
    }

    companion object {
        const val KEY_MSG = "msg"
        const val KEY_BTN = "btn_text"
        const val KEY_CALLBACK = "callback"
    }
}