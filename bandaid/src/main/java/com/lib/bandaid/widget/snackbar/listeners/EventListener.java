package com.lib.bandaid.widget.snackbar.listeners;


import com.lib.bandaid.widget.snackbar.Snackbar;

/**
 * Interface used to notify of all {@link Snackbar} display events. Useful if you want
 * to move other views while the Snackbar is on screen.
 */
public interface EventListener {
    /**
     * Called when a {@link Snackbar} is about to enter the screen
     *
     * @param snackbar the {@link Snackbar} that's being shown
     */
    public void onShow(Snackbar snackbar);

    /**
     * Called when a {@link Snackbar} is about to enter the screen while
     * a {@link Snackbar} is about to exit the screen by replacement.
     *
     * @param snackbar the {@link Snackbar} that's being shown
     */
    public void onShowByReplace(Snackbar snackbar);

    /**
     * Called when a {@link Snackbar} is fully shown
     *
     * @param snackbar the {@link Snackbar} that's being shown
     */
    public void onShown(Snackbar snackbar);

    /**
     * Called when a {@link Snackbar} is about to exit the screen
     *
     * @param snackbar the {@link Snackbar} that's being dismissed
     */
    public void onDismiss(Snackbar snackbar);

    /**
     * Called when a {@link Snackbar} is about to exit the screen
     * when a new {@link Snackbar} is about to enter the screen.
     *
     * @param snackbar the {@link Snackbar} that's being dismissed
     */
    public void onDismissByReplace(Snackbar snackbar);

    /**
     * Called when a {@link Snackbar} had just been dismissed
     *
     * @param snackbar the {@link Snackbar} that's being dismissed
     */
    public void onDismissed(Snackbar snackbar);
}
