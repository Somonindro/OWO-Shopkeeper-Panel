package com.shopKPR.network;

import com.shopKPR.bakirKhata.BakirRecord;
import com.shopKPR.homeComponents.accountInfo.notifications.Notifications;
import com.shopKPR.homeComponents.brandsComponent.entity.Brands;
import com.shopKPR.Model.CartListFromClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.Model.ShopKeeperOrders;
import com.shopKPR.Model.Shops;
import com.shopKPR.Model.UserDebts;
import com.shopKPR.Model.UserShopKeeper;
import com.shopKPR.Model.User_debt_details;
import com.shopKPR.categorySpinner.entity.CategoryEntity;
import com.shopKPR.categorySpinner.entity.SubCategoryEntity;
import com.shopKPR.homeComponents.floatingComponents.entities.Deals;
import com.shopKPR.homeComponents.floatingComponents.entities.Gifts;
import com.shopKPR.myShopManagement.userDebts.model.DebtDashBoardResponse;
import com.shopKPR.orderConfirmation.TimeSlot;
import com.shopKPR.qupon.Qupon;
import com.shopKPR.registerRequest.ShopPendingRequest;
import com.shopKPR.shopKeeperPanel.Referral;
import com.shopKPR.shopKeeperSettings.entitiy.ChangeShopInfo;
import com.shopKPR.userRegistration.ShopKeeperUser;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //Time Slots
    @GET("/getAllAvailableTimeSlots")
    Call<List<TimeSlot>> getAllAvailableTimeSlots();

    //Offer Banner
    @GET("/getBannerForSelectedCategories")
    Call<List<String>> bannerImages(@Query("categoryIds") List<Long> categoryIds);

    @GET("/getSubCategoriesPaging")
    Call<List<SubCategoryEntity>> getSubCategoriesPaging(@Query("page") int page,
                                                      @Query("categoryIds") List<Long> categoryIds);
    //Shop Registration
    @POST("/shopRegisterRequest")
    Call<ResponseBody> shopRegisterRequest(@Body ShopPendingRequest shopPendingRequest);

    @POST("/registerShopKeeper")
    Call<ResponseBody> registerShopKeeper(@Body UserShopKeeper userShopKeeper);

    @GET("/getShopKeeper")
    Call<ShopKeeperUser> getShopKeeper(@Query("mobileNumber") String mobileNumber);

    @PUT("/updateShopKeeperInfo")
    Call<ResponseBody> updateShopKeeperInfo(@Body ShopKeeperUser shopKeeperUser);


    //Products Management
    @GET("/getProductByCategories")
    Call<List<OwoProduct>> getProducts(@Query("page") int page,
                                       @Query("product_categories") Long[] categories);

    @GET("/getProductBySpecificCategory")
    Call<List<OwoProduct>> getProductsViaSpecificCategory(@Query("page") int page,
                                                          @Query("productCategory") Long productCategory);

    @GET("/getProductsBySubCategory")
    Call<List<OwoProduct>> getProductsBySubCategory(@Query("page") int page, @Query("subCategoryId") Long subCategoryId);

    @GET("/getBrandsViaCategory")
    Call<List<Brands>> getBrandsViaCategory(
            @Query("number") int number, @Query("categoryIds") List<Long> categoryIds);

    @GET("/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories();

    @GET("/getSpecificCategoryData")
    Call<List<CategoryEntity>> getSpecificCategoryData(@Query("categoryIds") List<Long> categoryIds);

    @GET("/getProductById")
    Call<OwoProduct> getProductById(@Query("productId") Long productId);

    @GET("/getBrandNameViaProductId")
    Call<ResponseBody> getBrandNameViaProductId(@Query("productId") Long productId);

    @GET("/getProductByBrand")
    Call<List<OwoProduct>> getProductViaBrand(
            @Query("page") int page, @Query("brandsId") Long brandsId);

    @GET("/getProductBySubCategory")
    Call<List<OwoProduct>> getProductBySubcategory(@Query("categoryIds") List<Long> categoryIds,
                                                   @Query("subCategoryName") String subCategoryName);

    @POST("/shopKeeperCart")
    Call<ResponseBody> cartListItems(@Body CartListFromClient cartList);

    @GET("/shop_keeper_cart_products")
    Call<List<CartListProduct>> getCartListProducts(
            @Query("mobile_number") String mobile_number
    );

    @PUT("/update_cart_list")
    Call<CartListProduct> updateCartList(
            @Body CartListProduct cartListProduct,
            @Query("mobile_number") String mobile_number
    );

    @DELETE("/delete_cart_product")
    Call<ResponseBody> delete_product_from_cart(
            @Query("product_id") long product_id,
            @Query("mobile_number") String mobile_number
    );

    @POST("/shop_keeper_order")
    Call<ResponseBody> createOrder(
            @Body ShopKeeperOrders shopKeeperOrders,
            @Query("mobile_number") String mobile_number
    );

    @GET("/get_shop_keeper_order")
    Call<List<ShopKeeperOrders>> getShopKeeperOrders(
            @Query("page") int page,
            @Query("mobile_number") String mobile_number
    );

    //Shop Management
    @GET("/getShopInfo")
    Call<Shops> getShopInfo(@Query("shop_phone") String shop_phone);

    @POST("/addUserDebt")
    Call<ResponseBody> addAUserDebt(
            @Body UserDebts userDebts,
            @Query("shop_mobile_number") String mobile_number
    );

    @GET("/getDebtDashBoardEntries")
    Call<DebtDashBoardResponse> getDebtDashBoardEntries(@Query("mobileNumber") String mobileNumber);

    @GET("/getUserDebtLists")
    Call<List<UserDebts>> getUserDebtLists(
            @Query("page") int page,
            @Query("shop_mobile_number") String shop_mobile_number
    );

    @GET("/getUserSpecificDebtDetails")
    Call<UserDebts> getUserDebtDetails(
            @Query("user_id") Long user_id
    );

    @GET("/getADebtListForAUser")
    Call<List<User_debt_details>> getDebtListForAnSpecificUser(@Query("user_id") Long user_id);

    @DELETE("/clearAllDebtDetails")
    Call<ResponseBody> deleteAUserDebtList(@Query("user_id") Long user_id);

    @POST("/addAdebtDetails")
    Call<ResponseBody> addADebtDetailsForACustomer(@Body User_debt_details user_debt_details,
                                                   @Query("user_id") Long user_id);

    @DELETE("/deleteAdebtDetails")
    Call<ResponseBody> deleteADebtDetails(
            @Query("id_of_debt_details") long id_of_debt_details,
            @Query("user_id") long user_id
    );

    @Multipart
    @POST("/imageController/{directory}")
    Call<ResponseBody> uploadImageToServer(
            @Path("directory") String directory,
            @Part MultipartBody.Part multipartFile);

    @DELETE("/getImageFromServer")
    Call<ResponseBody> deleteImage(@Query("path_of_image") String path_of_image);


    @POST("/addPaidAmountForAnUser")
    Call<ResponseBody> addPaidAmountForAnUser(@Query("userId") Long userId, @Query("paidAmount") Double userPaidAmount);


    //Searching and filtering
    @GET("/getAllSubCategoriesForCategory")
    Call<List<String>> getAllSubCategoriesForCategory(@Query("categoryId") Long categoryId);

    @GET("/getAllSubCategories")
    Call<List<SubCategoryEntity>> getAllSubCategories(@Query("categoryId") Long categoryId);

    @GET("/searchProduct")
    Call<List<OwoProduct>> searchProduct(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("product_name") String product_name
    );

    @GET("/searchProductDesc")
    Call<List<OwoProduct>> searchProductDesc(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("product_name") String product_name
    );

    @GET("/sortProductAlphabetically")
    Call<List<OwoProduct>> sortProductAlphabetically(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("alphabet") String alphabet
    );

    //Get similar products based on sub category
    @GET("/getSimilarProducts")
    Call<List<OwoProduct>> getSimilarProducts(@Query("product_sub_category_id") Long product_sub_category_id);


    @GET("/getSubCategoryIdViaName")
    Call<Long> getSubcategoryId(@Query("subCategoryName") String subCategoryName);

    @GET("/getProductsBySubCategory")
    Call<List<OwoProduct>> getSubCategoryBasedProducts(@Query("page") int page,
                                                       @Query("subCategoryId") Long subCategoryId);

    //Add product to wishList
    @POST("/addProductToWishList")
    Call<ResponseBody> addProductToWishList(@Query("shop_keeper_id") Long shop_keeper_id,
                                            @Query("product_id") Long productId);

    @DELETE("/deleteWishListProduct")
    Call<ResponseBody> deleteWishListProduct(@Query("shop_keeper_id") Long shop_keeper_id,
                                             @Query("product_id") Long productId);

    @GET("/getAllWishListProductsId")
    Call<List<Long>> getAllWishListProductsId(@Query("user_id") Long userId);

    @GET("/getAllWishListProducts")
    Call<List<OwoProduct>> wishListProducts(@Query("user_id") Long userId);

    @GET("/getAllBrands")
    Call<List<Brands>> getAllBrandsViaSubCategory(@Query("subCategoryId") Long subCategoryId);

    @GET("/getAllDeals")
    Call<List<Deals>> getAllDeals();

    @GET("/getAllGiftCards")
    Call<List<Gifts>> getGistsCardList();

    @GET("/getAQupon")
    Call<Qupon> getQupon(@Query("quponId") Long quponId);

    @GET("/checkUserAlreadyTakenCouponOrNot")
    Call<ResponseBody> checkUserAlreadyTakenCoupon(@Query("mobileNumber") String mobileNumber,
                                                   @Query("quponId") Long quponId);

    @POST("/addQuponToAnUser")
    Call<ResponseBody> addNewCouponToUser(@Query("mobileNumber") String mobileNumber, @Body Qupon qupon);

    @POST("/addReferralPoint")
    Call<ResponseBody> addReferralPointForUser(@Query("mobileNumber") String mobileNumber);

    @GET("/getReferralPoint")
    Call<Referral> getReferral(@Query("mobileNumber") String mobileNumber);

    @POST("/makeShopInfoChangeRequest")
    Call<ResponseBody> makeShopInfoChangeRequest(@Body ChangeShopInfo changeShopInfo);

    @GET("/getAllNotifications")
    Call<List<Notifications>> getAllNotifications();

    @PUT("/updateShopKeeperPin")
    Call<ResponseBody> changeShopKeeperPin(@Body UserShopKeeper userShopKeeper);


    //Bakir Khata Controller
    @POST("/addNewRecord")
    Call<ResponseBody> addNewBakirRecord(@Body BakirRecord bakirRecord);

    @DELETE("/deleteRecord")
    Call<ResponseBody> deleteRecord(@Query("recordId") Long recordId);

    @GET("/getAllBakirRecord")
    Call<List<BakirRecord>> bakirRecordList(@Query("mobileNumber") String mobileNumber);
}
