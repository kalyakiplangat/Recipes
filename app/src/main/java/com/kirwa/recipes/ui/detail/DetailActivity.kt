package com.kirwa.recipes.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kirwa.recipes.GeneralResponse
import com.kirwa.recipes.R
import com.kirwa.recipes.databinding.ActivityDetailBinding
import com.kirwa.recipes.logger
import com.kirwa.recipes.network.RecipeApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity(), PurchasesUpdatedListener {

    lateinit var binding: ActivityDetailBinding
    lateinit var buttonSubscribe: Button

    private lateinit var billingClient: BillingClient
    private val skuList = listOf("test_product_one", "test_product_two")

    private val apiClient by lazy {
        RecipeApiClient.getClient()
    }
    private var disposable = CompositeDisposable()
    private val status = MutableLiveData<GeneralResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        buttonSubscribe = binding.btnSubscribe

        val intent = intent.getStringExtra("rId")
        intent?.let { getRecipeDetail(it) }

        setupBillingClient()

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

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    logger("Setup Billing Done")
                    loadAllSKUs()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                logger("Failed")

            }
        })

    }

    private fun loadAllSKUs(){
        if (billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()
            billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
                // Process the result.
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                    for (skuDetails in skuDetailsList!!) {
                        if (skuDetails.sku == "test_product_one")
                            btnSubscribe.isEnabled = true
                            btnSubscribe.setOnClickListener {
                                val billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                billingClient.launchBillingFlow(this, billingFlowParams)
                            }
                    }
                }
                logger("Billing Client ready")
                Log.d("Ready", "Billing Client ready")

            }

        } else {
            logger("Billing Client not ready")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                acknowledgePurchase(purchase.purchaseToken)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            logger("User Cancelled")
            logger(billingResult.debugMessage.toString())
            Log.d("Cancelled", "Billing Client not Cancelled")

        } else {
            logger(billingResult.debugMessage.toString())
            // Handle any other error codes.
        }
    }


    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            logger(debugMessage)
            logger(responseCode)
            Log.d("success", "Billing Client purchase")
        }
    }
}