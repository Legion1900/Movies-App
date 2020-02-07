package com.legion1900.moviesapp.view.dialogs

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        getArgs()
        return AlertDialog.Builder(context)
            .setMessage(msg)
            .setPositiveButton(btnText, positiveCallback::onPositiveClick)
            .create()
    }

    private fun getArgs() {
        arguments?.apply {
            msg = getInt(KEY_MSG)
            btnText = getInt(KEY_BTN)
            positiveCallback = getParcelable(KEY_CALLBACK)
                ?: throw RuntimeException("${PositiveCallback::class} must be set")
        } ?: throw java.lang.RuntimeException("Arguments must be provided")
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isDisplayed) {
            isDisplayed = true
            super.show(manager, tag)
        }
    }

    override fun dismiss() {
        if (isDisplayed) {
            isDisplayed = false
            super.dismiss()
        }
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

        fun create(
            @StringRes msgTxt: Int,
            @StringRes btnTxt: Int,
            callback: PositiveCallback
        ): HostUnreachableDialogFragment {
            val fragment =
                HostUnreachableDialogFragment()
            val args = Bundle().apply {
                putInt(KEY_MSG, msgTxt)
                putInt(KEY_BTN, btnTxt)
                putParcelable(KEY_CALLBACK, callback)
            }
            fragment.arguments = args
            return fragment
        }
    }
}