package edu.uci.swe264p.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity_Debug";
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    static Retrofit retrofit = null;
    final static String API_KEY = "bc48f48ef11f4200f0df08bd3794f508";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting MainActivity");

        try {
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            Log.e(TAG, "CRITICAL: Layout inflation failed!", e);
            return;
        }

        try {
            initButtons();
            // 稍微延迟一点发起连接，或者在 UI 准备好后再连
            findViewById(android.R.id.content).post(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "CRITICAL: Initialization failed!", e);
            Toast.makeText(this, "Init Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initButtons() {
        Button btnProgramList = findViewById(R.id.btnProgramList);
        if (btnProgramList != null) {
            btnProgramList.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ProgramListActivity.class));
            });
        }

        Button btnMovieList = findViewById(R.id.btnMovieList);
        if (btnMovieList != null) {
            btnMovieList.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, MovieListActivity.class));
            });
        }
    }

    private void connect() {
        Log.d(TAG, "connect: Initiating API call");
        try {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

            MovieApiService movieApiService = retrofit.create(MovieApiService.class);
            Call<Movie> call = movieApiService.getMovie(603, API_KEY);

            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateUI(response.body());
                    } else {
                        Log.e(TAG, "API Error: Code " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(TAG, "Network failure: " + t.getMessage());
                }
            });
        } catch (Throwable t) {
            Log.e(TAG, "connect: Unexpected error", t);
        }
    }

    private void updateUI(Movie movie) {
        setTextSafe(R.id.txtTitle, movie.getTitle());
        setTextSafe(R.id.txtReleaseDate, movie.getReleaseDate());
        setTextSafe(R.id.txtVote, movie.getVoteAverage() != null ? movie.getVoteAverage().toString() : "0.0");
        setTextSafe(R.id.txtOverview, movie.getOverview());

        android.widget.ImageView imgPoster = findViewById(R.id.imgPoster);
        if (imgPoster != null && movie.getPosterPath() != null) {
            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            com.squareup.picasso.Picasso.get()
                    .load(posterUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imgPoster);
        }
    }

    private void setTextSafe(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setText(text != null ? text : "");
        }
    }
}
