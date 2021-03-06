package se.kicksort.metalarchives;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import se.kicksort.metalarchives.model.BandSearchResult;
import se.kicksort.metalarchives.search.SearchResultsView;

/**
 * Created by Gustav Sundin on 01/03/17.
 */

public class BaseActivity extends AppCompatActivity {
    public NavigationManager navigationManager;
    private SearchResultsView searchResults;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int lastScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        navigationManager = new NavigationManager(getSupportFragmentManager(), this);
        Disposable scrollEvents = navigationManager.getScrollEvents().subscribe(this::handleScroll);
        compositeDisposable.add(scrollEvents);
        NavigationManager.getInstance().openStartView();

        searchResults = (SearchResultsView) findViewById(R.id.search_results);
        Disposable searchResultClicks = searchResults.getSearchResultClicks().subscribe(this::openBandResult);
        compositeDisposable.add(searchResultClicks);
        NavigationManager.getInstance().openBand("Armory", "3540372230");
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void handleScroll(int scrollY) {
       /* int deltaY = scrollY - lastScrollY;

        if (scrollY < 10 && deltaY < 0) {
            showSearchBar();
        } else if (scrollY > 20) {
            hideSearchBar();
        }

        lastScrollY = scrollY;*/
    }

    public void showSearchBar() {
        getSupportActionBar().show();
    }

    public void hideSearchBar() {
        getSupportActionBar().hide();
    }

    private void openBandResult(BandSearchResult bandSearchResult) {
        hideKeyboard();
        Log.d("Band", bandSearchResult.getId());
        NavigationManager.getInstance().openBand(bandSearchResult.getBandName(), bandSearchResult.getId());
    }

    @Override
    public void onBackPressed() {
        // Note: we are intentionally not calling the super implementation since we have built or own navigation manager
        navigationManager.navigateBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);

        searchView.setOnQueryTextListener(searchResults.getQueryListener());

        return true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
