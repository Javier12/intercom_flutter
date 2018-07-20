package com.getchange.intercomflutter

import android.app.Application
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.identity.Registration

class IntercomFlutterPlugin(private val application: Application) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "intercom_flutter")
            channel.setMethodCallHandler(IntercomFlutterPlugin(registrar.context() as Application))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "initialize" -> {
                val apiKey = call.argument<String>("apiKey")
                val appId = call.argument<String>("appId")
                Intercom.initialize(application, apiKey, appId)
                result.success("Intercom initialized")
            }
            call.method == "registerIdentifiedUser" -> {
                val userId = call.argument<String>("userId")
                val registration = Registration.create().withUserId(userId)
                Intercom.client().registerIdentifiedUser(registration)
                result.success("User created")
            }
            call.method == "registerUnidentifiedUser" -> {
                Intercom.client().registerUnidentifiedUser()
                result.success("User created")
            }
            call.method == "logout" -> {
                Intercom.client().logout()
                result.success("logout")
            }
            call.method == "setLauncherVisibility" -> {
                val visibility = call.argument<String>("visibility")
                Intercom.client().setLauncherVisibility(Intercom.Visibility.valueOf(visibility))
                result.success("Showing launcher")
            }
            call.method == "displayMessenger" -> {
                Intercom.client().displayMessenger()
                result.success("Launched")
            }
            else -> result.notImplemented()
        }
    }
}
