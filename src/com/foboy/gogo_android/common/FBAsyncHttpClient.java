package com.foboy.gogo_android.common;

import org.apache.http.client.params.ClientPNames;

import com.loopj.android.http.AsyncHttpClient;

public class FBAsyncHttpClient extends AsyncHttpClient {
	public FBAsyncHttpClient()
	{
		super();
		this.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		this.setTimeout(30);
	}
}
