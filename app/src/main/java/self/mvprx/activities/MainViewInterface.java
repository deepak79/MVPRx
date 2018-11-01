package self.mvprx.activities;

import self.mvprx.pojo.MovieResponse;

public interface MainViewInterface {
    void showToast(String message);
    void displayMovies(MovieResponse movieResponse);
    void displayError(String error);
    void hideProgress();
    void showProgress();
}
