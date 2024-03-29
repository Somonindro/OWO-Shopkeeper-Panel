package com.shopKPR.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.Shops;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.adapters.MyShopManagementAdapter;
import com.shopKPR.adapters.Saleable_products;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShopActivity extends AppCompatActivity {

    private TextView shopName, shop_address, shop_service_mobile;
    private ImageView shop_image;
    private TextView referralPoint;
    private Shops shops = new Shops();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        RecyclerView shop_controlling_options = findViewById(R.id.shop_control_options);

        shopName = findViewById(R.id.my_shop_name);
        shop_address = findViewById(R.id.my_shop_address);
        shop_service_mobile = findViewById(R.id.my_shop_phone_number);
        shop_image = findViewById(R.id.my_shop_image);
        referralPoint = findViewById(R.id.referral_point);

        fetchFromDatabase();
        getReferralPoint();

        MyShopManagementAdapter myShopManagementAdapter = new MyShopManagementAdapter(MyShopActivity.this);

        shop_controlling_options.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                {
                    return 6;
                }
                else
                    return 2;
            }
        });

        shop_controlling_options.setLayoutManager(layoutManager);

        ConcatAdapter concatAdapter  = new ConcatAdapter(new Saleable_products(this), myShopManagementAdapter);

        shop_controlling_options.setAdapter(concatAdapter);
        ImageView back_button = findViewById(R.id.back_to_home);

        back_button.setOnClickListener(v -> onBackPressed());
    }

    private void getReferralPoint() {
        RetrofitClient.getInstance().getApi()
                .getReferral(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<Referral>() {
                    @Override
                    public void onResponse(@NotNull Call<Referral> call, @NotNull Response<Referral> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            referralPoint.setText(String.valueOf(response.body().getReferPoint()));
                        }
                        else
                        {
                            Toast.makeText(MyShopActivity.this, "You have not referred anyone yet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Referral> call, @NotNull Throwable t) {
                        Log.e("Referr", t.getMessage());
                        Toast.makeText(MyShopActivity.this, "Can not get referral point", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void fetchFromDatabase() {

        RetrofitClient.getInstance().getApi().getShopInfo(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if (response.isSuccessful()) {
                            shops = response.body();
                            assert shops != null;
                            shop_address.setText(shops.getShop_address());
                            shop_service_mobile.setText(shops.getShop_service_mobile());
                            shopName.setText(shops.getShop_name());

                            Glide.with(MyShopActivity.this).load(HostAddress.HOST_ADDRESS.getHostAddress() +
                                    shops.getShop_image_uri()).diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shop_image);

                        } else {
                            Toast.makeText(MyShopActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                        Toast.makeText(MyShopActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
