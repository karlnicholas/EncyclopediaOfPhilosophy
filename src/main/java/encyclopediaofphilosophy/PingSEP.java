package encyclopediaofphilosophy;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import encyclopediaofphilosophy.EncyclophiaOfPhilosophySpeechlet;

public class PingSEP implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(PingSEP.class);
	
	public void run() {
//		EncyclophiaOfPhilosophySpeechlet speechlet = new EncyclophiaOfPhilosophySpeechlet();
//		System.out.println(  speechlet.getEntryFromStanford() );
		try ( CloseableHttpClient httpclient = HttpClients.createDefault() ) {
			HttpGet httpGet = new HttpGet(EncyclophiaOfPhilosophySpeechlet.URL_PREFIX);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			log.info( response.getStatusLine().getReasonPhrase() );
		    HttpEntity entity = response.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
//		    entity.writeTo(System.out);
		    EntityUtils.consume(entity);
		    response.close();
		    httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.warn( e.getMessage() );
		}		
	}

}
