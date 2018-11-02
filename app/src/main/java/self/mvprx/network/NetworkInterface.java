package self.mvprx.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import self.mvprx.pojo.MovieResponse;

public interface NetworkInterface {

    @GET("4/list/1?")
    Observable<MovieResponse> getMovies(@Query("page") String page, @Query("api_key") String api_key);

    @GET("/3/search/movie")
    Observable<MovieResponse> getMoviesBasedOnQuery(@Query("api_key") String api_key, @Query("query") String q);
}
