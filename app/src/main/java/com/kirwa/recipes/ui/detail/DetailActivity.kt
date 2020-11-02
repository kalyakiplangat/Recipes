package com.kirwa.recipes.ui.detail

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.PointerIconCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.util.BillingHelper
import com.android.vending.billing.IInAppBillingService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kirwa.recipes.GeneralResponse
import com.kirwa.recipes.NetworkUtil
import com.kirwa.recipes.R
import com.kirwa.recipes.databinding.ActivityDetailBinding
import com.kirwa.recipes.network.RecipeApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(){

    lateinit var binding: ActivityDetailBinding
    lateinit var buttonSubscribe: Button

    private val skuList = listOf("test_product_one", "test_product_two")

    private val apiClient by lazy {
        RecipeApiClient.getClient()
    }
    private var disposable = CompositeDisposable()
    private val status = MutableLiveData<GeneralResponse>()

    private var mService: IInAppBillingService? = null
    private val mServiceConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IInAppBillingService.Stub.asInterface(service)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        buttonSubscribe = binding.btnSubscribe
        val context = applicationContext

        val intent = intent.getStringExtra("rId")
        intent?.let { getRecipeDetail(it) }

        buttonSubscribe.setOnClickListener {
            if (NetworkUtil.hasNetwork(this@DetailActivity)) {
                var bundle: Bundle? = null
                try {
                    bundle = this@DetailActivity.mService!!.getBuyIntent(
                        3,
                        this@DetailActivity.getPackageName(),
                        "test_product_3",
                        BillingClient.SkuType.SUBS,
                        "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ"
                    )
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
                try {
                    this@DetailActivity.startIntentSenderForResult(
                        (bundle!!.getParcelable<Parcelable>(
                            BillingHelper.RESPONSE_BUY_INTENT_KEY
                        ) as PendingIntent?)!!.intentSender,
                        PointerIconCompat.TYPE_CONTEXT_MENU,
                        Intent(),
                        Integer.valueOf(0).toInt(),
                        Integer.valueOf(0).toInt(),
                        Integer.valueOf(0).toInt()
                    )
                } catch (e2: SendIntentException) {
                    e2.printStackTrace()
                }
            } else {
                val builder = AlertDialog.Builder(this@DetailActivity)
                builder.setTitle("No internet connection" as CharSequence)
                builder.setMessage("Please make sure you have a working internet connection" as CharSequence)
                builder.show()
            }
        }

        val intent1 = Intent("com.android.vending.billing.InAppBillingService.BIND")
        intent1.setPackage("com.android.vending")
        bindService(intent1, mServiceConn, BIND_AUTO_CREATE)
        val sku = ArrayList<String>()
        sku.add("test_product_3")
        val bundle2 = Bundle()
        bundle2.putStringArrayList("ITEM_ID_LIST", sku)
        Handler().postDelayed({
            try {
                val skuDetails: Bundle = this@DetailActivity.mService!!.getSkuDetails(
                    3,
                    this@DetailActivity.getPackageName(),
                    BillingClient.SkuType.SUBS,
                    bundle2
                )
                if (skuDetails.getInt(BillingHelper.RESPONSE_CODE) == 0) {
                    val it: Iterator<*> =
                        skuDetails.getStringArrayList(BillingHelper.RESPONSE_GET_SKU_DETAILS_LIST)!!
                            .iterator()
                    while (it.hasNext()) {
                        var jSONObject: JSONObject? = null
                        try {
                            jSONObject = JSONObject(it.next() as String?)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        try {
                            jSONObject!!.getString("productId")
                        } catch (e2: JSONException) {
                            e2.printStackTrace()
                        }
                    }
                }
            } catch (e3: Exception) {
                Log.e("trist", e3.toString())
            }
        }, 3000)
    }

    private fun getRecipeDetail(rId: String) {
        disposable.add(apiClient.getRecipeDetail(rId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    status.value = GeneralResponse.SUCCESS
                    val ingredients = it.recipe.ingredients
                    val ingredientsList = StringBuilder()
                    ingredientsList.append("Ingredients:\n")
                    recipe_title.text = it.recipe.title.toString()
//                    recipe_description.text = it.recipe.ingredients.toString()
                    for (i in ingredients.indices) {
                        ingredientsList.append("${i + 1} .  ${ingredients[i]}\n")
                    }
                    recipe_description.text = ingredientsList.toString()
                    recipe_ratings.text = it.recipe.socialRank.roundToInt().toString()
                    Glide.with(recipeImageView.context)
                        .load(it.recipe.imageUrl)
                        .apply(
                            RequestOptions().placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image)
                        )
                        .into(recipeImageView)
                },
                {
                    this::onError
                    status.value = GeneralResponse.ERROR
                }
            ))

    }

    private fun onError(e: Throwable) {
        e.printStackTrace()
    }

}