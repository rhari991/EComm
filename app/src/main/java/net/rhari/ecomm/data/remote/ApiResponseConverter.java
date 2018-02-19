package net.rhari.ecomm.data.remote;

import net.rhari.ecomm.data.model.ApiResponse;
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.data.model.Tax;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import timber.log.Timber;

// This class is responsible for converting the server response into model objects that can be
// easily inserted into the SQLite database.
class ApiResponseConverter implements Converter<ResponseBody, ApiResponse> {

    private static final ApiResponseConverter INSTANCE = new ApiResponseConverter();

    static ApiResponseConverter getInstance() {
        return INSTANCE;
    }

    private ApiResponseConverter() {

    }

    @Override
    public ApiResponse convert(ResponseBody value) throws IOException {
        try {
            JSONObject rootJson = new JSONObject(value.string());
            List<Category> categories = parseCategories(rootJson);
            List<Product> products = parseProducts(rootJson);
            return new ApiResponse(categories, products, null, null, null);
        } catch (JSONException je) {
            Timber.e("Could not parse server response", je);
            return null;
        }
    }

    private List<Category> parseCategories(JSONObject rootJson) throws JSONException {
        List<Category> categories = new ArrayList<>();
        JSONArray categoriesJson = rootJson.getJSONArray("categories");

        // Add all categories
        for (int i = 0;i < categoriesJson.length();i++) {
            JSONObject categoryJson = categoriesJson.getJSONObject(i);
            Category category = new Category(categoryJson.getInt("id"),
                    categoryJson.getString("name"));
            categories.add(category);
        }

        // Assign parent ids to all categories
        for (int i = 0;i < categoriesJson.length();i++) {
            JSONObject categoryJson = categoriesJson.getJSONObject(i);
            int parentCategoryId = categoryJson.getInt("id");
            JSONArray childCategoriesJson = categoryJson.getJSONArray("child_categories");

            for (int j = 0;j < childCategoriesJson.length();j++) {
                int childCategoryId = childCategoriesJson.getInt(j);

                for (Category category : categories) {
                    if (category.getId() == childCategoryId) {
                        category.setParentId(parentCategoryId);
                        break;
                    }
                }
            }
        }

        return categories;
    }

    private List<Product> parseProducts(JSONObject rootJson) throws JSONException {
        List<Product> products = new ArrayList<>();
        JSONArray categoriesJson = rootJson.getJSONArray("categories");

        for (int i = 0;i < categoriesJson.length();i++) {
            JSONObject categoryJson = categoriesJson.getJSONObject(i);
            int categoryId = categoryJson.getInt("id");
            JSONArray productsJson = categoryJson.getJSONArray("products");

            for (int j = 0;j < productsJson.length();j++) {
                JSONObject productJson = productsJson.getJSONObject(j);

                int id = productJson.getInt("id");
                String name = productJson.getString("name");
                JSONObject taxJson = productJson.getJSONObject("tax");
                String taxName = taxJson.getString("name");
                double taxValue = taxJson.getDouble("value");
                Tax tax = new Tax(taxName, taxValue);

                Product product = new Product(id, name, categoryId, tax);
                products.add(product);
            }
        }

        return products;
    }
}
