package com.cornucopia.kotlin

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.cornucopia.kotlin.account.AccountManager
import com.cornucopia.kotlin.account.AccountManagerImpl
import com.cornucopia.kotlin.databinding.ActivityRxBinding
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers

class RxKotlinActivity: AppCompatActivity() {

    val wxApi = WXAPIFactory.createWXAPI(this, AccountManager.APP_ID)

    lateinit var binding: ActivityRxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testOne()

        binding.btnWxLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                loginWithWx()
            }
        });

        binding.btnWxMini.setOnClickListener { view ->
            Snackbar.make(view, "mini", Snackbar.LENGTH_SHORT).show()

            when (view)  {
                is Button -> {}
                is ImageButton -> {}
                is ImageView -> {}
                else -> {}
            }
         }

        binding.btnWxShare.setOnClickListener {
            // view not use

            shareWx();
        }

    }

    private fun shareWx() {
        var content = "shared content"

        val textObj = WXTextObject()
        textObj.text = content

        val msg = WXMediaMessage()
        msg.mediaObject = textObj

        msg.description = content

        val msgReq = SendMessageToWX.Req()
        msgReq.transaction = buildTransaction("text")
        msgReq.message = msg
        msgReq.scene = SendMessageToWX.Req.WXSceneSession

        wxApi.sendReq(msgReq)

    }

    fun buildTransaction(type: String): String {
        // trinocular operator not in kotlin
        if (null == type) {
            return System.currentTimeMillis().toString() // not String.valueOf
        } else {
            return type + System.currentTimeMillis()
        }
    }

    private fun testOne() {
        val list = listOf("alpha", "beta", "gamma", "delta", "epsilon")

        list.toObservable()
                .filter { it.length >= 5}
                .subscribeBy(
                        onNext = { Log.i("rx", it) },
                        onError = { it.printStackTrace() },
                        onComplete = { Log.i("rx", "Done!") }
                )
    }

    private fun loginWithWx() {
        val accountManager = AccountManagerImpl(this)
        accountManager.requestWxOpenId()
                .flatMap { accountManager.loginWithWx(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
                        onNext = {  },
                        onError = {},
                        onComplete = {}
                )
    }

    fun schedule(observable: Observable<Any>): Observable<Any> {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }


}