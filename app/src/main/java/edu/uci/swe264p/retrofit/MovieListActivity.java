package edu.uci.swe264p.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity {
    private static final String TAG = "MovieListActivity";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    // 您提供的 API 密钥
    private static final String API_KEY = "bc48f48ef11f4200f0df08bd3794f508";
    // 您提供的 令牌 (Read Access Token)
    private static final String AUTH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiYzQ4ZjQ4ZWYxMWY0MjAwZjBkZjA4YmQzNzk0ZjUwOCIsIm5iZiI6MTc3MjA1NTI5MC41MjksInN1YiI6IjY5OWY2YWZhOTE2NzI3MTYyNGVmZWMxZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.d5rTCfIoxpMwnYwy_beFFKxv7dXHlyFe7WYtO03l5kI";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        recyclerView = findViewById(R.id.rvMovieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fetchTopRatedMovies();
    }

    private void fetchTopRatedMovies() {
        // 创建一个拦截器，自动在所有请求头中加入 Authorization: Bearer <Token>
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + AUTH_TOKEN)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApiService service = retrofit.create(MovieApiService.class);
        
        // 虽然使用了令牌，我们同时也传入 API_KEY 以确保双重保险
        service.getTopRatedMovies(API_KEY).enqueue(new Callback<TopRatedResponse>() {
            @Override
            public void onResponse(Call<TopRatedResponse> call, Response<TopRatedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    if (movies != null && !movies.isEmpty()) {
                        Log.d(TAG, "Successfully fetched " + movies.size() + " movies");
                        recyclerView.setAdapter(new MovieListAdapter(movies));
                    } else {
                        Log.w(TAG, "No movies found in response");
                    }
                } else {
                    Log.e(TAG, "Response Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TopRatedResponse> call, Throwable t) {
                Log.e(TAG, "Network Failure: " + t.getMessage());
            }
        });
    }
}
