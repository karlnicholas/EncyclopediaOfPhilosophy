package encyclopediaofphilosophy;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import encyclopediaofphilosophy.EncyclophiaOfPhilosophySpeechlet;

public class PingSEP implements Runnable {
	String url;
	public PingSEP(String url) {
		this.url = url;
	}

	public void run() {
		try ( CloseableHttpClient httpclient = HttpClients.createDefault() ) {
			HttpGet httpGet = new HttpGet("https://plato.stanford.edu/archives/fall2016/"+url);
			CloseableHttpResponse response = httpclient.execute(httpGet);
		    HttpEntity entity = response.getEntity();
		    EntityUtils.consume(entity);
		    response.close();
		    httpclient.close();
		} catch (IOException e) {
		}		
	}
}
