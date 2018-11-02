package self.mvprx.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.mvprx.R;
import self.mvprx.activities.search.SearchActivity;
import self.mvprx.adapters.MoviesAdapter;
import self.mvprx.pojo.MovieResponse;

public class MainActivity extends AppCompatActivity implements MainViewInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    RecyclerView.Adapter adapter;
    MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        setMVP();
        getMovieList();
    }

    private void setMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void getMovieList() {
        mainPresenter.getMovies();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayMovies(MovieResponse movieResponse) {
        if (movieResponse != null) {
            Log.e(TAG, "" + movieResponse.toString());
            adapter = new MoviesAdapter(movieResponse.getResults(), MainActivity.this);
            rvMovies.setAdapter(adapter);
        }
    }

    @Override
    public void displayError(String error) {
        showToast(error);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            showToast("Search Clicked");
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
