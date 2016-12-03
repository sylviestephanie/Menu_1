package id.ac.umn.mobile.menu_1.service;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import id.ac.umn.mobile.menu_1.ListProvider;

/**
 * Created by abdelhaq on 03-Dec-16.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.e("bind me","please");

        return (new ListProvider(this.getApplicationContext(), intent));
    }
}
