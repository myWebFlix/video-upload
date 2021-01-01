package com.webflix.videoupload.api.v1.resources;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.webflix.videoupload.models.entities.VideoRawDataEntity;
import com.webflix.videoupload.services.beans.VideoRawDataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;


@ApplicationScoped
@Path("/upload")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS")
public class UploadResource {

	@Inject
	private VideoRawDataBean videoRawDataBean;

	@GET
	public Response getVideos() {
		List<VideoRawDataEntity> list = videoRawDataBean.getVideoRawData();
		return Response.ok(list).build();
	}

	@GET
	@Path("/{videoId}")
	public Response getUpload(@PathParam("videoId") Integer videoId) {

		List<VideoRawDataEntity> vrde = videoRawDataBean.getVideoRawData(videoId);

		return Response.status(Response.Status.OK).entity(vrde).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response uploadVideo(
			@HeaderParam("Video-Title") String title,
			@HeaderParam("Video-Description") String description,
			@HeaderParam("Video-Name") String name,
			InputStream uploadedInputStream) {

		System.out.println("Called");
		System.out.println(title);
		System.out.println(description);

		String uniqueId = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1000000));

		byte[] bytes = new byte[0];
		try (uploadedInputStream) {
			bytes = uploadedInputStream.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(bytes.length);

		String projectId = "webflix-295923";
		String bucketName = "webflix-videos";
		String objectName = uniqueId + "-" + name;

		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, bytes);

		System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);

		//Client httpClient = ClientBuilder.newClient();

		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost httpPost = new HttpPost("http://rok.zoxxnet.com/webflix/v1/videos");
			StringEntity entity = new StringEntity("{\"title\":\"" + title + "\",\"description\":\"" + description + "\"}");
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);

			String json = EntityUtils.toString(response.getEntity());
			JSONObject obj = new JSONObject(json);

			Integer video_id = obj.getInt("id");
			String url = "https://storage.googleapis.com/" + bucketName + "/" + objectName;

			VideoRawDataEntity vrde = new VideoRawDataEntity();
			vrde.setVideo_id(video_id);
			vrde.setUrl(url);
			vrde.setQuality("Source");
			videoRawDataBean.createVideoRawData(vrde);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
//
//		try {
//			return httpClient
//					.target("34.107.92.162/webflix/v1/videos")
//					.queryParam("videoId", videoId)
//					.request().get(new GenericType<Integer>() {
//					});
//		} catch (WebApplicationException | ProcessingException e) {
//			log.severe(e.getMessage());
//			throw new InternalServerErrorException(e);
//		}


		return Response.status(Response.Status.OK).build();
	}

}
