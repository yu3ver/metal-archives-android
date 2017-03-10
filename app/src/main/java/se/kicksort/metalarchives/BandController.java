package se.kicksort.metalarchives;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gustav Sundin on 10/03/17.
 */

public class BandController {
    private final String baseUrl = "http://em.wemakesites.net/";
    private Retrofit retrofit;
    private MetalArchivesService metalArchivesService;

    public BandController() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        metalArchivesService = retrofit.create(MetalArchivesService.class);
    }

    public Observable<Response> getBand(String band) {
        return metalArchivesService.getBand(band);
    }
}
