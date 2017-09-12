package com.mai.nix.maiapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Nix on 12.09.2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListFactory(getApplicationContext(), intent);
    }
}
