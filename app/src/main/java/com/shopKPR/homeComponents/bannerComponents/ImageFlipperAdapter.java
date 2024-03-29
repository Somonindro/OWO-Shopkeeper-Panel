package com.shopKPR.homeComponents.bannerComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.homeComponents.floatingComponents.deals.AllDealsActivity;
import com.shopKPR.homeComponents.floatingComponents.gifts.AllGifts;
import com.shopKPR.homeComponents.floatingComponents.offers.OffersActivity;
import com.shopKPR.homeComponents.homeProductsPaging.ShowSubCategoryProducts;
import com.shopKPR.homeComponents.tabbedComponents.ViewPagerAdapter;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageFlipperAdapter extends RecyclerView.Adapter<ImageFlipperAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<String> images = new ArrayList<>();
    private final FragmentManager fragmentManager;
    private final Lifecycle lifecycle;

    private final CapsuleAdapter capsuleAdapter;
    private final List<OwoProduct> owoCapsuleLists = new ArrayList<>();

    private final PediatricDropAdapter pediatricDropAdapter;
    private final List<OwoProduct> pediatricDropLists = new ArrayList<>();

    private final PowerOfSuspensionAdapter powerOfSuspensionAdapter;
    private final List<OwoProduct> powerOfSuspensionList = new ArrayList<>();

    private final TabletAdapter tabletAdapter;
    private final List<OwoProduct> tabletList = new ArrayList<>();

    private final HerbalAdapter herbalAdapter;
    private final List<OwoProduct> herbalsList = new ArrayList<>();

    public ImageFlipperAdapter(Context mCtx, List<String> images, FragmentManager fragmentManager,
                               Lifecycle lifecycle)
    {
        this.mCtx = mCtx;
        this.images.addAll(images);
        this.fragmentManager = fragmentManager;
        this.lifecycle = lifecycle;

        capsuleAdapter = new CapsuleAdapter(mCtx, owoCapsuleLists);
        pediatricDropAdapter = new PediatricDropAdapter(mCtx, pediatricDropLists);
        powerOfSuspensionAdapter = new PowerOfSuspensionAdapter(mCtx, powerOfSuspensionList);
        tabletAdapter = new TabletAdapter(mCtx, tabletList);
        herbalAdapter = new HerbalAdapter(mCtx, herbalsList);
    }

    @NonNull
    @Override
    public ImageFlipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.banner_slider, parent, false);
        return new ImageFlipperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFlipperAdapter.ViewHolder holder, int position) {
        fliptheView(holder.bannerFlipper);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewFlipper bannerFlipper;
        public TabLayout tabLayout;
        public ViewPager2 viewPager2;
        public ViewPagerAdapter viewPagerAdapter;

        public RecyclerView capsulesRecyclerView, pediatricDropRecyclerView, powerOfSuspensionRecyclerView,
                tabletRecyclerView, herbalsRecyclerView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            bannerFlipper = itemView.findViewById(R.id.view_flipper_offer);
            tabLayout = itemView.findViewById(R.id.tabbed_layout);
            viewPager2 = itemView.findViewById(R.id.category_and_brand_showing);

            FloatingActionButton giftCard = itemView.findViewById(R.id.gift_card);
            FloatingActionButton deals = itemView.findViewById(R.id.happy_deals);
            FloatingActionButton foreign = itemView.findViewById(R.id.foreign_medicine);
            FloatingActionButton offers = itemView.findViewById(R.id.offer);
            FloatingActionButton forYou = itemView.findViewById(R.id.for_you);


            viewPagerAdapter = new ViewPagerAdapter(fragmentManager, lifecycle);
            viewPager2.setAdapter(viewPagerAdapter);

            TextView capsuleShowMore = itemView.findViewById(R.id.capsule_show_more);
            TextView pediatricShowMore = itemView.findViewById(R.id.pediatric_show_more);
            TextView powerOfSuspensionShowMore = itemView.findViewById(R.id.power_of_suspension_show_more);
            TextView tabletShowMore = itemView.findViewById(R.id.tablet_show_more);
            TextView herbalShowMore = itemView.findViewById(R.id.herbal_show_more);

            capsulesRecyclerView = itemView.findViewById(R.id.capsule_recycler_view);
            capsulesRecyclerView.setAdapter(capsuleAdapter);
            capsulesRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            getCapsules("Capsules");

            pediatricDropRecyclerView = itemView.findViewById(R.id.pediatric_recycler_view);
            pediatricDropRecyclerView.setAdapter(pediatricDropAdapter);
            pediatricDropRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            getCapsules("Pediatric Drops");

            powerOfSuspensionRecyclerView = itemView.findViewById(R.id.power_of_suspension_recycler_view);
            powerOfSuspensionRecyclerView.setAdapter(powerOfSuspensionAdapter);
            powerOfSuspensionRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            getCapsules("Power of suspensions");

            tabletRecyclerView = itemView.findViewById(R.id.tablet_recycler_view);
            tabletRecyclerView.setAdapter(tabletAdapter);
            tabletRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            getCapsules("Tablets");

            herbalsRecyclerView = itemView.findViewById(R.id.herbal_recycler_view);
            herbalsRecyclerView.setAdapter(herbalAdapter);
            herbalsRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            getCapsules("Herbals");

            capsuleShowMore.setOnClickListener(v -> showMoreProducts("Capsules"));
            pediatricShowMore.setOnClickListener(v -> showMoreProducts("Pediatric Drops"));
            powerOfSuspensionShowMore.setOnClickListener(v -> showMoreProducts("Power of suspensions"));
            tabletShowMore.setOnClickListener(v -> showMoreProducts("Tablets"));
            herbalShowMore.setOnClickListener(v -> showMoreProducts("Herbals"));


            giftCard.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, AllGifts.class);
                mCtx.startActivity(intent);
            });

            deals.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, AllDealsActivity.class);
                mCtx.startActivity(intent);
            });

            foreign.setOnClickListener(v -> showMoreProducts("Foreign Medicines"));

            offers.setOnClickListener(v -> {
                Intent intent = new Intent(mCtx, OffersActivity.class);
                mCtx.startActivity(intent);
            });

            forYou.setOnClickListener(v -> {
                //This needs to implemented later.
                Toast.makeText(mCtx, "Coming soon...", Toast.LENGTH_SHORT).show();
            });

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
            {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            });

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            bannerFlipper.getLayoutParams().width = displaymetrics.widthPixels;
        }
    }

    private void showMoreProducts(String subCategoryName)
    {
        Intent intent = new Intent(mCtx, ShowSubCategoryProducts.class);
        intent.putExtra("subCategoryName", subCategoryName);
        mCtx.startActivity(intent);
    }

    private void fliptheView(ViewFlipper banner) {

        int size = images.size();

        for(int i=0; i<size; i++)
        {
            flipperImage(images.get(i), banner);
        }

    }

    private void getCapsules(String subcategoryName)
    {
        RetrofitClient.getInstance().getApi()
                .getProductBySubcategory(Prevalent.category_to_display, subcategoryName)
                .enqueue(new Callback<List<OwoProduct>>()
                {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            switch (subcategoryName)
                            {
                                case "Capsules" :
                                {
                                    owoCapsuleLists.clear();
                                    owoCapsuleLists.addAll(response.body());
                                    capsuleAdapter.notifyDataSetChanged();
                                    break;
                                }
                                case "Pediatric Drops":
                                {
                                    pediatricDropLists.clear();
                                    pediatricDropLists.addAll(response.body());
                                    pediatricDropAdapter.notifyDataSetChanged();
                                    break;
                                }
                                case "Power of suspensions":
                                {
                                    powerOfSuspensionList.clear();
                                    powerOfSuspensionList.addAll(response.body());
                                    powerOfSuspensionAdapter.notifyDataSetChanged();
                                    break;
                                }
                                case "Tablets" :
                                {
                                    tabletList.clear();
                                    tabletList.addAll(response.body());
                                    tabletAdapter.notifyDataSetChanged();
                                    break;
                                }
                                case "Herbals" :
                                {
                                    herbalsList.clear();
                                    herbalsList.addAll(response.body());
                                    herbalAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }

                        }
                        else
                        {
                            Toast.makeText(mCtx, "Can not get capsules", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Capsule", t.getMessage());
                        Toast.makeText(mCtx, "Can not get capsules", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void flipperImage(String image, ViewFlipper viewFlipper)
    {
        ImageView imageView = new ImageView(mCtx);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+image)
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(imageView);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(6000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(mCtx, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(mCtx, R.anim.slide_out_left);
    }

    public void updateItems(List<String> newList) {
        images.clear();
        images.addAll(newList);
        notifyDataSetChanged();
    }
}
