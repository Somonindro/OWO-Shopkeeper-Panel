package com.shopKPR.homeComponents.homeProductsPaging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.shopKPR.Model.OwoProduct;

public class SubCategoryProductsViewModel extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public SubCategoryProductsViewModel(Long subCategoryId) {

        SubCategoryProductsDataSourceFactory subCategoryProductsDataSourceFactory =
                new SubCategoryProductsDataSourceFactory(subCategoryId);

        liveDataSource = subCategoryProductsDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(subCategoryProductsDataSourceFactory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
