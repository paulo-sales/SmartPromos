package br.com.smartpromos.services.scroll;

/**
 * Created by Paulo on 13/05/2016.
 */
public interface EndlessScrollListener {
    void onScrollChanged(EndlessScrollView scrollView, int x, int y, int oldx, int oldy);
}
