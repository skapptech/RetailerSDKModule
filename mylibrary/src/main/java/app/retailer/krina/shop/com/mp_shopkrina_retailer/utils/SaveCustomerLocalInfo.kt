package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.content.Context
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.UserAuth
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs

class SaveCustomerLocalInfo {
    companion object {
        fun saveCustomerInfo(
            applicationContext: Context,
            customer: CustomerResponse?,
            isSplash: Boolean
        ) {
            if (customer != null) {
                SharePrefs.getInstance(applicationContext)
                    .putInt(SharePrefs.CUSTOMER_ID, customer.customerId)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.SK_CODE, customer.skcode)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CUSTOMER_NAME, customer.name)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.MOBILE_NUMBER, customer.mobile)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.SHOP_NAME, customer.shopName)
                SharePrefs.getInstance(applicationContext)
                    .putInt(SharePrefs.COMPANY_ID, customer.companyId)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CUSTOMER_TYPE, customer.customerType)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.SHIPPING_ADDRESS, customer.shippingAddress)
                SharePrefs.getInstance(applicationContext)
                    .putInt(SharePrefs.WAREHOUSE_ID, customer.warehouseid)
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.IS_SIGN_UP, customer.isSignup)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CUSTOMER_EMAIL, customer.emailid)
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.CUST_ACTIVE, customer.active)
                SharePrefs.getInstance(applicationContext)
                    .putInt(SharePrefs.CITY_ID, customer.cityid)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CITY_NAME, customer.city)
                SharePrefs.getInstance(applicationContext).putString(
                    SharePrefs.USER_PROFILE_IMAGE,
                    customer.uploadProfilePichure
                )
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.PASSWORD, customer.password)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CLUSTER_ID, customer.clusterId)

                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.LATITUDE, customer.lat)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.LONGITUDE, customer.lg)

                if (isSplash) {
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.LICENSE_NO, customer.licenseNumber)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.GST_NO, customer.refNo)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.CUSTOMER_VERIFY, customer.customerVerify)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_PRIME_MEMBER, customer.isPrimeCustomer)
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.PRIME_EXPIRY, customer.primeEndDate)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IsKPP, customer.isKPP)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_COMPANY_API_CALL, true)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.IS_FAV_API_CALL, true)
                    SharePrefs.getInstance(applicationContext)
                        .putBoolean(SharePrefs.CART_AMOUNT_API_CALL, false)
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_REQUIRED_LOCATION,
                        customer.isRequiredLocation
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_WAREHOUSE_AVAIL,
                        customer.isWarehouseLive
                    )
                    SharePrefs.getInstance(applicationContext).putBoolean(
                        SharePrefs.IS_SELLER_AVAIL,
                        customer.isSellerAvailable
                    )
                }

            }
        }

        fun saveTokenInfo(
            applicationContext: Context,
            userAut: UserAuth?
        ) {
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.TOKEN_PASSWORD, userAut!!.password)
            SharePrefs.getInstance(applicationContext)
                .putString(SharePrefs.TOKEN_NAME, userAut.userName)

        }
    }
}