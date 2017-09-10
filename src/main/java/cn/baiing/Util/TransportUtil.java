package cn.baiing.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class TransportUtil {
	
	public static TransportClient buildClient(){
		try {
			Settings settings = Settings.builder().put("client.transport.sniff", true).build();  
			TransportClient client = new PreBuiltTransportClient(settings)  
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));  
			return client;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 定制BulkProcessor
	 * @return
	 */
    public static BulkProcessor bulkProcess(TransportClient client) {
        BulkProcessor bulkProcessor = BulkProcessor.builder(client,
                new BulkProcessor.Listener() {
                  public void beforeBulk(long executionId,
                                           BulkRequest request) {
 
                    }
 
                    public void afterBulk(long executionId, BulkRequest request,  BulkResponse response) {
 
                    }
                    //设置ConcurrentRequest 为0，Throwable不抛错
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        System.out.println("happen fail = " + failure.getMessage() + " cause = " +failure.getCause());
                    }
                })
                .setBulkActions(3000)
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(20))
                .setConcurrentRequests(1)
                .build();
        return bulkProcessor;
    }

}
