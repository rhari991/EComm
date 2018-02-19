package net.rhari.ecomm.products;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.SortedProduct;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<SortedProduct> products;
    private String metricName;
    private OnListItemClickListener clickListener;

    ProductsAdapter(List<SortedProduct> products) {
        this.products = products;
    }

    void setOnClickListener(OnListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void swapData(List<SortedProduct> products, String metricName) {
        this.products.clear();
        this.products.addAll(products);
        this.metricName = metricName;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_product_list, parent, false);
        return new ProductViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        SortedProduct product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_product_name)
        TextView nameView;

        @BindView(R.id.text_product_metric)
        TextView metricView;

        ProductViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bind(SortedProduct product) {
            nameView.setText(product.getProduct().getName());
            metricView.setText(product.getRankingValue().getValue() + " " + metricName);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (clickListener != null) {
                clickListener.onListItemClick(products.get(position), position);
            }
        }
    }

    public interface OnListItemClickListener {

        void onListItemClick(SortedProduct product, int position);
    }
}