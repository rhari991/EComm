package net.rhari.ecomm.categories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.rhari.ecomm.R;
import net.rhari.ecomm.data.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnListItemClickListener clickListener;

    CategoriesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    void setOnClickListener(OnListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void swapData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);
        return new CategoryViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_category_name)
        TextView nameView;

        CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void bind(Category category) {
            nameView.setText(category.getName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (clickListener != null) {
                clickListener.onListItemClick(categories.get(position), position);
            }
        }
    }

    public interface OnListItemClickListener {

        void onListItemClick(Category category, int position);
    }
}
