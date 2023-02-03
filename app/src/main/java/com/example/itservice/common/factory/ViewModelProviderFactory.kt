package com.example.itservice.common.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.itservice.admin.addOffers.AddOfferViewModel
import com.example.itservice.admin.admin_dashboard.AdminDashBoradViewModel
import com.example.itservice.admin.login.AdminLoginViewModel
import com.example.itservice.common.parts_pack.modify_parts.PartsModifyViewModel
import com.example.itservice.admin.products_pack.add_product.AddProductViewModel
import com.example.itservice.admin.offer_modify.OfferModifyViewModel
import com.example.itservice.admin.products_pack.add_product_catagories.AddProductCatagoryViewModel
import com.example.itservice.admin.registration.AdminRegistrationViewModel
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.ServiceModifyViewModel
import com.example.itservice.common.service_pack.add_catagories.AddCatagoryViewModel
import com.example.itservice.common.service_pack.add_catagories.add_brand.add_service.AddServiceViewModel
import com.example.itservice.admin.service_pack.add_service.add_brandes.add_brand.AddBrandViewModel
import com.example.itservice.common.service_pack.display_service_catagory.DisplayServiceCatagoryViewModel
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.BrandListDisplayViewModel
import com.example.itservice.admin.service_pack.display_service_catagory.display_service_list.display_service.DisplayServiceListViewModel
import com.example.itservice.admin.users_and_engineers_list.UserAndEngineersListViewModel
import com.example.itservice.base.BaseViewModel
import com.example.itservice.common.display_parts.DisplayPartViewModel
import com.example.itservice.common.parts_pack.add_part.AddPartViewModel
import com.example.itservice.common.parts_pack.display_parts.available_parts_fragment.PartsViewModel
import com.example.itservice.common.product_pack.product_list.product_details.buy_product.payment.PayementViewModel
import com.example.itservice.common.service_pack.display_service_catagory.display_brand_list.display_service.show_service.ShowServiceViewModel
import com.example.itservice.common.splash.SplashViewModel
import com.example.itservice.engineer.dashboard.EngineerDashBoradViewModel
import com.example.itservice.engineer.login_fragment.EngineerLoginViewModel
import com.example.itservice.engineer.registration.EngineerRegistrationViewModel
import com.example.itservice.enterprise_user.enterprise_user_login.EnterpriseUserLoginViewModel
import com.example.itservice.enterprise_user.registation.EnterpriseUserRegistrationViewModel
import com.example.itservice.user.ask_service_catagory.UserServiceViewModel
import com.example.itservice.user.product_catagory.BuyOurProductCatagoryDisplayViewModel
import com.example.itservice.user.product_catagory.product_list.ProductListViewModel
import com.example.itservice.user.login.UserLoginViewModel
import com.example.itservice.user.product_catagory.product_list.product_details.ProductDetailViewModel
import com.example.itservice.user.registation.UserRegistationViewModel
import com.example.itservice.common.taken_service_catagory.TakenServiceCatagoryViewModel
import com.example.itservice.common.taken_service_catagory.service_list.ServiceListViewModel
import com.example.itservice.common.taken_service_catagory.service_list.service_modify.search_eng_frag.SearchForViewModel
import com.example.itservice.user.product_catagory.product_list.product_details.buy_product.CartViewModel
import com.example.itservice.user.profile.ProfileViewModel
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
        if(modelClass.isAssignableFrom(BuyOurProductCatagoryDisplayViewModel::class.java)) return BuyOurProductCatagoryDisplayViewModel() as T
        if(modelClass.isAssignableFrom(ProductListViewModel::class.java)) return ProductListViewModel() as T
        if(modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) return ProductDetailViewModel() as T
        if(modelClass.isAssignableFrom(UserServiceViewModel::class.java)) return UserServiceViewModel() as T
        if(modelClass.isAssignableFrom(TakenServiceCatagoryViewModel::class.java)) return TakenServiceCatagoryViewModel() as T
        if(modelClass.isAssignableFrom(ServiceListViewModel::class.java)) return ServiceListViewModel() as T
        if(modelClass.isAssignableFrom(AdminDashBoradViewModel::class.java)) return AdminDashBoradViewModel() as T
        if(modelClass.isAssignableFrom(OfferModifyViewModel::class.java)) return OfferModifyViewModel() as T
        if(modelClass.isAssignableFrom(ServiceModifyViewModel::class.java)) return ServiceModifyViewModel() as T
        if(modelClass.isAssignableFrom(PartsModifyViewModel::class.java)) return PartsModifyViewModel() as T
        if(modelClass.isAssignableFrom(EngineerDashBoradViewModel::class.java)) return EngineerDashBoradViewModel() as T
        if(modelClass.isAssignableFrom(AddServiceViewModel::class.java)) return AddServiceViewModel() as T
        if(modelClass.isAssignableFrom(AddCatagoryViewModel::class.java)) return AddCatagoryViewModel() as T
        if(modelClass.isAssignableFrom(DisplayServiceCatagoryViewModel::class.java)) return DisplayServiceCatagoryViewModel() as T
        if(modelClass.isAssignableFrom(BrandListDisplayViewModel::class.java)) return BrandListDisplayViewModel() as T
        if(modelClass.isAssignableFrom(AddBrandViewModel::class.java)) return AddBrandViewModel() as T
        if(modelClass.isAssignableFrom(DisplayServiceListViewModel::class.java)) return DisplayServiceListViewModel() as T
        if(modelClass.isAssignableFrom(ShowServiceViewModel::class.java)) return ShowServiceViewModel() as T
        if(modelClass.isAssignableFrom(SearchForViewModel::class.java)) return SearchForViewModel() as T
        if(modelClass.isAssignableFrom(PartsViewModel::class.java)) return PartsViewModel() as T
        if(modelClass.isAssignableFrom(DisplayPartViewModel::class.java)) return DisplayPartViewModel() as T
        if(modelClass.isAssignableFrom(AddPartViewModel::class.java)) return AddPartViewModel() as T
        if(modelClass.isAssignableFrom(AddProductViewModel::class.java)) return AddProductViewModel() as T
        if(modelClass.isAssignableFrom(AddProductCatagoryViewModel::class.java)) return AddProductCatagoryViewModel() as T
        if(modelClass.isAssignableFrom(UserAndEngineersListViewModel::class.java)) return UserAndEngineersListViewModel() as T
        if(modelClass.isAssignableFrom(CartViewModel::class.java)) return CartViewModel() as T
        if(modelClass.isAssignableFrom(PayementViewModel::class.java)) return PayementViewModel() as T
        if(modelClass.isAssignableFrom(AddOfferViewModel::class.java)) return AddOfferViewModel() as T
        if(modelClass.isAssignableFrom(BaseViewModel::class.java)) return BaseViewModel() as T
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) return ProfileViewModel() as T
        return super.create(modelClass)
    }
}