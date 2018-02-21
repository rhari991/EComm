package net.rhari.ecomm.variants;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.model.Variant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.VariantViewHolder> {

    private List<Variant> variants;

    VariantsAdapter(List<Variant> variants) {
        this.variants = variants;
    }

    void swapData(List<Variant> variants) {
        this.variants.clear();
        this.variants.addAll(variants);
        notifyDataSetChanged();
    }

    @Override
    public VariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_variant_list, parent, false);
        return new VariantViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VariantViewHolder holder, int position) {
        Variant variant = variants.get(position);
        holder.bind(variant);
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    class VariantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_size)
        TextView sizeView;

        @BindView(R.id.text_color)
        TextView colorView;

        @BindView(R.id.text_price)
        TextView priceView;

        VariantViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(Variant variant) {
            if (variant.getSize() != null) {
                sizeView.setVisibility(View.VISIBLE);
                sizeView.setText(String.valueOf(variant.getSize()));
            } else {
                sizeView.setVisibility(View.GONE);
            }
            colorView.setText(variant.getColor());
            priceView.setText("RS " + variant.getPrice());
        }
    }
}