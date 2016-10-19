package com.dev.stdev.njuskalonovosti.services;

import android.app.IntentService;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.database.DatabaseClass;
import com.dev.stdev.njuskalonovosti.database.TableSearch;
import com.dev.stdev.njuskalonovosti.models.SearchClass;

import java.util.List;


public class GetAllSearchService extends IntentService {

	TableSearch tableSearch = new TableSearch(DatabaseClass.getDatabase());

	public GetAllSearchService() {
		super("GetAllSearchService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {

			//Log.d("U servisu getFlatsAdvertisments","U servisu getFlatsAdvertisments");

			List<SearchClass> searchList = tableSearch.getAllSearch();

			for (int i = 0; i < searchList.size(); i++) {
				SearchClass pr = searchList.get(i);
				sendBroadcastMessage("PRETRAGE_RESP", pr);

			}


		}
	}


	private void sendBroadcastMessage(String intentFilterName, SearchClass f) {

		//Log.d("Šaljem Intent","Šaljem Intent");

		Intent intent = new Intent(intentFilterName);
		intent.putExtra("PRETRAGA_OBJ", f);
		sendBroadcast(intent);
	}


}
