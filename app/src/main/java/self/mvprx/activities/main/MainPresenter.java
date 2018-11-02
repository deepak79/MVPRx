package self.mvprx.activities.main;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import self.mvprx.Constants;
import self.mvprx.network.NetworkClient;
import self.mvprx.network.NetworkInterface;
import self.mvprx.pojo.MovieResponse;

import static android.support.constraint.Constraints.TAG;

public class MainPresenter implements MainPresenterInterface {

    MainViewInterface mainViewInterface;

    public MainPresenter(MainViewInterface mainViewInterface) {
        this.mainViewInterface = mainViewInterface;
    }

    @Override
    public void getMovies() {
        getObservable().subscribeWith(getObserver());
    }

    private Observable<MovieResponse> getObservable() {
        return NetworkClient.getRetrofit(4)
                .create(NetworkInterface.class)
                .getMovies("1",Constants.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<MovieResponse> getObserver() {
        return new DisposableObserver<MovieResponse>() {
            @Override
            public void onNext(MovieResponse movieResponse) {
                Log.e(TAG, "" + movieResponse);
                mainViewInterface.displayMovies(movieResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "" + e.getMessage());
                mainViewInterface.displayError(e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
                mainViewInterface.hideProgress();
            }
        };
    }
}
