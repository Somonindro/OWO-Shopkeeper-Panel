package com.shopKPR.homeComponents.bannerComponents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PowerOfSuspensionAdapter extends RecyclerView.Adapter<PowerOfSuspensionAdapter.ViewHolder>
{
    private final Context mctx;
    private final List<OwoProduct> owoProductList;

    public PowerOfSuspensionAdapter(Context mctx, List<OwoProduct> owoProductList) {
        this.mctx = mctx;
        this.owoProductList = owoProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mctx).inflate(R.layout.horizontal_slider_products,
                parent, false);

        return new PowerOfSuspensionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        OwoProduct item = owoProductList.get(position);

        if (item != null) {

            Glide.with(mctx).load(HostAddress.HOST_ADDRESS.getHostAddress()+item.getProductImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

            holder.txtProductName.setText(item.getProductName());

            String price = "৳ "+ item.getProductPrice();

            holder.txtProductPrice.setText(price);

            holder.txtProductPrice.setPaintFlags(holder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtProductPrice.setVisibility(View.VISIBLE);

            double discounted_price = item.getProductPrice() - item.getProductDiscount();

            String discount = "৳ "+ discounted_price;

            holder.txtProduct_discounted_price.setText(discount);

        } else {
            Toast.makeText(mctx, "No Product Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return owoProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductPrice = itemView.findViewById(R.id.product_price);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            Long id = owoProductList.get(getAbsoluteAdapterPosition()).getProductId();

            RetrofitClient.getInstance().getApi()
                    .getProductById(id)
                    .enqueue(new Callback<OwoProduct>() {
                        @Override
                        public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                            if(response.isSuccessful())
                            {
                                Intent intent = new Intent(mctx, ProductDetailsActivity.class);
                                intent.putExtra("Products", response.body());
                                mctx.startActivity(intent);
                            }
                            else
                            {
                                Log.e("Error", "Server error occurred");
                                Toast.makeText(mctx, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                            Toast.makeText(mctx, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                        }});
        }
    }
}
