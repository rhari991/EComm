package net.rhari.ecomm.categories;

import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.util.ListState;

import java.util.List;

class CategoriesState implements CategoriesContract.State {

    private final int parentCategoryId;
    private final List<Category> categories;
    private final ListState categoriesListState;

    CategoriesState(int parentCategoryId, List<Category> categories,
                    ListState categoriesListState) {
        this.parentCategoryId = parentCategoryId;
        this.categories = categories;
        this.categoriesListState = categoriesListState;
    }

    @Override
    public int getParentCategoryId() {
        return parentCategoryId;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public ListState getCategoriesListState() {
        return categoriesListState;
    }
}
