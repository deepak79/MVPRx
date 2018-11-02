package self.mvprx.activities.search;

import android.support.v7.widget.SearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import self.mvprx.Constants;
import self.mvprx.network.NetworkClient;
import self.mvprx.network.NetworkInterface;
import self.mvprx.pojo.MovieResponse;

public class SearchPresenter implements SearchPresenterInterface {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    SearchViewInterface searchViewInterface;

    public SearchPresenter(SearchViewInterface searchViewInterface) {
        this.searchViewInterface = searchViewInterface;
    }

    @Override
    public void getResultOnQuery(SearchView searchView) {
        getObservableQuery(searchView)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        if (s.equals("")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .switchMap(new Function<String, ObservableSource<MovieResponse>>() {
                    @Override
                    public Observable<MovieResponse> apply(@NonNull String s) throws Exception {
                        return NetworkClient.getRetrofit(3).create(NetworkInterface.class)
                                .getMoviesBasedOnQuery(Constants.API_KEY, s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observable<String> getObservableQuery(SearchView searchView) {
        final PublishSubject<String> publishSubject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                publishSubject.onNext(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                publishSubject.onNext(s);
                return false;
            }
        });
        return publishSubject;
    }

    public DisposableObserver<MovieResponse> getObserver() {
        return new DisposableObserver<MovieResponse>() {
            @Override
            public void onNext(MovieResponse movieResponse) {
                searchViewInterface.displayResult(movieResponse);
            }

            @Override
            public void onError(Throwable e) {
                searchViewInterface.displayError(e.getMessage());
            }

            @Override
            public void onComplete() {
                searchViewInterface.hideProgress();
            }
        };
    }
}
