package org.torproject.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import org.torproject.android.service.util.Prefs

class CustomBridgeBottomSheet(private val callbacks: ConnectionHelperCallbacks) :
    OrbotBottomSheetDialogFragment() {
    companion object {
        const val TAG = "CustomBridgeBottomSheet"
        private val bridgeStatement = Regex("(obfs4|meek|webtunnel)")
    }

    private lateinit var btnAction: Button
    private lateinit var etBridges: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.custom_bridge_bottom_sheet, container, false)
        v.findViewById<View>(R.id.tvCancel).setOnClickListener { dismiss() }
        btnAction = v.findViewById(R.id.btnAction)
        btnAction.setOnClickListener {
            Prefs.setBridgesList(etBridges.text.toString())
            closeAllSheets()
            callbacks.tryConnecting()
        }
        etBridges = v.findViewById(R.id.etBridges)
        configureMultilineEditTextScrollEvent(etBridges)
        var bridges = Prefs.getBridgesList()
        if (!bridges.contains(bridgeStatement)) bridges = ""
        etBridges.setText(bridges)
        updateUi()
        return v
    }

    private fun updateUi() {
        btnAction.isEnabled =
            !(etBridges.text.isEmpty() || !etBridges.text.contains(bridgeStatement))
    }

}
