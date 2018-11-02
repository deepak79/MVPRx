package self.mvprx.activities.search;

import self.mvprx.pojo.MovieResponse;

public interface SearchViewInterface {
    void showToast(String message);
    void displayResult(MovieResponse movieResponse);
    void displayError(String error);
    void showProgress();
    void hideProgress();
}
