package com.example.itservice.common.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.admin.admin_dashboard.AdminDashBoradViewModel
import com.example.itservice.admin.login.AdminLoginViewModel
import com.example.itservice.admin.modify_parts.PartsModifyViewModel
import com.example.itservice.admin.modify_products.ProductModifyViewModel
import com.example.itservice.admin.offer_modify.OfferModifyViewModel
import com.example.itservice.admin.registration.AdminRegistrationViewModel
import com.example.itservice.admin.service_modify.ServiceModifyViewModel
import com.example.itservice.common.splash.SplashViewModel
import com.example.itservice.enterprise_user.enterprise_user_login.engineer.dashboard.EngineerDashBoradViewModel
import com.example.itservice.enterprise_user.enterprise_user_login.engineer.login_fragment.EngineerLoginViewModel
import com.example.itservice.enterprise_user.enterprise_user_login.engineer.registration.EngineerRegistrationViewModel
import com.example.itservice.enterprise_user.enterprise_user_login.EnterpriseUserLoginViewModel
import com.example.itservice.enterprise_user.registation.EnterpriseUserRegistrationViewModel
import com.example.itservice.user.ask_service_catagory.SelectServiceCatgoryViewModel
import com.example.itservice.user.ask_service_catagory.ask_for_service.AskServiceViewModel
import com.example.itservice.user.product_catagory.BuyOurProductViewModel
import com.example.itservice.user.product_catagory.product_list.ProductListViewModel
import com.example.itservice.user.login.UserLoginViewModel
import com.example.itservice.user.product_catagory.product_list.product_details.ProductDetailViewModel
import com.example.itservice.user.registation.UserRegistationViewModel
import com.example.itservice.user.taken_service_catagory.TakenServiceCatagoryViewModel
import com.example.itservice.user.taken_service_catagory.service_list.ServiceListViewModel
import com.example.itservice.user.user_dash_board.UserdashboardViewModel
import javax.inject.Inject

class ViewModelProviderFactory @Inject constructor(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SplashViewModel::class.java)) return SplashViewModel() as T
        if(modelClass.isAssignableFrom(UserLoginViewModel::class.java)) return UserLoginViewModel() as T
        if(modelClass.isAssignableFrom(UserRegistationViewModel::class.java)) return UserRegistationViewModel() as T
        if(modelClass.isAssignableFrom(EnterpriseUserLoginViewModel::class.java)) return EnterpriseUserLoginViewModel() as T
        if(modelClass.isAssignableFrom(EnterpriseUserRegistrationViewModel::class.java)) return EnterpriseUserRegistrationViewModel() as T
        if(modelClass.isAssignableFrom(EngineerLoginViewModel::class.java)) return EngineerLoginViewModel() as T
        if(modelClass.isAssignableFrom(EngineerRegistrationViewModel::class.java)) return EngineerRegistrationViewModel() as T
        if(modelClass.isAssignableFrom(AdminRegistrationViewModel::class.java)) return AdminRegistrationViewModel() as T
        if(modelClass.isAssignableFrom(AdminLoginViewModel::class.java)) return AdminLoginViewModel() as T
        if(modelClass.isAssignableFrom(UserdashboardViewModel::class.java)) return UserdashboardViewModel() as T
        if(modelClass.isAssignableFrom(BuyOurProductViewModel::class.java)) return BuyOurProductViewModel() as T
        if(modelClass.isAssignableFrom(ProductListViewModel::class.java)) return ProductListViewModel() as T
        if(modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) return ProductDetailViewModel() as T
        if(modelClass.isAssignableFrom(SelectServiceCatgoryViewModel::class.java)) return SelectServiceCatgoryViewModel() as T
        if(modelClass.isAssignableFrom(AskServiceViewModel::class.java)) return AskServiceViewModel() as T
        if(modelClass.isAssignableFrom(TakenServiceCatagoryViewModel::class.java)) return TakenServiceCatagoryViewModel() as T
        if(modelClass.isAssignableFrom(ServiceListViewModel::class.java)) return ServiceListViewModel() as T
        if(modelClass.isAssignableFrom(AdminDashBoradViewModel::class.java)) return AdminDashBoradViewModel() as T
        if(modelClass.isAssignableFrom(ProductModifyViewModel::class.java)) return ProductModifyViewModel() as T
        if(modelClass.isAssignableFrom(OfferModifyViewModel::class.java)) return OfferModifyViewModel() as T
        if(modelClass.isAssignableFrom(ServiceModifyViewModel::class.java)) return ServiceModifyViewModel() as T
        if(modelClass.isAssignableFrom(PartsModifyViewModel::class.java)) return PartsModifyViewModel() as T
        if(modelClass.isAssignableFrom(EngineerDashBoradViewModel::class.java)) return EngineerDashBoradViewModel() as T

        return super.create(modelClass)
    }
}