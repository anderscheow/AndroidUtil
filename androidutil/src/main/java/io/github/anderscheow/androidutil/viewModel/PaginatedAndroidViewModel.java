package io.github.anderscheow.androidutil.viewModel;

import android.app.Application;
import androidx.databinding.ObservableField;

@Deprecated
public abstract class PaginatedAndroidViewModel<Args> extends BaseAndroidViewModel<Args> {

    public final ObservableField<Long> totalOfElements = new ObservableField<>();

    private int pageNumber = 0;
    private boolean hasNextPage = false;

    public PaginatedAndroidViewModel(Application context) {
        super(context);
    }

    @Override
    public void onRefresh() {
        setPageNumber(0);
    }

    protected void setTotalOfElements(long totalOfElements) {
        this.totalOfElements.set(totalOfElements);
        this.totalOfElements.notifyChange();
    }

    protected int getPageNumber() {
        return pageNumber;
    }

    protected void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    protected void incrementPageNumber() {
        this.pageNumber = pageNumber + 1;
    }

    protected boolean hasNextPage() {
        return hasNextPage;
    }

    protected void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
