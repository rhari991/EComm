package net.rhari.ecomm.data.remote;

import net.rhari.ecomm.data.model.ApiResponse;
import net.rhari.ecomm.data.model.Category;
import net.rhari.ecomm.data.model.Product;
import net.rhari.ecomm.data.model.RankingInfo;
import net.rhari.ecomm.data.model.RankingValue;
import net.rhari.ecomm.data.model.Tax;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            List<RankingInfo> rankingInfo = parseRankingInfo(rootJson);
            List<RankingValue> rankingValues = parseRankingValues(rootJson);
            return new ApiResponse(categories, products, null, rankingInfo, rankingValues);
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

    private List<RankingInfo> parseRankingInfo(JSONObject rootJson) throws JSONException {
        List<RankingInfo> rankingInfo = new ArrayList<>();
        JSONArray rankingsJson = rootJson.getJSONArray("rankings");

        int rankingId = 1;
        for (int i = 0;i < rankingsJson.length();i++) {
            JSONObject rankingJson = rankingsJson.getJSONObject(i);
            String description = rankingJson.getString("ranking");
            Iterator<String> keys = rankingJson.getJSONArray("products").getJSONObject(0).keys();
            String metricName = null;
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.equals("view_count")) {
                    metricName = "views";
                } else if (key.equals("order_count")) {
                    metricName = "orders";
                } else if (key.equals("shares")) {
                    metricName = "shares";
                }
            }
            RankingInfo info = new RankingInfo(rankingId, description, metricName);
            rankingInfo.add(info);
            rankingId++;
        }

        return rankingInfo;
    }

    private List<RankingValue> parseRankingValues(JSONObject rootJson) throws JSONException {
        List<RankingValue> rankingValues = new ArrayList<>();
        JSONArray rankingsJson = rootJson.getJSONArray("rankings");

        int rankingId = 1;
        for (int i = 0;i < rankingsJson.length();i++) {
            JSONObject rankingJson = rankingsJson.getJSONObject(i);
            JSONArray productsJson = rankingJson.getJSONArray("products");

            for (int j = 0;j < productsJson.length();j++) {
                JSONObject productJson = productsJson.getJSONObject(j);
                int productId = 0;
                int value = 0;
                Iterator<String> keys = productJson.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals("id")) {
                        productId = productJson.getInt(key);
                    } else {
                        value = productJson.getInt(key);
                    }
                }
                RankingValue rankingValue = new RankingValue(rankingId, productId, value);
                rankingValues.add(rankingValue);
            }
            rankingId++;
        }

        return rankingValues;
    }
}
