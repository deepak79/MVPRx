package self.mvprx.activities.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.mvprx.R;
import self.mvprx.adapters.MoviesAdapter;
import self.mvprx.pojo.MovieResponse;

public class SearchActivity extends AppCompatActivity implements SearchViewInterface {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvQueryResult)
    RecyclerView rvQueryResult;
    SearchPresenter searchPresenter;
    RecyclerView.Adapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchPresenter = new SearchPresenter(this);
        rvQueryResult.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Movie name..");
        searchPresenter.getResultOnQuery(searchView);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayResult(MovieResponse movieResponse) {
        adapter = new MoviesAdapter(movieResponse.getResults(), SearchActivity.this);
        rvQueryResult.setAdapter(adapter);
    }

    @Override
    public void displayError(String error) {
        showToast(error);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
