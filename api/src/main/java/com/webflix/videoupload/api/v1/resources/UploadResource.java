package com.webflix.videoupload.api.v1.resources;

import com.google.auth.oauth2.GoogleCredentials;
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
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;


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
	public Response getUpload(@PathParam("videoId") String videoId) {

		List<VideoRawDataEntity> vrde = videoRawDataBean.getVideoRawData(videoId);

		return Response.status(Response.Status.OK).entity(vrde).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response uploadVideo(InputStream uploadedInputStream) {

		System.out.println("Called");

		String videoId = UUID.randomUUID().toString();
		String videoLocation = UUID.randomUUID().toString();

		byte[] bytes = new byte[0];
		try (uploadedInputStream) {
			bytes = uploadedInputStream.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(bytes.length);



		String projectId = "webflix-295923";
		String bucketName = "webflix-videos";
		String objectName = "aaa";

		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		storage.create(blobInfo, bytes);

		System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);

		return Response.status(Response.Status.OK).build();
	}

}
