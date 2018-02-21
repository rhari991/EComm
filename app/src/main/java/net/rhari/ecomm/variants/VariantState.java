package net.rhari.ecomm.variants;

import net.rhari.ecomm.data.model.Variant;
import net.rhari.ecomm.util.ListState;

import java.util.List;

class VariantState implements VariantsContract.State {

    private final int productId;
    private final List<Variant> variants;
    private final ListState variantsListState;

    VariantState(int productId, List<Variant> variants,
                 ListState variantsListState) {
        this.productId = productId;
        this.variants = variants;
        this.variantsListState = variantsListState;
    }

    @Override
    public int getProductId() {
        return productId;
    }

    @Override
    public List<Variant> getVariants() {
        return variants;
    }

    @Override
    public ListState getVariantsListState() {
        return variantsListState;
    }
}